package rose.mary.trace.handler;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.envs.Variables;
import rose.mary.trace.data.common.InterfaceInfo;

import rose.mary.trace.data.common.Unmatch;
import rose.mary.trace.database.service.BotService;


public class UnmatchHandler implements Runnable{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	CacheProxy<String, Unmatch> unmatchCache;
	CacheProxy<String, InterfaceInfo> interfaceCache;
	
	MessageSource messageResource;
	
	private long delayForNoMessage = 10;
	
	private boolean isShutdown = true;
	
	private long exceptionDelay = 500;	
	
	private long delayForDoChecking = 1000;
	
	Thread thread;
	
	BotService botService;
	
	String name;
	
	public UnmatchHandler(
		String name,
		CacheProxy<String, Unmatch> unmatchCache,
		CacheProxy<String, InterfaceInfo> interfaceCache,
		BotService botService,
		MessageSource messageResource,
		long delayForNoMessage,
		long delayForDoChecking,
		long exceptionDelay
	) {
		this.name = name;
		this.unmatchCache = unmatchCache;
		this.interfaceCache = interfaceCache;
		this.botService = botService;
		this.messageResource = messageResource;
		this.delayForNoMessage = delayForNoMessage;
		this.delayForDoChecking = delayForDoChecking;
		this.exceptionDelay = exceptionDelay;
	}
	
	public void start() throws Exception {
		if (thread != null)
			stop();
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
		if (thread != null) thread.interrupt();
	}

	long beforeCheckEndTime = System.currentTimeMillis();
	 
	public void runAsap() {
		logger.info(Util.join("start UnmatchHandler:[" + name + "]"));
		
		while (true) {

			try {
				if(thread.isInterrupted()) break;
				
				Collection<Unmatch> values = unmatchCache.values();
				if (values == null || values.size() == 0) {
					try {
						Thread.sleep(delayForNoMessage);
						continue;
					} catch (InterruptedException e1) {
						isShutdown = true;
						break;
					}
				}
				
				boolean doCheck = System.currentTimeMillis() - beforeCheckEndTime >= delayForDoChecking;
				if(doCheck) {
					botService.updateUnmatch(unmatchCache);
					beforeCheckEndTime = System.currentTimeMillis();
				}else {
					try {
						logger.info("ItsNotTimeForCheckUnmatchYet.");
						Thread.sleep(delayForDoChecking); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}
			}catch (Exception e) {
				logger.error("", e);

				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}

			}
		}
		
		isShutdown = true;
		logger.info(Util.join("stop UnmatchHandler:[" + name + "]"));

	}
	 
	public void runGracefully() {
		logger.info(Util.join("start UnmatchHandler:[" + name + "]"));
		
		while (Thread.currentThread() == thread && !isShutdown) {

			try {
				
				Collection<Unmatch> values = unmatchCache.values();
				if (values == null || values.size() == 0) {
					try {
						Thread.sleep(delayForNoMessage);
						continue;
					} catch (InterruptedException e1) {
						isShutdown = true;
						break;
					}
				}
				
				boolean doCheck = System.currentTimeMillis() - beforeCheckEndTime >= delayForDoChecking;
				if(doCheck) {
					botService.updateUnmatch(unmatchCache);
					beforeCheckEndTime = System.currentTimeMillis();
				}else {
					try {
						logger.info("ItsNotTimeForCheckUnmatchYet.");
						Thread.sleep(delayForDoChecking); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}
			}catch (Exception e) {
				logger.error("", e);

				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}

			}
		}
		
		isShutdown = true;
		logger.info(Util.join("stop UnmatchHandler:[" + name + "]"));

	}
}
