package rose.mary.trace.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.TraceServer;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.channel.Channel;
import rose.mary.trace.core.config.DatabasePolicyConfig;
import rose.mary.trace.core.envs.Variables;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.data.policy.DatabasePolicy;
import rose.mary.trace.database.service.SystemService;
import rose.mary.trace.manager.ChannelManager;
import rose.mary.trace.manager.ServerManager;

/**
 * <pre>
 * 데이터베이스 Health 체크 결과에 따른 처리 절차를 구현한다.
 * SystemErrorHandler.java
 * 
 * </pre>
 * 
 * @author whoana
 * @since 20200515
 */
// @Aspect
// @Component
public class SystemErrorHandler implements Runnable {

	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");

	DatabasePolicy policy = DatabasePolicy.SHUTDOWN;

	ServerManager serverManager;

	ChannelManager channelManager;

	SystemService systemService;

	MessageSource messageResource;

	static boolean hasDatabaseError = false;

	private boolean isShutdown = true;

	private long exceptionDelay = 1000;

	private long policyCheckDelay = 10 * 1000;

	private int policyCount = 1;

	private DatabasePolicyConfig config;

	String name;

	Thread thread;

	CacheProxy<String, Trace> errorCache1;
	CacheProxy<String, State> errorCache2;

	public SystemErrorHandler(
			MessageSource messageResource,
			SystemService systemService,
			ServerManager serverManager,
			ChannelManager channelManager,
			DatabasePolicyConfig config,
			CacheProxy<String, Trace> errorCache1,
			CacheProxy<String, State> errorCache2) {
		this.messageResource = messageResource;
		this.systemService = systemService;
		this.serverManager = serverManager;
		this.channelManager = channelManager;
		this.config = config;
		this.name = config.getName();
		this.exceptionDelay = config.getExceptionDelay();
		this.policyCheckDelay = config.getPolicyCheckDelay();
		this.policyCount = config.getPolicyCount();
		policy = new DatabasePolicy(config.getPolicy());
		this.errorCache1 = errorCache1;
		this.errorCache2 = errorCache2;

	}

