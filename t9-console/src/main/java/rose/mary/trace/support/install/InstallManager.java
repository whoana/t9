/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.support.install;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

import org.apache.commons.io.FileUtils;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.helper.module.mte.ILinkMsgHandler;
import rose.mary.trace.core.helper.module.mte.MQMsgHandler;
import rose.mary.trace.support.console.AnsiUtil;

/**
 * <pre>
 * rose.mary.trace.support.install
 * InstallManager.java
 * </pre>
 * 
 * @author whoana
 * @date Jan 30, 2020
 */
public class InstallManager {

	static File logFile;
	static String JAVA_HOME = "";
	static String INSTALL_SRC_DIR = "t9-install-linux";
	static String T9_HOME = "";
	static int T9_PORT = 8090;
	Scanner console = null;
	public static boolean checkOption = true;
	public static boolean runOption = false;

	public static void main(String[] args) {
		InstallManager im = new InstallManager();
		try {
			im.install();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void install() throws Exception {

		File logFileSrc = new File(System.getProperty("user.dir"), "t9-install.log");
		logFileSrc.deleteOnExit();

		INSTALL_SRC_DIR = System.getProperty("src.dir", INSTALL_SRC_DIR);

		console = new Scanner(System.in);
		println("> =====================================================================");
		println("> T9 설치를 시작합니다.");
		println("> " + Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT));
		println("> ---------------------------------------------------------------------");
		setHome();
		println("> ---------------------------------------------------------------------");
		setPort();
		println("> ---------------------------------------------------------------------");
		setJavaHome();
		println("> ---------------------------------------------------------------------");
		setJdbc();
		println("> ---------------------------------------------------------------------");
		setQmgr();
		println("> ---------------------------------------------------------------------");
		setScript();
		println("> ---------------------------------------------------------------------");
		setQuickstart();
		println("> ---------------------------------------------------------------------");
		println("> T9 설치를 완료하였습니다.");
		println("> " + Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT));

		File desc = new File(T9_HOME, "t9-install.log");
		copyFile(logFileSrc, desc);

		if (runOption) {
			Runtime.getRuntime().exec(T9_HOME + "/bin/run.sh"); // 이거 실행이 잘 안된다. 문제가 뭐지 , 202206
			println("> 설치위치[" + T9_HOME + "]의 t9을 실행하였습니다.");
			println("> 설치위치[" + T9_HOME + "]로 이동하여 quickstart.txt 파일을 읽어보세요.");
		} else {
			println("> 설치위치[" + T9_HOME + "]로 이동하여 quickstart.txt 파일을 읽어보고 t9을 실행해 보세요.");
		}

	}

	void setPort() {
		println("> [T9 서비스 포트 설정]");
		T9_PORT = inputInt(console, "> T9 서비스 PORT 를 입력해주세요.(기본값:8090) :", "> T9_PORT 값은 숫자를 입력해 주세요.");
		println("> T9_PORT: " + T9_PORT);
	}

	void setHome() {
		String userHome = System.getProperty("user.home");
		String userDir = System.getProperty("user.dir");

		println("> [T9_HOME 설정]");
		println("> 현재위치:" + userDir);
		println("> 사용자홈:" + userHome);
		while (true) {

			println("> 설치 디렉토리 T9_HOME을 입력해주세요.(입력된 디렉토리가 없는 디렉토리일 경우 생성됨, 설치계정에 쓰기권한 필요) :");
			printIn();
			String home = console.nextLine();
			println("> T9_HOME: " + home);

			println("> 설치를 진행할까요? (yes | no) :");
			printIn();
			String ok = console.nextLine();
			if (Util.isEmpty(ok) || "yes".equalsIgnoreCase(ok)) {
				println("> yes");
				try {
					File homeDir = new File(home);
					homeDir.deleteOnExit();
					homeDir.mkdirs();
					T9_HOME = homeDir.getAbsolutePath();
					File installFileDir = new File(userDir, INSTALL_SRC_DIR);
					copyDirectory(installFileDir, homeDir);
				} catch (FileNotFoundException e) {
					println("> 압축 해제된 설치파일 디렉토리 위치가 올바르지 않습니다. 설치 매니저 실행 시 설치파일 경로를 지정해서 실행해 보세요.");
					println("> 설치 예) 압축해제한 설치파일 위치가 ./t9-install-linux 라면, ");
					println("> java -jar -Dsrc.dir=./t9-install-linux ./t9-install-linux/lib/t9-console-1.0.0.jar -i");

					writeToLogFile(e);
					e.printStackTrace();
					continue;
				} catch (Exception e) {
					println("> 예외가 발생되었습니다. 처리 후 다시 시도해 주십시요. ");
					writeToLogFile(e);
					e.printStackTrace();
					continue;
				}
				println("> T9_HOME[" + T9_HOME + "] 이 세팅되었습니다.");
				break;
			} else {
				println("> " + ok);
				println("> 설치를 취소하였습니다. 설치매니저를 종료합니다.");
				System.exit(0);
				// continue;
			}
		}
	}

