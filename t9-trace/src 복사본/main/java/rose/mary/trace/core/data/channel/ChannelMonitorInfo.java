/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.data.channel;

import java.io.Serializable;

/**
 * <pre>
 * rose.mary.trace.data.channel
 * ChannelMonitorInfo.java
 * </pre>
 * @author whoana
 * @date Aug 7, 2019
 */
public class ChannelMonitorInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double totalTps;

	/**
	 * @return the totalTps
	 */
	public double getTotalTps() {
		return totalTps;
	}

	/**
	 * @param totalTps the totalTps to set
	 */
	public void setTotalTps(double totalTps) {
		this.totalTps = totalTps;
	}
	
	
	
}
