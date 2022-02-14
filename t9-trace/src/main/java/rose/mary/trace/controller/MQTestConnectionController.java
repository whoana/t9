/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.controller;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody; 

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

import pep.per.mint.common.data.basic.ComMessage;
import pep.per.mint.common.util.Util;

/**
 * <pre>
 * rose.mary.trace.controller
 * MQTestConnectionController.java
 * </pre>
 * @author whoana
 * @date Aug 9, 2019
 */
//@RestController
public class MQTestConnectionController {

	
	@RequestMapping(
			value = "/trace/test/wmqcon", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<Map<String,String>, String> conn(
			HttpSession httpSession,
			@RequestBody ComMessage<Map<String,String>, String> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		

		Map<String, String> params = comMessage.getRequestObject();
		 
		String hostName = params.get("hostName");
		String qmgrName = params.get("qmgrName");
		int port = 0;
		try {
			port = Integer.parseInt(params.get("port"));
		}catch(Exception e) {
			
		}
		String userId = params.get("userId");
		String password = params.get("password");
		String channelName = params.get("channelName");
		boolean isBind = false;
		try {
			isBind = Boolean.parseBoolean(params.get("isBind"));
		}catch(Exception e) {
			isBind = false;
		}
		
		Hashtable<String, Object> envs = new Hashtable<String, Object>();
		if (isBind) {
			envs.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES_BINDINGS);//binding mode connect
		}else {
			if (channelName != null)envs.put(CMQC.CHANNEL_PROPERTY, channelName);
			if (hostName != null)   envs.put(CMQC.HOST_NAME_PROPERTY, hostName);
			if (port != 0)          envs.put(CMQC.PORT_PROPERTY, new Integer(port));
			if (userId != null)     envs.put(CMQC.USER_ID_PROPERTY, userId);
			if (password != null)   envs.put(CMQC.PASSWORD_PROPERTY, password);			
		}
		
		MQException.log = null;
		 
		System.out.println("start connection test");
		MQQueueManager qmgr = new MQQueueManager(qmgrName, envs);
		System.out.println("finish connection");
		qmgr.close();
		System.out.println("close connection");
		
		//--------------------------------------------------
		// 서비스 처리 종료시간을 얻어 CM에 세팅한다.
		//--------------------------------------------------
		{
			comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
		}
		
		comMessage.setResponseObject("connection success");
		
		return comMessage;
	}
	 
}
