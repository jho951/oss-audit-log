package com.auditlog.config;

import com.auditlog.api.AuditLogger;
import com.auditlog.api.AuditSink;
import com.auditlog.core.*;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * 감사 로그 컴포넌트를 자동으로 등록하는 Spring Boot 자동 설정입니다.
 */
@Configuration
@EnableConfigurationProperties(AuditLogProperties.class)
@ConditionalOnProperty(prefix = "auditlog", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuditLogAutoConfiguration {

	/**
	 * 설정값에 따라 파일 sink 또는 파일+ELK 합성 sink를 구성한 {@link AuditLogger}를 생성합니다.
	 *
	 * @param p 감사 로그 설정 프로퍼티
	 * @return 애플리케이션에서 사용할 감사 로거
	 */
	@Bean
	public AuditLogger auditLogger(AuditLogProperties p) {
		var sinks = new ArrayList<AuditSink>();

		sinks.add(new FileAuditSink(Path.of(p.getFilePath()), p.getServiceName(), p.getEnv()));

		if (p.isElkEnabled() && p.getElkEndpoint() != null && !p.getElkEndpoint().isBlank()) {
			sinks.add(new ElkHttpAuditSink(URI.create(p.getElkEndpoint()), p.getServiceName(), p.getEnv(), p.getElkApiKey()));
		}

		AuditSink sink = sinks.size() == 1 ? sinks.get(0) : new CompositeAuditSink(sinks);
		return new DefaultAuditLogger(sink);
	}
}
