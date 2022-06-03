/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.simulator;
 
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rose.mary.trace.apps.cache.CacheProxy;
import rose.mary.trace.data.common.InterfaceInfo;
import rose.mary.trace.helper.module.mte.ILinkMsgHandler;
import rose.mary.trace.helper.module.mte.MQMsgHandler;
import rose.mary.trace.helper.module.mte.MTEHeader;
import rose.mary.trace.helper.module.mte.MsgHandler;
import rose.mary.trace.util.IntCounter;
 

/**
 * <pre>
 * rose.mary.trace.test
 * TraceMsgGenerator.java
 * </pre>
 * @author whoana
 * @date Jul 23, 2019
 */
public class TraceMsgGenerator {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String MODULE_MQ 	= "w";
	public static final String MODULE_ILINK = "i";
	
	int port;					//-po
	String hostname; 			//-ho
	String qmgrName; 			//-qm
	String channelName; 		//-ch
	String queueName; 			//-qu
	String module;				//-mo
	int generateCount = 1000; 	//-ge
	int commitCount = 100;		//-co
	String userId; 
	String password;

	String data = "abcdefghijklmnop1234567890가나다라마바!@#$%^&*()";
	
	MsgHandler tmh;
	
	TraceMsgCreator tmc;
	
	Map<String, String> params;
	
	CacheProxy<String, InterfaceInfo> interfaceCache;
	
	List<String> interfaceList = new ArrayList<String>();
	
	IntCounter interfaceIndex = null;
	
	public TraceMsgGenerator(
		String hostname,
		int port,
		String qmgrName,
		String userId, 
		String password,
		String channelName,
		String queueName,
		String module,
		int generateCount,
		int commitCount,
		String data,
		TraceMsgCreator tmc,
		CacheProxy<String, InterfaceInfo> interfaceCache
	){
		this.hostname = hostname;
		this.port = port;
		this.qmgrName = qmgrName;
		this.userId = userId;
		this.password = password;
		this.channelName = channelName;
		this.queueName = queueName;
		this.module = module;
		this.generateCount = generateCount;
		this.commitCount = commitCount;
		this.data = data;
		this.tmc = tmc;
		this.interfaceCache = interfaceCache;
	}
	

	
	
	public void initialize() throws Exception{
		
		 
		if(interfaceCache != null) {
			Iterator<?> iterator = interfaceCache.iterator();
			
			iterator.forEachRemaining(new Consumer<Object>() {
				@Override
				public void accept(Object entry) {
					InterfaceInfo interfaceInfo = interfaceCache.getValue(entry);				
					interfaceList.add(interfaceInfo.getIntegrationId());
					//iterator.remove();
				}
			});
		}
		
		if(interfaceList.size() > 0) interfaceIndex = new IntCounter(0, interfaceList.size() - 1 , 1);
		
		tmc = tmc == null ? new DefaultTraceMsgCreator() : tmc;
		
		if(MsgHandler.MODULE_MQ.equalsIgnoreCase(module)) {
			tmh = new MQMsgHandler(qmgrName, hostname, port, channelName, userId, password, 1208, 1208, false, false);
		}else if(MODULE_ILINK.equalsIgnoreCase(module)) {
			tmh = new ILinkMsgHandler(qmgrName, hostname, port, channelName);
		}else {
			throw new Exception("NotFounMode:" + module);
		}
		tmh.open(queueName, MsgHandler.Q_OPEN_OPT_PUT);
		
		
		
		
	}
	
