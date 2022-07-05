package org.springframework.boot.loader;

import java.net.URL;

import org.springframework.boot.loader.archive.Archive;

public class T9L extends ExecutableArchiveLauncher {

    static final String BOOT_INF_CLASSES = "BOOT-INF/classes/";

    static final String BOOT_INF_LIB = "BOOT-INF/lib/";

    public T9L() {
    }

    protected T9L(Archive archive) {
        super(archive);
    }

    @Override
    protected boolean isNestedArchive(Archive.Entry entry) {
        if (entry.isDirectory()) {
            return entry.getName().equals(BOOT_INF_CLASSES);
        }
        return entry.getName().startsWith(BOOT_INF_LIB);
    }

    @Override
    protected ClassLoader createClassLoader(URL[] urls) throws Exception { 
        return new T9C(urls, getClass().getClassLoader());
    }

    public static void main(String[] args) throws Exception {
        new T9L().launch(args);
    }

}

// public T9Launcher() {
// }

// protected T9Launcher(Archive archive) {
// super(archive);
// }

// /**
// * Create a classloader for the specified URLs.
// *
// * @param urls the URLs
// * @return the classloader
// * @throws Exception if the classloader cannot be created
// */
// @Override
// protected ClassLoader createClassLoader(URL[] urls) throws Exception {
// // return new LaunchedURLClassLoader(urls, getClass().getClassLoader());
// // ClassLoader cl = super.createClassLoader(urls);
// System.out.println("\n\n\n\n\n whoana ClassLoader 11111 \n\n\n\n\n\n\n\n");
// ClassLoader cl = new T9ClassLoader(urls, getClass().getClassLoader());
// System.out.println("\n\n\n\n\n whoana ClassLoader 222 \n\n\n\n\n\n\n\n" +
// cl);
// return cl;
// }

// public static void main(String[] args) throws Exception {
// new T9Launcher().launch(args);
// }

// }
