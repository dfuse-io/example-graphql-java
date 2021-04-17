package io.dfuse.example.graphql.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionLifecycle {
	public TransactionState currentState;
	public TransactionTransitionType transitionName;

	@JsonTypeInfo(use = Id.NAME, property = "__typename", include = As.PROPERTY)
	@JsonSubTypes(value = { @JsonSubTypes.Type(value = TransactionTransitionInit.class, name = "TrxTransitionInit"),
			@JsonSubTypes.Type(value = TransactionTransitionPooled.class, name = "TrxTransitionPooled"),
			@JsonSubTypes.Type(value = TransactionTransitionSpeculativelyExecuted.class, name = "TrxTransitionSpeculativelyExecuted"),
			@JsonSubTypes.Type(value = TransactionTransitionReplaced.class, name = "TrxTransitionReplaced"),
			@JsonSubTypes.Type(value = TransactionTransitionMined.class, name = "TrxTransitionMined"),
			@JsonSubTypes.Type(value = TransactionTransitionForked.class, name = "TrxTransitionForked"),
			@JsonSubTypes.Type(value = TransactionTransitionConfirmed.class, name = "TrxTransitionConfirmed"), })
	public TransactionTransition transition;
}
