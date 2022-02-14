/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.helper.module.mte;
 
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQSimpleConnectionManager;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.CMQCFC;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.MQRFH2;
import com.ibm.mq.headers.MQRFH2.Element;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFMessageAgent;

import rose.mary.trace.exception.NoMoreMessageException;

/**
 * <pre>
 * rose.mary.helper.module
 * MqClientModule.java
 * </pre>
 * 
 * @author whoana
 * @date Jul 23, 2019
 */
public class MQMsgHandler implements MsgHandler {

	Logger logger = LoggerFactory.getLogger(MQMsgHandler.class);
	
	MQQueueManager qmgr;

	MQQueue queue;

	String hostName;
	String qmgrName;
	int port = 1414;
	String userId;
	String password;
	String channelName;
	int ccsid = 1208;
	int characterSet;
	boolean autoCommit = true;
	boolean bindMode = true;
	
	static MQSimpleConnectionManager myConnMan = new MQSimpleConnectionManager();
	
	public MQMsgHandler(
			String qmgrName, 
			String hostName, 
			int port, 
			String channelName, 
			String userId, 
			String password,
			int ccsid,
			int characterSet,
			boolean autoCommit,
			boolean bindMode) {
		this.qmgrName = qmgrName;
		this.hostName = hostName;
		this.port = port;
		this.channelName = channelName;
		this.userId = userId;
		this.password = password;
		this.ccsid = ccsid;
		this.characterSet = characterSet;
		this.autoCommit = autoCommit;
		this.bindMode = bindMode;

	}

	MQGetMessageOptions gmo = null;
	MQPutMessageOptions pmo = null;
 
	
	@Override
	public synchronized void open(String queueName, int openOpt) throws Exception {
		
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put(CMQC.CHANNEL_PROPERTY, channelName);
		params.put(CMQC.HOST_NAME_PROPERTY, hostName);
		params.put(CMQC.PORT_PROPERTY, new Integer(port));
		params.put(CMQC.CCSID_PROPERTY, ccsid);
		if (userId != null) params.put(CMQC.USER_ID_PROPERTY, userId);
		if (userId != null) params.put(CMQC.PASSWORD_PROPERTY, password);
  
		if(bindMode) {
			params.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES_BINDINGS);//binding mode connect			
		} 
		
		MQException.log = null;
		
		//myConnMan.setActive(MQSimpleConnectionManager.MODE_ACTIVE);
		//qmgr = new MQQueueManager(qmgrName, params, myConnMan);
		qmgr = new MQQueueManager(qmgrName, params);



