package rose.mary.trace.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
 

import pep.per.mint.common.util.Util;
import rose.mary.trace.channel.rest.RestChannel;
import rose.mary.trace.manager.ChannelManager; 

@Controller
public class RestChannelController {

	Logger logger = LoggerFactory.getLogger(getClass());			
	
	@Autowired
	ChannelManager channelManager;
	
	RestChannel restChannel;
	 
	
	/**
	 * <pre> 
	 * REST 방식 트레킹 수집 서비스 
	 * 주) 비동기 방식이므로 트레킹의 디비에 적재 여부와 무관하게 결과를 리턴한다.
	 *    즉 디비 적재에 실패해도 트래킹 요청이 정상적으로 수신되기 만 하면 응답은 OK이다.    
	 * url          : http://127.0.0.1:8090/traces
	 * HTTP.Method  : POST
	 * params       : 
	 * [{
	 *	"integrationId"	: "EG_EGSSRM_BD_001", 	//인터페이스ID
	 *	"originHostId"	: "testhost01",       	//원호스트아이디(송신,허브,수신 이 모두 동일한 값이어야 한다.)
	 *	"date"          : "20200320170050748",	//인터페이스발생일(송신,허브,수신 이 모두 동일한 값이어야 한다.)
	 *	"processId"     : "Sender01",         	//인터페이스 프로세스ID (송신,허브,수신 이 각기 틀린 값으로 설정)
	 *	"processDate"	: "20200320170050763",	//인터페이스 프로세스 시작 시간 
	 *	"processEndDate": "20200320170050763",	//인터페이스 프로세스 종료 시간 
	 *	"type"          : "RCVR",             	//송신 : SNDR, 허브 : BRKR, 수신 : RCVR
	 *	"hostId"        : "testhost02",       	//인터페이스 프로세스가 실행된 노드의 호스트ID
	 *	"status"        : "00",                 //프로세스가 처리 결과 
	 *	"todoNodeCount" : 1,                    //인터페이스 수신노드 개 수(모든 수신노드가 처리되면 인터페이스가 완료된 것으로 처리한다.)
	 *	"errorCode"     : "",                   //노드 처리 시 발생된 에러의 코드 값 
	 *	"errorMessage"  : "",                   //노드 처리 시 발생된 에러의 내용 
	 *	"recordCount"   : "0",                  //처리 데이터의 레코드 수 
	 *	"dataSize"      : "0",                  //처리 데이터의 사이즈 
	 *	"compress"      : "0",                  //처리 데이터의 압축 여부 (0:일반, 1:압축)
	 *	"data"          : ""                    //처리 데이터 
	 * }]
	 * </pre>
	 * 
	 * @param httpSession
	 * @param trace
	 * @param locale
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(
			value = "/traces",  
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody Map<String, Object> trace(
			HttpSession httpSession,
			@RequestBody List<Map<String, Object>> traces,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		Map<String, Object> res = new HashMap<String, Object>();
		
		try {
			
			if(channelManager.getRestChannel() == null) throw new Exception("RestChannel is not open yet.");
	 
			channelManager.getRestChannel().consumeAsync(traces);
			res.put("msg", "ok");
			res.put("cd", 0);
			
		}catch(Exception e) {
			
			res.put("msg", "fail");
			res.put("cd", 9);
			res.put("exception", e.getMessage());
			
			logger.error("", e);
			
		}
		
		return res;
		
	} 
	
	/**
	 * commitCount for creating test messages;
	 */
	int commitCount = 200;	
	
	
	/**
     * <pre> 
     * 
	 * REST 방식 트레킹 수집 서비스 테스트 함수  
	 * url          : http://127.0.0.1:8090/traces/test
	 * HTTP.Method  : POST
	 * params       : 파라메터인 testInfo 에는 테스트 integrationId, 테스트 count 값이 반드시 전달되어야 함.
	 * example)
	 * {
	 *	"integrationId"	: "EG_EGSSRM_BD_001",
	 *	"count"         : "100"
	 * }
	 *  
	 * </pre>
	 * @param httpSession
	 * @param testInfo  
	 * @param locale
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	@RequestMapping(
			value = "/traces/test",  
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody Map<String, Object> traceTest(
			HttpSession httpSession,
			@RequestBody Map<String, Object> testInfo,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		Map<String, Object> res = new HashMap<String, Object>();
		
		try {
			if(channelManager.getRestChannel() == null) throw new Exception("RestChannel is not open yet.");		
			
			
			List<String> integrationIds = new ArrayList<String>();
			String integrationId = (String)testInfo.get("integrationId");				
			integrationId = integrationId == null || integrationId.trim().length() == 0 ? "UNKNOWN-IF-001":integrationId;
			
			if(integrationId.contains(",")) {
				StringTokenizer st = new StringTokenizer(integrationId, ",");
				while(st.hasMoreTokens()) {
					integrationIds.add(st.nextToken());
				}
			}else {
				integrationIds.add(integrationId);					
			}
			
			logger.debug("The size of integrationId for test :" + integrationIds.size());
			
			int count = 1;
			try {
				count = Integer.parseInt((String)testInfo.get("count")); 
			}catch(Exception e) {
				logger.error("exception count = Integer.parseInt((String)testInfo.get(\"count\")) :", e);
				count = 1;
			}

			int totoalCount = 0;
			int val = count / commitCount;
			int remainder = count % commitCount;
			
			
			for(int i = 0 ; i < val ; i ++) {
				
				List<Map<String, Object>> traces = createTraces(integrationIds, commitCount);
				//logger.debug("traces:" + Util.toJSONPrettyString(traces));
				channelManager.getRestChannel().consumeAsync(traces);				
				totoalCount = totoalCount + traces.size();
			} 
			
			{
				List<Map<String, Object>> traces = createTraces(integrationIds, remainder);
				//logger.debug("traces:" + Util.toJSONPrettyString(traces));
				channelManager.getRestChannel().consumeAsync(traces);	
				totoalCount = totoalCount + traces.size();
			}
			
			logger.debug("created test message : " + totoalCount + " , value:" + val + ", remainder:" + remainder + ", commitCount:" + commitCount + ", count:" + count);
			res.put("msg", "ok");
			res.put("testCount", totoalCount);
			res.put("cd", 0);
			
		}catch(Exception e) {
			logger.error("exception test for traces:", e);
			res.put("msg", "fail");
			res.put("cd", 9);
			res.put("exception", e.getMessage());
			
		}
		
		return res;
		
	}
	  
	 
	
	private List<Map<String, Object>> createTraces(List<String> integrationIds, int count) throws InterruptedException {
		List<Map<String, Object>> traces = new ArrayList<Map<String, Object>>();
		for(int i = 0 ; i < count ; i ++) {
			
			String date1 = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI) + "000";
//			0 1 2 3 4 5
//		%   2 2 2 2 2 2
//		------------------
//		    0 1 0 1 0 1
		    
			String integrationId = integrationIds.get(i%integrationIds.size());
			
			Map trace1 = new HashMap<String, Object>();
			//required field
			trace1.put("integrationId"	, integrationId);
			trace1.put("originHostId"	, "host1");
			trace1.put("hostId"			, "host1");
			trace1.put("date"			, date1);
			trace1.put("processId"		, "process1");
			trace1.put("processDate"	, date1);
			trace1.put("processEndDate"	, date1);
			trace1.put("type"			, "SNDR");
			trace1.put("todoNodeCount"	, 1);
			trace1.put("status"			, "00");
			//option field
			trace1.put("errorCode"		, "");
			trace1.put("errorMessage"	, "");
			trace1.put("recordCount"	, "0");
			trace1.put("dataSize"		, "0");
			trace1.put("compress"		, "0");
			trace1.put("data"			, ""); 
			
			String date2 = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI) + "000";
			Map trace2 = new HashMap<String, Object>();
			trace2.put("integrationId"	, integrationId);
			trace2.put("originHostId"	, "host1");
			trace2.put("hostId"			, "host2");
			trace2.put("date"			, date1);
			trace2.put("processId"		, "process2");
			trace2.put("processDate"	, date2);
			trace2.put("processEndDate"	, date2);
			trace2.put("type"			, "BRKR");
			trace2.put("todoNodeCount"	, 1);
			trace2.put("status"			, "00");
			//option field
			trace2.put("errorCode"		, "");
			trace2.put("errorMessage"	, "");
			trace2.put("recordCount"	, "0");
			trace2.put("dataSize"		, "0");
			trace2.put("compress"		, "0");
			trace2.put("data"			, ""); 
			
			String date3 = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI) + "000";
			Map trace3 = new HashMap<String, Object>();
			trace3.put("integrationId"	, integrationId);
			trace3.put("originHostId"	, "host1");
			trace3.put("hostId"			, "host3");
			trace3.put("date"			, date1);
			trace3.put("processId"		, "process3");
			trace3.put("processDate"	, date3);
			trace3.put("processEndDate"	, date3);
			trace3.put("type"			, "RCVR");
			trace3.put("todoNodeCount"	, 1);
			String status = i%2 == 0 ? "99" : "00";
			trace3.put("status"			, status);
			//option field
			trace3.put("errorCode"		, "E1200");//5자리
			trace3.put("errorMessage"	, "JDBC ERROR: cant't get new Connection.");
			trace3.put("recordCount"	, "0");
			trace3.put("dataSize"		, "0");
			trace3.put("compress"		, "0");
			trace3.put("data"			, ""); 
			
			traces.add(trace1);
			traces.add(trace2);
			traces.add(trace3);
			
			
			Thread.sleep(1);
		}		

		return traces;
	}
	

}
