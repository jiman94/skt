# 최종 타임아웃 설정 추가 

# Stg 

docker ps -a 

docker rm e826a567aba1

docker run -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=20" -p 8080:8080 -p 50000:50000  -v /home/ec2-user/apps/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --restart unless-stopped  --name chicor-jenkins chicor-jenkins:latest

# locale

1. chicor-jenkins  빌드 

cd /home/ec2-user/apps/docker/docker-jenkins 

2. Docker 확인 후 docker build

docker build -t selenium-jenkins .

3. Docker run

docker run -d  -p 80:8080 -p 50001:50000  -v /Users/mz03-jmryu/Downloads/jenkins:/var/jenkins_home --restart unless-stopped --name selenium-jenkins selenium-jenkins 

# 모든 이미지 삭제하기

docker rmi $(docker images -q)


docker run -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=20" -p 8088:8080 -p 50001:50000  -v /Users/mz03-jmryu/Downloads/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --restart unless-stopped abc

---

docker run -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=20" -p 8088:8080 -p 50001:50000  -v /Users/mz03-jmryu/Downloads/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --restart unless-stopped  --name t001 test






docker run -d -p 8080:8080 -p 50000:50000 -v /home/k8sadm/apps/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock --restart unless-stopped --name chicor-jenkins chicor-jenkins:latest


docker run -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=20" -p 8088:8080 -p 50001:50000  -v /Users/mz03-jmryu/Downloads/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --restart unless-stopped  --name t001 test


docker run -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=20" -p 8088:8080 -p 50001:50000  -v /Users/mz03-jmryu/Downloads/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --restart unless-stopped  t aa:latest


docker run -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=20" -p 8088:8080 -p 50001:50000  -v /Users/mz03-jmryu/Downloads/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --restart unless-stopped  --name cc stevemac007/chromedriver-jenkins-agent


docker run -d  -p 8088:8080 -p 50001:50000  -v /Users/mz03-jmryu/Downloads/jenkins:/var/jenkins_home --restart unless-stopped  --name cc abc







docker build https://gitlab.YOUR_DOMAIN/PROJECT_PATH.git -t IMAGE_NAME
docker run -p HOST_MACHINE_PORT:DOCKER_PORT -p 50000:50000 t IMAGE_NAME

docker run -p 8088:8080 -p 50001:50000 t aa


docker run -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=20" -p 8088:8080 -p 50001:50000  -v /Users/mz03-jmryu/Downloads/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --restart unless-stopped  --name t001 test

docker run -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=20" -p 8088:8080 -p 50001:50000  -v /Users/mz03-jmryu/Downloads/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --restart unless-stopped  --name t001 test


/Users/mz03-jmryu/Downloads


RUN apt-get update && apt-get install -y openjdk-8-jdk
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN wget -N http://chromedriver.storage.googleapis.com/2.39/chromedriver_linux64.zip
RUN unzip chromedriver_linux64.zi

RUN wget -N http://chromedriver.storage.googleapis.com/2.39/chromedriver_linux64.zip

checkstyle:latest // pull the latest version of checkstyle
cucumber:0.0.2 // pull a specific version
cucumber-report:3.16.0
configuration-as-code:latest
configuration-as-code-support:latest


docker build https://gitlab.YOUR_DOMAIN/PROJECT_PATH.git -t IMAGE_NAME
docker run -p HOST_MACHINE_PORT:DOCKER_PORT -p 50000:50000 t IMAGE_NAME



Stg

docker build --build-arg JENKINS_VERSION=2.190.2 -t chicor-jenkins .
docker run -d -p 8080:8080 -p 50000:50000  -v /home/ec2-user/apps/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock --restart unless-stopped  --name chicor-jenkins 

api.stg.chicor.co.kr
dualstack.a908619ea605a11eaa0330a4c95c6d28-1300326692.ap-northeast-2.elb.amazonaws.com.




Prod 

/usr/share/zoneinfo/Asia/

sudo ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

-v /etc/localtime:/etc/localtime:ro \
-e TZ=Asia/Seoul \


docker rm e826a567aba1

docker run -d -p 8080:8080 -p 50000:50000  -v /app/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --restart unless-stopped  --name chicor-jenkins chicor-jenkins:latest

docker exec -u root -it 35bd73bfe189 /bin/bash

date

# 최종 타임아웃 설정 추가 


docker ps -a 

docker rm e826a567aba1

docker run -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=20" -p 8080:8080 -p 50000:50000  -v /home/ec2-user/apps/jenkins:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock -v /etc/localtime:/etc/localtime:ro -e TZ=Asia/Seoul --restart unless-stopped  --name chicor-jenkins chicor-jenkins:latest



docker run --restart=always -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=200" --name jenkins -p 8080:8080 -p 50000:50000 --volumes-from jenkins_bak jenkins

chicor-jenkins

docker run --restart=always -d --env JAVA_OPTS="-Dorg.jenkinsci.plugins.gitclient.Git.timeOut=20" --name jenkins -p 8080:8080 -p 50000:50000 --volumes-from jenkins_bak jenkins
