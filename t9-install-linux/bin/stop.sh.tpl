export T9_HOME=%T9_HOME%
arg1=$1
if [ "$arg1" = "-f" ]; then
        echo "stop forcely..."
        kill -9 $(cat ${T9_HOME}/iwannadie.pid)
elif [ "$arg1" = "-c" ]; then
        echo "stop by cmd kill..."
        kill $(cat ${T9_HOME}/iwannadie.pid)
else
    echo "stop gracefully..."
    curl -H 'Accept: application/json' -H 'Content-Type: application/json' -d '{"objectType": "ComMessage", "requestObject": null,"startTime": "20190416105001001","endTime": null,"errorCd":"0","errorMsg":"","userId": "iip","appId": "SU0810R01","checkSession":false}' http://127.0.0.1:%T9_PORT%/trace/managers/servers/shutdown?method=GET
fi
echo "shutdown server..."
