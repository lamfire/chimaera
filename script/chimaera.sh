#! /bin/sh
JAVA_HOME=/usr/local/java
JAVA_OPTS="-Xmx2048m -Xms2048m -Xmn512m -server -XX:PermSize=16m -XX:MaxPermSize=64m"

SCRIPT="$0"
while [ -h "$SCRIPT" ] ; do
  ls=`ls -ld "$SCRIPT"`
  # Drop everything prior to ->
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    SCRIPT="$link"
  else
    SCRIPT=`dirname "$SCRIPT"`/"$link"
  fi
done

SERVER_HOME=`dirname "$SCRIPT"`
SERVER_HOME=`cd "$SERVER_HOME"; pwd`
export SERVER_HOME
LIBDIR=$SERVER_HOME/lib
export LIBDIR

CLASSPATH=${CLASSPATH}:${SERVER_HOME}/conf
for lib in ${LIBDIR}/*.jar
do
 CLASSPATH=$CLASSPATH:$lib
 export CLASSPATH
done

PATH=${JAVA_HOME}/bin:${PATH}
export JAVA_OPTS
export JAVA_HOME
export PATH
java=$JAVA_HOME/bin/java

exec $java $JAVA_OPTS com.lamfire.chimaera.bootstrap.ChimaeraBootstrap $* &