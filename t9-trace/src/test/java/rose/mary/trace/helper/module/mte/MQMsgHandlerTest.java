/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.helper.module.mte;

import java.util.HashMap;
import java.util.Map;

import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.helper.module.mte.MQMsgHandler;
import rose.mary.trace.core.monitor.TPS;
import rose.mary.trace.core.monitor.ThroughputMonitor;
import rose.mary.trace.core.monitor.listener.MonitorListener;
import rose.mary.trace.core.monitor.listener.event.MonitorEvent;
import rose.mary.trace.core.parser.MQMessageParser;
import rose.mary.trace.core.parser.Parser;

/**
 * <pre>
 * rose.mary.trace.helper.module.mte
 * MQMsgHandlerTest.java
 * </pre>
 * 
 * @author whoana
 * @date Aug 14, 2019
 */
public class MQMsgHandlerTest {

	/**
	 * Test method for
	 * {@link rose.mary.trace.core.helper.module.mte.MQMsgHandler#get(int)}.
	 */
	// @Test
	public static void main(String args[]) {
		try {
			int testCount = 1;

			int waitTime = -1;
			String qmgrName = "QM1";
			String hostName = "127.0.0.1";
			int port = 1414;
			String channelName = "DEV.APP.SVRCONN";
			String queueName = "DEV.QUEUE.1";
			String userId = null;
			String password = null;
			int ccsid = 1208;
			int characterSet = 1208;
			boolean bindMode = false;
			boolean autoCommit = false;

			ThroughputMonitor tpm1 = new ThroughputMonitor(1000);
			tpm1.setMonitorListener(new MonitorListener<TPS>() {

				@Override
				public void watch(MonitorEvent<TPS> me) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("TPS1:" + me.getData().getValue());
				}

			});

			ThroughputMonitor tpm2 = new ThroughputMonitor(1000);
			tpm2.setMonitorListener(new MonitorListener<TPS>() {

				@Override
				public void watch(MonitorEvent<TPS> me) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("TPS2:" + me.getData().getValue());
				}

			});

			Map<String, Integer> nodeMap = new HashMap<String, Integer>();
			nodeMap.put("SNDR", 10);
			nodeMap.put("BRKR", 20);
			nodeMap.put("RCVR", 30);
			Parser parser = new MQMessageParser(nodeMap);

			for (int i = 0; i < testCount; i++) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						MQMsgHandler handler = null;
						try {

							handler = new MQMsgHandler(qmgrName, hostName, port, channelName, userId, password, ccsid,
									characterSet, autoCommit, bindMode);
							handler.open(queueName, MQMsgHandler.Q_QPEN_OPT_GET);

							System.out.println(Thread.currentThread().getName() + " start");

							int commitCount = 100;
							int uncommittedCount = 0;
							while (true) {
								Object msg = handler.get(waitTime);
								if (msg != null) {

									// tpm1.count();

									Trace trace = parser.parse(msg);

									tpm2.count();

									uncommittedCount++;

									if (uncommittedCount % commitCount == 0) {
										handler.commit();
									}

								} else {

								}
							}

						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							try {
								handler.rollback();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}).start();
			}

			// tpm1.start();
			tpm2.start();

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
