mkdir -p /Users/mz03-jmryu/Project/nexus-data

docker run --name nexus -d -p 12000:12000 -p 8081:8081 -v /Users/mz03-jmryu/Project/nexus-data:/nexus-data -u root sonatype/nexus3

docker start nexus 

cat /Users/mz03-jmryu/Project/nexus-data/admin.password
5e665afc-d472-42c8-bd73-d93fddc542e6% 

daemon.json
{
    "insecure-registries" : ["localhost:12000"]
}


netstat -an | grep 12000 | grep LIS

➜  ~ docker login localhost:12000
Username: admin
Password: 
Login Succeeded

➜  .docker cat daemon.json
{
  "debug" : true,
  "experimental" : false,
  "insecure-registries" : ["localhost:12000"]
}
➜  .docker pwd
/Users/mz03-jmryu/.docker
➜  .docker 




➜  .docker docker images -a
REPOSITORY                    TAG                 IMAGE ID            CREATED             SIZE
sonatype/nexus3               latest              70b481d1fca4        43 hours ago        632MB
selenium/node-firefox-debug   latest              99c91a9bdf99        12 days ago         887MB
selenium/node-chrome-debug    latest              04414d01df54        12 days ago         947MB
selenium/node-firefox         latest              92e71f8fb8c5        12 days ago         856MB
selenium/node-chrome          latest              1433019155fc        12 days ago         916MB
selenium/hub                  latest              0be610f7c224        12 days ago         263MB
➜  .docker docker push localhost:12000/selenium/node-chrome:latest
The push refers to repository [localhost:12000/selenium/node-chrome]
An image does not exist locally with the tag: localhost:12000/selenium/node-chrome
➜  .docker 
➜  .docker docker tag 1433019155fc localhost:12000/selenium/node-chrome:v20200810
➜  .docker docker push localhost:12000/selenium/node-chrome:v20200810
The push refers to repository [localhost:12000/selenium/node-chrome]
acfd2cc77832: Pushed 
8c9420685375: Pushed 
a11968c4e76d: Pushed 
378bfd004a81: Pushed 
29c44f736913: Pushed 
304d6f0e73c7: Pushed 
ed53e55ed6ba: Pushed 
f5431972c6b1: Pushed 
c6fcfef80957: Pushed 
18b2b7f24fb3: Pushed 
795cf48c75e6: Pushed 
48de39ce0ccc: Pushed 
d33162831a82: Pushed 
86b425a1c11d: Pushed 
4cf7057ab9b1: Pushed 
74fe6c18eb30: Pushed 
0ce51e7d4537: Pushed 
094354de3692: Pushed 
d239d3852e17: Pushed 
e0b3afb09dc3: Pushed 
6c01b5a53aac: Pushed 
2c6ac8e5063e: Pushed 
cc967c529ced: Pushed 
v20200810: digest: sha256:870610b017d23b8bc6f3667690a4925b4b3eebf6e63a33f78a5cb5ed846314b3 size: 5125
➜  .docker 


➜  .docker docker pull localhost:12000/selenium/node-chrome:v20200810
v20200810: Pulling from selenium/node-chrome
Digest: sha256:870610b017d23b8bc6f3667690a4925b4b3eebf6e63a33f78a5cb5ed846314b3
Status: Image is up to date for localhost:12000/selenium/node-chrome:v20200810
localhost:12000/selenium/node-chrome:v20200810
➜  .docker 


➜  .docker docker info
Client:
 Debug Mode: false

Server:
 Containers: 4
  Running: 1
  Paused: 0
  Stopped: 3
 Images: 6
 Server Version: 19.03.12
 Storage Driver: overlay2
  Backing Filesystem: extfs
  Supports d_type: true
  Native Overlay Diff: true
 Logging Driver: json-file
 Cgroup Driver: cgroupfs
 Plugins:
  Volume: local
  Network: bridge host ipvlan macvlan null overlay
  Log: awslogs fluentd gcplogs gelf journald json-file local logentries splunk syslog
 Swarm: inactive
 Runtimes: runc
 Default Runtime: runc
 Init Binary: docker-init
 containerd version: 7ad184331fa3e55e52b890ea95e65ba581ae3429
 runc version: dc9208a3303feef5b3839f4323d9beb36df0a9dd
 init version: fec3683
 Security Options:
  seccomp
   Profile: default
 Kernel Version: 4.19.76-linuxkit
 Operating System: Docker Desktop
 OSType: linux
 Architecture: x86_64
 CPUs: 6
 Total Memory: 1.944GiB
 Name: docker-desktop
 ID: ABQ3:F5YJ:IN2V:5Q76:HAXD:LSAN:KDTV:J4V4:JMTG:47ST:XCYJ:37WT
 Docker Root Dir: /var/lib/docker
 Debug Mode: true
  File Descriptors: 47
  Goroutines: 58
  System Time: 2020-08-12T13:36:22.899004115Z
  EventsListeners: 4
 HTTP Proxy: gateway.docker.internal:3128
 HTTPS Proxy: gateway.docker.internal:3129
 Registry: https://index.docker.io/v1/
 Labels:
 Experimental: false
 Insecure Registries:
  localhost:12000
  127.0.0.0/8
 Live Restore Enabled: false
 Product License: Community Engine

➜  .docker 

docker login -u admin -p admin http://localhost:12000


# [Docker] 실행 중이지 않은 Container 삭제하기
docker rm $( docker ps -f status=exited -q )


OSX

vi /Users/mz02-jmryu/.docker/config.json

vi /Users/mz02-jmryu/.docker/daemon.json

{
    "debug" : true,
    "experimental" : true, 
    "insecure-registries": ["localhost:12000"]
}


