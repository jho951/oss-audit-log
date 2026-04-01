# 아키텍처

## 모듈 구조

```text
audit-log/
├─ api/             : :audit-log-api (artifact: audit-log-api)
├─ core/            : :audit-log-core (artifact: audit-log-core)
├─ autoconfigure/   : :audit-log-spring-boot-autoconfigure (artifact: audit-log-spring-boot-autoconfigure)
└─ starter/         : :audit-log-spring-boot-starter (artifact: audit-log-spring-boot-starter)
```

## 핵심 타입

### `:audit-log-api` (`api/`)

- 패키지: `com.auditlog.api`, `com.auditlog.spi`
- `AuditEvent`: 표준 감사 이벤트
- `AuditActorType`, `AuditEventType`, `AuditResult`: 표준 분류 값
- `AuditContext`: 요청/추적 메타데이터
- `AuditLogger`: 감사 로그 기록 진입점
- `AuditSink`: 저장소 추상화

### `spi` (패키지는 `:audit-log-api` 모듈 내부)

- `AuditContextResolver`: 실행 컨텍스트 주입
- `AuditMaskingPolicy`: 민감정보 마스킹 정책

### `:audit-log-core` (`core/`)

- 패키지: `com.auditlog.core`
- `DefaultAuditLogger`: 컨텍스트 주입 + 마스킹 + sink 위임
- `FileAuditSink`: JSON Line 파일 기록
- `ElkHttpAuditSink`: HTTP 비동기 전송
- `CompositeAuditSink`: fan-out
- `AsyncAuditSink`: executor 기반 비동기 래퍼

### `:audit-log-spring-boot-autoconfigure` (`autoconfigure/`, artifactId: `audit-log-spring-boot-autoconfigure`)

- 패키지: `com.auditlog.autoconfigure`
- `AuditLogAutoConfiguration`: starter 자동 구성
- `AuditLogProperties`: 속성 바인딩
- `WebAuditContextFilter`: 요청 메타데이터 수집
- `WebAuditContextResolver`: 요청 컨텍스트를 이벤트에 반영

### `:audit-log-spring-boot-starter` (`starter/`, artifactId: `audit-log-spring-boot-starter`)

- 역할: Spring Boot 서비스의 기본 진입점 의존성
- 구성: `:audit-log-spring-boot-autoconfigure` + `spring-boot-starter`를 묶어서 제공

## 모듈 의존 관계

- `:audit-log-spring-boot-starter` -> `:audit-log-spring-boot-autoconfigure`
- `:audit-log-spring-boot-autoconfigure` -> `:audit-log-core`
- `:audit-log-core` -> `:audit-log-api`

## 데이터 흐름

1. 애플리케이션이 `AuditLogger`를 호출합니다.
2. `DefaultAuditLogger`가 등록된 `AuditContextResolver`들을 순서대로 조회해 요청 메타데이터를 채웁니다.
3. `AuditMaskingPolicy`가 `details`의 민감정보를 마스킹합니다.
4. 최종 이벤트를 `AuditSink`가 파일 또는 HTTP 저장소로 기록합니다.

## 설계 특징

- 표준화: 서비스별 제각각인 감사 필드를 공통 모델로 통합
- 확장성: sink, context resolver, masking policy 교체 가능
- 웹 친화성: HTTP 요청에서 traceId/requestId/clientIp/userAgent 자동 수집
- 운영성: fail-open 유지, 비동기 옵션 제공

## MSA 적용 방식

- 권장: 각 서비스가 이벤트를 생성하고 중앙 수집(ELK/Kafka/OTel backend 등)으로 전달
- 비권장: 모니터링 서버 한 곳에서만 감사 이벤트를 대신 생성
- 이유: 감사 이벤트의 핵심 맥락(행위자, 비즈니스 액션, 실패 사유)은 호출 서비스 내부에서 가장 정확하게 채워짐
