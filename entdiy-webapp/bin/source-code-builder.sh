#!/bin/bash

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
echo "Using SHELL_DIR: ${SHELL_DIR}"
RUN_DIR=`pwd`
echo "Using RUN_DIR: ${RUN_DIR}"

echo Rebuild codebuilder...
cd ${SHELL_DIR}/../../entdiy-devops/entdiy-dev-codebuilder
mvn clean install

echo Generate template files...
cd ${SHELL_DIR}/..
mvn clean compile exec:java -Psource-code-builder -DbasePackages="xyz.entdiy"
cd ${RUN_DIR}