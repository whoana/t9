/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.sample;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.function.Consumer;

import org.infinispan.Cache;
import org.infinispan.CacheCollection;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.transaction.TransactionMode;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import pep.per.mint.common.util.Util;

/**
 * <pre>
 * rose.mary.trace.sample
 * MapdbSample.java
 * </pre>
 * @author whoana
 * @date Sep 26, 2019
 */
public class MapdbSample {
	
	public static void fileDb() {
		
		try {
			DB db = DBMaker.fileDB("file.db").make();
			
			
			HTreeMap map = db.hashMap("test").createOrOpen();
			map.clear();
			
			
			int test = 1000000;
			long elapsed = System.currentTimeMillis();
			for(int i = 0 ; i < test ; i ++) map.put(i+"", "whoana"+i);
			System.out.println("elapsed1:"+(System.currentTimeMillis() - elapsed));
			
			 
			elapsed = System.currentTimeMillis();
			
			Consumer<String> consumer = new Consumer<String>(){
				int cnt = 0;
				@Override
				public void accept(String t) {
					// TODO Auto-generated method stub
					cnt ++; 
				} 
				public int getCnt() {
					return cnt;
				}
				
			};
			
			map.values().iterator().forEachRemaining(consumer);
			System.out.println("elapsed2:"+(System.currentTimeMillis() - elapsed));	
			System.out.println("size:" + map.size());
			
			
			
			
			
			/*
			NavigableSet<String> set = db.treeSet("dist").serializer(Serializer.STRING).createOrOpen();
			set.clear();
			int test = 10000;
			long elapsed = System.currentTimeMillis();
			for(int i = 0 ; i < test ; i ++) set.add("whoana"+i);
			System.out.println("elapsed1:"+(System.currentTimeMillis() - elapsed));
			
			 
			elapsed = System.currentTimeMillis();
			
			Consumer<String> consumer = new Consumer<String>(){
				int cnt = 0;
				@Override
				public void accept(String t) {
					// TODO Auto-generated method stub
					cnt ++; 
				} 
				public int getCnt() {
					return cnt;
				}
				
			};
			
			set.iterator().forEachRemaining(consumer);
//			while(true) {
//				String item = set.pollFirst();
//				if(item == null) break;
//				//System.out.println(item);
//			}
			System.out.println("elapsed2:"+(System.currentTimeMillis() - elapsed));	
			System.out.println("size:" + set.size());
			*/
			
			
			
			db.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void infinispan() {
		
		try {
			
			DefaultCacheManager cacheManager = new DefaultCacheManager();
			
			String name = "test";
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.persistence()
					.passivation(false)
					.addSingleFileStore()
					.preload(true)
					.shared(false)
					.fetchPersistentState(true)
					.ignoreModifications(false)
					.purgeOnStartup(false)
					.location("." + java.io.File.separator + "infinispan");
			
			builder.transaction().transactionMode(TransactionMode.NON_TRANSACTIONAL);
			
			// Define local cache configuration
			cacheManager.defineConfiguration(name, builder.build());
			// Obtain the local cache
			Cache<String, String> cache = cacheManager.getCache(name);
			cache.clear();
			
			int test = 10000;
			long elapsed = System.currentTimeMillis();
			for(int i = 0 ; i < test ; i ++) cache.put(i+"","whoana"+i);
			System.out.println("elapsed1:"+(System.currentTimeMillis() - elapsed));
			 
			elapsed = System.currentTimeMillis();
			 
				
			Consumer<String> consumer = new Consumer<String>(){
				int cnt = 0;
				@Override
				public void accept(String t) {
					// TODO Auto-generated method stub
					cnt ++; 
				} 
				public int getCnt() {
					return cnt;
				}
				
			};
			
			 
			cache.values().forEach(consumer);
			
			System.out.println("elapsed2:"+(System.currentTimeMillis() - elapsed));
			System.out.println("size:" + cache.size());
			
			
			
			
			
			elapsed = System.currentTimeMillis();
			 

			int cnt = 0;
			CacheCollection<String> cols = cache.values();
			for (String string : cols) {
				cnt ++; 
			}
			
			System.out.println("elapsed2:"+(System.currentTimeMillis() - elapsed));
			System.out.println("size:" + cache.size());
			
			
			cacheManager.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
 

	public static void main(String args[]) {
		//fileDb();
		//infinispan();
		for(int i = 0 ; i < 10 ; i ++) {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate date = LocalDate.now();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String timeColonPattern = "HHmmssSSSnnnnnnnnn";
			DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern(timeColonPattern);
			LocalTime colonTime = LocalTime.now();
			System.out.println(date.format(format)+timeColonFormatter.format(colonTime));
			
		}
		
		 
		
		 
		
		

		 
	}

}
