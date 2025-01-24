import { Component } from '@angular/core';
import { Device } from '../models/device.model';
import { DeviceService } from '../services/device.service';

@Component({
  selector: 'app-device-create',
  templateUrl: './device-create.component.html',
  styleUrls: ['./device-create.component.scss']
})

export class DeviceCreateComponent {

  newDevice: Device = { id: 0, name: '', address: '', description: '', maxhourlyenergyconsumption: 0 };
  errorMessage: string = '';

  constructor(private deviceService: DeviceService) {}

  createDevice(): void {
    this.deviceService.createDevice(this.newDevice).subscribe({
      next: (device) => {
        console.log('Device created:', device);
        this.newDevice = { id: 0, name: '', address: '', description: '', maxhourlyenergyconsumption: 0 };
      },
      error: (err) => {
        console.error('Error creating device', err);
        this.errorMessage = 'Failed to create device';
      }
    });
  }
}
