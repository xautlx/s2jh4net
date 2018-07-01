#!/bin/bash

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
echo "Using SHELL_DIR: ${SHELL_DIR}"

echo Rebuild root project...
cd ${SHELL_DIR}/../../..

cd entdiy-core
mvn install
cd ..

cd  entdiy-module/entdiy-module-common
mvn install
cd ../..

cd  entdiy-devops/entdiy-dev-codebuilder
mvn install
cd ../..

echo Generate template files...
cd ${1}
mvn clean compile exec:java -Psource-code-builder -DbasePackages="${2}"