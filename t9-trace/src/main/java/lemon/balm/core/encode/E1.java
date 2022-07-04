package lemon.balm.core.encode;
 
 
import lemon.balm.core.cache.CacheManager;
import lemon.balm.core.cache.InfinispanCacheManager;

public class E1 extends Encoder {
      

    CacheManager cacheManager = null;
 

    public E1(){
        cacheManager =  new InfinispanCacheManager();
    }

    @Override
    protected void createCCacheTask(String diskPath, String name) throws Exception {
        cCache = cacheManager.initializeCache(diskPath, name);   
    }

    @Override
    protected void createLCacheTask(String diskPath, String name) throws Exception {
        lCache = cacheManager.initializeCache(diskPath, name);   
    }

    @Override
    protected void postTask() throws Exception {
        cacheManager.close();
    }
}
