/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.manager;
 


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rose.mary.trace.apps.cache.CacheManagerProxy;
import rose.mary.trace.apps.cache.CacheProxy;
import rose.mary.trace.apps.cache.chronicle.ChronicleCacheManagerProxy;
import rose.mary.trace.apps.cache.ehcache.EhcacheManagerProxy;
import rose.mary.trace.apps.cache.infinispan.InfinispanCacheManagerProxy;
import rose.mary.trace.apps.manager.config.CacheManagerConfig;
import rose.mary.trace.data.common.InterfaceInfo;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.data.common.Unmatch;
import rose.mary.trace.exception.NotYetImplementedException;
import rose.mary.trace.system.SystemError;

/**
 * <pre>
 * rose.mary.trace.manager
 * CacheManager.java
 * </pre>
 * 
 * @author whoana
 * @date Aug 5, 2019
 */
public class CacheManager {
	

	Logger logger = LoggerFactory.getLogger(CacheManager.class);
	//Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");
	
	
	CacheManagerConfig cacheManagerConfig = null;

	CacheManagerProxy cmp = null; 
	
	List<CacheProxy<String, Trace>> distributeCaches = null;
	CacheProxy<String, Trace> mergeCache = null;
	CacheProxy<String, Trace> backupCache = null; 
	
	CacheProxy<String, Integer> routingCache = null;
	 
	List<CacheProxy<String, State>> botCaches = null;
	
	CacheProxy<String, State> finCache = null; 
	
	CacheProxy<String, Trace> errorCache01 = null;
	CacheProxy<String, State> errorCache02 = null;
	
	CacheProxy<String, Trace> testCache = null;
	 
	CacheProxy<String, InterfaceInfo> interfaceCache = null;
	
	CacheProxy<String, Unmatch> unmatchCache = null;
	
	CacheProxy<String, SystemError> systemErrorCache = null;
	
	Map<String, CacheProxy> cacheMap = new LinkedHashMap<String, CacheProxy>();
	
	public CacheManager(CacheManagerConfig cacheManagerConfig) {
		this.cacheManagerConfig = cacheManagerConfig;
	}
	
	public void prepare() throws Exception { 
		
		if(CacheManagerConfig.VENDOR_EHCACHE.equalsIgnoreCase(cacheManagerConfig.getVendor())) {
			cmp = new EhcacheManagerProxy(cacheManagerConfig);
		}else if(CacheManagerConfig.VENDOR_INFINISPAN.equalsIgnoreCase(cacheManagerConfig.getVendor())){
			cmp = new InfinispanCacheManagerProxy(cacheManagerConfig);
		}else if(CacheManagerConfig.VENDOR_CHRONICLE.equalsIgnoreCase(cacheManagerConfig.getVendor())){
			cmp = new ChronicleCacheManagerProxy(cacheManagerConfig);
		}else {
			throw new NotYetImplementedException();
		}
		cmp.initialize();
		distributeCaches = cmp.getDistributeCaches();
		mergeCache = cmp.getMergeCache();
		backupCache = cmp.getBackupCache();
		
		
		botCaches = cmp.getBotCaches();
		finCache  = cmp.getFinCache(); 
				
		routingCache = cmp.getRoutingCache();
		errorCache01 = cmp.getErrorCache01();
		errorCache02 = cmp.getErrorCache02();
		interfaceCache = cmp.getInterfaceCache();
		unmatchCache = cmp.getUnmatchCache();
		systemErrorCache = cmp.getSystemErrorCache();
		
		for(CacheProxy distributeCache : distributeCaches) {
			cacheMap.put(distributeCache.getName(), distributeCache);	
		}
		
		cacheMap.put(mergeCache.getName(), 	mergeCache);
		cacheMap.put(backupCache.getName(), backupCache);
		 
 

		for(CacheProxy botCache : botCaches) {
			cacheMap.put(botCache.getName(), botCache);	
		} 
 
		cacheMap.put(finCache.getName(), 		finCache);		
		cacheMap.put(routingCache.getName(), 	routingCache);
		cacheMap.put(errorCache01.getName(), 	errorCache01);
		cacheMap.put(errorCache02.getName(), 	errorCache02);
		cacheMap.put(interfaceCache.getName(), 	interfaceCache);
		cacheMap.put(unmatchCache.getName(), 	unmatchCache);
		cacheMap.put(systemErrorCache.getName(),systemErrorCache);
		
//		logger.info("All Caches are prepared.");		
//		Collection<CacheProxy> caches = cacheMap.values();
//		for (CacheProxy cache : caches) {
//			logger.info("The cache "+ cache.getName() + " is prepared.");
//		}
	}

	/**
	 * @return the testCache
	 */
	public CacheProxy<String, Trace> getTestCache() {
		return testCache;
	}

	public List<CacheProxy<String, Trace>> getDistributeCaches() {
		return distributeCaches;
	}
	
	

	/**
	 * @return
	 */
	public CacheProxy<String, Trace> getMergeCache() {
		return mergeCache;
	}

	
	
	/**
	 * @return the errorCache01
	 */
	public CacheProxy<String, Trace> getErrorCache01() {
		return errorCache01;
	}

	/**
	 * @return the errorCache02
	 */
	public CacheProxy<String, State> getErrorCache02() {
		return errorCache02;
	}

	/**
	 * @return the interfaceCache
	 */
	public CacheProxy<String, InterfaceInfo> getInterfaceCache() {
		return interfaceCache;
	}
	
	/**
	 * 
	 */
	public void closeCache() {
		try {
			cmp.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public CacheProxy<String, Trace> getBackupCache() {
		return backupCache;
	}
 

	/**
	 * @param cacheName
	 * @return
	 */
	public CacheProxy<String,?> getCache(String cacheName) {
		return cacheMap.get(cacheName);
	}

 	 

	public CacheProxy<String, Integer> getRoutingCache() {
		return routingCache;
	}
	
	 
	
	public Collection<CacheProxy> caches(){
		return cacheMap.values();
	}

	public List<CacheProxy<String, State>> getBotCaches() {
		return botCaches;
	}

	public CacheProxy<String, State> getFinCache() {
		return finCache;
	}

	public CacheProxy<String, Unmatch> getUnmatchCache() {
		return unmatchCache;
	}
	
	public CacheProxy<String, SystemError> getSystemErrorCache() {
		return systemErrorCache;
	}
 
}
