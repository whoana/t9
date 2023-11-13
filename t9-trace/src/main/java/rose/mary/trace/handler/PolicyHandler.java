package rose.mary.trace.handler;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.channel.Channel;
import rose.mary.trace.core.config.PolicyConfig;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.data.policy.SeriousErrorPolicy;
import rose.mary.trace.core.envs.Variables;
import rose.mary.trace.core.exception.SystemError;
import rose.mary.trace.database.service.SystemService;
import rose.mary.trace.manager.ChannelManager;
import rose.mary.trace.manager.ServerManager;
import rose.mary.trace.server.TraceServer;
import rose.mary.trace.system.SystemLogger;

/**
 * <pre>
 * 데이터베이스 Health 체크 결과에 따른 처리 절차를 구현한다.
 * DatabasePolicyHandler.java
 * 
 * </pre>
 * 
 * @author whoana
 * @since 20200515
 */
// @Aspect
// @Component
public class PolicyHandler implements Runnable {

	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");

	SeriousErrorPolicy policy = SeriousErrorPolicy.SHUTDOWN;

	ServerManager serverManager;

	ChannelManager channelManager;

	SystemService systemService;

	MessageSource messageResource;

	static boolean hasDatabaseError = false;

	private boolean isShutdown = true;

	private long exceptionDelay = 1000;

	private long policyCheckDelay = 1000;

	private long databaseCheckDelay = 10 * 1000;

	private int policyCount = 1;

	String name;

	Thread thread;

	CacheProxy<String, Trace> errorCache1;
	CacheProxy<String, State> errorCache2;
	CacheProxy<String, SystemError> systemErrorCache;

	public PolicyHandler(
			MessageSource messageResource,
			SystemService systemService,
			ServerManager serverManager,
			ChannelManager channelManager,
			PolicyConfig config,
			CacheProxy<String, Trace> errorCache1,
			CacheProxy<String, State> errorCache2,
			CacheProxy<String, SystemError> systemErrorCache) {
		this.messageResource = messageResource;
		this.systemService = systemService;
		this.serverManager = serverManager;
		this.channelManager = channelManager;
		this.name = config.getName();
		this.exceptionDelay = config.getExceptionDelay();
		this.policyCheckDelay = config.getPolicyCheckDelay();
		this.databaseCheckDelay = config.getDatabaseCheckDelay();
		this.policyCount = config.getPolicyCount();
		policy = new SeriousErrorPolicy(config.getPolicy());
		this.errorCache1 = errorCache1;
		this.errorCache2 = errorCache2;
		this.systemErrorCache = systemErrorCache;
	}

	public void start() throws Exception {
		if (thread != null)
			stop();
		thread = new Thread(this, name);
		thread.setName("PolicyHandler");
		isShutdown = false;
		thread.start();
	}

	public void stop() {
		if (Variables.startStopAsap) {
			stopAsap();
		} else {
			stopGracefully();
		}
	}

	public void run() {
		runGracefully();
	}

	public void stopGracefully() {
		isShutdown = true;
		if (thread != null) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}

	public void stopAsap() {
		isShutdown = true;
		if (thread != null)
			thread.interrupt();
	}

	boolean databaseHealth = false;
	int healthCheckFailCount = 0;
	long lastDatabaseHealthCheckTime = 0;

