package com.wulb2018.order.configure;

import com.wulb2018.order.handler.ExWsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author wulubin
 * @date 2026/1/25
 * @description TODO
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(testHandler(), "/ws/order_book")
                .setAllowedOrigins("*");
    }


    @Bean
    public WebSocketHandler testHandler() {
        return new ExWsHandler();
    }

//    @Component
//    public class OrderPushHandler extends TextWebSocketHandler {
//
//        @Override
//        protected void handleTextMessage(WebSocketSession session, TextMessage message) {
//            session.sendMessage(new TextMessage("hello"));
//        }
//    }
}
