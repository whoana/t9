package rose.mary.trace.apps.handler;

import java.io.Serializable;

import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Trace;

public interface StateCheckHandler extends Serializable{
	
	/**
	 * <pre>
	 * 채널별로 발생된 트레킹이 들어오는 시점에 Trace 객체를 체크하여 완료 처리 여부를 결정하고 State 객체에 결과를 세팅하는 스펙을 정의한다. 
	 * </pre>
	 * @param trace 트레킹 객체 
	 * @param state 트레킹 상태 객체 
	 * @throws Exception 
	 */
	public void checkAndSet(boolean first, Trace trace, State state) throws Exception;
	
}
