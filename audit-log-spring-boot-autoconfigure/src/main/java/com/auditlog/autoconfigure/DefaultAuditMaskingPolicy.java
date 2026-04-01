package com.auditlog.autoconfigure;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.auditlog.core.AuditMasker;
import com.auditlog.spi.AuditMaskingPolicy;

final class DefaultAuditMaskingPolicy implements AuditMaskingPolicy {
	private final Set<String> sensitiveKeys;

	DefaultAuditMaskingPolicy(List<String> sensitiveKeys) {
		this.sensitiveKeys = new LinkedHashSet<>(sensitiveKeys == null ? List.of() : sensitiveKeys);
	}

	@Override
	public Map<String, Object> mask(Map<String, Object> details) {
		return AuditMasker.mask(details, sensitiveKeys);
	}
}
