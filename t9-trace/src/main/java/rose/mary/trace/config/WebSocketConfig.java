/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.config;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import rose.mary.trace.manager.MonitorManager;

/**
 * <pre>
 * WebSocketConfig
 * 모니터 정보 PUSH 를 위한 웹소켓 설정  
 * MonitorManager start , stop 컨트롤이 @EventListener 내에서 (웹소켓 클라이언트 접속 및 종료 이벤트)호출된다. 
 * </pre>
 * @author whoana
 * @since 2020.03.23
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MonitorManager monitorManager;
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/console");
		
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/webconsole").withSockJS();
		
	}
	 
	private static int sessionCount = 0;
	boolean startMonitor = false;
	Object monitor = new Object();
	@EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		synchronized(monitor) {
			sessionCount ++;
			if(sessionCount > 0 && !startMonitor) {
				monitorManager.startMonitors();
				startMonitor = true;
			}
		}
        logger.info("Received a new web socket connection: session number:" + sessionCount);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    	synchronized(monitor) {
    		if(sessionCount > 0) sessionCount --;
    		if(sessionCount == 0) {
    			monitorManager.stopMonitors();
    			startMonitor = false;
    		}
    	}
    	logger.info("Closed a web socket connection: session number:" + sessionCount);
    }
    
    public static int currentSessionNumber() {
    	return sessionCount;
    }
	
}