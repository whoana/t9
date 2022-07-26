package rose.mary.trace.database.service;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import rose.mary.trace.database.mapper.m01.TpsMapper;

@Service
public class TpsService {
    
    Logger logger = LoggerFactory.getLogger(getClass());
    
    // @Autowired
    // @Qualifier("sqlSessionFactory01")
    // SqlSessionFactory sqlSessionFactory;
    
    @Autowired
    TpsMapper tpsMapper;
 
    public void recordAvgTps(String recordDate, String startDate, String endDate) throws Exception{
        tpsMapper.recordAvgTps(recordDate, startDate, endDate);
    }

    public int getAvgTps(String recordDate) throws Exception{
        return tpsMapper.getAvgTps(recordDate);
    }
}
