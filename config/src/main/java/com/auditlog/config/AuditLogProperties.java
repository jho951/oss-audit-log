package com.auditlog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@code auditlog.*} 설정 값을 바인딩하는 프로퍼티 클래스입니다.
 */
@ConfigurationProperties(prefix = "auditlog")
public class AuditLogProperties {
	private boolean enabled = true;
	private String serviceName = "unknown-service";
	private String env = "local";

	private String filePath = "./logs/audit.log";

	// ELK
	private boolean elkEnabled = false;
	private String elkEndpoint; // e.g. http://logstash:8080/audit
	private String elkApiKey;   // optional

	/**
	 * @return 감사 로그 기능 활성화 여부
	 */
	public boolean isEnabled() { return enabled; }

	/**
	 * @param enabled 감사 로그 기능 활성화 여부
	 */
	public void setEnabled(boolean enabled) { this.enabled = enabled; }

	/**
	 * @return 서비스 이름
	 */
	public String getServiceName() { return serviceName; }

	/**
	 * @param serviceName 서비스 이름
	 */
	public void setServiceName(String serviceName) { this.serviceName = serviceName; }

	/**
	 * @return 실행 환경명
	 */
	public String getEnv() { return env; }

	/**
	 * @param env 실행 환경명
	 */
	public void setEnv(String env) { this.env = env; }

	/**
	 * @return 파일 sink 경로
	 */
	public String getFilePath() { return filePath; }

	/**
	 * @param filePath 파일 sink 경로
	 */
	public void setFilePath(String filePath) { this.filePath = filePath; }

	/**
	 * @return ELK 전송 활성화 여부
	 */
	public boolean isElkEnabled() { return elkEnabled; }

	/**
	 * @param elkEnabled ELK 전송 활성화 여부
	 */
	public void setElkEnabled(boolean elkEnabled) { this.elkEnabled = elkEnabled; }

	/**
	 * @return ELK 엔드포인트 URL
	 */
	public String getElkEndpoint() { return elkEndpoint; }

	/**
	 * @param elkEndpoint ELK 엔드포인트 URL
	 */
	public void setElkEndpoint(String elkEndpoint) { this.elkEndpoint = elkEndpoint; }

	/**
	 * @return ELK API Key
	 */
	public String getElkApiKey() { return elkApiKey; }

	/**
	 * @param elkApiKey ELK API Key
	 */
	public void setElkApiKey(String elkApiKey) { this.elkApiKey = elkApiKey; }
}
