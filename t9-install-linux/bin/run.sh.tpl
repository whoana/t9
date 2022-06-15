#!/bin/sh
export JAVA_HOME=%JAVA_HOME%
export T9_HOME=%T9_HOME%
export ACTIVE_PROFILE=production

arg1=$1
if [ "$arg1" = "-f" ]; then
	echo "forground start..."
	${JAVA_HOME}/bin/java -Dapp.name=rosemary -Drose.mary.home=${T9_HOME} -Dspring.profiles.active=${ACTIVE_PROFILE} -jar ${T9_HOME}/lib/t9-trace-1.0.0.jar --spring.config.location=${T9_HOME}/config/application.yml
elif [ "$arg1" = "-c" ]; then
	echo "console start..."
	clear
	${JAVA_HOME}/bin/java -Dconsole.home=${T9_HOME} -Dconsole.port=8090 -jar ${T9_HOME}/lib/t9-console-1.0.0.jar
elif [ "$arg1" = "-r" ]; then
	echo "recovery mode start..."
	${JAVA_HOME}/bin/java -Dapp.name=rosemary -Drose.mary.home=${T9_HOME} -Drose.mary.run.mode=recovery -Dspring.profiles.active=${ACTIVE_PROFILE} -jar ${T9_HOME}/lib/t9-trace-1.0.0.jar --spring.config.location=${T9_HOME}/config/application.yml
elif [ "$arg1" = "-rc" ]; then
	echo "clear cache"
	rm -Rf {T9_HOME}/cache
	echo "background start..."
	nohup ${JAVA_HOME}/bin/java -Dapp.name=rosemary -Drose.mary.home=${T9_HOME} -Dspring.profiles.active=${ACTIVE_PROFILE} -jar ${T9_HOME}/lib/t9-trace-1.0.0.jar --spring.config.location=${T9_HOME}/config/application.yml 1>/dev/null 2>&1 &	
else
    echo "background start..."
	nohup ${JAVA_HOME}/bin/java -Dapp.name=rosemary -Drose.mary.home=${T9_HOME} -Dspring.profiles.active=${ACTIVE_PROFILE} -jar ${T9_HOME}/lib/t9-trace-1.0.0.jar --spring.config.location=${T9_HOME}/config/application.yml 1>/dev/null 2>&1 &
fi
