package com.enation.itbuilder.image2code.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author kingapex
 * @version 1.0
 * @data 2022/12/23 12:02
 * @since 7.3.0
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfig  implements WebSocketConfigurer {

    @Value("${image2code.api-address:}")
    private String apiAddress;

    @Value("${image2code.promptsLocation:classpath:/prompts/}")
    private String promptsLocation;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new Image2PageSocketHandler(apiAddress, promptsLocation), "/ws/image").setAllowedOrigins("*");
        registry.addHandler(new Text2PageSocketHandler(apiAddress), "/ws/text").setAllowedOrigins("*");

    }


}
