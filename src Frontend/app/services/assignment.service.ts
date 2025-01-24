// src/app/services/assignment.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DeviceAssignment } from '../models/device-assignment.model';

@Injectable({
  providedIn: 'root'
})
export class AssignmentService {
  private apiUrl = 'http://devices.localhost/devices/api/devices';
  //private apiUrl = 'http://localhost:8081/devices/api/devices';

  constructor(private http: HttpClient) {}

  getDeviceAssignments(): Observable<DeviceAssignment[]> {
    return this.http.get<DeviceAssignment[]>(`${this.apiUrl}`);
  }

  assignDevice(assignment: DeviceAssignment): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/create`, assignment);
  }

  removeAssignment(assignment: DeviceAssignment): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${assignment.userId}/${assignment.deviceId}`);
  }

  getDeviceAssignmentsForUser(userId: number): Observable<DeviceAssignment[]> {
    const url = `${this.apiUrl}/device-assignments/user/${userId}`;
    return this.http.get<DeviceAssignment[]>(url);
  }

  // Delete a specific device assignment by its ID
  deleteDeviceAssignment(assignmentId: number): Observable<void> {
    const url = `${this.apiUrl}/device-assignments/${assignmentId}`;
    return this.http.delete<void>(url);
  }
}
