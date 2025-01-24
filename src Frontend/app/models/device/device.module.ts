import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DeviceListComponent } from '../.././device-list/device-list.component';
import { DeviceCreateComponent } from '../.././device-create/device-create.component';
import { DeviceUpdateComponent } from '../.././device-update/device-update.component';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    DeviceListComponent,
    DeviceCreateComponent,
    DeviceUpdateComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule 
  ],
  exports: [
    DeviceListComponent,
    DeviceCreateComponent,
    DeviceUpdateComponent
  ]
})
export class DeviceModule { }