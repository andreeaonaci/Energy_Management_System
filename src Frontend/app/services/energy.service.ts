// energy.service.ts (EnergyMeasurementService)
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EnergyMeasurementService {

  private apiUrl = 'http://communication.localhost/monitoring';
  //private apiUrl = 'http://localhost:8082/monitoring';

  constructor(private http: HttpClient) {}

  getMeasurements(deviceId: string, date: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/measurements/history?deviceId=${deviceId}&date=${date}`);
  }

  getEnergyDataForDay(deviceId: number, date: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${deviceId}/date=${date}`);
  }
  getEnergyData(deviceId: number, date: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/energy/${deviceId}/${date}`);
  } 
}
