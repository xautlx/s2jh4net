#!/bin/bash

basePackages=com.entdiy

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
echo "Using SHELL_DIR: ${SHELL_DIR}"
RUN_DIR=`pwd`
echo "Using RUN_DIR: ${RUN_DIR}"

cd ${SHELL_DIR}/../../../entdiy-devops/entdiy-dev-codebuilder/bin
./source-code-builder.sh ${SHELL_DIR}/.. ${basePackages}

cd ${RUN_DIR}