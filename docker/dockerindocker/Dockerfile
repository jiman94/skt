FROM jenkins/jenkins:jdk11

# Changing the user to root
USER root
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# installing chrome driver
RUN apt-get update && apt-get -qq -y install curl

# Update the package list and install chrome
RUN apt-get update -y

# open jdk 13
RUN curl -O https://download.java.net/java/GA/jdk13/5b8a42f3905b406298b72d750b6919f6/33/GPL/openjdk-13_linux-x64_bin.tar.gz
RUN tar xvf openjdk-13_linux-x64_bin.tar.gz
RUN mv /opt/java/openjdk /opt/java/openjdk_backup
RUN mv jdk-13 /opt/java/openjdk

# Setup JAVA_HOME, this is useful for docker commandline
#ENV JAVA_HOME /opt/java/openjdk 
#RUN export JAVA_HOME

# Install maven
RUN apt-get -y update && apt-get install -y maven

# Install gradle
RUN apt-get -y update && apt-get install -y gradle

COPY docker_install.sh /docker_install.sh
RUN chmod +x /docker_install.sh
RUN /docker_install.sh

# FROM openjdk:13-jdk

#RUN usermod -aG docker jenkins
#USER jenkins

# USER jenkins