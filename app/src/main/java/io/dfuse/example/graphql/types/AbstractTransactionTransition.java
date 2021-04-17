package io.dfuse.example.graphql.types;

import javax.annotation.Nullable;

public abstract class AbstractTransactionTransition implements TransactionTransition {

	@Override
	@Nullable
	public BlockHeader getBlockHeader() {
		return null;
	}

	@Override
	@Nullable
	public Transaction getTransaction() {
		return null;
	}

	@Override
	@Nullable
	public TransactionTrace getTrace() {
		return null;
	}

	@Override
	public long getConfirmations() {
		return 0;
	}

	@Override
	@Nullable
	public String getReplaceById() {
		return null;
	}

}
