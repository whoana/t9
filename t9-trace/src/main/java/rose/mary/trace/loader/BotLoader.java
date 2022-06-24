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
import rose.mary.trace.core.data.common.Bot;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.envs.Variables;
import rose.mary.trace.core.exception.ExceptionHandler;
import rose.mary.trace.core.monitor.ThroughputMonitor;
import rose.mary.trace.database.service.BotService;

/**
 * <pre>
 * The BotLoader has a role to loading a {@link Bot Bot} message into the table TOP0503 
 * </pre>
 * @author whoana
 * @since Sep 19, 2019
 */
public class BotLoader implements Runnable{

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected final static int DEFAULT_COMMIT_COUNT = 1000;
	
	private boolean isShutdown = true;
	
	private BotService botService; 
	
	private Thread thread = null; 
	
	private ExceptionHandler exceptionHandler;
	
	private CacheProxy<String, State> botCache;
	
	private CacheProxy<String, State> errorCache;
	
	private ThroughputMonitor tpm;

	private int commitCount = DEFAULT_COMMIT_COUNT;
	 
	private long delayForNoMessage = 10;
	 
	private long exceptionDelay = 1;
	
	private Map<String, State> loadBots = new LinkedHashMap<String, State>(); 
	 
	
	private long commitLapse = System.currentTimeMillis();
	
	private long maxCommitWait = 1000;
 
	String name;
	
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
			CacheProxy<String, State> botCache,
			CacheProxy<String, State> errorCache, 
			BotService botService, 
			ThroughputMonitor tpm, 
			ExceptionHandler exceptionHandler) {
		this.name = name;
		this.commitCount = commitCount;
		this.botCache = botCache; 
		this.errorCache = errorCache;
		this.botService = botService;
		this.tpm = tpm;
		this.exceptionHandler = exceptionHandler;
		this.delayForNoMessage = delayForNoMessage;
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void commit() throws Exception{ 

		try {
			Collection<State> bots = loadBots.values();
			int count = loadBots.size(); 
			if(tpm != null) tpm.count(count); 
			botService.mergeBots(bots);
		}catch(Exception e) {
			if(errorCache != null) {
				errorCache.put(loadBots);
			}
			throw e;
		}finally {			
			botCache.removeAll(loadBots.keySet());        
			loadBots.clear(); 			
			commitLapse = System.currentTimeMillis();	 
		}
	}
	
	/**
	 * 
	 */
	public void rollback(){ 
		
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
		if(thread != null) stop();
		thread = new Thread(this, name); 
		isShutdown = false;
		thread.start();
	}
	
	public void stop() {
		if(Variables.startStopAsap) { 
			stopAsap();
		} else {
			stopGracefully();
		}
	}
	
	public void run() {
		if(Variables.startStopAsap) { 
			runAsap();
		} else {
			runGracefully();
		}
	}
	/**
	 * 
	 */
	public void stopAsap() {
		isShutdown = true;
		if(thread != null) thread.interrupt();
		
	} 
	
	
	public void stopGracefully() {
		isShutdown = true;
		if(thread != null) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				logger.error("",e);
			}
		}
	} 
	
	
	
	public void runAsap() {
		logger.info(Util.join("start botLoader:[" + name + "]"));
		while(true) { 
			try {
				
				if(thread.isInterrupted()) break;
				
				if(loadBots.size() > 0 && (System.currentTimeMillis() - commitLapse >= maxCommitWait)) {
					commit(); 
				}
				
				Set<String> keys = botCache.keys();
				if(keys == null || keys.size() == 0) {
					try {
						Thread.sleep(delayForNoMessage); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}
				
				for(String key : keys) {
					State state = botCache.get(key);
					addBatch(key, state); 
					if(loadBots.size() > 0 && (loadBots.size() % commitCount == 0 )) {
						try {
							commit();
							break;
						} catch (Exception e) {
							if(exceptionHandler != null) {
								exceptionHandler.handle("", e);
							}else {
								logger.error("", e);
							}
							break;
						} 
					}
				}
				
				
			} catch (Exception e) { 
				if(exceptionHandler != null) {
					exceptionHandler.handle("", e);
				}else {
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
			if(exceptionHandler != null) {
				exceptionHandler.handle("", e);
			}else {
				logger.error("", e);
			}
		} 
		 
		isShutdown = true; 
		logger.info(Util.join("stop botLoader:[" + name + "]"));
	}
	
	
	/**
	 * 
	 */
	public void runGracefully() {
		logger.info(Util.join("start botLoader:[" + name + "]"));
		while(Thread.currentThread() == thread && !isShutdown) { 
			try {
				if(loadBots.size() > 0 && (System.currentTimeMillis() - commitLapse >= maxCommitWait)) {
					commit(); 
				}
				
				Set<String> keys = null;
				if(botCache.isAccessable()) {
					keys = botCache.keys();
				}
				if(keys == null || keys.size() == 0) {
					try {
						Thread.sleep(delayForNoMessage); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}
				
				for(String key : keys) {
					State state = botCache.get(key);
					addBatch(key, state); 
					if(loadBots.size() > 0 && (loadBots.size() % commitCount == 0 )) {
						try {
							commit();
							break;
						} catch (Exception e) {
							if(exceptionHandler != null) {
								exceptionHandler.handle("", e);
							}else {
								logger.error("", e);
							}
							break;
						} 
					}
				}
				
				
			} catch (Exception e) { 
				if(exceptionHandler != null) {
					exceptionHandler.handle("", e);
				}else {
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
			if(exceptionHandler != null) {
				exceptionHandler.handle("", e);
			}else {
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
	 
}
