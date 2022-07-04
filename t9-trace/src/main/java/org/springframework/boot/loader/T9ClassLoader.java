package org.springframework.boot.loader;

import java.io.File;
import java.net.URL;

import javax.crypto.SecretKey;

import lemon.balm.core.cache.CacheManager;
import lemon.balm.core.cache.InfinispanCacheManager;
import lemon.balm.core.data.S9L;
import lemon.balm.core.util.CipherUtil;
import rose.mary.trace.core.cache.CacheProxy;

public class T9ClassLoader extends LaunchedURLClassLoader {
    CacheManager cm = null;
    CacheProxy<String, byte[]> cCache = null;
    CacheProxy<String, S9L> lCache = null;
    S9L s9l = null;
    
    public T9ClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        //TODO Auto-generated constructor stub
        cm = new InfinispanCacheManager();
        cCache = cm.initializeCache(System.getProperty("rose.mary.home") + File.separator + "cached-classes", "c");
        lCache = cm.initializeCache(System.getProperty("rose.mary.home") + File.separator + "cached-classes", "l");
            
            
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            Class<?> cls = super.findClass(name);
                
            // Class<?> cls = super.findClass(name);
            // if(cls == null){
            //     S9L s9l = lCache.get("s9l");
            //     SecretKey key = s9l.getK();
            //     byte[] iv = s9l.getI();
            //     byte[] data = cCache.get(name);
            //     byte[] decodedData = CipherUtil.decode(data, key, iv);
            //     cls = defineClass(name, decodedData, 0, decodedData.length);
            // }
            return cls;
        } catch (ClassNotFoundException e) {
            throw e;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }finally{
            
        }
   
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
        try {
            cm.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
