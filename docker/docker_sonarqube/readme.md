sysctl -w vm.max_map_count=262144
sysctl -w fs.file-max=65536
ulimit -n 65536
ulimit -u 4096

SonarQube7.9 사전 설치 제약 조건

2.2 본격설치
1) 호스트 <-> 컨테이너 퍼머넌트(permanent) 생성

mkdir -p ./sonarqube/conf
mkdir -p ./sonarqube/data
mkdir -p ./sonarqube/logs
mkdir -p ./sonarqube/extensions
mkdir -p ./sonarqube/postgres
chmod 777 ./sonarqube -R

2) docker-compose.yml 생성

$ vi /work/sonarqube/docker-compose.yml

version: "3.1"
services:
  sonarqube:
    image: sonarqube:7.9.1-community
    container_name: sonarqube7.9
    ports:
      - "9000:9000"
      - "9092:9092"
    networks:
      - sonarnet
    environment:
      - SONARQUBE_HOME=/opt/sonarqube
      - SONARQUBE_JDBC_USERNAME=sonar
      - SONARQUBE_JDBC_PASSWORD=sonar
      - SONARQUBE_JDBC_URL=jdbc:postgresql://db:5432/sonar
    volumes:
      - /app/sonarqube/conf:/opt/sonarqube/conf
      - /app/sonarqube/data:/opt/sonarqube/data
      - /app/sonarqube/logs:/opt/sonarqube/logs
      - /app/sonarqube/extensions:/opt/sonarqube/extensions
 
  db:
    image: postgres
    container_name: postgres
    networks:
      - sonarnet
    environment:
      - POSTGRES_USER=sonar
      - POSTGRES_PASSWORD=sonar
    volumes:
      - /app/sonarqube/postgres:/var/lib/postgresql/data
 
networks:
  sonarnet:
    driver: bridge

3) SonarQube 기동

$ cd /work/sonarqube
$ docker-compose up -d


$ docker-compose up -d