package rose.mary.trace.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rose.mary.trace.core.config.Config;
import rose.mary.trace.database.service.SystemService;
import rose.mary.trace.system.SystemErrorTester;

public class SystemErrorTestManager {
	
	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");
	
	Config config = null;
	 
	SystemService systemService;

	SystemErrorTester systemErrorTester;
	 
	public SystemErrorTestManager(
		Config config,
		SystemService systemService 
	) {
		this.config = config;
		this.systemService = systemService;  		
	}

	public void ready() throws Exception{ 
		
	}
 
	public void start() throws Exception {
		try {
			logger.info("SystemErrorTester starting");
			
			String name 		= config.getSystemErrorTestManagerConfig().getName();
			int delay 			= config.getSystemErrorTestManagerConfig().getDelay();
			int exceptionDelay  = config.getSystemErrorTestManagerConfig().getExceptionDelay();
			systemErrorTester 	= new SystemErrorTester(name,delay,exceptionDelay,systemService);
			
			systemErrorTester.start(false);
			
			logger.info("SystemErrorTester started");
		}catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 
	 */
	public void stop() {
		logger.info("SystemErrorTester stopping");
		if(systemErrorTester != null)  systemErrorTester.stop(false);
		logger.info("SystemErrorTester stopped");
	}
}
