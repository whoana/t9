/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.sample.chronicle;

import java.io.File;

import net.openhft.chronicle.core.values.LongValue;
import net.openhft.chronicle.map.ChronicleMap;

/**
 * <pre>
 * rose.mary.trace.sample.chronicle
 * PersistMap.java
 * </pre>
 * @author whoana
 * @date Sep 28, 2019
 */
public class PersistMap {
	public static void main(String args[]) {
		//write();
		read();
	}
	
	public static void write() {
		try {
			ChronicleMap<Integer, String> map = ChronicleMap
					  .of(Integer.class, String.class)
					  .name("country-map")
					  .entries(10000000)
					  .averageValue("America")
					  .createPersistedTo(new File("./country-details.dat"));
			long elapsed = System.currentTimeMillis();
			for(int i = 0 ; i < 100000 ; i ++)
				map.put(i, i +" msg msg msg");
			 System.out.println("1:" + (System.currentTimeMillis() - elapsed));
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void read() {
		try {
			ChronicleMap<Integer, String> map = ChronicleMap
					  .of(Integer.class, String.class)
					  .name("country-map")
					  .entries(10000000)
					  .averageValue("America")
					  .createPersistedTo(new File("./country-details.dat"));
			long elapsed = System.currentTimeMillis();
			for(int i = 0 ; i < 100000 ; i ++)
				map.get(i);
			 System.out.println("1:" + (System.currentTimeMillis() - elapsed));
			 System.out.println("size 1:"+ map.size());
			 map.remove(1);
			 System.out.println("size 2:" + map.size());
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
