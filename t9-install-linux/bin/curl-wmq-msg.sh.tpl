curl -H 'Accept: application/json' -H 'Content-Type: application/json' -d '{"objectType": "ComMessage", "requestObject":{"port":"%port%","hostname":"%hostName%","qmgrName":"%qmgrName%","userId":"%userId%","password":"%password%","channelName":"%channelName%","queueName":"%queueName%","module":"w","generateCount":"100","commitCount":"100","traceMsgCreator":"rose.mary.trace.core.simulator.DefaultTraceMsgCreator","data": "한글1234567890qwertyuiop"},"startTime": "20190416105001001","endTime": null,"errorCd":"0","errorMsg":"","userId": "iip","appId": "SU0810R01","checkSession":false}' http://127.0.0.1:8090/trace/test/generate/msgs?method=GET
