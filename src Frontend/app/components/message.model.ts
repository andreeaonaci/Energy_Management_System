export interface Message {
    id: string;
    message: string;
    senderId: string;
    receiverId: string;
    type: string;
    tempResponse: string;
    currentPage: string;
    isBroadcast: boolean;
  }

  export interface MessageResponse {
    body: Message;      // The actual message is inside the body
    headers: any;       // You can define a more specific type for headers if needed
    statusCode: string;
    statusCodeValue: number;
  }