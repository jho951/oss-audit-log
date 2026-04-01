package com.auditlog.autoconfigure;

import java.util.ArrayList;
import java.util.List;

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
	private boolean elkEnabled = false;
	private String elkEndpoint;
	private String elkApiKey;
	private boolean asyncEnabled = false;
	private int asyncThreadCount = 2;
	private int asyncQueueCapacity = 1000;
	private boolean webEnabled = true;
	private List<String> sensitiveKeys = new ArrayList<>(List.of(
		"password",
		"pwd",
		"secret",
		"token",
		"authorization",
		"credential",
		"refreshToken",
		"accessToken",
		"email",
		"phone"
	));

	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) { this.enabled = enabled; }
	public String getServiceName() { return serviceName; }
	public void setServiceName(String serviceName) { this.serviceName = serviceName; }
	public String getEnv() { return env; }
	public void setEnv(String env) { this.env = env; }
	public String getFilePath() { return filePath; }
	public void setFilePath(String filePath) { this.filePath = filePath; }
	public boolean isElkEnabled() { return elkEnabled; }
	public void setElkEnabled(boolean elkEnabled) { this.elkEnabled = elkEnabled; }
	public String getElkEndpoint() { return elkEndpoint; }
	public void setElkEndpoint(String elkEndpoint) { this.elkEndpoint = elkEndpoint; }
	public String getElkApiKey() { return elkApiKey; }
	public void setElkApiKey(String elkApiKey) { this.elkApiKey = elkApiKey; }
	public boolean isAsyncEnabled() { return asyncEnabled; }
	public void setAsyncEnabled(boolean asyncEnabled) { this.asyncEnabled = asyncEnabled; }
	public int getAsyncThreadCount() { return asyncThreadCount; }
	public void setAsyncThreadCount(int asyncThreadCount) { this.asyncThreadCount = asyncThreadCount; }
	public int getAsyncQueueCapacity() { return asyncQueueCapacity; }
	public void setAsyncQueueCapacity(int asyncQueueCapacity) { this.asyncQueueCapacity = asyncQueueCapacity; }
	public boolean isWebEnabled() { return webEnabled; }
	public void setWebEnabled(boolean webEnabled) { this.webEnabled = webEnabled; }
	public List<String> getSensitiveKeys() { return sensitiveKeys; }
	public void setSensitiveKeys(List<String> sensitiveKeys) { this.sensitiveKeys = sensitiveKeys; }
}
