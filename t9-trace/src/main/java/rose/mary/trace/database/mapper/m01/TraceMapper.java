/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.database.mapper.m01;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import rose.mary.trace.core.data.common.Trace;

/**
 * <pre>
 * rose.mary.trace.database.mapper
 * TraceMapper.java
 * </pre>
 * @author whoana
 * @date Aug 28, 2019
 */
@Mapper
public interface TraceMapper {
  
	int insert(Trace trace) throws Exception;
	
	int exist(@Param("integrationId") String integrationId, @Param("trackingDate")String trackingDate,@Param("originHostId") String originHostId, @Param("processId")String processId) throws Exception;

	int upsert(Trace trace) throws Exception;

    List<Trace> getList(@Param("integrationId") String integrationId, @Param("trackingDate")String trackingDate,@Param("originHostId")String originHostId) throws Exception;
	
}
