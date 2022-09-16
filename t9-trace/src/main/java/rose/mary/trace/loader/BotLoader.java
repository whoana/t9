/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.loader;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.data.common.BEL;
import rose.mary.trace.core.data.common.Bot;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.StateEvent;
import rose.mary.trace.core.database.FromDatabase;
//import rose.mary.trace.core.envs.Variables;
import rose.mary.trace.core.exception.ExceptionHandler;
import rose.mary.trace.core.monitor.ThroughputMonitor;
import rose.mary.trace.database.service.BotService;
import rose.mary.trace.system.SystemLogger;

/**
 * <pre>
 * The BotLoader has a role to loading a {@link Bot Bot} message into the table TOP0503
 * </pre>
 *
 * @author whoana
 * @since Sep 19, 2019
 */
public class BotLoader implements Runnable {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	protected static final int DEFAULT_COMMIT_COUNT = 1000;

	private boolean isShutdown = true;

	private BotService botService;

	private Thread thread = null;

	private ExceptionHandler exceptionHandler;

	private CacheProxy<String, StateEvent> botCache;

	private CacheProxy<String, State> finCache;

	private CacheProxy<String, State> errorCache;

	private ThroughputMonitor tpm;

	private int commitCount = DEFAULT_COMMIT_COUNT;

	private long delayForNoMessage = 10;

	private long exceptionDelay = 1;

	private Map<String, State> loadBots = new LinkedHashMap<String, State>();

	// private Map<String, String> dbLoadStates = new LinkedHashMap<String,
	// String>();

	private long commitLapse = System.currentTimeMillis();

	private long maxCommitWait = 1000;

	String name;

	FromDatabase fromDatabase;

	/**
	 *
	 * @param commitCount
	 * @param delayForNoMessage
	 * @param botCache
	 * @param errorCache
	 * @param botService
	 * @param tpm
	 * @param exceptionHandler
	 */
	public BotLoader(
			String name,
			int commitCount,
			long delayForNoMessage,
			CacheProxy<String, StateEvent> botCache,
			CacheProxy<String, State> finCache,
			CacheProxy<String, State> errorCache,
			BotService botService,
			ThroughputMonitor tpm,
			ExceptionHandler exceptionHandler) {
		this.name = name;
		this.commitCount = commitCount;
		this.botCache = botCache;
		this.finCache = finCache;
		this.errorCache = errorCache;
		this.botService = botService;
		this.tpm = tpm;
		this.exceptionHandler = exceptionHandler;
		this.delayForNoMessage = delayForNoMessage;
	}

	// finCache 삭제 옵션 , 외부 설정으로 등록되지 않은 단계
	// 필요해지면 놀출 20220825
	boolean deleteFinishedBotOpt = false;

	/**
	 *
	 * @throws Exception
	 */
	public void commit() throws Exception {
		try {
			Collection<State> bots = loadBots.values();
			int count = loadBots.size();
			if (tpm != null)
				tpm.count(count);
			botService.mergeBots(bots, finCache); // SIGKILL LOG : SKL-BL001

			// dbCache.put(dbLoadStates); // SIGKILL LOG : SKL-BL002

			botCache.removeAll(loadBots.keySet()); // SIGKILL LOG : SKL-BL003
		} catch (Exception e) {
			// ----------------------------------------------------
			// 20220905
			// 예외 발생시 에러캐시로 옮기고 B.C데이터 삭제하는 부분에 대해서는
			// 수정이 필요한지 고민해볼 부분이 있따.
			// 에러큐로 빼지 않고 그대로 놔두고 시스템 종료, 문제해결, 재기동 후
			// 에러큐에 넣지 안아도 B.C 에 있는 것은 재처리 되므로....
			// ----------------------------------------------------
			// if (errorCache != null) {
			// errorCache.put(loadBots);
			// }
			// botCache.removeAll(loadBots.keySet());
			logger.error("Loader commit Exception", e);
			throw e;
		} finally {
			// dbLoadStates.clear();
			loadBots.clear();
			commitLapse = System.currentTimeMillis();
		}
	}

	/**
	 *
	 */
	public void rollback() {
	}

	/**
	 *
	 * @param tpm
	 */
	public void setThroughputMonitor(ThroughputMonitor tpm) {
		this.tpm = tpm;
	}

	/**
	 *
	 * @return
	 */
	public ThroughputMonitor getThroughputMonitor() {
		return tpm;
	}

	/**
	 *
	 * @throws Exception
	 */
	public void start() throws Exception {
		if (thread != null)
			stop();
		thread = new Thread(this, name);
		isShutdown = false;
		thread.start();
	}

	public void stop() {
		// if (Variables.startStopAsap) {
		// stopAsap();
		// } else {
		// stopGracefully();
		// }
		// stopGracefully();

		isShutdown = true;
		if (thread != null)
			thread.interrupt();
	}

	public void run() {
		// if (Variables.startStopAsap) {
		// runAsap();
		// } else {
		// runGracefully();
		// }
		runGracefully();

	}

	/**
	 *
	 */
	/*
	 * public void stopAsap() {
	 * isShutdown = true;
	 * if (thread != null)
	 * thread.interrupt();
	 *
	 * }
	 */

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

