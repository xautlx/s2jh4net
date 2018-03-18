#!/bin/sh

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
BASE_DIR=${SHELL_DIR}/..

app_name="nginx"

config_dir=${BASE_DIR}/config
log_dir=${BASE_DIR}/logs
docker_name=${app_name}

case "$1" in
    start)
    echo docker run ${docker_name}...
    mkdir -p ${config_dir}; mkdir -p ${log_dir}

    docker run --name ${docker_name} -p 80:80 --restart=always --privileged=true \
                --link entdiy-8084:tomcat01 \
                --link entdiy-8085:tomcat02 \
                -v ${log_dir}:/var/log/nginx \
                -v ${config_dir}/nginx-proxy.conf:/etc/nginx/conf.d/default.conf \
                -e TZ="Asia/Shanghai" \
                -d nginx:1.12

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
    $0 stop
    $0 start
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
