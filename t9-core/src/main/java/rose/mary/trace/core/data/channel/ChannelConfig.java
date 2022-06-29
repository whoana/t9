/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.data.channel;

import java.io.Serializable;

/**
 * <pre>
 * rose.mary.trace.data.channel
 * ChannelConfig.java
 * </pre>
 * @author whoana
 * @date Aug 9, 2019
 */
public class ChannelConfig implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	int threadCount = 1;
	int type;
	String hostName;
	String qmgrName;
	int port;
	String userId;
	String password;
	String channelName;
	String queueName;
	int waitTime;
	String module;
	int ccsid;
	int characterSet;
	boolean autoCommit = true;
	boolean bindMode = false;
	long maxCommitWait = 1000;
	long delayForNoMessage = 10;
	int commitCount = 100;
	
	int maxCacheSize = 100; 
	long delayForMaxCache = 1000;
	
	int [] cacheIndex = {0};
	boolean healthCheck = false;
	boolean disable = false;
	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}
	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	/**
	 * @return the qmgrName
	 */
	public String getQmgrName() {
		return qmgrName;
	}
	/**
	 * @param qmgrName the qmgrName to set
	 */
	public void setQmgrName(String qmgrName) {
		this.qmgrName = qmgrName;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the channelName
	 */
	public String getChannelName() {
		return channelName;
	}
	/**
	 * @param channelName the channelName to set
	 */
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	/**
	 * @return the queueName
	 */
	public String getQueueName() {
		return queueName;
	}
	/**
	 * @param queueName the queueName to set
	 */
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}
	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}
	/**
	 * @return the ccsid
	 */
	public int getCcsid() {
		return ccsid;
	}
	/**
	 * @param ccsid the ccsid to set
	 */
	public void setCcsid(int ccsid) {
		this.ccsid = ccsid;
	}
	
	
	
	/**
	 * @return the characterSet
	 */
	public int getCharacterSet() {
		return characterSet;
	}
	/**
	 * @param characterSet the characterSet to set
	 */
	public void setCharacterSet(int characterSet) {
		this.characterSet = characterSet;
	}
	/**
	 * @return the autoCommit
	 */
	public boolean isAutoCommit() {
		return autoCommit;
	}
	/**
	 * @param autoCommit the autoCommit to set
	 */
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}
	/**
	 * @return the bindMode
	 */
	public boolean isBindMode() {
		return bindMode;
	}
	/**
	 * @param bindMode the bindMode to set
	 */
	public void setBindMode(boolean bindMode) {
		this.bindMode = bindMode;
	}
	/**
	 * @return the thread
	 */
	public int getThreadCount() {
		return threadCount;
	}
	/**
	 * @param thread the thread to set
	 */
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	/**
	 * @return
	 */
	public long getMaxCommitWait() {
		return maxCommitWait;
	}
	/**
	 * @return
	 */
	public long getDelayForNoMessage() {
		return delayForNoMessage;
	}
	/**
	 * @param maxCommitWait the maxCommitWait to set
	 */
	public void setMaxCommitWait(long maxCommitWait) {
		this.maxCommitWait = maxCommitWait;
	}
	/**
	 * @param delayForNoMessage the delayForNoMessage to set
	 */
	public void setDelayForNoMessage(long delayForNoMessage) {
		this.delayForNoMessage = delayForNoMessage;
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the cacheIndex
	 */
	public int[] getCacheIndex() {
		return cacheIndex;
	}
	/**
	 * @param cacheIndex the cacheIndex to set
	 */
	public void setCacheIndex(int[] cacheIndex) {
		this.cacheIndex = cacheIndex;
	}
	/**
	 * @return the healthCheck
	 */
	public boolean isHealthCheck() {
		return healthCheck;
	}
	/**
	 * @param healthCheck the healthCheck to set
	 */
	public void setHealthCheck(boolean healthCheck) {
		this.healthCheck = healthCheck;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public int getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}
	public int getMaxCacheSize() {
		return maxCacheSize;
	}
	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}
	public long getDelayForMaxCache() {
		return delayForMaxCache;
	}
	public void setDelayForMaxCache(long delayForMaxCache) {
		this.delayForMaxCache = delayForMaxCache;
	}
	 
	
	
	
}
