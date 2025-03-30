package com.spring.config;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;



public class WebSocketBrokerConfiguration implements WebSocketMessageBrokerConfigurer{
	@Override
	public  void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/wsecho").withSockJS();
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app")
		.enableSimpleBroker("/topic" , "/queue");
	}
	
	
}
