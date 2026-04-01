package com.auditlog.core;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 중첩된 details 맵을 순회하며 민감정보를 마스킹합니다.
 */
public final class AuditMasker {
	private static final String MASKED = "****";

	private AuditMasker() {}

	public static Map<String, Object> mask(Map<String, Object> source, Set<String> sensitiveKeys) {
		Map<String, Object> masked = new LinkedHashMap<>();
		if (source == null || source.isEmpty()) {
			return masked;
		}
		for (Map.Entry<String, Object> entry : source.entrySet()) {
			masked.put(entry.getKey(), maskValue(entry.getKey(), entry.getValue(), sensitiveKeys));
		}
		return masked;
	}

	private static Object maskValue(String key, Object value, Set<String> sensitiveKeys) {
		if (value == null) {
			return null;
		}
		if (isSensitive(key, sensitiveKeys)) {
			return MASKED;
		}
		if (value instanceof Map<?, ?> mapValue) {
			Map<String, Object> nested = new LinkedHashMap<>();
			for (Map.Entry<?, ?> entry : mapValue.entrySet()) {
				if (entry.getKey() != null) {
					nested.put(String.valueOf(entry.getKey()), maskValue(String.valueOf(entry.getKey()), entry.getValue(), sensitiveKeys));
				}
			}
			return nested;
		}
		if (value instanceof Iterable<?> iterable) {
			List<Object> items = new ArrayList<>();
			for (Object item : iterable) {
				items.add(maskValue(key, item, sensitiveKeys));
			}
			return items;
		}
		if (value.getClass().isArray()) {
			int length = Array.getLength(value);
			List<Object> items = new ArrayList<>(length);
			for (int index = 0; index < length; index++) {
				items.add(maskValue(key, Array.get(value, index), sensitiveKeys));
			}
			return items;
		}
		return value;
	}

	private static boolean isSensitive(String key, Set<String> sensitiveKeys) {
		if (key == null || sensitiveKeys == null || sensitiveKeys.isEmpty()) {
			return false;
		}
		String normalized = key.trim().toLowerCase();
		for (String sensitiveKey : sensitiveKeys) {
			if (sensitiveKey != null && normalized.contains(sensitiveKey.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
