#!/bin/bash

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
echo "Using SHELL_DIR: ${SHELL_DIR}"

echo Rebuild codebuilder...
cd ${SHELL_DIR}/..
mvn clean install

echo Generate template files...
cd ${1}
mvn clean compile exec:java -Psource-code-builder -DbasePackages="${2}"