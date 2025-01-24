import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { Device } from '../models/device.model';

@Injectable({
  providedIn: 'root',  // Ensures the service is available application-wide
})

export class DeviceService {
  private baseUrl = 'http://devices.localhost/devices';
  private apiUrl = 'http://devices.localhost/devices';
  //private baseUrl = 'http://localhost:8081/devices';
  //private apiUrl = 'http://localhost:8081/devices';

  constructor(private http: HttpClient) {

  }

  // Fetch all devices
  // save the devices in a variable and then return it

  getDevices(): Observable<Device[]> {
    var dev = this.http.get<Device[]>(`http://devices.localhost/devices/all`);
    //var dev = this.http.get<Device[]>(`http://localhost:8081/devices/all`);

    console.log(dev.subscribe(devices => console.log(devices)));
    
    return dev;
  }

  createDevice(device: Device): Observable<Device> {
    return this.http.post<Device>('http://devices.localhost/devices/create', device);
    //return this.http.post<Device>('http://localhost:8081/devices/create', device);
  }
  
  updateDevice(device: Device): Observable<Device> {
    return this.http.put<Device>(`http://devices.localhost/devices/${device.id}`, device);
    //return this.http.put<Device>(`http://localhost:8081/devices/${device.id}`, device);
  }  

  // Delete a device
  deleteDevice(deviceId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/delete/${deviceId}`); // Use base URL for delete
  }

  // Get devices for a specific user
  getDevicesForUser(userId: number): Observable<Device[]> {
    console.log(userId);
    return this.http.get<Device[]>(`${this.apiUrl}/user/${userId}`).pipe(
        catchError(error => {
            console.error('Error fetching devices for user', error);
            return throwError(() => error);
        })
    );
}

}
