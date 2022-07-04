/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.cache;

import java.io.Serializable;

/**
 * <pre>
 * rose.mary.trace.data.cache
 * CacheInfo.java
 * </pre>
 * @author whoana
 * @date Sep 3, 2019
 */
public class CacheInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int currentSizeC1;
	
	int currentSizeC2;
	
	long averageReadTime;
	
	long averageRemoveTime;
	
	long averageWriteTime;
	int currentNumberOfEntries;
	int currentNumberOfEntriesInMemory;
	long dataMemoryUsed;
	long evictions;
	long hits;
	long misses;
	long offHeapMemoryUsed;
	long removeHits;
	long removeMisses;
	long retrievals;
	long stores;
	long timeSinceReset;
	long timeSinceStart;
	long totalNumberOfEntries;
	/**
	 * @param size
	 */
	public void setCurrentSizeC1(int size) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param size
	 */
	public void setCurrentSizeC2(int size) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the currentSizeC1
	 */
	public int getCurrentSizeC1() {
		return currentSizeC1;
	}

	/**
	 * @return the currentSizeC2
	 */
	public int getCurrentSizeC2() {
		return currentSizeC2;
	}

	/**
	 * @return the averageReadTime
	 */
	public long getAverageReadTime() {
		return averageReadTime;
	}

	/**
	 * @param averageReadTime the averageReadTime to set
	 */
	public void setAverageReadTime(long averageReadTime) {
		this.averageReadTime = averageReadTime;
	}

	/**
	 * @return the averageRemoveTime
	 */
	public long getAverageRemoveTime() {
		return averageRemoveTime;
	}

	/**
	 * @param averageRemoveTime the averageRemoveTime to set
	 */
	public void setAverageRemoveTime(long averageRemoveTime) {
		this.averageRemoveTime = averageRemoveTime;
	}

	/**
	 * @return the averageWriteTime
	 */
	public long getAverageWriteTime() {
		return averageWriteTime;
	}

	/**
	 * @param averageWriteTime the averageWriteTime to set
	 */
	public void setAverageWriteTime(long averageWriteTime) {
		this.averageWriteTime = averageWriteTime;
	}

	/**
	 * @return the currentNumberOfEntries
	 */
	public int getCurrentNumberOfEntries() {
		return currentNumberOfEntries;
	}

	/**
	 * @param currentNumberOfEntries the currentNumberOfEntries to set
	 */
	public void setCurrentNumberOfEntries(int currentNumberOfEntries) {
		this.currentNumberOfEntries = currentNumberOfEntries;
	}

	/**
	 * @return the currentNumberOfEntriesInMemory
	 */
	public int getCurrentNumberOfEntriesInMemory() {
		return currentNumberOfEntriesInMemory;
	}

	/**
	 * @param currentNumberOfEntriesInMemory the currentNumberOfEntriesInMemory to set
	 */
	public void setCurrentNumberOfEntriesInMemory(int currentNumberOfEntriesInMemory) {
		this.currentNumberOfEntriesInMemory = currentNumberOfEntriesInMemory;
	}

	/**
	 * @return the dataMemoryUsed
	 */
	public long getDataMemoryUsed() {
		return dataMemoryUsed;
	}

	/**
	 * @param dataMemoryUsed the dataMemoryUsed to set
	 */
	public void setDataMemoryUsed(long dataMemoryUsed) {
		this.dataMemoryUsed = dataMemoryUsed;
	}

	/**
	 * @return the evictions
	 */
	public long getEvictions() {
		return evictions;
	}

	/**
	 * @param evictions the evictions to set
	 */
	public void setEvictions(long evictions) {
		this.evictions = evictions;
	}

	/**
	 * @return the hits
	 */
	public long getHits() {
		return hits;
	}

	/**
	 * @param hits the hits to set
	 */
	public void setHits(long hits) {
		this.hits = hits;
	}

	/**
	 * @return the misses
	 */
	public long getMisses() {
		return misses;
	}

	/**
	 * @param misses the misses to set
	 */
	public void setMisses(long misses) {
		this.misses = misses;
	}

	/**
	 * @return the offHeapMemoryUsed
	 */
	public long getOffHeapMemoryUsed() {
		return offHeapMemoryUsed;
	}

	/**
	 * @param offHeapMemoryUsed the offHeapMemoryUsed to set
	 */
	public void setOffHeapMemoryUsed(long offHeapMemoryUsed) {
		this.offHeapMemoryUsed = offHeapMemoryUsed;
	}

	/**
	 * @return the removeHits
	 */
	public long getRemoveHits() {
		return removeHits;
	}

	/**
	 * @param removeHits the removeHits to set
	 */
	public void setRemoveHits(long removeHits) {
		this.removeHits = removeHits;
	}

	/**
	 * @return the removeMisses
	 */
	public long getRemoveMisses() {
		return removeMisses;
	}

	/**
	 * @param removeMisses the removeMisses to set
	 */
	public void setRemoveMisses(long removeMisses) {
		this.removeMisses = removeMisses;
	}

	/**
	 * @return the retrievals
	 */
	public long getRetrievals() {
		return retrievals;
	}

	/**
	 * @param retrievals the retrievals to set
	 */
	public void setRetrievals(long retrievals) {
		this.retrievals = retrievals;
	}

	/**
	 * @return the stores
	 */
	public long getStores() {
		return stores;
	}

	/**
	 * @param stores the stores to set
	 */
	public void setStores(long stores) {
		this.stores = stores;
	}

	/**
	 * @return the timeSinceReset
	 */
	public long getTimeSinceReset() {
		return timeSinceReset;
	}

	/**
	 * @param timeSinceReset the timeSinceReset to set
	 */
	public void setTimeSinceReset(long timeSinceReset) {
		this.timeSinceReset = timeSinceReset;
	}

	/**
	 * @return the totalNumberOfEntries
	 */
	public long getTotalNumberOfEntries() {
		return totalNumberOfEntries;
	}

	/**
	 * @param totalNumberOfEntries the totalNumberOfEntries to set
	 */
	public void setTotalNumberOfEntries(long totalNumberOfEntries) {
		this.totalNumberOfEntries = totalNumberOfEntries;
	}

	/**
	 * @return the timeSinceStart
	 */
	public long getTimeSinceStart() {
		return timeSinceStart;
	}

	/**
	 * @param timeSinceStart the timeSinceStart to set
	 */
	public void setTimeSinceStart(long timeSinceStart) {
		this.timeSinceStart = timeSinceStart;
	}

	 
	
	
}
