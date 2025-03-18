# 📡 WebSocket Chatting

## ✅ 프로젝트 개요
이론으로만 접했던 WebSocket을 실무에서 사용할 기회가 없어서, 스스로 사용법을 익히고 구현한 간단한 채팅 서비스 프로젝트입니다.  
WebSocket과 STOMP 프로토콜을 적용하여 **실시간 양방향 통신**을 구현하고, JWT 인증과 보안 강화까지 구현했습니다.
---

## ✅ 주요 기능

- **회원 관리**
  - 로그인 / 회원가입 (JWT 인증 방식, MongoB)
  - 중복 로그인 방지
- **채팅 기능**
  - 채팅방 생성 / 조회 / 입장 / 퇴장
  - 실시간 채팅 (WebSocket + STOMP)
  - 채팅 메시지 저장 / 조회 (MongoDB)
- **보안**
  - httpOnly 쿠키에 JWT 저장 → XSS 방어
  - 클라이언트가 직접 토큰을 다루지 않아 보안 강화
  - 로그인하지 않은 사용자는 쿠키가 없어 메인페이지만 접근 가능
  - Spring Security 로 접근 제어 및 인증 처리

---

## ✅ 기술 스택

| 분야                              | 사용 기술                       |
|---------------------------------|-----------------------------|
| Language(언어)                    | Java 17                     |
| Framework(프레임워크)                | Spring Boot 3.2.2           |
| Database(데이터베이스)                | MongoDB 7.0.17              |
| Server(서버)                      | AWS EC2(Ubuntu 24.04.1 LTS) |
| Security(보안)                    | JWT, Spring Security        |
| CI/CD(배포)                       | Github Actions              |
| Client(클라이언트)                   | Thymeleaf + Fetch API       |
| Communication Protocol(통신 프로토콜) | WebSocket + STOMP           |
--------------------------------------------

## ✅ 아키텍처 및 주요 구현 포인트

### 1. WebSocket + STOMP 구조
- STOMP 프로토콜을 적용하여 구독/발행 구조를 사용
- 채팅방 입장 시 실시간 연결 생성, 퇴장 시 연결 종료 → **리소스 최적화**
- WebSocket 핸드쉐이크와 인증 절차는 JWT로 처리

### 2. JWT 인증과 보안 강화
- httpOnly 쿠키에 JWT 저장하여 **XSS 방지**
- 필터(`JwtAuthenticationFilter`)를 통해 인증이 필요한 요청 차단
- 중복 로그인 감지를 통해 동일 계정의 다중 접속 제한

### 3. CSR 방식
- 다른 프론트엔드 변경 고려(API 중심 설계)
- 데이터를 가져올 때 Fetch API 를 사용하여 데이터 로드

### 4.배포
- 
- GitHub Secrets + 환경 변수 를 사용하여 민감 정보 방어 
---

## ✅ 디렉토리 구조

```
src
└── main
    ├── java
    │   └── com.example.websocket.chatting
    │       ├── Application.java
    │       ├── common
    │       │   ├── config
    │       │   │   └── WebSocketConfig.java           # WebSocket 설정
    │       │   ├── exception
    │       │   │   ├── ExceptionController.java       # 글로벌 예외 처리
    │       │   │   └── ValidationCode.java            # 커스텀 상태 코드
    │       │   ├── security
    │       │   │   ├── EncryptionConfig.java          # 패스워드 암호화
    │       │   │   ├── JwtAuthenticationFilter.java   # JWT 검증 필터
    │       │   │   ├── JwtProvider.java               # JWT 생성/검증/관리
    │       │   │   └── SecurityConfig.java            # Spring Security 설정
    │       │   └── util
    │       │       └── CommonUtil.java
    │       ├── controller
    │       │   ├── ChatServiceController.java         # 채팅 REST API
    │       │   └── ChatViewController.java            # 뷰 처리용 Controller
    │       ├── dto
    │       │   └── ChatServiceRequestDto.java
    │       ├── model
    │       │   ├── ChatMessage.java
    │       │   ├── ChatRoom.java
    │       │   └── Member.java
    │       ├── repository
    │       │   └── MemberRepository.java
    │       └── service
    │           ├── ChatService.java
    │           └── impl
    │               └── ChatServiceImpl.java
    └── resources
        ├── application.yml
        ├── static/css
        │   ├── chat.css
        │   ├── index.css
        │   ├── login.css
        │   └── register.css
        └── templates
            ├── chat.html
            ├── error.html
            ├── index.html
            ├── login.html
            └── register.html
```