/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.finisher.plugin;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace;

/**
 * <pre>
 * NoWaitingCleaner
 * 완료된 건들만 삭제 처리한다.
 * </pre>
 * @author whoana
 * @since 2020.03.30
 */
public class NoWaitingCleaner implements Cleaner {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private CacheProxy<String, Trace> backupCache;
	
	private CacheProxy<String, State> finCache;
	
	private CacheProxy<String, Integer> routingCache;
	
	public NoWaitingCleaner(CacheProxy<String, Trace> backupCache, CacheProxy<String, State> finCache, CacheProxy<String, Integer> routingCache) {
		this.backupCache = backupCache;
		this.finCache = finCache;
		this.routingCache = routingCache;
	}
	
	@Override
	public void clean(long currentTime, State state) throws Exception {
		
		if(state.isFinish()) { //완료건만 삭제 
//			List<String> traceIds = bot.getTraceIds();
//			for(String id : traceIds) {
//				backupCache.remove(id);
//			}
			
			finCache.remove(state.getBotId());
			routingCache.remove(state.getBotId());
		}else {
			logger.debug("no delete bot:" + Util.toJSONString(state));
		}
	}

}
