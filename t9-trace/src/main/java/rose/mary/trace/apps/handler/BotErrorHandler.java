/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.handler;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import pep.per.mint.common.util.Util;
import rose.mary.trace.apps.cache.CacheProxy;
import rose.mary.trace.apps.envs.Variables;
import rose.mary.trace.data.common.State;
import rose.mary.trace.database.service.BotService;
import rose.mary.trace.system.SystemLogger;

/**
 * <pre>
 * Bot 데이터 DB 로딩시 에러난 건을 처리한다.
 * 에러 처리는 1건씩 처리하도록 한다.
 * 설정된 maxRetry 횟수 만큼 재시도한다. 
 * 처리할 수 없는 에러는 기록으로 남긴다.
 * 처리 성공한 건은 에러캐시에서 삭제한다.
 * 
 * BotErrorHandler.java
 * </pre>
 * @author whoana
 * @since 20200508
 */
public class BotErrorHandler implements Runnable{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	CacheProxy<String, State> errorCache;
	
	MessageSource messageResource;
	
	private long delayForNoMessage = 10;
	
	private boolean isShutdown = true;
	
	private long exceptionDelay = 1;	
	
	BotService botService;
	
	Thread thread;

	int maxRetry = 1;
	
	String name;
	
	public BotErrorHandler(
			String name,
			CacheProxy<String, State> errorCache, 
			MessageSource messageResource, 
			BotService botService, 
			long delayForNoMessage, 
			int maxRetry, 
			long exceptionDelay ) {
		this.name = name; 
		this.errorCache = errorCache;
		this.messageResource = messageResource;
		this.botService = botService;
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
		if (thread != null)thread.interrupt();
	}
	

	public void runAsap() {
		logger.info(Util.join("start BotErrorHandler:[" + name + "]"));
		while (true) {
			try {
				
				if(thread.isInterrupted()) break;
				
				Set<String> keys = errorCache.keys();
				if (keys == null || keys.size() == 0) {
					try {
						Thread.sleep(delayForNoMessage);
					} catch (InterruptedException ie) {
						isShutdown = true;
						break;
					}
					continue;
				}
				logger.info("The count to retry:" + keys.size());
				SystemLogger.info("count to retry:" + keys.size());
				
				String date = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
				for(String key : keys) {
					State state = errorCache.get(key);
					int retry = state.getRetry();
					retry = retry + 1;
					state.setRetry(retry);
					
					if(retry < maxRetry) {
						
						try {
							botService.mergeBot(state, date);
							String msg = messageResource.getMessage("success.msg.retry", null, null);
							state.setRetryErrorMsg(msg);
							errorCache.remove(key);
							logger.info(Util.toJSONString(state));
							
						}catch(Exception e) {
							 
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
							state.setRetryErrorMsg(msg);
							errorCache.put(key, state);
							logger.info(Util.toJSONString(state));
							 
							logger.error("BotErrorHandler exception:", e);
							try {
								Thread.sleep(exceptionDelay);
							} catch (InterruptedException ie) {
								isShutdown = true;
								break;
							}
							
						}
						
						
					}else{
						String msg = messageResource.getMessage("error.msg.too.much.retry", null, null);
						state.setRetryErrorMsg(msg);
						errorCache.remove(key);
						logger.info(Util.toJSONString(state));
					}
				}
				
				
			}catch(Throwable t) {
				logger.error("BotErrorHandler exception:", t);
				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException ie) {
					isShutdown = true;
					break;
				}
			}
		}

		isShutdown = true;
		logger.info(Util.join("stop BotErrorHandler:[" + name + "]"));
	}
	
	 
	public void runGracefully() {
		logger.info(Util.join("start BotErrorHandler:[" + name + "]"));
		while (Thread.currentThread() == thread && !isShutdown) {
			try {
				Set<String> keys = errorCache.keys();
				if (keys == null || keys.size() == 0) {
					try {
						Thread.sleep(delayForNoMessage);
					} catch (InterruptedException ie) {
						isShutdown = true;
						break;
					}
					continue;
				}
				 
				
				String date = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
				
				 
				for(String key : keys) {
					State state = errorCache.get(key);
					int retry = state.getRetry();
					if(retry < maxRetry) {
						
						try {
							botService.mergeBot(state, date);
							String msg = messageResource.getMessage("success.msg.retry", null, null);
							state.setRetryErrorMsg(msg);
							errorCache.remove(key);
							logger.info(Util.toJSONString(state));
							
						}catch(Exception e) {
							
							
							state.setRetry(retry + 1);
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
							state.setRetryErrorMsg(msg);
							errorCache.put(key, state);
							logger.info(Util.toJSONString(state));
							
							
							logger.error("BotErrorHandler exception:", e);
							try {
								Thread.sleep(exceptionDelay);
							} catch (InterruptedException ie) {
								isShutdown = true;
								break;
							}
							
						}
						
						
					}else{
						String msg = messageResource.getMessage("error.msg.too.much.retry", null, null);
						state.setRetryErrorMsg(msg);
						errorCache.remove(key);
						logger.info(Util.toJSONString(state));
					}
				}
				
				
			}catch(Throwable t) {
				logger.error("BotErrorHandler exception:", t);
				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException ie) {
					isShutdown = true;
					break;
				}
			}
		}

		isShutdown = true;
		logger.info(Util.join("stop BotErrorHandler:[" + name + "]"));
	}
	
	
	
	
}
