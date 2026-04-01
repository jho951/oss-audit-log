
# audit-log

`누가(actor), 언제(occurredAt), 어디서(clientIp), 무엇을(action/resource), 어떤 결과(result)`를
표준 필드로 기록해서 **보안 감사, 운영 추적, 장애 분석**에 재사용할 수 있도록 만든 멀티 모듈 라이브러리입니다.

- **언어**: Java 17
- **빌드 도구**: Gradle (멀티 모듈)
- **Spring 진입점**: `:audit-log-spring-boot-starter` 모듈 권장 (`:audit-log-spring-boot-autoconfigure` 직접 의존은 고급 사용)

## 모듈 구조

```text
audit-log/
├─ audit-log-api/                       # :audit-log-api (artifact: audit-log-api)
├─ audit-log-core/                      # :audit-log-core (artifact: audit-log-core)
├─ audit-log-spring-boot-autoconfigure/ # :audit-log-spring-boot-autoconfigure (artifact: audit-log-spring-boot-autoconfigure)
└─ audit-log-spring-boot-starter/       # :audit-log-spring-boot-starter (artifact: audit-log-spring-boot-starter)
```

## 빠른 시작

```gradle
dependencies {
    implementation "io.github.jho951:audit-log-spring-boot-starter:<version>"
}
```

## 포함 범위

- 표준 감사 이벤트 모델 (`AuditEvent`, `AuditResult`, `AuditActorType`)
- 성공/실패 기록 보조 API (`logSuccess`, `logFailure`)
- `details` 민감정보 마스킹
- 파일 sink 기본 제공, HTTP sink 선택 제공
- 웹 환경의 `traceId`, `requestId`, `clientIp`, `userAgent` 자동 주입
- async sink 래핑 옵션

## 문서

- [문서 인덱스](./docs/README.md)
- [아키텍처](./docs/ARCHITECTURE.md)
- [설정](./docs/CONFIGURATION.md)
- [사용 예시](./docs/USAGE.md)
- [운영 런북](./docs/RUNBOOK.md)
- [트러블슈팅](./docs/TROUBLESHOOTING.md)
