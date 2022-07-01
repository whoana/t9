/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pep.per.mint.common.data.basic.ComMessage;
import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.config.ChannelManagerConfig;
import rose.mary.trace.core.data.cache.CacheSummary;
import rose.mary.trace.core.data.channel.ChannelConfig;
import rose.mary.trace.core.data.channel.ChannelOperation;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.monitor.TPS;
import rose.mary.trace.manager.CacheManager;
import rose.mary.trace.manager.MonitorManager;
import rose.mary.trace.manager.ServerManager;
import rose.mary.trace.manager.TesterManager;
import rose.mary.trace.service.ConfigurationService;
import rose.mary.trace.service.GenerateTraceMsgService;

/**
 * <pre>
 * rose.mary.trace.controller
 * TraceServerController.java
 * </pre>
 * @author whoana
 * @date Aug 26, 2019
 */
@EnableAsync
@RestController
public class ServerController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ServerManager serverMgr;
	
	@Autowired
	MonitorManager monitorManager;
	

	@Autowired
	CacheManager cacheManager;
	
	@Autowired
	ConfigurationService configurationService;

	@Autowired
	GenerateTraceMsgService gtms;
	
	@Autowired 
	TesterManager testerManager;
	
	
	@RequestMapping(
			value = "/trace/console/servers/testers/start",  
			method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> startTester(
			HttpSession httpSession,  
			HttpServletRequest request) throws Throwable{
		
		 
		Map<String, Object> res = new HashMap<String, Object>();
		
		try {
			testerManager.start();
			res.put("cd", 0); 
			res.put("msg", "success to start tester"); 
		}catch(Exception e) {
			res.put("cd", 9); 
			res.put("msg", e.getMessage()); 
		}
	 
		return res;
	}
	
	@RequestMapping(
			value = "/trace/console/servers/testers/stop",  
			method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> stopTester(
			HttpSession httpSession,  
			HttpServletRequest request) throws Throwable{
		
		 
		Map<String, Object> res = new HashMap<String, Object>();
		
		try {
			testerManager.stop();
			res.put("cd", 0); 
			res.put("msg", "success to stop tester"); 
		}catch(Exception e) {
			res.put("cd", 9); 
			res.put("msg", e.getMessage()); 
		}
	 
		return res;
	}
	
	
	//@GetMapping("/trace/test/generate/msgs")
	@RequestMapping(
			value = "/trace/console/servers/generate/msgs/{number}",  
			method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> generateAutomatically(
			HttpSession httpSession, 
			@PathVariable int number,
			HttpServletRequest request) throws Throwable{
		
		 
		Map<String, Object> res = new HashMap<String, Object>();
		
		ChannelManagerConfig cfg = configurationService.getChannelManagerConfig();
		ChannelConfig cc = cfg.getChannelConfigs().get(0);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("port", cc.getPort() + "");
		params.put("hostname", cc.getHostName());
		params.put("qmgrName", cc.getQmgrName());
		params.put("channelName", cc.getChannelName());
		params.put("queueName", cc.getQueueName());
		params.put("module", cc.getModule());
		params.put("generateCount", number + "");
		params.put("commitCount", "100");
		params.put("traceMsgCreator", "rose.mary.trace.simulator.DefaultTraceMsgCreator");
		params.put("data", "한글1234567890qwertyuiop");
		
		
		String msg = gtms.generate(params);
		res.put("cd", 0); 
		res.put("msg", msg); 
	 
		return res;
	}
	
	
	
	
	
	@MessageMapping("/cache-summary")
	@SendTo("/topic/cache-summary")
	public CacheSummary getCacheSummary(Map request) throws Exception {
		CacheSummary csum = new CacheSummary();
		
//		
//		
//		@Component
//		public class ScheduledUpdatesOnTopic{
//
//		    @Autowired
//		    private SimpMessagingTemplate template;
//		    @Autowired
//		    private final MessagesSupplier messagesSupplier;
//
//		    @Scheduled(fixedDelay=300)
//		    public void publishUpdates(){
//		        template.convertAndSend("/topic/greetings", messagesSupplier.get());
//		    }
//		}
//		
//		
		return csum;
	}
	
	
	
	
	@RequestMapping( value = "/trace/console/servers/caches", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getCacheDataCount( HttpSession httpSession, Locale locale,  HttpServletRequest request) throws Throwable{
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			
			res.put("cd", 0);
			
			
			
			StringBuffer dcMsg = new StringBuffer("D.C:[");
			List<CacheProxy<String, Trace>> dcs = cacheManager.getDistributeCaches();
			{
				CacheProxy<String, Trace> dc = dcs.get(0);
				dcMsg.append("{" + dc.getName() + ":" + dc.getCacheInfo().getCurrentNumberOfEntries() + "}");
			}
			for(int i = 1 ; i < dcs.size() ; i ++) {
				CacheProxy<String, Trace> dc = dcs.get(i);
				dcMsg.append(",{" + dc.getName() + ":" + dc.getCacheInfo().getCurrentNumberOfEntries() + "}");
			}
			dcMsg.append("]");
		
			
			StringBuffer mcMsg = new StringBuffer("M.C:");
			CacheProxy<String, Trace> mc = cacheManager.getMergeCache();
			mcMsg.append("{" + mc.getName() + ":" + mc.getCacheInfo().getCurrentNumberOfEntries() + "}");
			
			StringBuffer bcMsg = new StringBuffer("B.C:[");
			List<CacheProxy<String, State>> bcs = cacheManager.getBotCaches();
			{
				CacheProxy<String, State> bc = bcs.get(0);
				bcMsg.append("{" + bc.getName() + ":" + bc.getCacheInfo().getCurrentNumberOfEntries() + "}");
			}
			for(int i = 1 ; i < bcs.size() ; i ++) {
				CacheProxy<String, State> bc = bcs.get(i);
				bcMsg.append(",{" + bc.getName() + ":" + bc.getCacheInfo().getCurrentNumberOfEntries() + "}");
			}
			bcMsg.append("]");
		
			
			StringBuffer fcMsg = new StringBuffer("F.C:");
			CacheProxy<String, State> fc = cacheManager.getFinCache();
			fcMsg.append("{" + fc.getName() + ":" + fc.getCacheInfo().getCurrentNumberOfEntries() + "}");
			
			
			String msg = "{" + dcMsg.toString() + "," + mcMsg.toString() + "," + bcMsg.toString() + "," + fcMsg.toString() + "}";
		    res.put("msg", msg);
		    
		}catch(Exception e) {
			res.put("cd", 9);
			res.put("msg", "error:startServer:(" + e.getMessage()+ ")");
			logger.error("", e);
		}
		return res;
	}
	
	@Async
	@RequestMapping(value = "/trace/console/servers/shutdown",  method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> shutdown( HttpSession httpSession, Locale locale,  HttpServletRequest request) throws Throwable{
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			serverMgr.shutdownServer(); 
			res.put("cd", 0);
		}catch(Exception e) {
			res.put("cd", 9);
			res.put("msg", "error:shutdown:(" + e.getMessage()+ ")");
			logger.error("", e);
		}
		return res;
	}
	
	
	@RequestMapping( value = "/trace/console/servers/start", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> startServerGet( HttpSession httpSession, Locale locale,  HttpServletRequest request) throws Throwable{
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			serverMgr.startServer();
			res.put("cd", 0);
		}catch(Exception e) {
			res.put("cd", 9);
			res.put("msg", "error:startServer:(" + e.getMessage()+ ")");
			logger.error("", e);
		}
		return res;
	}
	
	@RequestMapping(
			value = "/trace/console/servers/stop",
			method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> stopServerGet( HttpSession httpSession, Locale locale,  HttpServletRequest request) throws Throwable{
		Map<String, Object> res = new HashMap<String, Object>();
		try {
			serverMgr.stopServer();
			res.put("cd", 0);
		}catch(Exception e) {
			res.put("cd", 9);
			res.put("msg", "error:stopServer:(" + e.getMessage()+ ")");
			logger.error("", e);
		}
		return res;
	}

	
	@RequestMapping(value = "/trace/console/servers/check", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> checkServerGet(
			HttpSession httpSession,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		Map<String, Object> res = new HashMap<String, Object>();
		int state = 0;
		try {
			state = serverMgr.checkServerState();
			res.put("cd", state);
		}catch(Exception e) {
			res.put("cd", state);
			res.put("msg", "error:checkServer:(" + e.getMessage()+ ")");
			logger.error("", e);
		}
		
		return res;
	}
	
	@RequestMapping(value = "/trace/console/servers/tps", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getTps(
			HttpSession httpSession,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		Map<String, Object> res = new HashMap<String, Object>();
		int state = 0;
		try {
			TPS tps = monitorManager.getTps2();
			res.put("tps", tps.getValue());
			res.put("cd", 0);
			
		}catch(Exception e) {
			res.put("cd", 9);
			res.put("msg", "error:checkServer:(" + e.getMessage()+ ")");
			logger.error("", e);
		}
		
		return res;
	}
	
	
	@RequestMapping(
			value = "/trace/managers/servers/start", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> startServer(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		
		serverMgr.startServer();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	@RequestMapping(
			value = "/trace/managers/servers/stop", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> stopServer(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.stopServer();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	

	@RequestMapping(
			value = "/trace/managers/servers/trace-error-handler/start", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> startTraceErrorHandler(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.startTraceErrorHandler();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	@RequestMapping(
			value = "/trace/managers/servers/trace-error-handler/stop", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> stopTraceErrorHandler(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.stopTraceErrorHandler();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	
	@RequestMapping(
			value = "/trace/managers/servers/finisher/start", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> startFinisher(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.startFinisher();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	@RequestMapping(
			value = "/trace/managers/servers/finisher/stop", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> stopFinisher(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.stopFinisher();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	
	@RequestMapping(
			value = "/trace/managers/servers/botLoader/start", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> startBotLoader(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.startBotLoader();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	@RequestMapping(
			value = "/trace/managers/servers/botLoader/stop", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> stopBotLoader(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.stopBotLoader();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	@RequestMapping(
			value = "/trace/managers/servers/boter/start", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> startBoter(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.startBoter();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	@RequestMapping(
			value = "/trace/managers/servers/boter/stop", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> stopBoter(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.stopBoter();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	
	@RequestMapping(
			value = "/trace/managers/servers/loader/start", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> startLoader(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.startLoader();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	@RequestMapping(
			value = "/trace/managers/servers/loader/stop", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> stopLoader(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.stopLoader();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	
	@RequestMapping(
			value = "/trace/managers/servers/channel/start", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> startChannel(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.startChannel();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	@RequestMapping(
			value = "/trace/managers/servers/channel/stop", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> stopChannel(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.stopChannel();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	
	@Async
	@RequestMapping(
			value = "/trace/managers/servers/shutdown", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<ChannelOperation, ?> shutdown(
			HttpSession httpSession,
			@RequestBody ComMessage<ChannelOperation, ?> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		
		
		serverMgr.shutdownServer();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setErrorCd("0");
		
		return comMessage;
		
	}
	
	
	
	
	
	
}