	public void runGracefully() {
		logger.info(Util.join("start PolicyHandler:[" + name + "]"));
		while (Thread.currentThread() == thread && !isShutdown) {

			try {

				try {
					Thread.sleep(policyCheckDelay);
				} catch (InterruptedException ie) {
					isShutdown = true;
					break;
				}

				logger.debug("**************************************************");
				logger.debug("* Policy[" + policy + "] Info ");
				logger.debug("**************************************************");
				logger.debug("policyCount:" + policyCount);

				if ((System.currentTimeMillis() - lastDatabaseHealthCheckTime) > databaseCheckDelay) {

					if (systemService.databaseHealthCheck()) {
						healthCheckFailCount = 0;
						logger.debug("database health check result : true");
						databaseHealth = true;
						switch (policy.getPolicy()) {
							case SeriousErrorPolicy.stopChannel:
								if (!serverManager.getChannelsStarted()) {
									logger.debug("정책에 따라 멈쳐있던 채널들을 재시작 합니다.");
									serverManager.startChannel();
								}
								break;
							case SeriousErrorPolicy.stopServer:
								if (serverManager.checkServerState() == TraceServer.STATE_STOP) {
									logger.debug("정책에 따라 멈쳐있던 서버를 재시작 합니다.");
									serverManager.startServer();
								}
								break;
						}
						// databse 에 문제가 없으면 채널들을 체크해 본다.
						List<Channel> channels = channelManager.getChannels();
						if (channels != null && channels.size() > 0) {
							for (Channel channel : channels) {

								if (channel.isHealthCheck() && !channel.isShutdown()) {

									if (!channel.ping()) {
										logger.debug(Util.join("channel[", channel.getName(),
												"] health check result: false, so try to restart channel"));
										try {
											channel.stop();
											channel.start();
										} catch (Exception e) {
											logger.error(
													Util.join("I can not restart channel[", channel.getName(), "]"),
													e);
										}
									} else {
										logger.debug(
												Util.join("channel[", channel.getName(),
														"] health check result: true"));
									}

								}
							}
						}

					} else {
						healthCheckFailCount++;

						if (healthCheckFailCount >= policyCount) {
							databaseHealth = false;
						}
					}
					lastDatabaseHealthCheckTime = System.currentTimeMillis();
				}

				logger.debug("healthCheckFailCount:" + healthCheckFailCount);
				logger.debug("databaseHealth:" + databaseHealth);

				Set<String> keys = systemErrorCache.keys();
				SystemError error = null;
				for (String key : keys) {
					error = systemErrorCache.get(key);
					SystemLogger.error("PolicyHandler will handle the error:" + error.getId() + "," + error.getMsg());
					break;// 하나만 꺼낸다.
				}

				if (!databaseHealth || error != null) {
					serverManager.addWarnning("데이터베이스 연결에 문제가 있거나 시스템에러가 발생되었습니다.");
					serverManager.addWarnning("문제를 해결한 후에 T9 서버를 시작해 주세요.");
					serverManager.addWarnning("문제가 해결되기 전까지는 T9 서버를 재시작할 수 없습니다.");
					serverManager.addWarnning("문제해결을 위해서는 {T9_HOME}/logs/system.log를 참고해주세요.");
					// systemErrorCache.removeAll(keys);
					systemErrorCache.clear();// 전부 지우는게 좋겠어.
					runPolicy();
				} else {
					logger.debug("The system has no problem.");
				}

			} catch (Throwable t) {
				logger.error("PolicyHandler exception:", t);
				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException ie) {
					isShutdown = true;

					break;
				}
			}
		}

		isShutdown = true;
		logger.info(Util.join("stop PolicyHandler:[" + name + "]"));

	}

	private synchronized void runPolicy() throws Exception {
		switch (policy.getPolicy()) {
			case SeriousErrorPolicy.stopChannel:
				logger.info("Policy.stopChannel 정책을 실행합니다.");
				serverManager.stopChannel();
				logger.info("Policy.stopChannel 정책을 실행을 처리하였습니다.");
				break;
			case SeriousErrorPolicy.stopServer:
				logger.info("Policy.stopServer 정책을 실행합니다.");
				serverManager.stopServer();
				logger.info("Policy.stopServer 정책을 실행을 처리하였습니다.");
				break;
			case SeriousErrorPolicy.shutdownServer:
				logger.info("Policy.shutdownServer 정책을 실행합니다.");
				// isShutdown = true;
				serverManager.shutdownServer();
				logger.info("Policy.shutdownServer 정책을 실행을 처리하였습니다.");
				// thread.interrupt();
				break;
			default:
				logger.info("Policy정보가 존재하지 않습니다.(세팅값:" + policy.getPolicy() + ")");
				break;
		}
	}

	public static synchronized void setDatabaseError(boolean hasError) {
		hasDatabaseError = hasError;
	}

}
