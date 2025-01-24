import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from "../components/message.model"
import { BehaviorSubject } from 'rxjs';
import { WebSocketService } from './web-socket.service';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private baseUrl = 'http://localhost:8085/chat';
  //private baseUrl = 'http://localhost/chat';
  private messages: Message[] = []; // Store messages globally
  private messagesSubject: BehaviorSubject<Message[]> = new BehaviorSubject<Message[]>(this.messages);


  constructor(private http: HttpClient, private webSocketService: WebSocketService) {}

  // getMessages() {
  //   return this.messagesSubject.asObservable();
  // }

  // getAllMessages(userId: string): Observable<any> {
  //   return this.http.get(`${this.baseUrl}/all`, {
  //     params: { userId: userId }
  //   });
  // }

  // addMessage(message: Message) {
  //   this.messages.push(message);
  //   this.webSocketService.sendMessage(message);
  //   //this.messagesSubject.next(this.messages);
  // }
  // getAllMessages(userId: string): Observable<any> {
  //   return this.http.get(`${this.baseUrl}/all`, {
  //     params: { userId: userId }
  //   });
  // }

  // Add a message and send it through WebSocket
  addMessage(message: Message) {
    //this.messages.push(message);
    this.webSocketService.sendMessage(message);  // Send message through WebSocket
    //this.messagesSubject.next(this.messages);  // Update the message list
  }

  // Get the observable for messages
  getMessages() {
    return this.messagesSubject.asObservable();
  }

  // getMessagesForUser(senderId: string, receiverId: string): Observable<any> {
  //   return this.http.get(`${this.baseUrl}/all`, {
  //     params: { senderId: senderId, receiverId: receiverId }
  //   });
  // }
}