/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.controller;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pep.per.mint.common.data.basic.ComMessage;
import pep.per.mint.common.util.Util;
import rose.mary.trace.database.service.TpsService;
import rose.mary.trace.manager.ChannelManager;
import rose.mary.trace.service.GenerateTraceMsgService;

/**
 * <pre>
 * rose.mary.trace.controller
 * GenerateTraceMsgController.java
 * </pre>
 * 
 * @author whoana
 * @date Jul 26, 2019
 */
@RestController
public class GenerateTraceMsgController {

	@Autowired
	GenerateTraceMsgService gtms;

	@RequestMapping(value = "/trace/test/generate/msgs", params = "method=GET", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody ComMessage<Map<String, String>, String> generate(
			HttpSession httpSession,
			@RequestBody ComMessage<Map<String, String>, String> comMessage,
			Locale locale,
			HttpServletRequest request) throws Throwable {

		String errorCd = "";
		String errorMsg = "";
		Map params = comMessage.getRequestObject();
		String res = null;

		// Map<String, String> params = new HashMap<String, String>();
		// params.put("port", "58414");
		// params.put("hostname", "10.10.1.168");
		// params.put("qmgrName", "TESTQM08");
		// params.put("channelName", "SYSTEM.DEF.SVRCONN");
		// params.put("queueName", "TEST.LQ");
		// params.put("module", "w");
		// params.put("generateCount", "10");
		// params.put("commitCount", "10");
		// params.put("traceMsgCreator",
		// "rose.mary.trace.simulator.DefaultTraceMsgCreator");
		// params.put("data", "한글1234567890qwertyuiop");

		// params.put("port", "41414");
		// params.put("hostname", "10.10.1.10");
		// params.put("qmgrName", "IIP");
		// params.put("channelName", "SYSTEM.DEF.SVRCONN");
		// params.put("queueName", "TRACE.EQ");
		// params.put("module", "w");
		// params.put("generateCount", "10");
		// params.put("commitCount", "10");
		// params.put("traceMsgCreator",
		// "rose.mary.trace.simulator.DefaultTraceMsgCreator");
		// params.put("data", "한글1234567890qwertyuiop");

		res = gtms.generate(params);

		// --------------------------------------------------
		// 서비스 처리 종료시간을 얻어 CM에 세팅한다.
		// --------------------------------------------------
		{
			comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
		}

		comMessage.setResponseObject(res);
		return comMessage;
	}


	@RequestMapping(value = "/trace/test/generate/msgs/nodes", params = "method=GET", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody ComMessage<Map<String, String>, String> generateNodeMsg(
			HttpSession httpSession,
			@RequestBody ComMessage<Map<String, String>, String> comMessage,
			Locale locale,
			HttpServletRequest request) throws Throwable {
 
		Map<String, String> params = comMessage.getRequestObject();
		String res = null;
 

		res = gtms.generateNodeMsg(params);

		// --------------------------------------------------
		// 서비스 처리 종료시간을 얻어 CM에 세팅한다.
		// --------------------------------------------------
		{
			comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
		}

		comMessage.setResponseObject(res);
		return comMessage;
	}



	@Autowired
	ChannelManager channelManager;

	@Autowired
	TpsService tpsService;

	@RequestMapping(value = "/trace/test/tps", params = "method=GET", method = RequestMethod.POST, headers = "content-type=application/json")
	public @ResponseBody ComMessage<Map<String, String>, String> tps(
			HttpSession httpSession,
			@RequestBody ComMessage<Map<String, String>, String> comMessage,
			Locale locale,
			HttpServletRequest request) throws Throwable {
 
		Map<String, String> params = comMessage.getRequestObject();
		String res = null;

		channelManager.stopChannels();

		res = gtms.generate(params);

		String startDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
		channelManager.startChannels();

		int sampleTime = 1000 * 60 * 10; //default in milliseconds
		try {
			sampleTime = Integer.parseInt(params.get("sampleTime"));
			Thread.sleep(sampleTime);
		} catch (Exception e) {

		}
		String endDate = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
		String recordDate = endDate;
		tpsService.recordAvgTps(recordDate, startDate, endDate);
		int avgTps = tpsService.getAvgTps(recordDate);

		res = "avg tps:" + avgTps;
		// --------------------------------------------------
		// 서비스 처리 종료시간을 얻어 CM에 세팅한다.
		// --------------------------------------------------
		{
			comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
		}

		comMessage.setResponseObject(res);
		return comMessage;
	}

	// for thread test
	boolean isShutdown = false;
	Thread longTermThread = null;

	@GetMapping("/trace/test/lsnr-start")
	public String runListener() throws Throwable {
		if (longTermThread == null) {
			isShutdown = false;
			longTermThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (Thread.currentThread() == longTermThread && !isShutdown) {
						try {
							System.out.println("I'm long live thread:" + Thread.currentThread().getName());
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println("stop long live thread:" + Thread.currentThread().getName());
				}
			});
			longTermThread.start();
		}
		return "success";
	}

	@GetMapping("/trace/test/lsnr-stop")
	public String stopListener() throws Throwable {
		if (longTermThread != null) {
			isShutdown = true;
			longTermThread.join();
			longTermThread = null;
		}
		return "success";
	}

	boolean isShutdown2 = false;
	Thread longTermThread2 = null;

	@GetMapping("/trace/test/lsnr-start2")
	public String runListener2() throws Throwable {
		if (longTermThread2 == null) {
			isShutdown2 = false;
			longTermThread2 = new Thread(new Runnable() {
				@Override
				public void run() {
					while (Thread.currentThread() == longTermThread2 && !isShutdown2) {
						try {
							System.out.println("I'm long live thread2:" + Thread.currentThread().getName());
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println("stop long live thread2:" + Thread.currentThread().getName());
				}
			});
			longTermThread2.start();
		}
		return "success2";
	}

	@GetMapping("/trace/test/lsnr-stop2")
	public String stopListener2() throws Throwable {
		if (longTermThread2 != null) {
			isShutdown2 = true;
			longTermThread2.join();
			longTermThread2 = null;
		}
		return "success2";
	}
}
