/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.database.mapper.m02;
 
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; 
 

/**
 * <pre>
 * T9 system 운영 설정 모니터링 정보 관리를 위한 Mapper
 * </pre>
 * @author whoana
 * @since 20200429
 */
@Mapper
public interface SystemMapper {
	  
	/**
	 *  
	 * @return
	 * @throws Exception
	 */
	public int selectDatabaseHealthCheckDate() throws Exception;
	
	/**
	 * 
	 * @param checkDate
	 * @return
	 * @throws Exception
	 */
	public int updateDatabaseHealthCheckDate(@Param("checkDate") String checkDate) throws Exception;
	
	/**
	 * 
	 * @param checkDate
	 * @return
	 * @throws Exception
	 */
	public int insertDatabaseHealthCheckDate(@Param("checkDate") String checkDate) throws Exception;
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public int generateBadSqlGrammarException() throws Exception;
	
}
