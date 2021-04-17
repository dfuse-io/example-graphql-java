package io.dfuse.example.graphql.types;

public class TransactionTransitionReplaced extends AbstractTransactionTransition {
	public String replaceById;

	@Override
	public String getReplaceById() {
		return this.replaceById;
	}

	@Override
	public TransactionTransitionType type() {
		return TransactionTransitionType.REPLACED;
	}
}