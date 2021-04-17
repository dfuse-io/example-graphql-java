package io.dfuse.example.graphql;

import java.util.concurrent.Executor;

import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;

public class BearerCallCredentials extends CallCredentials {

	private static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization",
			Metadata.ASCII_STRING_MARSHALLER);

	private TokenManager tokenManager;

	public BearerCallCredentials(TokenManager manager) {
		this.tokenManager = manager;
	}

	@Override
	public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
		appExecutor.execute(() -> {
			String token;
			try {
				token = this.tokenManager.obtainToken();
			} catch (Throwable exception) {
				applier.fail(Status.fromThrowable(exception));
				return;
			}

			Metadata headers = new Metadata();
			headers.put(AUTHORIZATION_METADATA_KEY, "Bearer " + token);
			applier.apply(headers);
		});
	}

	@Override
	public void thisUsesUnstableApi() {
		// No-op
	}

}
