/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.helper.module.mte;

/**
 * <pre>
 * rose.mary.trace.helper.module.mte
 * MTEMessage.java
 * </pre>
 * @author whoana
 * @date Aug 1, 2019
 */
public class MTEMessage {

	MTEHeader header;
	byte[] data;
	/**
	 * @return the header
	 */
	public MTEHeader getHeader() {
		return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(MTEHeader header) {
		this.header = header;
	}
	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
	
	
}
