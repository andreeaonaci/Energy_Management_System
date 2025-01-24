//package config;
//
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//public class MyWebSocketHandler extends TextWebSocketHandler {
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        // This method is called once the connection is established
//        System.out.println("Connection established: " + session.getId());
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // Handle incoming message
//        System.out.println("Received message: " + message.getPayload());
//    }
//}
