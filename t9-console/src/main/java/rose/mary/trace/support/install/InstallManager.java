/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.support.install;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

import javax.management.RuntimeOperationsException;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

import org.apache.commons.io.FileUtils;

import pep.per.mint.common.util.Util;
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

		INSTALL_SRC_DIR = System.getProperty("src.dir", INSTALL_SRC_DIR);

		console = new Scanner(System.in);
		println("> =====================================================================");
		println("> T9 설치를 시작합니다.");
		println("> " + Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT));
		println("> ---------------------------------------------------------------------");
		setHome();
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

		File src = new File(System.getProperty("user.dir"), "t9-install.log");
		src.deleteOnExit();
		File desc = new File(T9_HOME, "t9-install.log");
		copyFile(src, desc);

		if (runOption) {
			Runtime.getRuntime().exec(T9_HOME + "/bin/run.sh");
			println("> 설치위치[" + T9_HOME + "]의 t9을 실행하였습니다.");
			println("> 설치위치[" + T9_HOME + "]로 이동하여 quickstart.txt 파일을 읽어보세요.");
		} else {
			println("> 설치위치[" + T9_HOME + "]로 이동하여 quickstart.txt 파일을 읽어보고 t9을 실행해 보세요.");
		}

	}

	void setHome() {
		String userHome = System.getProperty("user.home");
		String userDir = System.getProperty("user.dir");

		println("> [T9_HOME 설정]");
		println("> 현재위치:" + userDir);
		println("> 사용자홈:" + userHome);
		while (true) {

			println("> 설치 디렉토리 T9_HOME을 입력해주세요.(존재하지 않으면 직접 생성, 쓰기권한 필요) :");
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

	void setJdbc() throws IOException {
		String dirverName = null;
		String url = null;
		String username = null;
		String password = null;
		println("> [JDBC 정보 설정]");
		outer: while (true) {

			while (true) {
				println("> JDBC Driver Class Name 을 입력해 주세요. :");
				println("> ex) org.postgresql.Driver");
				printIn();
				dirverName = console.nextLine();
				if (!"org.postgresql.Driver".equals(dirverName) || !"org.postgresql.Driver".equals(dirverName)) {
					println("> 현재 지원가능한 드라이버는 org.postgresql.Driver 와 오라클입니다.");
					continue;
				}
				if (checkOption)
					try {
						Class.forName(dirverName);
					} catch (Exception e) {
						println("> 현재 지원가능한 드라이버는 org.postgresql.Driver 와 오라클입니다.");
						writeToLogFile(e);
						e.printStackTrace();
						continue;
					}

				break;
			}
			println("> JDBC dirverName: " + dirverName);

			while (true) {
				println("> 트래킹적재 데이터베이스 JDBC URL을 입력해주세요. :");
				println("> ex) jdbc:postgresql://127.0.0.1:5432/iipdb");
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

	void setQmgr() {

		String hostName = null;
		String qmgrName = null;
		int port = 0;
		String userId = null;
		String password = null;
		String channelName = null;
		String queueName = null;

		println("> [큐매니저 정보 설정]");
		while (true) {

			println("> 큐매니저 호스트명(주소)를 입력해주세요 :");
			printIn();
			hostName = console.nextLine();
			println("> hostName: " + hostName);

			println("> 큐매니저명을 입력해수세요 :");
			printIn();
			qmgrName = console.nextLine();
			println("> qmgrName: " + qmgrName);

			println("> 큐매니저 접속 리스너 port 를 입력해주세요 :");
			printIn();
			port = console.nextInt();
			println("> port: " + port);

			println("> userId를 입력해주세요 :");
			printIn();
			userId = console.next();
			println("> userId: " + userId);

			println("> password를 입력해주세요 :");
			printIn();
			password = console.next();
			println("> password: " + password);

			println("> channelName을 입력해주세요 :");
			printIn();
			channelName = console.next();
			println("> channelName: " + channelName);

			println("> queueName을 입력해주세요 :");
			printIn();
			queueName = console.next();
			println("> queueName: " + queueName);

			Hashtable<String, Object> params = new Hashtable<String, Object>();

			params.put(CMQC.CHANNEL_PROPERTY, channelName);

			params.put(CMQC.HOST_NAME_PROPERTY, hostName);
			params.put(CMQC.PORT_PROPERTY, new Integer(port));

			params.put(CMQC.USER_ID_PROPERTY, userId);

			params.put(CMQC.PASSWORD_PROPERTY, password);

			MQException.log = null;

			if (checkOption)
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
					writeToLogFile(e);
					e.printStackTrace();
					continue;
				}

			try {

				Map<String, Object> maps = new HashMap<String, Object>();
				maps.put("%hostName%", hostName);
				maps.put("%qmgrName%", qmgrName);
				maps.put("%port%", port + "");
				maps.put("%userId%", userId);

				maps.put("%password%", password);
				maps.put("%channelName%", channelName);
				maps.put("%queueName%", queueName);

				File template = new File(T9_HOME, "/config/config.json.tpl");
				File target = new File(T9_HOME, "/config/config.json");
				replaceFileContents(template, target, maps);
				template.deleteOnExit();
			} catch (Exception e) {
				println("> 큐매니저 접속 테스트 예외가 발생되었습니다. 처리 후 다시 시도해 주십시요. ");
				writeToLogFile(e);
				e.printStackTrace();
				break;
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
			File template = new File(T9_HOME, "/bin/run.sh.tpl");
			File target = new File(T9_HOME, "/bin/run.sh");
			replaceFileContents(template, target, params);
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
			File template = new File(T9_HOME, "/bin/stop.sh.tpl");
			File target = new File(T9_HOME, "/bin/stop.sh");
			replaceFileContents(template, target, params);
			template.deleteOnExit();
		} catch (Exception e) {
			String msg = "stop.sh 스크립트 설치시 예외 발생:";
			writeToLogFile(msg, e);
			throw new Exception(msg, e);
		}

		Runtime.getRuntime().exec("chmod +x " + T9_HOME + "/bin/*.sh");
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

}
