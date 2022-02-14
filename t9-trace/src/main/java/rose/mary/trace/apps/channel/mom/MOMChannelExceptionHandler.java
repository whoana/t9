/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.apps.channel.mom;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MessageEOFException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mq.MQMessage;

import rose.mary.trace.apps.channel.ChannelExceptionHandler;

/**
 * <pre>
 * MOMChannelExceptionHandler
 * MQ, ILink 채널을 통해 읽어들인 메시지를 파싱할때 발생하는 예외를 처리하기 위한 핸들러.
 * </pre>
 * @author whoana
 * @since 2020.03
 */
public class MOMChannelExceptionHandler implements ChannelExceptionHandler {

	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean translateOriginMsg = false;
	
	public MOMChannelExceptionHandler(boolean translateOriginMsg) {
		this.translateOriginMsg = translateOriginMsg;
	}
	
	@Override
	public void handleException(String msg, Exception e) {
		logger.error(msg, e);
	}

	@Override
	public void handleParserException(String msg, Exception e, Object trace) {
		if(translateOriginMsg) {
			StringBuffer orignMessage = new StringBuffer();
			
			if(trace instanceof BytesMessage) {
				BytesMessage tmsg = (BytesMessage)trace;
				while(true) {				
					try {
						String str = tmsg.readUTF();
						orignMessage.append(str);
					} catch (MessageEOFException  e1) {
						break;
					} catch (JMSException e1) {
						logger.error("JMSException:", e1);
						break;
					} catch(Exception e1) {  
						logger.error("Exception:", e1);
						break;
					}				
				}
				
			}else if(trace instanceof MQMessage) {
				MQMessage tmsg = (MQMessage)trace;
				try { 
					int length = tmsg.getDataLength();
					byte[] b = new byte[length];					
					tmsg.readFully(b);
					orignMessage.append(b);			 
				} catch(Exception e1) {  
					logger.error("Exception:", e1);
				}		 
			}else {
				orignMessage.append("msg format not supported.");
			}
			
			logger.error(msg + "orign msg:\n" + orignMessage.toString(), e);
		}else {
			logger.error(msg, e);
		}
	}

}
