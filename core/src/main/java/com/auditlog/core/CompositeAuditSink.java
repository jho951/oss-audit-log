package com.auditlog.core;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditSink;

import java.util.List;

/**
 * 여러 {@link AuditSink}로 이벤트를 fan-out 전송하는 합성 sink입니다.
 */
public final class CompositeAuditSink implements AuditSink {
	private final List<AuditSink> sinks;

	/**
	 * @param sinks fan-out 대상 sink 목록
	 */
	public CompositeAuditSink(List<AuditSink> sinks) {
		this.sinks = List.copyOf(sinks);
	}

	/**
	 * 각 sink에 순차적으로 이벤트를 전달합니다.
	 * 특정 sink 실패는 다른 sink 처리에 영향을 주지 않도록 예외를 흡수합니다.
	 *
	 * @param event 기록할 감사 이벤트
	 */
	@Override
	public void append(AuditEvent event) {
		for (AuditSink s : sinks) {
			try { s.append(event); } catch (Exception ignored) {}
		}
	}

	/**
	 * 포함된 모든 sink를 순차적으로 종료합니다.
	 */
	@Override
	public void close() {
		for (AuditSink s : sinks) {
			try { s.close(); } catch (Exception ignored) {}
		}
	}
}
