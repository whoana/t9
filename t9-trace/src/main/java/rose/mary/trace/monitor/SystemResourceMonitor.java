/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.monitor;

import java.io.File;
import java.lang.management.ManagementFactory;
//import com.sun.management.OperatingSystemMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * <pre>
 * rose.mary.trace.monitor
 * ResourceMonitor.java
 * </pre>
 * 
 * @author whoana
 * @date Aug 14, 2019
 */

public class SystemResourceMonitor extends Monitor<SystemResource> {

	
	
	OperatingSystemMXBean osb = null;

	double totalDiskAmt;
	double usedDiskAmt;
	double idleDiskAmt;
	double usedDiskPct;
	double idleDiskPct;

	double usedCpuPct;
	double idleCpuPct;

	double totalMemAmt;
	double idleMemAmt;
	double usedMemPct;
	double idleMemPct;

	String javaVersion;
	String javaVendor;
	String javaHome;
	String javaClassVersion;
	String javaClassPath;
	String osName;
	String osArch;
	String osVersion;
	String userName;
	String userHome;
	String userDir;
	
	SystemResource systemResource;
	
	/*
	java.version       : Java 버전
	java.vendor        : Java 공급자
	java.vendor.url    : Java 공급자 URL
	java.home          : Java가 위치한 디렉터리
	java.class.version : Java 클래스의 버전 ( 48(1.4), 49(1.5), 50(1.6), 51(1.7), 52(1.8) ...
	java.class.path    : App ClassLoader에 로딩된 클래스 경로
	java.ext.dir       : Ext ClassLoader에 로드할 클래스가 위치한 경로
	os.name            : OS명 의 이름
	os.arch            : OS 아키텍처
	os.version         : OS 버전
	file.separator     : 파일 구분자 /(Unix계열), \(Windows) ※ File 클래스의 separator변수(String) 을 사용해도 된다.
	path.separator     : 경로 구분자. :(Unix계열), ;(Windows) ※ File 클래스의 separator변수(String) 을 사용해도 된다.
	line.separator     : 개행 문자 0x0A(LF, Unix계열), 0x0D0x0A(CR/LF, Windows) ※ Unix 계열은 개행문자가 1Byte, Windows는 2Bytes 명심
	user.name          : 사용자 계정명
	user.home          : 사용자 홈 디렉토리
	user.dir           : 현재 작업 디렉토리
	*/
	
	
	public static Long getSystemCpuLoad() throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{ 
		 
		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		if (Class.forName("com.sun.management.OperatingSystemMXBean").isInstance(os)) {
			Method memorySize = os.getClass().getDeclaredMethod("getSystemCpuLoad");
			memorySize.setAccessible(true);
			return (Long) memorySize.invoke(os);
		}else{
			return 0L;
		}
		 
	}


	/**
	 * @param watchTime
	 */
	public SystemResourceMonitor(int watchTime) {
		super(watchTime);
		
		javaVersion = System.getProperty("java.version");      
		javaVendor  = System.getProperty("java.vendor");          
		javaHome    = System.getProperty("java.home");         
		javaClassVersion = System.getProperty("java.class.version");
		javaClassPath = System.getProperty("java.class.path");   
		osName   = System.getProperty("os.name");           
		osArch    = System.getProperty("os.arch");           
		osVersion = System.getProperty("os.version");        
		userName  = System.getProperty("user.name");         
		userHome  = System.getProperty("user.home");         
		userDir   = System.getProperty("user.dir");          
		try {
			osb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		}catch(Throwable t) {
			logger.warn("com.sun.management.OperatingSystemMXBean 이 지원되지 않는 환경입니다.", t);
		}
	}

