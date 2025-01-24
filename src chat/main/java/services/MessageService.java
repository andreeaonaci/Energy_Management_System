package services;

import entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.MessageRepository;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getMessagesByRecipient(String recipientId) {
        return messageRepository.findByReceiverId(Long.valueOf(recipientId));
    }

    public List<Message> getMessagesBySender(String senderId) {
        return messageRepository.findBySenderId(Long.valueOf(senderId));
    }
    @Transactional
    public Message saveMessage(Message chatMessage) {
        // The following is already correct
        Message messageEntity = new Message();
        messageEntity.setSenderId(chatMessage.getSenderId());
        messageEntity.setReceiverId(chatMessage.getReceiverId());
        messageEntity.setContent(chatMessage.getContent());

        return messageRepository.save(messageEntity);  // Persisting the message
    }

    public List<Message> getMessagesForRecipient(Long recipientId) {
        return messageRepository.findByReceiverId(recipientId);
    }

    public List<Message> getMessagesFromSender(Long senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    public void markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new RuntimeException("Message not found"));
        message.setRead(true);
        messageRepository.save(message);
    }

    // get all messages for a specific user
    public List<Message> getAllMessagesForUser(Long userId, Long otherUserId) {
        return messageRepository.findBySenderIdOrReceiverId(userId, otherUserId);
    }
}
