#!/usr/bin/env bash

SVC_NAME="mixtape"

kill_it() {
    echo "============== Stopping ================"
    docker rm -f ${SVC_NAME}
}

# Kill anything running anyway
kill_it

# Find the image
IMAGE=$(docker images | grep ${SVC_NAME} | head -n 1 | awk '{printf "%s:%s",$1,$2}')
[ -z "$IMAGE" ] && echo "No $SVC_NAME image found" && exit 1

DIR=$(cd "$(dirname "$0")"; pwd -P)

# RUNIT
docker run  \
    -v ${DIR}/local/data:/app/data \
    --name=$SVC_NAME \
    ${IMAGE}

echo "============== Running ================"
echo "===== Program execution finished ======"

