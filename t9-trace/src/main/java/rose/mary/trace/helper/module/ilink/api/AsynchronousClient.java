/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.helper.module.ilink.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.jms.BytesMessage;
import javax.jms.Message;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.mococo.ILinkAPI.jms.ILBytesMessage;
import com.mococo.ILinkAPI.jms.ILMapMessage;
import com.mococo.ILinkAPI.jms.ILMessage;
import com.mococo.ILinkAPI.jms.ILObjectMessage;
import com.mococo.ILinkAPI.jms.ILStreamMessage;
import com.mococo.ILinkAPI.jms.ILTextMessage;
import com.mococo.ILinkAPI.jms.ILinkMessage;
import com.mococo.ILinkAPI.jms.ILinkReportMessage;
import com.mococo.ILinkAPI.jms.ILinkRequestMessage;

import rose.mary.trace.monitor.ThroughputMonitor;

/**
 * <pre>
 * rose.mary.trace.ilink.api
 * AsynchronousClient.java
 * </pre>
 * rose.mary.trace.ilink.api.AsynchronousClient
 * @author whoana
 * @date Aug 21, 2019
 */
public class AsynchronousClient {

	//Logger logger = LoggerFactory.getLogger(getClass());

	String host = "10.10.1.10";

	int port = 10000;

	int readTimeOut = 10;
 
	private AsynchronousSocketChannel channel;

	private boolean isStop = false;

	public AsynchronousClient(String name, String host, int port, String queueName) {
		this.host = host;
		this.port = port;
		this.clientId = name;
		this.queueName = queueName;
		bodyMessage = new ILinkRequestMessage(3000, "", queueName, "-99");
	}

	public void connect(long connectionTimeout, int tryCount) throws Exception {

		if (channel != null) {
			try {
				channel.close();
			} catch (IOException ex) {
			}
			channel = null;
		}

		Exception ex = null;
		for (int i = 0; i < tryCount; i++) {
			try {
				channel = AsynchronousSocketChannel.open();

				InetSocketAddress hostAddress = new InetSocketAddress(host, port);
				Future<Void> future = channel.connect(hostAddress);
				future.get();
				isStop = false;
				break;
			} catch (Exception e) {
				ex = e;
				try {
					Thread.sleep(connectionTimeout);
				} catch (InterruptedException e1) {
				}
			}
		}
		if (ex != null)
			throw ex;

	}

	public void stop() {
		isStop = true;
		if (channel != null) {
			try {
				channel.close();
			} catch (IOException ex) {
			}
			channel = null;
		}
	}

	public byte[] read(int len) throws IOException, InterruptedException, ExecutionException {
		ByteBuffer buffer = null;
		try {
			buffer = ByteBuffer.allocate(len);

			Future<Integer> readResult = channel.read(buffer);
			Integer res = readResult.get();
			if (res == -1) {
				try {
					channel.close();
				} catch (IOException ex) {
				}
				channel = null;// isConnected 에서 null 체크로직을 사용하므로
			}
			// ----------------------------------------------------------
			// 버퍼사이즈만큼 읽어들여서 수정후 주석처리함.
			// byte [] data = buffer.array();
			// ----------------------------------------------------------
			buffer.flip();
			byte[] data = new byte[buffer.limit()];
			buffer.get(data);
			return data;
		} finally {
			if (buffer != null)
				buffer.clear();
		}
	}

	public void write(byte[] data) throws Exception {
		ByteBuffer buffer = null;
		try {
			buffer = ByteBuffer.wrap(data);
			Future<Integer> writeResult = channel.write(buffer);
			writeResult.get();
			buffer.clear();
		} finally {
			if (buffer != null)
				buffer.clear();
		}
	}

