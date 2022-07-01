/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.helper.module.mte;


/**
 * <pre>
 * MOCOMSYS 제품 로더, 트레커에서 사용되는 기존 RFH2 헤더 스펙을 구현한 메시지 헤더 
 * rose.mary.trace.helper.module
 * MCMSHeader.java
 * </pre>
 * 
 * @author whoana
 * @date Jul 23, 2019
 */
public class MTEHeader {

	String mcdMsd;
	String mcdSet;
	String mcdType;
	String mcdFmt;

	// --------------------------------------------------------------------
	// a = "usr.mte_info.interface_info"
	// --------------------------------------------------------------------
	String aHostId;
	String aGroupId;
	String aIntfId;
	String aDate;
	String aTime;
	String aGlobalId;

	// --------------------------------------------------------------------
	// b = "usr.mte_info.host_info"
	// --------------------------------------------------------------------
	String bHostId;
	String bOsType;
	String bOsVersion;

	// --------------------------------------------------------------------
	// c = "process_info"
	// --------------------------------------------------------------------
	String cDate;
	String cTime;
	String cProcessMode;
	String cProcessType;
	String cProcessId;
	String cHubCnt;
	String cSpokeCnt;
	String cRecvSpokeCnt;
	String cHopCnt;
	String cApplType;
	String cTimezone = "UTC+09";
	String cElaspsedTime; //elaspsed_time
	// --------------------------------------------------------------------
	// d = "status_info"
	// --------------------------------------------------------------------
	String dStatus;
	String dErrorType;
	String dErrorCode;
	String dErrorReason;
	String dErrorMessage;
	String dErrorqMsgId;
	String dTargetq;

	// --------------------------------------------------------------------
	// e = "sender_info"
	// --------------------------------------------------------------------
	String eReplytoqmgr;
	String eReplytoq;
	String eFileName;
	String eDirectory;
	String eExtractPgm;
	String eDescription;

	// --------------------------------------------------------------------
	// f = "receiver_info"
	// --------------------------------------------------------------------
	String fHostId;
	String fDirectory;
	String fFileName;
	String fUploadPgm;

	// --------------------------------------------------------------------
	// g = "data_info"
	// --------------------------------------------------------------------
	String gRecordCnt;
	String gRecordSize;
	String gDataSize;
	String gDataCompress;
	String gCompressionMethod;
	String gCompressionMode;
	String gCompressionSize;
	String gDataConversion;
	String gConvertedSize;
	String gConvMode;

	// --------------------------------------------------------------------
	// h = "data_key_info"
	// --------------------------------------------------------------------

	// --------------------------------------------------------------------
	// i = "custom_info"
	// --------------------------------------------------------------------
	String iMaster01;
	String iMaster02;
	String iMaster03;
	String iMaster04;
	String iMaster05;
	String iDetail01;
	String iDetail02;
	String iDetail03;
	String iDetail04;
	String iDetail05;
	
	// --------------------------------------------------------------------
	// j = "prev_host_info"
	// --------------------------------------------------------------------
	String jHostId = "";
	String jProcessId = "";
	

	/**
	 * @return the mcdMsd
	 */
	public String getMcdMsd() {
		return mcdMsd;
	}

	/**
	 * @param mcdMsd the mcdMsd to set
	 */
	public void setMcdMsd(String mcdMsd) {
		this.mcdMsd = mcdMsd;
	}

	/**
	 * @return the mcdSet
	 */
	public String getMcdSet() {
		return mcdSet;
	}

	/**
	 * @param mcdSet the mcdSet to set
	 */
	public void setMcdSet(String mcdSet) {
		this.mcdSet = mcdSet;
	}

	/**
	 * @return the mcdType
	 */
	public String getMcdType() {
		return mcdType;
	}

	/**
	 * @param mcdType the mcdType to set
	 */
	public void setMcdType(String mcdType) {
		this.mcdType = mcdType;
	}

	/**
	 * @return the mcdFmt
	 */
	public String getMcdFmt() {
		return mcdFmt;
	}

	/**
	 * @param mcdFmt the mcdFmt to set
	 */
	public void setMcdFmt(String mcdFmt) {
		this.mcdFmt = mcdFmt;
	}

	/**
	 * @return the aHostId
	 */
	public String getaHostId() {
		return aHostId;
	}

