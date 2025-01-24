//package config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketHandlerConfig implements WebSocketConfigurer {
//
//    private final ChatWebSocketHandler chatWebSocketHandler;
//
//    public WebSocketHandlerConfig(ChatWebSocketHandler chatWebSocketHandler) {
//        this.chatWebSocketHandler = chatWebSocketHandler;
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(chatWebSocketHandler, "/ws", "/chat")
//                .setAllowedOrigins("*"); // Allow connections from all origins
//    }
//}
