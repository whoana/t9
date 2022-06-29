/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.parser;

import rose.mary.trace.core.data.common.Trace;

/**
 * <pre>
 * rose.mary.trace.parser
 * Parser.java
 * </pre>
 * @author whoana
 * @date Aug 13, 2019
 */
public abstract class Parser {
	
	public static final String idSeperator = "@";
	
	public abstract Trace parse(Object trace) throws Exception;
}
