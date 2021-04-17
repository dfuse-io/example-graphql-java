package io.dfuse.example.graphql.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
	public String hash;
	public String to;
	public String from;
	public Input input;
}
