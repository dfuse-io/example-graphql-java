package io.dfuse.example.graphql;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import io.dfuse.example.graphql.types.TransactionLifecycle;
import io.dfuse.example.graphql.types.TransactionState;
import io.dfuse.example.graphql.types.TransactionTransition;
import io.dfuse.graphql.v1.Error;
import io.dfuse.graphql.v1.GraphQLGrpc;
import io.dfuse.graphql.v1.Request;
import io.dfuse.graphql.v1.Response;
import io.grpc.CallCredentials;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class App {
	private final String document;
	private final GraphQLGrpc.GraphQLBlockingStub blockingStub;
	private final CallCredentials callCredentials;

	public App(String document, Channel channel, TokenManager tokenManager) {
		this.document = document;
		this.blockingStub = GraphQLGrpc.newBlockingStub(channel);
		this.callCredentials = new BearerCallCredentials(tokenManager);
	}

	public void execute(String transactionId) {
		logInfo("Tracking transaction %s", transactionId);
		Request request = Request.newBuilder().setQuery(this.document).setVariables(
				Struct.newBuilder().putFields("hash", Value.newBuilder().setStringValue(transactionId).build()).build())
				.build();

		try {
			Iterator<Response> stream = blockingStub.withCallCredentials(this.callCredentials).execute(request);
			for (; stream.hasNext();) {
				Response response = stream.next();
				if (response.getErrorsCount() > 0) {
					logError("An error occurred with the transaction subscription");
					for (Error error : response.getErrorsList()) {
						logError("Error %s", error);
					}

					System.exit(1);
				}

				TransactionLifecycleResponse graphqlResponse = JSON.unmarshal(response.getData(),
						TransactionLifecycleResponse.class);
				TransactionLifecycle lifecycle = graphqlResponse.lifecycle;
				TransactionTransition transition = lifecycle.transition;
				TransactionState state = lifecycle.currentState;

				switch (transition.type()) {
				case INIT:
					// This is the initial transition state received initially, current state can be
					// inspected to understand
					// what is the current status of the transaction.
					if (state == TransactionState.IN_BLOCK && transition.getConfirmations() > 12) {
						logInfo("[INIT] Transaction has been confirmed by %d blocks, tracking completed\n%s",
								transition.getConfirmations(), JSON.marshal(transition));
						System.exit(0);
					} else {
						logInfo("[INIT] Transaction actual state is %s\n%s", state, JSON.marshal(transition));
					}
					break;

				case POOLED:
					// This event occurs when a transaction is now part of the mempool. It
					// could arrive here directly or from after being forked and returning to the
					// pool.
					logInfo("[POOLED] Transaction has been pooled with transition %s", JSON.marshal(transition));
					break;

				case REPLACED:
					// This event occurs when a transaction has been replaced by a new transaction
					// in the mempool. Once this happen, no more events could occur for this
					// transaction and a new tracking should start against the `replacedById` value.
					logInfo("[REPLACED] Transaction has been replaced by %s, tracking abandonned\n%s",
							transition.getReplaceById(), JSON.marshal(transition));
					System.exit(0);

				case SPECULATIVELY_EXECUTED:
					// This even occurs we speculatively execute the transaction after seeing it
					// as pooled. This speculative execution is performed at head block of the chain
					// at time the execution is performed and it not updated further (i.e. there is
					// a single speculative execution performed). The speculative execution gives a
					// full execution trace overview of the transaction, giving deep insights about
					// what would happen if this transaction was included in a block, including all
					// internal calls, balance changes, storage changes, etc.
					logInfo("[SPECULATIVELY_EXECUTED] Transaction has been speculatively executed\n%s",
							JSON.marshal(transition));
					break;

				case MINED:
					// This event occurs when a transaction is not part of a block in the chain. It
					// could arrive here directly without a prior pool event (maybe it was included
					// directly by the miner), from a pooled transaction or from a forked
					// transaction that is now included again in the longest chain.
					logInfo("[MINED] Transaction has been mined\n%s", JSON.marshal(transition));
					break;

				case FORKED:
					// This event occurs when a transaction was in a mined block that is now
					// reverted due to fork event, the transaction is not part of the longest chain
					// anymore, it could be included back later on.
					logInfo("[FORKED] Transaction has been forked\n%s", JSON.marshal(transition));

					break;

				case CONFIRMED:
					// This event occurs each time a new block has been mined on the chain that
					// increased the numbers of confirmations for this transaction.
					boolean isCompleted = transition.getConfirmations() > 12;
					String suffix = isCompleted ? ", tracking completed" : "";
					logInfo("[CONFIRMED] Transaction confirmed by %d blocks" + suffix + "\n%s",
							transition.getConfirmations(), JSON.marshal(transition));

					if (isCompleted) {
						System.exit(0);
					}
					break;
				}

				logInfo("");
			}

			return;
		} catch (StatusRuntimeException e) {
			logError("RPC failed %s", e.getStatus());
		} catch (IOException e) {
			logError("IO error %s", e);
		}
	}

	private static class TransactionLifecycleResponse {
		@JsonProperty("transactionLifecycle")
		public TransactionLifecycle lifecycle;
	}

	public static void main(String[] args) throws Exception {
		String apiKey = System.getenv("DFUSE_API_KEY");
		if (apiKey == null || apiKey.length() <= 0) {
			logError("The DFUSE_API_KEY environment variable must be set to a valid dfuse API key value");
			System.exit(1);
		}

		if (args.length > 0 && (args[0].equals("--help") || args[0].equals("-h"))) {
			usage();
		}

		String transactionId = "df98f1f0c3e962ac829a165e0f54e35a25148d1b1322808b258e49ab66dce697";
		if (args.length > 0) {
			transactionId = args[0];
		}

		ManagedChannel channel = ManagedChannelBuilder.forTarget("mainnet.eth.dfuse.io:443").build();
		String document = IO.readResource("gql/transaction_lifecycle.graphql");

		try {
			new App(document, channel, new TokenManager(apiKey)).execute(transactionId);
		} finally {
			channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
		}
	}

	private static void logInfo(String message, Object... args) {
		System.out.println(String.format(message, args));
	}

	private static void logError(String message, Object... args) {
		System.err.println(String.format(message, args));
	}

	private static void usage() {
		System.out.println("usage: tracker [<transactionHash>]");
		System.out.println("");
		System.out.println("Starts a dfuse Transaction Lifecycle tracker stream that receives");
		System.out.println("various events about the transaction. It tracks it until it's confirmed");
		System.out.println("by 12 blocks or replaced by another transaction completely.");
		System.out.println("");
		System.out.println("You can pass a transaction hash to track, if nothing is passed as an");
		System.out.println("argument, the hash df98f1f0c3e962ac829a165e0f54e35a25148d1b1322808b258e49ab66dce697 is");
		System.out.println("tracked for demo purposes.");
		System.out.println("");
		System.out.println("For a better sample of the events, push a transaction manually and track it");
		System.out.println("by passing its hash.");

		System.exit(1);
	}
}
