/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.channel.rest;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.channel.Channel; 
import rose.mary.trace.core.data.common.Trace; 
import rose.mary.trace.core.util.IntCounter;

/**
 * <pre>
 * RestChannel
 * RESTful Service provider로 부터 트레이스 메시지를 읽어 들이는 채널
 * </pre>
 * 
 * @author whoana
 *
 */

public class RestChannel extends Channel {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	List<CacheProxy<String, Trace>> caches;
	IntCounter counter;

	ThreadPoolTaskExecutor taskExecutor;

	public RestChannel(String name, int maxCacheSize,
			long delayForMaxCache, List<CacheProxy<String, Trace>> caches, ThreadPoolTaskExecutor taskExecutor) {
		super(name, false, 0, 0, 0, 0, maxCacheSize, delayForMaxCache, null);
		this.caches = caches;
		this.stateCheckerId = "rose.mary.trace.core.helper.checker.OldStateCheckHandler";
		this.counter = new IntCounter(0, caches.size() - 1, 1);
		this.taskExecutor = taskExecutor;
	}

	@Override
	public void start() throws Exception {
		isShutdown = false;
		logger.info(
				Util.join("start channel:", this.getClass().getName(), "[" + Thread.currentThread().getName() + "]"));
	}

	@Override
	public void stop() {
		isShutdown = true;
		logger.info(
				Util.join("stop channel:", this.getClass().getName(), "[" + Thread.currentThread().getName() + "]"));
	}

	public class Consummer implements Runnable {
		List<CacheProxy<String, Trace>> caches;
		Object msg;

		public Consummer(List<CacheProxy<String, Trace>> caches, Object msg) {
			this.caches = caches;
			this.msg = msg;
		}

		@Override
		public void run() {
			try {
				Trace trace = parser.parse(msg);
				trace.setStateCheckHandlerId(stateCheckerId);
				int index = counter.getAndIncrease();
				logger.debug("********************************** cache index:" + index);
				CacheProxy<String, Trace> cache = caches.get(index);
				cache.put(trace.getId(), trace);
			} catch (Exception me) {
				// 메시지 파싱시 예외난 것들은 롤백 처리하지 않고 로그만 남기도록 한다.
				// handler에서는 메시지 원본을 어떻게 처리할지 고민해 본다.
				if (channelExceptionHandler != null) {
					channelExceptionHandler.handleParserException("parser exception:", me, msg);
				} else {
					msg.toString();
					logger.error("parser exception:", me);
				}
			} finally {
				if (tpm != null)
					tpm.count();
			}
		}

	}

	public void consumeAsync(List<Map<String, Object>> traces) throws Exception {
		if (isShutdown)
			throw new Exception("RestChannel(" + this.hashCode() + ") channel is not open yet.");
		if (traces != null && traces.size() > 0) {

			for (Map<String, Object> trace : traces) {
				taskExecutor.execute(new Consummer(caches, trace));
			}
		}
	}

	public void consume(Object msg) throws Exception {

		if (isShutdown)
			throw new Exception("RestChannel(" + this.hashCode() + ") channel is not open yet.");

		if (msg != null) {
			try {
				Trace trace = parser.parse(msg);
				trace.setStateCheckHandlerId(stateCheckerId);
				int index = counter.getAndIncrease();
				logger.debug("********************************** cache index:" + index);
				CacheProxy<String, Trace> cache = caches.get(index);
				cache.put(trace.getId(), trace);
			} catch (Exception me) {
				// 메시지 파싱시 예외난 것들은 롤백 처리하지 않고 로그만 남기도록 한다.
				// handler에서는 메시지 원본을 어떻게 처리할지 고민해 본다.
				if (channelExceptionHandler != null) {
					channelExceptionHandler.handleParserException("parser exception:", me, msg);
				} else {
					msg.toString();
					logger.error("parser exception:", me);
				}
			} finally {
				if (tpm != null)
					tpm.count();
			}
		}

	}

	@Override
	protected Object trace() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initialize() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void commit() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void rollback() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean ping() {
		// TODO Auto-generated method stub
		return true;
	}

}
