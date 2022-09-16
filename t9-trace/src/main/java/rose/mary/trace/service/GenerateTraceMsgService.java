/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.service;
 
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rose.mary.trace.core.simulator.TraceMsgCreator;
import rose.mary.trace.core.simulator.TraceMsgGenerator;
import rose.mary.trace.manager.CacheManager;

/**
 * <pre>
 * rose.mary.trace.service.test
 * GenerateTraceMsgService.java
 * </pre>
 * @author whoana
 * @date Jul 26, 2019
 */
@Service
public class GenerateTraceMsgService {

	Logger logger = LoggerFactory.getLogger(GenerateTraceMsgService.class);
	
	
	@Autowired
	CacheManager cacheManager;
	 
	
	public String generate(Map<String, String> params) throws Throwable{
		StringBuffer res = new StringBuffer();
		try {
			int port;					//-po
			String hostname; 			//-ho
			String qmgrName; 			//-qm
			String userId; 				//-um
			String password; 			//-ps
			String channelName; 		//-ch
			String queueName; 			//-qu
			String module;				//-mo
			int generateCount = 1000; 	//-ge
			int commitCount = 100;		//-co
			String data;				//-da
			String traceMsgCreator;     //-tr
			TraceMsgCreator tmc = null;
	
			if (checkParams(params)) {
				qmgrName = params.get("qmgrName");
				userId = params.get("userId");
				password = params.get("password");
				queueName = params.get("queueName");
				hostname = params.get("hostname");
				channelName = params.get("channelName");
				module = params.get("module");
				data = params.get("data");
				try {
					port = Integer.parseInt(params.get("port"));
				} catch (NumberFormatException e) {
					port = 1414;
				}
				 
				try {
					generateCount = Integer.parseInt(params.get("generateCount"));
				} catch (NumberFormatException e) {
					generateCount = 1000;
				}
				
				try {
					commitCount = Integer.parseInt(params.get("commitCount"));
				} catch (NumberFormatException e) {
					commitCount = 100;
				}
				
				traceMsgCreator = params.get("traceMsgCreator");
				if(traceMsgCreator != null) {
					Class clazz = Class.forName(traceMsgCreator);
					tmc = (TraceMsgCreator)clazz.newInstance(); 
				}
				
			} else { 
				throw new IllegalArgumentException();
			}	
			 
			
			TraceMsgGenerator tmg = new TraceMsgGenerator(hostname, port, qmgrName, userId, password, channelName, queueName, module, generateCount, commitCount, data, tmc, cacheManager.getInterfaceCache());
			
			logger.info("------------------------------------------");
			logger.info("- start task information");
			logger.info("------------------------------------------");
			logger.info(tmg.information());
			logger.info("------------------------------------------");
			logger.info("- initailizing . . .");
			
			tmg.initialize();
			
			logger.info("- generating msgs");
			logger.info("------------------------------------------");
			StopWatch sw = new StopWatch();
			sw.start();
			tmg.generate();
			sw.stop();
			logger.info("------------------------------------------");
			logger.info("- generated msg successly.(elapsed:" + sw.getTime() + " ms)");
			logger.info("- end task");
			logger.info("------------------------------------------");
			return res.append(tmg.information()).toString();
		}catch (Throwable t) {
			logger.info("------------------------------------------");
			logger.info("- fail to generatd msgs.");
			logger.info("------------------------------------------");
			t.printStackTrace();
			throw t;
		}
	}
	



	public String generateNodeMsg(Map<String, String> params) throws Throwable{
		StringBuffer res = new StringBuffer();
		try {
			int port;					//-po
			String hostname; 			//-ho
			String qmgrName; 			//-qm
			String userId; 				//-um
			String password; 			//-ps
			String channelName; 		//-ch
			String queueName; 			//-qu
			String module;				//-mo
			int generateCount = 1000; 	//-ge
			int commitCount = 100;		//-co
			String data;				//-da
			String traceMsgCreator;     //-tr
			TraceMsgCreator tmc = null;
	
			if (checkParams(params)) {
				qmgrName = params.get("qmgrName");
				userId = params.get("userId");
				password = params.get("password");
				queueName = params.get("queueName");
				hostname = params.get("hostname");
				channelName = params.get("channelName");
				module = params.get("module");
				data = params.get("data");
				try {
					port = Integer.parseInt(params.get("port"));
				} catch (NumberFormatException e) {
					port = 1414;
				}
				 
				try {
					generateCount = Integer.parseInt(params.get("generateCount"));
				} catch (NumberFormatException e) {
					generateCount = 1000;
				}
				
				try {
					commitCount = Integer.parseInt(params.get("commitCount"));
				} catch (NumberFormatException e) {
					commitCount = 100;
				}
				
				traceMsgCreator = params.get("traceMsgCreator");
				if(traceMsgCreator != null) {
					Class clazz = Class.forName(traceMsgCreator);
					tmc = (TraceMsgCreator)clazz.newInstance(); 
				}
				
			} else { 
				throw new IllegalArgumentException();
			}	
			 
			
			TraceMsgGenerator tmg = new TraceMsgGenerator(hostname, port, qmgrName, userId, password, channelName, queueName, module, generateCount, commitCount, data, tmc, cacheManager.getInterfaceCache());
			 
			
			tmg.initialize();
			 
			tmg.generateNodeMsg(params);
			
			return res.append("ok").toString();
		}catch (Throwable t) {
			logger.info("------------------------------------------");
			logger.info("- fail to generatd msgs.");
			logger.info("------------------------------------------");
			t.printStackTrace();
			throw t;
		}
	}

	
	

	/**
	 * @return
	 */
	public static boolean checkParams(Map<String, String> params) {

		boolean ok = params.containsKey("port") && 
					 params.containsKey("hostname") && 
					 params.containsKey("qmgrName") && 
					 params.containsKey("channelName") && 
					 params.containsKey("queueName") &&
					 params.containsKey("module") &&
					 params.containsKey("generateCount") &&
					 params.containsKey("commitCount") &&
					 params.containsKey("data") ;
		
		
		
		if (ok) {
			try { Integer.parseInt((String) params.get("port")); } catch (NumberFormatException e) { ok = false; System.out.println("-po 파라미터는 숫자여야 합니다.");}
			try { Integer.parseInt((String) params.get("generateCount")); } catch (NumberFormatException e) { ok = false; System.out.println("-ge 파라미터는 숫자여야 합니다.");}
			try { Integer.parseInt((String) params.get("commitCount")); } catch (NumberFormatException e) { ok = false; System.out.println("-co 파라미터는 숫자여야 합니다.");}
		}

		return ok;
	}
	
}
