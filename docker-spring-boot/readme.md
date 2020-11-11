
#### API server : docker 

* OS 버전 : 확인 필요
* 스펙 : cloud 스펙
* port : 2345/4567 port 사용 방화벽 open (security server/manager PC 와 port 통신)

```basy 
/opt/app # grep . /etc/*-release
/etc/alpine-release:3.10.1
/etc/os-release:NAME="Alpine Linux"
/etc/os-release:ID=alpine
/etc/os-release:VERSION_ID=3.10.1
/etc/os-release:PRETTY_NAME="Alpine Linux v3.10"
/etc/os-release:HOME_URL="https://alpinelinux.org/"
/etc/os-release:BUG_REPORT_URL="https://bugs.alpinelinux.org/"
/opt/app # 
```
 

#### security server : key 관리 서버 

* OS 버전 : 확인 필요
* 스펙 : 최소 스펙 (1CPU/2 GB Memory/1G Disk)
* port : 4567 port 사용 방화벽 open (API server/manager PC 와 port 통신)

```bash  
[vmadmin@AZskeccpcubeonkeymng01 ~]$ grep . /etc/*-release
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
```

#### manager PC
* OS 버전 : 확인 필요
* 스펙 : windows7 64bit 이상 최소 스펙 (1CPU/1G Disk)
* port : 2345/4567 port 사용 방화벽 open (API server/security server 와 port 통신)


```bash
VDI 
windows 10

```

$ git clone https://github.com/mkyong/docker-java
$ cd docker-spring-boot
$ mvn clean package
$ java -jar target/spring-boot-web.jar

$ sudo docker build -t spring-boot:1.0 .
$ sudo docker run -d -p 8080:8080 -t spring-boot:1.0
