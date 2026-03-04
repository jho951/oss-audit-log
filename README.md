# audit-log

여러 서비스에서 공통으로 사용할 수 있는 **Audit Log 인프라 모듈**입니다.  
`누가(actor), 언제(timestamp), 어디서(ip), 무엇을(action/resource), 어떤 결과(result)`를 표준화된 포맷으로 기록해서,  
**보안·감사·장애 분석**에 활용할 수 있도록 설계되었습니다.

- **언어**: Java 17
- **빌드 도구**: Gradle (멀티 모듈)
- **Spring 의존성**: `config` 모듈에서만 사용 (core/api는 순수 Java)

---

## 1. 모듈 구조

```text
audit-log/
├─ settings.gradle
├─ build.gradle          # 공통 Gradle 설정
│
├─ core/                 # 순수 코어 도메인 (AuditEvent, AuditSink, AuditLogger 등)
│  └─ src/main/java/com/auditlog/core/...
│
├─ api/                  # 서비스에서 직접 쓸 수 있는 Facade (static AuditLog 등)
│  └─ src/main/java/com/auditlog/api/...
│
└─ config/               # Spring Boot 자동 설정 (starter 역할)
   └─ src/main/java/com/auditlog/config/...
```

---

## 2. 문서

- 전체 문서 인덱스: [`docs/README.md`](./docs/README.md)
- 아키텍처: [`docs/architecture.md`](./docs/architecture.md)
- 설정: [`docs/configuration.md`](./docs/configuration.md)
- 사용 예시: [`docs/usage.md`](./docs/usage.md)
- 트러블슈팅: [`docs/troubleshooting.md`](./docs/troubleshooting.md)
- 운영 런북: [`docs/runbook.md`](./docs/runbook.md)
