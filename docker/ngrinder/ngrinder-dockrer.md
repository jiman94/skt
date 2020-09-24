version: '3.5'
services:
  controller:
    image: ngrinder/controller
    restart: always
    ports: 
      - "1000:80"
      - "16001:16001"
      - "12000-12009:12000-12009"
    volumes:
      - ./ngrinder-controller:/opt/ngrinder-controller

  agent:
    image: ngrinder/agent
    restart: always
    links:    
      - controller

# docker run -d -v ~/ngrinder-controller:/opt/ngrinder-controller -p 80:80 -p 16001:16001 -p 12000-12009:12000-12009 ngrinder/controller:3.4      
# docker run -v ~/ngrinder-agent:/opt/ngrinder-agent -d ngrinder/agent:3.4 controller_ip:controller_web_port





Docker로 Redis 설치

Node #1

docker run -d --name redis-master01 --network host -v /redis/master01:/data redis:5.0.5-buster redis-server --port 6379 --cluster-enabled yes --cluster-config-file node.conf --cluster-node-timeout 5000 --bind 0.0.0.0
docker run -d --name redis-slave03 --network host -v /redis/slave03:/data redis:5.0.5-buster redis-server --port 6381 --cluster-enabled yes --cluster-config-file node.conf --cluster-node-timeout 5000 --bind 0.0.0.0 --appendonly yes
Node #2

docker run -d --name redis-master02 --network host -v /redis/master02:/data redis:5.0.5-buster redis-server --port 6380 --cluster-enabled yes --cluster-config-file node.conf --cluster-node-timeout 5000 --bind 0.0.0.0
docker run -d --name redis-slave01 --network host -v /redis/slave01:/data redis:5.0.5-buster redis-server --port 6379 --cluster-enabled yes --cluster-config-file node.conf --cluster-node-timeout 5000 --bind 0.0.0.0 --appendonly yes
Node #3

docker run -d --name redis-master03 --network host -v /redis/master03:/data redis:5.0.5-buster redis-server --port 6381 --cluster-enabled yes --cluster-config-file node.conf --cluster-node-timeout 5000 --bind 0.0.0.0
docker run -d --name redis-slave02 --network host -v /redis/slave02:/data redis:5.0.5-buster redis-server --port 6380 --cluster-enabled yes --cluster-config-file node.conf --cluster-node-timeout 5000 --bind 0.0.0.0 --appendonly yes

