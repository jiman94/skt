# Docker를 이용한 Jenkins 컨테이너 만들기(docker in docker)

1. Docker관리를 위한 폴더와 파일 구조

docker (도커 관리 폴더)
|-- jenkins_with_docker (도커를 포함한 젠킨슨 관리 폴더)
  |-- jenkins_home      (젠킨스 컨테이너의 볼륨 연결용 폴더, 빈 폴더 준비)
  |-- docker-compose.yml  (실제 컨테이너를 생성하는 docker-compose 파일)
  |-- Dockerfile         (docker-compose.yml에서 빌드할 jenkins 파일)
  |-- docker_install.sh   (Dockerfile에서 호출할 docker 설치 스크립트 파일)


2. 설정 파일들 준비

# Dockerfile
FROM jenkins/jenkins:lts

USER root 

COPY docker_install.sh /docker_install.sh

RUN chmod +x /docker_install.sh

RUN /docker_install.sh

# docker_install.sh

apt-get update && \
apt-get -y install apt-transport-https \
     ca-certificates \
     curl \
     gnupg2 \
     zip \
     unzip \
     software-properties-common && \
curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg > /tmp/dkey; apt-key add /tmp/dkey && \
add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
   $(lsb_release -cs) \
   stable" && \
apt-get update && \
apt-get -y install docker-ce

내용은 jenkins 내부에 docker를 설치하는 것이다.
jdk, maven, gradle, node등 필요한게 있다면 이런식으로 추가



# docker-compose.yml

version: '3'

services:
  jenkins:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: 'jenkins_docker'
    restart: always
    ports:
      - '8200:8080'
      - '50200:50000'
    expose:
      - '8080'
      - '50000'
    volumes:
      - './jenkins_home:/var/jenkins_home'
      - '/var/run/docker.sock:/var/run/docker.sock'
    environment:
      TZ: "Asia/Seoul"
networks:
  default:
    external:
      name: devops

3. 컨테이너 생성

sudo docker network create devops

sudo docker-compose up -d


4. jenkins 컨테이너에 접속해서 docker 명령어 테스트
http://localhost:8200


