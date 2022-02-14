/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pep.per.mint.common.data.basic.ComMessage;
import pep.per.mint.common.util.Util;
import rose.mary.trace.data.Foo;
import rose.mary.trace.data.channel.ChannelOperation;
import rose.mary.trace.database.service.FooService;

/**
 * <pre>
 * rose.mary.trace.controller
 * FooController.java
 * </pre>
 * @author whoana
 * @date Aug 27, 2019
 */
@RestController
public class FooController {
	
	@Autowired
	FooService fooService;
	
	@RequestMapping(
			value = "/trace/services/foos", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<?, List<Foo>> getFooList(
			HttpSession httpSession,
			@RequestBody ComMessage<?, List<Foo>> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
				
		List<Foo> list = fooService.getFooList();
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		comMessage.setResponseObject(list);
		
		return comMessage;
		
	}
	
	@RequestMapping(
			value = "/trace/services/foos/insert", 
			params = "method=GET", 
			method = RequestMethod.POST, 
			headers = "content-type=application/json")
	public @ResponseBody ComMessage<?, List<Foo>> insertList(
			HttpSession httpSession,
			@RequestBody ComMessage<?, List<Foo>> comMessage,
			Locale locale, 
			HttpServletRequest request) throws Throwable{
		
		
		long elapsed = System.currentTimeMillis();
		for(int i = 1 ; i <= 10 ; i ++) {
			List<Foo> list = new ArrayList<Foo>();
			for(int j = 1 ; j <= 1000; j++) {
				list.add(new Foo(UUID.randomUUID().toString(),"data"));
			}
			fooService.insertBatch(list);
		}
		System.out.println("insertList elapsed : "+(System.currentTimeMillis() - elapsed));
		comMessage.setEndTime(Util.getFormatedDate("yyyyMMddHHmmssSSS")); 
		 
		
		return comMessage;
		
	}
	
}