	/*
	 * public void runAsap() {
	 * logger.info(Util.join("start botLoader:[" + name + "]"));
	 * while (true) {
	 * try {
	 * 
	 * if (thread.isInterrupted())
	 * break;
	 * 
	 * if (loadBots.size() > 0 && (System.currentTimeMillis() - commitLapse >=
	 * maxCommitWait)) {
	 * commit();
	 * }
	 * 
	 * Set<String> keys = botCache.keys();
	 * if (keys == null || keys.size() == 0) {
	 * try {
	 * Thread.sleep(delayForNoMessage);
	 * continue;
	 * } catch (java.lang.InterruptedException ie) {
	 * isShutdown = true;
	 * break;
	 * }
	 * }
	 * 
	 * for (String key : keys) {
	 * //State state = botCache.get(key);
	 * 
	 * //botCache 에는 State 가 아니라 StateEvent 가 들어 있는 것으로 변경한댜.
	 * StateEvent evt = botCache.get(key);
	 * String botId = evt.getBotId();
	 * State state = finCache.get(botId);
	 * 
	 * addBatch(key, state);
	 * if (loadBots.size() > 0 && (loadBots.size() % commitCount == 0)) {
	 * try {
	 * commit();
	 * break;
	 * } catch (Exception e) {
	 * if (exceptionHandler != null) {
	 * exceptionHandler.handle("", e);
	 * } else {
	 * logger.error("", e);
	 * }
	 * break;
	 * }
	 * }
	 * }
	 * 
	 * } catch (Exception e) {
	 * if (exceptionHandler != null) {
	 * exceptionHandler.handle("", e);
	 * } else {
	 * logger.error("", e);
	 * }
	 * 
	 * try {
	 * Thread.sleep(exceptionDelay);
	 * } catch (InterruptedException e1) {
	 * isShutdown = true;
	 * break;
	 * }
	 * 
	 * }
	 * }
	 * 
	 * try {
	 * commit();
	 * } catch (Exception e) {
	 * if (exceptionHandler != null) {
	 * exceptionHandler.handle("", e);
	 * } else {
	 * logger.error("", e);
	 * }
	 * }
	 * 
	 * isShutdown = true;
	 * logger.info(Util.join("stop botLoader:[" + name + "]"));
	 * }
	 */

	/**
	 *
	 */
	public void runGracefully() {
		logger.info(Util.join("start botLoader:[" + name + "]"));
		while (Thread.currentThread() == thread && !isShutdown) {
			try {
				if (loadBots.size() > 0 &&
						(System.currentTimeMillis() - commitLapse >= maxCommitWait)) {
					commit();
				}

				Set<String> keys = null;
				if (botCache.isAccessable()) {
					keys = botCache.keys();
				}
				if (keys == null || keys.size() == 0) {
					try {
						Thread.sleep(delayForNoMessage);
						continue;
					} catch (java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}

				for (String key : keys) {
					// State state = botCache.get(key);
					// botCache 에는 State 가 아니라 StateEvent 가 들어 있는 것으로 변경한댜.
					StateEvent evt = botCache.get(key);
					String botId = evt.getBotId();
					State state = finCache.get(botId);
					// logger.debug(name + "," + botCache.getName() + ",key=" + key + ", tk=[" +
					// state.getContext() + "]");
					if (state == null && fromDatabase != null) {
						state = fromDatabase.getState(botId);
						// F.C 에서 삭제되어 디비에서 최종상태를 조회하여 얻어온 값으로 백로그컬럼에 상황을 로깅하기 위해 backLog 값을 세팅한다.
						if (State.ING.equals(state.getStatus())) {
							state.setBackendLog(BEL.W0001);
						} else {
							state.setBackendLog(BEL.W0002);
						}
						botCache.remove(key);
					}
					// 20220901
					// DB에도 없는 건에 대한 처리는 어떻게 할지 고민해보자.(논리적으로 없어야함. 있으면 골치아품)
					if (state == null)
						continue;

					addBatch(key, state);
					if (loadBots.size() > 0 && (loadBots.size() % commitCount == 0)) {
						try {
							commit();
							break;
						} catch (Exception e) {
							if (exceptionHandler != null) {
								exceptionHandler.handle("", e);
							} else {
								logger.error("", e);
							}
							break;
						}
					}
				}
			} catch (Exception e) {
				if (exceptionHandler != null) {
					exceptionHandler.handle("", e);
				} else {
					logger.error("", e);
				}

				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}
			}
		}

		try {
			commit();
		} catch (Exception e) {
			if (exceptionHandler != null) {
				exceptionHandler.handle("", e);
			} else {
				logger.error("", e);
			}
		}

		isShutdown = true;
		logger.info(Util.join("stop botLoader:[" + name + "]"));
	}

	/**
	 * @param trace
	 */
	private void addBatch(String key, State state) {
		loadBots.put(key, state);
		// dbLoadStates.put(key, state.getBotId());
	}

	/**
	 *
	 * @return
	 */
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	/**
	 *
	 * @param loaderExceptionHandler
	 */
	public void setExceptionHandler(ExceptionHandler loaderExceptionHandler) {
		this.exceptionHandler = loaderExceptionHandler;
	}

	/**
	 * @return the commitCount
	 */
	public int getCommitCount() {
		return commitCount;
	}

	/**
	 * @param commitCount the commitCount to set
	 */
	public void setCommitCount(int commitCount) {
		this.commitCount = commitCount;
	}

	public FromDatabase getFromDatabase() {
		return fromDatabase;
	}

	public void setFromDatabase(FromDatabase fromDatabase) {
		this.fromDatabase = fromDatabase;
	}
}
