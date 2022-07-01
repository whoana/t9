package rose.mary.trace.core.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TesterManagerConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	String name;
	
	int exceptionDelay = 5000;
	
	int repeatDelaySec = 300;
	
	List<Map> paramsList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getExceptionDelay() {
		return exceptionDelay;
	}

	public void setExceptionDelay(int exceptionDelay) {
		this.exceptionDelay = exceptionDelay;
	}

	public int getRepeatDelaySec() {
		return repeatDelaySec;
	}

	public void setRepeatDelaySec(int repeatDelaySec) {
		this.repeatDelaySec = repeatDelaySec;
	}

	public List<Map> getParamsList() {
		return paramsList;
	}

	public void setParamsList(List<Map> paramsList) {
		this.paramsList = paramsList;
	}
	
	
	
}
