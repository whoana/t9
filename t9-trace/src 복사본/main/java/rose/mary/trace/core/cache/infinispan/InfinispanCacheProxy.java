/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.cache.infinispan;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.infinispan.Cache;
import org.infinispan.CacheCollection;
import org.infinispan.stats.Stats;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.data.cache.CacheInfo;

import javax.transaction.TransactionManager;

/**
 * <pre>
 * The InfinispanCacheProxy is a implementation of {@link CacheProxy}. 
 * </pre>
 * @author whoana
 * @since Sep 2, 2019
 */
public class InfinispanCacheProxy<K, V> extends CacheProxy<K, V> {

	Cache<K, V> cache = null;
	
	public TransactionManager getTransactionManager() {
		return cache.getAdvancedCache().getTransactionManager();
	}
 

	public InfinispanCacheProxy(String name, Cache<K, V> cache) {
		super();
		super.name = name;
		this.cache = cache;
	}
	
	@Override
	public void put(K key, V value) throws Exception {
		cache.put(key, value); 
		
	}

	@Override
	public V get(K key) throws Exception {
		return cache.get(key);
	}
 
	public Iterator<K> keyIterator() throws Exception {
		return cache.keySet().iterator();
	}
 
	@Override
	public Iterator iterator() throws Exception {
		return cache.entrySet().iterator();
	}
	
	@Override
	public Iterator<V> iterator2() throws Exception {
		return cache.values().iterator();
	}
	
	public CacheCollection<V> values(){ 
		if(cache.getStatus().allowInvocations())
			return cache.values();
		else
			return null;
	}


	@Override
	public void removeAll(Set keys) throws Exception {
		for (Object key : keys) {
			cache.remove(key);
		}
	}

	@Override
	public void put(Map<K, V> entries) throws Exception {
		cache.putAll(entries);
	}

	@Override
	public K getKey(Object obj) {
		Entry<K, V> entry = (Entry<K, V>)obj;
		return entry != null ? entry.getKey() : null;
	}

	@Override
	public V getValue(Object obj) {
		Entry<K, V> entry = (Entry<K, V>)obj;
		return entry != null ? entry.getValue() : null;
	}

	@Override
	public int size() {
		return cache.size();
	}
	
	@Override
	public CacheInfo getCacheInfo() {
		CacheInfo info = new CacheInfo();
		Stats stats = cache.getAdvancedCache().getStats();
		
		info.setAverageReadTime(stats.getAverageReadTime());
		info.setAverageRemoveTime(stats.getAverageRemoveTime());
		info.setAverageWriteTime(stats.getAverageWriteTime());
		info.setCurrentNumberOfEntries(stats.getCurrentNumberOfEntries());
		info.setCurrentNumberOfEntriesInMemory(stats.getCurrentNumberOfEntriesInMemory());
		info.setDataMemoryUsed(stats.getDataMemoryUsed());
		info.setEvictions(stats.getEvictions());
		info.setHits(stats.getHits());
		info.setMisses(stats.getMisses());
		info.setOffHeapMemoryUsed(stats.getOffHeapMemoryUsed());
		info.setRemoveHits(stats.getRemoveHits());
		info.setRemoveMisses(stats.getRemoveMisses());
		info.setRetrievals(stats.getRetrievals());
		info.setStores(stats.getStores());
		info.setTimeSinceReset(stats.getTimeSinceReset());
		info.setTimeSinceStart(stats.getTimeSinceStart());
		info.setTotalNumberOfEntries(stats.getTotalNumberOfEntries());
		
		
		
		return info;
	}

	@Override
	public V remove(K key) throws Exception {
		return cache.remove(key);
	}


	@Override
	public Set<K> keys() throws Exception {
		// TODO Auto-generated method stub
		return cache.keySet();
	}


	@Override
	public boolean containsKey(K key) {
		return cache.containsKey(key);
	}


	@Override
	public void clear() {
		cache.clear();
	}
	
	@Override
	public boolean isAccessable() {
		return !(cache.getStatus().isStopping() || cache.getStatus().isTerminated());
	}
}
