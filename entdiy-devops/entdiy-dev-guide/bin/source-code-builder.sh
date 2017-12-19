#!/bin/bash

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
echo "Using SHELL_DIR: ${SHELL_DIR}"
RUN_DIR=`pwd`
echo "Using RUN_DIR: ${RUN_DIR}"

cd ${SHELL_DIR}/..
mvn clean compile exec:java -Psource-code-builder -DbasePackages="com.entdiy.dev"
cd ${RUN_DIR}