	/**
	 * @param aHostId the aHostId to set
	 */
	public void setaHostId(String aHostId) {
		this.aHostId = aHostId;
	}

	/**
	 * @return the aGroupId
	 */
	public String getaGroupId() {
		return aGroupId;
	}

	/**
	 * @param aGroupId the aGroupId to set
	 */
	public void setaGroupId(String aGroupId) {
		this.aGroupId = aGroupId;
	}

	/**
	 * @return the aIntfId
	 */
	public String getaIntfId() {
		return aIntfId;
	}

	/**
	 * @param aIntfId the aIntfId to set
	 */
	public void setaIntfId(String aIntfId) {
		this.aIntfId = aIntfId;
	}

	/**
	 * @return the aDate
	 */
	public String getaDate() {
		return aDate;
	}

	/**
	 * @param aDate the aDate to set
	 */
	public void setaDate(String aDate) {
		this.aDate = aDate;
	}

	/**
	 * @return the aTime
	 */
	public String getaTime() {
		return aTime;
	}

	/**
	 * @param aTime the aTime to set
	 */
	public void setaTime(String aTime) {
		this.aTime = aTime;
	}

	/**
	 * @return the aGlobalId
	 */
	public String getaGlobalId() {
		return aGlobalId;
	}

	/**
	 * @param aGlobalId the aGlobalId to set
	 */
	public void setaGlobalId(String aGlobalId) {
		this.aGlobalId = aGlobalId;
	}

	/**
	 * @return the bHostId
	 */
	public String getbHostId() {
		return bHostId;
	}

	/**
	 * @param bHostId the bHostId to set
	 */
	public void setbHostId(String bHostId) {
		this.bHostId = bHostId;
	}

	/**
	 * @return the bOsType
	 */
	public String getbOsType() {
		return bOsType;
	}

	/**
	 * @param bOsType the bOsType to set
	 */
	public void setbOsType(String bOsType) {
		this.bOsType = bOsType;
	}

	/**
	 * @return the bOsVersion
	 */
	public String getbOsVersion() {
		return bOsVersion;
	}

	/**
	 * @param bOsVersion the bOsVersion to set
	 */
	public void setbOsVersion(String bOsVersion) {
		this.bOsVersion = bOsVersion;
	}

	/**
	 * @return the cDate
	 */
	public String getcDate() {
		return cDate;
	}

	/**
	 * @param cDate the cDate to set
	 */
	public void setcDate(String cDate) {
		this.cDate = cDate;
	}

	/**
	 * @return the cTime
	 */
	public String getcTime() {
		return cTime;
	}

	/**
	 * @param cTime the cTime to set
	 */
	public void setcTime(String cTime) {
		this.cTime = cTime;
	}

	/**
	 * @return the cProcessMode
	 */
	public String getcProcessMode() {
		return cProcessMode;
	}

	/**
	 * @param cProcessMode the cProcessMode to set
	 */
	public void setcProcessMode(String cProcessMode) {
		this.cProcessMode = cProcessMode;
	}

	/**
	 * @return the cProcessType
	 */
	public String getcProcessType() {
		return cProcessType;
	}

	/**
	 * @param cProcessType the cProcessType to set
	 */
	public void setcProcessType(String cProcessType) {
		this.cProcessType = cProcessType;
	}

	/**
	 * @return the cProcessId
	 */
	public String getcProcessId() {
		return cProcessId;
	}

	/**
	 * @param cProcessId the cProcessId to set
	 */
	public void setcProcessId(String cProcessId) {
		this.cProcessId = cProcessId;
	}

	/**
	 * @return the cHubCnt
	 */
	public String getcHubCnt() {
		return cHubCnt;
	}

	/**
	 * @param cHubCnt the cHubCnt to set
	 */
	public void setcHubCnt(String cHubCnt) {
		this.cHubCnt = cHubCnt;
	}

	/**
	 * @return the cSpokeCnt
	 */
	public String getcSpokeCnt() {
		return cSpokeCnt;
	}

	/**
	 * @param cSpokeCnt the cSpokeCnt to set
	 */
	public void setcSpokeCnt(String cSpokeCnt) {
		this.cSpokeCnt = cSpokeCnt;
	}

	/**
	 * @return the cRecvSpokeCnt
	 */
	public String getcRecvSpokeCnt() {
		return cRecvSpokeCnt;
	}

