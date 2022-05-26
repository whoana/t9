package rose.mary.trace.support.console;

import java.io.BufferedReader;

/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import pep.per.mint.common.util.Util;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

/**
 * <pre>
 * rose.mary.trace.console
 * SystemConsole.java
 * -------------------------------
 * TO-DO
 * 1.트래킹 서버 환경설정 콘솔명령 추가(MQ, JDBC 설정) 
 * 2.다국어 처리
 * 3.환경 설정
 * 4.인스톨 처리
 * </pre>
 * 
 * @author whoana
 * @date Jan 22, 2020
 */
public class SystemConsole {
	final static int CMD_INSTALL = 0;
	final static int CMD_BOOT = 1;
	final static int CMD_SHUTDOWN = 2;
	final static int CMD_START = 3;
	final static int CMD_STOP = 4;
	final static int CMD_TPS = 5;
	final static int CMD_CHECK = 6;
	final static int CMD_TEST = 7;
	final static int CMD_EXIT = 9;

	String baseUrl;

	String port = "8090";

	String consoleHome = ".";

	Properties properties;

	RestTemplate rest = null;

	Scanner console = null;

	int readTimeout = 3;
	int connectTimeout = 3;

	/**
	 * @param home
	 * @param port
	 */
	public SystemConsole(String home, String port) {
		printTitle();
		baseUrl = "http://localhost:" + port + "/trace/console/servers";
		consoleHome = home;

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(connectTimeout * 1000); // 3 초(연결에 대한 타임아웃)
		factory.setReadTimeout(readTimeout * 30 * 1000); // 3 초(데이터 읽기에 대한 타임아웃)
		rest = new RestTemplate(factory);

		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		rest.setMessageConverters(messageConverters);

	}

