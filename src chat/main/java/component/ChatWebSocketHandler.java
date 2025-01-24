package component;

import entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@EnableWebSocket
public class ChatWebSocketHandler extends TextWebSocketHandler implements WebSocketConfigurer {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final List<Message> allMessages = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId"); // Extract user ID from session attributes
        userSessions.put(userId, session);
        logger.info("User " + userId + " connected");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("Received message: " + message.getPayload());
        String payload = message.getPayload();
        String receiverId = extractReceiverId(message); // Extract the receiver's ID from the message

        // Create the Message object and store it
        Message newMessage = new Message(); // Assume you have a Message constructor
        newMessage.setContent(payload);
        newMessage.setSenderId(Long.valueOf(session.getAttributes().get("userId").toString()));
        newMessage.setReceiverId(Long.valueOf(receiverId));
        allMessages.add(newMessage); // Store the message

        // Broadcast the message to the specific receiver
        WebSocketSession receiverSession = userSessions.get(receiverId);
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(payload));
        } else {
            logger.info("Receiver not connected: " + receiverId);
        }
    }

    private String extractReceiverId(TextMessage message) {
        // Extract the receiver's ID from the message
        return message.getPayload().split(",")[0];
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        userSessions.values().remove(session);
        logger.info("User disconnected");
    }

    public void broadcastMessage(String messageContent) {
        logger.info("Broadcasting message: " + messageContent);
        for (WebSocketSession session : userSessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(messageContent));
                } catch (IOException e) {
                    logger.error("Failed to send message to user", e);
                }
            }
        }
    }

    public List<Message> getAllMessages() {
        return allMessages; // Return all messages stored in memory
    }

    public List<Message> getAllMessagesForUser(Long userId, Long otherUserId) {
        List<Message> messages = new ArrayList<>();
        for (Message message : allMessages) {
            if ((message.getSenderId().equals(userId) && message.getReceiverId().equals(otherUserId)) ||
                    (message.getSenderId().equals(otherUserId) && message.getReceiverId().equals(userId))) {
                messages.add(message);
            }
        }
        return messages;
    }

    public Map<String, WebSocketSession> getAllSessions() {
        return userSessions;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this, "/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    // Method to display all stored messages
    public void displayAllMessages() {
        logger.info("Displaying all stored messages:");
        for (Message message : allMessages) {
            logger.info("Message from " + message.getSenderId() + " to " + message.getReceiverId() + ": " + message.getContent());
        }
    }
}
