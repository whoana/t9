/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.manager.config;

/**
 * <pre>
 * rose.mary.trace.apps.manager.config
 * HandlerManagerConfig.java
 * </pre>
 * @author whoana
 * @date Oct 11, 2019
 */
public class TraceErrorHandlerManagerConfig {
	
	String name;
	
	int delayForNoMessage = 1000;
	
	int exceptionDelay = 1000;

	int maxRetry = 2;
	
	/**
	 * @return the delayForNoMessage
	 */
	public int getDelayForNoMessage() {
		return delayForNoMessage;
	}

	/**
	 * @param delayForNoMessage the delayForNoMessage to set
	 */
	public void setDelayForNoMessage(int delayForNoMessage) {
		this.delayForNoMessage = delayForNoMessage;
	}

	/**
	 * @return the exceptionDelay
	 */
	public int getExceptionDelay() {
		return exceptionDelay;
	}

	/**
	 * @param exceptionDelay the exceptionDelay to set
	 */
	public void setExceptionDelay(int exceptionDelay) {
		this.exceptionDelay = exceptionDelay;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public int getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(int maxRetry) {
		this.maxRetry = maxRetry;
	}
	
	
	
	
}
