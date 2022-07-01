package rose.mary.trace.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.*;
import javax.xml.parsers.*;

import com.mococo.link.wrapper.*;

public class ILinkSendReceiveAPI {

	static SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static void main(String args[]) {

		log(" Send & Receive API Start.");

		boolean printInformation = true;

		/* Connection 정보 */
		/* 큐매니저 이름 & IP & Port */
		String queueManagerName = "IIP";
		String queueManagerHost = "10.10.1.10";
		int queueManagerPort = 10000;
		/* 서버 접속 정보(agent이름) */
		String hostName = "A_AGENT";

		int queueManagerMaxConnection = 30;
		/* Queue 정보 */
		/* 송신입력 큐(put Queue) */
		String sendQueueName = "TEST0001.LQ";
		/* 응답 큐(get Queue) */
		String receiveQueueName = "TEST.LQ";
		/* 에러큐(error Queue) */
		String errorQueueName = "DeadMessageQueue";
		/* 모니터링 큐(tracking queue) */
		String trackingQueueName = "DeadMessageQueue";

		/* Interface 정보 */
		/* 모니터링정보(그룹ID) */
		String groupID = "ILINKTEST";
		/* 모니터링정보(인터페이스ID) */
		String interfaceID = "DUMMY";
		/* 모니터링정보(송신 프로세스ID) */
		String processID_SNDR = "D_SEND";
		/* 모니터링정보(응답 프로세스ID) */
		String processID_RCVR = "D_RCVR";
		/* 모니터링정보(송신 프로세스 모드) */
		String processMODE_SNDR = "SNDR";
		/* 모니터링정보(응답 프로세스 모드) */
		String processMODE_RCVR = "RCVR";
		String[] dateTime = null;
		/* 수신 시스템 수 */
		String recv_spoke_cnt = "1";
		/* 프로세스 타입 */
		String process_TYPE = "";
		/* 보낼 메시지 데이터 */
		String data = "<?xml version=\"1.0\" encoding=\"EUC-KR\" ?>"
				+ "<data><JUMIN><TRAN_CD> </TRAN_CD></JUMIN></data>";

		if (printInformation) {

			log(" Connection Information {");
			log("  + ILink Queue Manager : " + queueManagerHost + ":" + queueManagerPort + "/" + queueManagerName);
			log("    - Send Queue : " + sendQueueName);
			log("    - Receive Queue : " + receiveQueueName);
			log("    - Error Queue : " + errorQueueName);
			log("    - Tracking Queue : " + trackingQueueName);
			log("  + Interface : " + groupID + "." + interfaceID + "." + processID_SNDR + "/" + processID_RCVR
					+ "{SEND/RECEIVE)");
			log(" }");
		}

		LinkConnectionManager connectionManager = null;
		ILinkHelper helper = null;

		TrackingMessage senderTrackingMessageObject = null;
		BytesMessage sendMessage = null;

		String messageID = "";
		/* ILink 커넥션 설정 */
		{

			connectionManager = LinkConnectionManager.getInstance();

			try {
				connectionManager.createConnectionPool(queueManagerName, queueManagerHost, queueManagerPort,
						queueManagerMaxConnection);
				helper = new ILinkHelper(hostName, queueManagerName);
				helper.init();
				log(" Connection Pool Created (" + queueManagerMaxConnection + ")EA");
			} catch (LinkWrapperException e) {
				e.printStackTrace();
				return;
			} catch (JMSException e) {
				e.printStackTrace();
				return;
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		/* Message 전송 */
		{

			/**
			 * 메시지 전송 순서 0. 메시지 전송 Helper 클래스 초기화 1. 메시지 헤더 생성 { 1) GroupID 설정 2)
			 * InterfaceID 설정 3) TaskID(UNIT FLOW 명) 설정 4) ProcessID 설정 5) ProcessMode 설정 }
			 * 2. 헤더 + 보낼 데이터 = 메시지 생성 3. 메시지 전송 4. 트래킹 메시지 전송
			 */
			BytesMessage trackingMessage = null;

			try {
				// 1. 메시지 헤더 생성
				senderTrackingMessageObject = helper.createTrackingMessage();
				{

					dateTime = senderTrackingMessageObject.getDateTime();
					senderTrackingMessageObject.setInterfaceDateTime(dateTime[0], dateTime[1]);
					// 모니터링정보(송신 GroupID, InterfaceID , ProcessID) 설정
					senderTrackingMessageObject.setSendProperties(groupID, interfaceID, processID_SNDR);
					// 모니터링정보(송신 ProcessInfo) 설정
					senderTrackingMessageObject.setProcessInfo(processID_SNDR, processMODE_SNDR, process_TYPE,
							recv_spoke_cnt, dateTime[0], dateTime[1]);
					// 상태 설정 (0:정상 , 99:에러)
					senderTrackingMessageObject.setStatus("00");

					trackingMessage = senderTrackingMessageObject.createSenderTrackingMessage();
					log(" Create Tracking Message - Complete.");

				}
				// 2. 헤더 + 보낼 데이터 = 메시지 생성
				sendMessage = helper.createSenderDataMessage(senderTrackingMessageObject, data.trim().getBytes("EUC-KR"));
				messageID = sendMessage.getJMSMessageID();
				log(" Create Send Message - Complete.");
				// 3. 메시지 전송
//                     helper.putMessage(sendQueueName, sendMessage,0);
				// Msg Expiration time 설정 시 (ms).
				//helper.putMessage(sendQueueName, sendMessage, 5000);
				
				
				//for (int i=1; i<10 ; i++) {
				//sendQueueName = "TEST000"+i+".LQ";
				//helper.putMessage(sendQueueName, sendMessage, 5000);
				//}
				
				//for (int i=10; i<100 ; i++) {
				//	sendQueueName = "TEST00"+i+".LQ";
				//	helper.putMessage(sendQueueName, sendMessage, 5000);
				//}
				
				//for (int i=100; i<200 ; i++) {
				//	sendQueueName = "TEST0"+i+".LQ";
				//	helper.putMessage(sendQueueName, sendMessage, 5000);
				//}
				
				log("start");
				long startTime = System.currentTimeMillis();
				for (int i=1; i<1000 ; i++) {
					
					//String ind = String.format("%04d", i);
					
					sendQueueName = "TEST0001.LQ";
					helper.putMessage(sendQueueName, sendMessage, 5000);
				}
				log("elapsed:" + (System.currentTimeMillis() - startTime));
				
				
				

				//log(" Send Message - Complete -> " + sendMessage.getJMSMessageID());
				// 4. 트래킹 메시지 전송
				//helper.putMessage(trackingQueueName, trackingMessage);
				//log(" Send Tracking Message - Complete -> " + trackingMessage.getJMSMessageID());

				helper.commit();
				log(" Process Complete.");
			
			
			
			
			
			
			} catch (Exception e) {

				log(e.getMessage());
				try {
//                         helper.putMessage(errorQueueName, sendMessage);

					TrackingMessage trackingMessageObject = helper.createTrackingMessage();
					{
						dateTime = trackingMessageObject.getDateTime();
						trackingMessageObject.setInterfaceDateTime(dateTime[0], dateTime[1]);
						// 모니터링정보(송신 GroupID, InterfaceID , ProcessID) 설정
						trackingMessageObject.setSendProperties(groupID, interfaceID, processID_SNDR);
						// 모니터링정보(송신 ProcessInfo) 설정
						trackingMessageObject.setProcessInfo(processID_SNDR, processMODE_SNDR, process_TYPE,
								recv_spoke_cnt, dateTime[0], dateTime[1]);
						// 상태 설정 (0:정상 , 99:에러)
						trackingMessageObject.setStatus("99");
						// 에러 내용
						trackingMessageObject.setErrorMessage(e.getMessage());
						trackingMessage = trackingMessageObject.createSenderTrackingMessage();
						log(" Create Error Tracking Message - Complete.");
					}
					helper.putMessage(trackingQueueName, trackingMessage);
					helper.commit();
					log(" Send Error Tracking Message - Complete - Complete.");
				} catch (Exception e1) {
					log(" Error Message Send Error. Cause, " + e1.getMessage());
				}
			}
		}
		/* Message 수신 */
		/*
		 * {
		 * 
		 *//**
			 * 메시지 수신 순서 0. 메시지 수신 Helper 클래스 초기화 1. 메시지 수신 4. 트래킹 메시지 전송
			 *//*
				 * BytesMessage trackingMessage = null; BytesMessage receiveMessage = null; try
				 * {
				 * 
				 * // 응답대기시간 (1000 * 초) long waitInteval = 1000 * 10; // long waitInteval = -1;
				 * // 무한 대기
				 * 
				 * // 3. 메시지 전송
				 * 
				 * // receiveMessage = helper.getCorrelationMessage(receiveQueueName,
				 * waitInteval, messageID); receiveMessage = helper.getMessage(receiveQueueName,
				 * waitInteval, messageID);
				 * 
				 * log(" Receive Message - Complete -> " + receiveMessage.getJMSMessageID());
				 * 
				 * receiveMessage.reset(); data = helper.getData(receiveMessage); log(" DATA ["
				 * + data + "]"); // 4. 트래킹 메시지 전송 TrackingMessage trackingMessageObject =
				 * helper.createTrackingMessage(); {
				 * 
				 * // 모니터링정보(응답GroupID, InterfaceID , ProcessID) 설정
				 * trackingMessageObject.setSendProperties(groupID, interfaceID,
				 * processID_RCVR); trackingMessageObject.setInterfaceDateTime(dateTime[0],
				 * dateTime[1]); // 모니터링정보(응답ProcessMode) 설정
				 * trackingMessageObject.setProcessMode(processMODE_RCVR); // 상태 설정 (0:정상 ,
				 * 99:에러) trackingMessageObject.setStatus("00"); receiveMessage.reset();
				 * trackingMessage =
				 * trackingMessageObject.createTrackingMessage(receiveMessage);
				 * log(" Create Tracking Message - Complete."); }
				 * helper.putMessage(trackingQueueName, trackingMessage);
				 * log(" Send Tracking Message - Complete -> " +
				 * trackingMessage.getJMSMessageID()); helper.commit();
				 * log(" Process Complete."); } catch (Exception e) {
				 * 
				 * log(e.getMessage()); try { helper.rollback();
				 * 
				 * if (e instanceof LinkWrapperException) { if (((LinkWrapperException)
				 * e).getReturnCode() == 2300) { log(" Message TimeOut"); } TrackingMessage
				 * trackingMessageObject = helper.createTrackingMessage(); Sender Tracking
				 * Message 로 Tracking 생성 {
				 * 
				 * // 모니터링정보(응답GroupID, InterfaceID , ProcessID) 설정
				 * trackingMessageObject.setSendProperties(groupID, interfaceID,
				 * processID_RCVR); trackingMessageObject.setInterfaceDateTime(dateTime[0],
				 * dateTime[1]); // 모니터링정보(응답ProcessMode) 설정
				 * trackingMessageObject.setProcessMode(processMODE_RCVR); // 상태 설정 (0:정상 ,
				 * 99:에러) trackingMessageObject.setStatus("99"); // 에러 내용
				 * trackingMessageObject.setErrorMessage(e.getMessage()); trackingMessage =
				 * trackingMessageObject.createSenderTrackingMessage();
				 * log(" Create Tracking Message - Complete."); } }
				 * helper.putMessage(trackingQueueName, trackingMessage); } catch (Exception e1)
				 * { log(" Error Message Send Error. Cause, " + e1.getMessage()); } } }
				 */
		/* 커넥션 정리 */
		{

			// Commit
			try {
				helper.commit();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LinkWrapperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				helper.release();
				log(" Connection Released.");
			}
		}

	}
	
	
	public static void mainbak(String args[]) {

		log(" Send & Receive API Start.");

		boolean printInformation = true;

		/* Connection 정보 */
		/* 큐매니저 이름 & IP & Port */
		String queueManagerName = "IIP";
		String queueManagerHost = "10.10.1.10";
		int queueManagerPort = 10000;
		/* 서버 접속 정보(agent이름) */
		String hostName = "A_AGENT";

		int queueManagerMaxConnection = 30;
		/* Queue 정보 */
		/* 송신입력 큐(put Queue) */
		String sendQueueName = "TEST0001.LQ";
		/* 응답 큐(get Queue) */
		String receiveQueueName = "TEST.LQ";
		/* 에러큐(error Queue) */
		String errorQueueName = "DeadMessageQueue";
		/* 모니터링 큐(tracking queue) */
		String trackingQueueName = "DeadMessageQueue";

		/* Interface 정보 */
		/* 모니터링정보(그룹ID) */
		String groupID = "ILINKTEST";
		/* 모니터링정보(인터페이스ID) */
		String interfaceID = "DUMMY";
		/* 모니터링정보(송신 프로세스ID) */
		String processID_SNDR = "D_SEND";
		/* 모니터링정보(응답 프로세스ID) */
		String processID_RCVR = "D_RCVR";
		/* 모니터링정보(송신 프로세스 모드) */
		String processMODE_SNDR = "SNDR";
		/* 모니터링정보(응답 프로세스 모드) */
		String processMODE_RCVR = "RCVR";
		String[] dateTime = null;
		/* 수신 시스템 수 */
		String recv_spoke_cnt = "1";
		/* 프로세스 타입 */
		String process_TYPE = "";
		/* 보낼 메시지 데이터 */
		String data = "<?xml version=\"1.0\" encoding=\"EUC-KR\" ?>"
				+ "<data><JUMIN><TRAN_CD> </TRAN_CD></JUMIN></data>";

		if (printInformation) {

			log(" Connection Information {");
			log("  + ILink Queue Manager : " + queueManagerHost + ":" + queueManagerPort + "/" + queueManagerName);
			log("    - Send Queue : " + sendQueueName);
			log("    - Receive Queue : " + receiveQueueName);
			log("    - Error Queue : " + errorQueueName);
			log("    - Tracking Queue : " + trackingQueueName);
			log("  + Interface : " + groupID + "." + interfaceID + "." + processID_SNDR + "/" + processID_RCVR
					+ "{SEND/RECEIVE)");
			log(" }");
		}

		LinkConnectionManager connectionManager = null;
		ILinkHelper helper = null;

		TrackingMessage senderTrackingMessageObject = null;
		BytesMessage sendMessage = null;

		String messageID = "";
		/* ILink 커넥션 설정 */
		{

			connectionManager = LinkConnectionManager.getInstance();

			try {
				connectionManager.createConnectionPool(queueManagerName, queueManagerHost, queueManagerPort,
						queueManagerMaxConnection);
				helper = new ILinkHelper(hostName, queueManagerName);
				helper.init();
				log(" Connection Pool Created (" + queueManagerMaxConnection + ")EA");
			} catch (LinkWrapperException e) {
				e.printStackTrace();
				return;
			} catch (JMSException e) {
				e.printStackTrace();
				return;
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		/* Message 전송 */
		{

			/**
			 * 메시지 전송 순서 0. 메시지 전송 Helper 클래스 초기화 1. 메시지 헤더 생성 { 1) GroupID 설정 2)
			 * InterfaceID 설정 3) TaskID(UNIT FLOW 명) 설정 4) ProcessID 설정 5) ProcessMode 설정 }
			 * 2. 헤더 + 보낼 데이터 = 메시지 생성 3. 메시지 전송 4. 트래킹 메시지 전송
			 */
			BytesMessage trackingMessage = null;

			try {
				// 1. 메시지 헤더 생성
				senderTrackingMessageObject = helper.createTrackingMessage();
				{

					dateTime = senderTrackingMessageObject.getDateTime();
					senderTrackingMessageObject.setInterfaceDateTime(dateTime[0], dateTime[1]);
					// 모니터링정보(송신 GroupID, InterfaceID , ProcessID) 설정
					senderTrackingMessageObject.setSendProperties(groupID, interfaceID, processID_SNDR);
					// 모니터링정보(송신 ProcessInfo) 설정
					senderTrackingMessageObject.setProcessInfo(processID_SNDR, processMODE_SNDR, process_TYPE,
							recv_spoke_cnt, dateTime[0], dateTime[1]);
					// 상태 설정 (0:정상 , 99:에러)
					senderTrackingMessageObject.setStatus("00");

					trackingMessage = senderTrackingMessageObject.createSenderTrackingMessage();
					log(" Create Tracking Message - Complete.");

				}
				// 2. 헤더 + 보낼 데이터 = 메시지 생성
				sendMessage = helper.createSenderDataMessage(senderTrackingMessageObject, data.trim().getBytes("EUC-KR"));
				messageID = sendMessage.getJMSMessageID();
				log(" Create Send Message - Complete.");
				// 3. 메시지 전송
//                     helper.putMessage(sendQueueName, sendMessage,0);
				// Msg Expiration time 설정 시 (ms).
				//helper.putMessage(sendQueueName, sendMessage, 5000);
				
				
				//for (int i=1; i<10 ; i++) {
				//sendQueueName = "TEST000"+i+".LQ";
				//helper.putMessage(sendQueueName, sendMessage, 5000);
				//}
				
				//for (int i=10; i<100 ; i++) {
				//	sendQueueName = "TEST00"+i+".LQ";
				//	helper.putMessage(sendQueueName, sendMessage, 5000);
				//}
				
				//for (int i=100; i<200 ; i++) {
				//	sendQueueName = "TEST0"+i+".LQ";
				//	helper.putMessage(sendQueueName, sendMessage, 5000);
				//}
				
				for (int i=1; i<1000 ; i++) {
					
					String ind = String.format("%04d", i);
					
					sendQueueName = "TEST"+ind+".LQ";
					helper.putMessage(sendQueueName, sendMessage, 5000);
				}
				
				
				

				log(" Send Message - Complete -> " + sendMessage.getJMSMessageID());
				// 4. 트래킹 메시지 전송
				//helper.putMessage(trackingQueueName, trackingMessage);
				//log(" Send Tracking Message - Complete -> " + trackingMessage.getJMSMessageID());

				helper.commit();
				log(" Process Complete.");
			
			
			
			
			
			
			} catch (Exception e) {

				log(e.getMessage());
				try {
//                         helper.putMessage(errorQueueName, sendMessage);

					TrackingMessage trackingMessageObject = helper.createTrackingMessage();
					{
						dateTime = trackingMessageObject.getDateTime();
						trackingMessageObject.setInterfaceDateTime(dateTime[0], dateTime[1]);
						// 모니터링정보(송신 GroupID, InterfaceID , ProcessID) 설정
						trackingMessageObject.setSendProperties(groupID, interfaceID, processID_SNDR);
						// 모니터링정보(송신 ProcessInfo) 설정
						trackingMessageObject.setProcessInfo(processID_SNDR, processMODE_SNDR, process_TYPE,
								recv_spoke_cnt, dateTime[0], dateTime[1]);
						// 상태 설정 (0:정상 , 99:에러)
						trackingMessageObject.setStatus("99");
						// 에러 내용
						trackingMessageObject.setErrorMessage(e.getMessage());
						trackingMessage = trackingMessageObject.createSenderTrackingMessage();
						log(" Create Error Tracking Message - Complete.");
					}
					helper.putMessage(trackingQueueName, trackingMessage);
					helper.commit();
					log(" Send Error Tracking Message - Complete - Complete.");
				} catch (Exception e1) {
					log(" Error Message Send Error. Cause, " + e1.getMessage());
				}
			}
		}
		/* Message 수신 */
		/*
		 * {
		 * 
		 *//**
			 * 메시지 수신 순서 0. 메시지 수신 Helper 클래스 초기화 1. 메시지 수신 4. 트래킹 메시지 전송
			 *//*
				 * BytesMessage trackingMessage = null; BytesMessage receiveMessage = null; try
				 * {
				 * 
				 * // 응답대기시간 (1000 * 초) long waitInteval = 1000 * 10; // long waitInteval = -1;
				 * // 무한 대기
				 * 
				 * // 3. 메시지 전송
				 * 
				 * // receiveMessage = helper.getCorrelationMessage(receiveQueueName,
				 * waitInteval, messageID); receiveMessage = helper.getMessage(receiveQueueName,
				 * waitInteval, messageID);
				 * 
				 * log(" Receive Message - Complete -> " + receiveMessage.getJMSMessageID());
				 * 
				 * receiveMessage.reset(); data = helper.getData(receiveMessage); log(" DATA ["
				 * + data + "]"); // 4. 트래킹 메시지 전송 TrackingMessage trackingMessageObject =
				 * helper.createTrackingMessage(); {
				 * 
				 * // 모니터링정보(응답GroupID, InterfaceID , ProcessID) 설정
				 * trackingMessageObject.setSendProperties(groupID, interfaceID,
				 * processID_RCVR); trackingMessageObject.setInterfaceDateTime(dateTime[0],
				 * dateTime[1]); // 모니터링정보(응답ProcessMode) 설정
				 * trackingMessageObject.setProcessMode(processMODE_RCVR); // 상태 설정 (0:정상 ,
				 * 99:에러) trackingMessageObject.setStatus("00"); receiveMessage.reset();
				 * trackingMessage =
				 * trackingMessageObject.createTrackingMessage(receiveMessage);
				 * log(" Create Tracking Message - Complete."); }
				 * helper.putMessage(trackingQueueName, trackingMessage);
				 * log(" Send Tracking Message - Complete -> " +
				 * trackingMessage.getJMSMessageID()); helper.commit();
				 * log(" Process Complete."); } catch (Exception e) {
				 * 
				 * log(e.getMessage()); try { helper.rollback();
				 * 
				 * if (e instanceof LinkWrapperException) { if (((LinkWrapperException)
				 * e).getReturnCode() == 2300) { log(" Message TimeOut"); } TrackingMessage
				 * trackingMessageObject = helper.createTrackingMessage(); Sender Tracking
				 * Message 로 Tracking 생성 {
				 * 
				 * // 모니터링정보(응답GroupID, InterfaceID , ProcessID) 설정
				 * trackingMessageObject.setSendProperties(groupID, interfaceID,
				 * processID_RCVR); trackingMessageObject.setInterfaceDateTime(dateTime[0],
				 * dateTime[1]); // 모니터링정보(응답ProcessMode) 설정
				 * trackingMessageObject.setProcessMode(processMODE_RCVR); // 상태 설정 (0:정상 ,
				 * 99:에러) trackingMessageObject.setStatus("99"); // 에러 내용
				 * trackingMessageObject.setErrorMessage(e.getMessage()); trackingMessage =
				 * trackingMessageObject.createSenderTrackingMessage();
				 * log(" Create Tracking Message - Complete."); } }
				 * helper.putMessage(trackingQueueName, trackingMessage); } catch (Exception e1)
				 * { log(" Error Message Send Error. Cause, " + e1.getMessage()); } } }
				 */
		/* 커넥션 정리 */
		{

			// Commit
			try {
				helper.commit();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LinkWrapperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				helper.release();
				log(" Connection Released.");
			}
		}

	}

	public static void log(String message) {

		message = "[" + formater.format(new Date()) + "]" + message;
		System.out.println(message);

//		try {
//
//			File logFile = new File("ILinkAPI.log");
//			{
//				if (!logFile.exists()) {
//					logFile.createNewFile();
//				}
//			}
//
//			FileOutputStream fis = new FileOutputStream(logFile, true);
//			fis.write(message.getBytes());
//			fis.write(System.getProperty("line.separator").getBytes());
//			fis.flush();
//			fis.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}