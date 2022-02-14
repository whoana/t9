/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.cache.infinispan;
 

import java.io.File;
import java.util.List;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.transaction.TransactionMode;

import rose.mary.trace.apps.cache.CacheManagerProxy;
import rose.mary.trace.apps.manager.config.CacheManagerConfig;
import rose.mary.trace.data.cache.CacheConfig;
import rose.mary.trace.data.common.Bot;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace;

/**
 * <pre>
 * The InfinispanCacheManagerProxy is a implementation of {@link CacheManagerProxy}.
 * </pre>
 * 
 * @author whoana
 * @since Sep 2, 2019
 */
public class InfinispanCacheManagerProxy extends CacheManagerProxy {

	DefaultCacheManager cacheManager = null;
 
	
	/**
	 * @param config
	 */
	public InfinispanCacheManagerProxy(CacheManagerConfig config) {
		super(config);
	}
 
	@Override
	public void initialize() throws Exception {

		cacheManager = new DefaultCacheManager();

		String diskPath = System.getProperty("rose.mary.home") + File.separator + config.getDiskPath();

		//----------------------------------------------------------------
		//distributeCache setting
		//----------------------------------------------------------------
		List<CacheConfig> distributeCacheConfigs = config.getDistributeCacheConfigs();
		for(CacheConfig distributeCacheConfig : distributeCacheConfigs) {
			InfinispanCacheProxy<String, Trace> icp = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, distributeCacheConfig);
			distributeCaches.add(icp);
		}
		
		//----------------------------------------------------------------
		//mergeCache setting
		//----------------------------------------------------------------
		CacheConfig mergeCacheConfig = config.getMergeCacheConfig();
		mergeCache = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, mergeCacheConfig);

		
		//----------------------------------------------------------------
		//backupCache setting
		//----------------------------------------------------------------
		CacheConfig backupCacheConfig = config.getBackupCacheConfig();
		backupCache = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, backupCacheConfig);
		
 
		//----------------------------------------------------------------
		//botCacheConfigs setting
		//----------------------------------------------------------------
		List<CacheConfig> botCacheConfigs = config.getBotCacheConfigs();
		for(CacheConfig botCacheConfig : botCacheConfigs) {
			InfinispanCacheProxy<String, State> cache = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, botCacheConfig);
			botCaches.add(cache);
		}
		 

		//----------------------------------------------------------------
		//finCacheConfig setting
		//----------------------------------------------------------------
		CacheConfig finCacheConfig = config.getFinCacheConfig();
		finCache = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, finCacheConfig);
		
	 
		
		
		CacheConfig routingCacheConfig = config.getRoutingCacheConfig();
		routingCache = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, routingCacheConfig);
		
		
		
		//----------------------------------------------------------------
		//errorCache01 setting
		//----------------------------------------------------------------
		CacheConfig errorCache01Config = config.getErrorCache01Config();
		errorCache01 = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, errorCache01Config);
		
	
		//----------------------------------------------------------------
		//errorCache02 setting
		//----------------------------------------------------------------
		CacheConfig errorCache02Config = config.getErrorCache02Config();
		errorCache02 = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, errorCache02Config);
	

		//----------------------------------------------------------------
		//testCache setting
		//----------------------------------------------------------------
		CacheConfig testCacheConfig = config.getTestCacheConfig();
		if(testCacheConfig != null) testCache = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, testCacheConfig);
		
		//----------------------------------------------------------------
		//interfaceCache setting
		//----------------------------------------------------------------
		CacheConfig interfaceCacheConfig = config.getInterfaceCacheConfig();
		if(interfaceCacheConfig != null) interfaceCache = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, interfaceCacheConfig);
	
		//----------------------------------------------------------------
		//unmatchCache setting
		//----------------------------------------------------------------
		CacheConfig unmatchCacheConfig = config.getUnmatchCacheConfig();
		if(unmatchCacheConfig != null) unmatchCache = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, unmatchCacheConfig);

		
		//----------------------------------------------------------------
		//systemErrorCache setting
		//----------------------------------------------------------------
		CacheConfig systemErrorCacheConfig = config.getSystemErrorCacheConfig();
		if(systemErrorCacheConfig != null) systemErrorCache = createCache(diskPath, TransactionMode.NON_TRANSACTIONAL, systemErrorCacheConfig);
		
		
	}

	/**
	 * @param cacheConfig
	 * @return
	 */
	private <K,V> InfinispanCacheProxy<K, V> createCache(String diskPath, TransactionMode mode, CacheConfig cacheConfig) {

		String name = cacheConfig.getName();
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.persistence()
				.passivation(false)
				.addSingleFileStore()
				.maxEntries(-1)
				.preload(true)
				.shared(false)
				.fetchPersistentState(false)
				.ignoreModifications(false)
				.purgeOnStartup(false)
				.location(diskPath + java.io.File.separator + name)
				.memory().evictionStrategy(EvictionStrategy.NONE)
				.memory().size(-1);
		
		//builder.simpleCache(true);
		
		builder.transaction().autoCommit(false);
		builder.transaction().transactionMode(mode);
		
		// Define local cache configuration
		cacheManager.defineConfiguration(name, builder.build());
		// Obtain the local cache
		Cache<K, V> cache = cacheManager.getCache(name);
		cache.getAdvancedCache().getStats().setStatisticsEnabled(true);
		
		
		
		return new InfinispanCacheProxy<K, V>(name, cache);
	}

	@Override
	public void close() throws Exception {
		cacheManager.stop();

	}

	
	

}
