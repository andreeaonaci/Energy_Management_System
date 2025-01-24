  import { Injectable } from '@angular/core';
  import { HttpClient } from '@angular/common/http';
  import { WebSocketSubject, webSocket } from 'rxjs/webSocket';
  import { Observable, BehaviorSubject, Subject } from 'rxjs';
  import { Message } from '../components/message.model'; // Adjust the import path as needed
  import { Stomp, Client } from '@stomp/stompjs';
  import SockJS from 'sockjs-client';
  import { ChatComponent } from '../components/chat/chat.component';

  @Injectable({
    providedIn: 'root',
  })

  export class WebSocketService {
    public stompClient!: Client;
    private messagesSubject: Subject<Message[]> = new Subject<Message[]>();
    private typingSubject: Subject<any> = new Subject<any>();
    public messages: Message[] = [];
    private receiverId: string = '';
    private loggedInUserId: string = '';
    private isConnected = false;
    public connect(): void {
      const socket = new SockJS('http://localhost:8085/websocket');
      this.stompClient = Stomp.over(socket);
    
      this.stompClient.onConnect = () => {  
        this.isConnected = true; // Mark as connected
        console.log('Connected to WebSocket');
        this.subscribeToTopics();
      };
    
      this.stompClient.onStompError = (frame) => {
        console.error('STOMP error:', frame);
      };
    
      this.stompClient.activate();
    }

    private subscribeToTopics(): void {
      this.stompClient.subscribe('/topic/messages', (message: any) => {
        const receivedMessage = JSON.parse(message.body);
        this.messages.push(receivedMessage);
        this.messagesSubject.next([receivedMessage]);
        console.log('Received message:', receivedMessage);
      });
    
      this.stompClient.subscribe('/topic/typings', (message: any) => {
        const typingNotification: Message = JSON.parse(message.body);
        console.log('Received typing notification:', typingNotification);
        this.typingSubject.next(typingNotification);
      });
    
      this.stompClient.subscribe('/topic/readseen', (message: any) => {
        const receivedMessage = JSON.parse(message.body);
        this.messages.push(receivedMessage);
        this.messagesSubject.next([receivedMessage]);
        console.log('Received message:', receivedMessage);
      });
    }  

    public sendMessage(message: Message): void {
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.publish({
          destination: '/app/send',
          body: JSON.stringify(message),
        });
        console.log('Message sent:', message);
      } else {
        console.error('WebSocket is not connected. Message not sent.');
      }
    }

    public disconnect(): void {
      if (this.stompClient) {
        this.stompClient.deactivate();
      }
    }

    public getMessages(): Observable<Message[]> {
      return this.messagesSubject.asObservable();
    }

    receiveMessage(message: Message) {
      //this.messages.push(message);
      //this.messagesSubject.next([...this.messages]);
    }

    public sendTypingNotification(senderId: string, receiverId: string, id: string): void 
    { 
      const message: Message = 
      { id, senderId, receiverId, isBroadcast: false,
        message: 'User is typing...', 
        type: 'TYPING', 
        tempResponse: '', 
        currentPage: '' 
      }; 
      console.log('Sending typing notification:', message);
      const destination = `/app/typing`;
      this.loggedInUserId = senderId;
      this.receiverId = receiverId;
      this.stompClient.publish({ 
        destination: destination, 
        body: JSON.stringify(message),
      });
    }

    public getTypingNotifications(): Observable<any> {
      return this.typingSubject.asObservable();
    }   

    public sendSeenNotification(senderId: string, receiverId: string, id: string): void {
      const seenMessage: Message = {
        isBroadcast: false,
        id,
        senderId,
        receiverId,
        message: 'Message seen',
        type: 'SEEN',
        tempResponse: '',
        currentPage: ''
      };
      console.log('Sending seen notification:', seenMessage);
    
      if (this.stompClient && this.stompClient.connected) {
        this.stompClient.publish({
          destination: '/app/seen',
          body: JSON.stringify(seenMessage)
        });
      } else {
        console.error('WebSocket is not connected. Message not sent.');
      }
    }

    public handleSeenNotification(seenNotification: Message): void {

    } 

    public getSeenNotifications(): Observable<any> {
      return this.messagesSubject.asObservable();
    }
  }
