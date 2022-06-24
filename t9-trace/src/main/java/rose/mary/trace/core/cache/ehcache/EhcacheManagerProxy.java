/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.cache.ehcache;

import java.io.File;
import java.util.List;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.service.StatisticsService; 
import org.ehcache.expiry.ExpiryPolicy;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;
import org.ehcache.impl.internal.statistics.DefaultStatisticsService;

import rose.mary.trace.core.cache.CacheManagerProxy;
import rose.mary.trace.core.config.CacheManagerConfig;
import rose.mary.trace.data.cache.CacheConfig;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.data.common.Unmatch;
import rose.mary.trace.system.SystemError;

/**
 * <pre>
 * The EhcacheManagerProxy is a implementation of {@link CacheManagerProxy}.
 * </pre>
 * @author whoana
 * @since Aug 30, 2019
 */
public class EhcacheManagerProxy extends CacheManagerProxy {
	 
	CacheManager cacheManager = null;

	public EhcacheManagerProxy(CacheManagerConfig config){
		super(config);
	}
	
	@Override
	public void initialize() throws Exception {

		
		StatisticsService statisticsService = new DefaultStatisticsService();
		 
		
		CacheManagerBuilder<CacheManager> cmb = CacheManagerBuilder.newCacheManagerBuilder();
		cmb.using(statisticsService);
		
		if(config.isPersistant()) {
			String diskPath = config.getDiskPath();
			cmb.with(new CacheManagerPersistenceConfiguration(new File(System.getProperty("rose.mary.home"), diskPath)));
		}
		
 
		//----------------------------------------------------------------
		//distributeCache setting
		//----------------------------------------------------------------
		List<CacheConfig> distributeCacheConfigs = config.getDistributeCacheConfigs();
		for(CacheConfig distributeCacheConfig : distributeCacheConfigs) {		
			setCache(cmb, distributeCacheConfig, config.isPersistant());			
		}
		
		//----------------------------------------------------------------
		//mergeCache setting
		//----------------------------------------------------------------
		CacheConfig mergeCacheConfig = config.getMergeCacheConfig();
		setCache(cmb, mergeCacheConfig, config.isPersistant());		
		
		//----------------------------------------------------------------
		//backupCache setting
		//----------------------------------------------------------------
		CacheConfig backupCacheConfig = config.getBackupCacheConfig();
		setCache(cmb, backupCacheConfig, config.isPersistant());		
		 

		//----------------------------------------------------------------
		//botCache setting
		//----------------------------------------------------------------
		List<CacheConfig> botCacheConfigs = config.getBotCacheConfigs();
		for(CacheConfig botCacheConfig : botCacheConfigs) {		
			setCache(cmb, botCacheConfig, config.isPersistant());			
		}
		//----------------------------------------------------------------
		//finCache setting
		//----------------------------------------------------------------
		CacheConfig finCacheConfig = config.getFinCacheConfig();
		setCache(cmb, finCacheConfig, config.isPersistant());

		//----------------------------------------------------------------
		//stateCache setting
		//----------------------------------------------------------------
		CacheConfig stateCacheConfig = config.getStateCacheConfig();
		setCache(cmb, stateCacheConfig, config.isPersistant());
		
		//----------------------------------------------------------------
		//routingCache setting
		//----------------------------------------------------------------
		CacheConfig routingCacheConfig = config.getRoutingCacheConfig();
		setCache(cmb, routingCacheConfig, config.isPersistant());
		
		//----------------------------------------------------------------
		//errorCache01 setting
		//----------------------------------------------------------------
		CacheConfig errorCache01Config = config.getErrorCache01Config();
		setCache(cmb, errorCache01Config, config.isPersistant());	
		
		//----------------------------------------------------------------
		//errorCache02 setting
		//----------------------------------------------------------------
		CacheConfig errorCache02Config = config.getErrorCache02Config();
		setCache(cmb, errorCache02Config, config.isPersistant());	
		
		//----------------------------------------------------------------
		//unmatchCache setting
		//----------------------------------------------------------------
		CacheConfig unmatchCacheConfig = config.getUnmatchCacheConfig();
		setCache(cmb, unmatchCacheConfig, config.isPersistant());	
		
		
		//----------------------------------------------------------------
		//systemErrorCache setting
		//----------------------------------------------------------------
		CacheConfig systemErrorCacheConfig = config.getSystemErrorCacheConfig();
		setCache(cmb, systemErrorCacheConfig, config.isPersistant());	
		
		
		cacheManager = cmb.build(true);
		
		 
		for(CacheConfig distributeCacheConfig : distributeCacheConfigs) {	
			Cache<String, Trace> c = cacheManager.getCache(distributeCacheConfig.getName(), String.class, Trace.class);
			distributeCaches.add(new EhcacheProxy<>(distributeCacheConfig.getName(), c));
		}
		
		{
			Cache<String, Trace> c = cacheManager.getCache(mergeCacheConfig.getName(), String.class, Trace.class);
			mergeCache = new EhcacheProxy<>(mergeCacheConfig.getName(),c);
		}
		
		{
			Cache<String, Trace> c = cacheManager.getCache(errorCache01Config.getName(), String.class, Trace.class);
			errorCache01 = new EhcacheProxy<>(errorCache01Config.getName(),c);
		}
		
		{
			Cache<String, State> c = cacheManager.getCache(errorCache02Config.getName(), String.class, State.class);
			errorCache02 = new EhcacheProxy<>(errorCache02Config.getName(),c);
		}
		
		{
			Cache<String, Trace> c = cacheManager.getCache(backupCacheConfig.getName(), String.class, Trace.class);
			backupCache = new EhcacheProxy<>(backupCacheConfig.getName(),c);
		}
				
		{
			Cache<String, Integer> c = cacheManager.getCache(routingCacheConfig.getName(), String.class, Integer.class);
			routingCache = new EhcacheProxy<>(routingCacheConfig.getName(),c);
		}
		
		for(CacheConfig botCacheConfig : botCacheConfigs) {	
			Cache<String, State> c = cacheManager.getCache(botCacheConfig.getName(), String.class, State.class);
			botCaches.add(new EhcacheProxy<>(botCacheConfig.getName(), c));
		}
		
		{
			Cache<String, State> c = cacheManager.getCache(finCacheConfig.getName(), String.class, State.class);
			finCache = new EhcacheProxy<>(finCacheConfig.getName(),c);
		}
		 
		{
			Cache<String, Unmatch> c = cacheManager.getCache(unmatchCacheConfig.getName(), String.class, Unmatch.class);
			unmatchCache = new EhcacheProxy<>(unmatchCacheConfig.getName(),c);
		}
		
		{
			Cache<String, SystemError> c = cacheManager.getCache(systemErrorCacheConfig.getName(), String.class, SystemError.class);
			systemErrorCache = new EhcacheProxy<>(systemErrorCacheConfig.getName(),c);
		}
		
	}

	
	private void setCache(CacheManagerBuilder<CacheManager> cmb, CacheConfig cfg, boolean persistant) throws Exception{
		 
		String cacheName = cfg.getName(); 
		int heapSize     = cfg.getHeapSize();
		int diskSize     = cfg.getDiskSize();
		
		MemoryUnit unit  = MemoryUnit.GB;
		
		if(cfg.getMemoryUnit() == CacheConfig.MEM_UNIT_B) {
			unit = MemoryUnit.B;
		}else if(cfg.getMemoryUnit() == CacheConfig.MEM_UNIT_K) {
			unit = MemoryUnit.KB;
		}else if(cfg.getMemoryUnit() == CacheConfig.MEM_UNIT_M) {
			unit = MemoryUnit.MB;
		}else if(cfg.getMemoryUnit() == CacheConfig.MEM_UNIT_G) {
			unit = MemoryUnit.GB;
		}
		
		if(persistant) {
			cmb.withCache(
					cacheName, 
					CacheConfigurationBuilder.newCacheConfigurationBuilder(
							String.class, 
							Trace.class,
							ResourcePoolsBuilder.heap(heapSize).disk(diskSize, unit, true)
					).withExpiry(ExpiryPolicy.NO_EXPIRY).build());			 
		} else {
			cmb.withCache(
					cacheName, 
					CacheConfigurationBuilder.newCacheConfigurationBuilder(
							String.class, 
							Trace.class,
							ResourcePoolsBuilder.heap(heapSize).disk(diskSize, unit, true)
					).withExpiry(ExpiryPolicy.NO_EXPIRY).build());
		}
	}
	

	@Override
	public void close() throws Exception {
		cacheManager.close();
	}

}
