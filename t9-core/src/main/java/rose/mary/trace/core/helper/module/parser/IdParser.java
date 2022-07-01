/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.helper.module.parser;

import rose.mary.trace.core.data.common.Trace;

/**
 * <pre>
 * rose.mary.trace.parser
 * IdParser.java
 * </pre>
 * @author whoana
 * @date Aug 5, 2019
 */
public interface IdParser {
	public String getId(Trace trace) throws Exception;
}
