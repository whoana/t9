/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace;

import java.io.File;

import javax.sql.DataSource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;

import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.data.common.RuntimeInfo;
import rose.mary.trace.core.envs.Variables;
import rose.mary.trace.core.monitor.SystemResource;
import rose.mary.trace.core.monitor.SystemResourceMonitor;
import rose.mary.trace.manager.CacheManager;
import rose.mary.trace.manager.ConfigurationManager;
import rose.mary.trace.manager.DatabasePolicyHandlerManager;
import rose.mary.trace.manager.InterfaceCacheManager;
import rose.mary.trace.manager.ServerManager;
import rose.mary.trace.system.SystemLogger;



/**
 * <pre>
 * The Application is a spring boot main application
 * </pre>
 * 
 * @author whoana
 * @since Aug 7, 2019
 */
@EnableAspectJAutoProxy
@RestController
@SpringBootApplication
@ImportResource("classpath:trace-context.xml")
@ComponentScan({ "rose.mary.trace" })
public class T9
implements CommandLineRunner, ApplicationListener<ContextClosedEvent>, InitializingBean, DisposableBean {
	
	public static RunMode runMode = RunMode.Server;
	
	static ConfigurableApplicationContext ctx;
	 

	@Autowired
	MessageSource messageSource;

	@Autowired
	SystemResourceMonitor srm;

	@Autowired
	DatabasePolicyHandlerManager databasePolicyHandlerManager;

	@Autowired
	ServerManager serverManager;

	// @Autowired
	// DistributorManager distributorManager;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	RuntimeInfo runtimeInfo;

	@Autowired
	InterfaceCacheManager interfaceCacheManager;

	@Autowired
	ConfigurationManager configurationManager;

	@Autowired
	DataSource dataSource;

	@Override
	public void destroy() throws Exception {
		SystemLogger.info("good-bye t9.");
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {

		try {
			// traceServer.stop();
			serverManager.stopServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cacheManager.closeCache();
		sayEnding();
	}
 


	@Override
	public void run(String... args) throws Exception {

		SystemLogger.info("args:" + Util.toJSONString(args));

		String mode = System.getProperty("rose.mary.run.mode", "server");
		if(mode.equals(RunMode.Server.getName())){
			runMode = RunMode.Server;
		}else if(mode.equals(RunMode.Distributor.getName())){
			runMode = RunMode.Distributor;
		}else if(mode.equals(RunMode.Recovery.getName())){
			runMode = RunMode.Recovery;
		}
 
		SystemLogger.info("RunMode:" + mode);

		String databaseName = JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseProductName");
		if (StringUtils.hasLength(databaseName)) {
			SystemLogger.info("databaseName:" + databaseName);
			Variables.databaseName = databaseName;
		}

		sayStartingMsg();
		afterBoot();
	}

	/**
	 * 
	 */
	private void afterBoot() throws Exception {

		switch(runMode){
			case Server : 
				{
					if (configurationManager.getServerManagerConfig().isStartOnBoot()){
						serverManager.startServer();
					}else{
						SystemLogger.info("getServerManagerConfig().isStartOnBoot():false");
					}
					databasePolicyHandlerManager.start();
				}
				break;
		    case Distributor : 
				{
					if (configurationManager.getServerManagerConfig().isStartOnBoot()){
						serverManager.startServer();
					}else{
						SystemLogger.info("getServerManagerConfig().isStartOnBoot():false");
					}
					databasePolicyHandlerManager.start();
				}
				break;
			case Recovery : 
				try {
					SystemLogger.info("recovery mode starting, not implemented yet");
					// ToDo: not implemented yet
					// recoveryHandler.start();
				} finally {
		
				}
				break;
			default:
				break;

		} 
	}


	public static void main(String[] args) {
		SpringApplicationBuilder sab = new SpringApplicationBuilder(T9.class);
		{
			String iwannadie = System.getProperty("rose.mary.home", ".") + File.separator + "iwannadie.pid";
			sab.build().addListeners(new ApplicationPidFileWriter(iwannadie));
		}
		ctx = sab.run(args);
	}

	public static void exitApplication() {
		int exitCode = 0;
		try {
			exitCode = SpringApplication.exit(ctx, new ExitCodeGenerator() {
				@Override
				public int getExitCode() {
					// no errors
					return 0;
				}
			});
		} catch (Throwable t) {
			// SystemLogger.error("boot shutdown error",t);
		}
		System.exit(exitCode);
	}

	private void sayStartingMsg() {
		StringBuffer msg = new StringBuffer();
		msg.append(System.lineSeparator());
		msg.append(SystemLogger.astars(80));
		msg.append(SystemLogger.astar)
				.append(" " + messageSource.getMessage("system.start.msg", null, "The server t9", null))
				.append(" starting").append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" date    :").append(Util.getFormatedDate())
				.append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" version :")
				.append(messageSource.getMessage("system.version", null, "1.0.0.", null))
				.append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" bootKey :").append(runtimeInfo.getBootKey())
				.append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" home    :").append(System.getProperty("rose.mary.home", "."))
				.append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" mode    :").append(System.getProperty("rose.mary.run.mode", "server"))
				.append(System.lineSeparator());

		msg.append(getSystemResource());

		msg.append(SystemLogger.astars(80));
		SystemLogger.info(msg.toString());

	}

	private StringBuffer getSystemResource() {
		StringBuffer msg = new StringBuffer();
		SystemResource res = srm.watch(); 
		msg.append(SystemLogger.astar).append(" system resource :").append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA01 : ").append(res.getJavaVersion()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA02 : ").append(res.getJavaVendor()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA03 : ").append(res.getJavaHome()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA04 : ").append(res.getJavaClassVersion()) .append(System.lineSeparator());
		// msg.append(SystemLog.astar).append("\tA06 : ").append(res.getJavaClassPath()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA07 : ").append(res.getOsName()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA08 : ").append(res.getOsArch()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA09 : ").append(res.getOsVersion()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA10 : ").append(res.getUserName()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA11 : ").append(res.getUserHome()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA12 : ").append(res.getUserDir()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA13 : ").append(res.getTotalDiskAmt()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA14 : ").append(res.getUsedDiskAmt()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA15 : ").append(res.getIdleDiskAmt()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA16 : ").append(res.getUsedDiskPct()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA17 : ").append(res.getIdleDiskPct()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA18 : ").append(res.getUsedCpuPct()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA19 : ").append(res.getIdleCpuPct()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA20 : ").append(res.getTotalMemAmt()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA21 : ").append(res.getIdleMemAmt()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA22 : ").append(res.getUsedMemPct()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tA23 : ").append(res.getIdleMemPct()).append(System.lineSeparator());

		return msg;
	}

	private void sayEnding() {
		StringBuffer msg = new StringBuffer();
		msg.append(System.lineSeparator());
		msg.append(SystemLogger.astars(80));
		msg.append(SystemLogger.astar)
				.append(" " + messageSource.getMessage("system.start.msg", null, "The server t9", null))
				.append(" ending").append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" date    :").append(Util.getFormatedDate())
				.append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" version :")
				.append(messageSource.getMessage("system.version", null, "1.0.0.", null))
				.append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" bootKey :").append(runtimeInfo.getBootKey())
				.append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" home    :").append(System.getProperty("rose.mary.home", "."))
				.append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" mode    :").append(System.getProperty("rose.mary.run.mode", "server"))
				.append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" running :")
				.append(((System.currentTimeMillis() - runtimeInfo.getStartedTime()) / 1000 / 60) + "M")
				.append(System.lineSeparator());

		msg.append(getSystemResource());

		msg.append(SystemLogger.astars(80));
		if (serverManager.getWarnning() != null) {
			msg.append(serverManager.getWarnning());
			msg.append(SystemLogger.astars(80));
		}
		SystemLogger.info(msg.toString());
	}

}
