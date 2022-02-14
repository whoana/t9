/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.boter;
 
import java.util.Collection;
import java.util.List;

import org.jgroups.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pep.per.mint.common.util.Util;
import rose.mary.trace.apps.cache.CacheProxy;
import rose.mary.trace.apps.envs.Variables;
import rose.mary.trace.apps.manager.config.BoterManagerConfig;
import rose.mary.trace.data.common.Bot;
import rose.mary.trace.data.common.InterfaceInfo;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace; 
import rose.mary.trace.exception.ExceptionHandler;
import rose.mary.trace.monitor.ThroughputMonitor;
import rose.mary.trace.util.IntCounter;

/**
 * <pre>
 * The Boter creates and updates {@link Bot Bot}.
 * </pre>
 * @author whoana
 * @date Sep 18, 2019
 */
public class Boter implements Runnable{

	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	final static int EXISTS_CHECK_CACHE = 0;
	final static int EXISTS_CHECK_DB    = 2;
	
	final static String NOT_MATCH_NM = "unregistered"; 
	
	protected final static int DEFAULT_COMMIT_COUNT = 1000;
	
	private boolean isShutdown = true; 
	
	private Thread thread = null; 
	
	private ExceptionHandler exceptionHandler;
	 
	private CacheProxy<String, Trace> mergeCache;
	
	private CacheProxy<String, State> errorCache;
	
	private CacheProxy<String, Trace> backupCache;
	
	private CacheProxy<String, State> finCache;
	 
	
	//private int existsCheckType = EXISTS_CHECK_CACHE; // 0:CacheProxy<String, State> stateCache,  1 : select from database
	
	List<CacheProxy<String, State>> botCaches;
	
	CacheProxy<String, InterfaceInfo> interfaceCache;
	
	CacheProxy<String, Integer> routingCache;
	
	private ThroughputMonitor tpm; 
	 
	private long delayForNoMessage = 10;
	 
	private long exceptionDelay = 5000;  
	
	private int maxRoutingCacheSize = 10000;
	
	IntCounter counter;

	BoterManagerConfig config;
	
	String name;
	
	public Boter( 
		BoterManagerConfig config,
		 
		CacheProxy<String, Trace> mergeCache, 
		CacheProxy<String, Trace> backupCache,
		List<CacheProxy<String, State>> botCaches, 
		CacheProxy<String, State> finCache, 
		CacheProxy<String, InterfaceInfo> interfaceCache,
		CacheProxy<String, State> errorCache,  
		CacheProxy<String, Integer> routingCache,
		ThroughputMonitor tpm, 
		ExceptionHandler exceptionHandler
	) { 
		this.name = config.getName();
		this.config = config;
		this.mergeCache = mergeCache;
		this.backupCache = backupCache;
		this.botCaches = botCaches;
		this.finCache  = finCache;
		this.interfaceCache = interfaceCache;
		this.errorCache = errorCache;
		this.routingCache = routingCache;
		this.tpm = tpm; 
		this.exceptionHandler = exceptionHandler;
		this.delayForNoMessage = config.getDelayForNoMessage();
		this.maxRoutingCacheSize = config.getMaxRoutingCacheSize();
		counter= new IntCounter(0, botCaches.size() - 1, 1);
	}
	
	public void setThroughputMonitor(ThroughputMonitor tpm) {
		this.tpm = tpm;
	}
	
	public ThroughputMonitor getThroughputMonitor() {
		return tpm;
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
		if(thread != null) {
				thread.interrupt();
		}
	}
	

	public void runAsap() {
		
		logger.info(Util.join("start boter:[" + name + "]"));
		
		while(true) { 

			try {
				
				if(thread.isInterrupted()) {
					break;
				}
				
				Collection<Trace> values = mergeCache.values();
				if(values == null || values.size() == 0){
					try {
						Thread.sleep(delayForNoMessage); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}
				
				for(Trace trace: values) {
					if(trace == null) continue;
					String key  = trace.getId();
					try {
						String botId = Util.join(trace.getIntegrationId(),"@", trace.getDate(), "@", trace.getOriginHostId());
 						State state = finCache.get(botId);
 						boolean first = false;
						if(state == null) {
							long currentDate = System.currentTimeMillis();
							state = new State();
							state.setCreateDate(currentDate);	
							state.setBotId(botId);
							first = true;													 
						}
						
						trace.getStateCheckHandler().checkAndSet(first, trace, state);	
						
						if(!state.skip()) {
							finCache.put(botId, state); 
							routeToBotCache(botId, state);
						}
						
					}catch(Exception e) {
						if(exceptionHandler != null) {
							exceptionHandler.handle("", e);
						}else {
							logger.error("", e);
						}
					}finally {
						//mergeCache에서 빼내야 다음걸 처리한다.
						mergeCache.remove(key);
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
		logger.info(Util.join("stop boter:[" + name + "]"));
		 
	}
 
	
	
	public void runGracefully() {
		
		logger.info(Util.join("start boter:[" + name + "]"));
		
		while(Thread.currentThread() == thread && !isShutdown) { 

			try {
				
				
				
				Collection<Trace> values = mergeCache.values();
				if(values == null || values.size() == 0){
					try {
						Thread.sleep(delayForNoMessage); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}
				
				for(Trace trace: values) {
					if(trace == null) continue;
					String key  = trace.getId();
					try {
						String botId = Util.join(trace.getIntegrationId(),"@", trace.getDate(), "@", trace.getOriginHostId());
 						State state = finCache.get(botId);
 						boolean first = false;
						if(state == null) {
							long currentDate = System.currentTimeMillis();
							state = new State();
							state.setCreateDate(currentDate);	
							state.setBotId(botId);
							first = true;													 
						}
						
						trace.getStateCheckHandler().checkAndSet(first, trace, state);	
						
						if(!state.skip()) {
							finCache.put(botId, state); 
							routeToBotCache(botId, state);
						}
						
					}catch(Exception e) {
						if(exceptionHandler != null) {
							exceptionHandler.handle("", e);
						}else {
							logger.error("", e);
						}
					}finally {
						//mergeCache에서 빼내야 다음걸 처리한다.
						mergeCache.remove(key);
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
		logger.info(Util.join("stop boter:[" + name + "]"));
		 
	}
 
	private void routeToBotCache(String id, State state) throws Exception {
		Integer index = getBotCacheIndex(id);
		
		CacheProxy<String, State> botCache = botCaches.get(index);
		
		String uniqId = UUID.randomUUID().toString();
		
		botCache.put(uniqId, state);
		
		logger.debug("put botCache["+index+"][" + state.getBotId() + ", status:" + state.getStatus() + ", created:"+state.getCreateDate()+"]:" + Util.toJSONString(state));
	}
	 
	  
	private synchronized Integer getBotCacheIndex(String id) throws Exception {
		Integer index = routingCache.get(id);		
		if(index == null) {			 
			index = counter.getAndIncrease();
			routingCache.put(id, index);
		}
		//if(routingCache.size() >= maxRoutingCacheSize)  routingCache.clear();
		return index;
	}
 
	
	public ExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

 
	public void setExceptionHandler(ExceptionHandler loaderExceptionHandler) {
		this.exceptionHandler = loaderExceptionHandler;
	}
 
	
}
