package com.auditlog.api;

/**
 * 감사 이벤트를 실제 저장소(파일, HTTP 등)에 기록하는 대상 인터페이스입니다.
 */
public interface AuditSink extends AutoCloseable {
	void write(AuditEvent event);

	default void append(AuditEvent event) {
		write(event);
	}

	@Override
	default void close() { /* no-op */ }
}
