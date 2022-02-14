/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.helper.module.mte;


import java.util.Enumeration;


import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException; 

import com.mococo.ILinkAPI.jms.ILConnectionFactory;
import com.mococo.ILinkAPI.jms.ILSession;

 

/**
 * <pre>
 * rose.mary.trace.helper.module
 * ILinkTraceMsgHandler.java
 * </pre>
 * @author whoana
 * @date Jul 25, 2019
 */
public class ILinkMsgHandler implements MsgHandler {
 
	Logger logger = LoggerFactory.getLogger(ILinkMsgHandler.class);
	
	String hostName = "10.10.1.10";
	String qmgrName;
	int port = 10000;
	String userId;
	String password;
	String channelName; 
	
	String queueName;
	Connection conn = null;
	Session session = null;
	Queue queue = null;
	MessageProducer producer = null;
	MessageConsumer consumer = null;
	QueueBrowser browser;
	//IdParser idParser = new MTEIdParser();
	
	/**
	 * @param qmgrName
	 * @param hostname
	 * @param port
	 * @param channelName
	 */
	public ILinkMsgHandler(String qmgrName, String hostName, int port, String channelName, String userId, String password) {
		this.qmgrName = qmgrName;
		this.hostName = hostName;
		this.port = port;
		this.channelName = channelName; 
		this.userId = userId;
		this.password = password;
		
	}
	
//	public void addListener(MessageListener listener) throws JMSException {
//		consumer.setMessageListener(listener);
//	}

	public ILinkMsgHandler(String qmgrName, String hostName, int port, String channelName) {
		this(qmgrName, hostName, port, channelName, null, null);
	}
	
	@Override
	public void open(String queueName, int openOpt) throws Exception {
		ConnectionFactory factory = new ILConnectionFactory(hostName, port);
		conn = factory.createConnection();
		session = conn.createSession(true, Session.SESSION_TRANSACTED);
		
		
		conn.start();
		this.queueName = queueName;
		queue = session.createQueue(queueName);
	
		
		if(Q_OPEN_OPT_PUT == openOpt) { 
			producer = session.createProducer(queue);
		}else if(Q_QPEN_OPT_GET == openOpt) {
			consumer = session.createConsumer(queue);
			
			browser = session.createBrowser(queue);
			
			
		}else{
			throw new Exception("UnSupported queue open option:" + openOpt);
		}
		
	}
	
	public void setListener(MessageListener ml) throws JMSException {
		consumer.setMessageListener(ml);
	}
	
	BytesMessage buildMessage(BytesMessage msg, String usr, String mcd, byte[] data) throws JMSException {
		byte[] usrData = usr.getBytes();
		
		byte[] mcdData = mcd.getBytes();
		int strucLength = RFH2Struct.rfh2HeaderSize;
		if (mcdData.length != 0) strucLength += 4 + mcdData.length;
		if (usrData.length != 0) strucLength += 4 + usrData.length;
		//-----------------------------------------------------------
		msg.writeBytes(RFH2Struct.strucId);     //"RFH "    [4 bytes]
		msg.writeInt(RFH2Struct.version);       //         2[4 bytes]
		msg.writeInt(strucLength);              //         n[4 bytes]
		msg.writeInt(RFH2Struct.encoding);      //       546[4 bytes]
		msg.writeInt(RFH2Struct.codedCharSetId);//        -2[4 bytes]
		msg.writeBytes(RFH2Struct.format);      //"MQSTR   "[8 bytes]
		msg.writeInt(RFH2Struct.flags);         //         0[4 bytes]
		msg.writeInt(RFH2Struct.nameValueCCSID);//      1208[4 bytes] 
		if (mcdData.length != 0) {
			msg.writeInt(mcdData.length);       //         n[4 bytes]
			msg.writeBytes(mcdData);            //"        "[n bytes]
		}
		if (usrData.length != 0) {
			msg.writeInt(usrData.length);       //         n[4 bytes]
			msg.writeBytes(usrData);            //"        "[n bytes]
		}
		//-----------------------------------------------------------
		//                           SUM : 44 + [usr len] + [mcd len] 
		//-----------------------------------------------------------
		msg.writeBytes(data);
		return msg;
	}
	
	@Override
	//public void put(MTEMessage msg) throws Exception {
	public void put(MTEHeader header, String data) throws Exception {
		//MTEHeader header =  msg.getHeader(); 
		String mcdData = header.getMcdData(); 
		String usrData = header.getUsrData();
		BytesMessage bmsg = buildMessage(session.createBytesMessage(), usrData, mcdData, data.getBytes());
		producer.send(bmsg);
	}
 
	/**
	 * 

javax.jms.JMSException: Failed to send data to the server: java.io.IOException: Broken pipe
-- ILAPI FUNCTION TRACE --
[17/09/2019 10:41:31.584 ILSession.sendILMessage(byte[] header, byte[] body)] ILSession.sendBytes: Try to send header.
[17/09/2019 10:41:31.584 ILSession.sendILMessage(byte[] header, byte[] body)] ILSession.sendBytes: Caught exception.

	at com.mococo.ILinkAPI.jms.ILSession.sendILMessage(ILSession.java:1454)
	at com.mococo.ILinkAPI.jms.ILMessageConsumer.receiveHelper(ILMessageConsumer.java:215)
	at com.mococo.ILinkAPI.jms.ILMessageConsumer.receive(ILMessageConsumer.java:132)
	at rose.mary.trace.helper.module.mte.ILinkMsgHandler.get(ILinkMsgHandler.java:163)
	at rose.mary.trace.channel.mom.MOMChannel.trace(MOMChannel.java:89)
	at rose.mary.trace.channel.Channel.run(Channel.java:138)
	at java.lang.Thread.run(Thread.java:748)

	 */
	@Override
	public Object get(int waitTime) throws Exception {
		
//		conn.start();
//		queue = session.createQueue(queueName);
//		consumer = session.createConsumer(queue);
		
		
//		BytesMessage msg = (BytesMessage) consumer.receive();
		BytesMessage msg = (BytesMessage) consumer.receive(waitTime);   
		//BytesMessage msg = (BytesMessage) consumer.receiveNoWait();
		 
			 
		
		return msg;
	}

	@Override
	public void startTransaction() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void commit() throws Exception {
		if(session != null) session.commit();
	}

	@Override
	public void rollback() throws Exception {
		if(session != null) session.rollback();
	}

	@Override
	public void close() {
		try {
			if(session != null) session.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
		try {
			if(conn != null) conn.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public Session getSession() {
		return session;
	}
	
	/**
	 * @param waitTime
	 * @return
	 * @throws JMSException 
	 */
	public Enumeration browse(int waitTime) throws JMSException {
		// TODO Auto-generated method stub
		return browser.getEnumeration();
	}

	/**
	 * @return
	 */
	public Destination getQueue() {
		// TODO Auto-generated method stub
		return queue;
	}

	@Override
	public boolean ping() {
		boolean ok = false;
		if(session != null) {
			try {
				ok = ((ILSession)session).isConnected();
			} catch (JMSException e) {
				ok = false;
				logger.error("", e);
			}
		}else {
			ok = false;
		}
		return ok;
	}

}
