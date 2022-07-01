/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.helper.module.mte;



import rose.mary.trace.core.helper.module.mte.MQMsgHandler;
import rose.mary.trace.core.monitor.ThroughputMonitor;

/**
 * <pre>
 * rose.mary.trace.helper.module.mte
 * MQMsgHandlerTest.java
 * </pre>
 * @author whoana
 * @date Aug 14, 2019
 */
public class MQMsgHandlerTest {

	 

	/**
	 * Test method for {@link rose.mary.trace.core.helper.module.mte.MQMsgHandler#get(int)}.
	 */
	//@Test
	public static void main(String args[]) {
		try {
			int testCount = 1;

			int waitTime = -1; 
			String qmgrName = "IIP"; 
			String hostName = "10.10.1.10"; 
			int port = 41414;
			String channelName = "IIP.SVRCONN"; 
			String queueName = "TRACE.EQ";
			String userId = null; 
			String password = null;
			int ccsid = 1208;
			int characterSet = 1208;
			boolean bindMode = false;
			boolean autoCommit = false;
			
			ThroughputMonitor tpm = new ThroughputMonitor(1000);
			
			
			for(int i = 0 ; i < testCount ; i++) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						MQMsgHandler handler = null;
						try {
							
							
							handler = new MQMsgHandler(qmgrName, hostName, port, channelName, userId, password, ccsid, characterSet, autoCommit, bindMode);
							handler.open(queueName, MQMsgHandler.Q_QPEN_OPT_GET);
							 
							System.out.println(Thread.currentThread().getName() + " start");
							
							int commitCount = 100;
							int uncommittedCount = 0;
							while(true) {
								Object msg = handler.get(waitTime);
								if(msg != null) {
									tpm.count();

									uncommittedCount ++;
									
									if (uncommittedCount % commitCount == 0) handler.rollback();
									
								}else {
									
								}
							}
							
							
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							try {
								handler.rollback();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}).start();
			}
			 
			tpm.start();
			
			 
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	 

}