	void setJavaHome() {

		println("> [JAVA_HOME 설정]");

		while (true) {
			println("> JAVA_HOME를 입력해주세요:");
			// print("> 자바홈을 입력해주세요.(defaultJavaHome:" + defaultJavaHome + ") :");
			printIn();
			String javaHome = console.nextLine();
			println("> JAVA_HOME : " + javaHome);
			File home = null;
			try {
				home = new File(javaHome);
			} catch (Exception e) {
				println("> 예외가 발생되었습니다. 처리 후 다시 시도해 주십시요. ");
				writeToLogFile(e);
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
				writeToLogFile(e);
				e.printStackTrace();
				continue;
			}
			JAVA_HOME = javaHome;
			println("> JAVA_HOME 이 세팅되었습니다.");
			break;
		}
	}

	final static int DBMS_POSTGRES = 1;
	final static int DBMS_ORACLE = 2;

	void setJdbc() throws IOException {
		String dirverName = null;
		String url = null;
		String username = null;
		String password = null;
		println("> [JDBC 정보 설정]");

		// ORACLE, Postgres 분기처리 작성 after
		while (true) {
			println("> 사용할 DBMS 제품을 선택하세요.:");
			println("> 1) Postgres		2) Oracle  (숫자를 입력하세요.)");
			printIn();
			int dbmsType = console.nextInt();
			console.nextLine();
			if (dbmsType == DBMS_POSTGRES) {
				dirverName = "org.postgresql.Driver";
				println("> 선택된 DBMS : Postgres");
			} else if (dbmsType == DBMS_ORACLE) {
				dirverName = "oracle.jdbc.OracleDriver";
				println("> 선택된 DBMS : Oracle");
			} else {
				println("> 입력된 값  : " + dbmsType);
				println("> 현재 지원가능한 DBMS 제품은 1) Oracle 과 2) Postgres 입니다. 둘 중 하나를 선택하세요.");
				continue;
			}

			try {
				Class.forName(dirverName);
			} catch (Exception e) {
				writeToLogFile(e);
				e.printStackTrace();
				continue;
			}
			println("> JDBC dirverName: " + dirverName);
			break;
		}

		outer: while (true) {

			while (true) {
				println("> 트래킹적재 데이터베이스 JDBC URL을 입력해주세요. :");
				println("> ex) Postgres --> jdbc:postgresql://127.0.0.1:5432/iipdb");
				println("> ex) Oracle   --> jdbc:oracle:thin:@127.0.0.1:1521:IIP");
				printIn();
				url = console.nextLine();
				println("> url: " + url);

				println("> 데이터베이스 접속 username 을 입력해주세요. :");
				printIn();
				username = console.nextLine();
				println("> username: " + username);

				println("> 데이터베이스 접속 password 을 입력해주세요. :");
				printIn();
				password = console.nextLine();
				println("> password: " + password);

				if (checkOption)
					try {
						// Class.forName(dirverName);
						Connection con = DriverManager.getConnection(url, username, password);
						con.close();
					} catch (Exception e) {
						println("> JDBC 접속 테스트 예외가 발생되었습니다. 처리 후 다시 시도해 주십시요. ");
						writeToLogFile(e);
						e.printStackTrace();
						continue;
					}

				try {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("%driverClassName%", dirverName);
					params.put("%jdbcUrl%", url);
					params.put("%username%", username);
					params.put("%password%", password);
					params.put("%t9Port%", T9_PORT + "");
					File template = new File(T9_HOME, "/config/application.yml.tpl");
					File target = new File(T9_HOME, "/config/application.yml");
					replaceFileContents(template, target, params);
					template.deleteOnExit();
				} catch (Exception e) {
					println("> JDBC 접속 테스트 예외가 발생되었습니다. 처리 후 다시 시도해 주십시요. ");
					writeToLogFile(e);
					e.printStackTrace();
					break outer;
				}

				println("> 트래킹적재 데이터베이스 정보를 설정완료하였습니다.");
				break outer;
			}
		}

	}

