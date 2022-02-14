/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.data.common;

import java.io.Serializable;

/**
 * <pre>
 * rose.mary.trace.data.common
 * InterfaceInfo.java
 * </pre>
 * @author whoana
 * @date Sep 16, 2019
 */
public class InterfaceInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3901180963602866834L;
	
	String interfaceId; 	
	String businessId; 	
	String businessNm; 	
	String interfaceNm; 	
	String integrationId;
	String channelId; 	
	String channelNm; 	
	String dataPrDir; 	
	String dataPrDirNm; 
	
	String dataPrMethod; 	
	String dataPrMethodNm; 
	
	
	String appPrMethod; 	
	String appPrMethodNm;
	
	String sendSystemId; 
	String sendSystemNm; 
	String sendResource; 
	String sendResourceNm;
	
	String sendOrgId; 
	String sendOrgNm;
	String recvOrgId; 
	String recvOrgNm; 
	
	
	String recvSystemId; 
	String recvSystemNm; 
	String recvResource; 
	String recvResourceNm;
	 
	/**
	 * @return the interfaceId
	 */
	public String getInterfaceId() {
		return interfaceId;
	}
	/**
	 * @param interfaceId the interfaceId to set
	 */
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
	/**
	 * @return the businessId
	 */
	public String getBusinessId() {
		return businessId;
	}
	/**
	 * @param businessId the businessId to set
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	/**
	 * @return the businessNm
	 */
	public String getBusinessNm() {
		return businessNm;
	}
	/**
	 * @param businessNm the businessNm to set
	 */
	public void setBusinessNm(String businessNm) {
		this.businessNm = businessNm;
	}
	/**
	 * @return the interfaceNm
	 */
	public String getInterfaceNm() {
		return interfaceNm;
	}
	/**
	 * @param interfaceNm the interfaceNm to set
	 */
	public void setInterfaceNm(String interfaceNm) {
		this.interfaceNm = interfaceNm;
	}
	/**
	 * @return the integrationId
	 */
	public String getIntegrationId() {
		return integrationId;
	}
	/**
	 * @param integrationId the integrationId to set
	 */
	public void setIntegrationId(String integrationId) {
		this.integrationId = integrationId;
	}
	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	/**
	 * @return the channelNm
	 */
	public String getChannelNm() {
		return channelNm;
	}
	/**
	 * @param channelNm the channelNm to set
	 */
	public void setChannelNm(String channelNm) {
		this.channelNm = channelNm;
	}
	/**
	 * @return the dataPrDir
	 */
	public String getDataPrDir() {
		return dataPrDir;
	}
	/**
	 * @param dataPrDir the dataPrDir to set
	 */
	public void setDataPrDir(String dataPrDir) {
		this.dataPrDir = dataPrDir;
	}
	/**
	 * @return the dataPrDirNm
	 */
	public String getDataPrDirNm() {
		return dataPrDirNm;
	}
	/**
	 * @param dataPrDirNm the dataPrDirNm to set
	 */
	public void setDataPrDirNm(String dataPrDirNm) {
		this.dataPrDirNm = dataPrDirNm;
	}
	/**
	 * @return the appPrMethod
	 */
	public String getAppPrMethod() {
		return appPrMethod;
	}
	/**
	 * @param appPrMethod the appPrMethod to set
	 */
	public void setAppPrMethod(String appPrMethod) {
		this.appPrMethod = appPrMethod;
	}
	/**
	 * @return the appPrMethodNm
	 */
	public String getAppPrMethodNm() {
		return appPrMethodNm;
	}
	/**
	 * @param appPrMethodNm the appPrMethodNm to set
	 */
	public void setAppPrMethodNm(String appPrMethodNm) {
		this.appPrMethodNm = appPrMethodNm;
	}
	/**
	 * @return the sendSystemId
	 */
	public String getSendSystemId() {
		return sendSystemId;
	}
	/**
	 * @param sendSystemId the sendSystemId to set
	 */
	public void setSendSystemId(String sendSystemId) {
		this.sendSystemId = sendSystemId;
	}
	/**
	 * @return the sendSystemNm
	 */
	public String getSendSystemNm() {
		return sendSystemNm;
	}
	/**
	 * @param sendSystemNm the sendSystemNm to set
	 */
	public void setSendSystemNm(String sendSystemNm) {
		this.sendSystemNm = sendSystemNm;
	}
	/**
	 * @return the sendResource
	 */
	public String getSendResource() {
		return sendResource;
	}
	/**
	 * @param sendResource the sendResource to set
	 */
	public void setSendResource(String sendResource) {
		this.sendResource = sendResource;
	}
	/**
	 * @return the sendResourceNm
	 */
	public String getSendResourceNm() {
		return sendResourceNm;
	}
	/**
	 * @param sendResourceNm the sendResourceNm to set
	 */
	public void setSendResourceNm(String sendResourceNm) {
		this.sendResourceNm = sendResourceNm;
	}
	/**
	 * @return the recvSystemId
	 */
	public String getRecvSystemId() {
		return recvSystemId;
	}
	/**
	 * @param recvSystemId the recvSystemId to set
	 */
	public void setRecvSystemId(String recvSystemId) {
		this.recvSystemId = recvSystemId;
	}
	/**
	 * @return the recvSystemNm
	 */
	public String getRecvSystemNm() {
		return recvSystemNm;
	}
	/**
	 * @param recvSystemNm the recvSystemNm to set
	 */
	public void setRecvSystemNm(String recvSystemNm) {
		this.recvSystemNm = recvSystemNm;
	}
	/**
	 * @return the recvResource
	 */
	public String getRecvResource() {
		return recvResource;
	}
	/**
	 * @param recvResource the recvResource to set
	 */
	public void setRecvResource(String recvResource) {
		this.recvResource = recvResource;
	}
	/**
	 * @return the recvResourceNm
	 */
	public String getRecvResourceNm() {
		return recvResourceNm;
	}
	/**
	 * @param recvResourceNm the recvResourceNm to set
	 */
	public void setRecvResourceNm(String recvResourceNm) {
		this.recvResourceNm = recvResourceNm;
	}
	
	/**
	 * @return the dataPrMethod
	 */
	public String getDataPrMethod() {
		return dataPrMethod;
	}
	/**
	 * @param dataPrMethod the dataPrMethod to set
	 */
	public void setDataPrMethod(String dataPrMethod) {
		this.dataPrMethod = dataPrMethod;
	}
	/**
	 * @return the dataPrMethodNm
	 */
	public String getDataPrMethodNm() {
		return dataPrMethodNm;
	}
	/**
	 * @param dataPrMethodNm the dataPrMethodNm to set
	 */
	public void setDataPrMethodNm(String dataPrMethodNm) {
		this.dataPrMethodNm = dataPrMethodNm;
	}
	public String getSendOrgId() {
		return sendOrgId;
	}
	public void setSendOrgId(String sendOrgId) {
		this.sendOrgId = sendOrgId;
	}
	public String getSendOrgNm() {
		return sendOrgNm;
	}
	public void setSendOrgNm(String sendOrgNm) {
		this.sendOrgNm = sendOrgNm;
	}
	public String getRecvOrgId() {
		return recvOrgId;
	}
	public void setRecvOrgId(String recvOrgId) {
		this.recvOrgId = recvOrgId;
	}
	public String getRecvOrgNm() {
		return recvOrgNm;
	}
	public void setRecvOrgNm(String recvOrgNm) {
		this.recvOrgNm = recvOrgNm;
	}
	
	
	
}
