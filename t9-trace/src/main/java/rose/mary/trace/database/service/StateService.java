package rose.mary.trace.database.service;

import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pep.per.mint.common.util.Util;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.database.FromDatabase;
import rose.mary.trace.core.helper.checker.StateCheckerMap;
import rose.mary.trace.database.mapper.m01.BotMapper;
import rose.mary.trace.database.mapper.m01.TraceMapper; 

/**
 * 
 */
@Service
public class StateService extends FromDatabase{
    
    Logger logger = LoggerFactory.getLogger(getClass());
 
    @Autowired
	TraceMapper traceMapper;

    @Autowired
    BotMapper botMapper;

    public State getState(String integrationId,String trackingDate, String orgHostId) throws Exception {
        State state = botMapper.getState(integrationId, trackingDate, orgHostId);
		if (state != null) {
			int finishNodeCount = state.getFinishNodeCount();
			int todoNodeCount = state.getTodoNodeCount();
			boolean senderReceived = state.isFinishSender();
			state.setBotId(Util.join(integrationId, "@", trackingDate, "@", orgHostId));
			state.setFinish(finishNodeCount >= todoNodeCount && senderReceived);
			state.setCreateDate(System.currentTimeMillis());
		} else { 
            //TOP0503에 존재하지 않으면 TOP0501에서 리스트를 조회 하여 최종상태를 만들어 리턴한다. 
            List<Trace> list = traceMapper.getList(integrationId, trackingDate, orgHostId);
            
            String botId = Util.join(integrationId, "@", trackingDate, "@", orgHostId);
            long currentDate = System.currentTimeMillis();
            state = new State();
            state.setCreateDate(currentDate);
            state.setBotId(botId);

            String stateCheckHandlerId = "rose.mary.trace.core.helper.checker.OldStateCheckHandler";//외부설정처리 예정 
            int tryCount = 0;
            for (Trace trace : list) {
                StateCheckerMap.map.get(stateCheckHandlerId).checkAndSet(tryCount == 0, trace, state);
                tryCount ++;
            }
        }       
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
