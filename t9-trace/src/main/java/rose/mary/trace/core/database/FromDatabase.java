package rose.mary.trace.core.database;

import rose.mary.trace.core.data.common.State;

abstract public class FromDatabase {

    abstract public State getState(String integrationId, String trackingDate, String orgHostId) throws Exception;
    abstract public State getState(String botId) throws Exception;

}
