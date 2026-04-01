package com.auditlog.autoconfigure;

import java.io.IOException;

import com.auditlog.api.AuditContext;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

final class WebAuditContextFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest httpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}

		try {
			WebAuditContextHolder.set(new AuditContext(
				resolveTraceId(httpServletRequest),
				resolveHeader(httpServletRequest, "X-Request-Id", "X-Correlation-Id"),
				resolveClientIp(httpServletRequest),
				httpServletRequest.getHeader("User-Agent")
			));
			chain.doFilter(request, response);
		} finally {
			WebAuditContextHolder.clear();
		}
	}

	private static String resolveTraceId(HttpServletRequest request) {
		String explicitTraceId = resolveHeader(request, "X-Trace-Id", "X-B3-TraceId");
		if (explicitTraceId != null) {
			return explicitTraceId;
		}

		String traceParent = request.getHeader("traceparent");
		if (traceParent == null || traceParent.isBlank()) {
			return null;
		}

		String[] tokens = traceParent.trim().split("-");
		if (tokens.length >= 4 && !tokens[1].isBlank()) {
			return tokens[1];
		}
		return traceParent;
	}

	private static String resolveHeader(HttpServletRequest request, String... names) {
		for (String name : names) {
			String value = request.getHeader(name);
			if (value != null && !value.isBlank()) {
				return value;
			}
		}
		return null;
	}

	private static String resolveClientIp(HttpServletRequest request) {
		String forwardedFor = resolveHeader(request, "X-Forwarded-For", "X-Real-IP");
		if (forwardedFor == null || forwardedFor.isBlank()) {
			return request.getRemoteAddr();
		}
		int commaIndex = forwardedFor.indexOf(',');
		return commaIndex >= 0 ? forwardedFor.substring(0, commaIndex).trim() : forwardedFor.trim();
	}
}
