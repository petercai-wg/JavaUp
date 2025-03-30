package com.spring.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;




@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(echoHanlder(),"/wsecho");
		
	}

	@Bean
	public EchoHanlder echoHanlder(){
		return new EchoHanlder();
	}
	
	class EchoHanlder extends TextWebSocketHandler{
		private List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
		
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			for(WebSocketSession s : sessions){
				s.sendMessage(message);
			}
			
		}
		
		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			sessions.add(session);
		}
	}
	
}
