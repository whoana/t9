/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.handler;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;


import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.envs.Variables;
import rose.mary.trace.database.service.TraceService;
import rose.mary.trace.system.SystemLogger;

/**
 * <pre>
 * Trace 로딩시 에러난 건을 처리한다.
 * 에러처리는 1건씩 처리하도록 한다.
 * 설정된 maxRetry 횟수 만큼 재시도한다. 
 * 처리할 수 없는 에러는 기록으로 남긴다.
 * 처리 성공한 건은 에러캐시에서 삭제한다.
 * 
 * TraceErrorHandler.java
 * </pre>
 * 
 * @author whoana
 * since Oct 8, 2019
 */
public class TraceErrorHandler implements Runnable {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	CacheProxy<String, Trace> errorCache;
	
	CacheProxy<String, Trace> mergeCache;
	
	MessageSource messageResource;
	
	private long delayForNoMessage = 10;
	
	private boolean isShutdown = true;
	
	private long exceptionDelay = 1;	 

	TraceService traceService;

	Thread thread;

	int maxRetry = 1;
	
	String name;
	
	public TraceErrorHandler(
			String name,
			CacheProxy<String, Trace> errorCache, 
			CacheProxy<String, Trace> mergeCache,
			MessageSource messageResource, 
			TraceService traceService, 
			long delayForNoMessage, 
			int maxRetry, 
			long exceptionDelay) {
		this.name = name;
		this.errorCache = errorCache;
		this.mergeCache = mergeCache;
		this.messageResource = messageResource;
		this.traceService = traceService;
		this.delayForNoMessage = delayForNoMessage;
		this.maxRetry = maxRetry;
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
	  
	public void runAsap() {

		logger.info(Util.join("start TraceErrorHandler:[" + name + "]"));
		while (true) {

			try {
				
				if(thread.isInterrupted()) break;
				
				Set<String> keys = errorCache.keys();
 
				if (keys == null || keys.size() == 0) {

					try {
						Thread.sleep(delayForNoMessage);
					} catch (InterruptedException e1) {
						isShutdown = true;
						break;
					}
					continue;
				}
 				
				 
				logger.info("The count to retry:" + keys.size());
				SystemLogger.info("count to retry:" + keys.size());
				 
				for(String key : keys) {
					Trace trace = errorCache.get(key);
					try {
						retry(trace);
					}catch(Exception e) {
						logger.error("TraceErrorHandler retry exception:", e);
						try {
							Thread.sleep(exceptionDelay);
						} catch (InterruptedException ie) {
							isShutdown = true;
							break;
						}
					}
				}
				 
				 
			} catch (Throwable t) {
				logger.error("TraceErrorHandler exception:", t);
				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException ie) {
					isShutdown = true;
					break;
				}
			}

		}

		isShutdown = true;
		logger.info(Util.join("stop TraceErrorHandler:[" + name + "]"));

	}
	
	public void runGracefully() {

		logger.info(Util.join("start TraceErrorHandler:[" + name + "]"));
		while(Thread.currentThread() == thread && !isShutdown) { 

			try {
				
				 
				
				Set<String> keys = errorCache.keys();
 
				if (keys == null || keys.size() == 0) {

					try {
						Thread.sleep(delayForNoMessage);
					} catch (InterruptedException e1) {
						isShutdown = true;
						break;
					}
					continue;
				}
 				
				 
				logger.info("The count to retry:" + keys.size());
				SystemLogger.info("count to retry:" + keys.size());
				 
				for(String key : keys) {
					Trace trace = errorCache.get(key);
					try {
						retry(trace);
					}catch(Exception e) {
						logger.error("TraceErrorHandler retry exception:", e);
						try {
							Thread.sleep(exceptionDelay);
						} catch (InterruptedException ie) {
							isShutdown = true;
							break;
						}
					}
				}
				 
				 
			} catch (Throwable t) {
				logger.error("TraceErrorHandler exception:", t);
				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException ie) {
					isShutdown = true;
					break;
				}
			}

		}

		isShutdown = true;
		logger.info(Util.join("stop TraceErrorHandler:[" + name + "]"));

	}
	
	private void retry(Trace trace) throws Exception {

		int retry = trace.getRetry();
		retry = retry + 1;
		trace.setRetry(retry);
		
		if (retry < maxRetry) {
			 
			if (traceService.exist(trace.getId())) {
				//이미 TOP0501에 존재하는 건은 에러캐시에서 삭제 처리 				
				String msg = messageResource.getMessage("error.msg.db.dup", null, null);
				trace.setRetryErrorMsg(msg);
				logger.info("duplica:" + Util.toJSONString(trace));
				errorCache.remove(trace.getId());
			} else {				
				try { 					
					traceService.insert(trace);								
					String msg = messageResource.getMessage("success.msg.retry", null, null);
					trace.setRetryErrorMsg(msg);
					logger.info("insert:" + Util.toJSONString(trace));
					mergeCache.put(trace.getId(), trace);
					errorCache.remove(trace.getId());
				} catch(Exception e) {
					String errorDetail = "";
					PrintWriter pw = null;
					try{
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						pw = new PrintWriter(baos);
						e.printStackTrace(pw);
						pw.flush();
						if(pw != null)  errorDetail = baos.toString();
					}finally{
						if(pw != null) pw.close();
					}
					String[] params = {errorDetail};
					String msg = messageResource.getMessage("error.msg.exception.retry", params, null);
					trace.setRetryErrorMsg(msg);
					logger.info("error:" + Util.toJSONString(trace));
					errorCache.put(trace.getId(), trace);
				}
			}

		} else {
			String msg = messageResource.getMessage("error.msg.too.much.retry", null, null);
			trace.setRetryErrorMsg(msg);
			logger.info("exceed:" + Util.toJSONString(trace));
			errorCache.remove(trace.getId());
		}
		 
	}
	 
 

}
