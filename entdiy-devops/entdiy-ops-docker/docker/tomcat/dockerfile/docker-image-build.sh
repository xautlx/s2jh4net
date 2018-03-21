#!/bin/sh

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
IMAGE_TARGET="entdiy/oracle-tomcat:8-jre8"

echo Prepare to build docker image: ${IMAGE_TARGET}
docker build -t="${IMAGE_TARGET}" ${SHELL_DIR}/.