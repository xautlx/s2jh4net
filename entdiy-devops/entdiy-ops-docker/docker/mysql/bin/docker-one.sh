#!/bin/sh

MYSQL_PASSWD="mysqlP@sswd123"

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
BASE_DIR=${SHELL_DIR}/..

app_name="mysql"
port="3306"

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
    docker run --name ${docker_name} -p $port:3306 --restart=always --privileged=true \
                -v $data_dir:/var/lib/mysql \
                -v $config_dir:/etc/entdiy/config \
                -e MYSQL_ROOT_PASSWORD=${MYSQL_PASSWD} \
                -e TZ="Asia/Shanghai" \
                -d mysql:5.7.20

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
    *)
    echo "Usage: $0 {start|stop|restart|init|status}"
    exit 1
    ;;
esac
exit 0
