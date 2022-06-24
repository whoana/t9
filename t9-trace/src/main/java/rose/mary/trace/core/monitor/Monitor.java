/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rose.mary.trace.core.monitor.listener.MonitorListener;
import rose.mary.trace.core.monitor.listener.event.MonitorEvent;


/**
 * <pre>
 * rose.mary.trace.monitor
 * Monitor.java
 * </pre>
 * @author whoana
 * @date Aug 12, 2019
 */
public abstract class Monitor<T> implements Runnable{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
 
	public abstract T watch();

	public abstract void reset();
	
	Thread thread;
	
	boolean isShutdown = false;
	
	int watchTime = 10 * 1000;

	MonitorListener<T> ml;

	public Monitor(int watchTime) {
		this.watchTime = watchTime;
	}
	
	
	public void start() {
		if(thread != null) {
			stop();
			reset();
		}
		thread = new Thread(this);
		isShutdown = false;
		thread.start();
		logger.debug(getClass().getName() + " started");
	}	
	
	public void stop() {
		if(thread != null) {
			try {
				isShutdown = true;
				thread.join();				
			} catch (InterruptedException e) { 
				logger.error("",e);
			}
		}
	}
	 	
	public void run() {
		
		while(Thread.currentThread() == thread && !isShutdown) {
			logger.debug(getClass().getName() + " watching ....");
			T value = watch();
			
			if(ml != null) { 
				try {
					MonitorEvent<T> me = new MonitorEvent<T>(value);
					ml.watch(me);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		logger.debug(getClass().getName() + " stop");
	}	
	
	
	public void setMonitorListener(MonitorListener<T> ml) {
		this.ml = ml;
	}
	
	public void unregisterMonitorListener() {
		this.ml = null;
	}

	public boolean isShutdown() {
		return isShutdown;
	}
	
}
