package com.claksion.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker// Stomp use
@Configuration
@EnableWebSocket // websocket server use
@RequiredArgsConstructor
public class StomWebSocketConfig implements WebSocketMessageBrokerConfigurer{

//    인바운드 : 들어오는 루트를 등록하는것
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/wss").setAllowedOriginPatterns("http://172.16.21.147").withSockJS();
    }

    /* 어플리케이션 내부에서 사용할 path를 지정할 수 있음 */
//     내보내는 방법, 접속되어 있는 클라이언트 들에게 데이터를 보내는 것,
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/send","/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }
//    @Override // (6)
//    public void configureClientInboundChannel (ChannelRegistration registration){
//        registration.interceptors(stompHandler);
//    }
}