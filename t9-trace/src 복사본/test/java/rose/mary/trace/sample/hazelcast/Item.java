package rose.mary.trace.sample.hazelcast;

import java.io.Serializable;

public class Item implements Serializable {
	String id;
	public Item(String id) {
		this.id = id;
	}
}
