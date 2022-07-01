/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.data.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.helper.checker.StateChecker; 

/**
 * <pre>
 * rose.mary.trace.msg
 * TraceMessage.java
 * </pre>
 * @author whoana
 * @date Jul 29, 2019
 */
public class Trace implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	StateChecker stateCheckHandler;
	
	/**
	 * <pre>
	 * 1) 트레이스 식별 ID
	 * 	적용 예)
	 * 	id = {그룹ID} + {인터페이스ID} + {일자} + {시간} +{호스트ID}
	 * </pre>
	 */
	String id;
	
	String integrationId;	
	
	String originHostId;
	
	String date;

	String processId;
	
	String processDate;
	
	String processEndDate;

	String timezone;
	
	String elapsedTime;
		
	String type;
	
	int seq;
	
	String previousHostId;
	
	String previousProcessId;
	
	String hostId;
	
	String os;
	
	String ip;
	
	String status;
	
	String errorCode;
	
	String errorMessage;
	
	int recordCount = 0;
	
	int dataSize = 0;
	
	String compress;
	
	String app;
	
	byte[] data;

	int todoNodeCount = 1;
	
	String regId;
	
	String regDate;
	
	int retry = 0;
	
	String retryErrorMsg;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the integrationId
	 */
	public String getIntegrationId() {
		return integrationId;
	}

	/**
	 * @param integrationId the interfaceId to set
	 */
	public void setIntegrationId(String integrationId) {
		this.integrationId = integrationId;
	}

	/**
	 * @return the hostId
	 */
	public String getHostId() {
		return hostId;
	}

	/**
	 * @param hostId the hostId to set
	 */
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the processId
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * @return the processDate
	 */
	public String getProcessDate() {
		return processDate;
	}

	/**
	 * @param processDate the processDate to set
	 */
	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}

	
	
	/**
	 * @return the processEndDate
	 */
	public String getProcessEndDate() {
		return processEndDate;
	}

	/**
	 * @param processEndDate the processEndDate to set
	 */
	public void setProcessEndDate(String processEndDate) {
		this.processEndDate = processEndDate;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the recordCount
	 */
	public int getRecordCount() {
		return recordCount;
	}

	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	/**
	 * @return the dataSize
	 */
	public int getDataSize() {
		return dataSize;
	}

	/**
	 * @param dataSize the dataSize to set
	 */
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	/**
	 * @return the compress
	 */
	public String getCompress() {
		return compress;
	}

	/**
	 * @param compress the compress to set
	 */
	public void setCompress(String compress) {
		this.compress = compress;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * @return the originHostId
	 */
	public String getOriginHostId() {
		return originHostId;
	}

	/**
	 * @param originHostId the originHostId to set
	 */
	public void setOriginHostId(String originHostId) {
		this.originHostId = originHostId;
	}

	/**
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * @param os the os to set
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * @return the todoNodeCount
	 */
	public int getTodoNodeCount() {
		return todoNodeCount;
	}

	/**
	 * @return the app
	 */
	public String getApp() {
		return app;
	}

	/**
	 * @param app the app to set
	 */
	public void setApp(String app) {
		this.app = app;
	}

	/**
	 * @param todoNodeCount the todoNodeCount to set
	 */
	public void setTodoNodeCount(int todoNodeCount) {
		this.todoNodeCount = todoNodeCount;
	}
	
	
	
	/**
	 * @return the regId
	 */
	public String getRegId() {
		return regId;
	}

	/**
	 * @param regId the regId to set
	 */
	public void setRegId(String regId) {
		this.regId = regId;
	}

	/**
	 * @return the regDate
	 */
	public String getRegDate() {
		return regDate;
	}

	/**
	 * @param regDate the regDate to set
	 */
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return Util.toJSONString(this);
	}

	
	/**
	 * @param finishChecker
	 */
	@JsonIgnore
	public void setStateCheckHandler(StateChecker stateCheckHandler) {
		this.stateCheckHandler = stateCheckHandler;
	}

	@JsonIgnore
	public StateChecker getStateCheckHandler() {
		return stateCheckHandler;
	}

	/**
	 * @return the retry
	 */
	public int getRetry() {
		return retry;
	}

	/**
	 * @param retry the retry to set
	 */
	public void setRetry(int retry) {
		this.retry = retry;
	}

	/**
	 * @return the retryErrorMsg
	 */
	public String getRetryErrorMsg() {
		return retryErrorMsg;
	}

	/**
	 * @param retryErrorMsg the retryErrorMsg to set
	 */
	public void setRetryErrorMsg(String retryErrorMsg) {
		this.retryErrorMsg = retryErrorMsg;
	}
	

	public void setPreviousHostId(String previousHostId) {
		this.previousHostId = previousHostId;
	}
	
	public void setPreviousProcessId(String previousProcessId) {
		this.previousProcessId = previousProcessId;
	}
	
	public void setTimezone(String timezone) {
		this.timezone = timezone;
		
	}
	
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;		
	}

	public String getTimezone() {
		return timezone;
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	public String getPreviousHostId() {
		return previousHostId;
	}

	public String getPreviousProcessId() {
		return previousProcessId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	
	
}
