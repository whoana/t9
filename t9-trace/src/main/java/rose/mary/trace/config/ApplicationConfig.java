/**
 * Copyright 2018 portal.mocomsys.com Inc. All Rights Reserved.
 */
package rose.mary.trace.config;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import rose.mary.trace.apps.TraceServer;
import rose.mary.trace.apps.manager.BotErrorHandlerManager;
import rose.mary.trace.apps.manager.BotLoaderManager;
import rose.mary.trace.apps.manager.BoterManager;
import rose.mary.trace.apps.manager.CacheManager;
import rose.mary.trace.apps.manager.ChannelManager;
import rose.mary.trace.apps.manager.ConfigurationManager;
import rose.mary.trace.apps.manager.DatabasePolicyHandlerManager;
import rose.mary.trace.apps.manager.FinisherManager;
import rose.mary.trace.apps.manager.InterfaceCacheManager;
import rose.mary.trace.apps.manager.LoaderManager;
import rose.mary.trace.apps.manager.MonitorManager;
import rose.mary.trace.apps.manager.ServerManager;
import rose.mary.trace.apps.manager.SystemErrorTestManager;
import rose.mary.trace.apps.manager.TraceErrorHandlerManager;
import rose.mary.trace.apps.manager.UnmatchHandlerManager;
import rose.mary.trace.data.common.RuntimeInfo;
import rose.mary.trace.database.service.BotService;
import rose.mary.trace.database.service.InterfaceService;
import rose.mary.trace.database.service.SystemService;
import rose.mary.trace.database.service.TraceService;
import rose.mary.trace.monitor.SystemResourceMonitor;
import rose.mary.trace.monitor.ThroughputMonitor;
import rose.mary.trace.system.SystemErrorDetector;

/**
 * <pre>
 * ApplicationConfig
 * </pre>
 * 
 * @author whoana
 * @date Aug 1, 2019
 */
@Configuration
@EnableAsync
public class ApplicationConfig {

	@Autowired
	ConfigurationManager configurationManager;

	@Autowired
	TraceService traceService;

	@Autowired
	SystemService systemService;

	@Bean
	public RuntimeInfo runtimeInfo() {
		RuntimeInfo runtimeInfo = new RuntimeInfo();
		runtimeInfo.setBootKey(UUID.randomUUID().toString());
		runtimeInfo.setStartedTime(System.currentTimeMillis());
		return runtimeInfo;
	}

	int schedulerPoolSize = 10;
	String schedulerPrefix = "scheduler";

