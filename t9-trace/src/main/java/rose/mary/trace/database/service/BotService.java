/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.database.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.data.common.Bot;
import rose.mary.trace.core.data.common.InterfaceInfo;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.Unmatch;
import rose.mary.trace.database.mapper.m01.BotMapper;
import rose.mary.trace.manager.CacheManager;

/**
 * <pre>
 * rose.mary.trace.database.service
 * BoterService.java
 * </pre>
 * 
 * @author whoana
 * @date Sep 19, 2019
 */
@Service
public class BotService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("sqlSessionFactory01")
	SqlSessionFactory sqlSessionFactory;

	@Autowired
	CacheManager cacheManager;

	@Autowired
	BotMapper botMapper;

	final static String NOT_MATCH_NM = "unregistered";

	public void mergeBot(State state, String date) throws Exception {

		InterfaceInfo interfaceInfo = cacheManager.getInterfaceCache().get(state.getIntegrationId());
		if (interfaceInfo == null) {

			state.setMatch(State.MATCH_NO);
			state.setRegDate(date);
			state.setModDate(date);

			Unmatch unmatch = cacheManager.getUnmatchCache().get(state.getIntegrationId());
			if (unmatch == null) {
				unmatch = new Unmatch();
				unmatch.setIntegrationId(state.getIntegrationId());
				unmatch.setMatch(State.MATCH_NO);
				unmatch.setRegDate(date);
			} else {
				unmatch.setModDate(date);
			}
			cacheManager.getUnmatchCache().put(state.getIntegrationId(), unmatch);
		} else {
			state.setMatch(State.MATCH_YES);
			Bot bot = new Bot();
			bot.setState(state);
			bot.setInterfaceInfo(interfaceInfo);
			bot.setRegDate(date);
			bot.setModDate(date);
			int res = botMapper.restore(bot);
		}
	}

	/**
	 * <pre>
	 * 등록되지 않은 인터페이스는  써머리 테이블[TOP0503]에 집계하지 않는다.
	 * 대신 미매핑 테이블[TOP0504] 미매핑 정보를 등록 또는 없데이트 한다.
	 * 향후 미매핑 로그도 써머리하려면 옵션 처리 할 수 있도록 수정해주자.
	 * </pre>
	 * 
	 * @param states
	 * @throws Exception
	 */
	public void mergeBots(Collection<State> states, CacheProxy<String, State> finCache) throws Exception {

		boolean autoCommit = false;
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession(ExecutorType.BATCH, autoCommit);
			// 20220825
			// 처리 속도 향상을 위해 한번만 실행하는 것으로 함 정확한 로킹을 위해서는 for 안으로 이동 필요함.
			String date = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
			Map<String, State> updateStates = new HashMap<String, State>();
			for (State state : states) {
				// String date = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
				Bot bot = new Bot();
				InterfaceInfo interfaceInfo = cacheManager.getInterfaceCache().get(state.getIntegrationId());
				if (interfaceInfo == null) {
					// 매치되는 것이 없으면 TOP0503에는 저장하지 않는다.
					// 대신 UnmatchCache 에 integrationId 를 키값으로 Unmatch 오브젝트를 등록해 둔다.

					// 저장 하려면 아래 로직으로 임시인터페이스 정보를 만들어 등록하도록 처리할 수 있다.(보류)
					// interfaceInfo = new InterfaceInfo();
					// interfaceInfo.setInterfaceId(state.getIntegrationId());
					// interfaceInfo.setIntegrationId(state.getIntegrationId());
					// interfaceInfo.setInterfaceNm(NOT_MATCH_NM);

					state.setMatch(State.MATCH_NO);
					state.setRegDate(date);
					state.setModDate(date);

					Unmatch unmatch = cacheManager.getUnmatchCache().get(state.getIntegrationId());
					if (unmatch == null) {
						unmatch = new Unmatch();
						unmatch.setIntegrationId(state.getIntegrationId());
						unmatch.setMatch(State.MATCH_NO);
						unmatch.setRegDate(date);
					} else {
						unmatch.setModDate(date);
					}
					cacheManager.getUnmatchCache().put(state.getIntegrationId(), unmatch);

				} else {
					state.setMatch(State.MATCH_YES);
					bot.setState(state);
					bot.setInterfaceInfo(interfaceInfo);
					bot.setRegDate(date);
					bot.setModDate(date);
					// logger.debug("add batchItem:" + Util.toJSONString(bot));
					int res = session.update("rose.mary.trace.database.mapper.m01.BotMapper.restore", bot);
				}
				state.setLoaded(true);
				updateStates.put(state.getBotId(), state);
			}
			session.flushStatements();
			session.commit();
			finCache.put(updateStates);
			
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	/**
	 * <pre>
	 * 	인터페이스 리스트에 존재하지 않는 건들을 재처리
	 * </pre>
	 * 
	 * @param unmatchCache
	 * @throws Exception
	 */
	public void updateUnmatch(CacheProxy<String, Unmatch> unmatchCache) throws Exception {
		boolean autoCommit = false;
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession(ExecutorType.BATCH, autoCommit);
			String date = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
			Collection<Unmatch> unmatchs = unmatchCache.values();

			for (Unmatch unmatch : unmatchs) {

				InterfaceInfo interfaceInfo = cacheManager.getInterfaceCache().get(unmatch.getIntegrationId());

				if (interfaceInfo == null) {
					unmatch.setMatch(State.MATCH_NO);
				} else {
					unmatch.setMatch(State.MATCH_YES);
					unmatchCache.remove(unmatch.getIntegrationId());
				}
				unmatch.setRegDate(date);
				unmatch.setModDate(date);

				int res = session.update("rose.mary.trace.database.mapper.m01.BotMapper.updateUnmatch", unmatch);

			}
			session.flushStatements();
			session.commit();
		} catch (Exception e) {
			session.rollback();
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

	public State getState(String integrationId, String trackingDate, String orgHostId) throws Exception {
		State state = botMapper.getState(integrationId, trackingDate, orgHostId);
		if (state != null) {
			int finishNodeCount = state.getFinishNodeCount();
			int todoNodeCount = state.getTodoNodeCount();
			boolean senderReceived = state.isFinishSender();
			state.setBotId(Util.join(integrationId, "@", trackingDate, "@", orgHostId));
			state.setFinish(finishNodeCount >= todoNodeCount && senderReceived);
			state.setCreateDate(System.currentTimeMillis());
		}
		return state;
	}

}
