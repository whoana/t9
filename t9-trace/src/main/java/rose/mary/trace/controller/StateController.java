package rose.mary.trace.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import rose.mary.trace.core.data.common.State;
import rose.mary.trace.database.service.StateService;

import pep.per.mint.common.data.basic.ComMessage;
import pep.per.mint.common.util.Util;
@RestController
public class StateController {
    
    @Autowired
    StateService stateService;

    @RequestMapping(
			value = "/trace/services/states", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<Map<String, String>, State> getFooList(
			HttpSession httpSession,
			@RequestBody ComMessage<Map<String, String>, State> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
        Map<String, String> params = comMessage.getRequestObject();
        String integrationId = params.get("integrationId");
        String trackingDate = params.get("trackingDate");
        String orgHostId = params.get("orgHostId");
        
		State state = stateService.getState(integrationId, trackingDate, orgHostId);
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setResponseObject(state);
		
		return comMessage;
		
	}

}