	/**
	 * @param cRecvSpokeCnt the cRecvSpokeCnt to set
	 */
	public void setcRecvSpokeCnt(String cRecvSpokeCnt) {
		this.cRecvSpokeCnt = cRecvSpokeCnt;
	}

	/**
	 * @return the cHopCnt
	 */
	public String getcHopCnt() {
		return cHopCnt;
	}

	/**
	 * @param cHopCnt the cHopCnt to set
	 */
	public void setcHopCnt(String cHopCnt) {
		this.cHopCnt = cHopCnt;
	}

	/**
	 * @return the cApplType
	 */
	public String getcApplType() {
		return cApplType;
	}

	/**
	 * @param cApplType the cApplType to set
	 */
	public void setcApplType(String cApplType) {
		this.cApplType = cApplType;
	}

	
	
	public String getcTimezone() {
		return cTimezone;
	}

	public void setcTimezone(String cTimezone) {
		this.cTimezone = cTimezone;
	}

	public String getcElaspsedTime() {
		return cElaspsedTime;
	}

	public void setcElaspsedTime(String cElaspsedTime) {
		this.cElaspsedTime = cElaspsedTime;
	}

	/**
	 * @return the dStatus
	 */
	public String getdStatus() {
		return dStatus;
	}

	/**
	 * @param dStatus the dStatus to set
	 */
	public void setdStatus(String dStatus) {
		this.dStatus = dStatus;
	}

	/**
	 * @return the dErrorType
	 */
	public String getdErrorType() {
		return dErrorType;
	}

	/**
	 * @param dErrorType the dErrorType to set
	 */
	public void setdErrorType(String dErrorType) {
		this.dErrorType = dErrorType;
	}

	/**
	 * @return the dErrorCode
	 */
	public String getdErrorCode() {
		return dErrorCode;
	}

	/**
	 * @param dErrorCode the dErrorCode to set
	 */
	public void setdErrorCode(String dErrorCode) {
		this.dErrorCode = dErrorCode;
	}

	/**
	 * @return the dErrorReason
	 */
	public String getdErrorReason() {
		return dErrorReason;
	}

	/**
	 * @param dErrorReason the dErrorReason to set
	 */
	public void setdErrorReason(String dErrorReason) {
		this.dErrorReason = dErrorReason;
	}

	/**
	 * @return the dErrorMessage
	 */
	public String getdErrorMessage() {
		return dErrorMessage;
	}

	/**
	 * @param dErrorMessage the dErrorMessage to set
	 */
	public void setdErrorMessage(String dErrorMessage) {
		this.dErrorMessage = dErrorMessage;
	}

	/**
	 * @return the dErrorqMsgId
	 */
	public String getdErrorqMsgId() {
		return dErrorqMsgId;
	}

	/**
	 * @param dErrorqMsgId the dErrorqMsgId to set
	 */
	public void setdErrorqMsgId(String dErrorqMsgId) {
		this.dErrorqMsgId = dErrorqMsgId;
	}

	/**
	 * @return the dTargetq
	 */
	public String getdTargetq() {
		return dTargetq;
	}

	/**
	 * @param dTargetq the dTargetq to set
	 */
	public void setdTargetq(String dTargetq) {
		this.dTargetq = dTargetq;
	}

	/**
	 * @return the eReplytoqmgr
	 */
	public String geteReplytoqmgr() {
		return eReplytoqmgr;
	}

	/**
	 * @param eReplytoqmgr the eReplytoqmgr to set
	 */
	public void seteReplytoqmgr(String eReplytoqmgr) {
		this.eReplytoqmgr = eReplytoqmgr;
	}

	/**
	 * @return the eReplytoq
	 */
	public String geteReplytoq() {
		return eReplytoq;
	}

	/**
	 * @param eReplytoq the eReplytoq to set
	 */
	public void seteReplytoq(String eReplytoq) {
		this.eReplytoq = eReplytoq;
	}

	/**
	 * @return the eFileName
	 */
	public String geteFileName() {
		return eFileName;
	}

	/**
	 * @param eFileName the eFileName to set
	 */
	public void seteFileName(String eFileName) {
		this.eFileName = eFileName;
	}

	/**
	 * @return the eDirectory
	 */
	public String geteDirectory() {
		return eDirectory;
	}

	/**
	 * @param eDirectory the eDirectory to set
	 */
	public void seteDirectory(String eDirectory) {
		this.eDirectory = eDirectory;
	}

