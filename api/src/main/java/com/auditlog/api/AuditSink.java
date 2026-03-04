package com.auditlog.api;

/**
 * 감사 이벤트를 실제 저장소(파일, HTTP 등)에 기록하는 대상 인터페이스입니다.
 */
public interface AuditSink extends AutoCloseable {
	/**
	 * 단일 감사 이벤트를 저장소에 추가합니다.
	 *
	 * @param event 기록할 감사 이벤트
	 */
	void append(AuditEvent event);

	/**
	 * 자원 정리가 필요한 구현체에서 재정의할 수 있습니다.
	 */
	@Override
	default void close() { /* no-op */ }
}
