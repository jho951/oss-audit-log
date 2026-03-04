# 설정 가이드

## 의존성

Spring Boot 애플리케이션에서는 `config` 모듈을 의존하면 자동 설정이 활성화됩니다.

```gradle
dependencies {
    implementation("io.github.jho951:audit-log-config:<version>")
}
```

## 설정 속성

`application.yml` 또는 `application.properties`에서 `auditlog.*`를 설정합니다.

### 공통

- `auditlog.enabled` (기본: `true`): 기능 활성화 여부
- `auditlog.service-name` (기본: `unknown-service`): 서비스 이름
- `auditlog.env` (기본: `local`): 실행 환경
- `auditlog.file-path` (기본: `./logs/audit.log`): 파일 sink 경로

### ELK/HTTP

- `auditlog.elk-enabled` (기본: `false`): ELK sink 활성화
- `auditlog.elk-endpoint`: 전송 대상 URL
- `auditlog.elk-api-key`: API Key (선택)

## 예시 (YAML)

```yaml
auditlog:
  enabled: true
  service-name: order-service
  env: prod
  file-path: /var/log/app/audit.log
  elk-enabled: true
  elk-endpoint: https://elk.example.com/audit
  elk-api-key: ${ELK_API_KEY}
```

## 동작 규칙

- `enabled=false`이면 `AuditLogger` 자동 구성 비활성화
- `elk-enabled=true`이고 `elk-endpoint`가 유효하면 파일 + ELK 동시 전송
- ELK 미설정 또는 비활성화 시 파일 sink만 사용
