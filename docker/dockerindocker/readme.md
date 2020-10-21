
http://security.debian.org/debian-security
http://deb.debian.org/debian
https://packagecloud.io/github/git-lfs/debian str

systemctl status firewalld.service

docker system prune -a

systemctl start firewalld

systemctl stop firewalld

# service firewalld stop
# systemctl stop firewalld

systemctl disable firewalld
# service firewalld start
# systemctl start firewalld
# systemctl enable firewalld



ssh vmadmin@20.194.7.131
vmadmin@20.194.7.131's password: 


VMadmin123!@#


docker network create devops


docker pull jenkins/jenkins:jdk11


docker volume ls


[1] bootstrap checks failed
[1]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]

sudo vi /etc/sysctl.conf
vm.max_map_count=262144

# 확인
sudo sysctl -p
 



ssh vmadmin@20.194.7.131
VMadmin123!@#


[vmadmin@AZskeccpjenkinssonaqube01 docker_jenkins]$ df -m
Filesystem                1M-blocks  Used Available Use% Mounted on
devtmpfs                       3889     0      3889   0% /dev
tmpfs                          3906     0      3906   0% /dev/shm
tmpfs                          3906     9      3897   1% /run
tmpfs                          3906     0      3906   0% /sys/fs/cgroup
/dev/mapper/rootvg-rootlv      2038   317      1722  16% /
/dev/mapper/rootvg-usrlv      10230  1778      8453  18% /usr
/dev/sdb1                       496    93       403  19% /boot
/dev/mapper/rootvg-tmplv       2038    47      1992   3% /tmp
/dev/sdb15                      495     7       488   2% /boot/efi
/dev/mapper/rootvg-varlv       8182  3359      4824  42% /var
/dev/mapper/rootvg-homelv      1014  1014         1 100% /home
/dev/sda1                     16061    45     15182   1% /mnt
tmpfs                           782     0       782   0% /run/user/1000
[vmadmin@AZskeccpjenkinssonaqube01 docker_jenkins]$ 




[root@AZskeccpjenkinssonaqube01 ~]# grep . /etc/*-release
/etc/os-release:NAME="Red Hat Enterprise Linux"
/etc/os-release:VERSION="8.2 (Ootpa)"
/etc/os-release:ID="rhel"
/etc/os-release:ID_LIKE="fedora"
/etc/os-release:VERSION_ID="8.2"
/etc/os-release:PLATFORM_ID="platform:el8"
/etc/os-release:PRETTY_NAME="Red Hat Enterprise Linux 8.2 (Ootpa)"
/etc/os-release:ANSI_COLOR="0;31"
/etc/os-release:CPE_NAME="cpe:/o:redhat:enterprise_linux:8.2:GA"
/etc/os-release:HOME_URL="https://www.redhat.com/"
/etc/os-release:BUG_REPORT_URL="https://bugzilla.redhat.com/"
/etc/os-release:REDHAT_BUGZILLA_PRODUCT="Red Hat Enterprise Linux 8"
/etc/os-release:REDHAT_BUGZILLA_PRODUCT_VERSION=8.2
/etc/os-release:REDHAT_SUPPORT_PRODUCT="Red Hat Enterprise Linux"
/etc/os-release:REDHAT_SUPPORT_PRODUCT_VERSION="8.2"
/etc/redhat-release:Red Hat Enterprise Linux release 8.2 (Ootpa)
/etc/system-release:Red Hat Enterprise Linux release 8.2 (Ootpa)
[root@AZskeccpjenkinssonaqube01 ~]# 


2. daemon.json 파일 작성하기


# Docker 재시작 하기



systemctl stop firewalld


sudo vi  /etc/docker/daemon.json


{
    "dns": ["1.1.1.1", "8.8.8.8"]
}

sudo systemctl daemon-reload
sudo systemctl restart docker

FROM jenkins/jenkins:jdk11

sudo apt-get install openjdk-13-jdk



http://20.194.7.131:8080/login?from=%2F



docker -v
docker-compose -v




sudo useradd jenkins
sudo usermod -aG docker jenkins


# Jenins Plugin 	

Azure 
Docker
CloudBees 
Publish Over SSH
SonarQubeScanner 
Kubernetes



# docker exec -it -uroot jenkins_docker /bin/bash
root@f30923330bb1:/# grrep . /etc/*-release
bash: grrep: command not found
root@f30923330bb1:/# grep . /etc/*-release
PRETTY_NAME="Debian GNU/Linux 10 (buster)"
NAME="Debian GNU/Linux"
VERSION_ID="10"
VERSION="10 (buster)"
VERSION_CODENAME=buster
ID=debian
HOME_URL="https://www.debian.org/"
SUPPORT_URL="https://www.debian.org/support"
BUG_REPORT_URL="https://bugs.debian.org/"

root@f30923330bb1:/# cd /usr/lib/jvm/

root@f30923330bb1:/usr/lib/jvm# ls
default-java  java-1.11.0-openjdk-a



# Installing JDK on Debian 10

sudo apt install openjdk-13-jdk

sudo apt install openjdk-11-jdk-headless


docker system prune -a



FROM mysql:8.0.17
FROM nginx:1.17.4
FROM openjdk:13-jdk

# 

ssh vmadmin@20.194.7.131
vmadmin@20.194.7.131's password: 

VMadmin123!@#



# jenkins disk 
Total reclaimed space: 3.285GB


# jdk 11 -> jdk15
chown -R vmadmin:vmadmin jenkins_home




FROM jenkins/jenkins:jdk11


curl -O https://download.java.net/java/GA/jdk13/5b8a42f3905b406298b72d750b6919f6/33/GPL/openjdk-13_linux-x64_bin.tar.gz
tar xvf openjdk-13_linux-x64_bin.tar.gz

mv /opt/java/openjdk /opt/java/openjdk_backup
mv jdk-13 /opt/java/openjdk

root@ac9a390ba040:/opt/java# java -version
openjdk version "13" 2019-09-17
OpenJDK Runtime Environment (build 13+33)
OpenJDK 64-Bit Server VM (build 13+33, mixed mode, sharing)
root@ac9a390ba040:/opt/java# 


root@ac9a390ba040:/opt/java/openjdk/bin# ls
jaotc  jarsigner  javac    javap  jconsole  jdeprscan  jfr    jimage  jjs    jmap  jps	       jshell  jstat   keytool	rmic  rmiregistry  unpack200
jar    java	  javadoc  jcmd   jdb	    jdeps      jhsdb  jinfo   jlink  jmod  jrunscript  jstack  jstatd  pack200	rmid  serialver
root@ac9a390ba040:/opt/java/openjdk/bin# java -version
openjdk version "11.0.8" 2020-07-14
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.8+10)
OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.8+10, mixed mode)
root@ac9a390ba040:/opt/java/openjdk/bin# 
root@ac9a390ba040:/opt/java/openjdk/bin# pwd
/opt/java/openjdk/bin
root@ac9a390ba040:/opt/java/openjdk/bin# 

