#!/bin/sh
docker stop gateway
docker rm -f gateway && docker build "$@" -t gateway . && \
docker run -it -d --name gateway --cpus="2"  --memory="1g" --memory-reservation="1g"  --network tgb --restart always --dns 8.8.8.8 -p 8085:8080 -p 127.0.0.1:5009:5005 -v /home/tgb/docker/data/gateway:/root/tgb gateway