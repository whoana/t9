/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.cache.chronicle;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.transaction.TransactionManager;
import net.openhft.chronicle.map.ChronicleMap;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.data.cache.CacheInfo;

/**
 * <pre>
 * The ChronicleCacheProxy is a implementation of {@link CacheProxy}. 
 * </pre>
 * @author whoana
 * @since Sep 29, 2019
 */
public class ChronicleCacheProxy<K, V> extends CacheProxy<K, V>{

	ChronicleMap<K, V> map;
	
	 
	
	public ChronicleCacheProxy(String name, ChronicleMap<K, V> map) {
		super();
		super.name = name;
		this.map = map;
	}
	
	@Override
	public TransactionManager getTransactionManager() throws Exception {
		return null;
	}

	@Override
	public void put(K key, V value) throws Exception {
		map.put(key, value);
	}

	@Override
	public V get(K key) throws Exception {
		return map.get(key);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return map.size();
	}

	@Override
	public CacheInfo getCacheInfo() {
		CacheInfo info = new CacheInfo();
		
		
		
		info.setAverageReadTime(0);
		info.setAverageRemoveTime(0);
		info.setAverageWriteTime(0);
		info.setCurrentNumberOfEntries(map.size());
		info.setCurrentNumberOfEntriesInMemory(map.size());
		info.setDataMemoryUsed(0);
		info.setEvictions(0);
		info.setHits(0);
		info.setMisses(0);
		info.setOffHeapMemoryUsed(map.offHeapMemoryUsed());
		info.setRemoveHits(0);
		info.setRemoveMisses(0);
		info.setRetrievals(0);
		info.setStores(map.size());
		info.setTimeSinceReset(0);
		info.setTimeSinceStart(0);
		info.setTotalNumberOfEntries(map.size());
		
		
		return info;
	}

	@Override
	public Iterator iterator() throws Exception {
		return map.values().iterator();
	}

	@Override
	public Iterator iterator2() throws Exception {
		return map.values().iterator();
	}

	@Override
	public void removeAll(Set keys) throws Exception {
		for(Object key : keys) map.remove(key);
	}

	@Override
	public V remove(K key) throws Exception {
		return map.remove(key);
	}

	@Override
	public void put(Map<K, V> entries) throws Exception {
		map.putAll(entries);
	}

	@Override
	public K getKey(Object entry) {
		return null;
	}

	@Override
	public V getValue(Object entry) {
		return (V)entry;
	}

	@Override
	public Collection<V> values() throws Exception {
		return map.values();
	}

	@Override
	public Set<K> keys() throws Exception {		
		return map.keySet();
	}

	@Override
	public boolean containsKey(K key) {
		// TODO Auto-generated method stub
		return map.containsKey(key);
	}

	@Override
	public void clear() {
		map.clear();
	}
	
	@Override
	public boolean isAccessable() {
		return true;
	}
}