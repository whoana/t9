/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.exception;

/**
 * <pre>
 * rose.mary.trace.exception
 * ZeroLengthMessageException.java
 * </pre>
 * @author whoana
 * @date June 15, 2022
 */
public class ZeroLengthMessageException extends Exception {
	
	public ZeroLengthMessageException() {
		super();
	}
	
	public ZeroLengthMessageException(String msg) {
		super(msg);
	}

}