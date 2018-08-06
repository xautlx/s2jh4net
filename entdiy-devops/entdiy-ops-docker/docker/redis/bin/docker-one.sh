#!/bin/sh

REDIS_PASSWD="redisP@sswd123"

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
BASE_DIR="$( cd "$SHELL_DIR/.." && pwd  )"

APP_NAME="entdiy-redis"
PORT="6379"

while getopts p:c: opt
do
  case $opt in
    p)
      PORT="$OPTARG"
    ;;    
  esac
done
shift $((OPTIND-1))

DATA_DIR=${BASE_DIR}/data
CONFIG_DIR=${BASE_DIR}/config
LOG_DIR=${BASE_DIR}/logs
DOCKER_NAME=${APP_NAME}

case "$1" in
    start)
    echo docker run ${DOCKER_NAME}...
    mkdir -p ${DATA_DIR}; mkdir -p ${CONFIG_DIR}; mkdir -p ${LOG_DIR}
    docker run --name ${DOCKER_NAME} -p $PORT:6379 --restart=always --privileged=true \
                -v ${DATA_DIR}:/data \
                -e TZ="Asia/Shanghai" \
                -d redis:3.2.11 redis-server --requirepass "$REDIS_PASSWD"

    echo docker started for $DOCKER_NAME.
    ;;
    stop)
    cids=$(docker ps -aq --filter "name=$DOCKER_NAME")
    if [ "$cids" == "" ]; then
       echo "Not running"
    else
       echo docker stop and rm container $DOCKER_NAME...
       docker stop -t 10 $cids && docker rm $cids
       echo docker stopped for $DOCKER_NAME.
    fi
    ;;
    restart)
    $0 -p $PORT stop
    $0 -p $PORT start
    ;;
    status)
    cids=$(docker ps -aq --filter "name=$DOCKER_NAME")
    if [ "$cids" == "" ]; then
       echo "Not running"
    else
       docker ps -a --filter "name=$DOCKER_NAME"
    fi
    ;;
    *)
    echo "Usage: $0 {start|stop|restart|status}"
    exit 1
    ;;
esac
exit 0
