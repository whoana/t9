/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.data.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * BOT(Bills of Trace)
 * 특정 구간의 어플리케이션 Trace 정보를 종합적으로 담고있는 요약 데이터  
 * <pre>
 * rose.mary.trace.data.common
 * BOT.java
 * </pre>
 * @author whoana
 * @date Aug 1, 2019
 */
public class Bot implements Serializable {
 
	private static final long serialVersionUID = 1L;
	 
	 
	State state;
	
	String regDate;
	
	String modDate;
	
	int retry = 0;	
	
	String retryErrorMsg;
	
	long createDate;
	
	InterfaceInfo interfaceInfo;

	List<String> traceIds = new ArrayList<String>();
	 

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return the traceIds
	 */
	public List<String> getTraceIds() {
		return traceIds;
	}

	/**
	 * @param traceIds the traceIds to set
	 */
	public void setTraceIds(List<String> traceIds) {
		this.traceIds = traceIds;
	}

	 
	/**
	 * @return the interfaceInfo
	 */
	public InterfaceInfo getInterfaceInfo() {
		return interfaceInfo;
	}

	/**
	 * @param interfaceInfo the interfaceInfo to set
	 */
	public void setInterfaceInfo(InterfaceInfo interfaceInfo) {
		this.interfaceInfo = interfaceInfo;
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
 
	public long elapsed() {
		return System.currentTimeMillis() - createDate;
	}
 
	boolean finishStore = false;
	/**
	 * @param b
	 */
	public void setFinishStore(boolean finishStore) {
		this.finishStore = finishStore;
	}

	public boolean getFinishStore() {
		return finishStore;
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

	public String getModDate() {
		return modDate;
	}

	public void setModDate(String modDate) {
		this.modDate = modDate;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	

  
	
}
