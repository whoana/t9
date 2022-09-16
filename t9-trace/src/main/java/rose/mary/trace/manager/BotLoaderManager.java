/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.config.BotLoaderManagerConfig;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.StateEvent;
import rose.mary.trace.core.database.FromDatabase;
import rose.mary.trace.core.monitor.ThroughputMonitor;
import rose.mary.trace.database.service.BotService;
import rose.mary.trace.loader.BotLoader;

/**
 * <pre>
 * rose.mary.trace.apps.manager
 * BotLoaderManager.java
 * </pre>
 * 
 * @author whoana
 * @date Sep 19, 2019
 */
public class BotLoaderManager {

	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");

	BotLoaderManagerConfig config;

	BotService botService;

	List<BotLoader> loaders = new ArrayList<BotLoader>();

	int threadCount = 1;

	int commitCount = 1000;

	int delayForNoMessage = 100;

	ThroughputMonitor tpm;

	CacheManager cacheManager;

	FromDatabase fromDatabase;

	public BotLoaderManager(
			BotLoaderManagerConfig config,
			BotService botService,
			CacheManager cacheManager,
			ThroughputMonitor tpm,
			FromDatabase fromDatabase) {
		this.config = config;
		this.botService = botService;
		this.threadCount = config.getThreadCount();
		this.commitCount = config.getCommitCount();
		this.delayForNoMessage = config.getDelayForNoMessage();
		this.tpm = tpm;
		this.cacheManager = cacheManager;
		this.fromDatabase = fromDatabase;
	}

	public void ready() throws Exception {

	}

	public void start() throws Exception {
		try {

			List<CacheProxy<String, StateEvent>> botCaches = cacheManager.getBotCaches();
			CacheProxy<String, State> finCache = cacheManager.getFinCache();
			CacheProxy<String, State> errorCache = cacheManager.getErrorCache02(); 

			int idx = 0;
			stop();
			loaders = new ArrayList<BotLoader>();
			logger.info("BotLoaders starting");

			for (CacheProxy<String, StateEvent> botCache : botCaches) {
				String name = Util.join(config.getName(), idx);
				// BotLoader loader = new BotLoader(name , commitCount, delayForNoMessage,
				// botCache, errorCache, botService, tpm, null);

				BotLoader loader = new BotLoader(
						name,
						commitCount,
						delayForNoMessage,
						botCache,
						finCache,
						errorCache, 
						botService,
						tpm,
						null);

				loaders.add(loader);
				loader.setFromDatabase(fromDatabase);
				loader.start();
				idx++;
			}
			logger.info("BotLoaders started");
		} catch (Exception e) {
			stop();
			throw e;
		}
	}

	/**
	 * 
	 */
	public void stop() {

		if (loaders != null && loaders.size() > 0) {

			logger.info("BotLoaders stopping");
			// java.util.ConcurrentModificationException 방지를 위해 변경함.
			// for (BotLoader loader : loaders) {
			// loader.stop();
			// }

			Iterator<BotLoader> iterator = loaders.iterator();
			while (iterator.hasNext()) {
				BotLoader loader = iterator.next();
				loader.stop();
				//iterator.remove();
			}
			loaders.clear();
			// loaders.clear();
			logger.info("BotLoaders stopped ");
		}
	}

}
