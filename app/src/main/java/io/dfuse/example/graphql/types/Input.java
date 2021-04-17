package io.dfuse.example.graphql.types;

import java.util.List;

import javax.annotation.Nullable;

public class Input {
	public String data;

	public List<Field> json;

	public static class Field {
		public String type;

		@Nullable
		public String name;

		public String value;
	}
}