	public void start() throws Exception {
		if (thread != null)
			stop();
		thread = new Thread(this, name);
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
		if (Variables.startStopAsap) {
			runAsap();
		} else {
			runGracefully();
		}
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

	// @AfterThrowing(pointcut = "execution(public *
	// rose.mary.trace.database.service.*Service.*())", throwing = "exception")
	// public void handleDatabaseException(JoinPoint jp, Throwable exception) {
	// if(exception instanceof org.apache.ibatis.exceptions.PersistenceException) {
	// try {
	// switch(policy.getPolicy()) {
	// case DatabasePolicy.stopChannel :
	// logger.info("DatabasePolicy.stopChannel 정책을 실행합니다.");
	// serverManager.stopChannel();
	// logger.info("DatabasePolicy.stopChannel 정책을 실행을 처리하였습니다.");
	// break;
	// case DatabasePolicy.stopServer :
	// logger.info("DatabasePolicy.stopServer 정책을 실행합니다.");
	// serverManager.stopServer();
	// logger.info("DatabasePolicy.stopServer 정책을 실행을 처리하였습니다.");
	// break;
	// case DatabasePolicy.shutdownServer :
	// logger.info("DatabasePolicy.shutdownServer 정책을 실행합니다.");
	// isShutdown = true;
	// serverManager.shutdownServer();
	// logger.info("DatabasePolicy.shutdownServer 정책을 실행을 처리하였습니다.");
	// break;
	// default :
	// logger.info("DatabasePolicy정보가 존재하지 않습니다.(세팅값:" + policy.getPolicy() + ")");
	// break;
	// }
	//
	// } catch (Exception e) {
	// logger.info("DatabasePolicy.stopChannel 정책 실행도중 예외가 발생되었습니다.",e);
	// }
	// }
	// }

	int healthCheckFailCount = 0;

	public void runAsap() {
		logger.info(Util.join("start SystemErrorHandler:[" + name + "]"));
		while (true) {

			try {
				if (thread.isInterrupted())
					break;

				logger.info("**************************************************");
				logger.info("* DatabasePolicy[" + policy + "] Info ");
				logger.info("**************************************************");
				logger.info("policyCount:" + policyCount);
				boolean databaseHealth = false;
				if (systemService.databaseHealthCheck()) {
					healthCheckFailCount = 0;
					logger.info("database health check result : true");
					databaseHealth = true;
					switch (policy.getPolicy()) {
						case DatabasePolicy.stopChannel:
							if (!serverManager.getChannelsStarted()) {
								logger.info("DatabasePolicy 정책에 따라 멈쳐있던 채널들을 재시작 합니다.");
								serverManager.startChannel();
							}
							break;
						case DatabasePolicy.stopServer:
							if (serverManager.checkServerState() == TraceServer.STATE_STOP) {
								logger.info("DatabasePolicy 정책에 따라 멈쳐있던 서버를 재시작 합니다.");
								serverManager.startServer();
							}
							break;
					}
					// databse 에 문제가 없으면 채널들을 체크해 본다.
					List<Channel> channels = channelManager.getChannels();
					if (channels != null && channels.size() > 0) {
						for (Channel channel : channels) {
							logger.info(Util.join("channel:", channel.getName(), " isHealthCheck:",
									channel.isHealthCheck(), ", running:", !channel.isShutdown()));
							if (channel.isHealthCheck() && !channel.isShutdown()) {

								if (!channel.ping()) {
									logger.info(Util.join("channel[", channel.getName(),
											"] health check result: false, so try to restart channel"));
									try {
										channel.stop();
										channel.start();
									} catch (Exception e) {
										logger.error(Util.join("I can not restart channel[", channel.getName(), "]"),
												e);
									}
								} else {
									logger.info(
											Util.join("channel[", channel.getName(), "] health check result: true"));
								}

							} else {

							}
						}
					}

				} else {
					healthCheckFailCount++;
					if (healthCheckFailCount >= policyCount) {
						databaseHealth = false;
					}
				}
				logger.info("healthCheckFailCount:" + healthCheckFailCount);
				logger.info("databaseHealth :" + databaseHealth);

				// 에러 캐시에 뭔가 쌓여 있으면.
				// boolean hasDatabaseProblem = errorCache1.size() > 0 || errorCache2.size() > 0
				// || !databaseHealth;
				// if(hasDatabaseProblem) {
				if (!databaseHealth) {
					serverManager.addWarnning("데이터베이스 연결에 문제가 있습니다.");
					// logger.info("[주의!]");
					// if(!databaseHealth) {
					// serverManager.addWarnning("데이터베이스 연결에 문제가 있습니다.");
					// logger.info("데이터베이스 연결에 문제가 있습니다.");
					// }
					// if(errorCache1.size() > 0) {
					// serverManager.addWarnning("처리하지 못한 트레킹 메시지가 존재합니다.");
					// //logger.info("처리하지 못한 트레킹 메시지가 존재합니다.");
					// }
					// if(errorCache2.size() > 0) {
					// serverManager.addWarnning("처리하지 못한 트레킹명세 메시지가 존재합니다.");
					// //logger.info("처리하지 못한 트레킹명세 메시지가 존재합니다.");
					// }
					serverManager.addWarnning("정책 실행 완료 후에 위의 문제를 해결한 후에 T9 서버를 시작해 주세요.");
					serverManager.addWarnning("문제가 해결되기 전까지는 T9 서버를 재시작할 수 없습니다.");
					// serverManager.addWarnning("데이터베이스와의 접속 및 서비스에 문제가 없다면 처리하지 못한 트레킹 및 트레킹명세
					// 메시지는 리커버리 모드 수행을 통해 복구를 진행해 주세요.");
					// serverManager.addWarnning("리커버리모드 실행 방법: {T9_HOME}/bin/run.sh -r ");

					// logger.info("정책 실행 완료 후에 위의 문제를 해결한 후에 T9 서버를 시작해 주세요.");
					// logger.info("문제가 해결되기 전까지는 T9 서버를 재시작할 수 없습니다.");
					runPolicy();
				} else {
					logger.info("The database has no problem.");
				}

				try {
					Thread.sleep(policyCheckDelay);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}

			} catch (Throwable t) {
				logger.error("SystemErrorHandler exception:", t);
				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}
			}
		}

		isShutdown = true;
		logger.info(Util.join("stop SystemErrorHandler:[" + name + "]"));

	}

