#!/bin/bash

source ../config

docker build -t chicor/scouter-host-agent:v${SCOUTER_VERSION} .
docker tag chicor/scouter-host-agent:v${SCOUTER_VERSION}  chicor/scouter-host-agent:latest
docker push chicor/scouter-host-agent:latest 

