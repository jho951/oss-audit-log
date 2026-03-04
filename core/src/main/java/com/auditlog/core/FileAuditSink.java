package com.auditlog.core;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditSink;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 감사 이벤트를 로컬 파일에 JSON Line 형식으로 append하는 구현체입니다.
 */
public final class FileAuditSink implements AuditSink {
	private final Path filePath;
	private final String serviceName;
	private final String env;
	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * @param filePath 로그 파일 경로
	 * @param serviceName 서비스 이름(없으면 {@code unknown-service})
	 * @param env 실행 환경(없으면 {@code local})
	 */
	public FileAuditSink(Path filePath, String serviceName, String env) {
		this.filePath = Objects.requireNonNull(filePath);
		this.serviceName = serviceName == null ? "unknown-service" : serviceName;
		this.env = env == null ? "local" : env;
		ensureParent();
	}

	/**
	 * 이벤트를 파일에 한 줄(JSON + 개행)로 기록합니다.
	 * 기록 실패는 fail-open 정책에 따라 예외를 외부로 전파하지 않습니다.
	 *
	 * @param event 기록할 감사 이벤트
	 */
	@Override
	public void append(AuditEvent event) {
		String line = Json.toJsonLine(event, serviceName, env);
		lock.lock();
		try {
			Files.writeString(
				filePath,
				line,
				StandardOpenOption.CREATE,
				StandardOpenOption.WRITE,
				StandardOpenOption.APPEND
			);
		} catch (IOException ex) {
			// 감사 로그는 "업무 실패"로 번지지 않게 기본은 삼킴(원하면 전략화 가능)
		} finally {
			lock.unlock();
		}
	}

	private void ensureParent() {
		try {
			Path parent = filePath.getParent();
			if (parent != null) Files.createDirectories(parent);
		} catch (IOException ignored) {}
	}
}
