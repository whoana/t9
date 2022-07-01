/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.helper.module.mte;


/**
 * <pre>
 * rose.mary.trace.helper.module
 * TraceMsgHandler.java
 * </pre>
 * @author whoana
 * @since Jul 24, 2019
 */
public interface MsgHandler {
	
	public static final String MODULE_MQ 	= "w";
	
	public static final String MODULE_ILINK = "i";
	
	public static final int Q_QPEN_OPT_GET = 10;
	
	public static final int Q_OPEN_OPT_PUT = 20;

	public void open(String queueName, int openOpt)throws Exception;
	
	public void put(MTEHeader header, String msg) throws Exception;
	
	public Object get(int waitTime) throws Exception;
	
	public void startTransaction() throws Exception;
	
	public void commit() throws Exception;
	
	public void rollback() throws Exception;
	
	public void close() ;

	/**
	 * @return
	 */
	public boolean ping();
	
	

}
