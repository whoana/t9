/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.finisher;


import java.util.Collection;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.config.FinisherManagerConfig;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.envs.Variables;
import rose.mary.trace.core.exception.ExceptionHandler;
import rose.mary.trace.core.finisher.plugin.Cleaner;
import rose.mary.trace.core.finisher.plugin.DefaultCleaner;
import rose.mary.trace.core.finisher.plugin.NoWaitingCleaner; 

/**
 * <pre>
 * rose.mary.trace.apps.finisher
 * Finisher.java
 * </pre>
 * @author whoana
 * @date Oct 2, 2019
 */
public class Finisher implements Runnable{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	 
	private boolean isShutdown = true;
	 
	private Thread thread = null; 
	
	private ExceptionHandler exceptionHandler;
	
	private CacheProxy<String, Trace> backupCache;
	
	private CacheProxy<String, State> finCache;
 	
	private CacheProxy<String, Integer> routingCache;
	
	private long delayForNoMessage = 60*1000; //1 minute
	 
	private long exceptionDelay = 5000;
	
	private long delayForDoCleaning = 1000;
 
	int waitForCleaning = 60 * 60 * 1000; //60분 
	boolean useWaitForCleaning = true;
	int waitForFinishedCleaning	= 10 * 60 * 1000; //10분 
	
	FinisherManagerConfig config;
	
	Cleaner cleaner; 

	String name;
	
	public Finisher(
			FinisherManagerConfig config,
			CacheProxy<String, Trace> backupCache, 
			CacheProxy<String, State> finCache, 
			CacheProxy<String, Integer> routingCache,
			ExceptionHandler exceptionHandler
	) {
		this.name = config.getName();
		this.backupCache = backupCache;
		this.finCache = finCache; 
		this.routingCache = routingCache;
		this.config = config;
		this.exceptionHandler = exceptionHandler;
		this.delayForNoMessage = config.getDelayForNoMessage();
		this.waitForCleaning = config.getWaitForCleaningSec() * 1000;
		this.delayForDoCleaning = config.getDelayForDoCleaning();
		this.useWaitForCleaning = config.isUseWaitForCleaning();
		this.waitForFinishedCleaning = config.getWaitForFinishedCleaningSec() * 1000;
		cleaner = useWaitForCleaning ? new DefaultCleaner(waitForCleaning, waitForFinishedCleaning, backupCache, finCache, routingCache) : new NoWaitingCleaner(backupCache, finCache, routingCache);
		
	}
	
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
	
	@Override
	public void run() {
		if(Variables.startStopAsap) { 
			runAsap();
		} else {
			runGracefully();
		}
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
	
	public void stopAsap() {
		isShutdown = true;
		if(thread != null) thread.interrupt();
	} 
	
	long beforeCleaningEndTime = System.currentTimeMillis();
	 
	
	public void runAsap() {
		logger.info(Util.join("start finisher:[" + name + "]"));
		
		while(true) { 
			try {
				
				if(thread.isInterrupted()) break;
				
				Collection<State> values = finCache.values();
				if(values == null || values.size() == 0)  {
					try {
						Thread.sleep(delayForNoMessage); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}
					
				//boolean doCleaning = System.currentTimeMillis() - beforeCleaningEndTime >= waitForCleaning;
				boolean doCleaning = System.currentTimeMillis() - beforeCleaningEndTime >= delayForDoCleaning;
				if(doCleaning) {
					try {
						
						logger.info("ItsTimeForCleaning.");

						long currentTime = System.currentTimeMillis();
						for (State state : values) {
							cleaner.clean(currentTime, state);	
						}
						
						beforeCleaningEndTime = System.currentTimeMillis();
						
						logger.info("success cleaning...");
						
					}catch(Exception e) {
						throw e;
					}finally {
						
					}
				}else {
					try {
						logger.info("ItsNotTimeForCleaningYet.");
						Thread.sleep(delayForDoCleaning); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
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
	 
	  
		isShutdown = true; 
		logger.info(Util.join("stop finisher:[" + name + "]"));
		
	} 
	
	
	
	public void runGracefully() {
		logger.info(Util.join("start finisher:[" + name + "]"));
		
		while(Thread.currentThread() == thread && !isShutdown) { 
			try {
				Collection<State> values = finCache.values();
				if(values == null || values.size() == 0)  {
					try {
						Thread.sleep(delayForNoMessage); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}
					
				//boolean doCleaning = System.currentTimeMillis() - beforeCleaningEndTime >= waitForCleaning;
				boolean doCleaning = System.currentTimeMillis() - beforeCleaningEndTime >= delayForDoCleaning;
				if(doCleaning) {
					try {
						
						logger.info("ItsTimeForCleaning.");

						long currentTime = System.currentTimeMillis();
						for (State state : values) {
							cleaner.clean(currentTime, state);	
						}
						
						beforeCleaningEndTime = System.currentTimeMillis();
						
						logger.info("success cleaning...");
						
					}catch(Exception e) {
						throw e;
					}finally {
						
					}
				}else {
					try {
						logger.info("ItsNotTimeForCleaningYet.");
						Thread.sleep(delayForDoCleaning); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
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
	 
	  
		isShutdown = true; 
		logger.info(Util.join("stop finisher:[" + name + "]"));
		
	} 
	
	
	 
}
	