	Map<String, Object> qmgrParams = new HashMap<String, Object>();

	final static int QMGR_WMQ = 1;
	final static int QMGR_ILIN = 2;

	void setQmgr() {
		String hostName = null;
		String qmgrName = null;
		int port = 0;
		String userId = null;
		String password = null;
		String channelName = null;
		String queueName = null;

		boolean wmqDisable = false;
		boolean iLinkDisable = true;
		println("> [큐매니저 정보 설정]");
		int qmgrType = 0;
		while (true) {
			println("> 사용할 QMGR 제품을 선택하세요.:");
			println("> 1) WebsphereMQ	2) ILink  (숫자를 입력하세요.)");
			printIn();
			qmgrType = console.nextInt();
			console.nextLine();
			if (qmgrType == QMGR_WMQ) {
				wmqDisable = false;
				iLinkDisable = true;
				println("> 선택된 QMGR 제품 : WebsphereMQ");
			} else if (qmgrType == QMGR_ILIN) {
				wmqDisable = true;
				iLinkDisable = false;
				println("> 선택된 QMGR 제품 : ILink");
			} else {
				println("> 입력된 값  : " + qmgrType);
				println("> 현재 지원가능한 DBMS 제품은 1) WebsphereMQ	2) ILink 입니다. 둘 중 하나를 선택하세요.");
				continue;
			}
			break;
		}

		while (true) {

			println("> 큐매니저 호스트명(주소)를 입력해주세요 :");
			printIn();
			hostName = console.nextLine();
			println("> hostName: " + hostName);

			println("> 큐매니저명을 입력해수세요 :");
			printIn();
			qmgrName = console.nextLine();
			println("> qmgrName: " + qmgrName);

			while (true) {
				println("> 큐매니저 접속 리스너 port 를 입력해주세요 :");
				printIn();
				try {
					port = console.nextInt();
					console.nextLine();
				} catch (java.util.InputMismatchException e) {
					println("> 리스너 port 는 숫자값 이어야합니다.");
				}
				println("> port: " + port);
				break;
			}
			println("> userId를 입력해주세요 :");
			printIn();
			userId = console.nextLine();
			println("> userId: " + userId);

			println("> password를 입력해주세요 :");
			printIn();
			password = console.nextLine();
			println("> password: " + password);

			println("> channelName을 입력해주세요 :");
			printIn();
			channelName = console.nextLine();
			println("> channelName: " + channelName);

			println("> queueName을 입력해주세요 :");
			printIn();
			queueName = console.nextLine();
			println("> queueName: " + queueName);

			if (checkOption) {
				if (qmgrType == QMGR_WMQ) {
					try {
						Hashtable<String, Object> params = new Hashtable<String, Object>();
						params.put(CMQC.CHANNEL_PROPERTY, channelName);
						params.put(CMQC.HOST_NAME_PROPERTY, hostName);
						params.put(CMQC.PORT_PROPERTY, new Integer(port));
						params.put(CMQC.USER_ID_PROPERTY, userId);
						params.put(CMQC.PASSWORD_PROPERTY, password);
						MQException.log = null;

						MQQueueManager qmgr = new MQQueueManager(qmgrName, params);
						int openOptions = CMQC.MQOO_INPUT_AS_Q_DEF + CMQC.MQOO_FAIL_IF_QUIESCING;
						MQQueue queue = qmgr.accessQueue(queueName, openOptions);
						queue = qmgr.accessQueue(queueName, openOptions);
						MQGetMessageOptions gmo = new MQGetMessageOptions();
						gmo.options = CMQC.MQGMO_PROPERTIES_FORCE_MQRFH2 + CMQC.MQGMO_FAIL_IF_QUIESCING
								+ CMQC.MQGMO_WAIT;
						queue.close();
						qmgr.close();

					} catch (Exception e) {
						println("> 큐매니저 접속 테스트 예외가 발생되었습니다. 올바른 정보를 확인후 다시 시도해 주십시요. ");
						writeToLogFile(e);
						e.printStackTrace();
						continue;
					}
				} else if (qmgrType == QMGR_ILIN) {
					try {
						ILinkMsgHandler handler = new ILinkMsgHandler(qmgrName, hostName, port, channelName, userId, password);
						handler.open(queueName, MQMsgHandler.Q_QPEN_OPT_GET);
						handler.close();
					} catch (Exception e) {
						println("> 큐매니저 접속 테스트 예외가 발생되었습니다. 올바른 정보를 확인후 다시 시도해 주십시요. ");
						writeToLogFile(e);
						e.printStackTrace();
						continue;
					}
				} else {
					println("> 설치 지원하지 않는 큐매니저 제품입니다.");
					continue;
				}

			}

			try {

				qmgrParams.put("%hostName%", hostName);
				qmgrParams.put("%qmgrName%", qmgrName);
				qmgrParams.put("%port%", port + "");
				qmgrParams.put("%userId%", userId);
				qmgrParams.put("%password%", password);
				qmgrParams.put("%channelName%", channelName);
				qmgrParams.put("%queueName%", queueName);
				qmgrParams.put("%wmqDisable%", wmqDisable + "");
				qmgrParams.put("%iLinkDisable%", iLinkDisable + "");

				File template = new File(T9_HOME, "/config/config.json.tpl");
				File target = new File(T9_HOME, "/config/config.json");
				replaceFileContents(template, target, qmgrParams);
				template.deleteOnExit();
			} catch (Exception e) {
				println("> 큐매니저 접속 정보 설정중 예외가 발생되었습니다. 개발자에게 문의해 주세요.");
				writeToLogFile(e);
				e.printStackTrace();
				continue;
			}

			println("> 큐매니저 정보 설정을 설정완료하였습니다.");
			break;

		}
	}

