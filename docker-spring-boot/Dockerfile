FROM openjdk:13-jdk-alpine

#docker run -it -p8080:8080 demo:0.0.1-SNAPSHOT
# docker build . --tag demo
# docker run -it -p8080:8080 demo:latest
#FROM openjdk:8-jdk-alpine
#EXPOSE 8080
#ARG JAR_FILE=target/my-application.jar
#ADD ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

#FROM openjdk:8-jdk-alpine

#VOLUME /tmp
#ADD RestAPI-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

# usermod -aG docker <user> 


# Refer to Maven build -> finalName
ARG JAR_FILE=target/spring-boot-web.jar

# cd /opt/app
WORKDIR /opt/app

# cp target/spring-boot-web.jar /opt/app/app.jar
COPY ${JAR_FILE} app.jar

# java -jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]

## sudo docker run -p 8080:8080 -t docker-spring-boot:1.0
## sudo docker run -p 80:8080 -t docker-spring-boot:1.0
## sudo docker run -p 443:8443 -t docker-spring-boot:1.0dd
