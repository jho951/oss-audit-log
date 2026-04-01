package com.auditlog.autoconfigure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.auditlog.api.AuditContext;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class WebAuditContextFilterTest {

	@AfterEach
	void tearDown() {
		WebAuditContextHolder.clear();
	}

	@Test
	void filterShouldCaptureRequestMetadataIntoThreadLocalContext() throws ServletException, IOException {
		WebAuditContextFilter filter = new WebAuditContextFilter();
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("X-Trace-Id", "trace-1");
		request.addHeader("X-Request-Id", "req-1");
		request.addHeader("X-Forwarded-For", "203.0.113.10, 10.0.0.1");
		request.addHeader("User-Agent", "JUnit");

		FilterChain chain = (servletRequest, servletResponse) -> {
			AuditContext context = WebAuditContextHolder.get();
			assertEquals("trace-1", context.traceId());
			assertEquals("req-1", context.requestId());
			assertEquals("203.0.113.10", context.clientIp());
			assertEquals("JUnit", context.userAgent());
		};

		filter.doFilter(request, new MockHttpServletResponse(), chain);
	}
}
