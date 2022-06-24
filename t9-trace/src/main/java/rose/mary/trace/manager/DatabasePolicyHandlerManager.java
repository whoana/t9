package rose.mary.trace.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.config.DatabasePolicyConfig;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.database.service.SystemService;
import rose.mary.trace.handler.DatabasePolicyHandler;

public class DatabasePolicyHandlerManager {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	DatabasePolicyConfig config = null;
	  
	MessageSource messageResource;
	
	SystemService systemService;
	
	ServerManager serverManager;
	
	ChannelManager channelManager;

	DatabasePolicyHandler databaseHealthCheckHandler;
	CacheManager cacheManager;
	public DatabasePolicyHandlerManager(
		DatabasePolicyConfig config,
		SystemService systemService,
		ServerManager serverManager,
		ChannelManager channelManager,
		MessageSource messageResource,
		CacheManager cacheManager
		
	) {
		this.config = config; 
		this.systemService = systemService;
		this.messageResource = messageResource;
		this.serverManager = serverManager;
		this.channelManager = channelManager;
		this.cacheManager = cacheManager;
		
	}

	public void ready() throws Exception{ 
	}

	

	public void start() throws Exception {
		try {
			stop();
			CacheProxy<String, Trace> errorCache1 = cacheManager.getErrorCache01();
			CacheProxy<String, State> errorCache2 = cacheManager.getErrorCache02();
			databaseHealthCheckHandler = new DatabasePolicyHandler(messageResource, systemService, serverManager, channelManager, config, errorCache1, errorCache2);
			databaseHealthCheckHandler.start();
		}catch (Exception e) {
			stop();
			throw e;
		}
	}
	
	/**
	 * 
	 */
	public void stop() {
		if(databaseHealthCheckHandler != null)  {
			databaseHealthCheckHandler.stop();
			databaseHealthCheckHandler = null;
		}
	}
}
