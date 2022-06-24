package rose.mary.trace.database.mapper.m01;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import rose.mary.trace.core.data.common.TLog;

@Mapper
public interface RetrieveTLogMapper {
    
    List<TLog> retrieve(Map params) throws Exception;

    int getTotalCount(Map params) throws Exception;
    
}
