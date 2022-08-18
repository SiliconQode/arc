#!/bin/bash

echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

docker build -t empirilytics/qservice .
docker push empirilytics/qservice:latest