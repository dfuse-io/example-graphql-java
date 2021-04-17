package io.dfuse.example.graphql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import com.google.common.io.Closeables;

public final class IO {
	public static String readResource(String name) throws IOException {
		ClassLoader classLoader = IO.class.getClassLoader();

		InputStream inputStream = null;
		try {
			inputStream = classLoader.getResourceAsStream(name);

			return toString(inputStream);
		} finally {
			Closeables.close(inputStream, true);
		}
	}

	public static String toString(InputStream stream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

		return reader.lines().collect(Collectors.joining(System.lineSeparator()));
	}
}
