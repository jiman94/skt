#!/bin/bash

source ../config

docker build -t chicor/scouter-server:v${SCOUTER_VERSION} .
docker tag chicor/scouter-server:v${SCOUTER_VERSION}  chicor/scouter-server:latest
docker push chicor/scouter-server:latest 

