/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import rose.mary.trace.apps.manager.BotErrorHandlerManager;
import rose.mary.trace.apps.manager.BotLoaderManager;
import rose.mary.trace.apps.manager.BoterManager;
import rose.mary.trace.apps.manager.ChannelManager;
import rose.mary.trace.apps.manager.ConfigurationManager;
import rose.mary.trace.apps.manager.DatabasePolicyHandlerManager;
import rose.mary.trace.apps.manager.FinisherManager;
import rose.mary.trace.apps.manager.LoaderManager;
import rose.mary.trace.apps.manager.MonitorManager;
import rose.mary.trace.apps.manager.SystemErrorTestManager;
import rose.mary.trace.apps.manager.TesterManager;
import rose.mary.trace.apps.manager.TraceErrorHandlerManager;
import rose.mary.trace.apps.manager.UnmatchHandlerManager;
import shaded.org.apache.http.cookie.CookieAttributeHandler;
 

/**
 * <pre>
 * The TraceServer controls apps. 
 * </pre>
 * @author whoana
 * @since Aug 23, 2019
 */ 
public class TraceServer {

	Logger logger = LoggerFactory.getLogger(getClass());
	 
	public final static int STATE_INIT     = 0;
	public final static int STATE_STOP     = 1;
	public final static int STATE_START    = 2;
	public final static int STATE_SHUTDOWN = 3;
	
	private int state = STATE_INIT;
	
	private String name;	
	 
	@Autowired
	private ChannelManager channelManager;
	
	@Autowired 
	private LoaderManager loaderManager;
	
	@Autowired 
	private BoterManager boterManager;
	
	@Autowired 
	private BotLoaderManager botLoaderManager;	

	@Autowired 
	private FinisherManager finisherManager;
	
	@Autowired
	private TraceErrorHandlerManager traceErrorHandlerManager;
	
	@Autowired
	private BotErrorHandlerManager botErrorHandlerManager;
	
	@Autowired 
	private MonitorManager monitorManager;
	
	@Autowired 
	private UnmatchHandlerManager unmatchHandlerManager;
	
	@Autowired 
	private DatabasePolicyHandlerManager databasePolicyHandlerManager;
	
	@Autowired
	private TesterManager testerManager;
	
	@Autowired
	private ConfigurationManager configurationManager;
	
	@Autowired
	private SystemErrorTestManager systemErrorTestManager;
	
	/**
	 * 
	 * @param name
	 */
	public TraceServer(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void ready() throws Exception{
		loaderManager.ready();
		boterManager.ready();
		botLoaderManager.ready();
		finisherManager.ready();
		botErrorHandlerManager.ready();
		traceErrorHandlerManager.ready();
		unmatchHandlerManager.ready();
		databasePolicyHandlerManager.ready();
		state = STATE_INIT;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception{
		startBotLoader();
		startBoter();
		startLoader();
		startChannel();
		startFinisher();
		if(startTraceErrorHandler) startTraceErrorHandler();
		if(startBotErrorHandler) startBotErrorHandler();
		startUnmatchHandler();
		//startDatabasePolicyHandler();
		
		if(configurationManager.getConfig().getSystemErrorTestManagerConfig() != null && configurationManager.getConfig().getSystemErrorTestManagerConfig().isStartOnLoad()) {
			systemErrorTestManager.start();
		}
		
		state = STATE_START;
	}
	
	boolean startBotErrorHandler = true;
	boolean startTraceErrorHandler = true;
	
	/**
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception{
		stopChannel();
		stopLoader();
		stopBoter();
		stopBotLoader();
		stopFinisher();
		if(startTraceErrorHandler) stopTraceErrorHandler();
		if(startBotErrorHandler) stopBotErrorHandler();
		stopUnmatchHandler();		
	
		//stopDatabasePolicyHandler();
		
		if(configurationManager.getConfig().getSystemErrorTestManagerConfig().isStartOnLoad()) {
			systemErrorTestManager.stop();
		}
		
		state = STATE_STOP;
	}
	
	private void startBotErrorHandler() throws Exception {
		botErrorHandlerManager.start();
	}
	
	private void stopBotErrorHandler() {
		botErrorHandlerManager.stop();
	}

	public void startDatabasePolicyHandler() throws Exception {
		databasePolicyHandlerManager.start();
	}

	public void stopDatabasePolicyHandler() {
		databasePolicyHandlerManager.stop();		
	}

	/**
	 * 
	 * @throws Exception
	 */
	private void startUnmatchHandler() throws Exception {
		unmatchHandlerManager.start();
	}

	/**
	 * 
	 */
	private void stopUnmatchHandler() {
		unmatchHandlerManager.stop();
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void startTraceErrorHandler() throws Exception {
		traceErrorHandlerManager.start();
	}
	
	/**
	 * 
	 */
	public void stopTraceErrorHandler() {
		traceErrorHandlerManager.stop();		
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void startFinisher() throws Exception {
		finisherManager.start();
	}
	
	/**
	 * 
	 */
	public void stopFinisher() {
		finisherManager.stop();		
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public void startChannel() throws Exception {
		if(ChannelManager.STATE_CHANNEL_STARTING == channelManager.getState() || ChannelManager.STATE_CHANNEL_STARTING == channelManager.getState()) {
			throw new Exception("ChannelManagerStateException(Now Channel manager is stopping or starting channels.)");
		}
		channelManager.startChannels();
		//channelManager.startChannelHealthCheck();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void stopChannel() throws Exception {
		//channelManager.stopChannelHealthCheck();
		if(ChannelManager.STATE_CHANNEL_STARTING == channelManager.getState() || ChannelManager.STATE_CHANNEL_STARTING == channelManager.getState()) {
			throw new Exception("ChannelManagerStateException(Now Channel manager is stopping or starting channels.)");
		}
		channelManager.stopChannels();	
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void startLoader() throws Exception{
		loaderManager.start();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void stopLoader() throws Exception{
		loaderManager.stop();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void startBoter() throws Exception{
		boterManager.start();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void stopBoter() throws Exception{
		boterManager.stop();
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void startBotLoader() throws Exception{
		botLoaderManager.start();
	}
	
	
	/**
	 * 
	 * @throws Exception
	 */
	public void stopBotLoader() throws Exception{
		botLoaderManager.stop();
	}
	
	public void startMonitor() throws Exception{
		monitorManager.startMonitors();
	}
	
	public void stopMonitor() throws Exception{
		monitorManager.stopMonitors();
	}
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	public boolean getChannelsStarted() {
		return channelManager.getChannelsStarted();
	}

	public void startGenerateMsgTester() throws Exception {
		testerManager.start();
		
	}

	public void stopGenerateMsgTester() {
		testerManager.stop();
	}
	
	
	
	
}
