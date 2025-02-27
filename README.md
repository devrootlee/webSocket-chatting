Web Socket Chatting

패키지 구조
````
````

웹 소캣 관련 설정파일



## github Actions를 이용한 자동 배포


## ec2 서버에 nginx통해서 서버 실행

## MongoDB 설치 (Amazon Linux 2)
1. ec2 public ip 주소를 얻은 뒤 ssh로 접근
````
ssh -i [/path/to/your-key].pem ec2-user@[EC2 퍼블릭 IP]
````
2. MongoDB 설치

amazon linux2는 기본 yum 저장소에 mongodb가 없어서 공식 mongodb 저장소를 추가

MongoDB 저장소 설정 파일 생성

````
sudo vi /etc/yum.repos.d/mongodb-org-5.0.repo

[mongodb-org-5.0]
name=MongoDB 5.0 Repository
baseurl=https://repo.mongodb.org/yum/amazon/2/mongodb-org/5.0/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-5.0.asc
````
작성 후 저장하고 나가기: :wq

MongoDB 설치
````
sudo yum install -y mongodb-org
````
3. MongoDB 시작 및 활성화

````
서비스 시작 : sudo systemctl start mongod
부팅 시 자동 실행 : sudo systemctl eable mongod
상태 확인 : sudo systemctl status mongod (active 상태면 성공)
````
