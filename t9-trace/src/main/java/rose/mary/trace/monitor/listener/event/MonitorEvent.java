/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.monitor.listener.event;

import rose.mary.trace.monitor.SystemResource;
import rose.mary.trace.monitor.TPS;

/**
 * <pre>
 * rose.mary.trace.listener.event
 * MonitorEvent.java
 * </pre>
 * @author whoana
 * @date Aug 16, 2019
 */
public class MonitorEvent<T> {
	
	public final static int TYPE_SYSTEM = 0; 
	public final static int TYPE_TPS = 1; 
	
	int type;
	
	T data;
	
	public MonitorEvent(T data) throws Exception {
		this.data = data;
		if(data instanceof SystemResource) { 
			type = TYPE_SYSTEM;
		}else if(data instanceof TPS) {
			type = TYPE_TPS;
		}else {
			throw new Exception("UnsupportedType");
		}
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	
	
}
