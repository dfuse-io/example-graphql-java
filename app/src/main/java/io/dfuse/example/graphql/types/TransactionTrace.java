package io.dfuse.example.graphql.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionTrace {
	public String hash;
	public String to;
	public String from;
	public Status status;

	@JsonProperty("flatCalls")
	public List<Call> calls;

	public static enum Status {
		SUCCEEDED, REVERTED, FAILED
	}
}
