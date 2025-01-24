package controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/notifications")
//@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {
    @MessageMapping("/send")
    @SendTo("/topic/notifications")
    public String sendNotification(String message) {
        return message;
    }
}