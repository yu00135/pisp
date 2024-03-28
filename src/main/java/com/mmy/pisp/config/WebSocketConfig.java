package com.mmy.pisp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.mmy.pisp.filter.WebSocketInterceptor;

import javax.annotation.Resource;

/**
 * @author : Mingyu Ma
 * @date : 2022/4/17 18:18
 */
@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    WebSocketHandler MyWebSocketHandler;

    @Resource
    WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(MyWebSocketHandler,"/ws")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }
}
