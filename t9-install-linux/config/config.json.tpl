{
	"channelManagerConfig": {
		"oldStateCheckHandlerConfig": {
			"usePreviousProcessInfo": false,
			"statusSuccess": "00",
			"statusIng"    : "01",
			"statusFail"   : "99",
			"nodeMap": {
				"S"   : 10, "SNDR": 10, "SEND": 10,
				"H"   : 20, "B"   : 20, "BRKR": 20, "SBRK": 20,
				"Q"   : 30, "E"   : 30, "REPL": 30, "RQST": 30,
				"RBRK": 40,
				"R"   : 50, "RECV": 50, "RCVR": 50
			}
		},
		"channelConfigs": [
			{
				"name": "RestChannel",
				"threadCount": 3,
				"type": 2,
				"hostName": "localhost",
				"qmgrName": "",
				"waitTime":1000,
				"port": 0,
				"userId": "none",
				"password": "none",
				"channelName": "none",
				"queueName": "none",
				"module": "n",
				"ccsid": 0,
				"characterSet": 0,
				"autoCommit": false,
				"bindMode": false,
				"maxCommitWait": 100,
				"delayForNoMessage": 1000,
				"commitCount": 100,
				"maxCacheSize":100, 
				"delayForMaxCache":1000,
				"cacheIndex": [0,1,2],
				"healthCheck": false,
				"disable":false
			},
			{
				"name": "MQChannel",
				"threadCount": 3,
				"type": 0,
				"hostName": "%hostName%",
				"qmgrName": "%qmgrName%",
				"port": %port%,
				"userId": "%userId%",
				"password": "%password%",
				"channelName": "%channelName%",
				"queueName": "%queueName%",
				"waitTime":1000,
				"module": "w",
				"ccsid": 1208,
				"characterSet": 1208,
				"autoCommit": false,
				"bindMode": false,
				"maxCommitWait": 100,
				"delayForNoMessage": 1000,
				"commitCount": 100,
				"maxCacheSize":1000000, 
				"delayForMaxCache":1000,				
				"cacheIndex": [0,1,2],
				"healthCheck": true,
				"disable": %wmqDisable%
			},{
				"name": "ILinkChannel",
				"threadCount": 3,
				"type": 0,
				"hostName": "%hostName%",
				"qmgrName": "%qmgrName%",
				"port": %port%,
				"userId": "%userId%",
				"password": "%password%",
				"channelName": "%channelName%",
				"queueName": "%queueName%",
				"waitTime":1000,
				"module": "i",
				"ccsid": 1208,
				"characterSet": 1208,
				"autoCommit": false,
				"bindMode": false,
				"maxCommitWait": 100,
				"delayForNoMessage": 1000,
				"commitCount": 100,
				"maxCacheSize":1000000,
				"delayForMaxCache":1000,
				"cacheIndex": [0,1,2],        
				"healthCheck": true,
				"disable": %iLinkDisable%
		    }
		],
		"delayOnException" : 5000,
		"translateMsgOnException" : false
	},
	"cacheManagerConfig": {
		"vendor": "infinispan",
		"diskPath": "caches",
		"persistant": true,
		"distributeCacheConfigs": [
			{
				"name": "dc01",
				"memoryUnit": 3,
				"heapSize": 10000,
				"diskSize": 3,
				"maxEntries": 1000000
			},
			{
				"name": "dc02",
				"memoryUnit": 3,
				"heapSize": 10000,
				"diskSize": 3,
				"maxEntries": 1000000
			},
			{
				"name": "dc03",
				"memoryUnit": 3,
				"heapSize": 10000,
				"diskSize": 3,
				"maxEntries": 1000000
			}
		],
		"mergeCacheConfig": {
			"name": "mc01",
			"memoryUnit": 3,
			"heapSize": 10000,
			"diskSize": 10,
			"maxEntries": 10000000
		},
		"backupCacheConfig": {
			"name": "backupCache",
			"memoryUnit": 3,
			"heapSize": 10000,
			"diskSize": 10,
			"maxEntries": 10000000
		},
		"botCacheConfigs": [
			{
				"name": "bc01",
				"memoryUnit": 3,
				"heapSize": 10000,
				"diskSize": 3,
				"maxEntries": 1000000
			},
			{
				"name": "bc02",
				"memoryUnit": 3,
				"heapSize": 10000,
				"diskSize": 3,
				"maxEntries": 1000000
			},
			{
				"name": "bc03",
				"memoryUnit": 3,
				"heapSize": 10000,
				"diskSize": 3,
				"maxEntries": 1000000
			}
		],
		"finCacheConfig": {
			"name": "fc01",
			"memoryUnit": 3,
			"heapSize": 10000000,
			"diskSize": 10,
			"maxEntries": 10000000
		},
		"routingCacheConfig": {
			"name": "rc01",
			"memoryUnit": 2,
			"heapSize": 10000,
			"diskSize": 500,
			"maxEntries": 100000000
		},
		"errorCache01Config": {
			"name": "ec01",
			"memoryUnit": 3,
			"heapSize": 10000,
			"diskSize": 10,
			"maxEntries": 10000000
		},
		"errorCache02Config": {
			"name": "ec02",
			"memoryUnit": 3,
			"heapSize": 10000,
			"diskSize": 10,
			"maxEntries": 10000000
		},
		"testCacheConfig": null,
		"interfaceCacheConfig": {
			"name": "ic01",
			"memoryUnit": 3,
			"heapSize": 1000000,
			"diskSize": 5,
			"maxEntries": 10000000
		},
		"unmatchCacheConfig": {
			"name": "uc01",
			"memoryUnit": 3,
			"heapSize": 1000000,
			"diskSize": 5,
			"maxEntries": 10000000
		},
		"systemErrorCacheConfig": {
			"name": "sec01",
			"memoryUnit": 3,
			"heapSize": 10000,
			"diskSize": 5,
			"maxEntries": 100000
		}
	},
	"serverManagerConfig": {
		"name": "t9",
		"version": "1.0.0",
		"site": "mapo",
		"startOnBoot": true
	},
	"loaderManagerConfig": {
		"threadCount": 1,
		"commitCount": 1000,
		"delayForNoMessage": 1000,
		"loadError": true,
		"loadContents": false,
		"name": "loader"
	},
	"boterManagerConfig": {
		"threadCount": 1,
		"commitCount": 1000,
		"delayForNoMessage": 1000,
		"maxRoutingCacheSize": 10000,
		"name": "boter"
	},
	"botLoaderManagerConfig": {
		"threadCount": 1,
		"commitCount": 1000,
		"delayForNoMessage": 1000,
		"name": "botLoader"
	},
	"finisherManagerConfig": {
		"threadCount": 1,
		"commitCount": 100,
		"delayForNoMessage": 1000,
		"useWaitForCleaning": true,
		"waitForFinishedCleaningSec": 1,
		"waitForCleaningSec": 600,
		"delayForDoCleaning": 5000, 
		"resetWhenStart": false,
		"name": "finisher"
	},
	"interfaceCacheManagerConfig": {
		"refreshDelay": 3600
	},
	"traceErrorHandlerManagerConfig": {
		"name": "traceErrorHandler",
		"delayForNoMessage": 10000,
		"maxRetry": 3, 
		"exceptionDelay": 5000
	},
	"botErrorHandlerManagerConfig": {
		"name": "traceErrorHandler",
		"delayForNoMessage": 10000,
		"maxRetry": 3, 
		"exceptionDelay": 5000
	},
	"unmatchHandlerManagerConfig": {
		"name": "unmatchHandler",
		"delayForNoMessage": 1000,
		"delayForDoChecking": 10000, 
		"exceptionDelay": 100
	},
	"policyConfig": {
		"name": "databasePolicyConfig",
		"policy": 100,
		"policyCheckDelay": 10000, 
		"exceptionDelay": 1000,
		"policyCount": 2
	}, 
	"testerManagerConfig": {
		"name": "generateMsgTester",
		"exceptionDelay": 5000,
		"repeatDelaySec":  300,
		"paramsList": [
			{
				"port": %port%,
				"hostName": "%hostName%",
				"qmgrName": "%qmgrName%",				
				"channelName": "%channelName%",
				"queueName": "%queueName%",
			  	"module":"w",
			  	"generateCount":"10000",
			  	"commitCount":"1000",
			  	"traceMsgCreator":"rose.mary.trace.simulator.DefaultTraceMsgCreator",
			  	"data": "한글1234567890qwertyuiop"
			},{
				"port": %port%,
				"hostName": "%hostName%",
				"qmgrName": "%qmgrName%",				
				"channelName": "%channelName%",
				"queueName": "%queueName%",
			  	"module":"i",
			  	"generateCount":"10000",
			  	"commitCount":"1000",
			  	"traceMsgCreator":"rose.mary.trace.simulator.DefaultTraceMsgCreator",
			  	"data": "한글1234567890qwertyuiop"
			}
			
		]
	},
	"systemErrorTestManagerConfig": {
		"name": "systemErrorTester",
		"startOnLoad" : false,
		"delay": 10000, 
		"exceptionDelay": 5000
	}	
}
