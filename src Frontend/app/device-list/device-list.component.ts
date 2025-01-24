import { Component, OnInit } from '@angular/core';
import { DeviceService } from '../services/device.service';
import { Device } from '../models/device.model';

@Component({
  selector: 'app-device-list',
  templateUrl: './device-list.component.html',
  styleUrls: ['./device-list.component.scss']
})
export class DeviceListComponent implements OnInit {
  devices: Device[] = [];

  constructor(private deviceService: DeviceService) {}

  ngOnInit(): void {
    this.loadDevices();
  }

  loadDevices(): void {
    this.deviceService.getDevices().subscribe({
      next: (devices) => {
        this.devices = devices;
      },
      error: (err) => {
        console.error('Error loading devices', err);
      }
    });
  }

  deleteDevice(deviceId: number): void {
    this.deviceService.deleteDevice(deviceId).subscribe({
      next: () => {
        this.devices = this.devices.filter(device => device.id !== deviceId);
      },
      error: (err) => {
        console.error('Error deleting device', err);
      }
    });
  }
}
