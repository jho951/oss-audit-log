package com.auditlog.core;

import com.auditlog.api.AuditEvent;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;

final class Json {
	private Json() {}

	static String toJsonLine(AuditEvent event, String serviceName, String env) {
		StringBuilder builder = new StringBuilder(512);
		builder.append("{");

		kv(builder, "eventId", event.getEventId());
		comma(builder);
		kv(builder, "@timestamp", event.getOccurredAt() != null ? event.getOccurredAt().toString() : null);
		comma(builder);
		kv(builder, "service", serviceName);
		comma(builder);
		kv(builder, "env", env);
		comma(builder);
		kv(builder, "eventType", event.getEventType() != null ? event.getEventType().name() : null);
		comma(builder);
		kv(builder, "action", event.getAction());
		comma(builder);
		kv(builder, "actorId", event.getActorId());
		comma(builder);
		kv(builder, "actorType", event.getActorType() != null ? event.getActorType().name() : null);
		comma(builder);
		kv(builder, "actorName", event.getActorName());
		comma(builder);
		kv(builder, "resourceType", event.getResourceType());
		comma(builder);
		kv(builder, "resourceId", event.getResourceId());
		comma(builder);
		kv(builder, "result", event.getResult() != null ? event.getResult().name() : null);
		comma(builder);
		kv(builder, "reason", event.getReason());
		comma(builder);
		kv(builder, "traceId", event.getTraceId());
		comma(builder);
		kv(builder, "requestId", event.getRequestId());
		comma(builder);
		kv(builder, "clientIp", event.getClientIp());
		comma(builder);
		kv(builder, "userAgent", event.getUserAgent());

		if (!event.getDetails().isEmpty()) {
			comma(builder);
			builder.append("\"details\":").append(toJsonValue(event.getDetails()));
		}

		builder.append("}\n");
		return builder.toString();
	}

	private static void comma(StringBuilder builder) {
		builder.append(",");
	}

	private static void kv(StringBuilder builder, String key, String value) {
		builder.append("\"").append(escape(key)).append("\":");
		if (value == null) {
			builder.append("null");
		} else {
			builder.append("\"").append(escape(value)).append("\"");
		}
	}

	@SuppressWarnings("unchecked")
	private static String toJsonValue(Object value) {
		if (value == null) {
			return "null";
		}
		if (value instanceof String stringValue) {
			return "\"" + escape(stringValue) + "\"";
		}
		if (value instanceof Number || value instanceof Boolean) {
			return value.toString();
		}
		if (value instanceof Map<?, ?> mapValue) {
			StringBuilder builder = new StringBuilder("{");
			Iterator<? extends Map.Entry<?, ?>> iterator = mapValue.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<?, ?> entry = iterator.next();
				builder.append("\"").append(escape(String.valueOf(entry.getKey()))).append("\":");
				builder.append(toJsonValue(entry.getValue()));
				if (iterator.hasNext()) {
					builder.append(",");
				}
			}
			builder.append("}");
			return builder.toString();
		}
		if (value instanceof Iterable<?> iterable) {
			StringBuilder builder = new StringBuilder("[");
			Iterator<?> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				builder.append(toJsonValue(iterator.next()));
				if (iterator.hasNext()) {
					builder.append(",");
				}
			}
			builder.append("]");
			return builder.toString();
		}
		if (value.getClass().isArray()) {
			StringBuilder builder = new StringBuilder("[");
			int length = Array.getLength(value);
			for (int index = 0; index < length; index++) {
				builder.append(toJsonValue(Array.get(value, index)));
				if (index < length - 1) {
					builder.append(",");
				}
			}
			builder.append("]");
			return builder.toString();
		}
		return "\"" + escape(String.valueOf(value)) + "\"";
	}

	private static String escape(String value) {
		if (value == null) {
			return null;
		}
		return value
			.replace("\\", "\\\\")
			.replace("\"", "\\\"")
			.replace("\n", "\\n")
			.replace("\r", "\\r")
			.replace("\t", "\\t");
	}
}
