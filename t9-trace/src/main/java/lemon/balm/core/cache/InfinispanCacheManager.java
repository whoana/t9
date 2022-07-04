package lemon.balm.core.cache;

import java.io.IOException;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.transaction.TransactionMode;
 
// import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.cache.infinispan.InfinispanCacheProxy;

public class InfinispanCacheManager extends CacheManager{
    DefaultCacheManager cacheManager = null;

    public InfinispanCacheManager(){
        cacheManager =  new DefaultCacheManager();   
    }

    @Override
    public <K,V> InfinispanCacheProxy<K, V> initializeCache(String diskPath, String name) {
 
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.persistence()
				.passivation(false)
				.addSingleFileStore()
				.maxEntries(-1)
				.preload(true)
				.shared(false)
				.fetchPersistentState(false)
				.ignoreModifications(false)
				.purgeOnStartup(false)
				.location(diskPath + java.io.File.separator + name)
				.memory().evictionStrategy(EvictionStrategy.NONE)
				.memory().size(-1);
		
		//builder.simpleCache(true);
		
		builder.transaction().autoCommit(false);
		builder.transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL);
		
		// Define local cache configuration
		cacheManager.defineConfiguration(name, builder.build());
		// Obtain the local cache
		Cache<K, V> cache = cacheManager.getCache(name);
		cache.getAdvancedCache().getStats().setStatisticsEnabled(true);
		
		
		
		return new InfinispanCacheProxy<K, V>(name, cache);
	}

    @Override
    public void close() throws Exception {
        cacheManager.close();
    }
    


}
