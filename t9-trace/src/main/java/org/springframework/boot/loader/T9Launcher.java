package org.springframework.boot.loader;

import java.net.URL;
 
import org.springframework.boot.loader.archive.Archive;

public class T9Launcher extends JarLauncher {
 

    public T9Launcher() {
    }

    protected T9Launcher(Archive archive) {
        super(archive);
    }

    /**
     * Create a classloader for the specified URLs.
     * 
     * @param urls the URLs
     * @return the classloader
     * @throws Exception if the classloader cannot be created
     */
    @Override
    protected ClassLoader createClassLoader(URL[] urls) throws Exception {
        // return new LaunchedURLClassLoader(urls, getClass().getClassLoader());
        // ClassLoader cl = super.createClassLoader(urls);
        System.out.println("\n\n\n\n\n whoana ClassLoader 11111 \n\n\n\n\n\n\n\n");
        ClassLoader cl = new T9ClassLoader(urls, getClass().getClassLoader());
        System.out.println("\n\n\n\n\n whoana ClassLoader 222 \n\n\n\n\n\n\n\n" + cl);
        return cl;
    }

    public static void main(String[] args) throws Exception {
        new T9Launcher().launch(args);
    }

}
