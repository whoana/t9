/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.loader;
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pep.per.mint.common.util.Util;
import rose.mary.trace.RunMode;
import rose.mary.trace.T9;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.envs.Variables;
import rose.mary.trace.core.exception.ExceptionHandler;
import rose.mary.trace.core.monitor.ThroughputMonitor;
import rose.mary.trace.database.service.TraceService;

/**
 * <pre>
 * rose.mary.trace.database
 * Loader.java
 * </pre>
 * 
 * @author whoana
 * @date Aug 26, 2019
 */
public class TraceLoader implements Runnable {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	protected final static int DEFAULT_COMMIT_COUNT = 1000;

	private boolean isShutdown = true;

	private TraceService traceLoadService;

	private Thread thread = null;

	private ExceptionHandler exceptionHandler;

	private CacheProxy<String, Trace> distributeCache;

	private CacheProxy<String, Trace> mergeCache;

	private CacheProxy<String, Trace> errorCache;

	private ThroughputMonitor tpm;

	private int commitCount = DEFAULT_COMMIT_COUNT;

	private long delayForNoMessage = 1000;

	private long exceptionDelay = 5000;

	private boolean loadError = true;

	private boolean loadContents = true;

	private Map<String, Trace> loadItems = new HashMap<String, Trace>();

	private long commitLapse = System.currentTimeMillis();

	private long maxCommitWait = 1000;

	private List<String> dups = new ArrayList<String>();

	private String oldKey = null;

	String name;

	/**
	 * 
	 * @param name
	 * @param commitCount
	 * @param delayForNoMessage
	 * @param loadError
	 * @param loadContents
	 * @param distributeCache
	 * @param mergeCache
	 * @param errorCache
	 * @param traceLoadService
	 * @param tpm
	 * @param exceptionHandler
	 */
	public TraceLoader(String name, int commitCount, long delayForNoMessage, boolean loadError, boolean loadContents,
			CacheProxy<String, Trace> distributeCache, CacheProxy<String, Trace> mergeCache,
			CacheProxy<String, Trace> errorCache, TraceService traceLoadService, ThroughputMonitor tpm,
			ExceptionHandler exceptionHandler) {
		this.name = name;
		this.commitCount = commitCount;
		this.distributeCache = distributeCache;
		this.mergeCache = mergeCache;
		this.traceLoadService = traceLoadService;
		this.tpm = tpm;
		this.exceptionHandler = exceptionHandler;
		this.delayForNoMessage = delayForNoMessage;
		this.loadError = loadError;
		this.loadContents = loadContents;
		this.errorCache = errorCache;
	}

	/**
	 * <pre>
	 * 	Database 관련 예외 발생시 에러큐로 빼고 채널을 종료 시킬 방안을 생각해 보자
	 * </pre>
	 * 
	 * @throws Exception
	 */
	public void commit() throws Exception {
		try {
			Collection<Trace> collection = loadItems.values();

			if (Variables.debugLineByLine)
				logger.debug(name + "-TLLBLD0101");

			traceLoadService.load(collection, loadError, loadContents);

			if (Variables.debugLineByLine)
				logger.debug(name + "-TLLBLD0102");

			if (tpm != null)
				tpm.count(loadItems.size());

			if (Variables.debugLineByLine)
				logger.debug(name + "-TLLBLD0103");

			if (T9.runMode == RunMode.Server) {
				mergeCache.put(loadItems);
			} else if (T9.runMode == RunMode.Distributor) {
				distribute(loadItems);
			}

			if (Variables.debugLineByLine)
				logger.debug(name + "-TLLBLD0104");

			distributeCache.removeAll(loadItems.keySet());

			if (Variables.debugLineByLine)
				logger.debug(name + "-TLLBLD0105");

		} catch (Exception e) {
			// ----------------------------------------------------
			// 20220905
			// 예외 발생시 에러캐시로 옮기고 D.C데이터 삭제하는 부분에 대해서는
			// 수정이 필요한지 고민해볼 부분이 있따.
			// 에러큐로 빼지 않고 그대로 놔두고 시스템 종료, 문제해결, 재기동 후
			// 에러큐에 넣지 안아도 D.C 에 있는 것은 재처리 되므로....
			//
			// 20221110
			// 에러발생된건이 재처리 될수 없는 건이라면, 이를테면 데이터 길이 오류등
			// 테이블에 입력될 수 없는 겅우라면
			// 로그 확인 후 캐시를 지우고 재기동하는 것이 합리적인 처리 정책인건지 고민해 볼것
			// ----------------------------------------------------
			if (errorCache != null) {
				errorCache.put(loadItems);
			}
			distributeCache.removeAll(loadItems.keySet());
			// ----------------------------------------------------

			// 20221111
			// errorCache를 이용하는 것으로 일단 수정하자.
			// logger.error("Loader commit Exception", e);
			// throw e;
		} finally {

			loadItems.clear();

			if (Variables.debugLineByLine)
				logger.debug(name + "-TLLBLD0106");

			commitLapse = System.currentTimeMillis();
		}
	}
	/*
	 * public void commit() throws Exception {
	 * 
	 * try {
	 * Collection<Trace> collection = loadItems.values();
	 * 
	 * traceLoadService.load(collection, loadError, loadContents);
	 * 
	 * if (tpm != null)
	 * tpm.count(loadItems.size());
	 * 
	 * if (T9.runMode == RunMode.Server) {
	 * mergeCache.put(loadItems);
	 * } else if (T9.runMode == RunMode.Distributor) {
	 * distribute(loadItems);
	 * }
	 * 
	 * distributeCache.removeAll(loadItems.keySet());
	 * loadItems.clear();
	 * 
	 * } catch (PersistenceException e) {
	 * if (errorCache != null) {
	 * errorCache.put(loadItems);
	 * }
	 * distributeCache.removeAll(loadItems.keySet());
	 * loadItems.clear();
	 * logger.error("Loader commit Exception", e);
	 * throw e;
	 * } catch (BatchUpdateException e) {
	 * if (errorCache != null) {
	 * errorCache.put(loadItems);
	 * }
	 * distributeCache.removeAll(loadItems.keySet());
	 * loadItems.clear();
	 * logger.error("Loader commit Exception", e);
	 * throw e;
	 * } catch (SQLException e) {
	 * if (errorCache != null) {
	 * errorCache.put(loadItems);
	 * }
	 * distributeCache.removeAll(loadItems.keySet());
	 * loadItems.clear();
	 * logger.error("Loader commit Exception", e);
	 * throw e;
	 * } catch (Exception e) {
	 * if (errorCache != null) {
	 * errorCache.put(loadItems);
	 * }
	 * distributeCache.removeAll(loadItems.keySet());
	 * loadItems.clear();
	 * logger.error("Loader commit Exception", e);
	 * throw e;
	 * } finally {
	 * 
	 * commitLapse = System.currentTimeMillis();
	 * // if(tm != null) tm.commit();
	 * }
	 * }
	 */

