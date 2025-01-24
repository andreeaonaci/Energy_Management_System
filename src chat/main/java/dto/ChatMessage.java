//package dto;
//
//import java.time.LocalDateTime;
//
//public class ChatMessage {
//    private Long senderId;
//    private Long recipientId;
//    private String content;
//    private MessageType type; // TEXT, READ, TYPING
//    private LocalDateTime timestamp;
//
//    // Default constructor for Jackson
//    public ChatMessage() {
//    }
//
//    public ChatMessage(Long senderId, Long recipientId, String content, MessageType type) {
//        this.senderId = senderId;
//        this.recipientId = recipientId;
//        this.content = content;
//        this.type = type;
//    }
//
//    public Long getSenderId() {
//        return senderId;
//    }
//
//    public void setSenderId(Long senderId) {
//        this.senderId = senderId;
//    }
//
//    public Long getRecipientId() {
//        return recipientId;
//    }
//
//    public void setRecipientId(Long recipientId) {
//        this.recipientId = recipientId;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public MessageType getType() {
//        return type;
//    }
//
//    public void setType(MessageType type) {
//        this.type = type;
//    }
//}
