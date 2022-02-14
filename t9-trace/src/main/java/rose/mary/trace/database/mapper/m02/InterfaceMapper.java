/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.database.mapper.m02;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import rose.mary.trace.data.common.InterfaceInfo;

/**
 * <pre>
 * rose.mary.trace.database.mapper.m02
 * InterfaceMapper.java
 * </pre>
 * @author whoana
 * @date Sep 16, 2019
 */
@Mapper
public interface InterfaceMapper {
	 
	public List<InterfaceInfo> retrieveList() throws Exception; 
	 
	public InterfaceInfo retrieve(@Param("integrationId") String integrationId) throws Exception; 
	
	
}
