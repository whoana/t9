package rose.mary.trace.database.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rose.mary.trace.data.common.TLog;
import rose.mary.trace.database.mapper.m01.RetrieveTLogMapper;


@Service
public class RetrieveTLogService {
    
    Logger logger = LoggerFactory.getLogger(getClass());     
    
    @Autowired
    RetrieveTLogMapper retrieveTLogMapper;

    public List<TLog> retrieve(Map params) throws Exception{
        return retrieveTLogMapper.retrieve(params);
    }

    public int getTotalCount(Map params) throws Exception{
        return retrieveTLogMapper.getTotalCount(params);
    }
}
