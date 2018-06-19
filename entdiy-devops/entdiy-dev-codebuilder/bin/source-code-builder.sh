#!/bin/bash

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
echo "Using SHELL_DIR: ${SHELL_DIR}"

echo Rebuild root project...
cd ${SHELL_DIR}/../../..

cd entdiy-core
mvn clean install

cd ../entdiy-devops/entdiy-dev-codebuilder
mvn clean install

echo Generate template files...
cd ${1}
mvn clean compile exec:java -Psource-code-builder -DbasePackages="${2}"