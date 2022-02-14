/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.cache;

import java.util.ArrayList;
import java.util.List;
 
import rose.mary.trace.apps.manager.config.CacheManagerConfig;
import rose.mary.trace.data.common.InterfaceInfo;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.data.common.Unmatch;
import rose.mary.trace.system.SystemError;

/**
 * <pre>
 * The CacheManagerProxy is a abstract class for managing caches.
 * </pre>
 * @author whoana
 * @since Aug 30, 2019
 */
public abstract class CacheManagerProxy {
	
	protected CacheManagerConfig config;
	
	protected List<CacheProxy<String, Trace>> distributeCaches = new ArrayList<CacheProxy<String, Trace>>();
	
	protected CacheProxy<String, Trace> mergeCache = null;
	
	protected CacheProxy<String, Trace> backupCache = null;
	
	protected CacheProxy<String, Trace> errorCache01 = null;
	
	protected List<CacheProxy<String, State>> botCaches = new ArrayList<CacheProxy<String, State>>();
	
	protected CacheProxy<String, State> finCache = null;
	
	protected CacheProxy<String, Integer> routingCache = null;
	
	protected CacheProxy<String, State> errorCache02 = null;

	protected CacheProxy<String, Trace> testCache = null;
	
	protected CacheProxy<String, InterfaceInfo> interfaceCache = null;
	
	protected CacheProxy<String, Unmatch> unmatchCache = null;
	
	protected CacheProxy<String, SystemError> systemErrorCache = null;
	
	public CacheManagerProxy(CacheManagerConfig config) {
		this.config = config;
	}
	
	public abstract void initialize() throws Exception;
	
	public abstract void close() throws Exception; 
	
	
	/**
	 * @return the distributeCaches
	 */
	public List<CacheProxy<String, Trace>> getDistributeCaches() {
		return distributeCaches;
	}

	/**
	 * @return the mergeCache
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
	 * @return testCache
	 */
	public CacheProxy<String, Trace> getTestCache(){
		return testCache ;
	}

	/**
	 * @return the interfaceCache
	 */
	public CacheProxy<String, InterfaceInfo> getInterfaceCache() {
		return interfaceCache;
	}

	/**
	 * @param interfaceCache the interfaceCache to set
	 */
	public void setInterfaceCache(CacheProxy<String, InterfaceInfo> interfaceCache) {
		this.interfaceCache = interfaceCache;
	}

	/**
	 * @return
	 */
	public CacheProxy<String, Trace> getBackupCache(){
		return backupCache;
	}
 
	/**
	 * @return
	 */
	public List<CacheProxy<String, State>> getBotCaches() {
		return botCaches;
	}

	/**
	 * @return
	 */
	public CacheProxy<String, Integer> getRoutingCache() {
		return routingCache;
	}
 

	public CacheProxy<String, State> getFinCache() {
		return finCache;
	}
	
	public CacheProxy<String, Unmatch> getUnmatchCache() {
		return unmatchCache;
	}

	public CacheProxy<String, SystemError> getSystemErrorCache(){
		return systemErrorCache;
	}
	
}
