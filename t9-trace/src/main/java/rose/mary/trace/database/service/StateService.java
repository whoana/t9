package rose.mary.trace.database.service;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.database.FromDatabase; 

@Service
public class StateService extends FromDatabase{
    
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    BotService botService;

    public State getState(String integrationId,String trackingDate, String orgHostId) throws Exception {
        State state = botService.getState(integrationId, trackingDate,  orgHostId);         
        return state;
    }

    @Override
    public State getState(String botId) throws Exception {
        StringTokenizer st = new StringTokenizer(botId,"@");
        String integrationId = st.nextToken();
        String trackingDate = st.nextToken();
        String orgHostId = st.nextToken();
        return getState(integrationId, trackingDate, orgHostId);
    }
}
