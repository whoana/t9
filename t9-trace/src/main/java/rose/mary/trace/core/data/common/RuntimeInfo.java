package rose.mary.trace.core.data.common;

import java.io.Serializable; 

/**
 * <pre>
 * RuntimeInfo.java
 * T9 실행 시점부터의 통계 데이터 생성을 위한 DATA 객체  
 * </pre>
 * @author whoana
 * @sine 20200515
 */
public class RuntimeInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	String bootKey;
	
	long startedTime;
	
	int traceCountFromBoot = 0;
	
	int botCountFromBoot = 0;
	
	int traceCountFromHour = 0;
	
	int traceCountFromMinute = 0;

	public String getBootKey() {
		return bootKey;
	}

	public void setBootKey(String bootKey) {
		this.bootKey = bootKey;
	}

	public long getStartedTime() {
		return startedTime;
	}

	public void setStartedTime(long startedTime) {
		this.startedTime = startedTime;
	}

	public int getTraceCountFromBoot() {
		return traceCountFromBoot;
	}

	public void setTraceCountFromBoot(int traceCountFromBoot) {
		this.traceCountFromBoot = traceCountFromBoot;
	}

	public int getBotCountFromBoot() {
		return botCountFromBoot;
	}

	public void setBotCountFromBoot(int botCountFromBoot) {
		this.botCountFromBoot = botCountFromBoot;
	}

	public int getTraceCountFromHour() {
		return traceCountFromHour;
	}

	public void setTraceCountFromHour(int traceCountFromHour) {
		this.traceCountFromHour = traceCountFromHour;
	}

	public int getTraceCountFromMinute() {
		return traceCountFromMinute;
	}

	public void setTraceCountFromMinute(int traceCountFromMinute) {
		this.traceCountFromMinute = traceCountFromMinute;
	}
	
	
	
	 
}