	List<String> keys = new ArrayList<String>();
	List<String> dups = new ArrayList<String>();
	public void generate() throws Throwable{
		try {
			int  putCount = 0;
			String interfaceId = null;
			String status = "00";
			for(int i = 0 ; i < generateCount ; i++) {
				//Thread.sleep(2);
				//interfaceId = tmc.createInterfaceId("INTF", i+1);
				interfaceId = ramdomInterfaceId("INTF", i+1);
				//logger.debug("generating interfaceId:"+interfaceId);
				status = tmc.createStatus(i+1);
				List<MTEHeader> headers = tmc.create(interfaceId, status);
				for(MTEHeader header : headers) {
				
					 
					String key =  header.getaIntfId() +"@"+ header.getaDate() + header.getaTime() +"@"+ header.getbHostId() +"@"+ header.getcProcessId();
					if(keys.contains(key)) {
						dups.add(key);
					}
					keys.add(key); 
					
					tmh.put(header, data);	
					putCount ++;
					if(putCount % commitCount == 0) { 
						System.out.println("msg committed(total:" + putCount + ")");
						tmh.commit();
					}
				}
			}

			 
			System.out.println("msg generator dup checking start.");
			for(String key : dups) {
				System.out.println(key);	 	 
			}
			System.out.println("msg generator dup checking end.");
			
			tmh.commit();
		}catch(Throwable t) {
			tmh.rollback();
			throw t;
		}finally {
			tmh.close();
		}
	}
 
	
	/**
	 * @return
	 */
	private String ramdomInterfaceId(String defaultPrefix, int index) {
		//int random = (int) Math.floor(Math.random() * interfaceList.size());
		String interfaceId = null;
		//logger.debug("interfaceList.size():" + interfaceList.size());
		if(interfaceList.size() > 0) {		
			//interfaceId = interfaceList.get(random);
			interfaceId = interfaceList.get(interfaceIndex.getAndIncrease());
		}else {
			interfaceId = tmc.createInterfaceId("INTF", index);
		}
		return interfaceId;
	}




	public static void main(String[] args) throws InterruptedException {

		try {
			int port;					//-po
			String hostname; 			//-ho
			String qmgrName; 			//-qm
			String userId;              //-uo
			String password;            //-ps
			String channelName; 		//-ch
			String queueName; 			//-qu
			String module;				//-mo
			int generateCount = 1000; 	//-ge
			int commitCount = 100;		//-co
			String data;				//-da
			String traceMsgCreator;     //-tr
			TraceMsgCreator tmc = null;
			
			Map<String, String> params = new HashMap<String, String>();
			if (args.length > 0 && (args.length % 2) == 0) {
				for (int i = 0; i < args.length; i += 2) {
					params.put(args[i], args[i + 1]);
				}
			} else {
				showHelp();
				throw new IllegalArgumentException();
			}
	
			if (checkParams(params)) {
				qmgrName = params.get("-qm");
				userId   = params.get("-uo");
				password = params.get("-ps");
				queueName = params.get("-qu");				
				hostname = params.get("-ho");
				channelName = params.get("-ch");
				module = params.get("-mo");
				data = params.get("-da");
				try {
					port = Integer.parseInt(params.get("-po"));
				} catch (NumberFormatException e) {
					port = 1414;
				}
				 
				try {
					generateCount = Integer.parseInt(params.get("-ge"));
				} catch (NumberFormatException e) {
					generateCount = 1000;
				}
				
				try {
					commitCount = Integer.parseInt(params.get("-co"));
				} catch (NumberFormatException e) {
					commitCount = 100;
				}
				
				traceMsgCreator = params.get("-tr");
				if(traceMsgCreator != null) {
					Class clazz = Class.forName(traceMsgCreator);
					tmc = (TraceMsgCreator)clazz.newInstance(); 
				}
				
			} else {
				showHelp();
				throw new IllegalArgumentException();
			}	
			 
			TraceMsgGenerator tmg = new TraceMsgGenerator(hostname, port, qmgrName, userId, password, channelName, queueName, module, generateCount, commitCount, data, tmc, null);
			
			System.out.println("------------------------------------------");
			System.out.println("- start task information");
			System.out.println("------------------------------------------");
			System.out.println(tmg.information());
			System.out.println("------------------------------------------");
			System.out.println("- initailizing . . .");
			tmg.initialize();
			
			System.out.println("- generating msgs");
			System.out.println("------------------------------------------");
			StopWatch sw = new StopWatch();
			sw.start();
			tmg.generate();
			sw.stop();
			System.out.println("------------------------------------------");
			System.out.println("- generated msg successly.(elapsed:" + sw.getTime() + " ms)");
			System.out.println("- end task");
			System.out.println("------------------------------------------");
		}catch (Throwable t) {
			System.out.println("------------------------------------------");
			System.out.println("- fail to generatd msgs.");
			System.out.println("------------------------------------------");
			t.printStackTrace();
		}
	
	}

	 

