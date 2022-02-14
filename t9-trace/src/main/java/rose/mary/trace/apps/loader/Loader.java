/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.loader;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



//import javax.transaction.TransactionManager;

import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

import pep.per.mint.common.util.Util;
import rose.mary.trace.apps.cache.CacheProxy;
import rose.mary.trace.apps.envs.Variables;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.database.service.TraceService;
import rose.mary.trace.exception.ExceptionHandler;
import rose.mary.trace.monitor.ThroughputMonitor; 

/**
 * <pre>
 * rose.mary.trace.database
 * Loader.java 
 * </pre>
 * @author whoana
 * @date Aug 26, 2019
 */
public class Loader implements Runnable{
	
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
	
	public Loader(String name, int commitCount, long delayForNoMessage, boolean loadError, boolean loadContents, CacheProxy<String, Trace> distributeCache, CacheProxy<String, Trace> mergeCache, CacheProxy<String, Trace> errorCache, TraceService traceLoadService, ThroughputMonitor tpm, ExceptionHandler exceptionHandler) {
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
	 
	public void commit() throws Exception{ 
		try { 
			Collection<Trace> col = loadItems.values();
			traceLoadService.load(col, loadError, loadContents);
			
			
			if(tpm != null) tpm.count(loadItems.size()); 
			mergeCache.put(loadItems);					
			distributeCache.removeAll(loadItems.keySet());
			loadItems.clear();			
			
	 
		}catch(PersistenceException e) { 
			if(errorCache != null) errorCache.put(loadItems);		
			distributeCache.removeAll(loadItems.keySet());
			loadItems.clear();
			logger.debug("\n여기는 Loader commit PersistenceException\n");
			throw e;
		}catch(BatchUpdateException e) { 
			if(errorCache != null) errorCache.put(loadItems);		
			distributeCache.removeAll(loadItems.keySet());
			loadItems.clear();
			logger.debug("\n여기는 Loader commit BatchUpdateException\n");
			throw e;
		}catch(SQLException e) { 
			if(errorCache != null) errorCache.put(loadItems);		
			distributeCache.removeAll(loadItems.keySet());
			loadItems.clear();
			logger.debug("\n여기는 Loader commit SQLException\n");
			throw e;
		}catch(Exception e) { 
			
			if(errorCache != null) errorCache.put(loadItems);
			distributeCache.removeAll(loadItems.keySet());
			loadItems.clear();
			logger.debug("\n여기는 Loader commit Exception\n");
			throw e;
		}finally {
			commitLapse = System.currentTimeMillis();	
			//if(tm != null) tm.commit();
		}
	}
	
 
	public void rollback(){
		 
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
		if(thread != null) thread.interrupt();
		
	} 
	

	public void runAsap() {
		
		logger.info(Util.join("start loader:[" + name + "]"));
		
		while(true) { 

			try {
				if(thread.isInterrupted())break;
				
				if(loadItems.size() > 0 && (System.currentTimeMillis() - commitLapse >= maxCommitWait)) {
					commit(); 
				}
				 
				Collection<Trace> values = distributeCache.values();
				if(values == null || values.size() == 0)  {
					try {
						Thread.sleep(delayForNoMessage); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}
				 
				
				
				String regDate = Util.getFormatedDate("yyyyMMddHHmmssSSS");
				for (Trace trace : values) {
					String key = trace.getId(); 
					trace.setRegDate(regDate);
					
					if(mergeCache.containsKey(key)) {
						//delete the trace loaded already.
						distributeCache.remove(key);
					}
					
					loadItems.put(key, trace);
					
					if(oldKey != null && oldKey.equals(key)) dups.add(key);
					
					if(loadItems.size() > 0 && (loadItems.size() % commitCount == 0 )) {
						
						try {
							commit();
							break;
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
								return;
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
		logger.info(Util.join("stop loader:[" + name + "]"));
	}

	
	
	public void runGracefully() {
		
		logger.info(Util.join("start loader:[" + name + "]"));
		
		while(Thread.currentThread() == thread && !isShutdown) { 

			try {
				if(loadItems.size() > 0 && (System.currentTimeMillis() - commitLapse >= maxCommitWait)) {
					commit(); 
				}
				 
				Collection<Trace> values = distributeCache.values();
				if(values == null || values.size() == 0)  {
					try {
						Thread.sleep(delayForNoMessage); 
						continue;
					}catch(java.lang.InterruptedException ie) {
						isShutdown = true;
						break;
					}
				}
				 
				
				
				String regDate = Util.getFormatedDate("yyyyMMddHHmmssSSS");
				for (Trace trace : values) {
					String key = trace.getId(); 
					trace.setRegDate(regDate);
					
					if(mergeCache.containsKey(key)) {
						//delete the trace loaded already.
						distributeCache.remove(key);
					}
					
					loadItems.put(key, trace);
					
					if(oldKey != null && oldKey.equals(key)) dups.add(key);
					
					if(loadItems.size() > 0 && (loadItems.size() % commitCount == 0 )) {
						
						try {
							commit();
							break;
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
								return;
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
