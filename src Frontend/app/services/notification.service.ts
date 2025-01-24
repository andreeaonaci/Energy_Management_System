

export class NotificationService {
    message: string = '';
    
    constructor() {
        this.message = 'Hello from NotificationService';
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

    showNotification(message: string, action: string) {
        this.message = message;
    }
}
