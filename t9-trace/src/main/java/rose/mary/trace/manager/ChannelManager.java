/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import pep.per.mint.common.util.Util;
import rose.mary.trace.channel.rest.RestChannel;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.channel.Channel;
import rose.mary.trace.core.channel.ChannelExceptionHandler;
import rose.mary.trace.core.channel.mom.MOMChannel;
import rose.mary.trace.core.channel.mom.MOMChannelExceptionHandler;
import rose.mary.trace.core.config.ChannelManagerConfig;
import rose.mary.trace.core.config.OldStateCheckHandlerConfig;
import rose.mary.trace.core.data.channel.ChannelConfig;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.helper.module.mte.MsgHandler;
import rose.mary.trace.core.monitor.ThroughputMonitor;
import rose.mary.trace.core.parser.BytesMessageParser;
import rose.mary.trace.core.parser.MQMessageParser;
import rose.mary.trace.core.parser.Parser;
import rose.mary.trace.core.parser.RestMessageParser;

/**
 * <pre>
 * rose.mary.trace.manager.channel
 * ChannelManager.java
 * </pre>
 * @author whoana
 * @since Jul 29, 2019
 */
public class ChannelManager {
	public static final int STATE_CHANNEL_NOTHING  = 0;
	public static final int STATE_CHANNEL_STARTING = 10;
	public static final int STATE_CHANNEL_STARTTED = 11;
	public static final int STATE_CHANNEL_ERROR_ON_START = 12;
	public static final int STATE_CHANNEL_STOPPING = 20;
	public static final int STATE_CHANNEL_STOPPED  = 21;
	public static final int STATE_CHANNEL_ERROR_ON_STOP = 22;
	
	//Logger logger = LoggerFactory.getLogger(ChannelManager.class);
	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");
	
	ChannelManagerConfig config;
	
	List<Channel> channels = new ArrayList<Channel>();
	
	CacheManager cacheManager = null;
	
	int state = STATE_CHANNEL_NOTHING;
	
	long delayOnException  = 5000;
	
	ThroughputMonitor tpm; 

	ChannelExceptionHandler momChannelExceptionHandler = null;
	ChannelExceptionHandler restChannelExceptionHandler = null;
	
	
	ThreadPoolTaskExecutor taskExecutor;
	
	public ChannelManager(ChannelManagerConfig config, CacheManager cacheManager, ThroughputMonitor tpm, ThreadPoolTaskExecutor taskExecutor) {
		this.config = config;
		this.cacheManager = cacheManager;
		this.tpm = tpm;
		this.momChannelExceptionHandler = new MOMChannelExceptionHandler(config.isTranslateMsgOnException());
		this.delayOnException = config.getDelayOnException();
		this.taskExecutor = taskExecutor;

	}
	
	
	boolean channelsStarted = false;
	
	/**
	 * @throws Exception 
	 */
	public synchronized void startChannels() throws Exception {
		
		try {
			
			stopChannels();
			
			state = STATE_CHANNEL_STARTING;
			
			channels = new ArrayList<Channel>();
			List<ChannelConfig> channelConfigs = config.getChannelConfigs();
			
			logger.info("channels starting");
			for(ChannelConfig cc : channelConfigs) {
				
				if(cc.isDisable()) continue;
				
				switch(cc.getType()) {
				case Channel.TYPE_WMQ : 
					startMOMChannel(cc, momChannelExceptionHandler, config.getOldStateCheckHandlerConfig());
					break;
				case Channel.TYPE_ILINK :
					startMOMChannel(cc, momChannelExceptionHandler, config.getOldStateCheckHandlerConfig());
					break;
				case Channel.TYPE_REST :
					//throw new Exception("REST 유형 채널은 아직 구현안함.");
					startRestChannel(cc, restChannelExceptionHandler, config.getOldStateCheckHandlerConfig());
					break;			
				case Channel.TYPE_TCP :
					throw new Exception("TCP 유형 채널은 아직 구현안함.");
					//break;			
					
				case Channel.TYPE_DB :
					throw new Exception("DB 유형 채널은 아직 구현안함.");
					//break;			
					
				case Channel.TYPE_FILE :
					throw new Exception("파일 유형 채널은 아직 구현안함.");
					//break;				
				default : 
					throw new Exception(Util.join("Unknown Channel type:", cc.getType()));
					//break;			
				}
			}
			channelsStarted = true;
			state = STATE_CHANNEL_STARTTED;
			logger.info("channels started");
		}catch(Exception e) {
			state = STATE_CHANNEL_ERROR_ON_START;
			throw e;
		}finally {
			
		}
	}
	
