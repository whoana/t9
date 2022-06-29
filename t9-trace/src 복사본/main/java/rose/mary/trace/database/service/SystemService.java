package rose.mary.trace.database.service;

import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pep.per.mint.common.util.Util;
import rose.mary.trace.database.mapper.m02.SystemMapper;

@Service
public class SystemService {
	
	Logger logger = LoggerFactory.getLogger("rose.mary.trace.SystemLogger");
	
	@Autowired
	SystemMapper systemMapper;
	
	public boolean databaseHealthCheck(){
		boolean alive = false;
		try {
			if(systemMapper.selectDatabaseHealthCheckDate() > 0) {
				systemMapper.updateDatabaseHealthCheckDate(Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI));				
			}else {
				systemMapper.insertDatabaseHealthCheckDate(Util.getFormatedDate(Util.DEFAULT_DATE_FORMAT_MI));
			}
			alive = true;
		}catch(PersistenceException t) {
			alive = false;
			logger.debug("databaseHealthCheck fail:",t);
		}catch(Throwable t) {
			alive = false;
			logger.debug("databaseHealthCheck fail:",t);
		}
		return alive;
	}
	
	
	public int generateBadSqlGrammarException() throws Exception {
		return systemMapper.generateBadSqlGrammarException();
	}
	
}
