package io.dfuse.example.graphql.types;

public enum TransactionTransitionType {
	INIT, SPECULATIVELY_EXECUTED, POOLED, MINED, FORKED, CONFIRMED, REPLACED;
}