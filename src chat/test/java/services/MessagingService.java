package services;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
public class MessagingService {

    private final Queue<String> messageQueue = new LinkedList<>();

    public void sendMessage(String message) {
        // Add the message to an in-memory queue
        synchronized (messageQueue) {
            messageQueue.add(message);
        }
    }

    public String getMessage() {
        // Retrieve and remove a message from the queue
        synchronized (messageQueue) {
            return messageQueue.poll();
        }
    }
}
