package io.dfuse.example.graphql.types;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Call {
	public long index;
	public long parentIndex;
	public long depth;
	public Type callType;
	public String from;
	public String to;
	public String value;
	public long gasConsumed;
	public long gasLimit;
	public boolean stateReverted;
	public Status status;
	@Nullable
	public String failureCause;
	public Method method;
	public Input input;
	public String returnData;
	public List<StorageChange> storageChanges;
	public List<BalanceChange> balanceChanges;
	public List<Log> logs;

	public static enum Type {
		CALL, CREATE, STATIC, DELEGATE, CALLCODE,
	}

	public static enum Status {
		SUCCEEDED, REVERTED, FAILED
	}

	public static class Method {
		public String textSignature;
		public String hexSignature;
	}
}
