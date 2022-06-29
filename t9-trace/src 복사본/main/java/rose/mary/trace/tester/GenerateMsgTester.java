package rose.mary.trace.tester;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pep.per.mint.common.util.Util;
import rose.mary.trace.service.GenerateTraceMsgService;

public class GenerateMsgTester implements Runnable{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	private boolean isShutdown = true;
	 
	private Thread thread = null; 
	
	private long exceptionDelay = 5000;
	
	private int repeatDelaySec = 5 * 60; //5 minutes 
	
	GenerateTraceMsgService gtms;
	
	List<Map> paramsList;
	
	String name;
	 
	public GenerateMsgTester(String name, long exceptionDelay, int repeatDelaySec, GenerateTraceMsgService gtms, List<Map> paramsList) {
		this.name = name;
		this.exceptionDelay = exceptionDelay;
		this.gtms = gtms;
		this.paramsList = paramsList;
		this.repeatDelaySec = repeatDelaySec;
	}
	
	@Override
	public void run() {
		logger.info(Util.join("start " + name));
		isShutdown = false;
		while(true) {
			try {
				if(thread.isInterrupted()) break;
				
				for(Map params : paramsList) {
					logger.info("테스트 메시지 생성을 시작합니다.");
					String res = gtms.generate(params);					
					logger.info("테스트 메시지 생성을 완료하였습니다.:" + res);
				}
				try {
					Thread.sleep(repeatDelaySec * 1000);
				} catch (InterruptedException e) {
					break;
				}
			}catch(Throwable t) {
				try {
					Thread.sleep(exceptionDelay);
				} catch (InterruptedException e) {
					break;
				}
				
			}
		}
		
		isShutdown = true; 
		logger.info(Util.join("stop " + name));
	}
	
	public void start() throws Exception {
		if(thread != null && thread.isAlive()) stop();
		thread = new Thread(this, name); 
		isShutdown = false;
		thread.start();
	}
	
	public void stop() {
		isShutdown = true;
		if(thread != null) thread.interrupt();
	} 
	
	

}
