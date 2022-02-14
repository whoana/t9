/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.manager.config;
 

/**
 * <pre>
 * rose.mary.trace.apps.manager.config
 * BotLoaderConfig.java
 * </pre>
 * @author whoana
 * @date Sep 24, 2019
 */
public class BotLoaderManagerConfig {
	
	int threadCount = 1;
	int commitCount = 1000;
	int delayForNoMessage = 100;	
	String name = BotLoaderManagerConfig.class.getName();
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
	 * @return the commitCount
	 */
	public int getCommitCount() {
		return commitCount;
	}
	/**
	 * @param commitCount the commitCount to set
	 */
	public void setCommitCount(int commitCount) {
		this.commitCount = commitCount;
	}
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

	
}
