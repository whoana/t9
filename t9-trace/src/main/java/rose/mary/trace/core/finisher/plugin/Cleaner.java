package rose.mary.trace.core.finisher.plugin;
 
import rose.mary.trace.data.common.State;

public interface Cleaner {

	public void clean(long currentTime, State state) throws Exception;
	
}
