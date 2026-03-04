package com.auditlog.core;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditLogger;
import com.auditlog.api.AuditSink;

/**
 * {@link AuditLogger}의 기본 구현체입니다.
 * 전달받은 이벤트를 지정된 {@link AuditSink}로 위임합니다.
 */
public final class DefaultAuditLogger implements AuditLogger {
	private final AuditSink sink;

	/**
	 * @param sink 이벤트를 기록할 sink
	 */
	public DefaultAuditLogger(AuditSink sink) {
		this.sink = sink;
	}

	/**
	 * 이벤트 기록 요청을 sink로 전달합니다.
	 *
	 * @param event 기록할 감사 이벤트
	 */
	@Override
	public void log(AuditEvent event) {
		sink.append(event);
	}
}
