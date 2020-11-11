FROM jenkins/jenkins:latest

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

RUN apt install vim
# Install maven
#RUN apt-get -y update && apt-get install -y maven

# Install gradle
RUN apt-get -y update && apt-get install -y gradle

RUN add-apt-repository ppa:cwchien/gradle
RUN apt -y install gradle
# gradle -v 

# RUN VERSION=6.5.1
# RUN wget https://services.gradle.org/distributions/gradle-${VERSION}-bin.zip -P /tmp
# RUN unzip -d /opt/gradle /tmp/gradle-${VERSION}-bin.zip
# RUN ln -s /opt/gradle/gradle-${VERSION} /opt/gradle/latest


# RUN apt install openjdk-11-jdk

COPY docker_install.sh /docker_install.sh
RUN chmod +x /docker_install.sh
RUN /docker_install.sh

#RUN usermod -aG docker jenkins
#USER jenkins

# USER jenkins