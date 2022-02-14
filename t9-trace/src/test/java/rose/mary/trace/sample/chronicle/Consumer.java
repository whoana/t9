/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.sample.chronicle;

import java.io.File;
import java.util.Set;

import net.openhft.chronicle.map.ChronicleMap;

/**
 * <pre>
 * rose.mary.trace.sample.chronicle
 * Consumer.java
 * </pre>
 * @author whoana
 * @date Sep 28, 2019
 */
public class Consumer {
	public static void main(String args[]) {
		try {
			ChronicleMap<Integer, String> map = ChronicleMap
					  .of(Integer.class, String.class)
					  .name("country-map")
					  .entries(10000000)
					  .averageValue("America")
					  .createOrRecoverPersistedTo(new File("./country-details.dat"));
			
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					map.close();
					System.out.println("consumer dieing");
				}
			}));
			while(true) {
				Set<Integer> keys = map.keySet();
				if(keys == null || keys.size() == 0) {
					Thread.sleep(10*1000);
					System.out.println("no message here!");
					continue;
				}
				for (Integer key : keys) {
					
					String msg = map.remove(key);
					System.out.println("msg :" + msg);
					System.out.println("current size :" + map.size());
				}
			}
			 
			  
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	} 
}
