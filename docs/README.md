# Audit Log 문서

현재 코드 구조는 Gradle project path 기준(`:audit-log-api`, `:audit-log-core`, `:audit-log-spring-boot-autoconfigure`, `:audit-log-spring-boot-starter`)이고,
배포 artifactId는 각각 `audit-log-api`, `audit-log-core`, `audit-log-spring-boot-autoconfigure`, `audit-log-spring-boot-starter`입니다.

## 문서 목록

1. [아키텍처](./ARCHITECTURE.md)
2. [설정 가이드](./CONFIGURATION.md)
3. [사용 가이드](./USAGE.md)
4. [운영 런북](./RUNBOOK.md)
5. [트러블슈팅](./TROUBLESHOOTING.md)

## 빠른 요약

- 모듈
  - `:audit-log-api` (`audit-log-api/`, artifactId: `audit-log-api`)
  - `:audit-log-core` (`audit-log-core/`, artifactId: `audit-log-core`)
  - `:audit-log-spring-boot-autoconfigure` (`audit-log-spring-boot-autoconfigure/`, artifactId: `audit-log-spring-boot-autoconfigure`)
  - `:audit-log-spring-boot-starter` (`audit-log-spring-boot-starter/`, artifactId: `audit-log-spring-boot-starter`)
- 기본 패키지
  - `com.auditlog.api`
  - `com.auditlog.core`
  - `com.auditlog.autoconfigure`
- 설정 prefix: `auditlog.*`
