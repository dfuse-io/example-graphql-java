package io.dfuse.example.graphql;

import java.io.IOException;

public class TokenException extends IOException {
	private static final long serialVersionUID = 8239912382490015209L;

	public TokenException(int code, String body) {
		this(code, body, null);
	}

	public TokenException(int code, String body, Throwable cause) {
		super(String.format("HTTP request to obtain token failed with status %d and body %s", code, body), cause);
	}
}
