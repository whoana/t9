package rose.mary.trace.sample;

public class LambdaDemo
{
   public static void main(String[] args)
   {
      new Thread(() -> {System.out.println("Hello"); System.out.println("Hello");}).start();
   }
}
