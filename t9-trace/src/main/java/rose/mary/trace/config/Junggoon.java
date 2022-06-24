package rose.mary.trace.config;

public class Junggoon extends ClassLoader{
    
    public Junggoon(){
        super();
    }

    @Override
    public Class<?> loadClass(String className)  throws ClassNotFoundException{
        System.out.println(getClass().getName() + "->Loading class: " + className);
        return super.loadClass(className);
    }
}