	@Bean(name = "taskScheduler")
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(schedulerPoolSize);
		scheduler.setThreadNamePrefix(schedulerPrefix);
		scheduler.initialize();
		return scheduler;
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		return executor;
	}

	@Bean(initMethod = "prepare")
	public ConfigurationManager configurationManager() throws Exception {
		ConfigurationManager manager = new ConfigurationManager();
		return manager;
	}

	@Bean("tpm1")
	public ThroughputMonitor channelThroughputMonitor() throws Exception {
		ThroughputMonitor monitor = new ThroughputMonitor(1000);
		return monitor;
	}

	@Bean("tpm2")
	public ThroughputMonitor loaderThroughputMonitor() throws Exception {
		ThroughputMonitor monitor = new ThroughputMonitor(1000);
		return monitor;
	}

	@Bean("tpm3")
	public ThroughputMonitor boterThroughputMonitor() throws Exception {
		ThroughputMonitor monitor = new ThroughputMonitor(1000);
		return monitor;
	}

	@Bean("tpm4")
	public ThroughputMonitor botLoaderThroughputMonitor() throws Exception {
		ThroughputMonitor monitor = new ThroughputMonitor(1000);
		return monitor;
	}

	@Bean
	public SystemResourceMonitor systemResourceMonitor() throws Exception {
		SystemResourceMonitor monitor = new SystemResourceMonitor(1000);
		return monitor;
	}

	@Bean
	public MonitorManager monitorManager(
			@Autowired SystemResourceMonitor srm,
			@Autowired @Qualifier("tpm1") ThroughputMonitor tpm1,
			@Autowired @Qualifier("tpm2") ThroughputMonitor tpm2,
			@Autowired @Qualifier("tpm3") ThroughputMonitor tpm3,
			@Autowired @Qualifier("tpm4") ThroughputMonitor tpm4) throws Exception {
		MonitorManager manager = new MonitorManager(srm, tpm1, tpm2, tpm3, tpm4);
		return manager;
	}

	@Bean(initMethod = "prepare")
	public CacheManager cacheManager(@Autowired ConfigurationManager configurationManager) throws Exception {
		CacheManager manager = new CacheManager(configurationManager.getCacheManagerConfig());
		return manager;
	}

	@Bean(initMethod = "prepare")
	public InterfaceCacheManager interfaceCacheManager(@Autowired ConfigurationManager configurationManager,
			@Autowired ThreadPoolTaskScheduler taskScheduler, @Autowired InterfaceService service,
			@Autowired CacheManager cacheManager) throws Exception {
		InterfaceCacheManager manager = new InterfaceCacheManager(configurationManager.getInterfaceCacheManagerConfig(),
				taskScheduler, service, cacheManager);
		return manager;
	}

	@Bean
	public ChannelManager channelManager(@Autowired ConfigurationManager configurationManager,
			@Autowired CacheManager cacheManager, @Autowired @Qualifier("tpm1") ThroughputMonitor tpm1)
			throws Exception {
		return new ChannelManager(configurationManager.getChannelManagerConfig(), cacheManager, tpm1);
	}

	@Bean
	public LoaderManager loaderManager(@Autowired ConfigurationManager configurationManager,
			@Autowired TraceService traceService, @Autowired CacheManager cacheManager,
			@Autowired @Qualifier("tpm2") ThroughputMonitor tpm2) throws Exception {
		return new LoaderManager(configurationManager.getLoaderManagerConfig(), traceService, cacheManager, tpm2);
	}

	@Bean
	public BoterManager boterManager(@Autowired ConfigurationManager configurationManager,
			@Autowired CacheManager cacheManager, @Autowired @Qualifier("tpm3") ThroughputMonitor tpm3)
			throws Exception {
		return new BoterManager(configurationManager.getBoterManagerConfig(), cacheManager, tpm3);
	}

	@Bean
	public BotLoaderManager botLoaderManager(@Autowired ConfigurationManager configurationManager,
			@Autowired BotService botService, @Autowired CacheManager cacheManager,
			@Autowired @Qualifier("tpm4") ThroughputMonitor tpm4) throws Exception {
		return new BotLoaderManager(configurationManager.getBotLoaderManagerConfig(), botService, cacheManager, tpm4);
	}

	@Bean
	public FinisherManager finisherManager(@Autowired ConfigurationManager configurationManager,
			@Autowired CacheManager cacheManager) throws Exception {
		return new FinisherManager(configurationManager.getFinisherManagerConfig(), cacheManager);
	}

	@Bean
	public TraceErrorHandlerManager traceErrorHandlerManager(@Autowired ConfigurationManager configurationManager,
			@Autowired CacheManager cacheManager, @Autowired TraceService traceService,
			@Autowired MessageSource messageSource) throws Exception {
		return new TraceErrorHandlerManager(configurationManager.getTraceErrorHandlerManagerConfig(), cacheManager,
				traceService, messageSource);
	}

	@Bean
	public BotErrorHandlerManager botErrorHandlerManager(@Autowired ConfigurationManager configurationManager,
			@Autowired CacheManager cacheManager, @Autowired BotService botService,
			@Autowired MessageSource messageSource) throws Exception {
		return new BotErrorHandlerManager(configurationManager.getBotErrorHandlerManagerConfig(), cacheManager,
				botService, messageSource);
	}

	@Bean
	public UnmatchHandlerManager unmatchHandlerManager(@Autowired ConfigurationManager configurationManager,
			@Autowired CacheManager cacheManager, @Autowired BotService botService,
			@Autowired MessageSource messageSource) throws Exception {
		return new UnmatchHandlerManager(configurationManager.getUnmatchHandlerManagerConfig(), cacheManager,
				botService, messageSource);
	}

	@Bean
	public SystemErrorTestManager systemErrorTesterManager(@Autowired ConfigurationManager configurationManager,
			@Autowired SystemService systemService) throws Exception {
		return new SystemErrorTestManager(configurationManager.getConfig(), systemService);
	}

	@Bean
	public DatabasePolicyHandlerManager databasePolicyHandlerManager(
			@Autowired ConfigurationManager configurationManager, @Autowired CacheManager cacheManager,
			@Autowired ServerManager serverManager, @Autowired ChannelManager channelManager,
			@Autowired SystemService systemService, @Autowired MessageSource messageSource) throws Exception {
		return new DatabasePolicyHandlerManager(configurationManager.getDatabasePolicyConfig(), systemService,
				serverManager, channelManager, messageSource, cacheManager);
	}

	@Bean
	public ServerManager serverManager() throws Exception {
		ServerManager manager = new ServerManager();
		return manager;
	}

	@Bean(initMethod = "ready")
	public TraceServer traceServer() throws Exception {
		String name = configurationManager.getServerManagerConfig().getName();
		TraceServer ts = new TraceServer(name);
		return ts;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		// localeResolver.setDefaultLocale(Locale.KOREA);
		return localeResolver;
	}

	@Bean(initMethod = "prepare")
	public SystemErrorDetector systemErrorDetector() {
		return new SystemErrorDetector();
	}

	 

}
