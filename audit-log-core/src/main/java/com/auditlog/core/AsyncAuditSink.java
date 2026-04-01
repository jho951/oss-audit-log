package com.auditlog.core;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditSink;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * sink 기록을 별도 executor에서 비동기로 처리합니다.
 */
public final class AsyncAuditSink implements AuditSink {
	private final AuditSink delegate;
	private final ExecutorService executorService;

	public AsyncAuditSink(AuditSink delegate, ExecutorService executorService) {
		this.delegate = delegate;
		this.executorService = executorService;
	}

	@Override
	public void write(AuditEvent event) {
		try {
			executorService.execute(() -> delegate.write(event));
		} catch (RuntimeException ignored) {
			// fail-open
		}
	}

	@Override
	public void close() {
		try {
			executorService.shutdown();
			executorService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException interruptedException) {
			Thread.currentThread().interrupt();
		} finally {
			delegate.close();
		}
	}
}
