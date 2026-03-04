package com.auditlog.api;

import java.util.Map;

/**
 * 감사 이벤트를 기록하는 진입점 인터페이스입니다.
 */
public interface AuditLogger {
	/**
	 * 이미 구성된 {@link AuditEvent}를 기록합니다.
	 *
	 * @param event 기록할 감사 이벤트
	 */
	void log(AuditEvent event);

	/**
	 * 개별 필드로 전달받은 값을 기반으로 {@link AuditEvent}를 생성해 기록합니다.
	 *
	 * @param eventType 이벤트 유형
	 * @param action 이벤트 액션명
	 * @param actorId 행위자 식별자
	 * @param actorIp 행위자 IP
	 * @param resourceType 대상 리소스 유형
	 * @param resourceId 대상 리소스 식별자
	 * @param result 처리 결과
	 * @param traceId 추적 ID
	 * @param details 부가 정보
	 */
	default void log(
		AuditEventType eventType,
		String action,
		String actorId,
		String actorIp,
		String resourceType,
		String resourceId,
		String result,
		String traceId,
		Map<String, String> details
	) {
		AuditEvent.Builder b = AuditEvent.builder(eventType, action)
			.actorId(actorId)
			.actorIp(actorIp)
			.resource(resourceType, resourceId)
			.result(result)
			.traceId(traceId);

		if (details != null) {
			details.forEach(b::detail);
		}

		log(b.build());
	}
}
