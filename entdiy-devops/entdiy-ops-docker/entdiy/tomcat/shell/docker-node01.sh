#!/bin/bash

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
${SHELL_DIR}/docker-deploy.sh -p 8084 $1
