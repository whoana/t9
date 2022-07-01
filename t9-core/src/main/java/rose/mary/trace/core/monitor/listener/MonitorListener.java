/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.monitor.listener;

import rose.mary.trace.core.monitor.listener.event.MonitorEvent;

/**
 * <pre>
 * rose.mary.trace.listener
 * MonitorListener.java
 * </pre>
 * @author whoana
 * @date Aug 16, 2019
 */
public interface MonitorListener<T> {

	public void watch(MonitorEvent<T> event) throws Exception;
	
}
