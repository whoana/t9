/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.parser;

import java.io.IOException;
import java.util.Map;

import com.ibm.mq.MQMessage;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.headers.MQRFH2;
import com.ibm.mq.headers.MQRFH2.Element;

import pep.per.mint.common.util.Util;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.exception.HaveNoTraceInfoException;
import rose.mary.trace.exception.NoMoreMessageException;
import rose.mary.trace.exception.ParsingException;
import rose.mary.trace.helper.module.mte.MTEStruct;

/**
 * <pre>
 * MQMessageParser
 * RFH2 헤더를 가진 MQ 메시지를 파싱한다. 
 * </pre>
 * @author whoana
 * @since Aug 13, 2019
 */
public class MQMessageParser extends Parser {

	Map<String, Integer> nodeMap;
	public MQMessageParser() {}
	public MQMessageParser(Map<String, Integer> nodeMap) {
		this.nodeMap = nodeMap;
	}
	
	@Override
	public Trace parse(Object traceObject) throws Exception {
		MQMessage msg = (MQMessage)traceObject; 
		
		if(msg.getTotalMessageLength() == 0) throw new NoMoreMessageException();
		
		if (CMQC.MQFMT_RF_HEADER_2.equals(msg.format)) {
			msg.seek(0);
			MQRFH2 rfh2 = new MQRFH2(msg);

//			int strucLen = rfh2.getStrucLength();
//			int encoding = rfh2.getEncoding();
//			int CCSID = rfh2.getCodedCharSetId();
//			String format = rfh2.getFormat();
//			int flags = rfh2.getFlags();
//			int nameValueCCSID = rfh2.getNameValueCCSID();
//
//			if (CMQC.MQFMT_STRING.equals(format)) {
//				String msgStr = msg.readStringOfByteLength(msg.getDataLength());
//			} else {
//			}

			//String id = null;
			String interfaceId = "";
			String originHostId = "";
			String hostId = "";
			String date = "";
			String processId = "";
			String processDate = "";
			String processEndDate = "";
			String type = "";
			String ip = "";
			String os = "";
			String status = "";
			String errorCode = "";
			String errorMessage = "";
			int recordCount = 0;
			int dataSize = 0;
			String compress = "0";
			String timezone = "UTC+9";
			String elapsedTime = "";
			String previousHostId = "";
			String previousProcessId = "";
			
			byte[] data = new byte[msg.getDataLength()];
			int todoNodeCount = 1;

			Element usr = parseElement(rfh2, MTEStruct.usr, true);

			Element mteInfo = parseElement(usr, MTEStruct.mte_info, true);
			// -------------------------------------------------------------------
			// set a:usr.mte_info.interface_info
			// -------------------------------------------------------------------
			Element interfaceInfo = parseElement(mteInfo, MTEStruct.a, true);
			originHostId = parseStringValue(interfaceInfo, MTEStruct.a_host_id, true);
			interfaceId = parseStringValue(interfaceInfo, MTEStruct.a_intf_id, true);
			date = parseStringValue(interfaceInfo, MTEStruct.a_date, true);
			date = date + parseStringValue(interfaceInfo, MTEStruct.a_time, true);
			// -------------------------------------------------------------------
			// set j:usr.mte_info.prev_host_info
			// -------------------------------------------------------------------
			Element prevHostInfo = parseElement(mteInfo, MTEStruct.j, false);
			previousHostId    = parseStringValue(prevHostInfo, MTEStruct.j_host_id, false);
			previousProcessId = parseStringValue(prevHostInfo, MTEStruct.j_process_id, false);
			// -------------------------------------------------------------------
			// set b:usr.mte_info.host_info
			// -------------------------------------------------------------------
			Element hostInfo = parseElement(mteInfo, MTEStruct.b, true);
			hostId = parseStringValue(hostInfo, MTEStruct.b_host_id, true);
			os     = parseStringValue(hostInfo, MTEStruct.b_os_type, false) + parseStringValue(hostInfo, MTEStruct.b_os_version, false);
			// -------------------------------------------------------------------
			// set c:usr.mte_info.process_info
			// -------------------------------------------------------------------
			Element processInfo = parseElement(mteInfo, MTEStruct.c, true);
			processId 	  = parseStringValue(processInfo, MTEStruct.c_process_id, true);
			processDate   = parseStringValue(processInfo, MTEStruct.c_date, true) + parseStringValue(processInfo, MTEStruct.c_time, true);
			processEndDate= processDate;
			type 		  = parseStringValue(processInfo, MTEStruct.c_process_mode, true);
			todoNodeCount = parseIntValue(processInfo, MTEStruct.c_recv_spoke_cnt, true);
			timezone      = parseStringValue(processInfo, MTEStruct.c_timezone, false);
			elapsedTime   = parseStringValue(processInfo, MTEStruct.c_elaspsed_time, false);
			
			// -------------------------------------------------------------------
			// set d:usr.mte_info.status_info
			// -------------------------------------------------------------------
			Element statusInfo = parseElement(mteInfo, MTEStruct.d, true);
			status       = parseStringValue(statusInfo, MTEStruct.d_status, true);
			errorCode    = parseStringValue(statusInfo, MTEStruct.d_error_code, true);
			errorMessage = parseStringValue(statusInfo, MTEStruct.d_error_message, true);
			// -------------------------------------------------------------------
			// set g:usr.mte_info.data_info
			// -------------------------------------------------------------------
			Element dataInfo = parseElement(mteInfo, MTEStruct.g, true);
			dataSize    = parseIntValue(dataInfo, MTEStruct.g_data_size, true);
			recordCount = parseIntValue(dataInfo, MTEStruct.g_record_cnt, true);
			compress    = parseStringValue(dataInfo, MTEStruct.g_data_compress, true);

			msg.readFully(data);

			Trace trace = new Trace();
			trace.setIntegrationId(interfaceId);
			trace.setHostId(hostId);
			trace.setDate(date);
			trace.setPreviousHostId(previousHostId);
			trace.setPreviousProcessId(previousProcessId);
			trace.setProcessId(processId);
			trace.setProcessDate(processDate);
			trace.setProcessEndDate(processEndDate);
			trace.setType(type);
			trace.setIp(ip);
			trace.setOriginHostId(originHostId);
			trace.setOs(os);
			trace.setStatus(status);
			trace.setErrorCode(errorCode);
			trace.setErrorMessage(errorMessage);
			trace.setRecordCount(recordCount);
			trace.setDataSize(dataSize);
			trace.setCompress(compress);
			trace.setData(data);
			trace.setTodoNodeCount(todoNodeCount);
			trace.setTimezone(timezone);
			trace.setElapsedTime(elapsedTime);
			trace.setId(Util.join(trace.getIntegrationId(),idSeperator, trace.getDate(), idSeperator, hostId, idSeperator, processId ));
			
			if(nodeMap != null) {
				 trace.setSeq(nodeMap.getOrDefault(trace.getType(), 0));
			}
			
			return trace;
		} else {
			HaveNoTraceInfoException e =  new HaveNoTraceInfoException();
			try {
				byte[] data = new byte[msg.getDataLength()];
				msg.readFully(data);
				e.setData(data);
			}catch(Exception se) {
		
			}
			throw e;
		}
		 
	}
	
	private int parseIntValue(Element parent, String name, boolean required) throws Exception {
		Element el = parseElement(parent, name, required);
		if(el != null) {
			String val = el.getStringValue();
			try {
				return Integer.parseInt(val != null && val.length() > 0 ? val.trim() : "0");
			}catch(NumberFormatException e) {
				return 0;
			}
		} else {
			return 0;			
		}
	}

	private String parseStringValue(Element parent, String name, boolean required) throws ParsingException {
		Element el = parseElement(parent, name, required);
		if(el != null) {
			String val = el.getStringValue();
			return val != null && val.length() > 0 ? val : "";
		} else {
			return "";			
		}
	}

	private Element parseElement(MQRFH2 rfh2, String name, boolean required) throws ParsingException, IOException {
		Element el = rfh2.getFolder(name, false);
		if (el == null && required)
			throw new ParsingException(Util.join("not found ", name, " info in a mq message"));
		return el;
	}

	private Element parseElement(Element parent, String name, boolean required) throws ParsingException {
		Element el = parent.getElement(name, false);
		if (el == null && required)
			throw new ParsingException(Util.join("not found ", name, " info in a mq message"));
		return el;
	}

}
