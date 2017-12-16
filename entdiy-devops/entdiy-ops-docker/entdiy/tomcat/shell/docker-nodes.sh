#!/bin/sh

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
echo Start node 8084 at `date`
${SHELL_DIR}/docker-one.sh -p 8084 $1

echo Sleep 30s from `date`
sleep 30s

echo Start node 8085 at `date`
${SHELL_DIR}/docker-one.sh -p 8085 $1
