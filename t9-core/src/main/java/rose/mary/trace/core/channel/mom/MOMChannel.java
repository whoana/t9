/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.core.channel.mom;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.channel.Channel;
import rose.mary.trace.core.config.OldStateCheckHandlerConfig;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.helper.module.mte.ILinkMsgHandler;
import rose.mary.trace.core.helper.module.mte.MQMsgHandler;
import rose.mary.trace.core.helper.module.mte.MsgHandler;
import rose.mary.trace.core.helper.checker.OldStateCheckHandler;

/**
 * <pre>
 * MQTraceListener
 * 큐로부터 트레이스 메시지를 읽어 들이는 채널
 * </pre>
 * 
 * @author whoana
 * @since Jul 29, 2019
 */
public class MOMChannel extends Channel {

	String hostName;
	String qmgrName;
	int port;
	String userId;
	String password;
	String channelName;
	String queueName;
	String module;
	int waitTime;
	int ccsid;
	int characterSet;
	boolean bindMode = true;

	MsgHandler mh = null;

	public MOMChannel(
			String name,
			String module,
			String qmgrName,
			String hostName,
			int port,
			String channelName,
			String queueName,
			int waitTime,
			String userId,
			String password,
			int ccsid,
			int characterSet,
			boolean bindMode,
			boolean autoCommit,
			int commitCount,
			long maxCommitWait,
			long delayForNoMessage,
			long delayOnException,
			int maxCacheSize,
			long delayForMaxCache,
			CacheProxy<String, Trace> cache,
			OldStateCheckHandlerConfig oschc) throws Exception {
		super(name, autoCommit, commitCount, maxCommitWait, delayForNoMessage, delayOnException, maxCacheSize,
				delayForMaxCache, cache);
		this.qmgrName = qmgrName;
		this.hostName = hostName;
		this.port = port;
		this.channelName = channelName;
		this.queueName = queueName;
		this.waitTime = waitTime;
		this.userId = userId;
		this.password = password;
		this.module = module;
		this.ccsid = ccsid;
		this.characterSet = characterSet;
		this.bindMode = bindMode;

		this.stateChecker = new OldStateCheckHandler(oschc);

	}

	@Override
	protected void initialize() throws Exception {

		if (MsgHandler.MODULE_MQ.equalsIgnoreCase(module)) {
			mh = new MQMsgHandler(qmgrName, hostName, port, channelName, userId, password, ccsid, characterSet,
					autoCommit, bindMode);
		} else if (MsgHandler.MODULE_ILINK.equalsIgnoreCase(module)) {
			mh = new ILinkMsgHandler(qmgrName, hostName, port, channelName);
		} else {
			throw new Exception("NotFounMode:" + module);
		}

		mh.open(queueName, MsgHandler.Q_QPEN_OPT_GET);
	}

	@Override
	public Object trace() throws Exception {

		return mh.get(waitTime);

	}

	// TransactionManager tm = null;
	@Override
	protected void commit() throws Exception {

		// tm = cache.getTransactionManager();
		// if(tm != null) tm.begin();
		cache.put(cacheMap);
		cacheMap.clear();
		// if(tm != null) tm.commit();
		mh.commit();
	}

	@Override
	protected void rollback() throws Exception {
		mh.rollback();
		cacheMap.clear();
		// if(tm != null) mh.rollback();
	}

	// @Override
	// protected void commit() throws Exception {
	//
	// mh.commit();
	//
	// }
	//
	// @Override
	// protected void rollback() throws Exception {
	//
	// //mh.rollback();
	// mh.commit();
	//
	// }

	@Override
	public boolean ping() {
		if (mh != null)
			return mh.ping();
		else
			return false;
	}

}
