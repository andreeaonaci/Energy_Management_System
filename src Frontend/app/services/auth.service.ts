import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://users.localhost/auth'; // Adjust accordingly
  //private apiUrl = 'http://localhost:8080/auth'; 

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { username, password });
  }

  logout() {
    // Handle logout
    this.router.navigate(['/login']);
  }

  isAdmin(): boolean {
    // Implement logic to check if the user is an admin
    return true;
  }

  isClient(): boolean {
    // Implement logic to check if the user is a client
    return true;
  }
}