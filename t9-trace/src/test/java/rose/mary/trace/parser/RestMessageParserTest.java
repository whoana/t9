package rose.mary.trace.parser;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeNotNull;
import static org.junit.Assume.assumeTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

import org.junit.Test;

import pep.per.mint.common.util.Util;
import rose.mary.trace.data.common.Trace;

public class RestMessageParserTest {

	Parser parser = new RestMessageParser();
	
	@Test
	public void test() throws Exception {
		Map<String, Object> msg = new HashMap<String, Object>();
		
		
		 
		
		msg.put("integrationId"	, "EG_EGSSRM_BD_001");
		msg.put("originHostId"	, "testhost01");
		msg.put("hostId"		, "testhost01");
		msg.put("date"			, Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI));
		msg.put("processId"		, "Sender01");
		msg.put("processDate"	, Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI));
		msg.put("processEndDate", Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI));
		msg.put("type"			, "SNDR");
		msg.put("todoNodeCount"	, 1);
		msg.put("status"		, "00");
		
		
		Trace trace = parser.parse(msg);
		assumeNotNull(trace);
		System.out.println(Util.toJSONPrettyString(trace));
		
		
	}
	
	 

}
