package com.auditlog.core;

import com.auditlog.api.AuditContext;
import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditLogger;
import com.auditlog.api.AuditSink;
import com.auditlog.spi.AuditContextResolver;
import com.auditlog.spi.AuditMaskingPolicy;

import java.util.List;

/**
 * {@link AuditLogger}의 기본 구현체입니다.
 * 이벤트에 컨텍스트와 마스킹 정책을 적용한 뒤 sink로 전달합니다.
 */
public final class DefaultAuditLogger implements AuditLogger {
	private final AuditSink sink;
	private final List<AuditContextResolver> contextResolvers;
	private final AuditMaskingPolicy maskingPolicy;

	public DefaultAuditLogger(
		AuditSink sink,
		List<AuditContextResolver> contextResolvers,
		AuditMaskingPolicy maskingPolicy
	) {
		this.sink = sink;
		this.contextResolvers = contextResolvers == null ? List.of() : List.copyOf(contextResolvers);
		this.maskingPolicy = maskingPolicy;
	}

	@Override
	public void log(AuditEvent event) {
		AuditEvent enriched = applyContext(event);
		AuditEvent masked = applyMasking(enriched);
		sink.write(masked);
	}

	private AuditEvent applyContext(AuditEvent event) {
		String traceId = event.getTraceId();
		String requestId = event.getRequestId();
		String clientIp = event.getClientIp();
		String userAgent = event.getUserAgent();

		for (AuditContextResolver resolver : contextResolvers) {
			AuditContext context = resolver.resolve(event);
			if (context == null) {
				continue;
			}
			if (isBlank(traceId) && !isBlank(context.traceId())) {
				traceId = context.traceId();
			}
			if (isBlank(requestId) && !isBlank(context.requestId())) {
				requestId = context.requestId();
			}
			if (isBlank(clientIp) && !isBlank(context.clientIp())) {
				clientIp = context.clientIp();
			}
			if (isBlank(userAgent) && !isBlank(context.userAgent())) {
				userAgent = context.userAgent();
			}
		}

		if (same(event.getTraceId(), traceId)
			&& same(event.getRequestId(), requestId)
			&& same(event.getClientIp(), clientIp)
			&& same(event.getUserAgent(), userAgent)) {
			return event;
		}

		return event.toBuilder()
			.traceId(traceId)
			.requestId(requestId)
			.clientIp(clientIp)
			.userAgent(userAgent)
			.build();
	}

	private AuditEvent applyMasking(AuditEvent event) {
		if (maskingPolicy == null) {
			return event;
		}
		return event.toBuilder()
			.details(maskingPolicy.mask(event.getDetails()))
			.build();
	}

	private static boolean isBlank(String value) {
		return value == null || value.isBlank();
	}

	private static boolean same(String left, String right) {
		return left == null ? right == null : left.equals(right);
	}
}
