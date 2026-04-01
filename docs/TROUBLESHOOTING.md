# 트러블슈팅

## 1. 파일 로그가 생성되지 않음

확인 순서:

1. `auditlog.enabled=true` 여부 확인
2. `auditlog.file-path` 경로와 디렉터리 권한 확인
3. 애플리케이션 코드에서 `AuditLogger.logSuccess(...)`/`logFailure(...)`가 실제 호출되는지 확인
4. async 사용 시 큐가 가득 차지 않았는지 확인

## 2. 웹 요청 메타데이터가 비어 있음

확인 순서:

1. `auditlog.web-enabled=true` 여부 확인
2. 서블릿 기반 애플리케이션인지 확인
3. `com.auditlog.autoconfigure.WebAuditContextFilter` bean 등록 여부 확인
4. 요청 헤더(`X-Trace-Id`, `X-Request-Id`, `X-Forwarded-For`, `traceparent`) 확인

## 3. ELK 전송이 누락됨

확인 순서:

1. `auditlog.elk-enabled=true`
2. `auditlog.elk-endpoint` 값 확인
3. 인증 키(`auditlog.elk-api-key`) 유효성 확인
4. 네트워크 egress / 방화벽 정책 확인

## 4. 민감정보가 마스킹되지 않음

확인 순서:

1. `details` 키 이름이 `sensitive-keys`와 매칭되는지 확인
2. 중첩 구조가 `Map`, `Iterable`, 배열 형태인지 확인
3. 커스텀 `AuditMaskingPolicy` bean이 기본 정책을 대체하지 않았는지 확인

## 5. 모듈/패키지 이름 혼선

확인 순서:

1. Gradle 모듈명이 `:audit-log-api`, `:audit-log-core`, `:audit-log-spring-boot-autoconfigure`, `:audit-log-spring-boot-starter`인지 확인
2. starter 아티팩트가 `audit-log-spring-boot-starter`인지 확인
3. 자동설정 클래스 경로가 `com.auditlog.autoconfigure.AuditLogAutoConfiguration`인지 확인
