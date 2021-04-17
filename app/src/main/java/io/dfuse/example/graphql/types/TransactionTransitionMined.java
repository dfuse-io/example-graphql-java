package io.dfuse.example.graphql.types;

public class TransactionTransitionMined extends AbstractTransactionTransition {
	public BlockHeader blockHeader;

	public TransactionTrace trace;

	public long confirmations;

	@Override
	public BlockHeader getBlockHeader() {
		return this.blockHeader;
	}

	@Override
	public TransactionTrace getTrace() {
		return this.trace;
	}

	@Override
	public long getConfirmations() {
		return this.confirmations;
	}

	@Override
	public TransactionTransitionType type() {
		return TransactionTransitionType.MINED;
	}
}