	public Message consume(byte[] request) throws Exception {

		//long elapsed = System.currentTimeMillis();

		ByteBuffer buffer = null;
		try {

			buffer = ByteBuffer.wrap(request);
			Map<String, Object> attachment = new HashMap<String, Object>();
			attachment.put("action", "w");
			attachment.put("buffer", buffer);
			Future<Integer> writeResult = channel.write(buffer);
			writeResult.get();

			//System.out.println("e0:" + (System.currentTimeMillis() - elapsed));
			//elapsed = System.currentTimeMillis();

			
			ILinkMessage msg = new ILinkMessage(0);

			byte[] header = read(msg.getHeaderLength());
			msg.setHeader(header);
 
			
			//System.out.println("e1:" + (System.currentTimeMillis() - elapsed));
			//elapsed = System.currentTimeMillis();

			if (msg.getMessageType() == 2002) {
				//throw new Exception("이건 메시지가 아니야 ");
				byte[] report = read(reportMessageLength); 
				ILinkReportMessage reportMessage = new ILinkReportMessage(report);
				msg.setInnerMessage(reportMessage);
				 
			} else if (msg.getMessageType() == 2000) {

				// static
				byte[] tmpBytes = read(ILMessage.getStaticLength());
				ILMessage tmpMsg = new ILMessage();
				tmpMsg.setHeader(tmpBytes);

				// property
				byte[] propertyLenBytes = read(ILMessage.messagePropertyLengthLength);
				String propertyLengthString = new String(propertyLenBytes, 0, ILMessage.messagePropertyLengthLength);
				propertyLengthString = ILMessage.truncateCString(propertyLengthString);
				int propertyLength = 0;
				if (propertyLengthString.length() > 0) {
					propertyLength = Integer.parseInt(propertyLengthString);

					if (propertyLength != 0) {
						byte[] propertyBytes = read(propertyLength);

					}
				}
				//System.out.println("e2:" + (System.currentTimeMillis() - elapsed));
				//elapsed = System.currentTimeMillis();
				
				switch (tmpMsg.getMessageType()) {
				case 2008: // Inner message type is BASIC.
					if (propertyLength > 0)
						tmpMsg.setProperty(propertyLenBytes);
					msg.setInnerMessage(tmpMsg);

					break;

				case 2009: // Inner message type is TEXT.
					ILTextMessage tmpTextMessage = new ILTextMessage();
					tmpTextMessage.initialize();
					tmpTextMessage.setHeader(tmpBytes);
					if (propertyLength > 0)
						tmpTextMessage.setProperty(propertyLenBytes);
					byte[] textLenBytes = read(ILTextMessage.dataLengthLength);
					tmpTextMessage.setDataLength(textLenBytes);
					byte[] textBytes = read(tmpTextMessage.getDataLength());
					tmpTextMessage.setText(new String(textBytes, 0, tmpTextMessage.getDataLength()));
					msg.setInnerMessage(tmpTextMessage);
					break;

				case 2010: // Inner message type is BYTES.

					ILBytesMessage innerMsg = new ILBytesMessage();
					innerMsg.initialize();
					innerMsg.setHeader(tmpBytes);
					if (propertyLength > 0)
						innerMsg.setProperty(propertyLenBytes);

					byte[] dataLengthBytes = read(ILTextMessage.dataLengthLength);
					String dataLengthString = new String(dataLengthBytes);
					dataLengthString = ILMessage.truncateCString(dataLengthString);
					int dataLength = Integer.parseInt(dataLengthString);

					byte[] byteData = read(dataLength);
					innerMsg.setData(byteData);
					innerMsg.reset();
					msg.setInnerMessage(innerMsg);

					//System.out.println("e3:" + (System.currentTimeMillis() - elapsed));
					//elapsed = System.currentTimeMillis();
					
					
					break;

				case 2011: // Inner message type is STREAM.
					break;

				case 2012: // Inner message type is MAP.
					break;

				case 2013: // Inner message type is OBJECT.
					break;

				case 2014: // Inner message type is BYTES_DATALESS.
					break;

				default:
					break;
				}

			} else {
				throw new Exception("MSG_TYPE is not " + MSG_TYPE_REPORT);
			}

			byte[] delimeter = read(1);
			if (delimeter.length > 0) {
				char ch = (char) delimeter[0];
				if (ch != '\n')
					throw new Exception("Data sync error(150)");
			}

			ILMessage trace = (ILMessage) msg.getContents();
			trace.setMode(0);

			// consumer.consume(trace);
			return trace;
		} finally {
			if (buffer != null) buffer.clear();
			//System.out.println("e4:" + (System.currentTimeMillis() - elapsed));
			 
			
		}
	}

//	public void consume(byte [] request) throws Exception {
//		ByteBuffer buffer = null;
//		try {
//	        buffer = ByteBuffer.wrap(request);
//	        Map<String, Object> attachment = new HashMap<String, Object>();
//	        attachment.put("action", "w");
//	        attachment.put("buffer", buffer);
//	        channel.write(buffer, attachment, new CompletionHandler<Integer, Map<String, Object>>() {
//
//				@Override
//				public void completed(Integer result, Map<String, Object> attachment) {
//					// TODO Auto-generated method stub
//					ILinkMessage msg = new ILinkMessage(0);
//					try {
//						byte [] header = read(msg.getHeaderLength());
//						msg.setHeader(header);
//						
//						if(msg.getMessageType() == 2002) {
//							throw new Exception("이건 메시지가 아니야 ");
//							/*
//							byte[] report = read(reportMessageLength);
//				            ILinkReportMessage reportMessage = new ILinkReportMessage(report);
//				            msg.setInnerMessage(reportMessage);
//				            */
//						}else if(msg.getMessageType() == 2000) {
//							
//							 
////			                tempByteArray = new byte[ILMessage.getStaticLength()];
////			                
////			                trace.append("ILSession.readBytes", "Try to reading data msg body.");
////			                ret = readBytes(tempByteArray, ILMessage.getStaticLength());
////			                trace.succeed();
////			                ILMessage tempMessage = new ILMessage();
////			                tempMessage.initialize();
////			                tempMessage.setHeader(tempByteArray);
//			                
//							
////			                byte propertyLengthArray[] = new byte[ILMessage.messagePropertyLengthLength];
////			                trace.append("ILSession.readBytes", "Try to reading data msg property length.");
////			                ret = readBytes(propertyLengthArray, propertyLengthArray.length);
////			                trace.succeed();
////			               
////			                String propertyLengthString = new String(propertyLengthArray, 0, ILMessage.messagePropertyLengthLength);
////			                propertyLengthString = ILMessage.truncateCString(propertyLengthString);
////			                int propertyLength = 0;
////			                if(propertyLengthString.length() > 0)
////			                    propertyLength = Integer.parseInt(propertyLengthString);
////			                trace.append(null, "Length of property:[" + propertyLength + "]");
//			                
//			                //static
//			                byte[] tmpBytes = read(ILMessage.getStaticLength());
//			                ILMessage tmpMsg = new ILMessage();
//			                tmpMsg.setHeader(tmpBytes);
//			                
//			                //property
//			                byte[] propertyLenBytes = read(ILMessage.messagePropertyLengthLength);
//			                String propertyLengthString = new String(propertyLenBytes, 0, ILMessage.messagePropertyLengthLength);
//			                propertyLengthString = ILMessage.truncateCString(propertyLengthString);
//			                int propertyLength = 0;
//			                if(propertyLengthString.length() > 0) {
//			                    propertyLength = Integer.parseInt(propertyLengthString);
//			                    
//			                    if(propertyLength != 0) {
//			                    	byte[] propertyBytes = read(propertyLength);
//			                    	 
//				                }
//			                }
//			                
//			                
////			                byte propertyArray[] = null;
////			                if(propertyLength != 0)
////			                {
////			                    propertyArray = new byte[propertyLength];
////			                    trace.append("ILSession.readBytes", "Try to reading data msg property.");
////			                    ret = readBytes(propertyArray, propertyArray.length);
////			                    trace.succeed();
////			                }
////			                trace.append(null, "Inner message type:[" + tempMessage.getMessageType() + "]");
//			                
//			                
//			                
//			                switch(tmpMsg.getMessageType()) {
//			                case 2008 : //Inner message type is BASIC.
//			                	if(propertyLength > 0) tmpMsg.setProperty(propertyLenBytes);
//			                	msg.setInnerMessage(tmpMsg);
//			                	break;
//			                	
//			                case 2009 : //Inner message type is TEXT.
//			                	ILTextMessage tmpTextMessage = new ILTextMessage();
//			                    tmpTextMessage.initialize();
//			                    tmpTextMessage.setHeader(tmpBytes);
//			                    if(propertyLength > 0) tmpTextMessage.setProperty(propertyLenBytes);
//			                    byte[] textLenBytes = read(ILTextMessage.dataLengthLength) ;
//			                    tmpTextMessage.setDataLength(textLenBytes);
//			                    byte[] textBytes = read(tmpTextMessage.getDataLength()) ;
//			                    tmpTextMessage.setText(new String(textBytes, 0, tmpTextMessage.getDataLength()));
//			                    msg.setInnerMessage(tmpTextMessage);
//			                	break;
//			                	
//			                case 2010 : //Inner message type is BYTES.
//			                	
//			                	ILBytesMessage innerMsg = new ILBytesMessage();
//			                    innerMsg.initialize();
//			                    innerMsg.setHeader(tmpBytes);
//			                    if(propertyLength > 0) innerMsg.setProperty(propertyLenBytes);
//			                    
//			                    byte[] dataLengthBytes = read(ILTextMessage.dataLengthLength);
//			                    String dataLengthString = new String(dataLengthBytes);
//			                    dataLengthString = ILMessage.truncateCString(dataLengthString);
//			                    int dataLength =  Integer.parseInt(dataLengthString);
//			                    
//			                    byte[] byteData = read(dataLength);
//			                    innerMsg.setData(byteData);
//			                    innerMsg.reset();
//			                    msg.setInnerMessage(innerMsg);
//			                     
//			                	break;
//			                	
//			                case 2011 : //Inner message type is STREAM.
//			                	break;
//			                	
//			                case 2012 : //Inner message type is MAP.
//			                	break;
//			                	
//			                case 2013 : //Inner message type is OBJECT.
//			                	break;
//			                	
//			                case 2014 : //Inner message type is BYTES_DATALESS.
//			                	break;
//			                	
//			                default : 
//			                	break;			                		
//			                } 
//							
//						}else {
//							throw new Exception("MSG_TYPE is not " + MSG_TYPE_REPORT);
//						}
//					
//						byte[] delimeter = read(1);        
//				        if(delimeter.length > 0) {
//				            char ch = (char)delimeter[0];
//				            if(ch != '\n')
//				                throw new Exception("Data sync error(150)");
//				        }
//				        
//				        ILMessage trace = (ILMessage)msg.getContents();
//				        trace.setMode(0);
////				        if(!mParentSession.getTransacted())
////				            if(mParentSession.getAcknowledgeMode() == 1)
////				                mParentSession.commitHelper();
////				            else
////				            if(mParentSession.getAcknowledgeMode() == 3)
////				                mParentSession.commitHelper();
////				            else
////				            if(mParentSession.getAcknowledgeMode() == 2)
////				                dataMessage.setParentSession(mParentSession);
////				        ilmessage = dataMessage;
////				        mStopped = true;
////				        return ilmessage;
//				        
//						
//				        consumer.consume(trace);
//				        
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					
//					
//				}
//
//				@Override
//				public void failed(Throwable exc, Map<String, Object> attachment) {
//					// TODO Auto-generated method stub
//					
//				}
//			});	        
//	        buffer.clear();
//		}finally {
//			if(buffer != null) buffer.clear();
//		}
//    }
//	

