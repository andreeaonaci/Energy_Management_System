package repository;

import entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiverId(Long receiverId);

    List<Message> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
    //List<Message> findByRecipientId(Long recipientId);
    List<Message> findBySenderId(Long senderId);
}

