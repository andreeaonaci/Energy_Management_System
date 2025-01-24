import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { AdminDashboardComponent } from './components/admin/admin.component';
import { provideHttpClient, withFetch } from '@angular/common/http'; 
import { ReactiveFormsModule } from '@angular/forms';
import { ChatComponent } from  './components/chat/chat.component';

import { AppComponent } from './app.component';
import { NotificationService } from './services/notification.service';
import { AppRoutingModule } from './app-routing.module';
import { DeviceListComponent } from './device/device-list/device-list.component';
import { DeviceCreateComponent } from './device/device-create/device-create.component';
import { DeviceUpdateComponent } from './device/device-update/device-update.component';
import { ClientComponent } from './components/client/client.component';
import { AdminSettingsComponent } from './components/admin/admin-settings/admin-settings.component';
import { LoginComponent } from './components/login/login.component';
import { DeviceDeleteComponent } from './device-delete/device-delete.component';
import { AuthGuard } from './guard/admin.guard';
import { NotificationComponent } from './notification/notification.component';
import { EnergyMeasurementService } from './services/energy.service';
import { TestPageComponent } from './components/test-page/test-page.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminDashboardComponent,
    DeviceListComponent,
    DeviceCreateComponent,
    DeviceUpdateComponent,
    ClientComponent,
    AdminSettingsComponent,
    DeviceDeleteComponent,
    NotificationComponent,
    TestPageComponent,
    ChatComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot([]), 
    RouterModule.forChild([]),
    AppRoutingModule
  ],
  providers: [
    provideHttpClient(withFetch()),
    EnergyMeasurementService,
    AuthGuard,
    NotificationService
    //WebsocketService
  ],
  bootstrap: [AppComponent]
})

export class AppModule { }