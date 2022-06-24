/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.manager;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.config.BotErrorHandlerManagerConfig;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.monitor.ThroughputMonitor;
import rose.mary.trace.database.service.BotService;
import rose.mary.trace.handler.BotErrorHandler;

/**
 * <pre>
 * BotErrorHandlerManager.java
 * </pre>
 * @author whoana
 * @since 20200508
 */
public class BotErrorHandlerManager {

	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");
	
	BotErrorHandlerManagerConfig config = null; 
	
	ThroughputMonitor tpm;
	
	CacheManager cacheManager;
	 
	MessageSource messageResource;
	
	BotService botService;
	
	BotErrorHandler botErrorHandler;
	
	public BotErrorHandlerManager(BotErrorHandlerManagerConfig config, CacheManager cacheManager, BotService botService, MessageSource messageResource) {
		this.config = config;   
		this.cacheManager = cacheManager;
		this.botService = botService;
		this.messageResource = messageResource;
	}
	
	public void ready() throws Exception{ 
	}

	

	public void start() throws Exception {
		try {
			 
			CacheProxy<String, State> errorCache  = cacheManager.getErrorCache02(); 
			logger.info("BotErrorHandler starting");
			botErrorHandler = new BotErrorHandler(config.getName(), errorCache, messageResource, botService, config.getDelayForNoMessage(), config.getMaxRetry(), config.getExceptionDelay());
			botErrorHandler.start();
			logger.info("BotErrorHandler started");
		}catch (Exception e) {
			stop();
			throw e;
		}
	}
	
	/**
	 * 
	 */
	public void stop() {
		logger.info("BotErrorHandler stopping");
		if(botErrorHandler != null)  botErrorHandler.stop();
		logger.info("BotErrorHandler stopped");
	}
	
}
