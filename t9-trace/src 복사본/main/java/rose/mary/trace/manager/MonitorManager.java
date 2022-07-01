/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rose.mary.trace.core.data.monitor.SystemMonitorData;
import rose.mary.trace.core.monitor.SystemResource;
import rose.mary.trace.core.monitor.SystemResourceMonitor;
import rose.mary.trace.core.monitor.TPS;
import rose.mary.trace.core.monitor.ThroughputMonitor;
import rose.mary.trace.core.monitor.listener.MonitorListener;
import rose.mary.trace.core.monitor.listener.event.MonitorEvent;

/**
 * <pre>
 * MonitorManager
 * 모니터링 구간별 모니터 객체를 관리한다.
 * </pre>
 * @author whoana
 * @since Aug 7, 2019
 */
public class MonitorManager {
	
	Logger logger = LoggerFactory.getLogger(MonitorManager.class);
	
	/**
	 * 
	 */
	SystemResourceMonitor srm; 
	
	/**
	 * Monitor for T1 area(channel) 
	 */
	ThroughputMonitor tpm1; 
	/**
	 * Monitor for T2 area(loading trace) 
	 */
	ThroughputMonitor tpm2; 
	/**
	 * Monitor for T3 area(translate bot) 
	 */
	ThroughputMonitor tpm3; 
	
	/**
	 * Monitor for T4 area(loading bot) 
	 */
	ThroughputMonitor tpm4; 
	
	public MonitorManager(SystemResourceMonitor srm, ThroughputMonitor tpm1, ThroughputMonitor tpm2, ThroughputMonitor tpm3, ThroughputMonitor tpm4) {
		this.srm  = srm;
		this.tpm1 = tpm1;
		this.tpm2 = tpm2;
		this.tpm3 = tpm3;
		this.tpm4 = tpm4;
		
	}
	
	public void startMonitors() {
		startSystemResourceMonitor();
		startThroughputMonitor1();
		startThroughputMonitor2();
		startThroughputMonitor3();
		startThroughputMonitor4();
	}
	
	public void startSystemResourceMonitor() {
		if(srm != null) {
			srm.setMonitorListener(new MonitorListener<SystemResource>() {				
				@Override
				public void watch(MonitorEvent<SystemResource> event) throws Exception {
					logger.info("watch monitor event :SystemResource: " + event.getData());
				}
			});
			
			srm.start();
		}
	}
	
	public void startThroughputMonitor1() {
		  
		if(tpm1 != null) {
			 
			tpm1.setMonitorListener(new MonitorListener<TPS>() {

				@Override
				public void watch(MonitorEvent<TPS> event) throws Exception {
					logger.info("watch monitor event :TPS1: " + event.getData());
				}
			});
			tpm1.start();
			
		}
	}
	
	public void startThroughputMonitor2() {
		
		if(tpm2 != null) {
			tpm2.setMonitorListener(new MonitorListener<TPS>() {

				@Override
				public void watch(MonitorEvent<TPS> event) throws Exception {
					logger.info("watch monitor event :TPS2: " + event.getData());
				}
			});
			tpm2.start();
		}
	}
	
	public void startThroughputMonitor3() {
		
		if(tpm3 != null) {
			tpm3.setMonitorListener(new MonitorListener<TPS>() {

				@Override
				public void watch(MonitorEvent<TPS> event) throws Exception {
					logger.info("watch monitor event :TPS3: " + event.getData());
				}
			});
			tpm3.start();
		}
	}
	
	public void startThroughputMonitor4() {
		
		if(tpm4 != null) {
			tpm4.setMonitorListener(new MonitorListener<TPS>() {

				@Override
				public void watch(MonitorEvent<TPS> event) throws Exception {
					logger.info("watch monitor event :TPS4: " + event.getData());
				}
			});
			tpm4.start();
		}
	}


	public SystemMonitorData getSystemMonitorData() {
		
		SystemMonitorData smd = new SystemMonitorData();
		
		if(srm != null) {
			smd.setTotalDiskAmt(srm.getTotalDiskAmt());
			smd.setUsedDiskAmt(srm.getUsedDiskAmt());
			smd.setIdleDiskAmt(srm.getIdleDiskAmt());
			smd.setUsedDiskPct(srm.getUsedDiskPct());
			smd.setIdleDiskPct(srm.getIdleDiskPct());
			smd.setUsedCpuPct(srm.getUsedCpuPct());
			smd.setIdleCpuPct(srm.getIdleCpuPct());
			smd.setTotalMemAmt(srm.getTotalMemAmt());
			smd.setIdleMemAmt(srm.getIdleMemAmt());
			smd.setUsedMemPct(srm.getUsedMemPct());
			smd.setIdleMemPct(srm.getIdleMemPct());
			smd.setJavaVersion(srm.getJavaVersion());
			smd.setJavaVendor(srm.getJavaVendor());
			smd.setJavaHome(srm.getJavaHome());
			smd.setJavaClassVersion(srm.getJavaClassVersion());
			//smd.setJavaClassPath(srm.getJavaClassPath());
			smd.setOsName(srm.getOsName());
			smd.setOsArch(srm.getOsArch());
			smd.setOsVersion(srm.getOsVersion());
			smd.setUserName(srm.getUserName());
			smd.setUserHome(srm.getUserHome());
			smd.setUserDir(srm.getUserDir());
		}
		
		if(tpm1 != null) {
			smd.setTps(tpm1.getTps(1000));			
		}
		
		return smd;
	}

	
	
	public TPS getTps1() {
		return tpm1.watch();
	}

	public TPS getTps2() {
		return tpm2.watch();
	}
	
	public TPS getTps3() {
		return tpm3.watch();
	}

	public TPS getTps4() {
		return tpm4.watch();
	}
	
	/**
	 * 
	 */
	public void stopMonitors() {
		stopSystemResourceMonitor();
		stopThroughputMonitor1();
		stopThroughputMonitor2();	
		stopThroughputMonitor3();
		stopThroughputMonitor4();
	}
	
	
	/**
	 * 
	 */
	public void stopSystemResourceMonitor() {
		if(srm != null) {
			srm.stop();
		}		
	}
	
	public void stopThroughputMonitor1() {
		if(tpm1 != null) {
			tpm1.stop();
		
		} 
	}
	
	public void stopThroughputMonitor2() {
		if(tpm2 != null) {
			tpm2.stop();
		
		} 
	}
	
	public void stopThroughputMonitor3() {
		if(tpm3 != null) {
			tpm3.stop();
		
		} 
	}
	
	public void stopThroughputMonitor4() {
		if(tpm4 != null) {
			tpm4.stop();
		
		} 
	}
 
	 
}
