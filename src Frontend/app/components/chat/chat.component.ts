import { Component, OnInit, OnDestroy, Input, OnChanges, SimpleChanges } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';
import { Message } from '../../components/message.model';
import { MessageService } from "../../services/message.service";
import { Client } from 'stompjs';
import { ClientComponent } from '../client/client.component';
import { UserService } from '../../services/user.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit, OnDestroy, OnChanges {
  userId = '';
  receiverId = '';
  messages: Message[] = [];
  newMessage = '';
  role = '';
  @Input() loggedInUserId = '';
  //@Input() selectedUserId = ''; // For individual chats
  @Input() userRole = ''; // 'ADMIN' or 'CLIENT'
  typingTimeout: any;
  typingMessage = '';
  typingIndicatorVisible = false;
  typingNotification: any = null;
  counter = 0;
  users: any[] = [];
  clientComponent!: ClientComponent;

  private selectedUserIdSubject = new BehaviorSubject<string>('');
  selectedUserId$: Observable<string> = this.selectedUserIdSubject.asObservable();

  @Input()
  set selectedUserId(value: string) {
    this.selectedUserIdSubject.next(value);
  }
  get selectedUserId(): string {
    return this.selectedUserIdSubject.value;
  }

  get isAdmin(): boolean {
    return this.userRole === 'ADMIN';
  }

  constructor(private webSocketService: WebSocketService, private messageService: MessageService,
    private userService: UserService
  ) {
    this.webSocketService.connect();
  }

  setSeenMessage(message: Message) {
    message.tempResponse = 'SEEN';
    if (this.webSocketService.stompClient && this.webSocketService.stompClient.connected) {
      this.webSocketService.stompClient.publish({
        destination: '/app/seen',
        body: JSON.stringify(message)
      });
    } else {
      console.error('WebSocket is not connected. Message not sent.');
    }
  }

  ngOnInit() {
    this.userRole = this.userService.getUserRole();
    console.log("User Role:", this.userRole);
    this.role = this.userRole === 'ADMIN' ? 'admin' : 'client';
    console.log("Logged-in User ID:", this.loggedInUserId);

    if (!this.selectedUserId && this.userRole === 'client') {
      console.warn('No admin ID passed to chat component for the client');
      return;
    }

    this.selectedUserId$.subscribe((newSelectedUserId) => {
      this.handleSelectedUserIdChange(newSelectedUserId);
    });

    this.webSocketService.getMessages().subscribe((messages: Message[]) => {
      this.getMessagesForHTML(messages);
    });

    this.webSocketService.getTypingNotifications().subscribe(
      (notification: any) => {
        console.log('Typing notification:', notification);
    
        if (notification.body.receiverId == this.loggedInUserId && notification.body.senderId == this.selectedUserId) {
          this.typingMessage = notification.body.message;
          this.typingIndicatorVisible = true;
          clearTimeout(this.typingTimeout);
          this.typingTimeout = setTimeout(() => {
            this.typingIndicatorVisible = false;
          }, 1500);
        }
      },
      (error) => {
        console.error('Error receiving typing notification:', error);
      }
    );
    
  }

  handleSelectedUserIdChange(newSelectedUserId: string) {
    this.messages.forEach((message) => {
      if (
        message.receiverId == newSelectedUserId &&
        message.senderId == this.loggedInUserId &&
        message.tempResponse != 'SEEN'
      )
      {
        console.log('Setting message seen:', message);
        this.setSeenMessage(message);
      }
  });
  }

  messagesHehe: Message[] = [];

  getMessagesForHTML(message: Message[]) {
    console.log('Received messages:', this.webSocketService.messages);
    console.log('Selected user ID:', this.selectedUserId.toString());
    console.log('Logged-in user ID:', this.loggedInUserId);
    console.log('Messsage sendId:', this.webSocketService.messages[0].senderId);
    console.log('Messsage receiverId:', this.webSocketService.messages[0].receiverId);
    this.messagesHehe = [];
    // copy in the messageHehe the value of messages
    this.messages.forEach((message) => {
      this.messagesHehe.push(message);
    });

    this.messages.push(...this.webSocketService.messages.filter((msg: any) => {
      return (
        ((msg.receiverId == this.selectedUserId && msg.senderId == this.loggedInUserId) ||
        (msg.senderId == this.selectedUserId && msg.receiverId == this.loggedInUserId)) &&
        this.messagesHehe.includes(msg) === false
      );
    }));
    console.log('Messages hehe:', this.messagesHehe);
    console.log('Messages after push:', this.messages);
    this.messages.forEach((message) => {
      console.log('Selected user ID:', this.selectedUserId);
      if (
        message.receiverId == this.loggedInUserId &&
        message.senderId == this.selectedUserId &&
        message.tempResponse != 'SEEN'
      ) {
        console.log('Setting message seen:', message);
        this.setSeenMessage(message);
      };
    });
  }

  onKeyUp(event: KeyboardEvent) {
    if (this.typingTimeout) 
    { 
    clearTimeout(this.typingTimeout); 
    
    } 
    
    this.webSocketService.sendTypingNotification(this.loggedInUserId, this.selectedUserId, this.counter.toString());
    this.typingTimeout = setTimeout(() => { this.typingIndicatorVisible = false; }, 2000);
  }

  sendBroadcastMessageButton() {
    this.userService.getAllUsers().subscribe((users) => {
      this.users = users;
    });
    console.log('Length:', this.users.length);
    this.users.forEach((user) => {
      console.log('User broadcastt:', user);
      if (user.role !== 'ADMIN') {
        console.log('User ala petrut:', user.id);
        this.sendBroadcastMessage(user.id.toString());
      }
    });
  }

  broadcastmessages: Message[] = [];

  public sendBroadcastMessage(receiverId: string) {
    if (this.newMessage.trim()) {
      const message: Message = {
        id: (++this.counter).toString(),
        message: this.newMessage,
        senderId: this.loggedInUserId,
        receiverId: receiverId,
        type: 'TEXT',
        tempResponse: '',
        currentPage: window.location.pathname,
        isBroadcast: true,
      };

      console.log('Sending broadcast message:', message);

      this.messageService.addMessage(message);
      if (receiverId === this.selectedUserId) {
        this.broadcastmessages.push(message);
        console.log('Broadcast messages:', this.broadcastmessages);
      }
    }
  }

  sendMessage() {
    if (this.newMessage.trim()) {
      const isAdmin = this.role === 'admin';
  
      const message: Message = {
        id: (++this.counter).toString(),
        message: this.newMessage,
        senderId: this.loggedInUserId,
        receiverId: this.selectedUserId, 
        type: 'TEXT',
        tempResponse: '',
        currentPage: window.location.pathname,
        isBroadcast: isAdmin ? true : false,
      };
  
      console.log('Sending message:', message);
  
      this.messageService.addMessage(message);
  
      this.newMessage = '';
    }
  }  

  ngOnChanges(changes: SimpleChanges) {
    
    if (changes['selectedUserId']) {
      this.messagesHehe = [];
      this.messages = this.webSocketService.messages.filter((msg: any) => {
        return (
          ((msg.receiverId == this.selectedUserId && msg.senderId == this.loggedInUserId) ||
          (msg.senderId == this.selectedUserId && msg.receiverId == this.loggedInUserId)) &&
          this.messages.includes(msg) === false
        );
      });
      this.messages.forEach((message) => {
        if (
          message.receiverId == this.loggedInUserId &&
          message.senderId == this.selectedUserId &&
          message.tempResponse != 'SEEN'
        ) {
          console.log('Setting message seen:', message);
          this.setSeenMessage(message);
        };
      });
    }
  }

  ngOnDestroy() {
    this.webSocketService.disconnect();
  }
}