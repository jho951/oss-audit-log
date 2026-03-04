# Audit Log 문서

`audit-log` 멀티 모듈 프로젝트 문서 인덱스입니다.

## 문서 목록

1. [아키텍처](./ARCHITECTURE.md)
2. [설정 가이드](./CONFIGURATION.md)
3. [사용 가이드](./usage.md)
4 [운영 런북](./runbook.md)

## 빠른 요약

- 목적: 서비스 공통 감사 로그(Audit Log) 표준화
- 런타임: Java 17
- 모듈:
  - `api`: 이벤트/로거/싱크 인터페이스
  - `core`: 파일/HTTP(ELK) 싱크 및 기본 구현
  - `config`: Spring Boot 자동 설정
