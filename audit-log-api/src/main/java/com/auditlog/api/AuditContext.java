package com.auditlog.api;

/** 로깅 시점에 자동으로 주입할 요청/추적 메타데이터입니다. */
public record AuditContext(
	String traceId,
	String requestId,
	String clientIp,
	String userAgent
) {
	public static final AuditContext EMPTY = new AuditContext(null, null, null, null);

	public static AuditContext empty() {
		return EMPTY;
	}
}
