package com.auditlog.api;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 단일 감사 이벤트를 표현하는 불변 객체입니다.
 */
public final class AuditEvent {

	private final Instant timestamp;
	private final String actorId;
	private final String actorIp;
	private final AuditEventType eventType;
	private final String action;
	private final String resourceType;
	private final String resourceId;
	private final String result;
	private final Map<String, String> details;
	private final String traceId;

	private AuditEvent(Builder builder) {
		this.timestamp = builder.timestamp != null ? builder.timestamp : Instant.now();
		this.actorId = builder.actorId;
		this.actorIp = builder.actorIp;
		this.eventType = builder.eventType;
		this.action = builder.action;
		this.resourceType = builder.resourceType;
		this.resourceId = builder.resourceId;
		this.result = builder.result;
		this.details = Collections.unmodifiableMap(new HashMap<>(builder.details));
		this.traceId = builder.traceId;
	}

	/**
	 * @return 이벤트 발생 시각
	 */
	public Instant getTimestamp() {
		return timestamp;
	}

	/**
	 * @return 행위자 식별자
	 */
	public String getActorId() {
		return actorId;
	}

	/**
	 * @return 행위자 IP
	 */
	public String getActorIp() {
		return actorIp;
	}

	/**
	 * @return 이벤트 유형
	 */
	public AuditEventType getEventType() {
		return eventType;
	}

	/**
	 * @return 액션명
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @return 대상 리소스 유형
	 */
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * @return 대상 리소스 식별자
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * @return 처리 결과
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @return 추가 메타데이터(불변 맵)
	 */
	public Map<String, String> getDetails() {
		return details;
	}

	/**
	 * @return 분산 추적을 위한 trace ID
	 */
	public String getTraceId() {
		return traceId;
	}

	/**
	 * 이벤트 유형과 액션명을 기반으로 빌더를 생성합니다.
	 *
	 * @param eventType 이벤트 유형
	 * @param action 액션명
	 * @return 이벤트 빌더
	 */
	public static Builder builder(AuditEventType eventType, String action) {
		return new Builder(eventType, action);
	}

	/**
	 * {@link AuditEvent}를 구성하는 빌더입니다.
	 */
	public static final class Builder {
		private Instant timestamp;
		private final AuditEventType eventType;
		private final String action;
		private String actorId;
		private String traceId;
		private String actorIp;
		private String resourceType;
		private String resourceId;
		private String result;
		private final Map<String, String> details = new HashMap<>();

		/**
		 * @param eventType 이벤트 유형
		 * @param action 액션명
		 */
		public Builder(AuditEventType eventType, String action) {
			this.eventType = eventType;
			this.action = action;
		}

		/**
		 * @param timestamp 이벤트 발생 시각
		 * @return 현재 빌더
		 */
		public Builder timestamp(Instant timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		/**
		 * @param actorId 행위자 식별자
		 * @return 현재 빌더
		 */
		public Builder actorId(String actorId) {
			this.actorId = actorId;
			return this;
		}

		/**
		 * @param actorIp 행위자 IP
		 * @return 현재 빌더
		 */
		public Builder actorIp(String actorIp) {
			this.actorIp = actorIp;
			return this;
		}

		/**
		 * @param resourceType 대상 리소스 유형
		 * @param resourceId 대상 리소스 식별자
		 * @return 현재 빌더
		 */
		public Builder resource(String resourceType, String resourceId) {
			this.resourceType = resourceType;
			this.resourceId = resourceId;
			return this;
		}

		/**
		 * @param result 처리 결과
		 * @return 현재 빌더
		 */
		public Builder result(String result) {
			this.result = result;
			return this;
		}

		/**
		 * 부가 정보를 추가합니다. key 또는 value가 null이면 무시합니다.
		 *
		 * @param key 부가 정보 키
		 * @param value 부가 정보 값
		 * @return 현재 빌더
		 */
		public Builder detail(String key, String value) {
			if (key != null && value != null) {
				this.details.put(key, value);
			}
			return this;
		}

		/**
		 * @param traceId 분산 추적 ID
		 * @return 현재 빌더
		 */
		public Builder traceId(String traceId) {
			this.traceId = traceId;
			return this;
		}

		/** @return 생성된 불변 감사 이벤트 */
		public AuditEvent build() {
			return new AuditEvent(this);
		}
	}
}
