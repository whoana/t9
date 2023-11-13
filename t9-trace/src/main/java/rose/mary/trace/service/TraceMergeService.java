package rose.mary.trace.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.manager.CacheManager;

@Service
public class TraceMergeService {
    
    @Autowired 
    CacheManager cacheManager;

    public void merge(Map<String, Trace> traces) throws Exception{
        cacheManager.getMergeCache().put(traces);
    }

}
