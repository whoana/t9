package rose.mary.trace.data.policy;

import java.io.Serializable;

public class DatabasePolicy implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int shutdownServer = 100;   
	public static final int stopServer     = 200;
	public static final int stopChannel    = 300;
	
	public static final DatabasePolicy SHUTDOWN     = new DatabasePolicy(shutdownServer);
	public static final DatabasePolicy STOP_SERVER  = new DatabasePolicy(stopServer);
	public static final DatabasePolicy STOP_CHANNEL = new DatabasePolicy(stopChannel);
	
	int policy = stopChannel;
	
	public DatabasePolicy() {
		this.policy = stopChannel;
	}
	
	public DatabasePolicy(int policy) {
		this.policy = policy;
	}

	public int getPolicy() {
		return policy;
	}

	public void setPolicy(int policy) {
		this.policy = policy;
	}
	
	@Override
	public String toString() {
		String str  = "unknown policy";
		switch(policy) {
		case shutdownServer :
			str = "shutdownServerPolicy";
			break;
		case stopChannel :
			str = "stopChannelPolicy";
			break;
		case stopServer :
			str = "stopServerPolicy";
			break;
		}
		return str;
	}
	
}
