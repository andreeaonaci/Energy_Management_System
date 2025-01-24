import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../components/user.model';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://users.localhost/users';  // Your Spring Boot API URL
  private baseUrl = 'http://users.localhost/users'; // Set the base URL here
  //private apiUrl = 'http://localhost:8080/users';  // Your Spring Boot API URL
  //private baseUrl = 'http://localhost:8080/users'; // Set the base URL here

  private userRoleSubject = new BehaviorSubject<string | null>(null);
  userRole$ = this.userRoleSubject.asObservable();

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: Object) {}

  authenticateUser(username: string, password: string): Observable<{ url: string; role: string; accessToken: string }> {
    return this.http.post<{ url: string; role: string; accessToken: string }>(`${this.baseUrl}/login`, { name: username, password })
      .pipe(tap(response => {
        if (response && response.role) {
          this.setUserRole(response.role); // Set user role on successful login
        }
      }));
  }

  getLoggedInUser(): string {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('userId') || '';
    }
    return '';
  }

  setUserId(userId: string) {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('userId', userId);
    }
  }

  // getAllUsers(): Observable<User[]> {
  //   let us = this.http.get<User[]>(this.apiUrl);
  //   console.log(us);
  //   return us;
  // }

  getAllUsers(): Observable<User[]> {
    // Get the JWT token from localStorage (or sessionStorage)
    const token = localStorage.getItem('jwtToken'); 

    // Set the Authorization header if the token exists
    const headers = new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });

    // Send the GET request with the Authorization header
    return this.http.get<User[]>(this.apiUrl, { headers });
  }

  createUser(user: User): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${user.id}`, user);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  setUserRole(role: string) {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('userRole', role);
    }
  }

  getUserRole(): string {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('userRole') || '';
    }
    return '';
  }

  clearUserRole() {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('userRole');
    }
  }

  getUserId(username: string, password: string): Observable<{ userId: string; role: string; url: string; accessToken: string }> {
    return this.http.post<{ userId: string; role: string; url: string; accessToken: string }>(
      `${this.baseUrl}/login`, 
      { name: username, password }
    ).pipe(
      tap(response => {
        if (response && response.userId) {
          console.log('Fetched userId:', response.userId);
        }
      })
    );
  }
}