	byte[] newline = new byte[] { 10 };

	String clientId = "api";
	final static int MSG_TYPE_CONNECT = 3003;
	final static int MSG_TYPE_MSG_GET = 3000;
	final static int MSG_TYPE_REPORT = 2002;
	final static int reportMessageLength = 215;

	public void createSession(boolean transacted, int acknowledgeMode) throws Exception {

		String connectionModeString = transacted ? Integer.toString(0) : Integer.toString(acknowledgeMode);

		ILinkMessage headerMessage = new ILinkMessage(2001);
		ILinkRequestMessage requestMessage = new ILinkRequestMessage(MSG_TYPE_CONNECT, connectionModeString, clientId,
				"");
		headerMessage.setInnerMessage(requestMessage);
		byte[] header = headerMessage.getHeaderByteArray();
		byte[] body = requestMessage.toByteArray();
		byte[] data = new byte[header.length + body.length + newline.length];

		System.arraycopy(header, 0, data, 0, header.length);
		System.arraycopy(body, 0, data, header.length, body.length);
		System.arraycopy(newline, 0, data, header.length + body.length, newline.length);

		write(data);

		byte[] b = read(header.length);
		headerMessage.setHeader(b);

		//logger.debug("header:" + new String(b));
		System.out.println("header:" + new String(b));
		if (headerMessage.getMessageType() == MSG_TYPE_REPORT) {
			byte[] report = read(reportMessageLength);
			ILinkReportMessage reportMessage = new ILinkReportMessage(report);
			headerMessage.setInnerMessage(reportMessage);
//            logger.debug("getArgument1:"+reportMessage.getArgument1());
//            logger.debug("getArgument2:"+reportMessage.getArgument2());
//            logger.debug("getArgument3:"+reportMessage.getArgument3());
//            logger.debug("getReportCode:"+reportMessage.getReportCode());
//            logger.debug("report:" + new String(report));
		} else {
			throw new Exception("MSG_TYPE is not " + MSG_TYPE_REPORT);
		}

		byte[] delimeter = read(1);
		if (delimeter.length > 0) {
			char ch = (char) delimeter[0];
			if (ch != '\n')
				throw new Exception("Data sync error(150)");
		}

	}