	/**
	 * @return the eExtractPgm
	 */
	public String geteExtractPgm() {
		return eExtractPgm;
	}

	/**
	 * @param eExtractPgm the eExtractPgm to set
	 */
	public void seteExtractPgm(String eExtractPgm) {
		this.eExtractPgm = eExtractPgm;
	}

	/**
	 * @return the eDescription
	 */
	public String geteDescription() {
		return eDescription;
	}

	/**
	 * @param eDescription the eDescription to set
	 */
	public void seteDescription(String eDescription) {
		this.eDescription = eDescription;
	}

	/**
	 * @return the fHostId
	 */
	public String getfHostId() {
		return fHostId;
	}

	/**
	 * @param fHostId the fHostId to set
	 */
	public void setfHostId(String fHostId) {
		this.fHostId = fHostId;
	}

	/**
	 * @return the fDirectory
	 */
	public String getfDirectory() {
		return fDirectory;
	}

	/**
	 * @param fDirectory the fDirectory to set
	 */
	public void setfDirectory(String fDirectory) {
		this.fDirectory = fDirectory;
	}

	/**
	 * @return the fFileName
	 */
	public String getfFileName() {
		return fFileName;
	}

	/**
	 * @param fFileName the fFileName to set
	 */
	public void setfFileName(String fFileName) {
		this.fFileName = fFileName;
	}

	/**
	 * @return the fUploadPgm
	 */
	public String getfUploadPgm() {
		return fUploadPgm;
	}

	/**
	 * @param fUploadPgm the fUploadPgm to set
	 */
	public void setfUploadPgm(String fUploadPgm) {
		this.fUploadPgm = fUploadPgm;
	}

	/**
	 * @return the gRecordCnt
	 */
	public String getgRecordCnt() {
		return gRecordCnt;
	}

	/**
	 * @param gRecordCnt the gRecordCnt to set
	 */
	public void setgRecordCnt(String gRecordCnt) {
		this.gRecordCnt = gRecordCnt;
	}

	/**
	 * @return the gRecordSize
	 */
	public String getgRecordSize() {
		return gRecordSize;
	}

	/**
	 * @param gRecordSize the gRecordSize to set
	 */
	public void setgRecordSize(String gRecordSize) {
		this.gRecordSize = gRecordSize;
	}

	/**
	 * @return the gDataSize
	 */
	public String getgDataSize() {
		return gDataSize;
	}

	/**
	 * @param gDataSize the gDataSize to set
	 */
	public void setgDataSize(String gDataSize) {
		this.gDataSize = gDataSize;
	}

	/**
	 * @return the gDataCompress
	 */
	public String getgDataCompress() {
		return gDataCompress;
	}

	/**
	 * @param gDataCompress the gDataCompress to set
	 */
	public void setgDataCompress(String gDataCompress) {
		this.gDataCompress = gDataCompress;
	}

	/**
	 * @return the gCompressionMethod
	 */
	public String getgCompressionMethod() {
		return gCompressionMethod;
	}

	/**
	 * @param gCompressionMethod the gCompressionMethod to set
	 */
	public void setgCompressionMethod(String gCompressionMethod) {
		this.gCompressionMethod = gCompressionMethod;
	}

	/**
	 * @return the gCompressionMode
	 */
	public String getgCompressionMode() {
		return gCompressionMode;
	}

	/**
	 * @param gCompressionMode the gCompressionMode to set
	 */
	public void setgCompressionMode(String gCompressionMode) {
		this.gCompressionMode = gCompressionMode;
	}

	/**
	 * @return the gCompressionSize
	 */
	public String getgCompressionSize() {
		return gCompressionSize;
	}

	/**
	 * @param gCompressionSize the gCompressionSize to set
	 */
	public void setgCompressionSize(String gCompressionSize) {
		this.gCompressionSize = gCompressionSize;
	}

	/**
	 * @return the gDataConversion
	 */
	public String getgDataConversion() {
		return gDataConversion;
	}

	/**
	 * @param gDataConversion the gDataConversion to set
	 */
	public void setgDataConversion(String gDataConversion) {
		this.gDataConversion = gDataConversion;
	}

	/**
	 * @return the gConvertedSize
	 */
	public String getgConvertedSize() {
		return gConvertedSize;
	}

