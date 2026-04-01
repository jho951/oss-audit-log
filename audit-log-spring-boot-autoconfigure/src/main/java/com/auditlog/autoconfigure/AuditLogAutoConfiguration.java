package com.auditlog.autoconfigure;

import com.auditlog.api.AuditLogger;
import com.auditlog.api.AuditSink;
import com.auditlog.core.AsyncAuditSink;
import com.auditlog.core.CompositeAuditSink;
import com.auditlog.core.DefaultAuditLogger;
import com.auditlog.core.ElkHttpAuditSink;
import com.auditlog.core.FileAuditSink;
import com.auditlog.spi.AuditContextResolver;
import com.auditlog.spi.AuditMaskingPolicy;

import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 감사 로그 컴포넌트를 자동으로 등록하는 Spring Boot 자동 설정입니다.
 */
@Configuration
@EnableConfigurationProperties(AuditLogProperties.class)
@ConditionalOnProperty(prefix = "auditlog", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuditLogAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public AuditMaskingPolicy auditMaskingPolicy(AuditLogProperties properties) {
		return new DefaultAuditMaskingPolicy(properties.getSensitiveKeys());
	}

	@Bean(destroyMethod = "close")
	@ConditionalOnMissingBean
	public AuditSink auditSink(AuditLogProperties properties) {
		List<AuditSink> sinks = new ArrayList<>();
		sinks.add(new FileAuditSink(Path.of(properties.getFilePath()), properties.getServiceName(), properties.getEnv()));

		if (properties.isElkEnabled() && properties.getElkEndpoint() != null && !properties.getElkEndpoint().isBlank()) {
			sinks.add(new ElkHttpAuditSink(
				URI.create(properties.getElkEndpoint()),
				properties.getServiceName(),
				properties.getEnv(),
				properties.getElkApiKey()
			));
		}

		AuditSink sink = sinks.size() == 1 ? sinks.get(0) : new CompositeAuditSink(sinks);
		if (!properties.isAsyncEnabled()) {
			return sink;
		}

		ExecutorService executorService = new ThreadPoolExecutor(
			properties.getAsyncThreadCount(),
			properties.getAsyncThreadCount(),
			60L,
			TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(properties.getAsyncQueueCapacity()),
			new ThreadPoolExecutor.DiscardPolicy()
		);
		return new AsyncAuditSink(sink, executorService);
	}

	@Bean
	@ConditionalOnMissingBean
	public AuditLogger auditLogger(
		AuditSink auditSink,
		ObjectProvider<AuditContextResolver> resolversProvider,
		AuditMaskingPolicy maskingPolicy
	) {
		List<AuditContextResolver> resolvers = resolversProvider.orderedStream().toList();
		return new DefaultAuditLogger(auditSink, resolvers, maskingPolicy);
	}

	@Configuration
	@ConditionalOnClass(name = "jakarta.servlet.Filter")
	@ConditionalOnProperty(prefix = "auditlog", name = "web-enabled", havingValue = "true", matchIfMissing = true)
	static class WebAuditConfiguration {

		@Bean
		@ConditionalOnMissingBean
		WebAuditContextFilter webAuditContextFilter() {
			return new WebAuditContextFilter();
		}

		@Bean
		@ConditionalOnMissingBean
		AuditContextResolver webAuditContextResolver() {
			return new WebAuditContextResolver();
		}
	}
}
