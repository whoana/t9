package rose.mary.trace.support.console;
/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */


import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * <pre>
 * rose.mary.trace.console
 * SystemConsole.java
 * </pre>
 * @author whoana
 * @date Jan 22, 2020
 */
public class SystemConsoleUS {
	
	final static int CMD_EXIT  		= 0;
	final static int CMD_START 		= 1;
	final static int CMD_STOP  		= 2;
	final static int CMD_TPS   		= 3;
	final static int CMD_BOOT  		= 4;
	final static int CMD_SHUTDOWN 	= 5;
	final static int CMD_CHECK 		= 6;
	
	String baseUrl = "http://localhost:8090/trace/console/servers";
	 
	RestTemplate rest = new RestTemplate(); 

	Scanner console = null;

	public void start() {

		try {

			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(new MappingJackson2HttpMessageConverter());
			rest.setMessageConverters(messageConverters);

			
			console = new Scanner(System.in);
			while(true) {
				try {
					pageOver(20);			
					help();
					
					println("input command:");
					int cmd = console.nextInt();							
					println("your command:" + cmd);
					exec(cmd);
					
				}catch(Exception e) {
					e.printStackTrace();
					console.next();
				}
				
			}
		}catch(Exception e) {
			e.printStackTrace();
			if(console != null) console.close();
		}
	}
	
	public static void main(String[] args) {

		SystemConsoleUS console = null;
		try {
			console = new SystemConsoleUS();
			console.start();
		}catch(Exception e) {
			e.printStackTrace(); 
		}
	}

	public void help() {
		println("====================================================");
		println("- trace system command list");
		println("----------------------------------------------------");
		println("- 1.start trace");
		println("- 2.stop  trace");
		println("- 3.show tps");
		println("- 4.boot trace server");
		println("- 5.shutdown trace server");
		println("- 6.check server state");
		println("- 0.exit");
		println("----------------------------------------------------");
	}

	/**
	 * 
	 */
	public void pageOver(int line) {
		//for(int i = 0; i < line ; i ++) println("");
		try {
			Runtime.getRuntime().exec("clear");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	/**
	 * @param cmd
	 */
	private void exec(int cmd) {
		pageOver(100);
		println("- your command : " + cmd);
		println("- run command ... ");
		try {
			switch(cmd) {
			case  CMD_EXIT : 
				println("- bye bye...");
				System.exit(0); 
				break;
			case  CMD_START : 
				startTrace();
				break;
			case  CMD_STOP : 
				stopTrace();
				break;
			case  CMD_TPS : 
				showTps();
				break;
			case  CMD_SHUTDOWN : 
				shutdownTraceSystem();
				break;				
			case  CMD_BOOT : 
				bootTraceSystem();
				break;
			case  CMD_CHECK : 
				checkSystemState();				
				break;			
			default:
				println("- unsupported command");
				break;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

 
	

	/**
	 * 
	 */
	private void checkSystemState() {
		try {
			println("- -----------------------------------------------");				
			println("- begin request check system state ...");
			println("- -----------------------------------------------");
			String url = baseUrl + "/check";
			
			
			Map<?, ?> res = rest.getForObject(url, Map.class);
			
			int state = (Integer)res.get("cd");
			println("- success request system state");
			println("- state:" + state);
		}catch(ResourceAccessException e) {
			println("- error request system state");
			println("- msg  :the trace system server maybe shutdown. you must 4.boot trace server first.");
			
		}catch(Exception e) {
			println("- error request system state:");
			e.printStackTrace();
		}finally {
			println("- -----------------------------------------------");
			println("- end request system state");
		}
	}

	/**
	 * 
	 */
	private void startTrace() {
		try {
			println("- -----------------------------------------------");				
			println("- begin request start ...");
			println("- -----------------------------------------------");
			String url = baseUrl + "/start";
			String res = (String)rest.getForObject(url, String.class);
			println("- success request start");
			println("- res:" + res);
		}catch(ResourceAccessException e) {
			println("- error request system state");
			println("- msg  :the trace system server maybe shutdown. you must 4.boot trace server first.");			  
		}catch(Exception e) {
			println("- error request start:");
			e.printStackTrace();
		}finally {
			println("- -----------------------------------------------");
			println("- end request start");
		}
	}
	
	
	/**
	 * 
	 */
	private void stopTrace() {
		try {
			println("- -----------------------------------------------");
			println("- begin request stop ...");
			println("- -----------------------------------------------");
			String url = baseUrl + "/stop";
			String res = (String)rest.getForObject(url, String.class);
			println("- success request stop");
			println("- res:" + res);
		}catch(ResourceAccessException e) {
			println("- error request system state");
			println("- msg  :the trace system server maybe shutdown. you must 4.boot trace server first.");			 
		}catch(Exception e) {
			println("- error request stop:");
			e.printStackTrace();
		}finally {
			println("- -----------------------------------------------");
			println("- end request stop");
		}	
	}
	
	
	/**
	 * 
	 */
	private void shutdownTraceSystem() {
		try {
			println("- -----------------------------------------------");
			println("- begin request shutdown ...");
			println("- -----------------------------------------------");
			String url = baseUrl + "/shutdown";
			String res = (String)rest.getForObject(url, String.class);
			println("- success request shutdown");
			println("- res:" + res);
		}catch(ResourceAccessException e) {
			println("- error request system state");
			println("- msg  :the trace system server maybe shutdown. you must 4.boot trace server first.");
		}catch(Exception e) {
			println("- error request shutdown:");
			e.printStackTrace();
		}finally {
			println("- -----------------------------------------------");
			println("- end request shutdown");
		}
	}
	
	/**
	 * 
	 */
	private void showTps() {
		// TODO Auto-generated method stub
		
	}

	
	private void bootTraceSystem() {
		try {
			println("- -----------------------------------------------");
			println("- begin request boot ...");
			println("- -----------------------------------------------");
			println("- success request boot");
			String command = "/Users/whoana/git/projectq/run.sh";
			Process process = new ProcessBuilder(command).start();
		}catch(Exception e) {
			println("- error request boot:");
			e.printStackTrace();
		}finally {
			println("- -----------------------------------------------");
			println("- end request boot");
		}
	}

	/**
	 * @param string
	 */
	private void println(String msg) {
		String bg = AnsiUtil.ANSI_BG_BLACK;
		String fg = AnsiUtil.ANSI_BRIGHT_GREEN;
		
		System.out.println(fg + bg + " " + msg + " ");
	}
	
	
}
