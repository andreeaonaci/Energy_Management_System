// import { Injectable } from '@angular/core';
// import { Stomp } from '@stomp/stompjs';
// import SockJS from 'sockjs-client';
// import { Observable, Subject } from 'rxjs';
// import { Message } from '../components/message.model'; // Adjust the import path if needed.

// @Injectable({
//   providedIn: 'root',
// })
// export class ChatService {
//   private stompClient: any;
//   private messageSubject = new Subject<Message>();
//   private readonly socketUrl = 'ws://localhost:8085/ws'; // Ensure this matches your backend endpoint.
//   private readonly destinationPath = '/app/send'; // Path for sending messages.

//   constructor() {
//     this.connect();
//   }

//   connect(): void {
//     const socket = new SockJS(this.socketUrl);
//     this.stompClient = Stomp.over(socket);

//     this.stompClient.connect({}, () => {
//       console.log('Connected to WebSocket');
      
//       // Subscribe to the topic for receiving messages
//       this.stompClient.subscribe('/topic/messages', (message: any) => {
//         const receivedMessage: Message = JSON.parse(message.body);
//         console.log('Received message:', receivedMessage);
//         this.messageSubject.next(receivedMessage);
//       });
//     }, this.handleError);
//   }

//   // sendMessage(message: Message): void {
//   //   if (this.stompClient && this.stompClient.connected) {
//   //     this.stompClient.send(this.destinationPath, {}, JSON.stringify(message));
//   //   } else {
//   //     console.error('WebSocket connection is not established.');
//   //   }
//   // }

//   getMessages(): Observable<Message> {
//     return this.messageSubject.asObservable();
//   }

//   private handleError(error: any): void {
//     console.error('WebSocket connection error:', error);
//   }
// }
