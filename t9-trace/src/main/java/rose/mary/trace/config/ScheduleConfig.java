package rose.mary.trace.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
 
import rose.mary.trace.data.cache.CacheSummary;
import rose.mary.trace.service.CacheMonitorService;

@Configuration
@EnableScheduling
//@Component
public class ScheduleConfig{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
    @Autowired
    private SimpMessagingTemplate smt;
     
    
    @Autowired
    private  CacheMonitorService cms;
    
    

    @Scheduled(fixedDelay=1000)
    public void pushCacheSummary() throws Exception{
    	if(WebSocketConfig.currentSessionNumber() == 0 ) {
    		//logger.info("I can't find login users to push cache summary.");
    		
    		return ;
    	}
    	logger.info("pushCacheSummary start");
    	
    	CacheSummary csum =  cms.getCacheSummary();
    	
    	smt.convertAndSend("/topic/cache-summary", csum);
    	//smt.setSendTimeout(sendTimeout);
    	//smt.setMessageConverter(messageConverter);
    	logger.info("pushCacheSummary end.");
    }
}

