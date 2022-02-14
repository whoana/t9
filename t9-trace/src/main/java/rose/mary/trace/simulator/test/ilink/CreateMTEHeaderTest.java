/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.simulator.test.ilink;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import com.mococo.ILinkAPI.jms.ILConnectionFactory;

import rose.mary.trace.simulator.test.Test;

/**
 * <pre>
 * rose.mary.trace.test.ilink
 * CreateMTEHeaderTest.java
 * </pre>
 * @author whoana
 * @date Jul 30, 2019
 */
public class CreateMTEHeaderTest extends Test{
	
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
			
			String usr = "<usr>1</usr>";
			String mcd = "<mcd>2</mcd>";
			msg = createMessageByte(msg,usr, mcd, "data part 123456789 가나다라마바사 qwertyu".getBytes());
			//msg.writeBytes("data part 123456789 가나다라마바사 qwertyu".getBytes());
	
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
			System.out.println(new CreateMTEHeaderTest().run(args));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static BytesMessage createMessageByte(BytesMessage mbis, String usr, String mcd, byte[] messageStr) throws JMSException {
		int rfhheaer_size = 36;
		
		byte[] usr_buf = usr.getBytes();
		int usr_len = usr_buf.length;
		byte[] mcdBytes = mcd.getBytes();
		int nTotalSize = rfhheaer_size;
		if (mcdBytes.length != 0) {
			nTotalSize += 4 + mcdBytes.length;
		}

		if (usr_len != 0) {
			nTotalSize += 4 + usr_len;
		}

		int mTotal = nTotalSize + messageStr.length;
//		MIByteArrayOutputStream mbis = new MIByteArrayOutputStream(mTotal);
		mbis.writeBytes("RFH ".getBytes());
		mbis.writeInt(2);
		mbis.writeInt(nTotalSize);
		mbis.writeInt(546);
		mbis.writeInt(-2);
		mbis.writeBytes("MQSTR   ".getBytes());
		mbis.writeInt(0);
		mbis.writeInt(1208);
		if (mcdBytes.length != 0) {
			mbis.writeInt(mcdBytes.length);
			mbis.writeBytes(mcdBytes);
		}

		if (usr_len != 0) {
			mbis.writeInt(usr_len);
			mbis.writeBytes(usr_buf);
		}

		mbis.writeBytes(messageStr);
 
		return mbis;
	}
	

}
