package io.dfuse.example.graphql.types;

public class BlockHeader {
	public String hash;
	public long number;
	public String parentHash;
	public String unclesHash;
	public String coinbase;
	public String stateRoot;
	public String transactionsRoot;
	public String receiptRoot;
	public String logsBloom;
	public long difficulty;
	public long gasLimit;
	public long gasUsed;
	public long timestamp;
	public String extraData;
	public String mixHash;
	public long nonce;
}
