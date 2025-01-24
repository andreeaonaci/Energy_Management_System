import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { AdminDashboardComponent } from './components/admin/admin.component';
import { AdminSettingsComponent } from './components/admin/admin-settings/admin-settings.component';
import { ClientComponent } from './components/client/client.component';
import { AuthGuard } from './guard/admin.guard';
import { NotificationComponent } from './notification/notification.component';
import { TestPageComponent } from './components/test-page/test-page.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'admin/dashboard', component: AdminDashboardComponent, canActivate: [AuthGuard], data: { expectedRole: 'ADMIN' } },
  { path: 'client/dashboard', component: ClientComponent, canActivate: [AuthGuard], data: { expectedRole: 'CLIENT' } },
  { path: 'login', component: LoginComponent },
  { path: 'notification', component: NotificationComponent },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  providers: [AuthGuard],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
