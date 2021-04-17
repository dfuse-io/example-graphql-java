package io.dfuse.example.graphql.types;

public class TransactionTransitionSpeculativelyExecuted extends AbstractTransactionTransition {
	public BlockHeader blockHeader;

	public Transaction transaction;

	public TransactionTrace trace;

	@Override
	public BlockHeader getBlockHeader() {
		return this.blockHeader;
	}

	@Override
	public Transaction getTransaction() {
		return this.transaction;
	}

	@Override
	public TransactionTrace getTrace() {
		return this.trace;
	}

	@Override
	public TransactionTransitionType type() {
		return TransactionTransitionType.SPECULATIVELY_EXECUTED;
	}
}