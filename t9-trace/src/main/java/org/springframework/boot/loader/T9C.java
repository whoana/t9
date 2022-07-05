package org.springframework.boot.loader;

import java.io.File;
import java.net.URL;

import lemon.balm.core.cache.CacheManager;
import lemon.balm.core.cache.InfinispanCacheManager;
import lemon.balm.core.data.S9L;
import lemon.balm.core.util.CipherUtil;
import rose.mary.trace.core.cache.CacheProxy;

public class T9C extends LaunchedURLClassLoader {
    
    CacheManager cm = null;
    CacheProxy<String, byte[]> cCache = null;
    CacheProxy<String, S9L> lCache = null;

    public T9C(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        init();
    }
    
    private final void init(){
        cm = new InfinispanCacheManager();
        cCache = cm.initializeCache(System.getProperty("rose.mary.home") + File.separator + "lib" + File.separator + "ext", "c");
        lCache = cm.initializeCache(System.getProperty("rose.mary.home") + File.separator + "lib" + File.separator + "ext", "l");
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
    
        Class<?> cls = null;
        try{
            cls = super.findClass(name);
        }catch(ClassNotFoundException e){
            try{
                S9L s9l = lCache.get("s9l");
                byte[] decodedData = CipherUtil.decode(cCache.get(name), s9l.getK(), s9l.getI());
                cls = defineClass(name, decodedData, 0, decodedData.length);
            }catch(Exception ex){
                throw new ClassNotFoundException(ex.getMessage());
            }
        }
        return cls;
    

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            cm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
