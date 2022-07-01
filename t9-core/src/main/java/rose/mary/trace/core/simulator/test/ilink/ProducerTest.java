/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.simulator.test.ilink;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import com.mococo.ILinkAPI.jms.ILConnectionFactory;

import rose.mary.trace.core.simulator.test.Test;

/**
 * <pre>
 * rose.mary.trace.test.ilink
 * ProducerTest.java
 * </pre>
 * 
 * @author whoana
 * @date Jul 30, 2019
 */
public class ProducerTest extends Test {

	@Override
	public boolean test(String[] args) throws Throwable {
		Connection conn = null;
		Session session = null;
		try {
			ConnectionFactory factory = new ILConnectionFactory("10.10.1.10", 10000);
			conn = factory.createConnection();
			session = conn.createSession(true, 0);
			conn.start();
			System.out.println("connected.");
	
			Queue queue = session.createQueue("TRACE.EQ");
			MessageProducer producer = session.createProducer(queue);
			System.out.println("Successful create queue.");
	
			// producer.setTimeToLive(10 * 1000);
			BytesMessage msg = session.createBytesMessage();
			msg.writeBytes("JMS1.1".getBytes());
	
			producer.send(msg);
			session.commit();
	
			System.out.println("Success!");
			return true;
		}catch(Throwable t) {
			t.printStackTrace();
			return false;
		}finally {
			if(session != null) session.close();
			if(conn != null) conn.close();
			
		}
	}

	@Override
	public String getSuccessMsg(String[] args) {
		return "true";
	}

	@Override
	public String getFailMsg(String[] args) {
		return "fail";
	}
	
	public static void main(String[] args ) {
		try {
			System.out.println(new ProducerTest().run(args));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
