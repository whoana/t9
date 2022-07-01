/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.data.common;

import java.io.Serializable;
 


/**
 * <pre>
 * 	Status
 * 	FinishCache
 * </pre>
 * @author whoana
 * @since 20200402
 */
public class State implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static String SUCCESS = "00";
	public final static String ING 	   = "01";
	public final static String FAIL    = "99";
	
	public static final String MATCH_YES = "0";
	public static final String MATCH_NO  = "1";
	
	
	long createDate;
	String botId			= "";
	String integrationId 	= "";
	String trackingDate  	= "";
	String orgHostId     	= "";
	String status 			= ING;
	String match            = MATCH_NO;
	int    recordCount   	= 0;
	int    dataAmount    	= 0;
	String compress      	= "0";
	int	   cost          	= 0;
	int    todoNodeCount 	= 1;
	int    finishNodeCount 	= 0;
	int    errorNodeCount  	= 0;
	String errorCode		= "";
	String errorMessage 	= "";
	
	boolean finish = false;
	boolean finishSender = false;
	boolean skip = false;
	Object context;
	
	String regDate = "";
	String modDate = "";
	
	int retry = 0;	
	String retryErrorMsg;
	
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	public String getBotId() {
		return botId;
	}
	public void setBotId(String botId) {
		this.botId = botId;
	}
	public String getIntegrationId() {
		return integrationId;
	}
	public void setIntegrationId(String integrationId) {
		this.integrationId = integrationId;
	}
	public String getTrackingDate() {
		return trackingDate;
	}
	public void setTrackingDate(String trackingDate) {
		this.trackingDate = trackingDate;
	}
	public String getOrgHostId() {
		return orgHostId;
	}
	public void setOrgHostId(String orgHostId) {
		this.orgHostId = orgHostId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public int getDataAmount() {
		return dataAmount;
	}
	public void setDataAmount(int dataAmount) {
		this.dataAmount = dataAmount;
	}
	public String getCompress() {
		return compress;
	}
	public void setCompress(String compress) {
		this.compress = compress;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getTodoNodeCount() {
		return todoNodeCount;
	}
	public void setTodoNodeCount(int todoNodeCount) {
		this.todoNodeCount = todoNodeCount;
	}
	public int getFinishNodeCount() {
		return finishNodeCount;
	}
	public void setFinishNodeCount(int finishNodeCount) {
		this.finishNodeCount = finishNodeCount;
	}
	public int getErrorNodeCount() {
		return errorNodeCount;
	}
	public void setErrorNodeCount(int errorNodeCount) {
		this.errorNodeCount = errorNodeCount;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public boolean isFinish() {
		return finish;
	}
	public void setFinish(boolean finish) {
		this.finish = finish;
	}
	 
	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}
	
	public boolean isFinishSender() {
		return finishSender;
	}
	
	public void setFinishSender(boolean finishSender) {
		this.finishSender = finishSender;
	}
	public Object getContext() {
		return context;
	}
	public void setContext(Object context) {
		this.context = context;
	}
	
	public void setSkip(boolean skip) {
		this.skip = skip;
	}
	
	public boolean skip() {
		return skip;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getModDate() {
		return modDate;
	}
	public void setModDate(String modDate) {
		this.modDate = modDate;
	}
	public boolean isSkip() {
		return skip;
	}
	public int getRetry() {
		return retry;
	}
	public void setRetry(int retry) {
		this.retry = retry;
	}
	public String getRetryErrorMsg() {
		return retryErrorMsg;
	}
	public void setRetryErrorMsg(String retryErrorMsg) {
		this.retryErrorMsg = retryErrorMsg;
	}
	 
	
	
}
