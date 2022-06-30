 java -cp ./t9-install-linux/lib/t9-trace-1.0.0.jar \
      -Drose.mary.home=/Users/whoana/DEV/workspace-vs/t9/home \
      -Dloader.main=rose.mary.trace.T9 \
      --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
      org.springframework.boot.loader.T9Launcher