/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pep.per.mint.common.util.Util;
import rose.mary.trace.apps.cache.CacheProxy;
import rose.mary.trace.apps.loader.Loader;
import rose.mary.trace.apps.manager.config.LoaderManagerConfig;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.database.service.TraceService;
import rose.mary.trace.monitor.ThroughputMonitor;

/**
 * <pre>
 * rose.mary.trace.manager
 * TraceLoaderManager.java
 * </pre>
 * @author whoana
 * @date Aug 28, 2019
 */
public class LoaderManager {
	
	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");
	
	LoaderManagerConfig config = null;
 
	TraceService traceService;
	
	List<Loader> loaders = new ArrayList<Loader>();
	
	int threadCount = 1;
	
	int commitCount = 1000;
	
	int delayForNoMessage = 100;
	
	ThroughputMonitor tpm;
	
	CacheManager cacheManager;
	
	boolean loadError = true;
	
	boolean loadContents = true;
	
	public LoaderManager(LoaderManagerConfig config, TraceService traceService, CacheManager cacheManager, ThroughputMonitor tpm) {
		this.config = config;
		this.traceService = traceService;
		this.threadCount = config.getThreadCount();
		this.commitCount = config.getCommitCount(); 
		this.delayForNoMessage = config.getDelayForNoMessage();
		this.tpm = tpm;
		this.cacheManager = cacheManager;
		this.loadError = config.isLoadError();
		this.loadContents = config.isLoadContents();
		
	}
	
	public void ready() throws Exception{ 
	}


	public void start() throws Exception {
		try {
			
			List<CacheProxy<String, Trace>> distributeCaches = cacheManager.getDistributeCaches();
			CacheProxy<String, Trace> mergeCache = cacheManager.getMergeCache();
			CacheProxy<String, Trace> errorCache = cacheManager.getErrorCache01();
			
			
			int idx = 0;
			stop();
			loaders = new ArrayList<Loader>();
			logger.info("Loaders starting");
			for(CacheProxy<String, Trace> distributeCache : distributeCaches) {
				String name = Util.join(config.getName(), idx);
				Loader loader = new Loader(name, commitCount, delayForNoMessage, loadError, loadContents, distributeCache, mergeCache, errorCache, traceService,tpm, null);			
				loaders.add(loader);
				loader.start();
				idx ++;
			}			
			logger.info("Loaders started");
		}catch (Exception e) {
			stop();
			throw e;
		}
	}
	
	/**
	 * 
	 */
	public void stop() {
		  
		if(loaders != null && loaders.size() > 0) {
			logger.info("Loaders stopping");
			
//			int idx = 0;
//			java.util.ConcurrentModificationException 방지를 위해 변경함.				
//			for (Loader loader : loaders) {
//				loader.stop();
//				//loaders.remove(loader);
//				logger.info("Loader(" + (idx ++) + ") stop....");
//			}
			
			Iterator<Loader> iterator = loaders.iterator();
			while(iterator.hasNext()) {
				Loader loader = iterator.next();
				loader.stop();
				iterator.remove();
			}
			
			
			logger.info("Loaders stopped");
		} 
	}


	
	
}
