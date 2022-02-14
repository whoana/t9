/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.cache.hazelcast;

import java.util.Collection;
import java.util.Map;

import com.hazelcast.core.MapStore;

import rose.mary.trace.data.common.Trace;
import rose.mary.trace.database.service.TraceCacheMapStoreService;

/**
 * <pre>
 * 아직 구현하지 않았다.
 * </pre>
 * @author whoana
 * @param <V>
 * @since Sep 28, 2019
 */
public class TraceMapStoreImpl implements MapStore<String, Trace> {

	TraceCacheMapStoreService tcms;
	
	public TraceMapStoreImpl(TraceCacheMapStoreService tcms) {
		this.tcms = tcms;
	}

	@Override
	public Trace load(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Trace> loadAll(Collection<String> keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<String> loadAllKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(String key, Trace value) {
		// TODO Auto-generated method stub
		//tcms.store(key, value);
	}

	@Override
	public void storeAll(Map<String, Trace> map) {
		// TODO Auto-generated method stub
		//tcms.storeAll(map);
	}

	@Override
	public void delete(String key) {
		// TODO Auto-generated method stub
		//tcms.delete(key);
	}

	@Override
	public void deleteAll(Collection<String> keys) {
		// TODO Auto-generated method stub
		//tcms.deleteAll(keys);
	}
	 

}
