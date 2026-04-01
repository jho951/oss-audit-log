package com.auditlog.api;

/** 감사 이벤트의 주체 유형입니다. */
public enum AuditActorType {
	USER,
	ADMIN,
	SYSTEM,
	SERVICE,
	ANONYMOUS,
	UNKNOWN
}
