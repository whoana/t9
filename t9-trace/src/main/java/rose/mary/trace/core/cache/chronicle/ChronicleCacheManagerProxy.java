/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.cache.chronicle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import net.openhft.chronicle.map.ChronicleMap;
import rose.mary.trace.core.cache.CacheManagerProxy;
import rose.mary.trace.core.config.CacheManagerConfig;
import rose.mary.trace.core.data.cache.CacheConfig;
import rose.mary.trace.core.data.common.InterfaceInfo;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.data.common.Unmatch;
import rose.mary.trace.core.system.SystemError;

/**
 * <pre>
 * The ChronicleCacheManagerProxy is a implementation of {@link CacheManagerProxy}.
 * </pre>
 * @author whoana
 * @since Sep 29, 2019
 */
public class ChronicleCacheManagerProxy extends CacheManagerProxy{
	Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * @param config
	 */
	public ChronicleCacheManagerProxy(CacheManagerConfig config) {
		super(config);
	}

	@Override
	public void initialize() throws Exception {
		 		
		String diskPath = System.getProperty("rose.mary.home") + File.separator + config.getDiskPath();
		File path = new File(diskPath);
		if(!path.exists()) path.mkdirs();
		
		//----------------------------------------------------------------
		//distributeCache setting
		//----------------------------------------------------------------
		List<CacheConfig> distributeCacheConfigs = config.getDistributeCacheConfigs();
		for(CacheConfig cfg : distributeCacheConfigs) {
			
			ChronicleCacheProxy<String, Trace> distributeCache = createCache(diskPath, cfg, String.class, Trace.class, new Trace());
			distributeCaches.add(distributeCache);
		}
			
		
		//----------------------------------------------------------------
		//mergeCache setting
		//----------------------------------------------------------------
		CacheConfig mergeCacheConfig = config.getMergeCacheConfig();
		mergeCache = createCache(diskPath, mergeCacheConfig, String.class, Trace.class, new Trace());
		
		//----------------------------------------------------------------
		//backupCache setting
		//----------------------------------------------------------------
		CacheConfig backupCacheConfig = config.getBackupCacheConfig();
		backupCache = createCache(diskPath, backupCacheConfig, String.class, Trace.class, new Trace());

	
		
		//----------------------------------------------------------------
		//botCacheConfig setting
		//----------------------------------------------------------------
		List<CacheConfig> botCacheConfigs = config.getBotCacheConfigs();
		for(CacheConfig botCacheConfig : botCacheConfigs) {
			ChronicleCacheProxy<String, State> cache = createCache(diskPath, botCacheConfig, String.class, State.class, new State());
			botCaches.add(cache);
		}
		
		//----------------------------------------------------------------
		//finCache setting
		//----------------------------------------------------------------
		CacheConfig finCacheConfig = config.getFinCacheConfig();
		finCache = createCache(diskPath, finCacheConfig, String.class, State.class, new State());
 
		//----------------------------------------------------------------
		//routingCache setting
		//----------------------------------------------------------------
		CacheConfig routingCacheConfig = config.getRoutingCacheConfig();
		routingCache = createCache(diskPath, routingCacheConfig, String.class, Integer.class, new Integer(0));
		
		
		
		//----------------------------------------------------------------
		//errorCache01 setting
		//----------------------------------------------------------------
		CacheConfig errorCache01Config = config.getErrorCache01Config();
		errorCache01 = createCache(diskPath, errorCache01Config, String.class, Trace.class, new Trace());
		
	
		//----------------------------------------------------------------
		//errorCache02 setting
		//----------------------------------------------------------------
		CacheConfig errorCache02Config = config.getErrorCache02Config();
		errorCache02 = createCache(diskPath, errorCache02Config, String.class, State.class, new State());
	
		//----------------------------------------------------------------
		//interfaceCache setting
		//----------------------------------------------------------------
		CacheConfig interfaceCacheConfig = config.getInterfaceCacheConfig();
		if(interfaceCacheConfig != null) interfaceCache = createCache(diskPath, interfaceCacheConfig, String.class, InterfaceInfo.class, new InterfaceInfo());

		//----------------------------------------------------------------
		//unmatchCache setting
		//----------------------------------------------------------------
		CacheConfig unmatchCacheConfig = config.getUnmatchCacheConfig();
		if(unmatchCacheConfig != null) unmatchCache = createCache(diskPath, unmatchCacheConfig, String.class, Unmatch.class, new Unmatch());

		
		//----------------------------------------------------------------
		//systemErrorCacheConfig setting
		//----------------------------------------------------------------
		CacheConfig systemErrorCacheConfig = config.getSystemErrorCacheConfig();
		if(systemErrorCacheConfig != null) systemErrorCache = createCache(diskPath, systemErrorCacheConfig, String.class, SystemError.class, new SystemError());
		
		
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(ChronicleMap map:createdMaps) {
					map.close();
				}
				logger.info("finish to close ChronicleMaps...");
			}
			
		}));
		
		 
	}

	List<ChronicleMap<?,?>> createdMaps = new ArrayList<ChronicleMap<?,?>>();
	/**
	 * @param cfg
	 * @return
	 * @throws IOException 
	 */
	private <K, V> ChronicleCacheProxy<K, V> createCache(String diskPath, CacheConfig cfg, Class<K> keyClass, Class<V> valueClass, V defaultValue) throws IOException {
		ChronicleMap<K, V> map = null;
		if(defaultValue instanceof Integer) {
			
			map = ChronicleMap
					.of(keyClass, valueClass)
					.name(cfg.getName())
					.entries(cfg.getMaxEntries())				
					.averageKeySize(50)
					.createOrRecoverPersistedTo(new File(diskPath + java.io.File.separator + cfg.getName()));
			
		}else {
			map = ChronicleMap
					.of(keyClass, valueClass)
					.name(cfg.getName())
					.entries(cfg.getMaxEntries())
					.averageValue(defaultValue)
					.averageKeySize(50)
					.createOrRecoverPersistedTo(new File(diskPath + java.io.File.separator + cfg.getName()));

			
		}
		createdMaps.add(map);

		ChronicleCacheProxy<K, V> cache = new ChronicleCacheProxy<K, V>(cfg.getName(), map);
		return cache;
	}

	@Override
	public void close() throws Exception {
	
		for(ChronicleMap map:createdMaps) {
			map.close();
		}
		
	}

}
