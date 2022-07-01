/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.cache.ehcache;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set; 

import javax.transaction.TransactionManager;

import org.ehcache.Cache;
import org.ehcache.Cache.Entry;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.data.cache.CacheInfo; 

/**
 * <pre>
 * The EhcacheProxy is a implementation of {@link CacheProxy}. 
 * </pre>
 * @author whoana
 * @param <V>
 * @since Aug 5, 2019
 */ 
public class EhcacheProxy<K, V> extends CacheProxy<K, V>{
  
	Cache<K, V> cache = null;
	 
	public EhcacheProxy(Cache<K, V> cache) {
		 this.cache = cache;
	}
	
	public EhcacheProxy(String name, Cache<K, V> cache) {
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

	
	public Iterator iterator() {
		return cache.iterator();
	}
	 
 
	
	@Override
	public void removeAll(Set keys) throws Exception {
		cache.removeAll(keys);
	}
	
	@Override
	public void put(Map<K, V> entries) throws Exception {
		cache.putAll(entries);
	}

	@Override
	public TransactionManager getTransactionManager() throws Exception{
		// TODO Auto-generated method stub
		//return null;
		throw new Exception("NotYetImplementationException");
	}
	
	@Override
	public K getKey(Object obj) {
		Entry<K, V> entry = (Entry<K, V>)obj;
		return entry != null ? entry.getKey() : null;
	}

	@Override
	public V getValue(Object obj) {
		Entry<String, V> entry = (Entry<String, V>)obj;
		return entry != null ? entry.getValue() : null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public CacheInfo getCacheInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V remove(K key) throws Exception {
		V value = cache.get(key);
		cache.remove(key);
		return value;
	}

	@Override
	public Iterator iterator2() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<K> keys() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsKey(K key) {
		// TODO Auto-generated method stub
		return cache.containsKey(key);
	}
	
	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public boolean isAccessable() {
		return true;
	}
}