	RestChannel restChannel = null;
	
	public RestChannel getRestChannel() {
		return restChannel;
	}
	
	/**
	 * @param config
	 * @param cache
	 * @return
	 * @throws Exception 
	 */
	private void startRestChannel(ChannelConfig config, ChannelExceptionHandler channelExceptionHandler, OldStateCheckHandlerConfig oschc) throws Exception {
		String name = config.getName();
		boolean healthCheck = config.isHealthCheck();
		 
		
		int idx = 0;
		List<CacheProxy<String, Trace>> caches = cacheManager.getDistributeCaches();
		
		int[] cacheIndex = config.getCacheIndex();
		
		List<CacheProxy<String, Trace>> selectCaches = new ArrayList<CacheProxy<String, Trace>>();
		
		//캐시설정정보보다 작으면 
		if(cacheIndex == null || cacheIndex.length == 0) {
			CacheProxy<String, Trace> c = caches.get(0);
			if(c == null)  throw new Exception(Util.join("NotFoundCacheException(index:",0,")"));
			selectCaches.add(c);	
		}else {			
			for(int i : cacheIndex) {
				CacheProxy<String, Trace> c = caches.get(i);
				if(c == null)  throw new Exception(Util.join("NotFoundCacheException(index:",0,")"));
				selectCaches.add(c);				
			}			
		}
		 
		
		
		restChannel = new RestChannel(name, config.getMaxCacheSize(), config.getDelayForMaxCache(), selectCaches, taskExecutor);
		if(tpm != null) restChannel.setThroughputMonitor(tpm);
		Map<String, Integer> nodeMap = oschc.getNodeMap();
		Parser parser = new RestMessageParser(nodeMap);
		
		restChannel.setParser(parser);
		restChannel.setHealthCheck(healthCheck);
		restChannel.setChannelExceptionHandler(channelExceptionHandler);
		channels.add(restChannel); 	
		restChannel.start();
		
		logger.info(Util.join("channel(", restChannel.getName(), ") started"));


	}
	 

	/**
	 * @param config
	 * @param cache
	 * @return
	 * @throws Exception 
	 */
	private void startMOMChannel(ChannelConfig config, ChannelExceptionHandler channelExceptionHandler, OldStateCheckHandlerConfig oschc) throws Exception {
		String name = config.getName();
		String hostName = config.getHostName();
		String qmgrName = config.getQmgrName();
		int port = config.getPort();
		String channelName = config.getChannelName();
		String queueName = config.getQueueName();
		int waitTime = config.getWaitTime();
		String module = config.getModule(); 
		String userId = config.getUserId();
		String password = config.getPassword();
		boolean healthCheck = config.isHealthCheck();
		
		int ccsid = config.getCcsid();
		int characterSet = config.getCharacterSet();
		boolean autoCommit = config.isAutoCommit();
		boolean bindMode = config.isBindMode();
		
		Map<String, Integer> nodeMap = oschc.getNodeMap();
	
		Parser parser = null;
		if(MsgHandler.MODULE_MQ.equalsIgnoreCase(module)) {
			parser = new MQMessageParser(nodeMap);
		}else if(MsgHandler.MODULE_ILINK.equalsIgnoreCase(module)){
			parser = new BytesMessageParser(nodeMap);
		}else {
			throw new Exception("NotFoundParserException");
		}
		
		long maxCommitWait = config.getMaxCommitWait(); 
		long delayForNoMessage = config.getDelayForNoMessage();
		
		int commitCount = config.getCommitCount();
		
		int maxCacheSize = config.getMaxCacheSize();
		long delayForMaxCache = config.getDelayForMaxCache();
		
		int idx = 0;
		List<CacheProxy<String, Trace>> caches = cacheManager.getDistributeCaches();
		
		int[] cacheIndex = config.getCacheIndex();
		
		List<CacheProxy<String, Trace>> selectCaches = new ArrayList<CacheProxy<String, Trace>>();
		
		//캐시설정정보보다 작으면 
		if(cacheIndex == null || cacheIndex.length == 0) {
			CacheProxy<String, Trace> c = caches.get(0);
			if(c == null)  throw new Exception(Util.join("NotFoundCacheException(index:",0,")"));
			selectCaches.add(c);	
		}else {			
			for(int i : cacheIndex) {
				CacheProxy<String, Trace> c = caches.get(i);
				if(c == null)  throw new Exception(Util.join("NotFoundCacheException(index:",0,")"));
				selectCaches.add(c);				
			}			
		}
		 
		
		for (CacheProxy<String, Trace> cache : selectCaches) {			
			Channel channel = new MOMChannel(
				name + (idx ++), module, qmgrName, hostName, port, 
				channelName, queueName, waitTime, userId, password, ccsid,characterSet, 
				bindMode, autoCommit, commitCount, maxCommitWait, delayForNoMessage, delayOnException, maxCacheSize, delayForMaxCache, cache );
			if(tpm != null) channel.setThroughputMonitor(tpm);
			channel.setParser(parser);
			channel.setHealthCheck(healthCheck);
			channel.setChannelExceptionHandler(channelExceptionHandler);
			channels.add(channel); 	
			channel.start();
			
			logger.info(Util.join("channel(", channel.getName(), ") started"));
		}


	}




