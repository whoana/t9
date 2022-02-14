/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.channel;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pep.per.mint.common.util.Util;

/**
 * <pre>
 * rose.mary.trace.channel
 * Writer.java
 * </pre>
 * @author whoana
 * @date Sep 9, 2019
 */
public class ChannelWriter implements Runnable {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	boolean isShutdown = true;
	
	Thread thread = null; 
	
	Channel channel = null;
	
	long commitLapse;
	
	public ChannelWriter(Channel channel) {
		this.channel = channel;
	}
	
	public void start() throws Exception {
		logger.info("channel writer property:");
		logger.info("\t commit count:" + channel.getCommitCount());
		logger.info("\t max commit wait:" + channel.getMaxCommitWait());
		
		
		if(thread != null) stop();
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {
		isShutdown = true;
		if(thread != null) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				logger.error("",e);
			}
		}
		
	}
	
	
	@Override
	public void run() {
		
		isShutdown = false;
		
		logger.info(Util.join("start channel writer:" , this.getClass().getName() , "[" + Thread.currentThread().getName() + "]"));
	 
		commitLapse = System.currentTimeMillis();
		
		while(Thread.currentThread() == thread && !isShutdown) {
			
			if( (channel.uncommitedSize() % channel.getCommitCount() == 0 || (System.currentTimeMillis() - commitLapse >= channel.getMaxCommitWait()))) {
			//if(channel.uncommitedSize() > 0 && (channel.uncommitedSize() % channel.getCommitCount() == 0 || (System.currentTimeMillis() - commitLapse >= channel.getMaxCommitWait()))) {
				try {
					channel.commit();
					
				} catch (Exception e) {
					
					try {
						channel.rollback();						
					} catch (Exception e1) {
						logger.error("rollback error",e1);
					}
					
					logger.error("commit error",e);
				} finally {
					commitLapse = System.currentTimeMillis();					
				}
			}else {
				try {
					Thread.sleep(channel.getDelayForNoMessage());
					logger.debug("no message");
				} catch (InterruptedException e) { 
				}
			}
		}
		
		 
		try {
			channel.commit();
		} catch (Exception e) {
			logger.error("", e);
		}
 
		
		logger.info("end tpm : " + channel.getThroughputMonitor().getCurrentThroughput());
	 
		
		isShutdown = true; 
		logger.info(Util.join("stop channel writer:" , this.getClass().getName() , "[" + Thread.currentThread().getName() + "]"));
		
		
	}

}
