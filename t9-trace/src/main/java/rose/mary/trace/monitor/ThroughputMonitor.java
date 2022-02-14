/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.monitor;


import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * rose.mary.trace.monitor
 * TPSMonitor.java
 * </pre>
 * 
 * @author whoana
 * @date Aug 12, 2019
 */
public class ThroughputMonitor extends Monitor<TPS> {
	
	Logger logger = LoggerFactory.getLogger(ThroughputMonitor.class);
	
	final static int MAX_THROUGHPUT = 1000000000;
	
	AtomicInteger throughput = new AtomicInteger(0);
	
	
	
	public ThroughputMonitor(int watchTime) {
		super(watchTime);
	}
	
	public void reset() {
		throughput.set(0);
	}
	
	public int getTps(int samplingTime) {
		int t1 = throughput.get();
		try {
			Thread.sleep(samplingTime);
		} catch (InterruptedException e) {
		}
		int tps = throughput.get() - t1;
		return tps;
	}
	
	 
	
	public int count() {
		int cnt = 0;
		if((cnt = throughput.incrementAndGet()) >= MAX_THROUGHPUT) reset();
		return cnt;
	}

	
	public int count(int delta) {
		int cnt = 0;
		if((cnt = throughput.addAndGet(delta)) >= MAX_THROUGHPUT) reset();
		return cnt;
	}
	
	
	public int getCurrentThroughput() {
		return throughput.get();
	}

	@Override
	public TPS watch() {
		long startTime = System.currentTimeMillis();
		int value = getTps(watchTime);
		long endTime = System.currentTimeMillis();
		int throughput = getCurrentThroughput();
		TPS tps = new TPS(value, startTime, endTime, throughput);
		return tps;
	}
 
}
