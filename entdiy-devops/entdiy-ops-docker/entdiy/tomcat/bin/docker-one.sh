#!/bin/sh

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
BASE_DIR=${SHELL_DIR}/..

app_name="entdiy"
port="8080"

while getopts p: opt
do
  case $opt in
    p)
      port="$OPTARG"
    ;;    
  esac
done
shift $((OPTIND-1))

app_dir=${BASE_DIR}/nodes/${port}/webapp
war_dir=${BASE_DIR}/war
data_dir=${BASE_DIR}/data
config_dir=${BASE_DIR}/config
log_dir=${BASE_DIR}/nodes/${port}/logs
docker_name=${app_name}-${port}

case "$1" in
    start)
    echo docker run ${docker_name}...
    mkdir -p ${app_dir} ; mkdir -p ${data_dir}; mkdir -p ${config_dir}; mkdir -p ${log_dir}
    deploy_dir="/usr/local/tomcat/webapps"
    docker run --name ${docker_name} -p $port:8080 --restart=always --privileged=true \
               --link redis:redis-server \
               --link mysql:mysql-server \
                -v $app_dir:$deploy_dir \
                -v $log_dir:/usr/local/tomcat/logs \
                -v $config_dir:/etc/entdiy/config \
                -v $data_dir:/etc/entdiy/data \
                -v $config_dir/tomcat/server.xml:/usr/local/tomcat/conf/server.xml \
                -e TZ="Asia/Shanghai" \
                -e JAVA_OPTS="-Dspring.profiles.active=production" \
                -e CATALINA_OPTS="-Xms256m -Xmx4096m -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=utf-8" \
                -d openweb/oracle-tomcat:8-jre8

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
    deploy)
    $0 -p $port stop
    rm -fr ${app_dir}
    mkdir -p ${app_dir}
    cp -v ${war_dir}/* ${app_dir}/.
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
    echo "Usage: $0 {start|stop|restart|deploy|status}"
    exit 1
    ;;
esac
exit 0
