package io.dfuse.example.graphql.types;

public class TransactionTransitionForked extends AbstractTransactionTransition {
	public Transaction transaction;

	@Override
	public Transaction getTransaction() {
		return this.transaction;
	}

	@Override
	public TransactionTransitionType type() {
		return TransactionTransitionType.FORKED;
	}
}