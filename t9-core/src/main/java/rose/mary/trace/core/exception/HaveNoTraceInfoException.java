/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.exception;

/**
 * <pre>
 * rose.mary.trace.exception
 * HaveNoTraceInfoException.java
 * </pre>
 * @author whoana
 * @date Aug 5, 2019
 */
public class HaveNoTraceInfoException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	byte[] data;
	
	public HaveNoTraceInfoException() {
		super();
	}
	
	public HaveNoTraceInfoException(String msg) {
		super(msg);
	}

	/**
	 * @param data
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public byte[] getData() {
		return data;
	}


}
