/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.helper.module.mte;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.helper.module.parser.IdParser;

/**
 * <pre>
 * rose.mary.trace.parser
 * MTEIdParser.java
 * </pre>
 * @author whoana
 * @date Aug 5, 2019
 */
public class MTEIdParser implements IdParser{

	@Override
	public String getId(Trace trace) throws Exception {
		return Util.join(trace.getIntegrationId(), trace.getDate());
	}

}
