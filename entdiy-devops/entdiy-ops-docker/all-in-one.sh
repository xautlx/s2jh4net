#!/bin/bash

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
echo "Using SHELL_DIR: ${SHELL_DIR}"
RUN_DIR=`PWD`
echo "Using RUN_DIR: ${RUN_DIR}"
ROOT_DIR=${SHELL_DIR}/../..
echo "Using ROOT_DIR: ${ROOT_DIR}"

port="8080"

while getopts p:c: opt
do
  case $opt in
    p)
      port="$OPTARG"
    ;;
  esac
done
shift $((OPTIND-1))

echo "Using PORT: ${port}"

echo Invoke maven to build all projects...
chmod +x ${SHELL_DIR}/tools/maven/bin/mvn
cd  ${ROOT_DIR}
${SHELL_DIR}/tools/maven/bin/mvn clean install

echo Copy build war to docker war dir...
mkdir -p ${SHELL_DIR}/entdiy/tomcat/war
\cp -fr ${ROOT_DIR}/entdiy-webapp/target/entdiy.war ${SHELL_DIR}/entdiy/tomcat/war/.

echo Deploy webapp to docker...
chmod +x ${SHELL_DIR}/entdiy/tomcat/shell/*.sh
${SHELL_DIR}/entdiy/tomcat/shell/docker-one.sh -p ${port} deploy

cd  ${RUN_DIR}