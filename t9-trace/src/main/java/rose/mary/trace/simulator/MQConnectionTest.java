/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.simulator;

import java.util.Hashtable;
 
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;

import rose.mary.trace.simulator.test.Test;

/**
 * <pre>
 * rose.mary.trace.test
 * MQConnectionTest.java
 * </pre>
 * @author whoana
 * @date Aug 9, 2019
 */
public class MQConnectionTest extends Test{

	
	
	Hashtable<String, Object> params = new Hashtable<String, Object>();

	@Override
	public boolean test(String[] args) throws Throwable {
		try {
			String hostName = args[0];
			String qmgrName = args[1];
			int port = Integer.parseInt(args[2]);
			String userId = args[3];
			String password = args[4];
			String channelName = args[5];
			boolean isBind = Boolean.parseBoolean(args[6]);
			 
			 	
			 
			if (channelName != null)params.put(CMQC.CHANNEL_PROPERTY, channelName);
			if (hostName != null)params.put(CMQC.HOST_NAME_PROPERTY, hostName);
			params.put(CMQC.PORT_PROPERTY, new Integer(port));
			if (userId != null) params.put(CMQC.USER_ID_PROPERTY, userId);
			if (password != null) params.put(CMQC.PASSWORD_PROPERTY, password);
			if (isBind)params.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES_BINDINGS);//binding mode connect
			
			MQException.log = null;
			 
			System.out.println("start connection test");
			MQQueueManager qmgr = new MQQueueManager(qmgrName, params);
			System.out.println("finish connection");
			qmgr.close();
			System.out.println("close connection");
			
			return true;
		}catch(Throwable t) {
			System.out.println("args list: hostname qmgrName port userId password channelName isBind");
			System.out.println("java -classpath ./projectq-0.0.1-SNAPSHOT.jar rose.mary.trace.test.MQConnectionTest 10.10.1.10 IIP 41414 mqm mqm IIP.SVRCONN false");
			
			throw t;
		}finally {
			System.out.println("end connection test");
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
	
	public static void main(String args[]) {
		try {
			new MQConnectionTest().test(args);
		}catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
