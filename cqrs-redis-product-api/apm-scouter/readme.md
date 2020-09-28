

docker run -it --name scouter-server -p 6100:6100/tcp -p 6101:6101/udp chicor/scouter-server

docker run -it --link scouter-server chicor/scouter-host-agent