	public void runGracefully() {
		logger.info(Util.join("start SystemErrorHandler:[" + name + "]"));
		while (Thread.currentThread() == thread && !isShutdown) {

			try {

				logger.info("**************************************************");
				logger.info("* DatabasePolicy[" + policy + "] Info ");
				logger.info("**************************************************");
				logger.info("policyCount:" + policyCount);
				boolean databaseHealth = false;
				if (systemService.databaseHealthCheck()) {
					healthCheckFailCount = 0;
					logger.info("database health check result : true");
					databaseHealth = true;
					switch (policy.getPolicy()) {
						case DatabasePolicy.stopChannel:
							if (!serverManager.getChannelsStarted()) {
								logger.info("DatabasePolicy 정책에 따라 멈쳐있던 채널들을 재시작 합니다.");
								serverManager.startChannel();
							}
							break;
						case DatabasePolicy.stopServer:
							if (serverManager.checkServerState() == TraceServer.STATE_STOP) {
								logger.info("DatabasePolicy 정책에 따라 멈쳐있던 서버를 재시작 합니다.");
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
									logger.info(Util.join("channel[", channel.getName(),
											"] health check result: false, so try to restart channel"));
									try {
										channel.stop();
										channel.start();
									} catch (Exception e) {
										logger.error(Util.join("I can not restart channel[", channel.getName(), "]"),
												e);
									}
								} else {
									logger.info(
											Util.join("channel[", channel.getName(), "] health check result: true"));
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
				logger.info("healthCheckFailCount:" + healthCheckFailCount);
				logger.info("databaseHealth :" + databaseHealth);

				// 에러 캐시에 뭔가 쌓여 있으면.
				// boolean hasDatabaseProblem = errorCache1.size() > 0 || errorCache2.size() > 0
				// || !databaseHealth;
				// if(hasDatabaseProblem) {
				if (!databaseHealth) {
					// logger.info("[주의!]");
					serverManager.addWarnning("데이터베이스 연결에 문제가 있습니다.");

					// if(errorCache1.size() > 0) {
					// serverManager.addWarnning("처리하지 못한 트레킹 메시지가 존재합니다.");
					// //logger.info("처리하지 못한 트레킹 메시지가 존재합니다.");
					// }
					// if(errorCache2.size() > 0) {
					// serverManager.addWarnning("처리하지 못한 트레킹명세 메시지가 존재합니다.");
					// //logger.info("처리하지 못한 트레킹명세 메시지가 존재합니다.");
					// }
					serverManager.addWarnning("정책 실행 완료 후에 위의 문제를 해결한 후에 T9 서버를 시작해 주세요.");
					serverManager.addWarnning("문제가 해결되기 전까지는 T9 서버를 재시작할 수 없습니다.");
					// serverManager.addWarnning("데이터베이스와의 접속 및 서비스에 문제가 없다면 처리하지 못한 트레킹 및 트레킹명세
					// 메시지는 리커버리 모드 수행을 통해 복구를 진행해 주세요.");
					// serverManager.addWarnning("리커버리모드 실행 방법: {T9_HOME}/bin/run.sh -r ");

					// logger.info("정책 실행 완료 후에 위의 문제를 해결한 후에 T9 서버를 시작해 주세요.");
					// logger.info("문제가 해결되기 전까지는 T9 서버를 재시작할 수 없습니다.");
					runPolicy();
				} else {
					logger.info("The database has no problem.");
				}

				try {
					Thread.sleep(policyCheckDelay);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}

			} catch (Throwable t) {
				logger.error("SystemErrorHandler exception:", t);
				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}
			}
		}

		isShutdown = true;
		logger.info(Util.join("stop SystemErrorHandler:[" + name + "]"));

	}

	private void runPolicy() throws Exception {
		switch (policy.getPolicy()) {
			case DatabasePolicy.stopChannel:
				logger.info("DatabasePolicy.stopChannel 정책을 실행합니다.");
				serverManager.stopChannel();
				logger.info("DatabasePolicy.stopChannel 정책을 실행을 처리하였습니다.");
				break;
			case DatabasePolicy.stopServer:
				logger.info("DatabasePolicy.stopServer 정책을 실행합니다.");
				serverManager.stopServer();
				logger.info("DatabasePolicy.stopServer 정책을 실행을 처리하였습니다.");
				break;
			case DatabasePolicy.shutdownServer:
				logger.info("DatabasePolicy.shutdownServer 정책을 실행합니다.");
				isShutdown = true;
				serverManager.shutdownServer();
				logger.info("DatabasePolicy.shutdownServer 정책을 실행을 처리하였습니다.");
				break;
			default:
				logger.info("DatabasePolicy정보가 존재하지 않습니다.(세팅값:" + policy.getPolicy() + ")");
				break;
		}
	}

	public static synchronized void setDatabaseError(boolean hasError) {
		hasDatabaseError = hasError;
	}

}
