export interface User {
    id: number;
    name: string;
    password: string;
    role: 'ADMIN' | 'CLIENT';
  }