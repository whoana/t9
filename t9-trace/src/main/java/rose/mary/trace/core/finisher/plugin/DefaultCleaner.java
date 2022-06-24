/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.finisher.plugin;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.Trace;
/**
 * <pre>
 * DefaultCleaner
 * 설정된 시간 보다 오래된 건들 중 완료된 건들만 삭제 처리한다.
 * </pre>
 * @author whoana
 * @since 2020.03.30
 */
public class DefaultCleaner implements Cleaner{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private CacheProxy<String, Trace> backupCache;
	
	private CacheProxy<String, State> finCache;
	
	private CacheProxy<String, Integer> routingCache;
	
	int waitForCleaning = 60 * 60 * 1000; //60분 
	 
	int  waitForFinishedCleaning = 60 * 10 * 1000;//10분
	
	public DefaultCleaner(
		int waitForCleaning,
		int waitForFinishedCleaning,
		CacheProxy<String, Trace> backupCache, 
		CacheProxy<String, State> finCache,
		CacheProxy<String, Integer> routingCache
	) {
		this.waitForCleaning = waitForCleaning;
		this.waitForFinishedCleaning = waitForFinishedCleaning;
		this.backupCache = backupCache;
		this.finCache = finCache;
		this.routingCache = routingCache;
	}
	
	@Override
	public void clean(long currentTime, State state) throws Exception{
		//----------------------------------------------------------						
		//완료된 건 삭제 
		//----------------------------------------------------------
		long elapsed = currentTime - state.getCreateDate();

		if(state.isFinish() && elapsed >= waitForFinishedCleaning) { // 완료건 중에 삭제시간이 도래된 것만 삭제 
			finCache.remove(state.getBotId());
			routingCache.remove(state.getBotId());
		}else {
			
			//logger.debug("notYetDeletedState:" + Util.toJSONString(state));
			
//			if(alreadyFinished(state.getBotId())) {
//				finCache.remove(state.getBotId());
//				routingCache.remove(state.getBotId());
//				return;
//			}
			
			//완료되지 않은 건들 중 청소시간을 넘긴 것들 삭제 처리
			if(elapsed >= waitForCleaning ) { 					
				finCache.remove(state.getBotId());
				routingCache.remove(state.getBotId());
			}
		}
		
		/*
		long elapsed = currentTime - state.getCreateDate();
		//----------------------------------------------------------						
		//청소시간을 넘긴 것들 중에 완료된 건에 대한 삭제처리 
		//----------------------------------------------------------
		if(elapsed >= waitForCleaning ) { 
			
			if(state.isFinish()) { //완료건만 삭제 
				finCache.remove(state.getBotId());
				routingCache.remove(state.getBotId());
			}
		}
		*/
	}

}
