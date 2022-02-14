package rose.mary.trace.apps.manager.config;

import java.io.Serializable;

public class SystemErrorTestManagerConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getExceptionDelay() {
		return exceptionDelay;
	}

	public void setExceptionDelay(int exceptionDelay) {
		this.exceptionDelay = exceptionDelay;
	}

	public boolean isStartOnLoad() {
		return startOnLoad;
	}

	public void setStartOnLoad(boolean startOnLoad) {
		this.startOnLoad = startOnLoad;
	}

	String name;
	
	int delay = 1000;
	
	int exceptionDelay = 1000;
	
	boolean startOnLoad = false;
	
	
	
}
