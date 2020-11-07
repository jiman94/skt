#!/usr/bin/env bash

# Update Scouter Ports
# Agent Control and Service Port(Default : TCP 6100)
net_tcp_listen_port=${NET_TCP_LISTEN_PORT:-6100}

# UDP Receive Port(Default : 6100)
net_udp_listen_port=${NET_UDP_LISTEN_PORT:-6101}

# JAVAOPTS
JAVAOPTS="${JAVAOPTS:--Xms512m}"

sed "s/%NET_TCP_LISTEN_PORT%/${net_tcp_listen_port}/g" -i conf/scouter.conf
sed "s/%NET_UDP_LISTEN_PORT%/${net_udp_listen_port}/g" -i conf/scouter.conf


SCOUTER_COLLECTOR_ADDR="127.0.0.1" \
SCOUTER_COLLECTOR_PORT="6100" \
SCOUTER_SERVICE_MAPS_OJB_TYPE="MZ01-JOSM.local:product" \
STORAGE_TYPE="scouter" \
java $JAVAOPTS -classpath ./scouter-server-boot.jar scouter.boot.Boot ./lib &
java -Dloader.path='zipkin-storage-scouter.jar,zipkin-storage-scouter.jar!lib' -Dspring.profiles.active=scouter -cp zipkin.jar org.springframework.boot.loader.PropertiesLauncher
