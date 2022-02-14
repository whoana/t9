/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.simulator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pep.per.mint.common.util.Util;
import rose.mary.trace.helper.module.mte.MTEHeader; 

/**
 * <pre>
 * 송신 허브 수신의 3가지 인터페이스 트레킹 헤더메시지를 생성하는 기본 TraceMsgCreator를 작성한다.
 * SENDER --> BROKER  --> RECEIVER 
 * rose.mary.trace.test
 * DefaultTraceMsgCreator.java
 * </pre>
 * @author whoana
 * @date Jul 25, 2019
 */
public class DefaultTraceMsgCreator implements TraceMsgCreator {

 
	
	@Override
	public List<MTEHeader> create() {
		return create("iaminterface", "00");
	}

	@Override
	public List<MTEHeader> create(String interfaceId, String status) {
		
		String groupId = "ta";
		String hostId = "host1";
		String stime = getNonoTime();
		String date = stime.substring(0, 8);
		String time = stime.substring(8);
		String globalId = UUID.randomUUID().toString();
		
		List<MTEHeader> list = new ArrayList<MTEHeader>();
		MTEHeader s = new MTEHeader();
		s.setMcdMsd("xml");
		s.setMcdSet("");
		s.setMcdType("");
		s.setMcdFmt("");
		//--------------------------------------------------------------------
		// a = "usr.mte_info.interface_info"
		//--------------------------------------------------------------------
		s.setaHostId(hostId);
		s.setaGroupId(groupId);
		s.setaIntfId(interfaceId);
		s.setaDate(date);
		s.setaTime(time);
		s.setaGlobalId(globalId);
		//--------------------------------------------------------------------
		// b = "usr.mte_info.host_info"
		//--------------------------------------------------------------------
		s.setbHostId("host1");
		s.setbOsType("macos");
		s.setbOsVersion("10.14.5");
		//--------------------------------------------------------------------
		// c = "process_info"
		//--------------------------------------------------------------------
		String ptime = getNonoTime();
		s.setcDate(ptime.substring(0, 8));
		s.setcTime(ptime.substring(8));
		s.setcProcessMode("SNDR");
		s.setcProcessType("rosemary");
		s.setcProcessId("node1");
		s.setcHubCnt("0");
		s.setcSpokeCnt("1");
		s.setcRecvSpokeCnt("1");
		s.setcHopCnt("1");
		s.setcApplType("SENDER");
		//--------------------------------------------------------------------
		// d = "status_info"
		//--------------------------------------------------------------------
		s.setdStatus("00");		 
		s.setdErrorType("");
		s.setdErrorCode("");
		s.setdErrorReason("");
		s.setdErrorMessage("");
		s.setdErrorqMsgId("");
		s.setdTargetq("");
		//--------------------------------------------------------------------
		// e = "sender_info"
		//--------------------------------------------------------------------
		s.seteReplytoqmgr("");
		s.seteReplytoq("");
		s.seteFileName("");
		s.seteDirectory("");
		s.seteExtractPgm("");
		s.seteDescription("");
		//--------------------------------------------------------------------
		// f = "receiver_info"
		//--------------------------------------------------------------------
		s.setfHostId("");
		s.setfDirectory("");
		s.setfFileName("");
		s.setfUploadPgm("");
		//--------------------------------------------------------------------
		// g = "data_info"
		//--------------------------------------------------------------------
		s.setgRecordCnt("1000");
		s.setgRecordSize("1000");
		s.setgDataSize("1000000");
		s.setgDataCompress("N");
		s.setgCompressionMethod("");
		s.setgCompressionMode("");
		s.setgCompressionSize("");
		s.setgDataConversion("N");
		s.setgConvertedSize("");
		s.setgConvMode("");
		
		MTEHeader b = new MTEHeader();
		b.setMcdMsd("xml");
		b.setMcdSet("");
		b.setMcdType("");
		b.setMcdFmt("");
		//--------------------------------------------------------------------
		// a = "usr.mte_info.interface_info"
		//--------------------------------------------------------------------
		b.setaHostId(hostId);
		b.setaGroupId(groupId);
		b.setaIntfId(interfaceId);
		b.setaDate(date);
		b.setaTime(time);
		b.setaGlobalId(globalId);
		//--------------------------------------------------------------------
		// b = "usr.mte_info.host_info"
		//--------------------------------------------------------------------
		b.setbHostId("host2");
		b.setbOsType("macos");
		b.setbOsVersion("10.14.5");
		//--------------------------------------------------------------------
		// c = "process_info"
		//--------------------------------------------------------------------
		ptime = getNonoTime();
		b.setcDate(ptime.substring(0, 8));
		b.setcTime(ptime.substring(8));
		b.setcProcessMode("BRKR");
		b.setcProcessType("rosemary");
		b.setcProcessId("node2");
		b.setcHubCnt("1");
		b.setcSpokeCnt("0");
		b.setcRecvSpokeCnt("1");
		b.setcHopCnt("1");
		b.setcApplType("HUB");
		//--------------------------------------------------------------------
		// d = "status_info"
		//--------------------------------------------------------------------
		b.setdStatus("00");
		b.setdErrorType("");
		b.setdErrorCode("");
		b.setdErrorReason("");
		b.setdErrorMessage("");
		b.setdErrorqMsgId("");
		b.setdTargetq("");
		//--------------------------------------------------------------------
		// e = "sender_info"
		//--------------------------------------------------------------------
		b.seteReplytoqmgr("");
		b.seteReplytoq("");
		b.seteFileName("");
		b.seteDirectory("");
		b.seteExtractPgm("");
		b.seteDescription("");
		//--------------------------------------------------------------------
		// f = "receiver_info"
		//--------------------------------------------------------------------
		b.setfHostId("");
		b.setfDirectory("");
		b.setfFileName("");
		b.setfUploadPgm("");
		//--------------------------------------------------------------------
		// g = "data_info"
		//--------------------------------------------------------------------
		b.setgRecordCnt("1000");
		b.setgRecordSize("1000");
		b.setgDataSize("1000000");
		b.setgDataCompress("N");
		b.setgCompressionMethod("");
		b.setgCompressionMode("");
		b.setgCompressionSize("");
		b.setgDataConversion("N");
		b.setgConvertedSize("");
		b.setgConvMode("");
		
		
		MTEHeader r = new MTEHeader();
		r.setMcdMsd("xml");
		r.setMcdSet("");
		r.setMcdType("");
		r.setMcdFmt("");
		//--------------------------------------------------------------------
		// a = "usr.mte_info.interface_info"
		//--------------------------------------------------------------------
		r.setaHostId(hostId);
		r.setaGroupId(groupId);
		r.setaIntfId(interfaceId);
		r.setaDate(date);
		r.setaTime(time);
		r.setaGlobalId(globalId);
		//--------------------------------------------------------------------
		// b = "usr.mte_info.host_info"
		//--------------------------------------------------------------------
		r.setbHostId("host3");
		r.setbOsType("macos");
		r.setbOsVersion("10.14.5");
		//--------------------------------------------------------------------
		// c = "process_info"
		//--------------------------------------------------------------------
		ptime = getNonoTime();
		r.setcDate(ptime.substring(0, 8));
		r.setcTime(ptime.substring(8));
		r.setcProcessMode("RCVR");
		r.setcProcessType("rosemary");
		r.setcProcessId("node3");
		r.setcHubCnt("0");
		r.setcSpokeCnt("1");
		r.setcRecvSpokeCnt("1");
		r.setcHopCnt("1");
		r.setcApplType("RECEIVER");
		//--------------------------------------------------------------------
		// d = "status_info"
		//--------------------------------------------------------------------
		r.setdStatus(status);
	 
		
		if("00".equals(status)) {
			r.setdErrorType("");
			r.setdErrorCode("");
			r.setdErrorReason("");
			r.setdErrorMessage("");
			r.setdErrorqMsgId("");
		}else {
			r.setdErrorType("db");
			r.setdErrorCode("OR100");
			r.setdErrorReason("dberr");
			r.setdErrorMessage("I hungry");
			r.setdErrorqMsgId("123456");
		}
		r.setdTargetq("");
		
		
		//--------------------------------------------------------------------
		// e = "sender_info"
		//--------------------------------------------------------------------
		r.seteReplytoqmgr("");
		r.seteReplytoq("");
		r.seteFileName("");
		r.seteDirectory("");
		r.seteExtractPgm("");
		r.seteDescription("");
		//--------------------------------------------------------------------
		// f = "receiver_info"
		//--------------------------------------------------------------------
		r.setfHostId("");
		r.setfDirectory("");
		r.setfFileName("");
		r.setfUploadPgm("");
		//--------------------------------------------------------------------
		// g = "data_info"
		//--------------------------------------------------------------------
		r.setgRecordCnt("1000");
		r.setgRecordSize("1000");
		r.setgDataSize("1000000");
		r.setgDataCompress("N");
		r.setgCompressionMethod("");
		r.setgCompressionMode("");
		r.setgCompressionSize("");
		r.setgDataConversion("N");
		r.setgConvertedSize("");
		r.setgConvMode("");
		
		list.add(s);
		list.add(b);
		list.add(r);
		
		return list;
	}

	@Override
	public String createInterfaceId(String prefix, int index) {
		return Util.join(prefix, Util.leftPad("" + (index % 10), 2, "0"));
	}

	@Override
	public String createStatus(int index) {
		return index % 5 == 0 ? "99" : "00";
	}

	
	public static String getNonoTime() {
		String time = null;
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate date = LocalDate.now();
		  
		DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern("HHmmssSSSnnnnnnnnn");
		LocalTime colonTime = LocalTime.now();
		
		time = date.format(format) + timeColonFormatter.format(colonTime);
		
		
		return time.substring(0,20);
	} 
	public static void main(String args[] ) { 
		System.out.println(getNonoTime()); 
	}
	
}