	/**
	 * 
	 */
	public synchronized void stopChannels() { 
		try {
			state = STATE_CHANNEL_STOPPING;
			if(channels != null && channels.size() > 0) {
				logger.info("channels stopping");
				for(Channel channel : channels) {
					channel.stop();
					logger.info(Util.join("channel(", channel.getName(), ") stop"));
				}
				channels.clear();
				if(restChannel != null) restChannel = null; 
				logger.info("channels stopped");
			}
			channelsStarted = false;
			state = STATE_CHANNEL_STOPPED;
		}catch(Exception e) {
			state = STATE_CHANNEL_ERROR_ON_STOP;
			logger.error("", e);
		}finally {
			
		}
	}
 
	Thread healthCheckThread;
	boolean isShutdown = true;
	long stopDelay = 1000;
	long healthCheckDelay = 5*1000;
	
	class ChannelHealthCheckTask implements Runnable{
		
		@Override
		public void run() {
			
			isShutdown = false;
			logger.info("start health check task ");	
			while(healthCheckThread == Thread.currentThread() && !isShutdown) {
				for(Channel channel : channels) {
					//if(channel != null && !channel.isShutdown() && channel.isHealthCheck()) {
					if(channel != null && channel.isHealthCheck()) {
						if(!channel.ping()) {
							logger.info(Util.join("channel[", channel.getName() , "] health check result: false, so try to restart channel" ) );
							try {
								channel.stop();
								Thread.sleep(stopDelay);
								channel.start();
							} catch (Exception e) { 
							}
						}else {
							
						}
					} 
				}
				
				try {
					Thread.sleep(healthCheckDelay);
				} catch (InterruptedException e) { 
				}
				
			}
			logger.info("stop health check task");	
			isShutdown = true;
		}
		
	}
	
	/**
	 * @deprecated 20200508 This funciton was moved to the DatabasePolicyHandler's function.  
	 */
	public void startChannelHealthCheck() {
		if(healthCheckThread != null) stopChannelHealthCheck();
		healthCheckThread = new Thread(new ChannelHealthCheckTask()); 
		healthCheckThread.start(); 
	}

	/**
	 * @deprecated 20200508 This funciton was moved to the DatabasePolicyHandler's function.  
	 */
	public void stopChannelHealthCheck() {
		isShutdown = true;
		if(healthCheckThread != null) {
			try {
				healthCheckThread.join();
			} catch (InterruptedException e) {
				logger.error("",e);
			}
		}
	}

	public boolean getChannelsStarted() {
		return channelsStarted;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public int getState() {
		return state;
	}

}
