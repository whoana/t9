/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.parser;

import java.util.Map;

import pep.per.mint.common.util.Util;
import rose.mary.trace.data.common.Trace;
import rose.mary.trace.exception.ParsingException;
/**
 * <pre>
 * RestMessageParser
 * Rest Service로 부터 유입된 트레킹 메시지를 파싱한다.
 * </pre>
 * @author whoana
 * @since 2020.03.19
 */
public class RestMessageParser extends Parser {

	Map<String, Integer> nodeMap;
	public RestMessageParser() {}
	public RestMessageParser(Map<String, Integer> nodeMap) {
		this.nodeMap = nodeMap;
	}
	
	@Override
	public Trace parse(Object msg) throws Exception {
	 
		Trace trace = new Trace();
		Map<String, Object> input = (Map<String, Object>)msg;
 
		String integrationId 	= parseStringValue(input, "integrationId"	, true);
		String originHostId   	= parseStringValue(input, "originHostId"	, true);
		String hostId 		  	= parseStringValue(input, "hostId"			, true);
		String date 		  	= parseStringValue(input, "date"			, true);
		String processId 	  	= parseStringValue(input, "processId"		, true);
		String processDate    	= parseStringValue(input, "processDate"		, true);
		String processEndDate 	= parseStringValue(input, "processEndDate"	, true);
		String type 			= parseStringValue(input, "type"			, true);
		int    todoNodeCount    = parseIntValue   (input, "todoNodeCount"	, true);
		String status 			= parseStringValue(input, "status"			, true);
		String errorCode 		= parseStringValue(input, "errorCode"		, false);
		String errorMessage 	= parseStringValue(input, "errorMessage"	, false);
		String recordCount 		= parseStringValue(input, "recordCount"		, false);
		String dataSize 		= parseStringValue(input, "dataSize"		, false);
		String compress 		= parseStringValue(input, "compress"		, false);
		String data 			= parseStringValue(input, "data"			, false);
		
		

		trace.setIntegrationId(integrationId);
		trace.setHostId(hostId);
		trace.setDate(date);
		trace.setProcessId(processId);
		trace.setProcessDate(processDate);
		trace.setProcessEndDate(processEndDate);
		trace.setType(type);
		trace.setIp("");
		trace.setOriginHostId(originHostId);
		trace.setOs("");
		trace.setStatus(status);
		trace.setErrorCode(errorCode);
		trace.setErrorMessage(errorMessage);
		
		trace.setRecordCount(Util.isEmpty(recordCount) ? "0" : recordCount );
		
		trace.setDataSize(Util.isEmpty(dataSize) ? "0" : dataSize );
		
		trace.setCompress(compress);
		trace.setData(data.getBytes());
		trace.setTodoNodeCount(todoNodeCount); 
		 

		trace.setId(Util.join(trace.getIntegrationId(),idSeperator, trace.getDate(), idSeperator, hostId, idSeperator, processId ));
		
		if(nodeMap != null) {
			 trace.setSeq(nodeMap.getOrDefault(trace.getType(), 0));
		}
		
		//check required fields
		return trace;
	}

	private int parseIntValue(Map<String, Object> input, String key, boolean required) throws ParsingException {
		Integer value = (Integer)input.get(key);
		if(required && value == null) throw new ParsingException(Util.join("not found ", key, " info in a message"));
		return value == null ? 0 : value;
	}

	private String parseStringValue(Map<String, Object> input, String key, boolean required) throws ParsingException {
		String value = (String)input.get(key);
		if(required && value == null) throw new ParsingException(Util.join("not found ", key, " info in a message"));
		return value == null ? "" : value;
	}
	
	

}
