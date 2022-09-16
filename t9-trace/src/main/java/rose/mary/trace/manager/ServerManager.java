/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.manager;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.zaxxer.hikari.HikariDataSource;

import rose.mary.trace.server.TraceServer;
import rose.mary.trace.system.SystemLogger;

/**
 * <pre>
 * rose.mary.trace.manager
 * ServerManager.java
 * </pre>
 * 
 * @author whoana
 * @date Aug 26, 2019
 */
public class ServerManager {

	Logger logger = LoggerFactory.getLogger(getClass());

	TraceServer server;

	DataSource datasource01;

	CacheManager cacheManager;

	ApplicationContext applicationContext;

	public ServerManager(TraceServer server, DataSource datasource01, ApplicationContext applicationContext,
			CacheManager cacheManager) {
		this.server = server;
		this.datasource01 = datasource01;
		this.applicationContext = applicationContext;
		this.cacheManager = cacheManager;
	}

	public void exitApplication() {

		int exitCode = 0;
		try {
			exitCode = SpringApplication.exit(applicationContext, new ExitCodeGenerator() {
				@Override
				public int getExitCode() {
					// no errors
					return 0;
				}
			});
		} catch (Throwable t) {
			SystemLogger.error("boot shutdown error", t);
		} finally {
			SystemLogger.info("exitApplication");
			System.exit(exitCode);
		}

	}

	public void startServer() throws Exception {
		server.start();
	}

	public void stopServer() throws Exception {
		server.stop();
	}

	public void startLoader() throws Exception {
		server.startLoader();
	}

	public void stopLoader() throws Exception {
		server.stopLoader();
	}

	private void stopDatasource() {
		if (datasource01 != null) {
			HikariDataSource hds = (HikariDataSource) datasource01;
			if (hds != null)
				hds.close();
		}
	}

	public void shutdownServer() throws Exception {
		Thread exitThread = new Thread(new Runnable() {
			public void run() {
				SystemLogger.info("Received command for shutting down server!");
				exitApplication();
			}
		});
		exitThread.start();
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

	public void startBoter() throws Exception {
		server.startBoter();
	}

	public void stopBoter() throws Exception {
		server.stopBoter();
	}

	public void startBotLoader() throws Exception {
		server.startBotLoader();

	}

	public void stopBotLoader() throws Exception {
		server.stopBotLoader();
	}

	public void startFinisher() throws Exception {
		server.startFinisher();

	}

	public void stopFinisher() throws Exception {
		server.stopFinisher();
	}

	public void startTraceErrorHandler() throws Exception {
		server.startTraceErrorHandler();

	}

	public void stopTraceErrorHandler() throws Exception {
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
		if (warnning == null) {
			warnning = new StringBuffer();
		}
		warnning.append(SystemLogger.astar).append(" " + msg).append(System.lineSeparator());
	}

	public StringBuffer getWarnning() {
		return warnning;
	}

	// public void startDatabasePolicyHandler() throws Exception{
	// server.startDatabasePolicyHandler();
	// }
}