	/**
	 * 
	 */
	public String information() {
		StringBuffer info = new StringBuffer();
 	
		info.append("hostname:").append(hostname).append("\n");
		info.append("port:").append(port).append("\n");
		info.append("qmgrName:").append(qmgrName).append("\n");
		info.append("channelName:").append(channelName).append("\n");
		info.append("queueName:").append(queueName).append("\n");
		info.append("module:").append(module).append("\n");
		info.append("generateCount:").append(generateCount).append("\n");
		info.append("commitCount:").append(commitCount).append("\n");
		info.append("traceMsgCreator:").append(tmc.getClass().getName()).append("\n");
		info.append("data:").append(data).append("\n");
		return info.toString();
	}


	/**
	 * int port;					//-po
	 * String hostname; 			//-ho
	 * String qmgrName; 			//-qm
	 * String channelName; 		    //-ch
	 * String queueName; 			//-qu
	 * String module;				//-mo
 	 * int generateCount = 1000; 	//-ge
	 * int commitCount = 100;		//-co
	 * String data;				    //-da
	 * String traceMsgCreator;      //-tr
	 * 
	 * 
	 */
	private static void showHelp() {
		System.out.println("Subject:");
		System.out.println("\tTraceMsgGenerator 는 모코엠시스 EAI ASIS 트레킹 제품을 테스트를 위해 RFH2 헤더정의를 포함한 트레킹 메시지를 큐에 발생시키는 테스트 프로그램입니다.");
		System.out.println("How to use:");
		System.out.println("\tjava TraceMsgGenerator -po {port} -ho {hostname} -qm {qmgrName} -ch {channelName} -qu {queueName} -mo {module} -ge {generateCount} -co {commitCount} -tr {traceMsgCreator} -da {data}");
		System.out.println("Params:");
		System.out.println("\t-po : port 큐매니저 접속 포트");
		System.out.println("\t-ho : hostname 큐매니저 실행 서버 IP");
		System.out.println("\t-qm : qmgrName 큐매니저명");
		System.out.println("\t-ch : channelName 채널명");
		System.out.println("\t-qu : queueName 큐명");
		System.out.println("\t-mo : module 제품모듈(w:wmq, i:ilink)");
		System.out.println("\t-ge : generateCount 인터페이스발생회수");
		System.out.println("\t-co : commitCount 커밋주기(건수)");
		System.out.println("\t-da : data 큐메시지내용");
		System.out.println("\t-tr : traceMsgCreato 인터페이스 트레이스 헤더 정의 클래스");
		System.out.println("Example:");
		System.out.println("\tjava TraceMsgGenerator -po 58414 -ho 10.1.1.168 -qm TESTQM08 -ch SYSTEM.DEF.SVRCONN -qu TEST.LQ -mo w -ge 1000 -co 100 -tr rose.mary.trace.test.DefaultTraceMsgCreator -da 1234567890");
		
	}

	/**
	 * @return
	 */
	private static boolean checkParams(Map<String, String> params) {

		boolean ok = params.containsKey("-po") && 
					 params.containsKey("-ho") && 
					 params.containsKey("-qm") && 
					 params.containsKey("-ch") && 
					 params.containsKey("-qu") &&
					 params.containsKey("-mo") &&
					 params.containsKey("-ge") &&
					 params.containsKey("-co") &&
					 params.containsKey("-da") ;
		
		
		
		if (ok) {
			try { Integer.parseInt((String) params.get("-po")); } catch (NumberFormatException e) { ok = false; System.out.println("-po 파라미터는 숫자여야 합니다.");}
			try { Integer.parseInt((String) params.get("-ge")); } catch (NumberFormatException e) { ok = false; System.out.println("-ge 파라미터는 숫자여야 합니다.");}
			try { Integer.parseInt((String) params.get("-co")); } catch (NumberFormatException e) { ok = false; System.out.println("-co 파라미터는 숫자여야 합니다.");}
		}

		return ok;
	}

}