	void setScript() throws Exception {
		println("> [스크립트 설치]");
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("%T9_HOME%", T9_HOME);
			params.put("%JAVA_HOME%", JAVA_HOME);
			params.put("%T9_PORT%", T9_PORT + "");
			File template = new File(T9_HOME, "/bin/run.sh.tpl");
			File target = new File(T9_HOME, "/bin/run.sh");
			replaceFileContents(template, target, params);
			target.setExecutable(true);
			template.deleteOnExit();
		} catch (Exception e) {
			String msg = "run.sh 스크립트 설치시 예외 발생:";
			writeToLogFile(msg, e);
			throw new Exception(msg, e);
		}

		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("%T9_HOME%", T9_HOME);
			params.put("%JAVA_HOME%", JAVA_HOME);
			params.put("%T9_PORT%", T9_PORT + "");
			File template = new File(T9_HOME, "/bin/stop.sh.tpl");
			File target = new File(T9_HOME, "/bin/stop.sh");
			replaceFileContents(template, target, params);
			target.setExecutable(true);
			template.deleteOnExit();
		} catch (Exception e) {
			String msg = "stop.sh 스크립트 설치시 예외 발생:";
			writeToLogFile(msg, e);
			throw new Exception(msg, e);
		}

		try {
			qmgrParams.put("%T9_PORT%", T9_PORT + "");
			File template = new File(T9_HOME, "/bin/curl-wmq-msg.sh.tpl");
			File target = new File(T9_HOME, "/bin/curl-wmq-msg.sh");
			replaceFileContents(template, target, qmgrParams);
			target.setExecutable(true);
			template.deleteOnExit();
		} catch (Exception e) {
			String msg = "curl-wmq-msg.sh 스크립트 설치시 예외 발생:";
			writeToLogFile(msg, e);
			// throw new Exception(msg, e);
		}

		try {
			File testshell = new File(T9_HOME, "/bin/test.sh");
			testshell.setExecutable(true);
		} catch (Exception e) {
			String msg = "test.sh 스크립트 설치시 예외 발생:";
			writeToLogFile(msg, e);
			throw new Exception(msg, e);
		}

		try {
			File template = new File(T9_HOME, "/bin/tps.sh.tpl");
			File target = new File(T9_HOME, "/bin/tps.sh");
			replaceFileContents(template, target, qmgrParams);
			target.setExecutable(true);
			template.deleteOnExit();
		} catch (Exception e) {
			String msg = "tps.sh 스크립트 설치시 예외 발생:";
			writeToLogFile(msg, e);
			// throw new Exception(msg, e);
		}

		try {
			File testshell = new File(T9_HOME, "/bin/tps-test.sh");
			testshell.setExecutable(true);
		} catch (Exception e) {
			String msg = "tps-test.sh 스크립트 설치시 예외 발생:";
			writeToLogFile(msg, e);
			throw new Exception(msg, e);
		}

		try {
			File testshell = new File(T9_HOME, "/bin/diskspeed.sh");
			testshell.setExecutable(true);
		} catch (Exception e) {
			String msg = "diskspeed.sh 스크립트 설치시 예외 발생:";
			writeToLogFile(msg, e);
			throw new Exception(msg, e);
		}

		// Runtime.getRuntime().exec("chmod +x " + T9_HOME + "/bin/*.sh");
		println("> 스크립트 설치를 완료하였습니다.");
	}

	void setQuickstart() throws Exception {
		println("> [가이드 설치]");
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("%T9_HOME%", T9_HOME);
			File template = new File(T9_HOME, "/quickstart.txt.tpl");
			File target = new File(T9_HOME, "/quickstart.txt");
			replaceFileContents(template, target, params);
			template.deleteOnExit();
		} catch (Exception e) {
			String msg = "quickstart.txt 생성 예외 발생:";
			writeToLogFile(msg, e);
			throw new Exception(msg, e);
		}
		println("> 가이드 설치를 완료하였습니다.");
	}

	/**
	 * @param string
	 */
	public static void println(String msg) {
		String bgColor = AnsiUtil.ANSI_BG_BLACK;
		String fgColor = AnsiUtil.ANSI_BRIGHT_WHITE;
		System.out.println(fgColor + bgColor + " " + msg + " ");
		writeToLogFile(msg + System.lineSeparator());
	}

	/**
	 * @param string
	 */
	public static void print(String msg) {
		String bgColor = AnsiUtil.ANSI_BG_BLACK;
		String fgColor = AnsiUtil.ANSI_BRIGHT_WHITE;
		System.out.print(fgColor + bgColor + " " + msg + " ");
		writeToLogFile(msg);
	}

	public static void printIn() {
		print("<");
	}

	static void writeToLogFile(String msg) {
		try {
			if (logFile == null) {
				logFile = new File(".", "t9-install.log");
			}
			FileUtils.writeStringToFile(logFile, msg, "utf8", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void writeToLogFile(String msg, Exception e) {
		if (!Util.isEmpty(msg))
			writeToLogFile(msg + System.lineSeparator());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		e.printStackTrace(new PrintStream(bos));
		writeToLogFile(bos.toString() + System.lineSeparator());
	}

	static void writeToLogFile(Exception e) {
		writeToLogFile(null, e);
	}

	void copyDirectory(File sourceDirectory, File destinationDirectory) throws IOException {
		FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
	}

	void copyFile(File sourceFile, File destinationFile) throws IOException {
		FileUtils.copyFile(sourceFile, destinationFile);

	}

	void replaceFileContents(File template, File dest, Map<String, Object> params) throws IOException {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(template)));
			StringBuffer sb = new StringBuffer();
			do {
				String line = reader.readLine();
				if (line == null)
					break;
				sb.append(line).append(System.lineSeparator());
			} while (true);

			String src = sb.toString();

			for (String key : params.keySet()) {
				src = src.replaceAll(key, (String) params.get(key));
			}

			writer = new BufferedWriter(new FileWriter(dest));
			writer.write(src);
			writer.flush();

		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (writer != null)
					writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static int inputInt(Scanner console, String inputMsg, String typeErrorMsg) {

		while (true) {
			println(inputMsg);
			printIn();
			try {
				String var = null;
				if (console.hasNextLine())
					var = console.nextLine();
				return Util.isEmpty(var) || System.lineSeparator().equals(var) ? T9_PORT : Integer.parseInt(var);
			} catch (Exception e) {
				println(typeErrorMsg);
				continue;
			}
		}
	}

	public static String inputStr(Scanner console, String inputMsg, String typeErrorMsg) {

		while (true) {
			println(inputMsg);
			printIn();
			try {
				String output = null;
				if (console.hasNextLine())
					output = console.nextLine();
				if (Util.isEmpty(output))
					throw new Exception(typeErrorMsg);
			} catch (Exception e) {
				println(typeErrorMsg);
				continue;
			}
		}
	}

}
