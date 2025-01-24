import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DeviceAssignmentService {
  private apiUrl = 'http://devices.localhost/api/monitoring/assignments';
  //private apiUrl = 'http://localhost:8081/api/monitoring/assignments';

  constructor(private http: HttpClient) {}

  // Get assignments for a specific user
  getAssignmentsByUser(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/user/${userId}`);
  }

  // Get assignments for a specific device
  getAssignmentsByDevice(deviceId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/device/${deviceId}`);
  }

  // Assign a device to a user
  createAssignment(userId: number, deviceId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/create`, { userId, deviceId });
  }

  // Delete an assignment
  deleteAssignment(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
