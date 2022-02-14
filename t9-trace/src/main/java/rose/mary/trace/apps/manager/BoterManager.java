/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.manager;

 

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import rose.mary.trace.apps.boter.Boter;
import rose.mary.trace.apps.cache.CacheProxy; 
import rose.mary.trace.apps.manager.config.BoterManagerConfig; 

import rose.mary.trace.data.common.InterfaceInfo;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace; 
import rose.mary.trace.exception.ExceptionHandler;
import rose.mary.trace.monitor.ThroughputMonitor;

/**
 * <pre>
 * rose.mary.trace.apps.manager
 * BoterManager.java
 * </pre>
 * @author whoana
 * @date Sep 18, 2019
 */
public class BoterManager {

	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");
	
	BoterManagerConfig config = null; 
	
	Boter boter; 
	
	int delayForNoMessage = 100;
	
	ThroughputMonitor tpm;
	
	CacheManager cacheManager;
	
	ExceptionHandler exceptionHandler = null;
	
	public BoterManager(BoterManagerConfig config, CacheManager cacheManager, ThroughputMonitor tpm) {
		this.config = config;  
		this.delayForNoMessage = config.getDelayForNoMessage();
		this.tpm = tpm;
		this.cacheManager = cacheManager;
	}
	
	public void ready() throws Exception{ 
	}

	

	public void start() throws Exception {
		try {
			CacheProxy<String, Trace> mergeCache  			 = cacheManager.getMergeCache(); 
			CacheProxy<String, Trace> backupCache 			 = cacheManager.getBackupCache();
			List<CacheProxy<String, State>> botCaches    	 = cacheManager.getBotCaches();
			CacheProxy<String, State> finCache				 = cacheManager.getFinCache();
			CacheProxy<String, InterfaceInfo> interfaceCache = cacheManager.getInterfaceCache();
			CacheProxy<String, State> errorCache 			 = cacheManager.getErrorCache02(); 
			CacheProxy<String, Integer> routingCache 		 = cacheManager.getRoutingCache(); 
			logger.info("Boter starting"); 
			boter = new Boter(config, mergeCache, backupCache, botCaches, finCache, interfaceCache, errorCache, routingCache, tpm, exceptionHandler);
			boter.start();
			logger.info("Boter started");
		}catch (Exception e) {
			stop();
			throw e;
		}
	}
	
	/**
	 * 
	 */
	public void stop() {		  
		if(boter != null)  boter.stop();
	}


}