	/**
	 * 
	 */
	public void commit() throws Exception {
		ILinkMessage headerMessage = new ILinkMessage(2001);
		ILinkRequestMessage requestMessage = new ILinkRequestMessage(3008, "", "", "");
		headerMessage.setInnerMessage(requestMessage);
		byte[] header = headerMessage.getHeaderByteArray();
		byte[] body = requestMessage.toByteArray();
		byte[] data = new byte[header.length + body.length + newline.length];

		System.arraycopy(header, 0, data, 0, header.length);
		System.arraycopy(body, 0, data, header.length, body.length);
		System.arraycopy(newline, 0, data, header.length + body.length, newline.length);

		write(data);
		 
		
		ILinkMessage msg = new ILinkMessage(0);
		byte[] b = read(msg.getHeaderLength());
		msg.setHeader(b); 

		//if (msg.getMessageType() == 2002) {
			//throw new Exception("이건 메시지가 아니야 ");
			byte[] report = read(reportMessageLength); 
			ILinkReportMessage reportMessage = new ILinkReportMessage(report);
			msg.setInnerMessage(reportMessage);
		//}
			
			
	}

	public static void pause() throws IOException {
		System.in.read();
	}

	Consumer consumer;

	/**
	 * 
	 */
	public void createConsumer(Consumer consumer) {
		this.consumer = consumer;

	}

