package rose.mary.trace.data.cache;
 
import java.util.ArrayList;
import java.util.List;
 

public class CacheSummary {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int tps;
	
	List<Integer> dcDepth = new ArrayList<Integer>();
	
	int mcDepth = 0;

	List<Integer> bcDepth = new ArrayList<Integer>();
	
	int fcDepth = 0;
	
	int ucDepth = 0;

	public List<Integer> getDcDepth() {
		return dcDepth;
	}

	public void setDcDepth(List<Integer> dcDepth) {
		this.dcDepth = dcDepth;
	}

	public int getMcDepth() {
		return mcDepth;
	}

	public void setMcDepth(int mcDepth) {
		this.mcDepth = mcDepth;
	}

	public List<Integer> getBcDepth() {
		return bcDepth;
	}

	public void setBcDepth(List<Integer> bcDepth) {
		this.bcDepth = bcDepth;
	}

	public int getFcDepth() {
		return fcDepth;
	}

	public void setFcDepth(int fcDepth) {
		this.fcDepth = fcDepth;
	}

	public int getTps() {
		return tps;
	}

	public void setTps(int tps) {
		this.tps = tps;
	}

	public void setUcDepth(int ucDepth) {
		this.ucDepth = ucDepth;
	}

	public int getUcDepth() {
		return ucDepth;
	}
	
	
	
	
}
