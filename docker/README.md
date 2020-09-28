# kafka-stack-docker-compose

## Stack version

# Requirements

## Docker

```
export DOCKER_HOST_IP=127.0.0.1
```

## Multiple Zookeeper / Multiple Kafka

zookeeper : 3
kafka : 3



```
docker-compose -f kafka-multiple.yml up
docker-compose -f kafka-multiple.yml down
```

## Full stack

 - Single Zookeeper: `$DOCKER_HOST_IP:2181`
 - Single Kafka: `$DOCKER_HOST_IP:9092`
 - Kafka Schema Registry: `$DOCKER_HOST_IP:8081`
 - Kafka Schema Registry UI: `$DOCKER_HOST_IP:8001`
 - Kafka Rest Proxy: `$DOCKER_HOST_IP:8082`
 - Kafka Topics UI: `$DOCKER_HOST_IP:8000`
 - Kafka Connect: `$DOCKER_HOST_IP:8083`
 - Kafka Connect UI: `$DOCKER_HOST_IP:8003`
 - KSQL Server: `$DOCKER_HOST_IP:8088`
 - Zoonavigator Web: `$DOCKER_HOST_IP:8004`

 ```
 docker-compose -f full-stack.yml up
 docker-compose -f full-stack.yml down
 ```
