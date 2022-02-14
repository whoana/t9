/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.helper.module.ilink.api;

import com.mococo.ILinkAPI.jms.ILinkMessage;
import com.mococo.ILinkAPI.jms.ILinkRequestMessage;

import rose.mary.trace.monitor.ThroughputMonitor;

/**
 * <pre>
 * rose.mary.trace.ilink.api
 * ILinkMessageListener.java
 * </pre>
 * @author whoana
 * @date Aug 20, 2019
 */
public class ILinkMessageListener implements MessageListener{
	
	GetMessageApi gmapi = null; 
	
	ThroughputMonitor tpm;
	
	int commitCount = 2;

	int uncommittedCount = 0;
	
	public ILinkMessageListener(GetMessageApi gmapi, ThroughputMonitor tpm) {
		this.gmapi = gmapi;
		this.tpm = tpm;
	}

	@Override
	public void onMessage(Object msg) throws Exception {
		if (msg == null) return;

		System.out .println("receive msg [" + ((byte[]) msg).length + "][" + new String(((byte[]) msg)) + "]");
		ILinkMessage linkMessage = new ILinkMessage(0);
		linkMessage.setHeader((byte[]) msg);

		if (linkMessage.getMessageType() == 2000) {
			tpm.count();
			uncommittedCount++;
			if (uncommittedCount % commitCount == 0) {
				ILinkMessage messageBuffer = new ILinkMessage(2001);
				ILinkRequestMessage requestMessage = new ILinkRequestMessage(3008, "", "", "");
				messageBuffer.setInnerMessage(requestMessage);
				byte[] header = messageBuffer.getHeaderByteArray();
				byte[] body = requestMessage.toByteArray();

				gmapi.send(header);
				gmapi.send(body);
				byte[] newline = new byte[] { 10 };
				gmapi.send(newline);

				uncommittedCount = 0;

			}
		}

	}
	
	
}