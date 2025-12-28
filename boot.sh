#!/usr/bin/env bash
#
# XXX服务运行脚本
#
# sh /path/to/boot.sh start
# sh /path/to/boot.sh restart debug
# sh /path/to/boot.sh status
#
#set -ex
MEM=$(free -m | grep Mem | awk '{print $2}')
HALF_MEM=$((MEM / 2))
MIN_MEM=$((HALF_MEM / 3))
# which java
JAVA_EXEC="java"
# java --help-extra
JAVA_OPTS="-server -Xmn${MIN_MEM}m -Xms${HALF_MEM}m -Xmx${HALF_MEM}m -Xss1m"
DEBUG_OPTS=""

DIR="$(cd "$(dirname "$0")" && pwd)"
APP_FILE="${DIR}/ijleex-boot-2.0.0-exec.jar"
APP_ARGS="--spring.profiles.active=dev"

export LD_LIBRARY_PATH=${DIR}:$LD_LIBRARY_PATH

# 启动
start() {
  DEBUG="$1"

  if [ "${DEBUG}" = "debug" ]; then
    DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
  fi

  PID=$(pgrep -fn "${APP_FILE}")
  if [ -z "${PID}" ]; then
    echo "[INFO] Starting App: java ${DEBUG_OPTS} ${JAVA_OPTS} -jar ${APP_FILE} ${APP_ARGS}  $(date +"%F %T")" >>"${DIR}/boot.log"
    # java -XX:+PrintFlagsFinal -version
    nohup $JAVA_EXEC $DEBUG_OPTS $JAVA_OPTS -XX:+UseG1GC \
      -XX:InitialCodeCacheSize=2m -XX:ReservedCodeCacheSize=240m \
      -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m \
      -XX:+AlwaysPreTouch -XX:ParallelGCThreads=8 \
      -Djava.awt.headless=true -Duser.timezone=Asia/Shanghai -Dfile.encoding=UTF-8 \
      -jar $APP_FILE $APP_ARGS >/dev/null 2>&1 &

    PID=$!
    echo "[SUCCESS] App started... (PID: ${PID})  $(date +"%F %T")"
  else
    echo "[WARN] App is running... (PID: ${PID})  $(date +"%F %T")"
  fi
  return 0
}

# 停止
stop() {
  RESTART="$1"

  PID=$(pgrep -fn "${APP_FILE}")
  if [ -z "${PID}" ]; then
    echo "[WARN] App is not running  $(date +"%F %T")"
  else
    echo "[INFO] Stopping App... (PID: ${PID})  $(date +"%F %T")"
    if [ "${RESTART}" = "restart" ]; then
      kill -9 "${PID}"
    else
      kill -15 "${PID}"
    fi
    echo "[SUCCESS] App stopped  $(date +"%F %T")"
  fi
  return 0
}

# 重启
restart() {
  DEBUG="$1"

  stop "restart"
  start "${DEBUG}"
  return 0
}

# 查看状态
status() {
  PID=$(pgrep -fn "${APP_FILE}")
  if [ -z "${PID}" ]; then
    echo "[WARN] App is not running  $(date +"%F %T")"
  else
    echo "[INFO] App is running... (PID: ${PID})  $(date +"%F %T")"
  fi
  return 0
}

# 用法提示
usage() {
  echo "Usage: sh $0 {start|stop|restart|status} [debug]"
  exit 1
}

# 入口函数
main() {
  # 启动命令：start/stop/restart/status/...
  CMD="$1" DEBUG="$2"
  echo "[INFO] sh $0 $1 $2  $(date +"%F %T")" >>"${DIR}/boot.log"

  case "${CMD}" in
  start) start "${DEBUG}" ;;
  stop) stop ;;
  restart) restart "${DEBUG}" ;;
  status) status ;;
  *) usage ;;
  esac

  return 0
}

main "$@"
exit $?
