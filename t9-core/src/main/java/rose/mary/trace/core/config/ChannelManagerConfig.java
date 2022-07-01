/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.config;

import java.util.ArrayList;
import java.util.List;

import rose.mary.trace.core.data.channel.ChannelConfig;

/**
 * <pre>
 * rose.mary.trace.config
 * ChannelConfig.java
 * </pre>
 * @author whoana
 * @date Aug 23, 2019
 */
public class ChannelManagerConfig {
	
	OldStateCheckHandlerConfig oldStateCheckHandlerConfig; 
	
	List<ChannelConfig> channelConfigs = new ArrayList<ChannelConfig>();
	
	boolean translateMsgOnException = false;
	int delayOnException = 5000;
	/**
	 * @return the channels
	 */
	public List<ChannelConfig> getChannelConfigs() {
		return channelConfigs;
	}

	/**
	 * @param channels the channels to set
	 */
	public void setChannelConfigs(List<ChannelConfig> channelConfigs) {
		this.channelConfigs = channelConfigs;
	}

	public boolean isTranslateMsgOnException() {
		return translateMsgOnException;
	}

	public void setTranslateMsgOnException(boolean translateMsgOnException) {
		this.translateMsgOnException = translateMsgOnException;
	} 
	
	public OldStateCheckHandlerConfig getOldStateCheckHandlerConfig() {
		return oldStateCheckHandlerConfig;
	}

	public void setOldStateCheckHandlerConfig(OldStateCheckHandlerConfig oldStateCheckHandlerConfig) {
		this.oldStateCheckHandlerConfig = oldStateCheckHandlerConfig;
	}

	public int getDelayOnException() {
		return delayOnException;
	}

	public void setDelayOnException(int delayOnException) {
		this.delayOnException = delayOnException;
	}
	
	
		
}
