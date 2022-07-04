 java -cp ./t9-install-linux/lib/t9-trace-1.0.0.jar \
      -Drose.mary.home=/Users/whoana/DEV/workspace-vs/t9/home \
      -Dloader.main=rose.mary.trace.T9 \
      --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
      org.springframework.boot.loader.T9Launcher

cpath=./lib/t9-trace-1.0.0.jar
cpath=$cpath:./lib/t9-cache-1.0.0.jar
cpath=$cpath:./lib/infinispan-core-9.4.16.Final.jar
cpath=$cpath:./lib/infinispan-commons-9.4.16.Final.jar
cpath=$cpath:./lib/jboss-logging-3.4.1.Final.jar
cpath=$cpath:./lib/jboss-transaction-api_1.2_spec-1.1.1.Final.jar
cpath=$cpath:./lib/jboss-marshalling-osgi-2.0.6.Final.jar
cpath=$cpath:./lib/caffeine-2.8.0.jar
cpath=$cpath:./lib/reactive-streams-1.0.3.jar
cpath=$cpath:./lib/rxjava-2.2.15.jar
java -cp $cpath \
      -Drose.mary.home=/Users/whoana/DEV/workspace-vs/t9/apps/t9home \
      -Dloader.main=rose.mary.trace.T9 \
      org.springframework.boot.loader.T9Launcher