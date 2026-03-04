# 사용 가이드

## 기본 사용

`AuditLogger`를 주입받아 이벤트를 기록합니다.

```java
import com.auditlog.api.AuditEventType;
import com.auditlog.api.AuditLogger;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginService {
    private final AuditLogger auditLogger;

    public LoginService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    public void login(String userId, String ip, String traceId) {
        auditLogger.log(
            AuditEventType.LOGIN,
            "USER_LOGIN",
            userId,
            ip,
            "USER",
            userId,
            "SUCCESS",
            traceId,
            Map.of("method", "password")
        );
    }
}
```

## Builder 사용

필요한 필드만 선택적으로 구성할 때 `AuditEvent.builder(...)`를 사용할 수 있습니다.

```java
import com.auditlog.api.AuditEvent;
import com.auditlog.api.AuditEventType;

AuditEvent event = AuditEvent.builder(AuditEventType.UPDATE, "ORDER_STATUS_CHANGE")
    .actorId("admin-1")
    .actorIp("10.0.0.1")
    .resource("ORDER", "A-1024")
    .result("SUCCESS")
    .traceId("8fba3c...")
    .detail("from", "PAID")
    .detail("to", "SHIPPED")
    .build();
```

## 로그 포맷

파일 및 HTTP 전송 모두 JSON Line(한 줄당 한 이벤트) 포맷을 사용합니다.

```json
{
  "@timestamp": "2026-02-28T12:34:56Z",
  "service": "order-service",
  "env": "prod",
  "eventType": "LOGIN",
  "action": "USER_LOGIN",
  "actorId": "u-100",
  "actorIp": "192.168.0.10",
  "resourceType": "USER",
  "resourceId": "u-100",
  "result": "SUCCESS",
  "traceId": "abc123",
  "details": { "method": "password" }
}
```

## 운영 시 권장사항

- `service-name`, `env`, `traceId`를 항상 채워 추적성을 확보
- 파일 경로는 로그 수집기(Filebeat 등)가 읽는 위치로 지정
- `details`에는 민감정보(비밀번호/토큰/개인식별정보) 저장 금지
