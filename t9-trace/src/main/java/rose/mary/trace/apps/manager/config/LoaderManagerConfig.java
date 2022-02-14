/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.manager.config;

import rose.mary.trace.apps.manager.LoaderManager;

/**
 * <pre>
 * rose.mary.trace.manager
 * TraceLoaderManagerConfig.java
 * </pre>
 * @author whoana
 * @date Aug 28, 2019
 */
public class LoaderManagerConfig {
	
	int threadCount = 1;
	int commitCount = 1000;
	int delayForNoMessage = 100;
	boolean loadError = true;
	boolean loadContents = false;
	boolean stopAsap = false;
	String name = LoaderManager.class.getName();

	
	
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
	 * @return
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
	 * @return the loadError
	 */
	public boolean isLoadError() {
		return loadError;
	}

	/**
	 * @param loadError the loadError to set
	 */
	public void setLoadError(boolean loadError) {
		this.loadError = loadError;
	}

	/**
	 * @return the loadContents
	 */
	public boolean isLoadContents() {
		return loadContents;
	}

	/**
	 * @param loadContents the loadContents to set
	 */
	public void setLoadContents(boolean loadContents) {
		this.loadContents = loadContents;
	}

	public boolean isStopAsap() {
		return stopAsap;
	}

	public void setStopAsap(boolean stopAsap) {
		this.stopAsap = stopAsap;
	}
	
	
	
}
