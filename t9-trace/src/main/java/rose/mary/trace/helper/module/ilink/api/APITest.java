/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.helper.module.ilink.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Queue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.mococo.ILinkAPI.jms.ILinkMessage;
import com.mococo.ILinkAPI.jms.ILinkRequestMessage;

import pep.per.mint.common.util.Util;
import rose.mary.trace.monitor.ThroughputMonitor;
 

/**
 * <pre>
 * rose.mary.trace.ilink.api
 * APITest.java
 * </pre>
 * @author whoana
 * @date Aug 20, 2019
 */
public class APITest {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	public static void main(String [] args) {
		try {
			Queue q = new ConcurrentLinkedQueue(); 
			String host = "10.10.1.10";
			int port = 10000; 
			int readTimeOut = 10;
			int queueLimitSize = 10000;
			APITest test = new APITest(host,port,q, readTimeOut, queueLimitSize);

			
			while(true) {
				try {
					test.start();
					break;
				}catch(Exception e) {
					e.printStackTrace();
					Thread.sleep(1000);
				}
			}
			
			
			
			
			
			
			
			ILinkMessage messageBuffer = new ILinkMessage(2001);
			ILinkRequestMessage requestMessage = new ILinkRequestMessage(3008, "", "", "");
			messageBuffer.setInnerMessage(requestMessage);
			byte[] header = messageBuffer.getHeaderByteArray();
			byte[] body = requestMessage.toByteArray();
			
			
			StringBuffer sb = new StringBuffer();
			sb.append(Util.rightPad("25",10," "));
			sb.append(Util.rightPad("2001",5," "));
			sb.append(Util.rightPad("255",10," "));
			sb.append(Util.rightPad("3003",5," "));
			sb.append(Util.rightPad("0",70," "));
			sb.append(Util.rightPad("whoana",70," "));
			sb.append(Util.rightPad("",70," "));
			sb.append(Util.rightPad("0",10," "));
			String msg = sb.toString();
			System.out.println("send msg [" + msg.getBytes().length + "][" + msg + "]");
			System.out.println("send msg [" + (header.length + body.length) + "][" + new String(header) + new String(body) + "]");
			
		 	
			System.out.println("start...");
			ThroughputMonitor tpm = new ThroughputMonitor(1000);
			test.listen(new MessageListener() {
				
				int commitCount = 100;
				int uncommittedCount = 0;
				
				@Override
				public void onMessage(Object msg) throws Exception {
					
					
					//System.out.println("receive msg [" + ((byte[])msg).length + "][" + new String(((byte[])msg)) + "]");
					if(msg == null ) return;

					System.out.println("receive msg [" + ((byte[])msg).length + "][" + new String(((byte[])msg),0,25) + "]");
					
					ILinkMessage linkMessage = new ILinkMessage(0);
					linkMessage.setHeader((byte[])msg);
					
					//ILinkReportMessage reportMessage = (ILinkReportMessage) linkMessage.getContents();
					
					//System.out.println("linkMessage.getMessageType():"+linkMessage.getMessageType());
					//System.out.println("reportMessage.getReportCode():"+reportMessage.getReportCode());
					
//					if (linkMessage.getMessageType() != 2000) {
//						if (linkMessage.getMessageType() != 2002) {
//							return;
//						}
//
//						ILinkReportMessage reportMessage = (ILinkReportMessage) linkMessage.getContents();
//						int code = reportMessage.getReportCode();
//						String errorCode = reportMessage.getArgument1();
//						String errorMessage = reportMessage.getArgument2();
//						throw new Exception(errorMessage+"("+errorCode+")");
//					}
					if(linkMessage.getMessageType() == 2000) {
						//ILMessage dataMessage = (ILMessage) linkMessage.getContents();
						//dataMessage.setMode(0);
						tpm.count();
						uncommittedCount++;
						//this.mParentSession.commitHelper();
						if (uncommittedCount % commitCount == 0){
							ILinkMessage messageBuffer = new ILinkMessage(2001);
							ILinkRequestMessage requestMessage = new ILinkRequestMessage(3008, "", "", "");
							messageBuffer.setInnerMessage(requestMessage);
							byte[] header = messageBuffer.getHeaderByteArray();
							byte[] body = requestMessage.toByteArray();
							
							test.send(header);
							test.send(body);
							byte[] newline = new byte[]{10};
							test.send(newline);		
							
							uncommittedCount = 0;
							 
						}
					}
					
				}
			});
			
			
			System.out.println("listen...");
			
			test.send(header);
			test.send(body);
			byte[] newline = new byte[]{10};
			test.send(newline);
			
			
			//read message
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true) {
						try {
							ILinkMessage getRequestMessage = new ILinkMessage(2001);
							ILinkRequestMessage requestMessage = new ILinkRequestMessage(3000, "", "TRACE.EQ", "-99");
							
	
							byte[] header;
							byte[] body;
							 
	
							getRequestMessage.setInnerMessage(requestMessage);
							header = getRequestMessage.getHeaderByteArray();
							body = requestMessage.toByteArray();
	
							test.send(header);
							test.send(body);
							byte[] newline = new byte[]{10};
							test.send(newline);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
//							try {
//								Thread.sleep(10);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						}
					}
				}
			}).start();

			tpm.start();
			
			//ping message 
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					while(true)
//					try {
//						ILinkMessage mimqMessage = new ILinkMessage(2001);
//						ILinkRequestMessage requestMessage = new ILinkRequestMessage(3029, "", "", "");
//						mimqMessage.setInnerMessage(requestMessage);
//						byte[] header = mimqMessage.getHeaderByteArray();
//						byte[] body = requestMessage.toByteArray();
//							test.send(header);
//						test.send(body);
//						byte[] newline = new byte[]{10};
//						test.send(newline);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} finally {
//						try {
//							Thread.sleep(3*1000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					
//				}
//			}).start();
			
			Thread.sleep(Integer.MAX_VALUE);
			
