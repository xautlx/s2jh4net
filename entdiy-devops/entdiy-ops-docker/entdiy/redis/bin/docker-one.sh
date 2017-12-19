#!/bin/sh

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
BASE_DIR=${SHELL_DIR}/..

app_name="redis"
port="6379"

while getopts p:c: opt
do
  case $opt in
    p)
      port="$OPTARG"
    ;;    
  esac
done
shift $((OPTIND-1))

data_dir=${BASE_DIR}/data
config_dir=${BASE_DIR}/config
log_dir=${BASE_DIR}/logs
docker_name=${app_name}

case "$1" in
    start)
    echo docker run ${docker_name}...
    mkdir -p ${data_dir}; mkdir -p ${config_dir}; mkdir -p ${log_dir}
    docker run --name ${docker_name} -p $port:6379 --restart=always --privileged=true \
                -v $data_dir:/data \
                -e TZ="Asia/Shanghai" \
                -d redis:3.2.11

    echo docker started for $docker_name.
    ;;
    stop)
    cids=$(docker ps -aq --filter "name=$docker_name")
    if [ "$cids" == "" ]; then
       echo "Not running"
    else
       echo docker stop and rm container $docker_name...
       docker stop -t 10 $cids && docker rm $cids
       echo docker stopped for $docker_name.
    fi
    ;;
    restart)
    $0 -p $port stop
    $0 -p $port start
    ;;
    status)
    cids=$(docker ps -aq --filter "name=$docker_name")
    if [ "$cids" == "" ]; then
       echo "Not running"
    else
       docker ps -a --filter "name=$docker_name"
    fi
    ;;
    *)
    echo "Usage: $0 {start|stop|restart|status}"
    exit 1
    ;;
esac
exit 0