	@Override
	public SystemResource watch() {
		systemResource = new SystemResource();
		
		if(osb != null) {
			double load = 0;
			
			while (true) {
				load = osb.getSystemCpuLoad();
				try {
					Thread.sleep(watchTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (load * 100.0 > 0.0 || load * 100.0 < 100.0)
					break;
			}
	
			usedCpuPct = load * 100.0;
			idleCpuPct = 100.0 - load * 100.0;
	
			totalDiskAmt = 0.0;
			usedDiskAmt = 0.0;
			idleDiskAmt = 0.0;
	
			for (File f : File.listRoots()) {
				totalDiskAmt = totalDiskAmt + (f.getTotalSpace() / (1024 * 1024) / 1000.0);
				usedDiskAmt = usedDiskAmt + ((f.getTotalSpace() - f.getUsableSpace()) / (1024 * 1024) / 1000.0);
				idleDiskAmt = idleDiskAmt + (f.getUsableSpace() / (1024 * 1024) / 1000.0);
			}
	
			usedDiskPct = 100.0 * (totalDiskAmt - idleDiskAmt) / totalDiskAmt;
			idleDiskPct = 100.0 - usedDiskPct;
	 
			totalMemAmt = (double) (osb.getTotalPhysicalMemorySize() / (1024 * 1024) / 1000.0);
			idleMemAmt = (double) (osb.getFreePhysicalMemorySize() / (1024 * 1024) / 1000.0);
			idleMemPct = 100.0 * idleMemAmt / totalMemAmt;
			usedMemPct = 100.0 - idleMemPct; 

			systemResource.setUsedCpuPct(usedCpuPct);
			systemResource.setIdleCpuPct(idleCpuPct);
			systemResource.setTotalDiskAmt(totalDiskAmt);
			systemResource.setUsedDiskAmt(usedDiskAmt);
			systemResource.setIdleDiskAmt(idleDiskAmt);
			systemResource.setUsedDiskPct(usedDiskPct);
			systemResource.setIdleDiskPct(idleDiskPct);
			systemResource.setTotalMemAmt(totalMemAmt);
			systemResource.setIdleMemAmt(idleMemAmt);
			systemResource.setIdleMemPct(idleMemPct);
			systemResource.setUsedMemPct(usedMemPct);
		}
		
		
		systemResource.setJavaVersion(javaVersion);      
		systemResource.setJavaVendor(javaVendor);          
		systemResource.setJavaHome(javaHome);         
		systemResource.setJavaClassVersion(javaClassVersion);
		systemResource.setJavaClassPath(javaClassPath);   
		systemResource.setOsName(osName);           
		systemResource.setOsArch(osArch);           
		systemResource.setOsVersion(osVersion);        
		systemResource.setUserName(userName);         
		systemResource.setUserHome(userHome);         
		systemResource.setUserDir(userDir);     
		
		
		
		
		
//		System.out.println("------------------------------------");
//		System.out.println("--DISK");
//		System.out.println("------------------------------------");
//		System.out.println("totalDiskAmt" + getTotalDiskAmt());
//		System.out.println("usedDiskAmt" + getUsedDiskAmt());
//		System.out.println("idleDiskAmt" + getIdleDiskAmt());
//		System.out.println("usedDiskPct" + getUsedDiskPct());
//		System.out.println("idleDiskPct" + getIdleDiskPct());
//		System.out.println("------------------------------------");
//		System.out.println("--CPU");
//		System.out.println("------------------------------------");
//		System.out.println("usedCpuPct" + getUsedCpuPct());
//		System.out.println("idleCpuPct" + getIdleCpuPct());
//		System.out.println("------------------------------------");
//		System.out.println("--MEMORY");
//		System.out.println("------------------------------------");
//		System.out.println("totalMemAmt" + getTotalMemAmt());
//		System.out.println("idleMemAmt" + getIdleMemAmt());
//		System.out.println("usedMemPct" + getUsedMemPct());
//		System.out.println("idleMemPct" + getIdleMemPct());
		
		
		return systemResource;
	}

	/**
	 * @return the totalDiskAmt
	 */
	public double getTotalDiskAmt() {
		return totalDiskAmt;
	}

	/**
	 * @return the usedDiskAmt
	 */
	public double getUsedDiskAmt() {
		return usedDiskAmt;
	}

	/**
	 * @return the idleDiskAmt
	 */
	public double getIdleDiskAmt() {
		return idleDiskAmt;
	}

	/**
	 * @return the usedDiskPct
	 */
	public double getUsedDiskPct() {
		return usedDiskPct;
	}

	/**
	 * @return the idleDiskPct
	 */
	public double getIdleDiskPct() {
		return idleDiskPct;
	}

	/**
	 * @return the usedCpuPct
	 */
	public double getUsedCpuPct() {
		return usedCpuPct;
	}

	/**
	 * @return the idleCpuPct
	 */
	public double getIdleCpuPct() {
		return idleCpuPct;
	}

	/**
	 * @return the totalMemAmt
	 */
	public double getTotalMemAmt() {
		return totalMemAmt;
	}

	/**
	 * @return the idleMemAmt
	 */
	public double getIdleMemAmt() {
		return idleMemAmt;
	}

	/**
	 * @return the usedMemPct
	 */
	public double getUsedMemPct() {
		return usedMemPct;
	}

	/**
	 * @return the idleMemPct
	 */
	public double getIdleMemPct() {
		return idleMemPct;
	}
	
	

	/**
	 * @return the osb
	 */
	public OperatingSystemMXBean getOsb() {
		return osb;
	}

	/**
	 * @return the javaVersion
	 */
	public String getJavaVersion() {
		return javaVersion;
	}

	/**
	 * @return the javaVendor
	 */
	public String getJavaVendor() {
		return javaVendor;
	}

	/**
	 * @return the javaHome
	 */
	public String getJavaHome() {
		return javaHome;
	}

	/**
	 * @return the javaClassVersion
	 */
	public String getJavaClassVersion() {
		return javaClassVersion;
	}

	/**
	 * @return the javaClassPath
	 */
	public String getJavaClassPath() {
		return javaClassPath;
	}

	/**
	 * @return the osName
	 */
	public String getOsName() {
		return osName;
	}

	/**
	 * @return the osArch
	 */
	public String getOsArch() {
		return osArch;
	}

	/**
	 * @return the osVersion
	 */
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return the userHome
	 */
	public String getUserHome() {
		return userHome;
	}

	/**
	 * @return the userDir
	 */
	public String getUserDir() {
		return userDir;
	}

	public static void main(String args[]) {
		 
		SystemResourceMonitor rm = new SystemResourceMonitor(1000);

		rm.start();
		
		
		
		
		for(int i = 0 ; i < 10 ; i ++) {
			try { 
				System.out.println("------------------------------------");
				System.out.println("--DISK");
				System.out.println("------------------------------------");
				System.out.println("totalDiskAmt: " + rm.getTotalDiskAmt());
				System.out.println("usedDiskAmt: "  + rm.getUsedDiskAmt());
				System.out.println("idleDiskAmt: "  + rm.getIdleDiskAmt());
				System.out.println("usedDiskPct: "  + rm.getUsedDiskPct());
				System.out.println("idleDiskPct: "  + rm.getIdleDiskPct());
				System.out.println("------------------------------------");
				System.out.println("--CPU");
				System.out.println("------------------------------------");				
				System.out.println("usedCpuPct: " + rm.getUsedCpuPct());
				System.out.println("idleCpuPct: " + rm.getIdleCpuPct());
				System.out.println("------------------------------------");
				System.out.println("--MEMORY");
				System.out.println("------------------------------------");
				System.out.println("totalMemAmt: " + rm.getTotalMemAmt());
				System.out.println("idleMemAmt: "  + rm.getIdleMemAmt());
				System.out.println("usedMemPct: "  + rm.getUsedMemPct());
				System.out.println("idleMemPct: "  + rm.getIdleMemPct());
				System.out.println();
				System.out.println();
				Thread.sleep(1000);
				
				
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}
		}

	}
	
	public SystemResource getSystemResource() {
		return systemResource;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
 
}
