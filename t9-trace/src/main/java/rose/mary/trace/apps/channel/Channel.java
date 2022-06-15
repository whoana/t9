/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pep.per.mint.common.util.Util;
import rose.mary.trace.apps.cache.CacheProxy;
import rose.mary.trace.apps.envs.Variables;
import rose.mary.trace.apps.handler.StateCheckHandler;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.exception.HaveNoTraceInfoException;
import rose.mary.trace.exception.NoMoreMessageException;
import rose.mary.trace.exception.ZeroLengthMessageException;
import rose.mary.trace.monitor.ThroughputMonitor;
import rose.mary.trace.parser.Parser;

/**
 * <pre>
 * Channel
 * 다양한 채널로부터 트레킹 메시지를 받아들이기 위해 디자인된 최상위 채널클래스
 * </pre>
 * 
 * @author whoana
 * @since Jul 29, 2019
 */
public abstract class Channel implements Runnable {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final int TYPE_WMQ = 0;
	public static final int TYPE_ILINK = 1;
	public static final int TYPE_REST = 2;
	public static final int TYPE_TCP = 3;
	public static final int TYPE_DB = 4;
	public static final int TYPE_FILE = 5;
	public static final int TYPE_TEST = 6;

	protected final static int DEFAULT_COMMIT_COUNT = 1000;

	protected boolean isShutdown = true;

	Thread thread = null;

	protected StateCheckHandler sch;

	protected ChannelExceptionHandler channelExceptionHandler;

	protected CacheProxy<String, Trace> cache;

	int maxCacheSize = 100;

	long delayForMaxCache = 1000;// cache 유량 제어

	protected ThroughputMonitor tpm;

	int commitCount = DEFAULT_COMMIT_COUNT;

	protected boolean autoCommit = false;

	protected Parser parser = null;

	String name;

	ChannelWriter channelWriter;

	protected boolean healthCheck = false;

	long delayOnException = 5000;
	long delayForNoMessage = 1000;
	long commitLapse = System.currentTimeMillis();
	long maxCommitWait = 100;

	int totalCommitCount = 0;

	int waitForInitialize = 5000;

	public Channel(String name, boolean autoCommit, int commitCount, long maxCommitWait, long delayForNoMessage,
			long delayOnException, int maxCacheSize, long delayForMaxCache, CacheProxy<String, Trace> cache) {
		this.name = name;
		this.commitCount = commitCount;
		this.autoCommit = autoCommit;
		this.maxCommitWait = maxCommitWait;
		this.maxCacheSize = maxCacheSize;
		this.delayForMaxCache = delayForMaxCache;
		this.delayForNoMessage = delayForNoMessage;
		this.delayOnException = delayOnException;
		this.cache = cache;
		// if(!autoCommit) channelWriter = new ChannelWriter(this);
	}

	protected abstract Object trace() throws Exception;

	protected abstract void initialize() throws Exception;

	protected abstract void commit() throws Exception;

	protected abstract void rollback() throws Exception;

	public abstract boolean ping();

	public static final int STATE_INIT = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_SHUTDOWN = 2;

	int state = STATE_SHUTDOWN;

	public int getState() {
		return state;
	}

	public void setThroughputMonitor(ThroughputMonitor tpm) {
		this.tpm = tpm;
	}

	public ThroughputMonitor getThroughputMonitor() {
		return tpm;
	}

