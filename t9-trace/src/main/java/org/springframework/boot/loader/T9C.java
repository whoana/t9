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
    CacheProxy<String, Object> oCache = null;
    
    S9L s9l = null;

    public T9C(URL[] urls, ClassLoader parent) throws Exception {
        super(urls, parent);
        init();
    }

    private void init() throws Exception {
        cm = new InfinispanCacheManager();
        System.out.println("path:" + System.getProperty("rose.mary.home") + File.separator + "lib" + File.separator + "ext");
        cCache = cm.initializeCache(
                System.getProperty("rose.mary.home") + File.separator + "lib" + File.separator + "ext", "c");

        lCache = cm.initializeCache(
                System.getProperty("rose.mary.home") + File.separator + "lib" + File.separator + "ext", "l");

        oCache = cm.initializeCache(
                System.getProperty("rose.mary.home") + File.separator + "lib" + File.separator + "ext", "o");
        
        s9l = lCache.get("s9l");

    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        Class<?> cls = null;
        try {
            cls = super.findClass(name);
        } catch (ClassNotFoundException e) {
            try {
                byte[] decodedData = CipherUtil.decode(cCache.get(name), s9l.getK(), s9l.getI());
                if(decodedData == null || decodedData.length == 0) {
                    throw new ClassNotFoundException("class[" + name + "]decodedData is null");
                }
                cls = defineClass(name, decodedData, 0, decodedData.length);
            } catch (Exception ex) {
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