		if (Q_OPEN_OPT_PUT == openOpt) {
			int openOptions = CMQC.MQOO_OUTPUT + CMQC.MQOO_FAIL_IF_QUIESCING;
			queue = qmgr.accessQueue(queueName, openOptions);

			pmo = new MQPutMessageOptions();
			if (!autoCommit)
				pmo.options = CMQC.MQPMO_SYNCPOINT;

		} else if (Q_QPEN_OPT_GET == openOpt) {
			int openOptions = CMQC.MQOO_INPUT_AS_Q_DEF + CMQC.MQOO_FAIL_IF_QUIESCING;
		 
			queue = qmgr.accessQueue(queueName, openOptions);

			gmo = new MQGetMessageOptions();
			//gmo.options = CMQC.MQGMO_PROPERTIES_FORCE_MQRFH2 + CMQC.MQGMO_FAIL_IF_QUIESCING + CMQC.MQGMO_NO_WAIT;
			//gmo.options = CMQC.MQGMO_PROPERTIES_FORCE_MQRFH2 + CMQC.MQGMO_NO_WAIT;
			gmo.options = CMQC.MQGMO_PROPERTIES_FORCE_MQRFH2 + CMQC.MQGMO_FAIL_IF_QUIESCING + CMQC.MQGMO_WAIT;
			
			if (!autoCommit)
				gmo.options = gmo.options + CMQC.MQGMO_SYNCPOINT;

		} else {
			throw new Exception("UnSupported queue open option:" + openOpt);
		}
	}

	@Override
	// public void put(MTEMessage msg) throws Exception {
	public void put(MTEHeader header, String data) throws Exception {

		// MTEHeader header = msg.getHeader();
		// String data = new String(msg.getData());

		MQMessage putMsg = new MQMessage();
		putMsg.characterSet = characterSet;

		// Set the RFH2 Values
		MQRFH2 rfh2 = new MQRFH2();
		rfh2.setEncoding(CMQC.MQENC_NATIVE);
		rfh2.setCodedCharSetId(CMQC.MQCCSI_INHERIT);
		rfh2.setFormat(CMQC.MQFMT_STRING);
		rfh2.setFlags(0);
		rfh2.setNameValueCCSID(1208);

		// set mcd
		rfh2.setFieldValue(MTEStruct.mcd, MTEStruct.msd, header.getMcdMsd());
		rfh2.setFieldValue(MTEStruct.mcd, MTEStruct.set, header.getMcdSet());
		rfh2.setFieldValue(MTEStruct.mcd, MTEStruct.type,header.getMcdType());
		rfh2.setFieldValue(MTEStruct.mcd, MTEStruct.fmt, header.getMcdFmt());

		Element mteInfo = rfh2.getFolder(MTEStruct.usr, true).addElement(MTEStruct.mte_info);
		// -------------------------------------------------------------------
		// set a:usr.mte_info.interface_info
		// -------------------------------------------------------------------
		Element interfaceInfo = mteInfo.addElement(MTEStruct.a);
		interfaceInfo.setValue(MTEStruct.a_group_id,  header.getaGroupId());
		interfaceInfo.setValue(MTEStruct.a_host_id,   header.getaHostId());
		interfaceInfo.setValue(MTEStruct.a_intf_id,   header.getaIntfId());
		interfaceInfo.setValue(MTEStruct.a_date,      header.getaDate());
		interfaceInfo.setValue(MTEStruct.a_time, 	  header.getaTime());
		interfaceInfo.setValue(MTEStruct.a_global_id, header.getaGlobalId());
		
		// -------------------------------------------------------------------
		// set j:usr.mte_info.prev_host_info
		// -------------------------------------------------------------------
		Element prevHostInfo = mteInfo.addElement(MTEStruct.j);
		prevHostInfo.setValue(MTEStruct.j_host_id,    header.getjHostId());
		prevHostInfo.setValue(MTEStruct.j_process_id, header.getjProcessId());
		
		// -------------------------------------------------------------------
		// set b:usr.mte_info.host_info
		// -------------------------------------------------------------------
		Element hostInfo = mteInfo.addElement(MTEStruct.b);
		hostInfo.setValue(MTEStruct.b_host_id, header.getbHostId());
		hostInfo.setValue(MTEStruct.b_os_type, header.getbOsType());
		hostInfo.setValue(MTEStruct.b_os_version, header.getbOsVersion());
		// -------------------------------------------------------------------
		// set c:usr.mte_info.process_info
		// -------------------------------------------------------------------
		Element processInfo = mteInfo.addElement(MTEStruct.c);
		processInfo.setValue(MTEStruct.c_date, header.getcDate());
		processInfo.setValue(MTEStruct.c_time, header.getcTime());
		processInfo.setValue(MTEStruct.c_process_mode, header.getcProcessMode());
		processInfo.setValue(MTEStruct.c_process_type, header.getcProcessType());
		processInfo.setValue(MTEStruct.c_process_id, header.getcProcessId());
		processInfo.setValue(MTEStruct.c_hub_cnt, header.getcHubCnt());
		processInfo.setValue(MTEStruct.c_spoke_cnt, header.getcSpokeCnt());
		processInfo.setValue(MTEStruct.c_recv_spoke_cnt, header.getcRecvSpokeCnt());
		processInfo.setValue(MTEStruct.c_hop_cnt, header.getcHopCnt());
		processInfo.setValue(MTEStruct.c_appl_type, header.getcApplType());
		processInfo.setValue(MTEStruct.c_timezone, header.getcTimezone());
		processInfo.setValue(MTEStruct.c_elaspsed_time, header.getcElaspsedTime());
		
		// -------------------------------------------------------------------
		// set d:usr.mte_info.status_info
		// -------------------------------------------------------------------
		Element statusInfo = mteInfo.addElement(MTEStruct.d);
		statusInfo.setValue(MTEStruct.d_status, header.getdStatus());
		statusInfo.setValue(MTEStruct.d_error_type, header.getdErrorType());
		statusInfo.setValue(MTEStruct.d_error_code, header.getdErrorCode());
		statusInfo.setValue(MTEStruct.d_error_reason, header.getdErrorReason());
		statusInfo.setValue(MTEStruct.d_error_message, header.getdErrorMessage());
		statusInfo.setValue(MTEStruct.d_errorq_msgid, header.getdErrorqMsgId());
		statusInfo.setValue(MTEStruct.d_targetq, header.getdTargetq());
		// -------------------------------------------------------------------
		// set e:usr.mte_info.sender_info
		// -------------------------------------------------------------------
		Element senderInfo = mteInfo.addElement(MTEStruct.e);
		senderInfo.setValue(MTEStruct.e_replytoqmgr, header.geteReplytoqmgr());
		senderInfo.setValue(MTEStruct.e_replytoq, header.geteReplytoq());
		senderInfo.setValue(MTEStruct.e_file_name, header.geteFileName());
		senderInfo.setValue(MTEStruct.e_directory, header.geteDirectory());
		senderInfo.setValue(MTEStruct.e_extract_pgm, header.geteExtractPgm());
		senderInfo.setValue(MTEStruct.e_description, header.geteDescription());
		// -------------------------------------------------------------------
		// set f:usr.mte_info.receiver_info
		// -------------------------------------------------------------------
		Element receiverInfo = mteInfo.addElement(MTEStruct.f);
		receiverInfo.setValue(MTEStruct.f_host_id, header.getfHostId());
		receiverInfo.setValue(MTEStruct.f_directory, header.getfDirectory());
		receiverInfo.setValue(MTEStruct.f_file_name, header.getfFileName());
		receiverInfo.setValue(MTEStruct.f_upload_pgm, header.getfUploadPgm());
		// -------------------------------------------------------------------
		// set g:usr.mte_info.data_info
		// -------------------------------------------------------------------
		Element dataInfo = mteInfo.addElement(MTEStruct.g);
		dataInfo.setValue(MTEStruct.g_record_cnt, header.getgRecordCnt());
		dataInfo.setValue(MTEStruct.g_record_size, header.getgRecordSize());
		dataInfo.setValue(MTEStruct.g_data_size, header.getgDataSize());
		dataInfo.setValue(MTEStruct.g_data_compress, header.getgDataCompress());
		dataInfo.setValue(MTEStruct.g_compression_method, header.getgCompressionMethod());
		dataInfo.setValue(MTEStruct.g_compression_mode, header.getgCompressionMode());
		dataInfo.setValue(MTEStruct.g_compression_size, header.getgCompressionSize());
		dataInfo.setValue(MTEStruct.g_data_conversion, header.getgDataConversion());
		dataInfo.setValue(MTEStruct.g_converted_size, header.getgConvertedSize());
		dataInfo.setValue(MTEStruct.g_conv_mode, header.getgConvMode());

		// Set the MQRFH2 structure to the message
		rfh2.write(putMsg);

		// Write message data
		putMsg.writeString(data);

		// Set MQMD values
		putMsg.messageId = CMQC.MQMI_NONE;
		putMsg.correlationId = CMQC.MQCI_NONE;
		putMsg.messageType = CMQC.MQMT_DATAGRAM;
		// IMPORTANT: Set the format to MQRFH2 aka JMS Message.
		putMsg.format = CMQC.MQFMT_RF_HEADER_2;

		// put the message on the queue
		queue.put(putMsg, pmo);

	}
 
	 
	
	@Override
	public Object get(int waitTime) throws Exception {
		  
		MQMessage msg = new MQMessage();
		msg.characterSet = characterSet;
		gmo.waitInterval = waitTime;
		try {
			
			queue.get(msg, gmo);
			 
		} catch (com.ibm.mq.MQException mqe) {
			if (mqe.getReason() == 2033) throw new NoMoreMessageException("The queue (" + queue.getName().trim() + ") does not have message anymore.");
		} catch(Exception e) {
			throw e;
		} 
		return msg;
	}
 
	@Override
	public void startTransaction() throws Exception {
		// qmgr.begin();
	}

	@Override
	public void commit() throws Exception {
		qmgr.commit();
	}

	@Override
	public void rollback() throws Exception {
		qmgr.backout();
	}

	@Override
	public synchronized void close() {
		try {
			if (queue != null) {
				try {
					queue.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (qmgr != null) {
				try {
					qmgr.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}finally {
			//myConnMan.setActive(MQSimpleConnectionManager.MODE_INACTIVE);
		}
	}

	
	@Override
	public boolean ping() {
		PCFMessageAgent agent = null;
		PCFMessage   request = null;
	    PCFMessage[] responses = null;
	    boolean ok = false;
		try {
			if(qmgr != null) { 
				agent = new PCFMessageAgent(qmgr);
				request = new PCFMessage(CMQCFC.MQCMD_PING_Q_MGR);
				
				responses = agent.send(request);
				
				
				
				for (int i = 0; i < responses.length; i++) {
					if(responses[i].getCompCode() == CMQC.MQCC_OK) {
						ok = true;
						break;
					}
	//	            if(responses[i].getCompCode() == CMQC.MQCC_OK) {
	//	            	System.out.println("ok responses["+ i + "]:" + responses[i].toString());
	//	            }else {
	//	            	System.out.println("not ok responses["+ i + "]:" + responses[i].getCompCode());
	//	            }
				}
			}else {
				ok = false;
			}
			
			 
		} catch (Exception e) {
			logger.error("", e);
			ok = false;
		}finally {
			try {
				if(agent != null) agent.disconnect();
			} catch (MQDataException e) {
			}
		}
		
		
		
		return ok;
	}

}
