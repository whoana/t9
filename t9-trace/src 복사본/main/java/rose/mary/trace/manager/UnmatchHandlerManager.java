package rose.mary.trace.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.config.UnmatchHandlerManagerConfig;
import rose.mary.trace.core.data.common.InterfaceInfo;
import rose.mary.trace.core.data.common.Unmatch;
import rose.mary.trace.database.service.BotService;
import rose.mary.trace.handler.UnmatchHandler;

public class UnmatchHandlerManager {
	
	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");
	
	UnmatchHandlerManagerConfig config = null;
	
	int delayForNoMessage = 100;
	
	int exceptionDelay = 100;
	
	int delayForDoChecking = 3600;
	
	CacheManager cacheManager;
	 
	MessageSource messageResource;
	
	BotService botService;

	UnmatchHandler unmatchHandler;
	
	public UnmatchHandlerManager(
		UnmatchHandlerManagerConfig config,
		CacheManager cacheManager,
		BotService botService,
		MessageSource messageResource
		
	) {
		this.config = config;
		this.delayForNoMessage = config.getDelayForNoMessage(); 
		this.exceptionDelay = config.getExceptionDelay(); 
		this.delayForDoChecking = config.getDelayForDoChecking(); 
		
		this.cacheManager = cacheManager;
		this.botService = botService;
		this.messageResource = messageResource;
		
	}

	public void ready() throws Exception{ 
	}

	

	public void start() throws Exception {
		try {
			CacheProxy<String, Unmatch> unmatchCache  = cacheManager.getUnmatchCache(); 
			CacheProxy<String, InterfaceInfo> interfaceCache  = cacheManager.getInterfaceCache(); 
			logger.info("UnmatchHandler starting");
			unmatchHandler = new UnmatchHandler(config.getName(), unmatchCache, interfaceCache, botService, messageResource, delayForNoMessage , delayForDoChecking, exceptionDelay);
			unmatchHandler.start();
			logger.info("UnmatchHandler started");
		}catch (Exception e) {
			stop();
			throw e;
		}
	}
	
	/**
	 * 
	 */
	public void stop() {
		logger.info("UnmatchHandler stopping");
		if(unmatchHandler != null)  unmatchHandler.stop();
		logger.info("UnmatchHandler stopped");
	}
}
