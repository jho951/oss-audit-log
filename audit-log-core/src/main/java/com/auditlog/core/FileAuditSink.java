package com.auditlog.core;

import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditSink;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

	public FileAuditSink(Path filePath, String serviceName, String env) {
		this.filePath = Objects.requireNonNull(filePath);
		this.serviceName = serviceName == null ? "unknown-service" : serviceName;
		this.env = env == null ? "local" : env;
		ensureParent();
	}

	@Override
	public void write(AuditEvent event) {
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
		} catch (IOException ignored) {
			// fail-open
		} finally {
			lock.unlock();
		}
	}

	private void ensureParent() {
		try {
			Path parent = filePath.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
		} catch (IOException ignored) {
			// fail-open
		}
	}
}
