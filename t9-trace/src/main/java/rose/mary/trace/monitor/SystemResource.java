/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.monitor;

import java.io.Serializable;

import pep.per.mint.common.util.Util;

/**
 * <pre>
 * rose.mary.trace.monitor
 * SystemResource.java
 * </pre>
 * @author whoana
 * @date Aug 23, 2019
 */
public class SystemResource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	/**
	 * @return the totalDiskAmt
	 */
	public double getTotalDiskAmt() {
		return totalDiskAmt;
	}
	/**
	 * @param totalDiskAmt the totalDiskAmt to set
	 */
	public void setTotalDiskAmt(double totalDiskAmt) {
		this.totalDiskAmt = totalDiskAmt;
	}
	/**
	 * @return the usedDiskAmt
	 */
	public double getUsedDiskAmt() {
		return usedDiskAmt;
	}
	/**
	 * @param usedDiskAmt the usedDiskAmt to set
	 */
	public void setUsedDiskAmt(double usedDiskAmt) {
		this.usedDiskAmt = usedDiskAmt;
	}
	/**
	 * @return the idleDiskAmt
	 */
	public double getIdleDiskAmt() {
		return idleDiskAmt;
	}
	/**
	 * @param idleDiskAmt the idleDiskAmt to set
	 */
	public void setIdleDiskAmt(double idleDiskAmt) {
		this.idleDiskAmt = idleDiskAmt;
	}
	/**
	 * @return the usedDiskPct
	 */
	public double getUsedDiskPct() {
		return usedDiskPct;
	}
	/**
	 * @param usedDiskPct the usedDiskPct to set
	 */
	public void setUsedDiskPct(double usedDiskPct) {
		this.usedDiskPct = usedDiskPct;
	}
	/**
	 * @return the idleDiskPct
	 */
	public double getIdleDiskPct() {
		return idleDiskPct;
	}
	/**
	 * @param idleDiskPct the idleDiskPct to set
	 */
	public void setIdleDiskPct(double idleDiskPct) {
		this.idleDiskPct = idleDiskPct;
	}
	/**
	 * @return the usedCpuPct
	 */
	public double getUsedCpuPct() {
		return usedCpuPct;
	}
	/**
	 * @param usedCpuPct the usedCpuPct to set
	 */
	public void setUsedCpuPct(double usedCpuPct) {
		this.usedCpuPct = usedCpuPct;
	}
	/**
	 * @return the idleCpuPct
	 */
	public double getIdleCpuPct() {
		return idleCpuPct;
	}
	/**
	 * @param idleCpuPct the idleCpuPct to set
	 */
	public void setIdleCpuPct(double idleCpuPct) {
		this.idleCpuPct = idleCpuPct;
	}
	/**
	 * @return the totalMemAmt
	 */
	public double getTotalMemAmt() {
		return totalMemAmt;
	}
	/**
	 * @param totalMemAmt the totalMemAmt to set
	 */
	public void setTotalMemAmt(double totalMemAmt) {
		this.totalMemAmt = totalMemAmt;
	}
	/**
	 * @return the idleMemAmt
	 */
	public double getIdleMemAmt() {
		return idleMemAmt;
	}
	/**
	 * @param idleMemAmt the idleMemAmt to set
	 */
	public void setIdleMemAmt(double idleMemAmt) {
		this.idleMemAmt = idleMemAmt;
	}
	/**
	 * @return the usedMemPct
	 */
	public double getUsedMemPct() {
		return usedMemPct;
	}
	/**
	 * @param usedMemPct the usedMemPct to set
	 */
	public void setUsedMemPct(double usedMemPct) {
		this.usedMemPct = usedMemPct;
	}
	/**
	 * @return the idleMemPct
	 */
	public double getIdleMemPct() {
		return idleMemPct;
	}
	/**
	 * @param idleMemPct the idleMemPct to set
	 */
	public void setIdleMemPct(double idleMemPct) {
		this.idleMemPct = idleMemPct;
	}
	/**
	 * @return the javaVersion
	 */
	public String getJavaVersion() {
		return javaVersion;
	}
	/**
	 * @param javaVersion the javaVersion to set
	 */
	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}
	/**
	 * @return the javaVendor
	 */
	public String getJavaVendor() {
		return javaVendor;
	}
	/**
	 * @param javaVendor the javaVendor to set
	 */
	public void setJavaVendor(String javaVendor) {
		this.javaVendor = javaVendor;
	}
	/**
	 * @return the javaHome
	 */
	public String getJavaHome() {
		return javaHome;
	}
	/**
	 * @param javaHome the javaHome to set
	 */
	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}
	/**
	 * @return the javaClassVersion
	 */
	public String getJavaClassVersion() {
		return javaClassVersion;
	}
	/**
	 * @param javaClassVersion the javaClassVersion to set
	 */
	public void setJavaClassVersion(String javaClassVersion) {
		this.javaClassVersion = javaClassVersion;
	}
	/**
	 * @return the javaClassPath
	 */
	public String getJavaClassPath() {
		return javaClassPath;
	}
	/**
	 * @param javaClassPath the javaClassPath to set
	 */
	public void setJavaClassPath(String javaClassPath) {
		this.javaClassPath = javaClassPath;
	}
	/**
	 * @return the osName
	 */
	public String getOsName() {
		return osName;
	}
	/**
	 * @param osName the osName to set
	 */
	public void setOsName(String osName) {
		this.osName = osName;
	}
	/**
	 * @return the osArch
	 */
	public String getOsArch() {
		return osArch;
	}
	/**
	 * @param osArch the osArch to set
	 */
	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}
	/**
	 * @return the osVersion
	 */
	public String getOsVersion() {
		return osVersion;
	}
	/**
	 * @param osVersion the osVersion to set
	 */
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the userHome
	 */
	public String getUserHome() {
		return userHome;
	}
	/**
	 * @param userHome the userHome to set
	 */
	public void setUserHome(String userHome) {
		this.userHome = userHome;
	}
	/**
	 * @return the userDir
	 */
	public String getUserDir() {
		return userDir;
	}
	/**
	 * @param userDir the userDir to set
	 */
	public void setUserDir(String userDir) {
		this.userDir = userDir;
	}
	
	@Override
	public String toString() {
		return Util.toJSONString(this);
	}
	
	
}
