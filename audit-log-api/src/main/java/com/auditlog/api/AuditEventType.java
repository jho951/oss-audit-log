package com.auditlog.api;

/**
 * 감사 이벤트 유형.
 */
public enum AuditEventType {
	/** 로그인 이벤트 */
	LOGIN,
	/** 로그아웃 이벤트 */
	LOGOUT,
	/** 조회 이벤트 */
	READ,
	/** 생성 이벤트 */
	CREATE,
	/** 수정 이벤트 */
	UPDATE,
	/** 삭제 이벤트 */
	DELETE,
	/** 시스템 내부 이벤트 */
	SYSTEM,
	/** 사용자 정의 이벤트 */
	CUSTOM
}
