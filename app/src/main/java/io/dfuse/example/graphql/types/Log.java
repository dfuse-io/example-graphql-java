package io.dfuse.example.graphql.types;

import java.util.List;

public class Log {
	public String address;
	public List<String> topics;
	public String data;
	public long blockIndex;
	public long transactionIndex;
}
