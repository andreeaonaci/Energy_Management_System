package services;

//import dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import entity.Message;

import java.util.HashSet;
import java.util.Set;

@Service
public class WebSocketService extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Autowired
    private MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("New WebSocket connection established: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String messagePayload = message.getPayload();

        // Here we assume the message contains senderId, recipientId, and content
        // (These should come from the frontend)
        Message chatMessage = parseMessage(messagePayload);

        // Handle the message - Store it in the database using MessageService
        entity.Message newMessage = new entity.Message();
        newMessage.setContent(chatMessage.getContent());
        newMessage.setSender(chatMessage.getSenderId());
        newMessage.setReceiver(chatMessage.getReceiverId());
        messageService.saveMessage(newMessage);  // Save the message to the database

        // Broadcast the message to all connected clients
        broadcastMessage(chatMessage);

        // Respond back to the sender (if needed)
        try {
            session.sendMessage(new TextMessage("Message received: " + messagePayload));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
        System.out.println("WebSocket connection closed: " + session.getId());
    }

    public void broadcastMessage(Message chatMessage) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    // Send the message to each connected client (you can choose to filter by recipient if needed)
                    session.sendMessage(new TextMessage(chatMessage.getSenderId() + ": " + chatMessage.getContent()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Message parseMessage(String messagePayload) {
        String[] parts = messagePayload.split(":");
        Long senderId = Long.valueOf(parts[0]);
        Long recipientId = Long.valueOf(parts[1]);
        String content = parts[2];
        return new Message(senderId, recipientId, content);
    }
}

