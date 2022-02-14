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
import rose.mary.trace.apps.TraceServer;
import rose.mary.trace.apps.envs.Variables;
import rose.mary.trace.apps.manager.CacheManager;
import rose.mary.trace.apps.manager.ConfigurationManager;
import rose.mary.trace.apps.manager.InterfaceCacheManager;
import rose.mary.trace.apps.manager.ServerManager;
import rose.mary.trace.data.common.RuntimeInfo;
import rose.mary.trace.database.service.TraceService;
import rose.mary.trace.monitor.SystemResource;
import rose.mary.trace.monitor.SystemResourceMonitor;
import rose.mary.trace.system.SystemLogger;


/**
 * <pre>
 * The Application is a spring boot main application
 * </pre>
 * @author whoana
 * @since Aug 7, 2019
 */
@EnableAspectJAutoProxy
@RestController
@SpringBootApplication
@ImportResource("classpath:trace-context.xml")
@ComponentScan({"rose.mary.trace"})
public class T9 implements CommandLineRunner, ApplicationListener<ContextClosedEvent>, InitializingBean, DisposableBean {
	
	//static Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");

	@Autowired
	TraceService traceService;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	SystemResourceMonitor srm;
	
	@Autowired
	TraceServer traceServer;
	
	@Autowired
	ServerManager serverManager;
	
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
			traceServer.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cacheManager.closeCache();
		sayEnding();
	}
 
	@Override
	public void run(String... args) throws Exception {
		
		SystemLogger.info("args:" + Util.toJSONString(args));
		
		isRecoveryMode = System.getProperty("rose.mary.run.mode", "server").equals("recovery") ? true : false;
		
		SystemLogger.info("isRecoveryMode:" + isRecoveryMode);
		
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
		if(isRecoveryMode) {
			try {
				SystemLogger.info("recovery mode starting");
				//recoveryHandler.start();
			}finally { 
				
			}
			
		}else {
			if(configurationManager.getServerManagerConfig().isStartOnBoot()) traceServer.start();
			else SystemLogger.info("getServerManagerConfig().isStartOnBoot():false");
			traceServer.startDatabasePolicyHandler();
		}
	}
	
	static ConfigurableApplicationContext ctx;
	
	static boolean isRecoveryMode = false;
	
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
		}catch(Throwable t) {
			//SystemLogger.error("boot shutdown error",t);
		}
		System.exit(exitCode);
	}

 
	
	 
	private void sayStartingMsg() {		 
		StringBuffer msg = new StringBuffer();
		msg.append(System.lineSeparator());
		msg.append(SystemLogger.astars(80));
		msg.append(SystemLogger.astar).append(" " + messageSource.getMessage("system.start.msg", null, "The server t9", null)).append(" starting").append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" date    :").append(Util.getFormatedDate()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" version :").append(messageSource.getMessage("system.version", null, "1.0.0.", null)).append(System.lineSeparator());	
		msg.append(SystemLogger.astar).append(" bootKey :").append(runtimeInfo.getBootKey()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" home    :").append(System.getProperty("rose.mary.home", ".")).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" mode    :").append(System.getProperty("rose.mary.run.mode", "server")).append(System.lineSeparator());
		
		
		msg.append(getSystemResource());
		
		msg.append(SystemLogger.astars(80));		
		SystemLogger.info(msg.toString());	 
		 
	}

	private StringBuffer getSystemResource() {
		StringBuffer msg = new StringBuffer();
		SystemResource res = srm.watch();
		msg.append(SystemLogger.astar).append(" system resource :").append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tjavaVersion  :").append(res.getJavaVersion()).append(System.lineSeparator());		
		msg.append(SystemLogger.astar).append("\tjavaVendor   :").append(res.getJavaVendor()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tjavaHome     :").append(res.getJavaHome()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tjavaClassVersion:").append(res.getJavaClassVersion()).append(System.lineSeparator());
		//msg.append(SystemLog.astar).append("\tjavaClassPath:").append(res.getJavaClassPath()).append(System.lineSeparator());		
		msg.append(SystemLogger.astar).append("\tosName       :").append(res.getOsName()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tosArch       :").append(res.getOsArch()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tosVersion    :").append(res.getOsVersion()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tuserName     :").append(res.getUserName()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tuserHome     :").append(res.getUserHome()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tuserDir      :").append(res.getUserDir()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\ttotalDiskAmt :").append(res.getTotalDiskAmt()).append(System.lineSeparator());		
		msg.append(SystemLogger.astar).append("\tusedDiskAmt  :").append(res.getUsedDiskAmt()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tidleDiskAmt  :").append(res.getIdleDiskAmt()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tusedDiskPct  :").append(res.getUsedDiskPct()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tidleDiskPct  :").append(res.getIdleDiskPct()).append(System.lineSeparator()); 
		msg.append(SystemLogger.astar).append("\tusedCpuPct   :").append(res.getUsedCpuPct()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tidleCpuPct   :").append(res.getIdleCpuPct()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\ttotalMemAmt  :").append(res.getTotalMemAmt()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tidleMemAmt   :").append(res.getIdleMemAmt()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tusedMemPct   :").append(res.getUsedMemPct()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append("\tidleMemPct   :").append(res.getIdleMemPct()).append(System.lineSeparator());
		return msg;
	}
	
	private void sayEnding() {
		StringBuffer msg = new StringBuffer();
		msg.append(System.lineSeparator());
		msg.append(SystemLogger.astars(80));
		msg.append(SystemLogger.astar).append(" " + messageSource.getMessage("system.start.msg", null, "The server t9", null)).append(" ending").append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" date    :").append(Util.getFormatedDate()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" version :").append(messageSource.getMessage("system.version", null, "1.0.0.", null)).append(System.lineSeparator());	
		msg.append(SystemLogger.astar).append(" bootKey :").append(runtimeInfo.getBootKey()).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" home    :").append(System.getProperty("rose.mary.home", ".")).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" mode    :").append(System.getProperty("rose.mary.run.mode", "server")).append(System.lineSeparator());
		msg.append(SystemLogger.astar).append(" running :").append(((System.currentTimeMillis() - runtimeInfo.getStartedTime())/1000/60) + "M").append(System.lineSeparator());
		
		msg.append(getSystemResource());
		
		msg.append(SystemLogger.astars(80));
		if(serverManager.getWarnning() != null) {
			msg.append(serverManager.getWarnning());
			msg.append(SystemLogger.astars(80));
		}
		SystemLogger.info(msg.toString());
	}
	
 
	
}
