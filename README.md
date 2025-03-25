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

📌 해당 코드 파일: [WebSocketConfig.java](src/main/java/com/example/websocket/chatting/common/config/WebSocketConfig.java)
 
### 2. JWT 인증과 보안 강화
- httpOnly 쿠키에 JWT 저장하여 **XSS 방지**
- Access Token 단독 사용 → 만료 시 재로그인 필요 (Refresh Token 미사용)
- JWT 만료 시 자동 로그아웃 -> 로그인 화면으로 이동
- 필터(`JwtAuthenticationFilter`)를 통해 인증이 필요한 요청 차단
- 중복 로그인 감지를 통해 동일 계정의 다중 접속 제한 
 
📌 해당 코드 파일: [SecurityConfig.java](src/main/java/com/example/websocket/chatting/common/security/SecurityConfig.java)

### 3. API 중심 설계
- 현재 코드 변경없이 다른 프론트엔드와 연동 가능([swagger](http://ec2-3-27-119-223.ap-southeast-2.compute.amazonaws.com/swagger-ui/index.html))
- Thymeleaf 에서 데이터를 가져올 때 Fetch API 를 사용하여 데이터 로드

📌해당 코드 파일: [templates](src/main/resources/templates)


### 4.배포
- AWS EC2(Ubuntu) + GitHub Actions 기반 CI/CD 구축
- 프로퍼티 값을 GitHub Secrets에 저장하여 민감 정보 보호
- 배포 시 secrets 값을 서버 환경변수로 설정 → property 파일에 반영 후 jar 실행
- Zero-Downtime 워크플로우 적용

📌해당 코드 파일: [workflow.yml](.github/workflows/workflow.yml)

### 5.모니터링
- Prometheus(매트릭 수집)
  - EC2 및 애플리케이션 상태를 모니터링하는 역할을 함
- Grafana(대시보드)
  - Prometheus에서 수집한 데이터를 시각화하는 역할을 함


## ✅ 디렉토리 구조

```
└── src
    └── main
        ├── java
        │   └── com
        │       └── example
        │           └── websocket
        │               └── chatting
        │                   ├── Application.java
        │                   ├── common
        │                   │   ├── config
        │                   │   │   └── WebSocketConfig.java : websocket 설정
        │                   │   ├── exception
        │                   │   │   ├── ExceptionController.java : global 예외처리
        │                   │   │   └── ValidationCode.java : 상태코드 enum
        │                   │   ├── security
        │                   │   │   ├── EncryptionConfig.java : 패스워드 암호화
        │                   │   │   ├── JwtAuthenticationFilter.java : jwt 필터
        │                   │   │   ├── JwtProvider.java : jwt 유틸(생성, 검증)
        │                   │   │   └── SecurityConfig.java : spring security 설정
        │                   │   └── util
        │                   │       ├── CommonUtil.java : 공통 유틸(response)
        │                   ├── controller
        │                   │   ├── ChatServiceController.java : 채팅 서비스 REST 컨트롤러
        │                   │   └── ChatViewController.java : 채팅 서비스 View 컨트롤러
        │                   ├── dto
        │                   │   ├── ChatServiceRequestDto.java : 채팅 서비스 REST 컨트롤러 requestDto
        │                   │   └── MemberListDto.java : DTO Projection
        │                   ├── model
        │                   │   ├── Chatroom.java : chatroom 콜렉션
        │                   │   ├── ChatroomMessage.java : chatroom_message 콜렌션
        │                   │   └── Member.java : member 콜렉션
        │                   ├── repository
        │                   │   ├── ChatroomMessageRepository.java : chatroomMessage 레포지토리
        │                   │   ├── ChatroomRepository.java : chatroom 레포지토리
        │                   │   └── MemberRepository.java : member 레포지토리
        │                   └── service
        │                       └── ChatService.java : 스프링부트 실행
        └── resources
            ├── application.yml : application property
            ├── static
            │   ├── css : 스타일 시트
            │   │   ├── chat.css
            │   │   ├── chatroom-create.css
            │   │   ├── chatroom.css
            │   │   ├── footer.css
            │   │   ├── index.css
            │   │   ├── login.css
            │   │   ├── mypage.css
            │   │   └── register.css
            │   └── favicon.ico : 파비콘
            └── templates
                ├── chat.html : 채팅방 리스트 화면
                ├── chatroom.html : 채팅방 화면
                ├── error.html : 에러 화면
                ├── index.html : 메인 화면
                ├── login.html : 로그인 화면
                ├── mypage.html : 마이페이지 화면
                └── register.html : 회원가입 화면
```

## ✅ MongoDB Schema
```
- member(유저)
{
  _id : MongoDB 고유 ID (PK)
  nickname : 유저 ID (unique)
  password : 비밀번호
}

- chatroom(채팅방)
{
  _id : MongoDB 고유 ID (PK)
  initiator : 생성자
  participant : 참석자
  lastMessage : 마지막 메시지
}

- chatroom_message(채팅방 메시지)
{
  _id : MongoDB 고유 ID (PK)
  roomId : 방 ID - chatroom(_id)
  sender : 발신자 nickname
  timestamp : 메시지 발송 시간
}
```

## ✅ 회고
- Websocket,MongoDB,CI/CD를 공부하고 사용해보고 싶어서 만들어본 프로젝트입니다.
더 많은 기능을 구현할 수 있지만 프론트도 같이 작업을 해줘야해서 이정도까지만 하고 마칩니다.
- 고민하고 시간이 걸렸던 부분
  - thymeleaf 에서 SSR 방식을 사용하지 않고 fetch API를 이용해서 데이터를 삽입하는 방식
  - CI/CD workflow 에서 application.yml 의 민감정보를 어떤식으로 넣어야할 지 고민