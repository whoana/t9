/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.simulator;

import java.util.List;

import rose.mary.trace.helper.module.mte.MTEHeader;

/**
 * <pre>
 * rose.mary.trace.test
 * TraceMsgCreator.java
 * </pre>
 * @author whoana
 * @date Jul 24, 2019
 */
public interface TraceMsgCreator {

	public List<MTEHeader> create();

	/**
	 * @param interfaceId
	 * @param status
	 * @return
	 */
	public List<MTEHeader> create(String interfaceId, String status);

	/**
	 * @param string
	 * @param i
	 * @return
	 */
	public String createInterfaceId(String prefix, int index);
	 

	/**
	 * @return
	 */
	public String createStatus(int index);
	
}