	String queueName = "TRACE.EQ";
	Thread consumerThread;
	boolean consumerIsStop = false;
	/*
	 * public void startConsumer() throws Exception{ consumerThread = new Thread();
	 * consumerIsStop = false; //while(!Thread.currentThread().isInterrupted() &&
	 * !consumerIsStop) {
	 * 
	 * 
	 * ILinkMessage headerMessage = new ILinkMessage(2001); ILinkRequestMessage
	 * bodyMessage = new ILinkRequestMessage(3000, "", queueName, "-99");
	 * 
	 * 
	 * byte[] header; byte[] body;
	 * 
	 * 
	 * headerMessage.setInnerMessage(bodyMessage); header =
	 * headerMessage.getHeaderByteArray(); body = bodyMessage.toByteArray();
	 * 
	 * byte[] data = new byte[header.length + body.length + newline.length];
	 * 
	 * System.arraycopy(header, 0, data, 0, header.length); System.arraycopy(body,
	 * 0, data, header.length, body.length); System.arraycopy(newline, 0, data,
	 * header.length + body.length, newline.length);
	 * 
	 * consume(data); //} }
	 */
	ILinkMessage headerMessage = new ILinkMessage(2001);
	ILinkRequestMessage bodyMessage = null;
	byte[] getMessageData;

	public void startConsumer() throws Exception {
		byte[] header;
		byte[] body;

		headerMessage.setInnerMessage(bodyMessage);
		header = headerMessage.getHeaderByteArray();
		body = bodyMessage.toByteArray();

		getMessageData = new byte[header.length + body.length + newline.length];

		System.arraycopy(header, 0, getMessageData, 0, header.length);
		System.arraycopy(body, 0, getMessageData, header.length, body.length);
		System.arraycopy(newline, 0, getMessageData, header.length + body.length, newline.length);

	}

