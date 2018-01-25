#!/bin/bash

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
echo "Using SHELL_DIR: ${SHELL_DIR}"
RUN_DIR=`pwd`
echo "Using RUN_DIR: ${RUN_DIR}"

# 修改当前项目模块包前缀
basePackages=com.entdiy

# 注意根据实际目录层次结构做必要修改指向entdiy-dev-codebuilder项目目录
cd ${SHELL_DIR}/../../../entdiy-devops/entdiy-dev-codebuilder/bin

./source-code-builder.sh ${SHELL_DIR}/.. ${basePackages}