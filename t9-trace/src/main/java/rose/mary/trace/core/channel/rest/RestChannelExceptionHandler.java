/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.channel.rest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import pep.per.mint.common.util.Util;
import rose.mary.trace.core.channel.ChannelExceptionHandler;

/**
 * <pre>
 * MOMChannelExceptionHandler
 * MQ, ILink 채널을 통해 읽어들인 메시지를 파싱할때 발생하는 예외를 처리하기 위한 핸들러.
 * </pre>
 * @author whoana
 * @since 2020.03
 */
public class RestChannelExceptionHandler implements ChannelExceptionHandler {

	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean translateOriginMsg = false;
	
	public RestChannelExceptionHandler(boolean translateOriginMsg) {
		this.translateOriginMsg = translateOriginMsg;
	}
	
	@Override
	public void handleException(String msg, Exception e) {
		logger.error(msg, e);
	}

	@Override
	public void handleParserException(String msg, Exception e, Object trace) {
		 if(translateOriginMsg) {
			String orignMessage = Util.toJSONString(trace); 
			logger.error(msg + "orign msg:\n" + orignMessage, e);
		}else {
			logger.error(msg, e);
		}
	}

}
