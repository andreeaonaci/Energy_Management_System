import { Component, Input } from '@angular/core';
import { DeviceService } from '../services/device.service';
import { Device } from '../models/device.model';

@Component({
  selector: 'app-device-update',
  templateUrl: './device-update.component.html',
  styleUrls: ['./device-update.component.scss']
})
export class DeviceUpdateComponent {
  @Input() selectedDevice: Device | null = null;
  errorMessage: string = '';

  constructor(private deviceService: DeviceService) {}

  updateDevice(): void {
    if (this.selectedDevice) {
      this.deviceService.updateDevice(this.selectedDevice).subscribe({
        next: (device) => {
          console.log('Device updated:', device);
          this.selectedDevice = null;
        },
        error: (err) => {
          console.error('Error updating device', err);
          this.errorMessage = 'Failed to update device';
        }
      });
    }
  }
}
