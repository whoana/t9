package rose.mary.trace.core.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemLogger {
	public final static String loggerName = "rose.mary.trace.SystemLogger";  
	public final static String astar = "*";
	static Logger logger = LoggerFactory.getLogger(loggerName);
	
	public static Logger getLogger() {
		return logger;
	}
	
	
	public static void info(String msg) {
		logger.info(msg);
	}


	public static void info(String msg, Throwable t) {
		logger.info(msg, t);
	}
	
	
	public static String astars(int len) {
		StringBuffer str = new StringBuffer();
		for(int i = 0 ; i < len ; i ++)str.append(astar);			
		return str.append(System.lineSeparator()).toString();
	}


	public static void error(String msg, Throwable t) {
		logger.error(msg, t);
	}
}
