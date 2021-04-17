package io.dfuse.example.graphql;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JSON {
	private static final ObjectMapper mapper = createMapper();

	public static <T> String marshal(T input) throws JsonProcessingException {
		return mapper.writeValueAsString(input);
	}

	public static <T> String mustMarshal(T input) {
		try {
			return marshal(input);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.exit(1);

			// Impossible to reach
			return "";
		}
	}

	public static <T> T unmarshal(String input, Class<T> clazz) throws IOException {
		return mapper.readValue(input, clazz);
	}

	public static <T> T unmarshal(InputStream input, Class<T> clazz) throws IOException {
		return mapper.readValue(input, clazz);
	}

	private static ObjectMapper createMapper() {
		ObjectMapper mapper = new ObjectMapper();

		mapper.registerModule(new JavaTimeModule());
		mapper.setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY));

		return mapper;
	}
}
