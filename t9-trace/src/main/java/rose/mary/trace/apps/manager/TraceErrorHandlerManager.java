/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.manager;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
 

import rose.mary.trace.apps.cache.CacheProxy;
import rose.mary.trace.apps.handler.TraceErrorHandler;
import rose.mary.trace.apps.manager.config.TraceErrorHandlerManagerConfig;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.database.service.TraceService;
import rose.mary.trace.monitor.ThroughputMonitor;

/**
 * <pre>
 * rose.mary.trace.apps.manager
 * HandlerManager.java
 * </pre>
 * @author whoana
 * @date Oct 11, 2019
 */
public class TraceErrorHandlerManager {

	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");
	
	TraceErrorHandlerManagerConfig config = null; 
	
	int delayForNoMessage = 100;
	
	int exceptionDelay = 100;
	
	int maxRetry = 2;               
	
	ThroughputMonitor tpm;
	
	CacheManager cacheManager;
	 
	MessageSource messageResource;
	
	TraceService traceService;
	
	TraceErrorHandler traceErrorHandler;
	
	public TraceErrorHandlerManager(TraceErrorHandlerManagerConfig config, CacheManager cacheManager, TraceService traceService, MessageSource messageResource) {
		this.config = config;  
		this.delayForNoMessage = config.getDelayForNoMessage();
		this.exceptionDelay = config.getExceptionDelay();
		this.maxRetry = config.getMaxRetry();
		this.cacheManager = cacheManager;
		this.traceService = traceService;
		this.messageResource = messageResource;
	}
	
	public void ready() throws Exception{ 
	}

	

	public void start() throws Exception {
		try {
			 
			CacheProxy<String, Trace> errorCache01  = cacheManager.getErrorCache01(); 
			CacheProxy<String, Trace> mergeCache   = cacheManager.getMergeCache(); 
			
			
			logger.info("TraceErrorHandler starting");
			traceErrorHandler = new TraceErrorHandler(config.getName(), errorCache01, mergeCache, messageResource, traceService, delayForNoMessage, maxRetry, exceptionDelay);
			traceErrorHandler.start();
			logger.info("TraceErrorHandler started");
		}catch (Exception e) {
			stop();
			throw e;
		}
	}
	
	/**
	 * 
	 */
	public void stop() {
		logger.info("TraceErrorHandler stopping");
		if(traceErrorHandler != null)  traceErrorHandler.stop();
		logger.info("TraceErrorHandler stopped");
	}
	
}
