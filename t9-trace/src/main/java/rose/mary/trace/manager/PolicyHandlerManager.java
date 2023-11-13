package rose.mary.trace.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.config.PolicyConfig;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.exception.SystemError;
import rose.mary.trace.database.service.SystemService;
import rose.mary.trace.handler.PolicyHandler;

public class PolicyHandlerManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	PolicyConfig config = null;

	MessageSource messageResource;

	SystemService systemService;

	ServerManager serverManager;

	ChannelManager channelManager;

	PolicyHandler policyHandler;

	CacheManager cacheManager;

	public PolicyHandlerManager(
			PolicyConfig config,
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

	public void ready() throws Exception {
	}

	public void start() throws Exception {
		try {
			stop();
			CacheProxy<String, Trace> errorCache1 = cacheManager.getErrorCache01();
			CacheProxy<String, State> errorCache2 = cacheManager.getErrorCache02();
			CacheProxy<String, SystemError> systemErrorCache = cacheManager.getSystemErrorCache();
			policyHandler = new PolicyHandler(messageResource, systemService, serverManager,
					channelManager, config, errorCache1, errorCache2, systemErrorCache);
			policyHandler.start();
		} catch (Exception e) {
			stop();
			throw e;
		}
	}

	/**
	 * 
	 */
	public void stop() {
		if (policyHandler != null) {
			policyHandler.stop();
			policyHandler = null;
		}
	}
}
