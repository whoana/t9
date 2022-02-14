/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.sample.chronicle;

import java.io.File;
import java.util.Scanner;
import java.util.Set;

import net.openhft.chronicle.map.ChronicleMap;

/**
 * <pre>
 * rose.mary.trace.sample.chronicle
 * Producer.java
 * </pre>
 * @author whoana
 * @date Sep 28, 2019
 */
public class Producer {
	public static void main(String args[]) {
		try {
			ChronicleMap<Integer, String> map = ChronicleMap
					  .of(Integer.class, String.class)
					  .name("country-map")
					  .entries(10000000)
					  .averageValue("America")
					  .createOrRecoverPersistedTo(new File("./country-details.dat"));
					  //.createPersistedTo(new File("./country-details.dat"));
			
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					map.close();
					System.out.println("producer dieing");
				}
			}));

			int key = 0;
			Scanner scanner = new Scanner(System.in);
			while(true) {
				System.out.println("input:");
				if(scanner.hasNext()) {
					String msg = scanner.next();
					map.put(key ++, msg);
					System.out.println("your msg:" + msg);
				}
			}
			  
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	} 
}
