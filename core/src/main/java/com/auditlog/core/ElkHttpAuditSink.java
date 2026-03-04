package com.auditlog.core;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditSink;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * 감사 이벤트를 HTTP로 전송하는 sink 구현체입니다.
 */
public final class ElkHttpAuditSink implements AuditSink {
	private final HttpClient client;
	private final URI endpoint;
	private final String serviceName;
	private final String env;
	private final String apiKey; // optional

	/**
	 * @param endpoint ELK/수집 엔드포인트
	 * @param serviceName 서비스 이름(없으면 {@code unknown-service})
	 * @param env 실행 환경(없으면 {@code local})
	 * @param apiKey 인증 API Key(선택)
	 */
	public ElkHttpAuditSink(URI endpoint, String serviceName, String env, String apiKey) {
		this.client = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(2))
			.build();
		this.endpoint = endpoint;
		this.serviceName = serviceName == null ? "unknown-service" : serviceName;
		this.env = env == null ? "local" : env;
		this.apiKey = apiKey; // Elastic API Key 쓰면 "ApiKey xxx"
	}

	/**
	 * 이벤트를 비동기 HTTP 요청으로 전송합니다.
	 * 전송 실패는 fail-open 정책에 따라 예외를 외부로 전파하지 않습니다.
	 *
	 * @param event 전송할 감사 이벤트
	 */
	@Override
	public void append(AuditEvent event) {
		try {
			String body = Json.toJsonLine(event, serviceName, env); // 1줄 JSON
			HttpRequest.Builder b = HttpRequest.newBuilder(endpoint)
				.timeout(Duration.ofSeconds(2))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body));

			if (apiKey != null && !apiKey.isBlank()) {
				b.header("Authorization", apiKey.startsWith("ApiKey ") ? apiKey : ("ApiKey " + apiKey));
			}

			client.sendAsync(b.build(), HttpResponse.BodyHandlers.discarding());
		} catch (Exception ignored) {
			// v1: fail-open
		}
	}
}