	/**
	 * 
	 */
	private void printTitle() {
		try {
			InputStream is = ClassLoader.getSystemResource("title.txt").openStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int b = 0;
			while (true) {
				b = is.read();
				if (b == -1)
					break;
				baos.write(b);
			}

			String title = baos.toString("UTF-8");
			// AnsiUtil.printInformation(title);
			println(title);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
	}

	public static void main(String[] args) {
		SystemConsole console = null;
		try {
			String home = System.getProperty("console.home", System.getenv("user.dir"));
			String port = System.getProperty("console.port", "8090");
			console = new SystemConsole(home, port);
			console.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {

		try {

			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(new MappingJackson2HttpMessageConverter());
			rest.setMessageConverters(messageConverters);

			console = new Scanner(System.in);

			println("> 트레킹 콘솔을 실행합니다.");

			waitFor(500);
			while (true) {
				try {
					help();
					print("> 리스트에서 명령 번호를 선택해 주세요 :");
					int cmd = console.nextInt();
					exec(cmd);

					//
					// while(true) {
					// System.console().printf(" > 계속하려면 엔터를 입력해주세요.[Enter]");
					// String newLine = console.next();
					// System.out.println(":["+newLine +"]");
					// if(newLine.trim().equals("\n")) {
					// AnsiUtil.clearScreen();
					// break;
					// }
					// }
					//
				} catch (Exception e) {
					e.printStackTrace();
					console.next();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			if (console != null)
				console.close();
		}
	}

	/**
	 * 
	 * @param ms
	 */
	private void waitFor(long ms) {
		System.out.print(" > ");
		for (int i = 0; i < ms; i = i + 100) {
			try {
				Thread.sleep(i);
				System.out.print(".");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		System.out.print("\n");

	}

	public void help() {

		printCmd("> [콘솔 리스트]");
		printCmd("> [0.설치 / 1.서버시작 / 2.서버종료 / 3.트래킹시작 / 4.트레킹종료 / 5.TPS보기 / 6.서버상태 / 9.콘솔종료]");
		System.out.println(AnsiUtil.ANSI_RESET);
	}

	/**
	 * @param cmd
	 */
	private void exec(int cmd) {

		try {
			switch (cmd) {
				case CMD_INSTALL:
					println("> T9 트래킹서버 설치를 진행합니다.");
					install();
					break;
				case CMD_EXIT:
					println("> 콘솔을 종료합니다.");
					waitFor(500);
					System.exit(0);
					break;
				case CMD_START:
					println("> 트래킹 시작 명령을 요청합니다.");
					startTrace(500);

					break;
				case CMD_STOP:
					while (true) {
						print("> 트래킹을 종료하시겠습니까? (yes/no):");
						String subCmd = console.next();
						if ("no".equalsIgnoreCase(subCmd)) {
							println("> 트래킹을 종료 요청을 취소합니다.");
							break;
						} else if ("yes".equalsIgnoreCase(subCmd)) {
							waitFor(500);
							stopTrace();
							break;
						} else if (subCmd.trim().length() == 0) {
							waitFor(500);
							stopTrace();
							break;
						} else {
							continue;
						}
					}

					break;
				case CMD_TPS:
					println("> 트래킹 TPS 보기 명령을 요청합니다.");
					waitFor(500);
					showTps();
					break;
				case CMD_SHUTDOWN:

					while (true) {
						print("> 서버를 종료하시겠습니까? (yes/no):");
						String subCmd = console.next();
						if ("no".equalsIgnoreCase(subCmd)) {
							println("> 서버 종료를 취소합니다.");
							break;
						} else if ("yes".equalsIgnoreCase(subCmd)) {
							waitFor(500);
							shutdownTraceSystem();
							break;
						} else if (subCmd.trim().length() == 0) {
							waitFor(500);
							shutdownTraceSystem();
							break;
						} else {
							continue;
						}
					}

					break;
				case CMD_BOOT:
					println("> 서버 시작 명령을 요청합니다.");
					bootTraceSystem(2000);

					break;
				case CMD_CHECK:
					println("> 서버 상태 조회 명령을 요청합니다.");
					waitFor(500);
					checkSystemState();
					break;
				case CMD_TEST:

					println("> 트레이스 메시지 발생 명령을 요청합니다.");
					waitFor(500);
					testTraceMsgCreate();
					break;
				default:
					println("> 지원되지 않는 명령입니다. ");
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	private void install() {
		println("> 트레킹서버 설치를 시작합니다.");
		setHome();
		setJavaHome();
		setJdbc();
		setQmgr();
	}

	static String T9_HOME = "";

	void setHome() {
		// String javaVersion = System.getProperty("java.version");
		// String javaVendor = System.getProperty("java.vendor");
		// String defaultJavaHome = System.getProperty("java.home");
		// String javaClassVersion = System.getProperty("java.class.version");
		// String javaClassPath = System.getProperty("java.class.path");
		// String osName = System.getProperty("os.name");
		// String osArch = System.getProperty("os.arch");
		// String osVersion = System.getProperty("os.version");
		// String userName = System.getProperty("user.name");
		String userHome = System.getProperty("user.home");
		String userDir = System.getProperty("user.dir");

		println("> [T9_HOME 설정]");
		println("> userDir:" + userDir);
		println("> userHome:" + userHome);
		while (true) {

			println("> 설치 디렉토리 T9_HOME을 입력해주세요 (존재하지 않으면 직접 생성합니다. 설치작업에는 쓰기권한이 필요합니다.) :");
			String home = console.next();
			println("> home: " + home);
			print("> 입력한 위치에 설치를 진행할까요? (yes | no) :");
			String ok = console.next();
			if ("yes".equalsIgnoreCase(ok)) {
				try {
					File homeDir = new File(home);
					homeDir.mkdirs();
					homeDir.exists();
					T9_HOME = homeDir.getAbsolutePath();

					File installHome = new File(T9_HOME);
					File installFiles = new File(userDir, "install");
					File[] files = installFiles.listFiles();
					for (File file : files) {
						println("> " + file.toString());
						file.renameTo(new File(installHome, file.getName()));
						installFiles.deleteOnExit();
					}
					// File installHome = new File(T9_HOME);

				} catch (Exception e) {
					println("> 예외가 발생되었습니다. 처리 후 다시 시도해 주십시요. ");
					e.printStackTrace();
					continue;
				}
				println("> T9_HOME[" + T9_HOME + "] 이 세팅되었습니다.");
				break;
			} else {
				println("> 설치를 취소하였습니다.");
				continue;
			}
		}
	}

	void setJdbc() {

		println("> [JDBC 정보 설정]");
		outer: while (true) {

			String dirverName = null;
			while (true) {
				println("> JDBC Driver Class Name 을 입력해 주세요. :");
				println("> ex) org.postgresql.Driver");
				dirverName = console.next();
				if (!"org.postgresql.Driver".equals(dirverName) || !"org.postgresql.Driver".equals(dirverName)) {
					print("> 현재 지원가능한 드라이버는 org.postgresql.Driver 와 오라클입니다.");
					continue;
				}
				break;
			}
			println("> JDBC dirverName: " + dirverName);

			while (true) {
				println("> 트래킹적재 데이터베이스 JDBC URL을 입력해주세요. :");
				println("> ex) jdbc:postgresql://127.0.0.1:5432/iipdb");
				String url = console.next();
				println("> url: " + url);

				print("> 데이터베이스 접속 username 을 입력해주세요. :");
				String username = console.next();
				println("> username: " + username);

				print("> 데이터베이스 접속 password 을 입력해주세요. :");
				String password = console.next();
				println("> password: " + password);

				try {
					// String url = "jdbc:postgresql://10.10.1.10:5432/iipdb";
					// Properties properties = new Properties();
					// properties.setProperty(" driver-class-name", "org.postgresql.Driver");
					// properties.setProperty("username", "iip");
					// properties.setProperty("password", "iip");
					Class.forName(dirverName);
					Connection con = DriverManager.getConnection(url, username, password);
					con.close();
				} catch (Exception e) {
					println("> JDBC 접속 테스트 예외가 발생되었습니다. 처리 후 다시 시도해 주십시요. ");
					e.printStackTrace();
					continue;
				}

				println("> 트래킹적재 데이터베이스 정보를 설정완료하였습니다.");
				break outer;
			}
		}

	}

	void setQmgr() {
		// "hostName": "10.10.1.10",
		// "qmgrName": "TEST_HANA_QM",
		// "port": 1414,
		// "userId": "mqm",
		// "password": "mqm",
		// "channelName": "SYSTEM.DEF.SVRCONN",
		// "queueName": "TRACE.EQ",

		println("> [큐매니저 정보 설정]");
		while (true) {

			print("> 큐매니저호스트명(주소)를 입력해수세요 :");
			String hostName = console.next();
			println("> hostName: " + hostName);

			print("> 큐매니저명을 입력해수세요 :");
			String qmgrName = console.next();
			println("> qmgrName: " + qmgrName);

			print("> 큐매니저 접속 리스너 port 를 입력해수세요 :");
			int port = console.nextInt();
			println("> port: " + port);

			print("> userId를 입력해수세요 :");
			String userId = console.next();
			println("> userId: " + userId);

			print("> password를 입력해수세요 :");
			String password = console.next();
			println("> password: " + password);

			print("> channelName을 입력해수세요 :");
			String channelName = console.next();
			println("> channelName: " + channelName);

			print("> queueName을 입력해수세요 :");
			String queueName = console.next();
			println("> queueName: " + queueName);

			Hashtable<String, Object> params = new Hashtable<String, Object>();

			params.put(CMQC.CHANNEL_PROPERTY, channelName);

			params.put(CMQC.HOST_NAME_PROPERTY, hostName);
			params.put(CMQC.PORT_PROPERTY, new Integer(port));

			params.put(CMQC.USER_ID_PROPERTY, userId);

			params.put(CMQC.PASSWORD_PROPERTY, password);

			MQException.log = null;

			try {
				MQQueueManager qmgr = new MQQueueManager(qmgrName, params);
				int openOptions = CMQC.MQOO_INPUT_AS_Q_DEF + CMQC.MQOO_FAIL_IF_QUIESCING;
				MQQueue queue = qmgr.accessQueue(queueName, openOptions);
				queue = qmgr.accessQueue(queueName, openOptions);
				MQGetMessageOptions gmo = new MQGetMessageOptions();
				gmo.options = CMQC.MQGMO_PROPERTIES_FORCE_MQRFH2 + CMQC.MQGMO_FAIL_IF_QUIESCING + CMQC.MQGMO_WAIT;
				queue.close();
				qmgr.close();

			} catch (Exception e) {
				println("> 큐매니저 접속 테스트 예외가 발생되었습니다. 올바른 정보를 확인후 다시 시도해 주십시요. ");
				e.printStackTrace();
				continue;
			}

			println("> 큐매니저 정보 설정을 설정완료하였습니다.");
			break;

		}
	}

	void setJavaHome() {

		String javaVersion = System.getProperty("java.version");
		String javaVendor = System.getProperty("java.vendor");
		String defaultJavaHome = System.getProperty("java.home");
		String javaClassVersion = System.getProperty("java.class.version");
		String javaClassPath = System.getProperty("java.class.path");
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		String osVersion = System.getProperty("os.version");
		String userName = System.getProperty("user.name");
		String userHome = System.getProperty("user.home");
		String userDir = System.getProperty("user.dir");

		println("> [JAVA_HOME 정보 설정]");

		while (true) {
			print("> JAVA_HOME를 입력해주세요.");
			// print("> 자바홈을 입력해주세요.(defaultJavaHome:" + defaultJavaHome + ") :");
			String javaHome = console.next();
			println("> home: " + javaHome);
			File home = null;
			try {
				home = new File(javaHome);
			} catch (Exception e) {
				println("> 예외가 발생되었습니다. 처리 후 다시 시도해 주십시요. ");
				e.printStackTrace();
				continue;
			}

			if (!home.exists()) {
				println("> 존재하지 않는 디렉토리입니다.");
				continue;

			}

			try {
				Runtime.getRuntime().exec(javaHome + "/bin/java -version");
			} catch (Exception e) {
				println("> 예외가 발생되었습니다. 처리 후 다시 시도해 주십시요. ");
				e.printStackTrace();
				continue;
			}
			println("> JAVA_HOME 이 세팅되었습니다.");
			break;
		}
	}

	public final static int SERVER_STATE_INIT = 0;
	public final static int SERVER_STATE_STOP = 1;
	public final static int SERVER_STATE_START = 2;
	public final static int SERVER_STATE_SHUTDOWN = 3;
	public final static int SERVER_STATE_UNKNOWN = 9;

	/**
	 *  
	 */
	private void checkSystemState() {
		try {
			String url = baseUrl + "/check";
			Map<?, ?> res = rest.getForObject(url, Map.class);
			int state = (Integer) res.get("cd");

			if (SERVER_STATE_INIT == state) {
				println("> 서버 초기화 상태입니다.(트래킹 서버를 처음 기동한 상태이며 트래킹을 시작하지 않은 상태입니다.)(state:" + state + ")");
			} else if (SERVER_STATE_STOP == state) {
				println("> 트래킹 종료 상태입니다.(state:" + state + ")");
			} else if (SERVER_STATE_START == state) {
				println("> 트래킹 시작 상태입니다.(state:" + state + ")");
			} else if (SERVER_STATE_SHUTDOWN == state) {
				println("> 서버 종료 상태입니다.(state:" + state + ")");
			} else {
				println("> 정의되지 않은 상태입니다.(state:" + state + ")");
			}
		} catch (org.springframework.web.client.HttpServerErrorException.InternalServerError e) {
			println("> 서버 에러가 발생하였습니다.");
			println("> status code:" + e.getRawStatusCode() + " , msg:" + e.getResponseBodyAsString());
		} catch (ResourceAccessException e) {
			println("> 트래킹 서버에 접속할 수 없습니다.");
			println("> 먼저 1.트래킹서버시작 을 실행시켜주십시오.");

		} catch (Exception e) {
			println("> 다음과 같은 에러가 발생하였습니다.");
			e.printStackTrace();
		} finally {
			println("> 서버 상태 조회를 마침니다.");
		}
	}

	private int checkServerState() {
		try {
			String url = baseUrl + "/check";
			Map<?, ?> res = rest.getForObject(url, Map.class);
			int state = (Integer) res.get("cd");
			return state;
		} catch (ResourceAccessException e) {
			return SERVER_STATE_UNKNOWN;
		} catch (Exception e) {
			return SERVER_STATE_UNKNOWN;
		}
	}

	private void testTraceMsgCreate() {
		try {
			String url = baseUrl + "/generate/msgs/100";
			// 타임아웃 재설정
			// {
			// HttpComponentsClientHttpRequestFactory factory = new
			// HttpComponentsClientHttpRequestFactory();
			// factory.setConnectTimeout(connectTimeout * 1000); //3 초(연결에 대한 타임아웃)
			// factory.setReadTimeout(readTimeout * 30 * 1000); //3 초(데이터 읽기에 대한 타임아웃)
			// rest = new RestTemplate(factory);
			// }

			Map<?, ?> res = rest.getForObject(url, Map.class);
			String msg = (String) res.get("msg");
			println("> " + msg);
		} catch (Exception e) {
			println("> 다음과 같은 에러가 발생하였습니다.");
			e.printStackTrace();
		} finally {
			println("> 메시지 발생 요청을 마침니다.");
		}

	}

	public static final int CD_SUCCESS = 0;
	public static final int CD_FAIL = 1;

	/**
	 * 
	 */
	private void startTrace(long wait) {
		try {
			String url = baseUrl + "/start";
			Map<?, ?> res = rest.getForObject(url, Map.class);
			waitFor(wait);
			int cd = (Integer) res.get("cd");
			if (cd == CD_SUCCESS) {
				println("> 트레킹 시작 요청을 성공하였습니다.(cd:" + cd + ")");
			} else {
				println("> 트레킹 시작에 문제가 발생하였습니다.(cd:" + cd + ")");
				String msg = (String) res.get("msg");
				println("> 예외 : " + msg);
			}
		} catch (ResourceAccessException e) {
			println("> 트래킹 서버에 접속할 수 없습니다.");
			println("> 먼저 1.트래킹서버시작 을 실행시켜주십시오.");
		} catch (Exception e) {
			println("> 다음과 같은 에러가 발생하였습니다.");
			e.printStackTrace();
		} finally {
			println("> 트래킹 시작 요청을 마침니다.");
		}
	}

	/**
	 * 
	 */
	private void stopTrace() {
		try {
			String url = baseUrl + "/stop";
			Map<?, ?> res = rest.getForObject(url, Map.class);
			int cd = (Integer) res.get("cd");
			if (cd == CD_SUCCESS) {
				println("> 트레킹 종료 요청을 성공하였습니다.(cd:" + cd + ")");
			} else {
				println("> 트레킹 종료에 문제가 발생하였습니다.(cd:" + cd + ")");
				String msg = (String) res.get("msg");
				println("> 예외 : " + msg);
			}
		} catch (ResourceAccessException e) {
			println("> 트래킹 서버에 접속할 수 없습니다.");
			println("> 먼저 4.트래킹서버시작 을 실행시켜주십시오.");
		} catch (Exception e) {
			println("> 다음과 같은 에러가 발생하였습니다.");
			e.printStackTrace();
		} finally {
			println("> 트래킹 종료 요청을 마침니다.");
		}
	}

	/**
	 * 
	 */
	private void shutdownTraceSystem() {

		try {
			String command = consoleHome + File.separator + "bin" + File.separator + "stop.sh";
			Process process = new ProcessBuilder(command).start();
			println("> 트레킹서버를 종료하였습니다.");
		} catch (Exception e) {
			println("> 다음과 같은 에러가 발생하였습니다.");
			e.printStackTrace();
		} finally {
			println("> 트래킹서버 종료 요청을 마침니다.");
		}

	}

	/**
	 * 
	 */
	private void shutdownTraceSystemRemote() {

		try {
			String url = baseUrl + "/shutdown";
			Map<?, ?> res = rest.getForObject(url, Map.class);
			// int cd = (Integer)res.get("cd");
			// if(cd == CD_SUCCESS) {
			// println("> 트레킹서버 종료 요청을 성공하였습니다.(cd:" + cd + ")");
			// } else {
			// println("> 트레킹서버 종료에 문제가 발생하였습니다.(cd:" + cd + ")");
			// String msg = (String)res.get("msg");
			// println("> 예외 : " + msg);
			// }
		} catch (ResourceAccessException e) {
			println("> 트래킹 서버에 접속할 수 없습니다.");
			println("> 먼저 4.트래킹서버시작 을 실행시켜주십시오.");
		} catch (Exception e) {
			println("> 다음과 같은 에러가 발생하였습니다.");
			e.printStackTrace();
		} finally {
			println("> 트래킹서버 종료 요청을 마침니다.");
		}

	}

	/**
	 * 
	 */
	private void showTps() {
		try {
			String url = baseUrl + "/tps";
			Map<?, ?> res = rest.getForObject(url, Map.class);
			int cd = (Integer) res.get("cd");
			if (cd == CD_SUCCESS) {
				int tps = (Integer) res.get("tps");
				println("> 현재 TPS:" + tps);
			} else {
				println("> TPS 정보를 조회하는데 문제가 발생했습니다.(cd:" + cd + ")");
				String msg = (String) res.get("msg");
				println("> 예외 : " + msg);
			}
		} catch (ResourceAccessException e) {
			println("> 트래킹 서버에 접속할 수 없습니다.");
			println("> 먼저 14.트래킹서버시작 을 실행시켜주십시오.");
		} catch (Exception e) {
			println("> 다음과 같은 에러가 발생하였습니다.");
			e.printStackTrace();
		} finally {
			println("> TPS조회 요청을 마침니다.");
		}
	}

	int bootWait = 5;

	private void bootTraceSystem(long wait) {
		try {
			int state = checkServerState();
			if (state == SERVER_STATE_UNKNOWN) {
				String command = consoleHome + File.separator + "bin" + File.separator + "run.sh";
				Process process = new ProcessBuilder(command).start();
				waitFor(wait);

				do {
					// println("> 서버 상태체크를 요청합니다.");
					state = checkServerState();
					// println("> 서버 상태체크 요청을 완료하였습니다.");

					if (state == SERVER_STATE_INIT) {
						println("> 트래킹 시작 명령을 요청합니다.");
						startTrace(500);
						break;
					} else if (state == SERVER_STATE_UNKNOWN) {
						println("> 아직 서버가 시작되지 않았습니다. 서버 시작 지연이 계속될 경우 서버 로그를 점검하고 설정값들을 확인해 보세요.[콘솔 나가기:Control+c]");
					}
					Thread.sleep(bootWait * 1000);
				} while (true);

				println("> 트레킹서버를 실행하였습니다.");
			} else {
				println("> 트래킹서버가 이미 실행 중입니다.");
			}

		} catch (Exception e) {
			println("> 다음과 같은 에러가 발생하였습니다.");
			e.printStackTrace();
		} finally {
			println("> 트래킹서버 시작 요청을 마침니다.");
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

	/**
	 * @param string
	 */
	private void print(String msg) {
		String bg = AnsiUtil.ANSI_BG_BLACK;
		String fg = AnsiUtil.ANSI_BRIGHT_GREEN;

		System.out.print(fg + bg + " " + msg + " ");
	}

	private void printCmd(String msg) {
		String bg = AnsiUtil.ANSI_BG_BLUE;
		String fg = AnsiUtil.ANSI_BRIGHT_GREEN;

		System.out.println(fg + bg + " " + msg + " ");

	}

}
