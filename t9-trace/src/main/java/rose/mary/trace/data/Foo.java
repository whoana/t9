/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.data;

import java.io.Serializable;

/**
 * <pre>
 * rose.mary.trace.data
 * Foo.java
 * </pre>
 * @author whoana
 * @date Aug 27, 2019
 */
public class Foo implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id;
	String val;
	/**
	 * @param string
	 * @param string2
	 */
	public Foo(String id, String val) {
		this.id = id;
		this.val = val;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the val
	 */
	public String getVal() {
		return val;
	}
	/**
	 * @param val the val to set
	 */
	public void setVal(String val) {
		this.val = val;
	}
	
	
}
