/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.exception;

/**
 * <pre>
 * rose.mary.trace.database
 * LoaderExceptionHandler.java
 * </pre>
 * @author whoana
 * @date Aug 26, 2019
 */
public interface ExceptionHandler {
	public void handle(String msg, Exception e); 
}
