package rose.mary.trace.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.core.data.cache.CacheSummary;
import rose.mary.trace.core.data.common.State;
import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.core.data.common.Unmatch;
import rose.mary.trace.core.monitor.TPS;
import rose.mary.trace.manager.CacheManager;
import rose.mary.trace.manager.MonitorManager;

@Service
public class CacheMonitorService {
	
	@Autowired
	CacheManager cacheManager;
	
	@Autowired
	MonitorManager monitorManager;
	
	public CacheSummary getCacheSummary() throws Exception{
		
		CacheSummary csum = new CacheSummary();
   	 
		TPS tps1 = monitorManager.getTps1();
		csum.setTps(tps1.getValue());
		
		List<CacheProxy<String, Trace>> dcs = cacheManager.getDistributeCaches();
		List<Integer> dcDepth = new ArrayList<Integer>();
		for(CacheProxy<String, Trace> dc : dcs) {
			int depth = dc.getCacheInfo().getCurrentNumberOfEntries();
			dcDepth.add(depth);
			
		} 
		csum.setDcDepth(dcDepth);
	
		 
		CacheProxy<String, Trace> mc = cacheManager.getMergeCache();
		csum.setMcDepth(mc.getCacheInfo().getCurrentNumberOfEntries()); 
		
	
		List<CacheProxy<String, State>> bcs = cacheManager.getBotCaches(); 
		List<Integer> bcDepth = new ArrayList<Integer>();
		for(CacheProxy<String, State> bc : bcs) {
			int depth = bc.getCacheInfo().getCurrentNumberOfEntries();
			bcDepth.add(depth);
		} 
		csum.setBcDepth(bcDepth);
	 
		CacheProxy<String, State> fc = cacheManager.getFinCache();
		csum.setFcDepth(fc.getCacheInfo().getCurrentNumberOfEntries()); 
		
		
		CacheProxy<String, Unmatch> uc = cacheManager.getUnmatchCache();
		csum.setUcDepth(uc.getCacheInfo().getCurrentNumberOfEntries()); 
		
		
		
		
		return csum;
	}
}
