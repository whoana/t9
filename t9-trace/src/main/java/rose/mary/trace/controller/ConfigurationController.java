/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.config.BotLoaderManagerConfig;
import rose.mary.trace.core.config.BoterManagerConfig;
import rose.mary.trace.core.config.CacheManagerConfig;
import rose.mary.trace.core.config.ChannelManagerConfig;
import rose.mary.trace.core.config.Config;
import rose.mary.trace.core.config.InterfaceCacheManagerConfig;
import rose.mary.trace.core.config.LoaderManagerConfig;
import rose.mary.trace.core.config.ServerManagerConfig;
import rose.mary.trace.core.config.TraceErrorHandlerManagerConfig;
import rose.mary.trace.service.ConfigurationService;
 
/**
 * <pre>
 * rose.mary.trace.controller
 * ConfigurationController.java
 * </pre>
 * @author whoana
 * @date Nov 20, 2019
 */
@Controller 
public class ConfigurationController  {
	
	Logger logger = LoggerFactory.getLogger(getClass());
			
	
	@Autowired
	ConfigurationService configurationService;
	
	@CrossOrigin
	@RequestMapping(
			value = "/admin/console/configs", 
			method = RequestMethod.GET, 
			headers = "content-type=application/json")
	public @ResponseBody Config getConfig(
			HttpSession httpSession,
			Locale locale, 
			HttpServletRequest request) throws Throwable{ 
		 
		return configurationService.getConfig();
		
	}

	
	
	@CrossOrigin
	@RequestMapping(
			value = "/admin/console/configs", 
			method = RequestMethod.PUT, 
			headers = "content-type=application/json")
	public @ResponseBody String setConfig(
			@RequestBody Config config,
			HttpSession httpSession,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		try {
			
			logger.debug("config:" + Util.toJSONPrettyString(config));			 
			configurationService.setConfig(config); 
			return "success";
			
		}catch(Exception e) {
			logger.error("",e);
			return "fail:" + e.getMessage();
		}
		
	}
	

	/**
	 * @param channelManagerConfig the channelManagerConfig to set
	 * @throws Exception 
	 */
	public synchronized void setChannelManagerConfig(ChannelManagerConfig channelManagerConfig) throws Exception {
		configurationService.setChannelManagerConfig(channelManagerConfig);
	}
	
	
	/**
	 * @return the cacheManagerConfig
	 */
	public CacheManagerConfig getCacheManagerConfig() {
		return configurationService.getCacheManagerConfig();
	}


	/**
	 * @param cacheManagerConfig the cacheManagerConfig to set
	 * @throws Exception 
	 */
	public synchronized void setCacheManagerConfig(CacheManagerConfig cacheManagerConfig) throws Exception {
		configurationService.setCacheManagerConfig(cacheManagerConfig);
	}
 
	
	
	
	/**
	 * @return the serverManagerConfig
	 */
	public ServerManagerConfig getServerManagerConfig() {
		return configurationService.getServerManagerConfig();
	}


	/**
	 * @param serverManagerConfig the serverManagerConfig to set
	 * @throws Exception 
	 */
	public synchronized void setServerManagerConfig(ServerManagerConfig serverManagerConfig) throws Exception {
		configurationService.setServerManagerConfig(serverManagerConfig);
	}

	
	

	/**
	 * @return the loaderManagerConfig
	 */
	public LoaderManagerConfig getLoaderManagerConfig() {
		return configurationService.getLoaderManagerConfig();
	}


	/**
	 * @param loaderManagerConfig the loaderManagerConfig to set
	 * @throws Exception 
	 */
	public synchronized void setLoaderManagerConfig(LoaderManagerConfig loaderManagerConfig) throws Exception {
		configurationService.setLoaderManagerConfig(loaderManagerConfig);
	}

	
	

	/**
	 * @return the interfaceCacheManagerConfig
	 */
	public InterfaceCacheManagerConfig getInterfaceCacheManagerConfig() {
		return configurationService.getInterfaceCacheManagerConfig();
	}


	/**
	 * @param interfaceCacheManagerConfig the interfaceCacheManagerConfig to set
	 * @throws Exception 
	 */
	public synchronized void setInterfaceCacheManagerConfig(InterfaceCacheManagerConfig interfaceCacheManagerConfig) throws Exception {
		configurationService.setInterfaceCacheManagerConfig(interfaceCacheManagerConfig);
	}
	
	


	/**
	 * @return the boterManagerConfig
	 */
	public BoterManagerConfig getBoterManagerConfig() {
		return configurationService.getBoterManagerConfig();
	}


	/**
	 * @param boterManagerConfig the boterManagerConfig to set
	 * @throws Exception 
	 */
	public synchronized void setBoterManagerConfig(BoterManagerConfig boterManagerConfig) throws Exception {
		configurationService.setBoterManagerConfig(boterManagerConfig);
	}

	
	

	/**
	 * @return the botLoaderManagerConfig
	 */
	public BotLoaderManagerConfig getBotLoaderManagerConfig() {
		return configurationService.getBotLoaderManagerConfig();
	}


	/**
	 * @param botLoaderManagerConfig the botLoaderManagerConfig to set
	 * @throws Exception 
	 */
	public synchronized void setBotLoaderManagerConfig(BotLoaderManagerConfig botLoaderManagerConfig) throws Exception {
		configurationService.setBotLoaderManagerConfig(botLoaderManagerConfig);
	}

	
	

	/**
	 * @return the traceErrorHandlerManagerConfig
	 */
	public TraceErrorHandlerManagerConfig getTraceErrorHandlerManagerConfig() {
		return configurationService.getTraceErrorHandlerManagerConfig();
	}


	/**
	 * @param traceErrorHandlerManagerConfig the traceErrorHandlerManagerConfig to set
	 * @throws Exception 
	 */
	public synchronized void setTraceErrorHandlerManagerConfig(TraceErrorHandlerManagerConfig traceErrorHandlerManagerConfig) throws Exception {
		configurationService.setTraceErrorHandlerManagerConfig(traceErrorHandlerManagerConfig);
	}
	
	
}
