/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.restore;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.data.common.Trace;

/**
 * <pre>
 * Input으로 최초 들어온 메시지를 백업하고 비정상 종료 후 재시작시 복구 프로세스를 처리한다.
 * 옵션 처리: 
 * 	백업기간 : 시간단위 
 * 	백업여부 
 * 
 * rose.mary.trace.apps.restore
 * Resore.java
 * </pre>
 * @author whoana
 * @date Oct 21, 2019
 */
public class Restore {
	
	boolean restore = false;
	
	/**
	 * 백업 시간 
	 * default 1시간
	 * 1시간 백업 , 1시간이 지난 건들은 삭제.   
	 */
	int restoreTime = 1;
	
	CacheProxy<String, Trace> restoreCache;
	
	public void restore() {
		
	}
	
	public void reset() {
		
	}
	
	public void clear() {
		
	}
	
	public void backup(Trace trace) throws Exception {
		restoreCache.put(trace.getId(), trace);
	}

	public void start() {
		
	}
	
	public void stop() {
		
	}
	
}
