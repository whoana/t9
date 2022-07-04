/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.controller;
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pep.per.mint.common.data.basic.ComMessage;
import pep.per.mint.common.util.Util;
import rose.mary.trace.core.cache.CacheInfo;
import rose.mary.trace.core.cache.CacheProxy;
import rose.mary.trace.manager.CacheManager;

/**
 * <pre>
 * rose.mary.trace.controller
 * CacheController.java
 * </pre>
 * @author whoana
 * @date Sep 3, 2019
 */
@RestController
public class CacheController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	CacheManager cacheManager;
	
	@RequestMapping(
			value = "/trace/managers/caches", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<?, Map> getCacheInfo(
			HttpSession httpSession,
			@RequestBody ComMessage<?, Map> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		try {
		    Map<String, Object> res = new LinkedHashMap<String, Object>();
		    
//		    List<CacheProxy<String, Trace>> caches = cacheManager.getDistributeCaches();
//		    for (CacheProxy<String, Trace> cache : caches) {
//		    	CacheInfo info = cache.getCacheInfo();
//		    	if(info!=null) res.put(cache.getName(), info);
//		    	 
//			}
//		    
//		    {
//		    	CacheInfo info = cacheManager.getMergeCache().getCacheInfo();
//		    	if(info!=null) res.put("mergeCache", info);
//		    }
//		    
//		    List<CacheProxy<String, BotEvent>> botEventCaches = cacheManager.getBotEventCaches();
//		    for (CacheProxy<String, BotEvent> cache : botEventCaches) {
//		    	CacheInfo info = cache.getCacheInfo();
//		    	if(info!=null) res.put(cache.getName(), info); 
//			}
//		    
//		    { 
//		    	CacheInfo info = cacheManager.getBoterCache().getCacheInfo();
//		    	if(info!=null) res.put("botCache", info);
//		    }
		    
		    Collection<CacheProxy> caches = cacheManager.caches();
		    
		    for(CacheProxy cache : caches) {
		    	CacheInfo info = cache.getCacheInfo();
		    	if(info!=null) res.put(cache.getName(), info);
		    	 
		    }
		    
			comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
			
			comMessage.setResponseObject(res);
			
			return comMessage;
		}catch(Throwable t) {
			logger.error("",t);
			throw t;
		}
		
	}
	
	
	@RequestMapping(
			value = "/trace/managers/caches/{cacheName}", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<?, Object> getCacheData(
			HttpSession httpSession,
			@PathVariable("cacheName") String cacheName,
			@RequestBody ComMessage<?, Object> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		
	    List list = new ArrayList();
	  
	    CacheProxy cache = cacheManager.getCache(cacheName);
	    Iterator iterator = cache.iterator();
	    
	    if(iterator != null && cache.size() >= 0) {
		    iterator.forEachRemaining(new Consumer() {
	
				@Override
				public void accept(Object t) {
					iterator.remove();
					logger.info(Util.toJSONString(t));
					list.add(t);
				}
		    	
		    });
	    }	    
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS"));
		
		comMessage.setResponseObject(list);
		
		return comMessage;
		
	}
	
	
	 
	
}
