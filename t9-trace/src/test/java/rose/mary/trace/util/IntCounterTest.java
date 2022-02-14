package rose.mary.trace.util; 

import org.junit.Test;

public class IntCounterTest {

	@Test
	public void testGetAndIncrease() {
		
		IntCounter counter = new IntCounter(0, 5, 2);
		
		
		
		Thread t1 = new Thread(new Runnable() {			
			@Override
			public void run() {
				for(int i = 0 ; i < 5 ; i ++) System.out.println("t1:" + System.nanoTime() + ":" + counter.getAndIncrease());
			}
			
		});
		
		Thread t2 = new Thread(new Runnable() {			
			@Override
			public void run() {
				for(int i = 0 ; i < 5 ; i ++) System.out.println("t2:" + System.nanoTime() + ":" + counter.getAndIncrease());
			}
			
		});
		
		
		t2.start();
		t1.start();
		
		
	}

}
