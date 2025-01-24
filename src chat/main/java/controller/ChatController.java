package controller;

import component.ChatWebSocketHandler;
import entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import services.MessageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
//@RequestMapping("/chat")
@EnableWebSocketMessageBroker
//@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final ConcurrentHashMap<String, Queue<Message>> userMessages = new ConcurrentHashMap<>();
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(MessageService messageService, SimpMessagingTemplate messagingTemplate, ChatWebSocketHandler chatWebSocketHandler) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    private final ChatWebSocketHandler chatWebSocketHandler;

    /**
     * Handles sending messages via WebSocket.
     */
//    @PostMapping("/send")
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ResponseEntity<Message> sendWebSocketMessage(@Payload Message chatMessage) {
        System.out.println("sunt aici");
        messagingTemplate.convertAndSend("/topic/messages", chatMessage);
        return ResponseEntity.ok(chatMessage);
    }

    @MessageMapping("/reload")
    @SendTo("/topic/reload")
    public Map<String, Boolean> broadcastReload() {
        return Map.of("reload", true);
    }

    @PostMapping("/read/{messageId}")
    public String markMessageAsRead(@PathVariable Long messageId) {
        System.out.println("Marking message as read: " + messageId);
        messageService.markMessageAsRead(messageId);
        return "Message marked as read.";
    }

    /**
     * Retrieves messages for a specific recipient.
     */
    @GetMapping("/receive/{recipientId}")
    public List<Message> receiveMessages(@PathVariable Long recipientId) {
        System.out.println("Fetching messages for recipient: " + recipientId);
        return messageService.getMessagesForRecipient(recipientId);
    }

    /**
     * Sends a typing notification via WebSocket.
     * When the sender starts typing, the receiver will be notified.
     */
    @MessageMapping("/typing")
    @SendTo("/topic/typings")
    public ResponseEntity<Message> notifyTyping(@Payload Message typingNotification) {
        //String receiverId = String.valueOf(typingNotification.getReceiverId());
        System.out.println("Typing notification: " + typingNotification);
        messagingTemplate.convertAndSend("/topic/typings/", typingNotification);
        return ResponseEntity.ok(typingNotification);
    }

    @GetMapping("/chat/all")
    public ResponseEntity<List<Message>> getMessagesForUser(
            @RequestParam("senderId") String senderId,
            @RequestParam("receiverId") String receiverId) {

        System.out.println("Fetching messages between sender " + senderId + " and receiver " + receiverId);
        List<Message> allMessages = chatWebSocketHandler.getAllMessagesForUser(Long.valueOf(senderId), Long.valueOf(receiverId));
        return ResponseEntity.ok(allMessages);
    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<Message>> getAllMessages(@RequestParam String userId) {
//        List<Message> allMessages = chatWebSocketHandler.getAllMessages();
//        return ResponseEntity.ok(allMessages);
//    }

    @MessageMapping("/seen")
    @SendTo("/topic/readseen")
    public ResponseEntity<Message> notifySeen(@Payload Message seenNotification) {
        System.out.println("Received seen notification: " + seenNotification);
        return ResponseEntity.ok(seenNotification);
    }
}