	/**
	 * @param gConvertedSize the gConvertedSize to set
	 */
	public void setgConvertedSize(String gConvertedSize) {
		this.gConvertedSize = gConvertedSize;
	}

	/**
	 * @return the gConvMode
	 */
	public String getgConvMode() {
		return gConvMode;
	}

	/**
	 * @param gConvMode the gConvMode to set
	 */
	public void setgConvMode(String gConvMode) {
		this.gConvMode = gConvMode;
	}

	/**
	 * @return the iMaster01
	 */
	public String getiMaster01() {
		return iMaster01;
	}

	/**
	 * @param iMaster01 the iMaster01 to set
	 */
	public void setiMaster01(String iMaster01) {
		this.iMaster01 = iMaster01;
	}

	/**
	 * @return the iMaster02
	 */
	public String getiMaster02() {
		return iMaster02;
	}

	/**
	 * @param iMaster02 the iMaster02 to set
	 */
	public void setiMaster02(String iMaster02) {
		this.iMaster02 = iMaster02;
	}

	/**
	 * @return the iMaster03
	 */
	public String getiMaster03() {
		return iMaster03;
	}

	/**
	 * @param iMaster03 the iMaster03 to set
	 */
	public void setiMaster03(String iMaster03) {
		this.iMaster03 = iMaster03;
	}

	/**
	 * @return the iMaster04
	 */
	public String getiMaster04() {
		return iMaster04;
	}

	/**
	 * @param iMaster04 the iMaster04 to set
	 */
	public void setiMaster04(String iMaster04) {
		this.iMaster04 = iMaster04;
	}

	/**
	 * @return the iMaster05
	 */
	public String getiMaster05() {
		return iMaster05;
	}

	/**
	 * @param iMaster05 the iMaster05 to set
	 */
	public void setiMaster05(String iMaster05) {
		this.iMaster05 = iMaster05;
	}

	/**
	 * @return the iDetail01
	 */
	public String getiDetail01() {
		return iDetail01;
	}

	/**
	 * @param iDetail01 the iDetail01 to set
	 */
	public void setiDetail01(String iDetail01) {
		this.iDetail01 = iDetail01;
	}

	/**
	 * @return the iDetail02
	 */
	public String getiDetail02() {
		return iDetail02;
	}

	/**
	 * @param iDetail02 the iDetail02 to set
	 */
	public void setiDetail02(String iDetail02) {
		this.iDetail02 = iDetail02;
	}

	/**
	 * @return the iDetail03
	 */
	public String getiDetail03() {
		return iDetail03;
	}

	/**
	 * @param iDetail03 the iDetail03 to set
	 */
	public void setiDetail03(String iDetail03) {
		this.iDetail03 = iDetail03;
	}

	/**
	 * @return the iDetail04
	 */
	public String getiDetail04() {
		return iDetail04;
	}

	/**
	 * @param iDetail04 the iDetail04 to set
	 */
	public void setiDetail04(String iDetail04) {
		this.iDetail04 = iDetail04;
	}

	/**
	 * @return the iDetail05
	 */
	public String getiDetail05() {
		return iDetail05;
	}

	/**
	 * @param iDetail05 the iDetail05 to set
	 */
	public void setiDetail05(String iDetail05) {
		this.iDetail05 = iDetail05;
	}
	
	

	public String getjHostId() {
		return jHostId;
	}

	public void setjHostId(String jHostId) {
		this.jHostId = jHostId;
	}

	public String getjProcessId() {
		return jProcessId;
	}

	public void setjProcessId(String jProcessId) {
		this.jProcessId = jProcessId;
	}

	public String getMcdData() {
		StringBuffer mcd = new StringBuffer();
		mcd.append("<mcd>");
		mcd.append("<Msd>").append(mcdMsd).append("</Msd>");
		mcd.append("<Set>").append(mcdSet).append("</Set>");
		mcd.append("<Type>").append(mcdType).append("</Type>");
		mcd.append("<Fmt>").append(mcdFmt).append("</Fmt>");
		mcd.append("</mcd>");
		return mcd.toString();
	}
 

