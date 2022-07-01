package org.springframework.boot.loader;

import java.net.URL;
 
import org.springframework.boot.loader.archive.Archive;

public class T9Launcher extends JarLauncher {
 

    public T9Launcher() {
 
        System.out.println("\n\n\n\n\n\nT9Launcher()\n\n\n\n\n\n\n\n");
    }

    protected T9Launcher(Archive archive) {
        super(archive);
 
        System.out.println("\n\n\n\n\n\nT9Launcher(archive)\n\n\n\n\n\n\n\n");
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
        ClassLoader cl = super.createClassLoader(urls);
 
        System.out.println("\n\n\n\n\nwhoanaClassLoader\n\n\n\n\n\n\n\n" + cl);
        return cl;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("\n\n\n\nT9Launcher start..\n\n\n\n\n\n\n\n");
        new T9Launcher().launch(args);
    }

}
