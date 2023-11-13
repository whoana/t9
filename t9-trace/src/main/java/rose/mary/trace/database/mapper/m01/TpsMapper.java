package rose.mary.trace.database.mapper.m01;
 
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; 

@Mapper
public interface TpsMapper {
 
	int getAvgTps(@Param("recordDate") String recordDate)throws Exception;
 
	 
	int recordAvgTps(@Param("recordDate") String recordDate, @Param("startDate") String startDate, @Param("endDate") String endDate)throws Exception;

}
