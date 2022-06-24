package rose.mary.trace.core.system;
 
   
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;   

import pep.per.mint.common.util.Util; 
import rose.mary.trace.database.service.SystemService; 

/**
 * <pre>
 * SystemError를 발생시키는 테스터
 * SystemErrorTester.java
 * 
 * </pre>
 * @author whoana
 * @since 20200515
 */

public class SystemErrorTester implements Runnable{
	
	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");
	
	SystemService systemService;
	
	private boolean isShutdown = true;
	
	private long exceptionDelay = 1000;	  
	
	private long delay = 10000;
	
	String name;
	
	Thread thread;
	
	public SystemErrorTester(
		String name, 
		long delay,
		long exceptionDelay,
		SystemService systemService
	) {
		this.name = name;
		this.delay = delay;
		this.exceptionDelay = exceptionDelay;
		this.systemService = systemService;
	}
	
	public void start(boolean gracefully) throws Exception {
		if (thread != null && thread.isAlive()) stop(gracefully);
		thread = new Thread(this, name);
		isShutdown = false;
		thread.start();
	}

	public void stop(boolean gracefully) {
		isShutdown = true;
		if(gracefully) { 
			if (thread != null) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					logger.error("", e);
				}
			}
		} else {
			if (thread != null)thread.interrupt();
		}
	}
	
	public void run() {
		logger.info(Util.join("start " + name));
		while (Thread.currentThread() == thread && !isShutdown) {
			try {
			
				if(thread.isInterrupted()) break;
				doSomthing();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException ie) {
					isShutdown = true;
					break;
				}
			}catch(Throwable t) {
				logger.error(Util.join(name," exception:"), t);
				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException e1) {
					isShutdown = true;
					break;
				}
			}
		}
		
		isShutdown = true;
		logger.info(Util.join("stop ", name));
	}

	private void doSomthing() throws Throwable {
		systemService.generateBadSqlGrammarException();
	}
	 
    
	
}
