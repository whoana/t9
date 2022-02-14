/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.data.cache;

/**
 * <pre>
 * rose.mary.trace.data.cache
 * CacheConfig.java
 * </pre>
 * @author whoana
 * @date Aug 23, 2019
 */
public class CacheConfig {
 
		
	public static int MEM_UNIT_B = 0;
	public static int MEM_UNIT_K = 1;
	public static int MEM_UNIT_M = 2;
	public static int MEM_UNIT_G = 3;
	
	String name;
	

	int memoryUnit = MEM_UNIT_G;
	int heapSize;
	int diskSize; 
	int maxEntries = 100000;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	 
	/**
	 * @return the memoryUnit
	 */
	public int getMemoryUnit() {
		return memoryUnit;
	}
	/**
	 * @param memoryUnit the memoryUnit to set
	 */
	public void setMemoryUnit(int memoryUnit) {
		this.memoryUnit = memoryUnit;
	}
	/**
	 * @return the heapSize
	 */
	public int getHeapSize() {
		return heapSize;
	}
	/**
	 * @param heapSize the heapSize to set
	 */
	public void setHeapSize(int heapSize) {
		this.heapSize = heapSize;
	}
	/**
	 * @return the diskSize
	 */
	public int getDiskSize() {
		return diskSize;
	}
	/**
	 * @param diskSize the diskSize to set
	 */
	public void setDiskSize(int diskSize) {
		this.diskSize = diskSize;
	}
	/**
	 * @return the maxEntries
	 */
	public int getMaxEntries() {
		return maxEntries;
	}
	/**
	 * @param maxEntries the maxEntries to set
	 */
	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}
	 
	
	
}