//			while(true) {
//				Scanner sc = new Scanner(System.in);
//				String string = sc.nextLine();
//				System.out.println("input data: " + string);
//				test.send(string.getBytes());
//			}
			
			
//			헤더길이	 	string	  0	10	"25"
//			헤더유형	    string	 10	 5	"2001"
//			바디길이		string	 15	10	"225"
//			OP코드		string	  0	 5	"3003"
//			ARG1		string	  5	70	"0"(연결모드)
//			ARG2		string	 75	70	""(클라이어트ID)
//			ARG3		string	145	70	""
//			확장데이터길이	string	215	10	"0"
//			
			
		}catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	
	

	
	/**
	 * 읽기 버퍼 최대 사이즈 설정 
	 *  
	 */
	static final int READ_BUFFER_CAPACITY = 10000000;
	
	/**
	 * queue 사이즈 산정 
	 *    메시지 당 사이즈 : 1000 bytes(1k)
	 *    메시지 건수 = 1000 개 
	 *    큐 사이즈 =  메시지 당 사이즈 * 메시지 건수 = 1000 bytes * 1000 개 = 1M
	 */	
	static final int DEFAULT_Q_LIMIT_SZ = 100000;
	private int queueLimitSize = DEFAULT_Q_LIMIT_SZ;
	
	private AsynchronousSocketChannel client;
	private String host;
	private int port;
	private Future<Void> future;
	private Queue<byte[]> receiveQueue;
	Thread listenerThread = null;
	private int readTimeOut = 10;
	//private ConnectionCheckListener connectionCheckListener;
	private boolean systemout = true;
	private boolean isStop = false;

	
	/**
	 * 
	 * @param host 접속 서버 IP 
	 * @param port 접속 서버 포트 
	 * @param receiveQueue 서버로부터 수신받은 메시지 저장을 위한 큐 
	 * @param readTimeOut  블락킹 소켓 리드 타임아웃 설정 값 (밀리세컨드값)   
	 */
	public APITest(String host, int port, Queue<byte[]> receiveQueue, int readTimeOut) {
		this(host,port,receiveQueue, readTimeOut, DEFAULT_Q_LIMIT_SZ);
	}
	
	/**
	 * 
	 * @param host 접속 서버 IP 
	 * @param port 접속 서버 포트 
	 * @param receiveQueue 서버로부터 수신받은 메시지 저장을 위한 큐 
	 * @param readTimeOut  블락킹 소켓 리드 타임아웃 설정 값 (밀리세컨드값)   
	 * @param queueLimitSize 수신메시지 큐 최대 사이즈 (기본값 1000)
	 */
	public APITest(String host, int port, Queue<byte[]> receiveQueue, int readTimeOut, int queueLimitSize) {
		this.host = host;
		this.port = port;
		this.receiveQueue = receiveQueue;
		this.readTimeOut = readTimeOut;
		this.queueLimitSize = queueLimitSize;
	}
	
	/**
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void start() throws IOException {
		
		
		if(listenerThread != null) {
			listenerThread.interrupt();
			listenerThread = null;
		}
		
		if(client != null) {
			try {client.close(); } catch (IOException ex) {}
			client = null;
		}
		
		try {
			client = AsynchronousSocketChannel.open();
	        InetSocketAddress hostAddress = new InetSocketAddress(host, port);
	        future = client.connect(hostAddress);
			future.get();
			isStop = false;
		} catch (Exception e) {
			throw new IOException(e);
		}  
	}
	
	
	
	
	/**
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void listen(MessageListener ml) {

		
		final ByteBuffer buffer = ByteBuffer.allocate(READ_BUFFER_CAPACITY);

		listenerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(!Thread.currentThread().isInterrupted()) {
					try { 
						if(isStop) { 
							if(logger != null) {
								logger.debug("socket client stopping." );
							}else {
								if(systemout) System.out.println("socket client stopping.");
							}
							
							break;
						}
						Future<Integer> readResult = client.read(buffer);
						Integer res = readResult.get();
						if(res == -1) {
							if(logger != null) {
								logger.debug("connecction closed on reading:" + res);
							}else {
								if(systemout) System.out.println("connecction closed on reading:" + res);
							}
//							if(connectionCheckListener != null) {
//								connectionCheckListener.check();
//							}else {
								try {client.close(); } catch (IOException ex) {}
								client = null;//isConnected 에서 null 체크로직을 사용하므로 
								break;
//							} 
						}
						//----------------------------------------------------------
						//버퍼사이즈만큼 읽어들여서 수정후 주석처리함.
						//byte [] data = buffer.array();
						//----------------------------------------------------------
						buffer.flip();
						byte [] data = new byte[buffer.limit()];
						buffer.get(data);
//						if(receiveQueue.size() >= queueLimitSize) {
//							byte[] b = receiveQueue.remove();
//							if(logger != null) {
//								logger.debug("reached queue max limit:" + new String(b));
//							}else {
//								if(systemout) System.out.println("reached queue max limit:" + new String(b));
//							}
//						}
						if(ml != null)
							try {
								ml.onMessage(data);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//						logger.debug( "{dataLength:" + data.length + ", data:" + new String(data) + "}");
//						
//						receiveQueue.offer(data);
//						
//						if(logger != null) {
//							logger.debug( "{class:ksnbct2, method:listen, queueSize:" + receiveQueue.size() + ", dataLength:" + data.length + ", data:" + new String(data) + "}");
//						}else {							
//							if(systemout) {
//								System.out.println("{class:ksnbct2, method:listen, queueSize:" + receiveQueue.size() + ", dataLength:" + data.length + ", data:" + new String(data) + "}");
//							}
//						}
					} catch (InterruptedException e) {
						if(logger != null) {
							logger.debug("listener thread stop.");
						}else {	
							if(systemout) System.out.println(this.getClass().getSimpleName() + " listener thread stop.");
						}
						break;
					} catch (ExecutionException e) {
						if(logger != null) {
							logger.error("listener exception", e);
						}else {
							e.printStackTrace();
						}
					} finally {
						buffer.clear();
					}
				}
			}
			
		});
		
		listenerThread.start();
		
		
	}

	/**
	 * 
	 * @param data
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void send(byte [] data) throws IOException { 
		try {
	        ByteBuffer buffer = ByteBuffer.wrap(data);
	        Future<Integer> writeResult = client.write(buffer);
	        writeResult.get();
	        buffer.clear();
		}catch(Exception e) {
			throw new IOException(e);
		}
    }
	
	/**
	 * 
	 */
	public void stop() {
		
		if(logger != null) {
			logger.debug("The client socket will stop.");
		}else {
			if(systemout) System.out.println("The client socket will stop.");
		}
		
		isStop = true;
    	if(listenerThread != null) {
			listenerThread.interrupt();
			listenerThread = null;
		}
		
		if(client != null) {
			try {client.close(); } catch (IOException ex) {}
			client = null;
		}
    	 
    }
	
 

	
	
	
}