	public void start() throws Exception {
		if (thread != null && thread.isAlive())
			stop();
		thread = new Thread(this, name);

		state = STATE_INIT;
		while (true) {
			try {
				logger.info(Util.join("initailizing channel:", name));
				initialize();
				logger.info(Util.join("finish initailizing channel:", name));
				break;
			} catch (Exception e) {
				logger.error("fail to initailize channel:", name, " error:", e);
				try {
					Thread.sleep(delayOnException);
				} catch (InterruptedException ie) {
					logger.error("", ie);
				}
			}
		}

		logger.info(Util.join("success to initailize channel:", name));
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

	public void runAsap() {

		isShutdown = false;
		logger.info(Util.join("start channel:", name));
		state = STATE_RUNNING;

		while (true) {
			try {

				if (thread.isInterrupted())
					break;

				if ((cacheMap.size() > 0 && (cacheMap.size() % commitCount == 0
						|| (System.currentTimeMillis() - commitLapse >= maxCommitWait)))) {
					try {
						commit();
						totalCommitCount = totalCommitCount + cacheMap.size();

					} finally {
						commitLapse = System.currentTimeMillis();
					}
				}

				if (cache.size() >= maxCacheSize) {

					try {
						Thread.sleep(delayForMaxCache);
					} catch (InterruptedException e) {
						isShutdown = true;
						break;
					}

					continue;
				}

				Object msg = trace();
				if (msg != null) {
					try {
						Trace trace = parser.parse(msg);
						trace.setStateCheckHandler(sch);
						cacheMap.put(trace.getId(), trace);
					} catch (ZeroLengthMessageException ze){
						//to-do : handle NoMoreMessageException
						//2022.07
						//MQ 채널이 가끔 이상해질때 사이즈 0인 메시지가 발생하는지 
						//parser.parse(msg) 에서 
						//if(msg.getTotalMessageLength() == 0) 인 경우가 발생된다. 
						//다음에 이경우가 발생하면 큐에 제대로 된 메시지를 넣어보자. 
						//아무래도 이건 MQ 버그인듯 싶다.

						logger.info("Length of message is 0, continue to get next message after waitting delay time : " + delayOnException);
						try {
							Thread.sleep(delayOnException);
						} catch (InterruptedException e1) {
							
						}
						continue;

					} catch (Exception me) {
						// 메시지 파싱시 예외난 것들은 롤백 처리하지 않고 로그만 남기도록 한다.
						// handler에서는 메시지 원본을 어떻게 처리할지 고민해 본다.
						if (channelExceptionHandler != null) {
							channelExceptionHandler.handleParserException("parser exception:", me, msg);
						} else {
							msg.toString();
							logger.error("parser exception:", me);
						}
						try {
							Thread.sleep(delayOnException);
						} catch (InterruptedException e1) {
							isShutdown = true;
							break;
						}
					} finally {
						if (tpm != null)
							tpm.count();
					}
				}

			} catch (NoMoreMessageException e) {
				try {
					Thread.sleep(delayForNoMessage);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}
			} catch (HaveNoTraceInfoException e) {
				byte[] data = e.getData();
				// 에러 캐시에 따로 보관할지 옵션처리해주자
				// errorMessageCache.put(UUID.randomUUID(),d);
				if (data != null) {
					logger.info("The data (having no header):(len: " + data.length + ")" + new String(data));
				}
				try {
					Thread.sleep(delayForNoMessage);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}

			} catch (Exception e) {

				try {
					rollback();
				} catch (Exception e2) {
					logger.error("", e2);
				}

				if (channelExceptionHandler != null) {
					channelExceptionHandler.handleException("", e);
				} else {
					logger.error("", e);
				}

				try {
					Thread.sleep(delayOnException);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}

			}
		}

		isShutdown = true;
		state = STATE_SHUTDOWN;
		logger.info(Util.join("stop channel:", name));
		logger.info(Util.join("totalCommitCount:", totalCommitCount));

	}

	public void runGracefully() {
		isShutdown = false;
		logger.info(Util.join("start channel:", name));
		state = STATE_RUNNING;

		while (Thread.currentThread() == thread && !isShutdown) {
			try {

				if ((cacheMap.size() > 0 && (cacheMap.size() % commitCount == 0
						|| (System.currentTimeMillis() - commitLapse >= maxCommitWait)))) {
					try {
						commit();
						totalCommitCount = totalCommitCount + cacheMap.size();

					} finally {
						commitLapse = System.currentTimeMillis();
					}
				}

				if (cache.size() >= maxCacheSize) {

					try {
						Thread.sleep(delayForMaxCache);
					} catch (InterruptedException e) {
						isShutdown = true;
						// return;
						break;
					}

					continue;
				}

				Object msg = trace();
				if (msg != null) {
					try {
						Trace trace = parser.parse(msg);
						trace.setStateCheckHandler(sch);
						cacheMap.put(trace.getId(), trace);
					} catch (ZeroLengthMessageException ze){
						//to-do : handle NoMoreMessageException
						//2022.07
						//MQ 채널이 가끔 이상해질때 사이즈 0인 메시지가 발생하는지 
						//parser.parse(msg) 에서 
						//if(msg.getTotalMessageLength() == 0) 인 경우가 발생된다. 
						//다음에 이경우가 발생하면 큐에 제대로 된 메시지를 넣어보자. 
						//아무래도 이건 MQ 버그인듯 싶다.

						logger.info("Length of message is 0, continue to get next message after waitting delay time : " + delayOnException);
						try {
							Thread.sleep(delayOnException);
						} catch (InterruptedException e1) {
							
						}
						continue;
					} catch (Exception me) {
						// 메시지 파싱시 예외난 것들은 롤백 처리하지 않고 로그만 남기도록 한다.
						// handler에서는 메시지 원본을 어떻게 처리할지 고민해 본다.
						if (channelExceptionHandler != null) {
							channelExceptionHandler.handleParserException("parser exception:", me, msg);
						} else {
							msg.toString();
							logger.error("parser exception:", me);
						}
						try {
							Thread.sleep(delayOnException);
						} catch (InterruptedException e1) {
							isShutdown = true;
							// return;
							break;
						}
					} finally {
						if (tpm != null)
							tpm.count();
					}
				}

			} catch (NoMoreMessageException e) {
				try {
					Thread.sleep(delayForNoMessage);
				} catch (InterruptedException e1) {
					isShutdown = true;
					// return;
					break;
				}
			} catch (HaveNoTraceInfoException e) {
				byte[] data = e.getData();
				// 에러 캐시에 따로 보관할지 옵션처리해주자
				// errorMessageCache.put(UUID.randomUUID(),d);
				if (data != null) {
					logger.info("The data (having no header):(len: " + data.length + ")" + new String(data));
				}
				try {
					Thread.sleep(delayForNoMessage);
				} catch (InterruptedException e1) {
					isShutdown = true;
					// return;
					break;
				}

			} catch (Exception e) {

				try {
					rollback();
				} catch (Exception e2) {
					logger.error("", e2);
				}

				if (channelExceptionHandler != null) {
					channelExceptionHandler.handleException("", e);
				} else {
					logger.error("", e);
				}

				try {
					Thread.sleep(delayOnException);
				} catch (InterruptedException e1) {
					isShutdown = true;
					// return;
					break;
				}

			}
		}

		isShutdown = true;
		state = STATE_SHUTDOWN;
		logger.info(Util.join("stop channel:", name));
		logger.info(Util.join("totalCommitCount:", totalCommitCount));

	}

	protected Map<String, Trace> cacheMap = new ConcurrentHashMap<String, Trace>();

	public int uncommitedSize() {
		return cacheMap.size();
	}

	public Map<String, Trace> uncommitedMap() {
		return cacheMap;
	}

	/**
	 * @return the channelExceptionHandler
	 */
	public ChannelExceptionHandler getChannelExceptionHandler() {
		return channelExceptionHandler;
	}

	/**
	 * @param channelExceptionHandler the channelExceptionHandler to set
	 */
	public void setChannelExceptionHandler(ChannelExceptionHandler channelExceptionHandler) {
		this.channelExceptionHandler = channelExceptionHandler;
	}

	/**
	 * @return the cacheProxy
	 */
	public CacheProxy<String, Trace> getCache() {
		return cache;
	}

	/**
	 * @return the parser
	 */
	public Parser getParser() {
		return parser;
	}

	/**
	 * @param parser the parser to set
	 */
	public void setParser(Parser parser) {
		this.parser = parser;
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

	/**
	 * @return the autoCommit
	 */
	public boolean isAutoCommit() {
		return autoCommit;
	}

	/**
	 * @param autoCommit the autoCommit to set
	 */
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public long getMaxCommitWait() {
		return maxCommitWait;
	}

	public long getDelayForNoMessage() {
		return delayForNoMessage;
	}

	public void setHealthCheck(boolean healthCheck) {
		this.healthCheck = healthCheck;
	}

	public boolean isHealthCheck() {
		return healthCheck;
	}

	public boolean isShutdown() {
		return isShutdown;
	}

}
