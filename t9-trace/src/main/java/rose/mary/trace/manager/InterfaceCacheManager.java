/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.manager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.config.InterfaceCacheManagerConfig;
import rose.mary.trace.core.data.common.InterfaceInfo;
import rose.mary.trace.database.service.InterfaceService;

/**
 * <pre>
 * rose.mary.trace.manager
 * InterfaceCacheManager.java
 * </pre>
 * 
 * @author whoana
 * @date Sep 16, 2019
 */
public class InterfaceCacheManager {

	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");

	int refreshDelay = 60;

	InterfaceService service;

	InterfaceCacheManagerConfig config;

	ThreadPoolTaskScheduler scheduler;

	CacheProxy<String, InterfaceInfo> interfaceCache;
	 

	public InterfaceCacheManager(
		InterfaceCacheManagerConfig config, 
		ThreadPoolTaskScheduler scheduler,
		InterfaceService service, 
		CacheManager cacheManager
	) {
		this.config = config;
		this.service = service;
		this.scheduler = scheduler; 
		interfaceCache = cacheManager.getInterfaceCache();
		refreshDelay = this.config.getRefreshDelay();
	}

	public void refresh(String integrationId) throws Exception {
		InterfaceInfo interfaceInfo = service.retrieve(integrationId);
	 
		interfaceCache.put(integrationId, interfaceInfo);

	}

	public void refresh() throws Exception {
		List<InterfaceInfo> list = service.retrieve();
		logger.info("start refreshing interface cache");
		for (InterfaceInfo interfaceInfo : list) {
			String key = interfaceInfo.getIntegrationId();
			trim(interfaceInfo);
			interfaceCache.put(key, interfaceInfo);
			
			logger.debug(Util.join(key ,":", Util.toJSONString(interfaceInfo)));
		
		}
		logger.info("end refreshing interface cache");
	}

	public void prepare() throws Exception {
		refresh();
		schedule();
	}

	private void schedule() {
		PeriodicTrigger periodicTrigger = new PeriodicTrigger(refreshDelay, TimeUnit.SECONDS);

		scheduler.schedule(new Runnable() {

			@Override
			public void run() {

				int cacheCount = 0;
				try {

					logger.info("start caching interface info");
					List<InterfaceInfo> list = service.retrieve();
					for (InterfaceInfo interfaceInfo : list) {
						String key = interfaceInfo.getIntegrationId();
						
						
						trim(interfaceInfo);
						
						interfaceCache.put(key, interfaceInfo);
						cacheCount ++;
					}


				} catch (Exception e) {
					logger.error("", e);
				}				
				logger.info(Util.join("end caching interface info(caching count:", cacheCount, ")"));
			}

			int maxNameLength = 50;
			
		}, periodicTrigger);
	}

	private void trim(InterfaceInfo interfaceInfo) {
		// TODO Auto-generated method stub
//				String appPrMethodNm = interfaceInfo.getAppPrMethodNm();
//				if(maxNameLength  <= appPrMethodNm.getBytes().length) {
//					
//				}
	}
}
