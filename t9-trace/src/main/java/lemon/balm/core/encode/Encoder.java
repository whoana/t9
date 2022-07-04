package lemon.balm.core.encode;
 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import lemon.balm.core.data.S9L;
import lemon.balm.core.util.CipherUtil;
import rose.mary.trace.core.cache.CacheProxy;

public abstract class Encoder {
    
    S9L s9l;

    CacheProxy<String, byte[]> cCache;

    CacheProxy<String, S9L> lCache;
 
 
    protected abstract void createCCacheTask(String diskPath, String name) throws Exception ;
    protected abstract void createLCacheTask(String diskPath, String name) throws Exception ;

    protected abstract void postTask() throws Exception ;

    private void mainTask(String source) throws Exception{

        Files.list(Paths.get(source)).forEach(new Consumer<Path>() {
            @Override
            public void accept(Path src) {
                try {
                    encode(src, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public final void doTask(S9L s9l, String source, String output) throws Exception {
        try{
            this.s9l = s9l;
            createCCacheTask(output, "c");
            createLCacheTask(output, "l");
            lCache.put("s9l", s9l);
            mainTask(source);
        }finally{
            postTask();
        }
    }

    public void encode(Path sourcePath, String pkg) throws Exception {

        StringBuffer packages = new StringBuffer();
        if(pkg != null ) packages.append(pkg);
        if (Files.isDirectory(sourcePath)) {   
            
            if(pkg != null ) packages.append(".").append(sourcePath.getFileName().toString());
            else packages.append(sourcePath.getFileName().toString()); 
            Files.list(sourcePath).forEach(new Consumer<Path>() {

                @Override
                public void accept(Path src) {
                    try {
                        encode(src, packages.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }    
                }

            });
              
        } else {
            String className = pkg == null ? sourcePath.getFileName().toString() :  pkg + "." + sourcePath.getFileName().toString();
            className = className.substring(0, className.length() - 6);
            System.out.println("class:" + className);
            System.out.println(sourcePath);

            byte[] data = Files.readAllBytes(sourcePath);
            byte[] encodedData = CipherUtil.encode(data, s9l.getK(), s9l.getI());
            System.out.println("encoded data[" + encodedData.length + "]");
            cCache.put(className, encodedData);
            byte[] cachedData = cCache.get(className);
            System.out.println("cache data[" + cachedData.length + "]");
    
        }
    }
 

}
