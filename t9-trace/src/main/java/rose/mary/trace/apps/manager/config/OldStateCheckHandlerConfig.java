package rose.mary.trace.apps.manager.config;

import java.io.Serializable;
import java.util.Map;

public class OldStateCheckHandlerConfig implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean usePreviousProcessInfo = false;
	
	String statusSuccess = "00";
	String statusIng 	 = "01";
	String statusFail 	 = "99";
	
	Map<String, Integer> nodeMap;

	public boolean isUsePreviousProcessInfo() {
		return usePreviousProcessInfo;
	}

	public void setUsePreviousProcessInfo(boolean usePreviousProcessInfo) {
		this.usePreviousProcessInfo = usePreviousProcessInfo;
	}

	public String getStatusSuccess() {
		return statusSuccess;
	}

	public void setStatusSuccess(String statusSuccess) {
		this.statusSuccess = statusSuccess;
	}

	public String getStatusIng() {
		return statusIng;
	}

	public void setStatusIng(String statusIng) {
		this.statusIng = statusIng;
	}

	public String getStatusFail() {
		return statusFail;
	}

	public void setStatusFail(String statusFail) {
		this.statusFail = statusFail;
	}

	public Map<String, Integer> getNodeMap() {
		return nodeMap;
	}

	public void setNodeMap(Map<String, Integer> nodeMap) {
		this.nodeMap = nodeMap;
	}
	
	
	
}
