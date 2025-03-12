Web Socket Chatting

## 

### - Skill Stack
- Server
  - Ubuntu 24.04.1
- Language
  - Java 17
- Framework
  - Spring Boot 3.2.2
- DB
  - MongoDB 7.0.17
- CI/CD
  - Github Actions

### - Package Structure
````
.
├── README.md
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
        │                   │   │   ├── ExceptionController.java : @RestControllerAdvice 설정
        │                   │   │   └── ValidationCode.java : 커스텀 http 상태코드
        │                   │   ├── security
        │                   │   │   ├── EncryptionConfig.java : 사용자 패스워드 암호화
        │                   │   │   ├── JwtAuthenticationFilter.java : 허용되지 않은 URI 접근 시 jwt 확인하는 필터
        │                   │   │   ├── JwtProvider.java : jwt 설정(생성, 검증, 저장된 값 확인, 유효기간 확인) 
        │                   │   │   └── SecurityConfig.java : Spring security 설정
        │                   │   └── util
        │                   │       └── CommonUtil.java : util
        │                   ├── controller
        │                   │   ├── ChatServiceController.java : chat rest-controller
        │                   │   └── ChatViewController.java : chat view-controller
        │                   ├── dto
        │                   │   └── ChatServiceRequestDto.java : chat-restcontroller requestdto 
        │                   ├── model
        │                   │   ├── ChatMessage.java : chat_message model
        │                   │   ├── ChatRoom.java : chat_room model
        │                   │   └── Member.java : member model
        │                   ├── repository
        │                   │   └── MemberRepository.java : member repository
        │                   └── service
        │                       ├── ChatService.java 
        │                       └── impl
        │                           └── ChatServiceImpl.java : chat rest-service
        └── resources
            ├── application.yml
            ├── static
            │   └── css
            │       ├── chat.css
            │       ├── index.css
            │       ├── login.css
            │       └── register.css
            └── templates
                ├── chat.html
                ├── error.html
                ├── index.html
                ├── login.html
                └── register.html
````




### - Github Actions 설정
1. workflow 파일 생성
````
프로젝트에 다음 경로로 파일 생성 : ./github/workflows.[workflow 파일명].yml
````

2. Github secrets 설정
````
Settings > Secrets and variables > Actions에서 비밀 변수(Secrets)를 설정
````

3. secrets 안에 서버접근 키, 서버 호스트, 서버 유저명 셋팅
````
SERVER_PEM_KEY : pem key value
SERVER_HOST : server public ip
SERVER_USER : server ssh name
````

4. 위 세 개의 값을 이용하여 workflow 작성


### - MongoDB 설치 방법(Ubuntu)
1. ec2 public ip 주소를 얻은 뒤 ssh로 접근
````
ssh -i [/path/파일명].pem ubuntu@[EC2 퍼블릭 IP]
````
2. MongoDB 설치

서버 업데이트 및 필수 패키지 설치
````
sudo apt update
sudo apt upgrade -y
sudo apt install -y gnupg curl
````

MongoDB 공식 GPG 키 추가
````
curl -fsSL https://www.mongodb.org/static/pgp/server-7.0.asc | sudo gpg --dearmor -o /usr/share/keyrings/mongodb-server-7.0.gpg
````

MongoDB 저장소 추가
````
echo "deb [ arch=amd64,arm64 signed-by=/usr/share/keyrings/mongodb-server-7.0.gpg ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/7.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list
````

패키지 목록 업데이트 및 MongoDB 설치
````
sudo apt update
sudo apt install -y mongodb-org
````

3. MongoDB 시작 및 활성화

````
서비스 시작 : sudo systemctl start mongod
부팅 시 자동 실행 : sudo systemctl eable mongod
상태 확인 : sudo systemctl status mongod (active 상태면 성공)
````

### - Feature
웹소켓 적용 로직
사용자 채팅방 입장 -> 사용자 웹소캣 생성 -> 채팅방에서는 대화내용 저장 -> 채팅방 나가면 웹소캣 close 