package io.dfuse.example.graphql.types;

import javax.annotation.Nullable;

public interface TransactionTransition {
	TransactionTransitionType type();

	@Nullable
	BlockHeader getBlockHeader();

	@Nullable
	Transaction getTransaction();

	@Nullable
	TransactionTrace getTrace();

	long getConfirmations();

	@Nullable
	String getReplaceById();
}
