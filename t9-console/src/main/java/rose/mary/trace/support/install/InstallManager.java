/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.support.install;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;


import static rose.mary.trace.support.console.AnsiUtil.*;

/**
 * <pre>
 * rose.mary.trace.support.install
 * InstallManager.java
 * </pre>
 * @author whoana
 * @date Jan 30, 2020
 */
public class InstallManager {

	Scanner console = null;
	 
	
	public void install() throws Exception {

		try {			
			console = new Scanner(System.in);
			
			String title = "트레킹 서버 설치를 시작합니다. 안내에 따라 설치를 진행해 주세요.";
			printInformation(title);
			
			String startTime = "설치시작시간 : " + new Date().toString();
			printInformation(startTime);
			
			String home1 = "설치 기본 위치를 입력해 주세요 (입력 예: /homes/users/tomi/itrace)";
			String home2 = "설치 기본 위치는 쓰기 권한이 주어져야 합니다. 설치위치에 해당하는 폴더가 존재하지 않으면 설치관리자가 폴더를 생성합니다.";
			String home3 = "";			
			printQuestion(home1);
			printInformation(home2);
			printInformation(home3);
			
			String home = console.next();
			while(home == null || home.trim().length() == 0||!checkHome(home)) {
				printWarning("입력한 정보가 적당하지 않습니다.");
				printQuestion(home1);
				printInformation(home2);
				printInformation(home3);
				home = console.next();				
			}
			
			printInformation("입력한 위치에 설치를 진행합니다.");
			printInformation("설치 위치:" + home);
			
		 }catch(Exception e) {		
			printWarning("설치 예외 발생");
			printWarning(e.getMessage());
			e.printStackTrace();
		 }finally {
			 if(console != null) console.close();	
			 String endTime = "설치종료시간 : " + new Date().toString();
			 printInformation(endTime);
		 }
	}


	/**
	 * @param home
	 * @return
	 */
	private boolean checkHome(String home) {
		try {
			File file = new File(home);
			return file.mkdirs();
		}catch(Exception e) {			
			return false;
		}
	}
	
	
	public static void main(String[] args) {

		
		 InstallManager im = null; try { im = new InstallManager(); im.install();
		 }catch(Exception e) { e.printStackTrace(); }
		

		 /*
		try {
			Path path = FileSystems.getDefault().getPath("/Users/whoana/DEV/workspace-tracking/projectq-support/a");
			//Path path = FileSystems.getDefault().getPath(".");
			Iterator<Path> paths = path.iterator();
			Path checkPath = FileSystems.getDefault().getPath("");
			for (Path path2 : path) {
				checkPath = FileSystems.getDefault().getPath(checkPath.toString() + File.separator + path2.toString());
				
				System.out.println("checkPath:" + checkPath.toString() + ", exist:" + Files.exists(checkPath) + ", isWritable:" + Files.isWritable(checkPath));
			}
			//System.out.println("exists" + Files.exists(path, LinkOption.NOFOLLOW_LINKS));
		}catch(Exception e) {
			e.printStackTrace();
		}
		*/
//		Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
//		for (Path name: dirs) {
//		    System.err.println(name);
//		}
	}

	
	
}
