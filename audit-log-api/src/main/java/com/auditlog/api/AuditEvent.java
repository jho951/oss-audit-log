package com.auditlog.api;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 단일 감사 이벤트를 표현하는 불변 객체입니다.
 */
public final class AuditEvent {

	private final String eventId;
	private final Instant occurredAt;
	private final String actorId;
	private final AuditActorType actorType;
	private final String actorName;
	private final AuditEventType eventType;
	private final String action;
	private final String resourceType;
	private final String resourceId;
	private final AuditResult result;
	private final String reason;
	private final String traceId;
	private final String requestId;
	private final String clientIp;
	private final String userAgent;
	private final Map<String, Object> details;

	private AuditEvent(Builder builder) {
		this.eventId = builder.eventId != null ? builder.eventId : UUID.randomUUID().toString();
		this.occurredAt = builder.occurredAt != null ? builder.occurredAt : Instant.now();
		this.actorId = builder.actorId;
		this.actorType = builder.actorType != null ? builder.actorType : AuditActorType.UNKNOWN;
		this.actorName = builder.actorName;
		this.eventType = builder.eventType != null ? builder.eventType : AuditEventType.CUSTOM;
		this.action = builder.action;
		this.resourceType = builder.resourceType;
		this.resourceId = builder.resourceId;
		this.result = builder.result != null ? builder.result : AuditResult.SUCCESS;
		this.reason = builder.reason;
		this.traceId = builder.traceId;
		this.requestId = builder.requestId;
		this.clientIp = builder.clientIp;
		this.userAgent = builder.userAgent;
		this.details = Collections.unmodifiableMap(new LinkedHashMap<>(builder.details));
	}

	public String getEventId() {
		return eventId;
	}

	public Instant getOccurredAt() {
		return occurredAt;
	}

	public String getActorId() {
		return actorId;
	}

	public AuditActorType getActorType() {
		return actorType;
	}

	public String getActorName() {
		return actorName;
	}

	public AuditEventType getEventType() {
		return eventType;
	}

	public String getAction() {
		return action;
	}

	public String getResourceType() {
		return resourceType;
	}

	public String getResourceId() {
		return resourceId;
	}

	public AuditResult getResult() {
		return result;
	}

	public String getReason() {
		return reason;
	}

	public String getTraceId() {
		return traceId;
	}

	public String getRequestId() {
		return requestId;
	}

	public String getClientIp() {
		return clientIp;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public Builder toBuilder() {
		return new Builder(eventType, action)
			.eventId(eventId)
			.occurredAt(occurredAt)
			.actorId(actorId)
			.actorType(actorType)
			.actorName(actorName)
			.resource(resourceType, resourceId)
			.result(result)
			.reason(reason)
			.traceId(traceId)
			.requestId(requestId)
			.clientIp(clientIp)
			.userAgent(userAgent)
			.details(details);
	}

	public static Builder builder(AuditEventType eventType, String action) {
		return new Builder(eventType, action);
	}

	public static final class Builder {
		private String eventId;
		private Instant occurredAt;
		private String actorId;
		private AuditActorType actorType;
		private String actorName;
		private final AuditEventType eventType;
		private final String action;
		private String resourceType;
		private String resourceId;
		private AuditResult result;
		private String reason;
		private String traceId;
		private String requestId;
		private String clientIp;
		private String userAgent;
		private final Map<String, Object> details = new LinkedHashMap<>();

		public Builder(AuditEventType eventType, String action) {
			this.eventType = eventType;
			this.action = action;
		}

		public Builder eventId(String eventId) {
			this.eventId = eventId;
			return this;
		}

		public Builder occurredAt(Instant occurredAt) {
			this.occurredAt = occurredAt;
			return this;
		}

		public Builder actorId(String actorId) {
			this.actorId = actorId;
			return this;
		}

		public Builder actorType(AuditActorType actorType) {
			this.actorType = actorType;
			return this;
		}

		public Builder actorName(String actorName) {
			this.actorName = actorName;
			return this;
		}

		public Builder actor(String actorId, AuditActorType actorType, String actorName) {
			this.actorId = actorId;
			this.actorType = actorType;
			this.actorName = actorName;
			return this;
		}

		public Builder resource(String resourceType, String resourceId) {
			this.resourceType = resourceType;
			this.resourceId = resourceId;
			return this;
		}

		public Builder result(AuditResult result) {
			this.result = result;
			return this;
		}

		public Builder success() {
			this.result = AuditResult.SUCCESS;
			this.reason = null;
			return this;
		}

		public Builder failure(String reason) {
			this.result = AuditResult.FAILURE;
			this.reason = reason;
			return this;
		}

		public Builder reason(String reason) {
			this.reason = reason;
			return this;
		}

		public Builder traceId(String traceId) {
			this.traceId = traceId;
			return this;
		}

		public Builder requestId(String requestId) {
			this.requestId = requestId;
			return this;
		}

		public Builder clientIp(String clientIp) {
			this.clientIp = clientIp;
			return this;
		}

		public Builder userAgent(String userAgent) {
			this.userAgent = userAgent;
			return this;
		}

		public Builder detail(String key, Object value) {
			if (key != null && value != null) {
				this.details.put(key, value);
			}
			return this;
		}

		public Builder details(Map<String, ?> details) {
			if (details != null) {
				details.forEach((key, value) -> {
					if (key != null && value != null) {
						this.details.put(key, value);
					}
				});
			}
			return this;
		}

		public AuditEvent build() {
			return new AuditEvent(this);
		}
	}
}
