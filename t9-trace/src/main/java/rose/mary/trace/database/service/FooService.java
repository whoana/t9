/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.database.service;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import rose.mary.trace.data.Foo;
import rose.mary.trace.database.mapper.m01.FooMapper;

/**
 * <pre>
 * rose.mary.trace.database.service
 * FooService.java
 * </pre>
 * @author whoana
 * @date Aug 27, 2019
 */
@Service
public class FooService {

	@Autowired
	FooMapper fooMapper;
	
	@Autowired
	@Qualifier("sqlSessionFactory01")
	SqlSessionFactory sqlSessionFactory;
	
	
	
	public List<Foo> getFooList() throws Exception {
		return fooMapper.findList();
	}
	
	public void insertBatch(List<Foo> list)throws Exception {
		boolean autoCommit = false;
		SqlSession session = null;
		long elapsed = System.currentTimeMillis();
		try {
			session = sqlSessionFactory.openSession(ExecutorType.BATCH, autoCommit);
			//elapsed = System.currentTimeMillis();
			//System.out.println("open db:" + (System.currentTimeMillis() - elapsed));
			for (Foo foo : list) {
				int res = session.insert("rose.mary.trace.database.mapper.m01.FooMapper.insert", foo);				
			}
			//System.out.println("insert:" + (System.currentTimeMillis() - elapsed));
			//elapsed = System.currentTimeMillis();
			session.flushStatements();
			//System.out.println("flushStatements:" + (System.currentTimeMillis() - elapsed));
			//elapsed = System.currentTimeMillis();
			session.commit();
			//System.out.println("commit:" + (System.currentTimeMillis() - elapsed));
			
		}catch (Exception e) {
			session.rollback();
			throw e;
		}finally {
			if(session != null) session.close(); 
		}
		
	}
	
	public void insertNonBatch(List<Foo> list)throws Exception {
	 
			 
			for (Foo foo : list) {
				int res = fooMapper.insert(foo);				
			}
			 
		
	}
	
	
}
