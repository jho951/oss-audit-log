# 아키텍처

## 모듈 구조

```text
audit-log/
├─ api      : 도메인 모델과 추상화
├─ core     : 기본 구현체
└─ config   : Spring Boot 자동 설정
```

## 책임 분리

### `api`

- `AuditEvent`: 감사 이벤트 불변 객체(Builder 패턴)
- `AuditEventType`: 표준 이벤트 타입 enum
- `AuditLogger`: 이벤트 기록 진입점 인터페이스
- `AuditSink`: 실제 저장/전송 대상 인터페이스

### `core`

- `DefaultAuditLogger`: `AuditSink`에 위임
- `FileAuditSink`: JSON Line 파일 append
- `ElkHttpAuditSink`: HTTP 비동기 전송
- `CompositeAuditSink`: 여러 sink fan-out
- `Json`: 내부 JSON 직렬화 유틸

### `config`

- `AuditLogProperties`: `auditlog.*` 속성 바인딩
- `AuditLogAutoConfiguration`: 조건부 Bean 자동 구성

## 데이터 흐름

1. 애플리케이션이 `AuditLogger.log(...)` 호출
2. `AuditEvent` 생성/전달
3. `AuditSink` 구현체가 이벤트를 JSON Line 포맷으로 처리
4. 파일 또는 ELK(HTTP)로 기록

## 설계 특징

- Fail-open: 로그 기록 실패가 업무 실패로 전파되지 않도록 예외를 내부에서 흡수
- 확장성: `AuditSink` 구현 추가로 저장소 확장 가능
- 프레임워크 분리: `api/core`는 순수 Java, `config`에서만 Spring 의존
