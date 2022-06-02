/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.support.console;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * <pre>
 * rose.mary.trace.support.console
 * ANSI.java
 * </pre>
 * 
 * @author whoana
 * @date Jan 30, 2020
 */
public class AnsiUtil {

	public static final String ANSI_CLEAR = "\033[H\033[2J";
	public static final String ANSI_RESET = "\u001B[0m";

	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BRIGHT_BLACK = "\u001B[90m";
	public static final String ANSI_BRIGHT_RED = "\u001B[91m";
	public static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
	public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
	public static final String ANSI_BRIGHT_BLUE = "\u001B[94m";
	public static final String ANSI_BRIGHT_PURPLE = "\u001B[95m";
	public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
	public static final String ANSI_BRIGHT_WHITE = "\u001B[97m";

	public static final String[] FOREGROUNDS = {
			ANSI_BLACK,
			ANSI_RED,
			ANSI_GREEN,
			ANSI_YELLOW,
			ANSI_BLUE,
			ANSI_PURPLE,
			ANSI_CYAN,
			ANSI_WHITE,
			ANSI_BRIGHT_BLACK,
			ANSI_BRIGHT_RED,
			ANSI_BRIGHT_GREEN,
			ANSI_BRIGHT_YELLOW,
			ANSI_BRIGHT_BLUE,
			ANSI_BRIGHT_PURPLE,
			ANSI_BRIGHT_CYAN,
			ANSI_BRIGHT_WHITE
	};

	public static final String ANSI_BG_BLACK = "\u001B[40m";
	public static final String ANSI_BG_RED = "\u001B[41m";
	public static final String ANSI_BG_GREEN = "\u001B[42m";
	public static final String ANSI_BG_YELLOW = "\u001B[43m";
	public static final String ANSI_BG_BLUE = "\u001B[44m";
	public static final String ANSI_BG_PURPLE = "\u001B[45m";
	public static final String ANSI_BG_CYAN = "\u001B[46m";
	public static final String ANSI_BG_WHITE = "\u001B[47m";

	public static final String ANSI_BRIGHT_BG_BLACK = "\u001B[100m";
	public static final String ANSI_BRIGHT_BG_RED = "\u001B[101m";
	public static final String ANSI_BRIGHT_BG_GREEN = "\u001B[102m";
	public static final String ANSI_BRIGHT_BG_YELLOW = "\u001B[103m";
	public static final String ANSI_BRIGHT_BG_BLUE = "\u001B[104m";
	public static final String ANSI_BRIGHT_BG_PURPLE = "\u001B[105m";
	public static final String ANSI_BRIGHT_BG_CYAN = "\u001B[106m";
	public static final String ANSI_BRIGHT_BG_WHITE = "\u001B[107m";

	public static final String[] BACKGROUNDS = {
			ANSI_BG_BLACK,
			ANSI_BG_RED,
			ANSI_BG_GREEN,
			ANSI_BG_YELLOW,
			ANSI_BG_BLUE,
			ANSI_BG_PURPLE,
			ANSI_BG_CYAN,
			ANSI_BG_WHITE,
			ANSI_BRIGHT_BG_BLACK,
			ANSI_BRIGHT_BG_RED,
			ANSI_BRIGHT_BG_GREEN,
			ANSI_BRIGHT_BG_YELLOW,
			ANSI_BRIGHT_BG_BLUE,
			ANSI_BRIGHT_BG_PURPLE,
			ANSI_BRIGHT_BG_CYAN,
			ANSI_BRIGHT_BG_WHITE
	};

	public static String bline = "";
	public static int width = 100;
	public static String space = " ";

	static {
		for (int i = 0; i < width; i++) {
			bline = bline + space;
		}
	}

	public static void println(String fgColor, String bgColor, String msg) {

		int len = msg.getBytes().length;
		int padding = width - len > 0 ? width - len : 0;

		for (int i = 0; i < padding; i++) {
			msg = msg + space;
		}

		System.out.println(fgColor + bgColor + " " + bline + " ");
		System.out.println(fgColor + bgColor + " " + msg + " ");
		System.out.println(fgColor + bgColor + " " + bline + " ");

	}

	/**
	 * @param string
	 */
	public static void println(String msg) {
		String bgColor = AnsiUtil.ANSI_BG_BLACK;
		// String fgColor = AnsiUtil.ANSI_BRIGHT_GREEN;
		String fgColor = AnsiUtil.ANSI_BRIGHT_WHITE;
		System.out.println(fgColor + bgColor + " " + msg + " ");
	}

	void writeToFile(File file, String msg) throws IOException {
		FileUtils.writeStringToFile(file, msg, "utf8", true);
	}
	 

	/**
	 * @param string
	 */
	public static void print(String msg) {
		String bgColor = AnsiUtil.ANSI_BG_BLACK;
		// String fgColor = AnsiUtil.ANSI_BRIGHT_GREEN;
		String fgColor = AnsiUtil.ANSI_BRIGHT_WHITE;
		System.out.print(fgColor + bgColor + " " + msg + " ");
	}

	public static void printIn() {
		String bgColor = ANSI_BG_BLACK;
		String fgColor = ANSI_BRIGHT_WHITE;
		System.out.print(fgColor + bgColor + " " + "<" + " ");
	}

	public static void printIn(StringBuffer log) {
		printIn();
		log.append(" " + "<" + " ");
	}

	public static void printQuestion(String msg) {
		String fgColor = ANSI_BLACK;
		String bgColor = ANSI_BRIGHT_BG_YELLOW;
		println(fgColor, bgColor, msg);
	}

	public static void printWarning(String msg) {
		String fgColor = ANSI_BLACK;
		String bgColor = ANSI_BRIGHT_BG_RED;
		println(fgColor, bgColor, msg);
	}

	public static void printInformation(String msg) {
		String fgColor = ANSI_BLACK;
		String bgColor = ANSI_BG_WHITE;
		println(fgColor, bgColor, msg);
	}

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	public static void test() {
		System.out.println("\n  Default text\n");

		for (String fg : FOREGROUNDS) {
			for (String bg : BACKGROUNDS)
				System.out.print(fg + bg + "  TEST  ");
			System.out.println(ANSI_RESET);
		}

		System.out.println(ANSI_RESET + "\n  Back to default.\n");

		System.out.println(ANSI_CLEAR);
		System.out.flush();

		printInformation("printInform");
		printQuestion("printQuestion");
		printWarning("printWarning");
	}

	public static void main(String[] args) {
		test();
	}
}
