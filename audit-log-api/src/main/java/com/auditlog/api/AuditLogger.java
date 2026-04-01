package com.auditlog.api;

import java.util.Map;

/**
 * 감사 이벤트를 기록하는 진입점 인터페이스입니다.
 */
public interface AuditLogger {
	void log(AuditEvent event);

	default void logSuccess(AuditEvent.Builder builder) {
		log(builder.success().build());
	}

	default void logFailure(AuditEvent.Builder builder, String reason) {
		log(builder.failure(reason).build());
	}

	default void log(
		AuditEventType eventType,
		String action,
		String actorId,
		AuditActorType actorType,
		String actorName,
		String resourceType,
		String resourceId,
		AuditResult result,
		String reason,
		Map<String, ?> details
	) {
		AuditEvent.Builder builder = AuditEvent.builder(eventType, action)
			.actor(actorId, actorType, actorName)
			.resource(resourceType, resourceId)
			.result(result)
			.reason(reason)
			.details(details);
		log(builder.build());
	}
}
