package io.dfuse.example.graphql.types;

public class TransactionTransitionPooled extends AbstractTransactionTransition {
	public Transaction transaction;

	@Override
	public Transaction getTransaction() {
		return this.transaction;
	}

	@Override
	public TransactionTransitionType type() {
		return TransactionTransitionType.POOLED;
	}
}