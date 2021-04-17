package io.dfuse.example.graphql.types;

import javax.annotation.Nullable;

public class TransactionTransitionInit extends AbstractTransactionTransition {
	@Nullable
	public BlockHeader blockHeader;

	@Nullable
	public Transaction transaction;

	@Nullable
	public TransactionTrace trace;

	public long confirmations;

	@Nullable
	public String replaceById;

	@Override
	@Nullable
	public BlockHeader getBlockHeader() {
		return this.blockHeader;
	}

	@Override
	@Nullable
	public Transaction getTransaction() {
		return this.transaction;
	}

	@Override
	@Nullable
	public TransactionTrace getTrace() {
		return this.trace;
	}

	@Override
	public long getConfirmations() {
		return this.confirmations;
	}

	@Override
	@Nullable
	public String getReplaceById() {
		return this.replaceById;
	}

	@Override
	public TransactionTransitionType type() {
		return TransactionTransitionType.INIT;
	}
}
