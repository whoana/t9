/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.manager;
 

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.zaxxer.hikari.HikariDataSource;

import rose.mary.trace.T9;
import rose.mary.trace.apps.TraceServer;
import rose.mary.trace.system.SystemLogger;

/**
 * <pre>
 * rose.mary.trace.manager
 * ServerManager.java
 * </pre>
 * @author whoana
 * @date Aug 26, 2019
 */
public class ServerManager {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	TraceServer server;
	
	@Autowired
	@Qualifier("datasource01")
	DataSource datasource01;

	
	public void startServer() throws Exception{
		server.start();
	}
	
	public void stopServer() throws Exception{
		server.stop();
	}
	
	public void startLoader() throws Exception{
		server.startLoader();
	}
	
	public void stopLoader() throws Exception{
		server.stopLoader();
	}
	
	private void stopDatasource() {
		if(datasource01 != null) {
			HikariDataSource hds = (HikariDataSource)datasource01;
			hds.close();
		}
	}
	public void shutdownServer() throws Exception{
		server.stop();
		stopDatasource();
		T9.exitApplication();		  
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void startChannel() throws Exception {
		server.startChannel();
	}
	
	public void stopChannel() throws Exception {
		server.stopChannel();
	}
	
	public boolean getChannelsStarted() {
		return server.getChannelsStarted();
	}
	
	public void startBoter() throws Exception{
		server.startBoter();
	}
	
	public void stopBoter() throws Exception{
		server.stopBoter();
	}
	
	public void startBotLoader() throws Exception{
		server.startBotLoader();

	}
	
	public void stopBotLoader() throws Exception{
		server.stopBotLoader();
	}
	
	public void startFinisher() throws Exception{
		server.startFinisher();

	}
	
	public void stopFinisher() throws Exception{
		server.stopFinisher();
	}
	
	public void startTraceErrorHandler() throws Exception{
		server.startTraceErrorHandler();

	}
	
	public void stopTraceErrorHandler() throws Exception{
		server.stopTraceErrorHandler();
	}
 
	public void startTester() throws Exception {
		server.startGenerateMsgTester();
	}
	
	public void stopTester() {
		server.stopGenerateMsgTester();
	}
	
	
	public int checkServerState() {
		return server.getState();
	}

	private StringBuffer warnning = null;
	
	 
	public void addWarnning(String msg) {
		if(warnning == null) {
			warnning = new StringBuffer();
		}
		warnning.append(SystemLogger.astar).append(" " + msg).append(System.lineSeparator());
	}
	
	public StringBuffer getWarnning() {
		return warnning;
	}
	
}
