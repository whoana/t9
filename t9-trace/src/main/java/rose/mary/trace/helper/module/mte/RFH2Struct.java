/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.helper.module.mte;

import pep.per.mint.common.util.Util;

/**
 * <pre>
 * rose.mary.trace.helper.module
 * RHF2Struct.java
 * </pre>
 * @author whoana
 * @date Jul 31, 2019
 */
public class RFH2Struct {

	public final static int rfh2HeaderSize = 36;
	public final static byte[] strucId = Util.rightPad("RFH",4," ").getBytes();
	public final static int version = 2;
	public final static int encoding = 546;
	public final static int codedCharSetId = -2;
	public final static byte[] format =  Util.rightPad("MQSTR",8," ").getBytes();
	public final static int flags = 0;
	public final static int nameValueCCSID = 1208;
	
}
