/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.manager;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.config.FinisherManagerConfig;
import rose.mary.trace.core.finisher.Finisher;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace; 
import rose.mary.trace.exception.ExceptionHandler; 

/**
 * <pre>
 * rose.mary.trace.apps.manager
 * FinisherManager.java
 * </pre>
 * 
 * @author whoana
 * @date Oct 2, 2019
 */
public class FinisherManager {

	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");

	FinisherManagerConfig config = null;
	
	CacheManager cacheManager;

	Finisher finisher = null;
	
	public FinisherManager(FinisherManagerConfig config, CacheManager cacheManager) {
		this.config = config;
		this.cacheManager = cacheManager;
	}

	public void ready() throws Exception {
	}

	public void start() throws Exception {
	 
		CacheProxy<String, Trace> backupCache = cacheManager.getBackupCache();
		CacheProxy<String, State> finCache = cacheManager.getFinCache();
		CacheProxy<String, Integer> routingCache = cacheManager.getRoutingCache(); 
		logger.info("Finishers starting");
		ExceptionHandler eh = null;
		finisher = new Finisher(config, backupCache, finCache, routingCache, eh);
		finisher.start();
		logger.info("Finishers started");
	}

	/**
	 * 
	 */
	public void stop() {
		logger.info("Finishers stopping");			
		if(finisher != null) finisher.stop();
		logger.info("Finishers stopped");
		finisher = null;
	}
 
}
