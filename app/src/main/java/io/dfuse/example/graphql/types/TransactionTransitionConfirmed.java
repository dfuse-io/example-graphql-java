package io.dfuse.example.graphql.types;

public class TransactionTransitionConfirmed extends AbstractTransactionTransition {
	public long confirmations;

	@Override
	public long getConfirmations() {
		return this.confirmations;
	}

	@Override
	public TransactionTransitionType type() {
		return TransactionTransitionType.CONFIRMED;
	}
}