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
import rose.mary.trace.core.config.LoaderManagerConfig;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.monitor.ThroughputMonitor;
import rose.mary.trace.database.service.TraceService;
import rose.mary.trace.loader.Distributor;
import rose.mary.trace.loader.TraceLoader;

/**
 * <pre>
 * rose.mary.trace.manager
 * TraceLoaderManager.java
 * </pre>
 * 
 * @author whoana
 * @date Aug 28, 2019
 */
public class LoaderManager {

	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");

	LoaderManagerConfig config = null;

	TraceService traceService;

	List<TraceLoader> loaders = new ArrayList<TraceLoader>();

	int threadCount = 1;

	int commitCount = 1000;

	int delayForNoMessage = 100;

	ThroughputMonitor tpm;

	CacheManager cacheManager;

	boolean loadError = true;

	boolean loadContents = true;

	public LoaderManager(LoaderManagerConfig config, TraceService traceService, CacheManager cacheManager,
			ThroughputMonitor tpm) {
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

	public void ready() throws Exception {
	}

	public void start() throws Exception {
		try {

			List<CacheProxy<String, Trace>> distributeCaches = cacheManager.getDistributeCaches();
			CacheProxy<String, Trace> mergeCache = cacheManager.getMergeCache();
			CacheProxy<String, Trace> errorCache = cacheManager.getErrorCache01();

			int idx = 0;
			stop();
			loaders = new ArrayList<TraceLoader>();
			logger.info("Loaders starting");

			threadCount = config.getThreadCount();
			logger.info("Loaders thread count per cache:" + threadCount);
			boolean setThreadCountPerCache = false;
			for (CacheProxy<String, Trace> distributeCache : distributeCaches) {

				if (setThreadCountPerCache) {// change setThreadCountPerCache value to true for test
					// TODO: 하나의 캐시에 대해 멀티 스레드로 로더를 실행하는 것을 테스트 해보자
					for (int i = 0; i < threadCount; i++) {
						createLoader(idx, distributeCache, mergeCache, errorCache);
						idx++;
					}
				} else {
					createLoader(idx, distributeCache, mergeCache, errorCache);
					idx++;
				}
			}
			logger.info("Loaders started");
		} catch (Exception e) {
			stop();
			throw e;
		}
	}

	private void createLoader(int idx, CacheProxy<String, Trace> distributeCache, CacheProxy<String, Trace> mergeCache,
			CacheProxy<String, Trace> errorCache) throws Exception {
		String name = Util.join(config.getName(), idx);
		TraceLoader loader = new TraceLoader(name, commitCount, delayForNoMessage, loadError, loadContents,
				distributeCache, mergeCache, errorCache, traceService, tpm, null);
		loader.setDistributor(new Distributor());
		loaders.add(loader);
		loader.start();
	}

	/**
	 * 
	 */
	public void stop() {

		if (loaders != null && loaders.size() > 0) {
			logger.info("Loaders stopping");

			// int idx = 0;
			// java.util.ConcurrentModificationException 방지를 위해 변경함.
			// for (Loader loader : loaders) {
			// loader.stop();
			// //loaders.remove(loader);
			// logger.info("Loader(" + (idx ++) + ") stop....");
			// }

			Iterator<TraceLoader> iterator = loaders.iterator();
			while (iterator.hasNext()) {
				TraceLoader loader = iterator.next();
				loader.stop();
				iterator.remove();
			}

			logger.info("Loaders stopped");
		}
	}

}