	Distributor distributor;

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	private void distribute(Map<String, Trace> traces) throws Exception {
		distributor.distribute(traces);
	}

	public void rollback() {

	}

	public void setThroughputMonitor(ThroughputMonitor tpm) {
		this.tpm = tpm;
	}

	public ThroughputMonitor getThroughputMonitor() {
		return tpm;
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
			thread.interrupt();
			// try {
			// thread.join();
			// } catch (InterruptedException e) {
			// logger.error("", e);
			// }
		}
	}

	public void stopAsap() {
		isShutdown = true;
		if (thread != null)
			thread.interrupt();

	}

	/**
	 * @deprecated since 202209
	 */
	public void runAsap() {

		logger.info(Util.join("start loader:[" + name + "]"));

		while (true) {

			try {
				if (thread.isInterrupted())
					break;

				if (loadItems.size() > 0 && (System.currentTimeMillis() - commitLapse >= maxCommitWait)) {
					commit();
				}

				Collection<Trace> values = distributeCache.values();
				if (values == null || values.size() == 0) {
					try {
						Thread.sleep(delayForNoMessage);
						continue;
					} catch (java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}

				String regDate = Util.getFormatedDate("yyyyMMddHHmmssSSS");
				for (Trace trace : values) {
					String key = trace.getId();
					trace.setRegDate(regDate);

					if (mergeCache.containsKey(key)) {
						// delete the trace loaded already.
						distributeCache.remove(key);
					}

					loadItems.put(key, trace);

					// expect to delte block comming soon.
					// if (oldKey != null && oldKey.equals(key))
					// dups.add(key);

					if (loadItems.size() > 0 && (loadItems.size() % commitCount == 0)) {

						try {
							commit();
							break;
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
								return;
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
		logger.info(Util.join("stop loader:[" + name + "]"));
	}

	public void runGracefully() {

		logger.info(Util.join("start loader:[" + name + "]"));

		while (Thread.currentThread() == thread && !isShutdown) {

			try {
				if (loadItems.size() > 0 && (System.currentTimeMillis() - commitLapse >= maxCommitWait)) {
					if (Variables.debugLineByLine)
						logger.debug(name + "-TLLBLD0100");
					commit();
					if (Variables.debugLineByLine)
						logger.debug(name + "-TLLBLD0199");
				}

				Collection<Trace> values = distributeCache.values();
				if (values == null || values.size() == 0) {
					try {
						Thread.sleep(delayForNoMessage);
						continue;
					} catch (java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}

				String regDate = Util.getFormatedDate("yyyyMMddHHmmssSSS");
				for (Trace trace : values) {
					String key = trace.getId();
					trace.setRegDate(regDate);

					if (mergeCache.containsKey(key)) {
						// delete the trace loaded already.
						distributeCache.remove(key);
					}

					loadItems.put(key, trace);

					// exepct to delete comming soon(current date : 202209)
					// if (oldKey != null && oldKey.equals(key)) dups.add(key);

					if (loadItems.size() > 0 && (loadItems.size() % commitCount == 0)) {

						try {
							if (Variables.debugLineByLine)
								logger.debug(name + "-TLLBLD0100");
							commit();
							if (Variables.debugLineByLine)
								logger.debug(name + "-TLLBLD0199");
							break;
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
								return;
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
		logger.info(Util.join("stop loader:[" + name + "]"));
	}

	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

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

}
