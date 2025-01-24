import { Component, OnDestroy, OnInit  } from '@angular/core';
import { LoginComponent } from '../components/login/login.component';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
//import { WebsocketService } from '../services/web-socket.service'

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.scss'
})
export class NotificationComponent {
  message: string = '';
  notifications: string[] = [];

  constructor(private userService: UserService,
    private router: Router) {//, private webSocketService: WebsocketService
    this.message = 'Hello from NotificationComponent';
  }

  clearMessage() {
    this.message = '';
  }

  setMessage(message: string) {
    this.message = message;
  }

  getMessage() {
    return this.message;
  }

  // ngOnInit(): void {
  //   this.webSocketService.connect((message: string) => {
  //     this.notifications.push(message);
  //   });
  // }

  // ngOnDestroy(): void {
  //   this.webSocketService.disconnect();
  // }

  // constructor(private notificationService: NotificationService) {}
  
  // ngOnInit(): void {
  //   // Subscribe to notifications from the WebSocket service
  //   this.webSocketService.getNotifications().subscribe((notifications) => {
  //     this.notifications = notifications;
  //   });
  // }

  // login() {
  //   this.router.navigate(['/']);
  // }
}
