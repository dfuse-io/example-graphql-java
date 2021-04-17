package io.dfuse.example.graphql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.io.Closeables;

// TokenManager manages dfuse API token which are short-lived JWT token obtain
// from the dfuse API key. The token are good for 24h and should be cached to avoid
// being rate limited when trying to obtain a new token. Indeed, the `/v1/auth/issue`
// HTTP request is heavy rate limited at 6 req/s, so be sure to correctly cache the
// token.
public class TokenManager {
	private String apiKey;
	private String activeToken;
	private Instant expiresAt;

	private ReadWriteLock lock;

	public TokenManager(String apiKey) {
		this.apiKey = apiKey;
		this.activeToken = "";
		this.expiresAt = Instant.now();

		this.lock = new ReentrantReadWriteLock();

		// TODO Cache token to disk
		// The token should be written to disk once obtained and reloaded
		// from disk when the TokenManager starts. This is to ensure that if
		// there is a bug in your code that make your process restart in a tight
		// loop, you will not be hit by the rate limiter.
	}

	public String obtainToken() throws IOException {
		lock.readLock().lock();
		try {
			if (this.activeToken.length() > 0 && this.expiresAt.minus(Duration.ofMinutes(5)).isAfter(Instant.now())) {
				return this.activeToken;
			}
		} finally {
			lock.readLock().unlock();
		}

		lock.writeLock().lock();
		try {
			this.refreshActiveToken();

			return this.activeToken;
		} finally {
			lock.writeLock().unlock();
		}
	}

	private void refreshActiveToken() throws IOException {
		Response response = Request.post("https://auth.dfuse.io/v1/auth/issue")
				.bodyString(JSON.mustMarshal(new IssueTokenRequest(this.apiKey)), ContentType.APPLICATION_JSON)
				.execute();

		IssueTokenResponse tokenResponse = response.handleResponse(new HttpClientResponseHandler<IssueTokenResponse>() {
			@Override
			public IssueTokenResponse handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
				InputStream content = null;
				try {
					content = response.getEntity().getContent();
					if (response.getCode() != 200) {
						throw new TokenException(response.getCode(), inputStreamToString(content));
					}

					return JSON.unmarshal(content, IssueTokenResponse.class);
				} finally {
					Closeables.close(content, true);
				}
			}
		});

		this.activeToken = tokenResponse.token;
		this.expiresAt = tokenResponse.expiresAt;
	}

	private String inputStreamToString(InputStream stream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

		return reader.lines().collect(Collectors.joining("\n"));
	}

	private static class IssueTokenRequest {
		@JsonProperty("api_key")
		public String apiKey;

		public IssueTokenRequest(String apiKey) {
			this.apiKey = apiKey;
		}
	}

	private static class IssueTokenResponse {
		@JsonProperty("token")
		public String token;

		@JsonProperty("expires_at")
		public Instant expiresAt;

	}
}
