/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.exception;

/**
 * <pre>
 * rose.mary.trace.exception
 * DatabaseException.java
 * </pre>
 * @author whoana
 * @date Sep 3, 2019
 */
public class DatabaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public DatabaseException() {
		super();
	}
	
	public DatabaseException(String msg) {
		super(msg);
	}
	
	public DatabaseException(Exception e) {
		super(e);
	}
	
	public DatabaseException(String msg, Exception e) {
		super(msg, e);
	}
	
}
