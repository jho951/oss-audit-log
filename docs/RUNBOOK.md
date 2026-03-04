# 운영 런북 (5분 점검)

감사 로그 장애가 의심될 때 5분 안에 상태를 확인하고 1차 조치하기 위한 절차입니다.

## 0) 준비

- 대상: `audit-log`를 사용하는 Spring Boot 서비스
- 확인 대상: 파일 로그, ELK 전송, 애플리케이션 설정/빈 상태

## 1분차: 설정/기동 상태 확인

1. `auditlog.enabled` 값 확인 (`true`여야 함)
2. `auditlog.file-path`/`auditlog.elk-enabled`/`auditlog.elk-endpoint` 확인
3. 애플리케이션 시작 로그에서 `AuditLogAutoConfiguration` 적용 여부 확인

판단:
- 자동 설정이 안 붙으면 의존성/설정 문제
- 붙었는데 로그가 없으면 호출 흐름 또는 출력 경로 문제

## 2분차: 파일 로그 경로 확인

1. 파일 경로 존재/권한 확인

```bash
ls -la /var/log/app
touch /var/log/app/audit.log
```

2. 최근 append 여부 확인

```bash
tail -n 20 /var/log/app/audit.log
```

판단:
- `touch` 실패: 권한 또는 경로 설정 오류
- 파일은 있는데 이벤트 없음: 코드에서 `AuditLogger.log(...)` 호출 누락 가능성

## 3분차: 애플리케이션에서 실제 호출 확인

1. 핵심 기능(로그인/수정/삭제) 1건 실행
2. 실행 직후 파일 tail 확인
3. 필요 시 임시로 샘플 로그 1건을 강제 호출해 append 여부 확인

판단:
- 샘플 로그는 찍히는데 실제 기능 로그가 없으면 비즈니스 흐름 문제

## 4분차: ELK 경로 확인 (활성화된 경우)

1. 설정 재확인: `elk-enabled=true`, `elk-endpoint` 비어있지 않음
2. 네트워크/인증 수동 점검

```bash
curl -i -X POST "https://elk.example.com/audit" \
  -H "Content-Type: application/json" \
  -H "Authorization: ApiKey <KEY>" \
  -d '{"ping":"ok"}'
```

판단:
- 401/403: API Key 또는 권한 문제
- 5xx/timeout: ELK 또는 네트워크 경로 문제

## 5분차: 1차 조치

1. 파일 로깅 우선 복구 (`file-path` 권한/경로 수정)
2. ELK 장애 시 `elk-enabled=false`로 임시 우회 후 파일 기반 수집 유지
3. 장애 시간대/영향 범위/임시 조치를 운영 채널에 공유

## 에스컬레이션 기준

- 10분 이상 파일 로그가 전혀 생성되지 않음
- ELK 전송 실패가 15분 이상 지속
- 감사 로그 누락으로 컴플라이언스 영향 가능성 존재

## 사후 조치 체크리스트

1. 원인 분류: 설정/권한/코드/네트워크/외부 시스템
2. 재발 방지: 배포 전 설정 검증, 권한 점검 스크립트, 모니터링 알람
3. 문서 반영: `troubleshooting.md`와 본 런북 업데이트
