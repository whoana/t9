package rose.mary.trace.core.data.common;

import java.io.Serializable;

public class Unmatch implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String integrationId;
	String regDate;
	String modDate;
	String match = "1";
	
	public String getIntegrationId() {
		return integrationId;
	}
	public void setIntegrationId(String integrationId) {
		this.integrationId = integrationId;
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
	public String getMatch() {
		return match;
	}
	public void setMatch(String match) {
		this.match = match;
	}
	
	
	
}
