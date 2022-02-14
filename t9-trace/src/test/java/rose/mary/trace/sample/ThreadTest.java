package rose.mary.trace.sample;

/**
 * <pre>
 * 스레드 시작 종료를 컨트롤하기 위한 샘플 코드
 * stopGracefully 옵션에 따라 즉시 종료 또는 우아하게 종료 가능하도록 코드 구성.
 * </pre>
 * @since 202005 
 * @author whoana
 */

public class ThreadTest implements Runnable{

	public void start() {
		isShutdown = false;
		thread = new Thread(this);
		thread.start();
		
	}
	
	boolean stopGracefully = true;
	public void stop() {
		isShutdown = true;
		if(stopGracefully) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			thread.interrupt();
		}
		
	}
	
	Thread  thread = null;
	boolean isShutdown = false;
	@Override
	public void run(){
		while(Thread.currentThread() == thread && !isShutdown){
			System.out.println("무한 실행");
			
			
			try {
				Thread.sleep(60* 1000);
			} catch (InterruptedException e) {
				//e.printStackTrace();
				//break;
			}
			 

			if(thread.isInterrupted()){
				System.out.println("인터럽트당함.");
				break;
			}
			
//			for(int i = 0 ; true; i ++) {
//				System.out.print("");
//			}
		}
		System.out.println("실행 종료");
	}
	
	
	public static void main(String[] args) {

		ThreadTest  thread = new ThreadTest();
		thread.start();
		
		// 0.5초 후에 스레드를 종료
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
		thread.stop();
		 
	}
	
}
