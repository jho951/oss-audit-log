package com.auditlog.autoconfigure;

import com.auditlog.api.AuditContext;
import com.auditlog.api.AuditEvent;
import com.auditlog.spi.AuditContextResolver;

final class WebAuditContextResolver implements AuditContextResolver {
	@Override
	public AuditContext resolve(AuditEvent event) {
		AuditContext context = WebAuditContextHolder.get();
		return context != null ? context : AuditContext.empty();
	}
}
