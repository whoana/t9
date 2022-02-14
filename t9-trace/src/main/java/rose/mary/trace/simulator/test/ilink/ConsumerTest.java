/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.simulator.test.ilink;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import com.mococo.ILinkAPI.jms.ILConnectionFactory;

import rose.mary.trace.simulator.test.Test;

/**
 * <pre>
 * rose.mary.trace.test.ilink
 * ConsumerTest.java
 * </pre>
 * 
 * @author whoana
 * @date Jul 30, 2019
 */
public class ConsumerTest extends Test {

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
			MessageConsumer consumer = session.createConsumer(queue);
			System.out.println("Successful create queue.");

			BytesMessage msg = (BytesMessage) consumer.receiveNoWait();
			if (msg != null) {
				byte[] data = new byte[(int) msg.getBodyLength()];
				msg.readBytes(data);
				System.out.println("content : " + new String(data));
			} else {
				System.out.println("->There is no data");
			}

			session.commit();
			System.out.println("Get Success!");
			return true;
		} catch (Throwable t) {
			session.rollback();
			t.printStackTrace();
			return false;
		} finally {
			if (session != null)
				session.close();
			if (conn != null)
				conn.close();

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
			System.out.println(new ConsumerTest().run(args));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
