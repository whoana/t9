/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.data.channel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * rose.mary.trace.data.channel
 * ChannelInfo.java
 * </pre>
 * @author whoana
 * @date Aug 7, 2019
 */
public class ChannelOperation implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final int OP_ADD 		= 0;
	public static final int OP_DELETE 	= 1;
	public static final int OP_START 	= 2;	
	public static final int OP_SUSPEND 	= 3;
	public static final int OP_RESUME 	= 4;
	public static final int OP_STOP  	= 5;
	
	List<ChannelConfig> channelConfigs = new ArrayList<ChannelConfig>(); 
	
	int operation = OP_START;
	
	int threadCount = 1;
 
	/**
	 * @return the operation
	 */
	public int getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(int operation) {
		this.operation = operation;
	}

	/**
	 * @return the threadCount
	 */
	public int getThreadCount() {
		return threadCount;
	}

	/**
	 * @param threadCount the threadCount to set
	 */
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	/**
	 * @return the channelConfigs
	 */
	public List<ChannelConfig> getChannelConfigs() {
		return channelConfigs;
	}

	/**
	 * @param channelConfigs the channelConfigs to set
	 */
	public void setChannelConfigs(List<ChannelConfig> channelConfigs) {
		this.channelConfigs = channelConfigs;
	}
	
	
	
}
