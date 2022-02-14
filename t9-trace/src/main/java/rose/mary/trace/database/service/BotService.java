/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.database.service;


import java.util.Collection; 

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pep.per.mint.common.util.Util;
import rose.mary.trace.apps.cache.CacheProxy;
import rose.mary.trace.apps.manager.CacheManager;
import rose.mary.trace.data.common.Bot;
import rose.mary.trace.data.common.InterfaceInfo;
import rose.mary.trace.data.common.State;
import rose.mary.trace.data.common.Unmatch;
import rose.mary.trace.database.mapper.m01.BotMapper; 

/**
 * <pre>
 * rose.mary.trace.database.service
 * BoterService.java
 * </pre>
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
		if(interfaceInfo == null) { 
			
			state.setMatch(State.MATCH_NO);					
			state.setRegDate(date);
			state.setModDate(date);
			
			Unmatch unmatch = cacheManager.getUnmatchCache().get(state.getIntegrationId());
			if(unmatch == null) {
				unmatch = new Unmatch();
				unmatch.setIntegrationId(state.getIntegrationId());
				unmatch.setMatch(State.MATCH_NO);
				unmatch.setRegDate(date);
			}else { 
				unmatch.setModDate(date);
			}
			cacheManager.getUnmatchCache().put(state.getIntegrationId(), unmatch);
		}else {
			state.setMatch(State.MATCH_YES);
			Bot bot = new Bot();
			bot.setState(state);
			bot.setInterfaceInfo(interfaceInfo);				
			bot.setRegDate(date);
			bot.setModDate(date); 
			int res = botMapper.restore(bot);				
		}				
	}
 
	
	public void mergeBots(Collection<State> states) throws Exception {
		
		boolean autoCommit = false;
		SqlSession session = null; 
		try {
			session = sqlSessionFactory.openSession(ExecutorType.BATCH, autoCommit); 
			String date = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
			for (State state : states) {
				 
				
				Bot bot = new Bot();
				InterfaceInfo interfaceInfo = cacheManager.getInterfaceCache().get(state.getIntegrationId()); 				
				if(interfaceInfo == null) {
					//매치되는 것이 없으면 TOP0503에는 저장하지 않는다.
					//interfaceInfo = new InterfaceInfo();								
					//interfaceInfo.setInterfaceId(state.getIntegrationId());
					//interfaceInfo.setIntegrationId(state.getIntegrationId());
					//interfaceInfo.setInterfaceNm(NOT_MATCH_NM);
					
					state.setMatch(State.MATCH_NO);					
					state.setRegDate(date);
					state.setModDate(date);
					
					Unmatch unmatch = cacheManager.getUnmatchCache().get(state.getIntegrationId());
					if(unmatch == null) {
						unmatch = new Unmatch();
						unmatch.setIntegrationId(state.getIntegrationId());
						unmatch.setMatch(State.MATCH_NO);
						unmatch.setRegDate(date);
					}else { 
						unmatch.setModDate(date);
					}
					cacheManager.getUnmatchCache().put(state.getIntegrationId(), unmatch);
					 
					
				}else {
					state.setMatch(State.MATCH_YES);
					bot.setState(state);
					bot.setInterfaceInfo(interfaceInfo);				
					bot.setRegDate(date);
					bot.setModDate(date);
					//logger.debug("add batchItem:" + Util.toJSONString(bot));
					int res = session.update("rose.mary.trace.database.mapper.m01.BotMapper.restore", bot);				
				}				
			} 
			session.flushStatements(); 
			session.commit();  
		}catch (Exception e) {
			session.rollback();
			throw e;
		}finally {
			if(session != null) session.close(); 
		}	
	}
	
	
	
	
	public void updateUnmatch(CacheProxy<String, Unmatch> unmatchCache) throws Exception {
		boolean autoCommit = false;
		SqlSession session = null; 
		try {
			session = sqlSessionFactory.openSession(ExecutorType.BATCH, autoCommit); 
			String date = Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI);
			Collection<Unmatch> unmatchs = unmatchCache.values();
			
			for (Unmatch unmatch : unmatchs) {
				
				InterfaceInfo interfaceInfo = cacheManager.getInterfaceCache().get(unmatch.getIntegrationId()); 				
				
				
				if(interfaceInfo == null ) {	
					unmatch.setMatch(State.MATCH_NO);
				}else {
					unmatch.setMatch(State.MATCH_YES);						
					unmatchCache.remove(unmatch.getIntegrationId());
				}
				unmatch.setRegDate(date);
				unmatch.setModDate(date);
				 
				int res = session.update("rose.mary.trace.database.mapper.m01.BotMapper.updateUnmatch", unmatch);				
						
			} 
			session.flushStatements(); 
			session.commit(); 
		}catch (Exception e) {
			session.rollback();
			throw e;
		}finally {
			if(session != null) session.close(); 
		}	
	}
	
	
}
