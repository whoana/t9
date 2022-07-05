#!/bin/sh
export JAVA_HOME=%JAVA_HOME%
export T9_HOME=%T9_HOME%
export ACTIVE_PROFILE=production
export CPATH=$T9_HOME/lib/t9-trace-1.0.0.jar
export CPATH=$CPATH:$T9_HOME/lib/ext/t9-cache-1.0.0.jar
export CPATH=$CPATH:$T9_HOME/lib/ext/infinispan-core-9.4.16.Final.jar
export CPATH=$CPATH:$T9_HOME/lib/ext/infinispan-commons-9.4.16.Final.jar
export CPATH=$CPATH:$T9_HOME/lib/ext/jboss-logging-3.4.1.Final.jar
export CPATH=$CPATH:$T9_HOME/lib/ext/jboss-transaction-api_1.2_spec-1.1.1.Final.jar
export CPATH=$CPATH:$T9_HOME/lib/ext/jboss-marshalling-osgi-2.0.6.Final.jar
export CPATH=$CPATH:$T9_HOME/lib/ext/caffeine-2.8.0.jar
export CPATH=$CPATH:$T9_HOME/lib/ext/reactive-streams-1.0.3.jar
export CPATH=$CPATH:$T9_HOME/lib/ext/rxjava-2.2.15.jar
export CPATH=$CPATH:$T9_HOME/lib/ext/s9-utility-1.0.0.jar

arg1=$1
if [ "$arg1" = "-f" ]; then
	echo "forground start..."
	${JAVA_HOME}/bin/java -cp $CPATH \
						  -Dapp.name=rosemary \
						  -Drose.mary.home=${T9_HOME} \
						  -Dspring.profiles.active=${ACTIVE_PROFILE} \
						  -Dspring.config.location=${T9_HOME}/config/application.yml \
						  -Dloader.main=rose.mary.trace.T9 \
      					  org.springframework.boot.loader.T9L
elif [ "$arg1" = "-c" ]; then
	echo "console start..."
	clear
	${JAVA_HOME}/bin/java -Dconsole.home=${T9_HOME} -Dconsole.port=8090 -jar ${T9_HOME}/lib/t9-console-1.0.0.jar
elif [ "$arg1" = "-r" ]; then
	echo "recovery mode start..."
	${JAVA_HOME}/bin/java -cp $CPATH \
						  -Dapp.name=rosemary \
						  -Drose.mary.home=${T9_HOME} \
						  -Drose.mary.run.mode=recovery \
						  -Dspring.profiles.active=${ACTIVE_PROFILE} \
						  -Dspring.config.location=${T9_HOME}/config/application.yml \
						  -Dloader.main=rose.mary.trace.T9 \
      					  org.springframework.boot.loader.T9L
elif [ "$arg1" = "-rc" ]; then
	echo "clear cache"
	rm -Rf {T9_HOME}/cache
	echo "background start..."
	nohup ${JAVA_HOME}/bin/java -cp $CPATH \
						  -Dapp.name=rosemary \
						  -Drose.mary.home=${T9_HOME} \
						  -Dspring.profiles.active=${ACTIVE_PROFILE} \
						  -Dspring.config.location=${T9_HOME}/config/application.yml \
						  -Dloader.main=rose.mary.trace.T9 \
      					  org.springframework.boot.loader.T9L 1>/dev/null 2>&1 &
else
    echo "background start..."
	nohup ${JAVA_HOME}/bin/java -cp $CPATH \
						  -Dapp.name=rosemary \
						  -Drose.mary.home=${T9_HOME} \
						  -Dspring.profiles.active=${ACTIVE_PROFILE} \
						  -Dspring.config.location=${T9_HOME}/config/application.yml \
						  -Dloader.main=rose.mary.trace.T9 \
      					  org.springframework.boot.loader.T9L 1>/dev/null 2>&1 &
fi
