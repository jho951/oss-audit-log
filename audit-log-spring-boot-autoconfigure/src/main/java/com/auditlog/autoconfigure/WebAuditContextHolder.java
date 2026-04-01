package com.auditlog.autoconfigure;

import com.auditlog.api.AuditContext;

final class WebAuditContextHolder {
	private static final ThreadLocal<AuditContext> HOLDER = new ThreadLocal<>();

	private WebAuditContextHolder() {}

	static void set(AuditContext auditContext) {
		HOLDER.set(auditContext);
	}

	static AuditContext get() {
		return HOLDER.get();
	}

	static void clear() {
		HOLDER.remove();
	}
}
