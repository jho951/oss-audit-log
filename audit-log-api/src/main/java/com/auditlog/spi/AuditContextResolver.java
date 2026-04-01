package com.auditlog.spi;

import com.auditlog.api.AuditContext;
import com.auditlog.api.AuditEvent;

/**
 * 감사 이벤트에 주입할 실행 컨텍스트를 해석합니다.
 */
public interface AuditContextResolver {
	AuditContext resolve(AuditEvent event);
}
