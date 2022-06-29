/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.config;
 

/**
 * <pre>
 * rose.mary.trace.apps.manager.config
 * FinisherManagerConfig.java
 * </pre>
 * @author whoana
 * @date Oct 2, 2019
 */
public class FinisherManagerConfig {
	
	int threadCount = 1;
	int commitCount = 1000;
	int delayForNoMessage = 1000;
	int waitForCleaningSec = 3600;
	int waitForFinishedCleaningSec = 600;
	int delayForDoCleaning = 1000;
	boolean useWaitForCleaning = true;
	boolean resetWhenStart = true;
	String name = FinisherManagerConfig.class.getName();
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
	
	
	/**
	 * @param waitForCleaningSec the waitForCleaningSec to set
	 */
	public void setWaitForCleaningSec(int waitForCleaningSec) {
		this.waitForCleaningSec = waitForCleaningSec;
	}
	/**
	 * @return
	 */
	public int getWaitForCleaningSec() {
		// TODO Auto-generated method stub
		return waitForCleaningSec;
	}
	public int getDelayForDoCleaning() {
		return delayForDoCleaning;
	}
	public void setDelayForDoCleaning(int delayForDoCleaning) {
		this.delayForDoCleaning = delayForDoCleaning;
	}
	public boolean isUseWaitForCleaning() {
		return useWaitForCleaning;
	}
	public void setUseWaitForCleaning(boolean useWaitForCleaning) {
		this.useWaitForCleaning = useWaitForCleaning;
	}
	public boolean isResetWhenStart() {
		return resetWhenStart;
	}
	public void setResetWhenStart(boolean resetWhenStart) {
		this.resetWhenStart = resetWhenStart;
	}
	public int getWaitForFinishedCleaningSec() {
		return waitForFinishedCleaningSec;
	}
	public void setWaitForFinishedCleaningSec(int waitForFinishedCleaningSec) {
		this.waitForFinishedCleaningSec = waitForFinishedCleaningSec;
	}
	
	
	
}
