/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.database.service;

import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import rose.mary.trace.core.data.common.InterfaceInfo;
import rose.mary.trace.database.mapper.m02.InterfaceMapper;

/**
 * <pre>
 * rose.mary.trace.database.service
 * InterfaceRetrieveService.java
 * </pre>
 * @author whoana
 * @date Sep 16, 2019
 */
@Service
public class InterfaceService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	InterfaceMapper interfaceRetrieveMapper;
	
	@Autowired
	//@Qualifier("sqlSessionFactory02")
	@Qualifier("sqlSessionFactory01")
	SqlSessionFactory sqlSessionFactory;
	
	public List<InterfaceInfo> retrieve() throws Exception{
		SqlSession session = sqlSessionFactory.openSession();
		List<InterfaceInfo> list = null;
		try {
			list = session.selectList("rose.mary.trace.database.mapper.m02.InterfaceMapper.retrieveList", null);
		}catch(Exception e) {
			throw e;
		}finally {
			if(session != null) session.close();
		}
		return list;
	}
	
	public InterfaceInfo retrieve(String integrationId) throws Exception{
		return interfaceRetrieveMapper.retrieve(integrationId);
	}
	 
	
}
