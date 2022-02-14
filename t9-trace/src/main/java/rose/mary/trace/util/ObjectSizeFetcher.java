package rose.mary.trace.util;

import java.lang.instrument.Instrumentation;

import rose.mary.trace.data.common.State;

public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }
    
}