	public String getUsrData() {
		StringBuffer usr = new StringBuffer();
		usr.append("<usr><mte_info>");

		usr.append("<interface_info>");
		usr.append("<group_id>").append(aGroupId).append("</group_id>");
		usr.append("<host_id>").append(aHostId).append("</host_id>");
		usr.append("<intf_id>").append(aIntfId).append("</intf_id>");
		usr.append("<date>").append(aDate).append("</date>");
		usr.append("<time>").append(aTime).append("</time>");
		usr.append("<global_id>").append(aGlobalId).append("</global_id>");
		usr.append("</interface_info>");

		usr.append("<prev_host_info>");
		usr.append("<host_id>").append(jHostId).append("</host_id>");
		usr.append("<process_id>").append(jProcessId).append("</process_id>");
		usr.append("</prev_host_info>");
		
		usr.append("<host_info>");
		usr.append("<host_id>").append(bHostId).append("</host_id>");
		usr.append("<os_type>").append(bOsType).append("</os_type>");
		usr.append("<os_version>").append(bOsVersion).append("</os_version>");
		usr.append("</host_info>");

		usr.append("<process_info>");
		usr.append("<date>").append(cDate).append("</date>");
		usr.append("<time>").append(cTime).append("</time>");
		usr.append("<process_mode>").append(cProcessMode).append("</process_mode>");
		usr.append("<process_type>").append(cProcessType).append("</process_type>");
		usr.append("<process_id>").append(cProcessId).append("</process_id>");
		usr.append("<hub_cnt>").append(cHubCnt).append("</hub_cnt>");
		usr.append("<spoke_cnt>").append(cSpokeCnt).append("</spoke_cnt>");
		usr.append("<recv_spoke_cnt>").append(cRecvSpokeCnt).append("</recv_spoke_cnt>");
		usr.append("<hop_cnt>").append(cHopCnt).append("</hop_cnt>");
		usr.append("<appl_type>").append(cApplType).append("</appl_type>");
		usr.append("<timezone>").append(cTimezone).append("</timezone>");
		usr.append("<elaspsed_time>").append(cElaspsedTime).append("</elaspsed_time>");
		usr.append("</process_info>");
		 


		usr.append("<status_info>");
		usr.append("<status>").append(dStatus).append("</status>");
		usr.append("<error_type>").append(dErrorType).append("</error_type>");
		usr.append("<error_code>").append(dErrorCode).append("</error_code>");
		usr.append("<error_reason>").append(dErrorReason).append("</error_reason>");
		usr.append("<error_message>").append(dErrorMessage).append("</error_message>");
		usr.append("<errorq_msgid>").append(dErrorqMsgId).append("</errorq_msgid>");
		usr.append("<targetq>").append(dTargetq).append("</targetq>");
		usr.append("</status_info>");

		usr.append("<sender_info>");
		usr.append("<replytoqmgr>").append(eReplytoqmgr).append("</replytoqmgr>");
		usr.append("<replytoq>").append(eReplytoq).append("</replytoq>");
		usr.append("<file_name>").append(eFileName).append("</file_name>");
		usr.append("<directory>").append(eDirectory).append("</directory>");
		usr.append("<extract_pgm>").append(eExtractPgm).append("</extract_pgm>");
		usr.append("<description>").append(eDescription).append("</description>");
		usr.append("</sender_info>");

		usr.append("<receiver_info>");
		usr.append("<host_id>").append(fHostId).append("</host_id>");
		usr.append("<directory>").append(fDirectory).append("</directory>");
		usr.append("<file_name>").append(fFileName).append("</file_name>");
		usr.append("<upload_pgm>").append(fUploadPgm).append("</upload_pgm>");
		usr.append("</receiver_info>");

		usr.append("<data_info>");
		usr.append("<record_cnt>").append(gRecordCnt).append("</record_cnt>");
		usr.append("<record_size>").append(gRecordSize).append("</record_size>");
		usr.append("<data_size>").append(gDataSize).append("</data_size>");
		usr.append("<data_compress>").append(gDataCompress).append("</data_compress>");
		usr.append("<compression_method>").append(gCompressionMethod).append("</compression_method>");
		usr.append("<compression_mode>").append(gCompressionMode).append("</compression_mode>");
		usr.append("<compression_size>").append(gCompressionSize).append("</compression_size>");
		usr.append("<data_conversion>").append(gDataConversion).append("</data_conversion>");
		usr.append("<converted_size>").append(gConvertedSize).append("</converted_size>");
		usr.append("<conv_mode>").append(gConvMode).append("</conv_mode>");
		usr.append("</data_info>");

		usr.append("</mte_info></usr>");
		return usr.toString();
	}

}