	public Message getMessage() throws Exception {

//
//		byte[] header;
//		byte[] body;
//		 
//
//		headerMessage.setInnerMessage(bodyMessage);
//		header = headerMessage.getHeaderByteArray();
//		body = bodyMessage.toByteArray();
//
//		byte[] data = new byte[header.length + body.length + newline.length];
//		
//		System.arraycopy(header,  0, data, 0,                           header.length);
//		System.arraycopy(body,    0, data, header.length,               body.length);
//		System.arraycopy(newline, 0, data, header.length + body.length, newline.length);

		return consume(getMessageData);

	}

	public void stopConsumer() throws Exception {
		consumerThread = new Thread();

		consumerIsStop = true;
		if (consumerThread != null) {
			consumerThread.interrupt();
		}

	}

	public static void main1(String[] args) {

		ThroughputMonitor tpm = new ThroughputMonitor(1000);

		long connectionTimeout = 5 * 1000;
		int tryCount = 100;
		String host = "10.10.1.10";
		int port = 10000;
		boolean transacted = true;
		int acknowledgeMode = 0;
		AsynchronousClient ac = null;
		try {
			ac = new AsynchronousClient("c1", host, port, "TRACE.EQ");
			ac.connect(connectionTimeout, tryCount);
			ac.createSession(transacted, acknowledgeMode);
//			ac.createConsumer(new Consumer<Message>() {
//
//				@Override
//				public void consume(Message msg) throws Exception {
//					// TODO Auto-generated method stub
//					tpm.count();
//					BytesMessage trace = (BytesMessage)msg; 
//					byte [] data = new byte[(int) trace.getBodyLength()];
//					trace.readBytes(data);
//					System.out.println("trace:"+trace.getBodyLength() + ":" + new String(data));
//				}
//				
//			});
			tpm.start();
			ac.startConsumer();
			for (int i = 0; i < 2; i++) {
				// long elapsed = System.currentTimeMillis();
				Message msg = ac.getMessage();
				// elapsed = System.currentTimeMillis() - elapsed;
				tpm.count();
				// BytesMessage trace = (BytesMessage)msg;
				// byte [] data = new byte[(int) trace.getBodyLength()];
				// trace.readBytes(data);
				// System.out.println("trace("+elapsed+" ms):"+trace.getBodyLength() + ":" + new
				// String(data));
				// Thread.sleep(1);

			}

			pause();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ac != null)
				ac.stop();
		}
	}
	
	
	public static void main(String[] args) {

		
		ThroughputMonitor tpm = new ThroughputMonitor(1000);

		long connectionTimeout = 5 * 1000;
		int tryCount = 100;
		final String host = args[0];
		final int port = Integer.parseInt(args[1]);
//		if(args == null || args[0].length() == 0) {
//			host = "10.10.1.10";
//		}else {
//			host = args[0];
//		}
		
		final String queueName = args[2];
		 
		boolean transacted = false;
		int acknowledgeMode = 0;
		
		
		
		int testThreadCount  = 1;
		try {
			testThreadCount = Integer.parseInt(args[3]);
		}catch(Exception e) {
			testThreadCount = 1;
		}
		
		for(int i = 0 ; i < testThreadCount; i ++) {
			new Thread(new Runnable() {
				int commitCount = 100;
				int uncommittedCount = 0;
				@Override
				public void run() {
					try {
						AsynchronousClient ac = null;
						ac = new AsynchronousClient("client", host, port, queueName);
						ac.connect(connectionTimeout, tryCount);
						ac.createSession(transacted, acknowledgeMode);
			 
						ac.startConsumer();
						while(true) {
							// long elapsed = System.currentTimeMillis();
							Message msg = ac.getMessage();
//							 BytesMessage trace = (BytesMessage)msg;
//							 byte [] data = new byte[(int) trace.getBodyLength()];
//							 trace.readBytes(data);
//							 System.out.println("trace("+elapsed+" ms):"+trace.getBodyLength() + ":" + new String(data));
							
							tpm.count();
	
//							uncommittedCount++;
//							//this.mParentSession.commitHelper();
//							if (uncommittedCount % commitCount == 0){
//								ac.commit();
//								uncommittedCount = 0;
//							}
						}
	
						 
	
					} catch (Exception e) {
						e.printStackTrace();
					}  
				}
			}).start();
		}
		
		tpm.start();
		
		
	}
	

}
