import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  private apiUrl = '/api/settings'; // Replace with actual API endpoint

  constructor(private http: HttpClient) {}

  // Fetch settings from the server
  getSettings(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  // Save settings to the server
  saveSettings(settings: any): Observable<any> {
    return this.http.put<any>(this.apiUrl, settings);
  }
}