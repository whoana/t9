/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.manager;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.TransactionManager;

import org.junit.Before;
import org.junit.Test;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.manager.CacheManager;
import rose.mary.trace.manager.ConfigurationManager;

/**
 * <pre>
 * rose.mary.trace.core.manager
 * CacheManagerTest.java
 * </pre>
 * @author whoana
 * @date Sep 6, 2019
 */
public class CacheManagerTest {
	CacheManager cacheManager = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.setProperty("rose.mary.home", "/Users/whoana/Documents/gitlab/t9/t9-trace/bin/main");
	}

	//@Test
	public void test() {
		try {
			ConfigurationManager configurationManager = new ConfigurationManager();
			configurationManager.prepare();
			cacheManager = new CacheManager(configurationManager.getCacheManagerConfig());
			cacheManager.prepare();
			
			
			
			CacheProxy<String, Trace> cache = cacheManager.getDistributeCaches().get(1);
			//TransactionManager tm = cache.getTransactionManager();
			//tm.begin();
			//tm.commit();
			int dup = 0;
			int tryCnt = 0;
			Map<String, String> map = new HashMap<String, String>();
			while(true) {
				Iterator iterator = cache.iterator();
				
				
				while(iterator.hasNext()) {
					Object entry = iterator.next();
					iterator.remove();
					String key = cache.getKey(entry);
					System.out.println("key:" + key);
					if(map.containsKey(key)) {
						dup ++;
					}
					map.put(key,key);
					tryCnt ++;
					if(tryCnt % 100 == 0) {
						//TransactionManager tm = cache.getTransactionManager();
						//tm.begin();
						//tm.commit();
						break;
					}
					
				}
				
				//if(dup > 0) {
					System.out.println("dup:"+dup);
				
					System.out.println("tryCnt:"+tryCnt);
					Thread.sleep(500);
				//}
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	//@Test
	public void testRestore() {
		try {
			ConfigurationManager configurationManager = new ConfigurationManager();
			configurationManager.prepare();
			cacheManager = new CacheManager(configurationManager.getCacheManagerConfig());
			cacheManager.prepare();
			
			CacheProxy<String, Trace> mc = cacheManager.getMergeCache();
			Iterator iterator = mc.iterator();
			int totalCount = 0;
			List<String> dups = new ArrayList<String>();
			String oldKey = null;
			while(iterator.hasNext()) {
				Object entry = iterator.next();
				iterator.remove();
				totalCount ++;
				CacheProxy<String, Trace> dc = cacheManager.getDistributeCaches().get((totalCount % 5));
				
				String key = dc.getKey(entry);
				Trace trace = dc.getValue(entry);
				dc.put(key, trace);
				if(oldKey != null && oldKey.equals(key)) {
					dups.add(key);
				}
				oldKey = key;
			}
			
			System.out.println("dup size:"+ dups.size());
			for(String key : dups) {
				System.out.println(key);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	//@Test
	public void testCopy() {
		try {
			ConfigurationManager configurationManager = new ConfigurationManager();
			configurationManager.prepare();
			cacheManager = new CacheManager(configurationManager.getCacheManagerConfig());
			cacheManager.prepare();
			
			CacheProxy<String, Trace> mc = cacheManager.getMergeCache();
			Iterator iterator = mc.iterator();
			int totalCount = 0;
			 
			CacheProxy<String, Trace> target = cacheManager.getTestCache();
			while(iterator.hasNext()) {
				Object entry = iterator.next();
				iterator.remove();
				totalCount ++;
				
				String key = mc.getKey(entry);
				Trace trace = mc.getValue(entry);
				target.put(key, trace);
				
			}
			
		    System.out.println("total count:"+totalCount);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	@Test
	public void testReferencePut() {
		try {
			ConfigurationManager configurationManager = new ConfigurationManager();
			configurationManager.prepare();
			cacheManager = new CacheManager(configurationManager.getCacheManagerConfig());
			cacheManager.prepare();
			
			CacheProxy<String, Trace> ec = cacheManager.getErrorCache01();
			Trace trace = new Trace();
			trace.setId("1");
			trace.setHostId("whoana");
			ec.put("1", trace);
			trace.setHostId("teo");
			
			
			Trace trace1 = ec.get("1");
			System.out.println("host1:" + trace1.getHostId());
			
			ec.put("1", trace);
			Trace trace2 = ec.get("1");
			System.out.println("host2:" + trace2.getHostId());
			
			
			cacheManager.closeCache();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Test
	public void testReferenceGet() {
		try {
			ConfigurationManager configurationManager = new ConfigurationManager();
			configurationManager.prepare();
			cacheManager = new CacheManager(configurationManager.getCacheManagerConfig());
			cacheManager.prepare();
			
			CacheProxy<String, Trace> ec = cacheManager.getErrorCache01();
			
			Trace trace = ec.get("1");
			System.out.println("host:" + trace.getHostId());
			
			
			
			cacheManager.closeCache();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
