package rose.mary.trace.sample.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;

public class StoreMember {

    public static void main(String[] args) throws Exception {
    	//MapStoreConfig mconfig = new MapStoreConfig().setClassName("");
    	
    	
       // QueueStoreConfig storeConfig = new QueueStoreConfig().setClassName(TheQueueStore.class.getName());
    	QueueStoreConfig storeConfig = new QueueStoreConfig();
    	
    	TheQueueStore  storeImplementation = new TheQueueStore();
    	
        storeConfig.setStoreImplementation(storeImplementation);
        
        QueueConfig queueConfig = new QueueConfig().setName("queue");
        queueConfig.setQueueStoreConfig(storeConfig);
        Config config = new Config().addQueueConfig(queueConfig);
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        IQueue<Item> queue = hz.getQueue("queue");
        long elapsed = System.currentTimeMillis();
        for(int i = 0 ; i < 2; i ++) {
        	queue.put(new Item(i +""));
        }
        System.out.println("1:" + (System.currentTimeMillis() - elapsed));
        elapsed = System.currentTimeMillis();
        for(int i = 0 ; i < 2; i ++) {        	
        	queue.poll();
        }
        System.out.println("2:" + (System.currentTimeMillis() - elapsed));
        System.exit(0);
    }
}
