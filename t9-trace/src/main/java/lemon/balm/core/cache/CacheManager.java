package lemon.balm.core.cache;

import rose.mary.trace.core.cache.CacheProxy;

public abstract class CacheManager {

    public abstract <K,V> CacheProxy<K,V> initializeCache(String diskPath, String name);
    public abstract void close() throws Exception;

}
