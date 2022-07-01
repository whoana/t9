/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.simulator.test.mq;

import rose.mary.trace.core.exception.NoMoreMessageException;
import rose.mary.trace.core.helper.module.mte.MQMsgHandler;
import rose.mary.trace.core.helper.module.mte.MsgHandler;
import rose.mary.trace.core.simulator.test.Test; 

/**
 * <pre>
 * rose.mary.trace.test.mq
 * ParseMsgTest.java
 * </pre>
 * @author whoana
 * @date Aug 2, 2019
 */
public class ParseMsgTest extends Test {

	@Override
	public boolean test(String[] args) throws Throwable {
		
		Thread t1 = new Thread(new GetThread());
		Thread t2 = new Thread(new GetThread());
		Thread t3 = new Thread(new GetThread());
		Thread t4 = new Thread(new GetThread());
		Thread t5 = new Thread(new GetThread());
//		Thread t6 = new Thread(new GetThread());
//		Thread t7 = new Thread(new GetThread());
//		Thread t8 = new Thread(new GetThread());
//		Thread t9 = new Thread(new GetThread());
//		Thread t10 = new Thread(new GetThread());
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
//		t6.start();
//		t7.start();
//		t8.start();
//		t9.start();
//		t10.start();
		return true;
	}

	
	class GetThread implements Runnable{

		@Override
		public void run() {
			MsgHandler mh = null;
			try {
				int port = 41414;			
				String hostname = "10.10.1.10";
				String qmgrName = "IIP";
				String channelName = "SYSTEM.DEF.SVRCONN"; 
				String queueName = "TRACE.EQ";
				
				mh = new  MQMsgHandler(qmgrName, hostname,  port, channelName,null, null,1208,1208, false,false);
				mh.open(queueName, MsgHandler.Q_QPEN_OPT_GET);
				Object trace = null;
				int count = 0 ; 
				long elapsed = System.currentTimeMillis();
				do{					
					try {
						trace = mh.get(1000);
						System.out.println(trace);
						count ++;
					}catch(NoMoreMessageException e) {
						break;
					}					
				}while(trace != null);
				
				elapsed = System.currentTimeMillis() - elapsed;
				String name = Thread.currentThread().getName();
				double sec = elapsed/1000;
				double tps = 1000*count/elapsed;
				//System.out.println("----------------------------------------");
				//System.out.println(name + "-> elapsed :" + elapsed + " ms)");
				//System.out.println(name + "-> msg : " + count + " 건");
				System.out.println("{name:"+name+",tps:" + tps + ",elapsed:" + elapsed + ",count:" + count + "}");
				mh.rollback();
				//mh.commit();
			}catch(Throwable t) {
				try {
					mh.rollback();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				t.printStackTrace();
			}finally {
				if(mh != null) mh.close();
			}
		}
		
	}
	
	@Override
	public String getSuccessMsg(String[] args) {
		return "success";
	}

	@Override
	public String getFailMsg(String[] args) {
		return "fail";
	}

	
	public static void main(String[] args ) {
		try {
			System.out.println(new ParseMsgTest().run(args));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
