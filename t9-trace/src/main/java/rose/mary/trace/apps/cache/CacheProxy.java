/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.transaction.TransactionManager;

import rose.mary.trace.data.cache.CacheInfo; 

/**
 * <pre>
 * The Cache is a abstract cache class. 
 * </pre>
 * @author whoana
 * @since Aug 5, 2019
 */
public abstract class CacheProxy<K,V>{
	/**
	 * 
	 */
	protected String name;
	 
	/**
	 * 캐시의 트랜젝션 관리자를 리턴합나다.
	 * @return
	 * @throws Exception
	 */
	public abstract TransactionManager getTransactionManager() throws Exception;
	
	/**
	 * 캐시에 값일 입력합니다. 
	 * @param key 키 
	 * @param value 값 
	 * @throws Exception
	 */
	public abstract void put(K key, V value) throws Exception;
	
	/**
	 * 키에 해당하는 값을 리턴합니다.
	 * @param key 키 
	 * @return 값 
	 * @throws Exception
	 */
	public abstract V get(K key) throws Exception;
	
	/**
	 * 캐시에 저장된 현재 오브젝트 개 수를 리턴합니다.
	 * @return
	 */
	public abstract int size();
	
	/**
	 * 현재상태의 캐시 정보 ({@link CacheInfo})를 리턴합니다. 
	 * @return
	 */
	public abstract CacheInfo getCacheInfo();
	 
	/**
	 * 캐시 내에 저장된 값들의 오브젝트들을 참조할 수 있도록 컬렉션 {@link Collection} 객체를 반환합니다.  
	 * @return
	 * @throws Exception
	 */
	public abstract Collection<V> values()throws Exception;
	
	/**
	 * 캐시 내에 저장된 키들을 참조할 수 있도록 셋 {@link Set} 객체를 반환합니다.  
	 * @return
	 * @throws Exception
	 */
	public abstract Set<K> keys()throws Exception;
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract Iterator iterator() throws Exception;
 
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract Iterator iterator2() throws Exception;

	/**
	 * 
	 * @param keys
	 * @throws Exception
	 */
	public abstract void removeAll(Set keys) throws Exception;
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public abstract boolean containsKey(K key) ;
	
	/**
	 * @param removeKeys
	 */
	public abstract V remove(K key) throws Exception;

	/**
	 * @param entries
	 * @throws Exception
	 */
	public abstract void put(Map<K, V> entries) throws Exception ;

	/**
	 * @param entry
	 * @return
	 */
	public abstract K getKey(Object entry);

	/**
	 * @param entry
	 * @return
	 */
	public abstract V getValue(Object entry);

	/**
	 * 캐시 이름을 리턴합니다.
	 * @return 이름 {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * 캐시 이름을 지정합니다. 
	 * @param name 캐시 이름 {@link #name}
	 */
	public void setName(String name) {
		this.name = name;
	}
 
	 
	public abstract void clear();
	
	public abstract boolean isAccessable();
}
