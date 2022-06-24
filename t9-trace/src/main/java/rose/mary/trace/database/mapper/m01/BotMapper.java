/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.database.mapper.m01;

import org.apache.ibatis.annotations.Mapper;

import rose.mary.trace.core.data.common.Bot;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.Unmatch;

/**
 * <pre>
 * rose.mary.trace.database.mapper.m01
 * BOTMapper.java
 * </pre>
 * @author whoana
 * @date Sep 18, 2019
 */
@Mapper
public interface BotMapper {
	
	public int restore(Bot bot) throws Exception;
	
	public int updateUnmatch(Unmatch unmatch) throws Exception;

}
