package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private SimpMessagingTemplate template;
//    public void sendNotification(String message) {
//        try {
//            template = new SimpMessagingTemplate(new MessageChannel() {
//                @Override
//                public boolean send(Message<?> message, long timeout) {
//                    return false;
//                }
//            });
//            logger.info("Sending notification to /topic/notifications: {}", message);
//            template.convertAndSend("/topic/notifications", message);
//        } catch (Exception e) {
//            logger.error("Error sending notification: {}", e.getMessage(), e);
//        }
//    }
public void sendNotification(String message) {
    try {
//        template = new SimpMessagingTemplate(new MessageChannel() {
//            @Override
//            public boolean send(Message<?> message, long timeout) {
//                return false;
//            }
//        });

        logger.info("Sending notification to /topic/notifications: {}", message);

        // Create a message object
//        Message<String> messageToSend = MessageBuilder.withPayload(message)
//                .setHeader("destination", "/topic/notifications")
//                .build();
//
//        // Send the message
//        template.send("/topic/notifications", messageToSend);
    } catch (Exception e) {
        logger.error("Error sending notification: {}", e.getMessage(), e);
    }
}
}
