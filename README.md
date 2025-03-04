Web Socket Chatting

## 

### - Skill Stack
- Server
  - Ubuntu 24.04.1
- DB
  - MongoDB 7.0.17
- CI / CD
  - Github Actions
- Language
  - Java 17

### - 패키지 구조
````
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

### - nginx

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