 #java -cp ./t9-trace/build/libs/t9-trace-1.0.0.jar \
 #     -Drose.mary.home=/Users/whoana/DEV/workspace-vs/t9/home \
 #     -Dloader.main=rose.mary.trace.T9 \
 #     org.springframework.boot.loader.T9Launcher

cpath=./t9-trace/build/libs/t9-trace-1.0.0.jar
cpath=$cpath:./home/lib/ext/t9-cache-1.0.0.jar
cpath=$cpath:./home/lib/ext/infinispan-core-9.4.16.Final.jar
cpath=$cpath:./home/lib/ext/infinispan-commons-9.4.16.Final.jar
cpath=$cpath:./home/lib/ext/jboss-logging-3.4.1.Final.jar
cpath=$cpath:./home/lib/ext/jboss-transaction-api_1.2_spec-1.1.1.Final.jar
cpath=$cpath:./home/lib/ext/jboss-marshalling-osgi-2.0.6.Final.jar
cpath=$cpath:./home/lib/ext/caffeine-2.8.0.jar
cpath=$cpath:./home/lib/ext/reactive-streams-1.0.3.jar
cpath=$cpath:./home/lib/ext/rxjava-2.2.15.jar
cpath=$cpath:./home/lib/ext/s9-utility-1.0.0.jar

/Library/Java/JavaVirtualMachines/jdk1.8.0_251.jdk/Contents/Home/bin/java -cp $cpath \
      -Drose.mary.home=/Users/whoana/DEV/workspace-vs/t9/home \
      -Dloader.main=rose.mary.trace.T9 \
      -Dspring.config.location=./home/config/application.yml \
      org.springframework.boot.loader.T9L