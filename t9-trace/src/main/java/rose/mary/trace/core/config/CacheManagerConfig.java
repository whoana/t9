/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.config;
 
 

import java.util.List;

import rose.mary.trace.core.data.cache.CacheConfig; 
 
 
/**
 * <pre>
 * rose.mary.trace.envs
 * CacheEnvs.java
 * </pre>
 * @author whoana
 * @date Aug 9, 2019
 */
public class CacheManagerConfig {
	   
	public static final String VENDOR_EHCACHE = "ehcache";
	
	public static final String VENDOR_INFINISPAN = "infinispan";
	
	public static final String VENDOR_CHRONICLE = "chronicle";
	
	
	String vendor = VENDOR_INFINISPAN;
	
	String diskPath;
	
	boolean persistant = true;
	  
	List<CacheConfig> distributeCacheConfigs;
	
	CacheConfig mergeCacheConfig;
	
	CacheConfig backupCacheConfig;
	
	List<CacheConfig> botCacheConfigs;
	
	CacheConfig finCacheConfig;
	
	CacheConfig stateCacheConfig;
	
	CacheConfig routingCacheConfig;
	
	CacheConfig errorCache01Config;
	
	CacheConfig errorCache02Config;

	CacheConfig testCacheConfig;
	
	CacheConfig interfaceCacheConfig;
	
	CacheConfig unmatchCacheConfig;
	
	CacheConfig systemErrorCacheConfig;
	
	/**
	 * @return the testCacheConfig
	 */
	public CacheConfig getTestCacheConfig() {
		return testCacheConfig;
	}

	/**
	 * @param testCacheConfig the testCacheConfig to set
	 */
	public void setTestCacheConfig(CacheConfig testCacheConfig) {
		this.testCacheConfig = testCacheConfig;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the diskPath
	 */
	public String getDiskPath() {
		return diskPath;
	}

	/**
	 * @param diskPath the diskPath to set
	 */
	public void setDiskPath(String diskPath) {
		this.diskPath = diskPath;
	}

	/**
	 * @return the persistant
	 */
	public boolean isPersistant() {
		return persistant;
	}

	/**
	 * @param persistant the persistant to set
	 */
	public void setPersistant(boolean persistant) {
		this.persistant = persistant;
	}

	

	/**
	 * @return the distributeCacheConfigs
	 */
	public List<CacheConfig> getDistributeCacheConfigs() {
		return distributeCacheConfigs;
	}

	/**
	 * @param distributeCacheConfigs the distributeCacheConfigs to set
	 */
	public void setDistributeCacheConfigs(List<CacheConfig> distributeCacheConfigs) {
		this.distributeCacheConfigs = distributeCacheConfigs;
	}

	/**
	 * @return the mergeCacheConfig
	 */
	public CacheConfig getMergeCacheConfig() {
		return mergeCacheConfig;
	}

	/**
	 * @param mergeCacheConfig the mergeCacheConfig to set
	 */
	public void setMergeCacheConfig(CacheConfig mergeCacheConfig) {
		this.mergeCacheConfig = mergeCacheConfig;
	}

	/**
	 * @return the errorCache01Config
	 */
	public CacheConfig getErrorCache01Config() {
		return errorCache01Config;
	}

	/**
	 * @param errorCache01Config the errorCache01Config to set
	 */
	public void setErrorCache01Config(CacheConfig errorCache01Config) {
		this.errorCache01Config = errorCache01Config;
	}

	/**
	 * @return the errorCache02Config
	 */
	public CacheConfig getErrorCache02Config() {
		return errorCache02Config;
	}

	/**
	 * @param errorCache02Config the errorCache02Config to set
	 */
	public void setErrorCache02Config(CacheConfig errorCache02Config) {
		this.errorCache02Config = errorCache02Config;
	}

	
	public void setInterfaceCacheConfig(CacheConfig interfaceCacheConfig) {
		this.interfaceCacheConfig = interfaceCacheConfig;
	}
	
	
	/**
	 * @return
	 */
	public CacheConfig getInterfaceCacheConfig() {
		return interfaceCacheConfig;
	}

	/**
	 * @return the backupCacheConfig
	 */
	public CacheConfig getBackupCacheConfig() {
		return backupCacheConfig;
	}

	/**
	 * @param backupCacheConfig the backupCacheConfig to set
	 */
	public void setBackupCacheConfig(CacheConfig backupCacheConfig) {
		this.backupCacheConfig = backupCacheConfig;
	}
 
	/**
	 * 
	 * @return
	 */
	public List<CacheConfig> getBotCacheConfigs() {
		return botCacheConfigs;
	}

	/**
	 * 
	 * @param botCacheConfigs
	 */
	public void setBotCacheConfigs(List<CacheConfig> botCacheConfigs) {
		this.botCacheConfigs = botCacheConfigs;
	}

	/**
	 * @return the routingCacheConfig
	 */
	public CacheConfig getRoutingCacheConfig() {
		return routingCacheConfig;
	}

	/**
	 * @param routingCacheConfig the routingCacheConfig to set
	 */
	public void setRoutingCacheConfig(CacheConfig routingCacheConfig) {
		this.routingCacheConfig = routingCacheConfig;
	}


	public CacheConfig getFinCacheConfig() {
		return finCacheConfig;
	}

	public void setFinCacheConfig(CacheConfig finCacheConfig) {
		this.finCacheConfig = finCacheConfig;
	}

	
	
	public void setStateCacheConfig(CacheConfig stateCacheConfig) {
		this.stateCacheConfig = stateCacheConfig;
	}

	public CacheConfig getStateCacheConfig() {
		return stateCacheConfig;
	}

	public CacheConfig getUnmatchCacheConfig() {
		return unmatchCacheConfig;
	}

	public void setUnmatchCacheConfig(CacheConfig unmatchCacheConfig) {
		this.unmatchCacheConfig = unmatchCacheConfig;
	}

	public CacheConfig getSystemErrorCacheConfig() {
		return systemErrorCacheConfig;
	}

	public void setSystemErrorCacheConfig(CacheConfig systemErrorCacheConfig) {
		this.systemErrorCacheConfig = systemErrorCacheConfig;
	}

	
	
 
	
}
