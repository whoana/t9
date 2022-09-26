package rose.mary.trace.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.data.common.StateEvent;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.system.SystemLogger;

public class RecoveryManager {
    
    Logger logger = LoggerFactory.getLogger(getClass());

    ConfigurationManager configurationManager;

    PolicyHandlerManager policyHandlerManager;

    CacheManager cacheManager;
    
    ServerManager serverManager;
    
    long recoveryTaskDelay = 5000;
 
    public RecoveryManager(
        ConfigurationManager configurationManager, 
        PolicyHandlerManager policyHandlerManager,
        CacheManager cacheManager, 
        ServerManager serverManager, 
        long recoveryTaskDelay) {
        this.configurationManager = configurationManager;
        this.policyHandlerManager = policyHandlerManager;
        this.cacheManager = cacheManager;
        this.serverManager = serverManager;
        this.recoveryTaskDelay = recoveryTaskDelay;
    }


    public void start() throws Exception{
        
        SystemLogger.info("RecoveryManager start...");

        serverManager.startBotLoader();
        serverManager.startLoader();
        serverManager.startBoter();
        
        while(true){
            Thread.sleep(recoveryTaskDelay);
            SystemLogger.info("check to recovery task to be done!");
            if(isNothingToDistribute(cacheManager.getDistributeCaches()) && isNothingToBot(cacheManager.getBotCaches())){
                SystemLogger.info("recovery task was done!");
                serverManager.stopLoader();
                serverManager.stopBoter();
                serverManager.stopBotLoader();
                break;
            }
        } 
        
        
        if (configurationManager.getServerManagerConfig().isStartOnBoot()) {
            SystemLogger.info("start server normally");
            serverManager.startServer();
        } else {
            SystemLogger.info("getServerManagerConfig().isStartOnBoot():false");
        }
        policyHandlerManager.start();
    }


    public boolean haveRecoverableItems(){
        return !isNothingToDistribute(cacheManager.getDistributeCaches()) || !isNothingToBot(cacheManager.getBotCaches());
    }


    private boolean isNothingToBot(List<CacheProxy<String, StateEvent>> botCaches) {
        for (CacheProxy<String,StateEvent> bc : botCaches) {
            if(bc.size() > 0) return false;
            else continue;
        }
        return true;
    }


    private boolean isNothingToDistribute(List<CacheProxy<String, Trace>> distributeCaches) {
        for (CacheProxy<String,Trace> dc : distributeCaches) {
            if(dc.size() > 0) return false;
            else continue;
        }
        return true;
    }
}
