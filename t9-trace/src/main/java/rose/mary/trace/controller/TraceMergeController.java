package rose.mary.trace.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import rose.mary.trace.core.data.common.Trace;
import rose.mary.trace.service.TraceMergeService;

@Controller
public class TraceMergeController {
    
    Logger logger = LoggerFactory.getLogger(getClass());	

    @Autowired
    TraceMergeService traceMergeService;
    
    
    @RequestMapping(
			value = "/traces/mergers",  
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody Map<String, Object> trace(
			HttpSession httpSession,
			@RequestBody Map<String, Trace> traces,
			Locale locale, 
			HttpServletRequest request) throws Throwable{

        Map<String, Object> res = new HashMap<String, Object>();
        try{
            traceMergeService.merge(traces);
            res.put("msg", "ok");
			res.put("cd", 0);
        }catch(Exception e){
            res.put("msg", "fail");
			res.put("cd", 9);
			res.put("exception", e.getMessage());
            logger.error("", e);
        }

        return res;
                
    }

}
