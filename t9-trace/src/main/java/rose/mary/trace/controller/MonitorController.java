/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.controller;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pep.per.mint.common.data.basic.ComMessage;
import pep.per.mint.common.util.Util;
import rose.mary.trace.apps.manager.MonitorManager;
import rose.mary.trace.data.monitor.SystemMonitorData;
import rose.mary.trace.monitor.TPS;

/**
 * <pre>
 * rose.mary.trace.controller
 * MonitorController.java
 * </pre>
 * @author whoana
 * @date Aug 26, 2019
 */
@RestController
public class MonitorController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	public final static int OP_START_MON_SYS  =  0;
	public final static int OP_START_MON_TPS1 = 10;
	public final static int OP_START_MON_TPS2 = 20;
	public final static int OP_START_MON_TPS3 = 30;
	public final static int OP_START_MON_TPS4 = 40; 
	
	
	public final static int OP_STOP_MON_SYS   =  1;
	public final static int OP_STOP_MON_TPS1  = 11;
	public final static int OP_STOP_MON_TPS2  = 21;
	public final static int OP_STOP_MON_TPS3  = 31;
	public final static int OP_STOP_MON_TPS4  = 41; 
	
	public final static int OP_GET_SYS   =  2;
	public final static int OP_GET_TPS1  = 12;
	public final static int OP_GET_TPS2  = 22;
	public final static int OP_GET_TPS3  = 32;
	public final static int OP_GET_TPS4  = 42; 
	
	@Autowired
	MonitorManager monitorManager;
	
	@RequestMapping(
			value = "/trace/managers/monitors/operations", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<Map<String, ?>, Object> operateMonitor(
			HttpSession httpSession,
			@RequestBody ComMessage<Map<String, ?>, Object> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		int op = (Integer)comMessage.getRequestObject().get("op");
		switch(op) {
		
		case OP_START_MON_SYS :
			logger.info("request startSystemResourceMonitor");
			monitorManager.startSystemResourceMonitor();
			break;
			
		case OP_START_MON_TPS1 :
			logger.info("request startThroughputMonitor1");
			monitorManager.startThroughputMonitor1();
			break;
		case OP_START_MON_TPS2 :
			logger.info("request startThroughputMonitor2");
			monitorManager.startThroughputMonitor2();
			break;
		case OP_START_MON_TPS3 :
			logger.info("request startThroughputMonitor3");
			monitorManager.startThroughputMonitor3();
			break;
		case OP_START_MON_TPS4 :
			logger.info("request startThroughputMonitor4");
			monitorManager.startThroughputMonitor4();
			break;   
			
		case OP_STOP_MON_TPS1 :
			logger.info("request stopThroughputMonitor1");
			monitorManager.stopThroughputMonitor1();
			break;
			
		case OP_STOP_MON_TPS2 :
			logger.info("request stopThroughputMonitor2");
			monitorManager.stopThroughputMonitor2();
			break;

		case OP_STOP_MON_TPS3 :
			logger.info("request stopThroughputMonitor3");
			monitorManager.stopThroughputMonitor3();
			break;

		case OP_STOP_MON_TPS4 :
			logger.info("request stopThroughputMonitor4");
			monitorManager.stopThroughputMonitor4();
			break;

			
		case OP_GET_SYS :
			logger.info("request getSystemMonitorData");
			SystemMonitorData data = monitorManager.getSystemMonitorData();
			comMessage.setResponseObject(data);
			break;
			
		case OP_GET_TPS1 :
			logger.info("request getTps1");
			TPS tps1 = monitorManager.getTps1();
			comMessage.setResponseObject(tps1);
			break;
			
		case OP_GET_TPS2 :
			logger.info("request getTps2");
			TPS tps2 = monitorManager.getTps2();
			comMessage.setResponseObject(tps2);
			break;
			
		case OP_GET_TPS3 :
			logger.info("request getTps3");
			TPS tps3 = monitorManager.getTps3();
			comMessage.setResponseObject(tps3);
			break;
			
		case OP_GET_TPS4 :
			logger.info("request getTps2");
			TPS tps4 = monitorManager.getTps4();
			comMessage.setResponseObject(tps4);
			break;
			
			
		default :
				break;
				
			
		}
		

		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	 
	
	
	@RequestMapping(
			value = "/trace/managers/monitors/system-resource", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<Map<String,String>, SystemMonitorData> getSystemResource(
			HttpSession httpSession,
			@RequestBody ComMessage<Map<String,String>, SystemMonitorData> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		
	 
		SystemMonitorData cmd = monitorManager.getSystemMonitorData();
		
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
		
		comMessage.setResponseObject(cmd);
		
		return comMessage;
		
	}
	
	@RequestMapping(
			value = "/trace/managers/monitors/tps1", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<?, TPS> getTps1(
			HttpSession httpSession,
			@RequestBody ComMessage<?, TPS> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		
	 
		TPS tps = monitorManager.getTps1();
		
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
		
		comMessage.setResponseObject(tps);
		
		return comMessage;
		
	}
	
	@RequestMapping(
			value = "/trace/managers/monitors/tps2", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<?, TPS> getTps2(
			HttpSession httpSession,
			@RequestBody ComMessage<?, TPS> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		
	 
		TPS tps = monitorManager.getTps2();
		
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
		
		comMessage.setResponseObject(tps);
		
		return comMessage;
		
	}
	
	
	
}
