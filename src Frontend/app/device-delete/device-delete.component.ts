import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DeviceService } from '../services/device.service';
import { Device } from '../models/device.model';

@Component({
  selector: 'app-device-delete',
  templateUrl: './device-delete.component.html',
  styleUrls: ['./device-delete.component.scss']
})
export class DeviceDeleteComponent implements OnInit {
  newDevice: Device = { id: 0, name: '', address: '', description: '', maxhourlyenergyconsumption: 0 };

  constructor(
    private deviceService: DeviceService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.newDevice.id = id ? +id : 0;

    this.deviceService.getDevices().subscribe(devices => {
      const device = devices.find(d => d.id === this.newDevice.id);
      if (device) {
        this.newDevice = device;
      }
    });
  }

  deleteDevice(): void {
    this.deviceService.deleteDevice(this.newDevice.id).subscribe({
      next: () => {
        alert(`Device ${this.newDevice.name || ''} deleted successfully!`);
        this.router.navigate(['/devices']);
      },
      error: (err) => {
        console.error('Error deleting device', err);
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/devices']);
  }
}
