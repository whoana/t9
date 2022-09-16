/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rose.mary.trace.manager.BotErrorHandlerManager;
import rose.mary.trace.manager.BotLoaderManager;
import rose.mary.trace.manager.BoterManager;
import rose.mary.trace.manager.ChannelManager;
import rose.mary.trace.manager.ConfigurationManager;
import rose.mary.trace.manager.FinisherManager;
import rose.mary.trace.manager.LoaderManager;
import rose.mary.trace.manager.MonitorManager;
import rose.mary.trace.manager.SystemErrorTestManager;
import rose.mary.trace.manager.TesterManager;
import rose.mary.trace.manager.TraceErrorHandlerManager;
import rose.mary.trace.manager.UnmatchHandlerManager;
import rose.mary.trace.manager.CacheManager;

/**
 * <pre>
 * The TraceServer controls apps.
 * </pre>
 * 
 * @author whoana
 * @since Aug 23, 2019
 */
public class TraceServer {

	Logger logger = LoggerFactory.getLogger(getClass());

	public final static int STATE_INIT = 0;
	public final static int STATE_STOP = 1;
	public final static int STATE_START = 2;
	public final static int STATE_SHUTDOWN = 3;

	private int state = STATE_INIT;

	private String name;

	// DatabasePolicyHandlerManager databasePolicyHandlerManager;

	ChannelManager channelManager;
	LoaderManager loaderManager;
	BoterManager boterManager;
	BotLoaderManager botLoaderManager;
	FinisherManager finisherManager;
	TraceErrorHandlerManager traceErrorHandlerManager;
	BotErrorHandlerManager botErrorHandlerManager;
	MonitorManager monitorManager;
	UnmatchHandlerManager unmatchHandlerManager;
	// DatabasePolicyHandlerManager databasePolicyHandlerManager;
	TesterManager testerManager;
	ConfigurationManager configurationManager;
	SystemErrorTestManager systemErrorTestManager;

	CacheManager cacheManager;

	/**
	 * 
	 * @param name
	 */
	public TraceServer(
			String name,
			ChannelManager channelManager,
			LoaderManager loaderManager,
			BoterManager boterManager,
			BotLoaderManager botLoaderManager,
			FinisherManager finisherManager,
			TraceErrorHandlerManager traceErrorHandlerManager,
			BotErrorHandlerManager botErrorHandlerManager,
			MonitorManager monitorManager,
			UnmatchHandlerManager unmatchHandlerManager,
			// DatabasePolicyHandlerManager databasePolicyHandlerManager,
			TesterManager testerManager,
			ConfigurationManager configurationManager,
			SystemErrorTestManager systemErrorTestManager,
			CacheManager cacheManager) {
		this.name = name;
		this.channelManager = channelManager;
		this.loaderManager = loaderManager;
		this.boterManager = boterManager;
		this.botLoaderManager = botLoaderManager;
		this.finisherManager = finisherManager;
		this.traceErrorHandlerManager = traceErrorHandlerManager;
		this.botErrorHandlerManager = botErrorHandlerManager;
		this.monitorManager = monitorManager;
		this.unmatchHandlerManager = unmatchHandlerManager;
		// this.databasePolicyHandlerManager = databasePolicyHandlerManager;
		this.testerManager = testerManager;
		this.configurationManager = configurationManager;
		this.systemErrorTestManager = systemErrorTestManager;
		this.cacheManager = cacheManager;

	}

	/**
	 * 
	 * @throws Exception
	 */
	public void ready() throws Exception {

		loaderManager.ready();
		boterManager.ready();
		botLoaderManager.ready();
		finisherManager.ready();
		botErrorHandlerManager.ready();
		traceErrorHandlerManager.ready();
		unmatchHandlerManager.ready();
		// databasePolicyHandlerManager.ready();
		state = STATE_INIT;
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception {

		startBotLoader();
		startBoter();
		startLoader();
		startChannel();
		startFinisher();
		if (startTraceErrorHandler)
			startTraceErrorHandler();
		if (startBotErrorHandler)
			startBotErrorHandler();
		startUnmatchHandler();
		// startDatabasePolicyHandler();

		if (configurationManager.getConfig().getSystemErrorTestManagerConfig() != null
				&& configurationManager.getConfig().getSystemErrorTestManagerConfig().isStartOnLoad()) {
			systemErrorTestManager.start();
		}

		state = STATE_START;
	}

	boolean startBotErrorHandler = true;
	boolean startTraceErrorHandler = true;

	long stopDelay = 1000;

	/**
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {
		stopChannel();
		Thread.sleep(stopDelay);
		stopLoader();
		Thread.sleep(stopDelay);
		stopBoter();
		Thread.sleep(stopDelay);
		stopBotLoader();
		Thread.sleep(stopDelay);
		stopFinisher();

		if (startTraceErrorHandler)
			stopTraceErrorHandler();
		if (startBotErrorHandler)
			stopBotErrorHandler();
		stopUnmatchHandler();

		// stopDatabasePolicyHandler();

		if (configurationManager.getConfig().getSystemErrorTestManagerConfig().isStartOnLoad()) {
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

	// public void startDatabasePolicyHandler() throws Exception {
	// databasePolicyHandlerManager.start();
	// }

	// public void stopDatabasePolicyHandler() {
	// databasePolicyHandlerManager.stop();
	// }

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
		if (ChannelManager.STATE_CHANNEL_STARTING == channelManager.getState()
				|| ChannelManager.STATE_CHANNEL_STARTING == channelManager.getState()) {
			throw new Exception("ChannelManagerStateException(Now Channel manager is stopping or starting channels.)");
		}
		channelManager.startChannels();
		// channelManager.startChannelHealthCheck();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void stopChannel() throws Exception {
		// channelManager.stopChannelHealthCheck();
		if (ChannelManager.STATE_CHANNEL_STARTING == channelManager.getState()
				|| ChannelManager.STATE_CHANNEL_STARTING == channelManager.getState()) {
			throw new Exception("ChannelManagerStateException(Now Channel manager is stopping or starting channels.)");
		}
		channelManager.stopChannels();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void startLoader() throws Exception {
		loaderManager.start();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void stopLoader() throws Exception {
		loaderManager.stop();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void startBoter() throws Exception {
		boterManager.start();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void stopBoter() throws Exception {
		boterManager.stop();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void startBotLoader() throws Exception {
		botLoaderManager.start();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void stopBotLoader() throws Exception {
		botLoaderManager.stop();
	}

	public void startMonitor() throws Exception {
		monitorManager.startMonitors();
	}

	public void stopMonitor() throws Exception {
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
