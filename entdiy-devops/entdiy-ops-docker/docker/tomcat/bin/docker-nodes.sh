#!/bin/sh

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
echo Start node 8084 at `date`
${SHELL_DIR}/docker-one.sh -p 8084 $1

seconds=30
echo "Sleep ${seconds}s to wait node startup finish..."
printf "Sleeping ";while(( seconds >0 )); do
  printf .
  ((seconds--))
  sleep 1s
done

echo Start node 8085 at `date`
${SHELL_DIR}/docker-one.sh -p 8085 $1
