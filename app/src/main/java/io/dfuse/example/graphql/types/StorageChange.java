package io.dfuse.example.graphql.types;

import javax.annotation.Nullable;

public class StorageChange {
	public String key;

	public String address;

	@Nullable
	public String oldValue;

	@Nullable
	public String newValue;
}
