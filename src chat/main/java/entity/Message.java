package entity;


import jakarta.persistence.*;

@Entity
@Table(name = "chat")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sender_id")
    private Long senderId;
    @Column(name = "receiver_id")
    private Long receiverId;
    @Column(name = "message")
    private String message;

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isRead() {
        return read;
    }

    private String currentPage;

    public void setRead(boolean read) {
        this.read = read;
    }

    @Column(name = "status")
    private boolean read;

    public Message(Long senderId, Long receiverId, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = content;
        this.read = false;
    }

    public Message() {

    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setContent(String content) {
        this.message = content;
    }

    public Long setSender(Long senderId) {
        return senderId;
    }

    public Long setReceiver(Long receiverId) {
        return receiverId;
    }

    public String getContent() {
        return message;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}

