/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rose.mary.trace.apps.manager.ConfigurationManager;
import rose.mary.trace.apps.manager.config.BotLoaderManagerConfig;
import rose.mary.trace.apps.manager.config.BoterManagerConfig;
import rose.mary.trace.apps.manager.config.CacheManagerConfig;
import rose.mary.trace.apps.manager.config.ChannelManagerConfig;
import rose.mary.trace.apps.manager.config.Config;
import rose.mary.trace.apps.manager.config.InterfaceCacheManagerConfig;
import rose.mary.trace.apps.manager.config.LoaderManagerConfig;
import rose.mary.trace.apps.manager.config.ServerManagerConfig;
import rose.mary.trace.apps.manager.config.TraceErrorHandlerManagerConfig;

/**
 * <pre>
 * rose.mary.trace.service
 * ConfigurationService.java
 * </pre>
 * @author whoana
 * @date Nov 20, 2019
 */

@Service
public class ConfigurationService {
	
	@Autowired ConfigurationManager configurationManager;
	
	
	public Config getConfig() {
		return configurationManager.getConfig();
	}
	
	public void setConfig(Config config) throws Exception {
		configurationManager.setConfig(config);
	}
	

	/**
	 * @return the channelManagerConfig
	 */
	public ChannelManagerConfig getChannelManagerConfig() {
		return configurationManager.getChannelManagerConfig();
	}


	/**
	 * @param channelManagerConfig the channelManagerConfig to set
	 * @throws Exception 
	 */
	public void setChannelManagerConfig(ChannelManagerConfig channelManagerConfig) throws Exception {
		configurationManager.setChannelManagerConfig(channelManagerConfig);
	}
	
	
	/**
	 * @return the cacheManagerConfig
	 */
	public CacheManagerConfig getCacheManagerConfig() {
		return configurationManager.getCacheManagerConfig();
	}


	/**
	 * @param cacheManagerConfig the cacheManagerConfig to set
	 * @throws Exception 
	 */
	public void setCacheManagerConfig(CacheManagerConfig cacheManagerConfig) throws Exception {
		configurationManager.setCacheManagerConfig(cacheManagerConfig);
	}
 
	
	
	
	/**
	 * @return the serverManagerConfig
	 */
	public ServerManagerConfig getServerManagerConfig() {
		return configurationManager.getServerManagerConfig();
	}


	/**
	 * @param serverManagerConfig the serverManagerConfig to set
	 * @throws Exception 
	 */
	public void setServerManagerConfig(ServerManagerConfig serverManagerConfig) throws Exception {
		configurationManager.setServerManagerConfig(serverManagerConfig);
	}

	
	

	/**
	 * @return the loaderManagerConfig
	 */
	public LoaderManagerConfig getLoaderManagerConfig() {
		return configurationManager.getLoaderManagerConfig();
	}


	/**
	 * @param loaderManagerConfig the loaderManagerConfig to set
	 * @throws Exception 
	 */
	public void setLoaderManagerConfig(LoaderManagerConfig loaderManagerConfig) throws Exception {
		configurationManager.setLoaderManagerConfig(loaderManagerConfig);
	}

	
	

	/**
	 * @return the interfaceCacheManagerConfig
	 */
	public InterfaceCacheManagerConfig getInterfaceCacheManagerConfig() {
		return configurationManager.getInterfaceCacheManagerConfig();
	}


	/**
	 * @param interfaceCacheManagerConfig the interfaceCacheManagerConfig to set
	 * @throws Exception 
	 */
	public void setInterfaceCacheManagerConfig(InterfaceCacheManagerConfig interfaceCacheManagerConfig) throws Exception {
		configurationManager.setInterfaceCacheManagerConfig(interfaceCacheManagerConfig);
	}
	
	


	/**
	 * @return the boterManagerConfig
	 */
	public BoterManagerConfig getBoterManagerConfig() {
		return configurationManager.getBoterManagerConfig();
	}


	/**
	 * @param boterManagerConfig the boterManagerConfig to set
	 * @throws Exception 
	 */
	public void setBoterManagerConfig(BoterManagerConfig boterManagerConfig) throws Exception {
		configurationManager.setBoterManagerConfig(boterManagerConfig);
	}

	
	

	/**
	 * @return the botLoaderManagerConfig
	 */
	public BotLoaderManagerConfig getBotLoaderManagerConfig() {
		return configurationManager.getBotLoaderManagerConfig();
	}


	/**
	 * @param botLoaderManagerConfig the botLoaderManagerConfig to set
	 * @throws Exception 
	 */
	public void setBotLoaderManagerConfig(BotLoaderManagerConfig botLoaderManagerConfig) throws Exception {
		configurationManager.setBotLoaderManagerConfig(botLoaderManagerConfig);
	}

	
	

	/**
	 * @return the traceErrorHandlerManagerConfig
	 */
	public TraceErrorHandlerManagerConfig getTraceErrorHandlerManagerConfig() {
		return configurationManager.getTraceErrorHandlerManagerConfig();
	}


	/**
	 * @param traceErrorHandlerManagerConfig the traceErrorHandlerManagerConfig to set
	 * @throws Exception 
	 */
	public void setTraceErrorHandlerManagerConfig(TraceErrorHandlerManagerConfig traceErrorHandlerManagerConfig) throws Exception {
		configurationManager.setTraceErrorHandlerManagerConfig(traceErrorHandlerManagerConfig);
	}
	
}
