/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.simulator.test.ilink;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;


import com.mococo.ILinkAPI.jms.ILConnectionFactory;

import rose.mary.trace.simulator.test.Test;

/**
 * <pre>
 * rose.mary.trace.test.ilink
 * ConnectionTest.java
 * </pre>
 * @author whoana
 * @date Jul 30, 2019
 */
public class ConnectionTest extends Test {
 

	@Override
	public boolean test(String[] args) throws Throwable {
		try {
			
			ConnectionFactory factory = new ILConnectionFactory("10.10.1.10", 10000);
			Connection conn = factory.createConnection();
			//Session session = conn.createSession(true, 0);
			conn.start();
			System.out.println("connected.");
			//session.close();
			conn.close();
	
			return true;
		}catch(Throwable t) {
			t.printStackTrace();
			return false;
		}
	}

	@Override
	public String getSuccessMsg(String[] args) {
		return "success";
	}

	@Override
	public String getFailMsg(String[] args) {
		// TODO Auto-generated method stub
		return "fail";
	}
	
	public static void main(String[] args ) {
		try {
			System.out.println(new ConnectionTest().run(args));
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
