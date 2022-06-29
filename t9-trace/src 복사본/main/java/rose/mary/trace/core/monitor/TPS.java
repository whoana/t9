/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.monitor;

import java.io.Serializable;

import pep.per.mint.common.util.Util;

/**
 * <pre>
 * rose.mary.trace.monitor
 * TPS.java
 * </pre>
 * @author whoana
 * @date Aug 23, 2019
 */
public class TPS implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int value;
	
	long startTime;
	
	long endTime;

	long sampleTime;
	
	String startTimeString;
	
	String endTimeString;
	
	int currentThroughputCount = 0;
	
	/**
	 * @param value
	 */
	public TPS(int value) {
		this.value = value;
	}
	
	public TPS(int value,  long startTime, long endTime, int currentThroughputCount) {
		this(value);
		this.startTime = startTime;
		this.endTime = endTime;
		this.sampleTime = endTime - startTime;
		this.startTimeString = Util.getFormatedDate(startTime, Util.DEFAULT_DATE_FORMAT_MI);
		this.endTimeString = Util.getFormatedDate(endTime, Util.DEFAULT_DATE_FORMAT_MI);
		this.currentThroughputCount = currentThroughputCount;
	}
	
	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public int getValue() {
		return value;
	}

	
	
	/**
	 * @return the sampleTime
	 */
	public long getSampleTime() {
		return sampleTime;
	}

	/**
	 * @param sampleTime the sampleTime to set
	 */
	public void setSampleTime(long sampleTime) {
		this.sampleTime = sampleTime;
	}

	/**
	 * @return the startTimeString
	 */
	public String getStartTimeString() {
		return startTimeString;
	}

	/**
	 * @param startTimeString the startTimeString to set
	 */
	public void setStartTimeString(String startTimeString) {
		this.startTimeString = startTimeString;
	}

	/**
	 * @return the endTimeString
	 */
	public String getEndTimeString() {
		return endTimeString;
	}

	/**
	 * @param endTimeString the endTimeString to set
	 */
	public void setEndTimeString(String endTimeString) {
		this.endTimeString = endTimeString;
	}

	
	
	/**
	 * @return the currentThroughputCount
	 */
	public int getCurrentThroughputCount() {
		return currentThroughputCount;
	}

	/**
	 * @param currentThroughputCount the currentThroughputCount to set
	 */
	public void setCurrentThroughputCount(int currentThroughputCount) {
		this.currentThroughputCount = currentThroughputCount;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

}
