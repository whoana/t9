package rose.mary.trace.core.data.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="objectType")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TLog {

    @JsonProperty String integrationId = "";     
    @JsonProperty String trackingDate = "";      
    @JsonProperty String orgHostId = "";       
    @JsonProperty String interfaceId = "";     
    @JsonProperty String status = "";          
    @JsonProperty String match = "";           
    @JsonProperty int recordCnt = 0;       
    @JsonProperty int dataAmt = 0;         
    @JsonProperty String cmp = "";             
    @JsonProperty int cst = 0;             
    @JsonProperty int tdc = 0;             
    @JsonProperty int fnc = 0;             
    @JsonProperty int erc = 0;             
    @JsonProperty String errorCd = "";           
    @JsonProperty String errorMsg = "";              
    @JsonProperty String businessId = "";            
    @JsonProperty String businessNm = "";            
    @JsonProperty String interfaceNm = "";           
    @JsonProperty String channelId = "";             
    @JsonProperty String channelNm = "";                 
    @JsonProperty String dataPrDir = "";             
    @JsonProperty String dataPrDirNm = "";               
    @JsonProperty String dataPrMethod = "";              
    @JsonProperty String dataPrMethodNm = "";                
    @JsonProperty String appPrMethod = "";               
    @JsonProperty String appPrMethodNm = "";             
    @JsonProperty String sndOrgId = "";              
    @JsonProperty String sndOrgNm = "";              
    @JsonProperty String sndSystemId = "";               
    @JsonProperty String sndSystemNm = "";               
    @JsonProperty String sndResType = "";                
    @JsonProperty String sndResNm = "";                  
    @JsonProperty String rcvOrgId = "";              
    @JsonProperty String rcvOrgNm = "";                  
    @JsonProperty String rcvSystemId = "";                   
    @JsonProperty String rcvSystemNm = "";               
    @JsonProperty String rcvResType = "";                
    @JsonProperty String rcvResNm = "";                  
    @JsonProperty String regDate = "";               
    @JsonProperty String modDate = "";               
    @JsonProperty String businessCd = "";                    
    @JsonProperty String channelCd = "";                 
    @JsonProperty String sndOrgCd = "";                      
    @JsonProperty String rcvOrgCd = "";                  
    @JsonProperty String sndSystemCd = "";               
    @JsonProperty String rcvSystemCd = "";
    public String getIntegrationId() {
        return integrationId;
    }
    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }
    public String getTrackingDate() {
        return trackingDate;
    }
    public void setTrackingDate(String trackingDate) {
        this.trackingDate = trackingDate;
    }
    public String getOrgHostId() {
        return orgHostId;
    }
    public void setOrgHostId(String orgHostId) {
        this.orgHostId = orgHostId;
    }
    public String getInterfaceId() {
        return interfaceId;
    }
    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMatch() {
        return match;
    }
    public void setMatch(String match) {
        this.match = match;
    }
    public int getRecordCnt() {
        return recordCnt;
    }
    public void setRecordCnt(int recordCnt) {
        this.recordCnt = recordCnt;
    }
    public int getDataAmt() {
        return dataAmt;
    }
    public void setDataAmt(int dataAmt) {
        this.dataAmt = dataAmt;
    }
    public String getCmp() {
        return cmp;
    }
    public void setCmp(String cmp) {
        this.cmp = cmp;
    }
    public int getCst() {
        return cst;
    }
    public void setCst(int cst) {
        this.cst = cst;
    }
    public int getTdc() {
        return tdc;
    }
    public void setTdc(int tdc) {
        this.tdc = tdc;
    }
    public int getFnc() {
        return fnc;
    }
    public void setFnc(int fnc) {
        this.fnc = fnc;
    }
    public int getErc() {
        return erc;
    }
    public void setErc(int erc) {
        this.erc = erc;
    }
    public String getErrorCd() {
        return errorCd;
    }
    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public String getBusinessId() {
        return businessId;
    }
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    public String getBusinessNm() {
        return businessNm;
    }
    public void setBusinessNm(String businessNm) {
        this.businessNm = businessNm;
    }
    public String getInterfaceNm() {
        return interfaceNm;
    }
    public void setInterfaceNm(String interfaceNm) {
        this.interfaceNm = interfaceNm;
    }
    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    public String getChannelNm() {
        return channelNm;
    }
    public void setChannelNm(String channelNm) {
        this.channelNm = channelNm;
    }
    public String getDataPrDir() {
        return dataPrDir;
    }
    public void setDataPrDir(String dataPrDir) {
        this.dataPrDir = dataPrDir;
    }
    public String getDataPrDirNm() {
        return dataPrDirNm;
    }
    public void setDataPrDirNm(String dataPrDirNm) {
        this.dataPrDirNm = dataPrDirNm;
    }
    public String getDataPrMethod() {
        return dataPrMethod;
    }
    public void setDataPrMethod(String dataPrMethod) {
        this.dataPrMethod = dataPrMethod;
    }
    public String getDataPrMethodNm() {
        return dataPrMethodNm;
    }
    public void setDataPrMethodNm(String dataPrMethodNm) {
        this.dataPrMethodNm = dataPrMethodNm;
    }
    public String getAppPrMethod() {
        return appPrMethod;
    }
    public void setAppPrMethod(String appPrMethod) {
        this.appPrMethod = appPrMethod;
    }
    public String getAppPrMethodNm() {
        return appPrMethodNm;
    }
    public void setAppPrMethodNm(String appPrMethodNm) {
        this.appPrMethodNm = appPrMethodNm;
    }
    public String getSndOrgId() {
        return sndOrgId;
    }
    public void setSndOrgId(String sndOrgId) {
        this.sndOrgId = sndOrgId;
    }
    public String getSndOrgNm() {
        return sndOrgNm;
    }
    public void setSndOrgNm(String sndOrgNm) {
        this.sndOrgNm = sndOrgNm;
    }
    public String getSndSystemId() {
        return sndSystemId;
    }
    public void setSndSystemId(String sndSystemId) {
        this.sndSystemId = sndSystemId;
    }
    public String getSndSystemNm() {
        return sndSystemNm;
    }
    public void setSndSystemNm(String sndSystemNm) {
        this.sndSystemNm = sndSystemNm;
    }
    public String getSndResType() {
        return sndResType;
    }
    public void setSndResType(String sndResType) {
        this.sndResType = sndResType;
    }
    public String getSndResNm() {
        return sndResNm;
    }
    public void setSndResNm(String sndResNm) {
        this.sndResNm = sndResNm;
    }
    public String getRcvOrgId() {
        return rcvOrgId;
    }
    public void setRcvOrgId(String rcvOrgId) {
        this.rcvOrgId = rcvOrgId;
    }
    public String getRcvOrgNm() {
        return rcvOrgNm;
    }
    public void setRcvOrgNm(String rcvOrgNm) {
        this.rcvOrgNm = rcvOrgNm;
    }
    public String getRcvSystemId() {
        return rcvSystemId;
    }
    public void setRcvSystemId(String rcvSystemId) {
        this.rcvSystemId = rcvSystemId;
    }
    public String getRcvSystemNm() {
        return rcvSystemNm;
    }
    public void setRcvSystemNm(String rcvSystemNm) {
        this.rcvSystemNm = rcvSystemNm;
    }
    public String getRcvResType() {
        return rcvResType;
    }
    public void setRcvResType(String rcvResType) {
        this.rcvResType = rcvResType;
    }
    public String getRcvResNm() {
        return rcvResNm;
    }
    public void setRcvResNm(String rcvResNm) {
        this.rcvResNm = rcvResNm;
    }
    public String getRegDate() {
        return regDate;
    }
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
    public String getModDate() {
        return modDate;
    }
    public void setModDate(String modDate) {
        this.modDate = modDate;
    }
    public String getBusinessCd() {
        return businessCd;
    }
    public void setBusinessCd(String businessCd) {
        this.businessCd = businessCd;
    }
    public String getChannelCd() {
        return channelCd;
    }
    public void setChannelCd(String channelCd) {
        this.channelCd = channelCd;
    }
    public String getSndOrgCd() {
        return sndOrgCd;
    }
    public void setSndOrgCd(String sndOrgCd) {
        this.sndOrgCd = sndOrgCd;
    }
    public String getRcvOrgCd() {
        return rcvOrgCd;
    }
    public void setRcvOrgCd(String rcvOrgCd) {
        this.rcvOrgCd = rcvOrgCd;
    }
    public String getSndSystemCd() {
        return sndSystemCd;
    }
    public void setSndSystemCd(String sndSystemCd) {
        this.sndSystemCd = sndSystemCd;
    }
    public String getRcvSystemCd() {
        return rcvSystemCd;
    }
    public void setRcvSystemCd(String rcvSystemCd) {
        this.rcvSystemCd = rcvSystemCd;
    }           

    
    
}
