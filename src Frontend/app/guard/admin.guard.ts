import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { AppComponent } from '../app.component';
import { NotificationService } from '../services/notification.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private userService: UserService, private router: Router, private notificationService: NotificationService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const expectedRole = route.data['expectedRole'];
    const role = this.userService.getUserRole();

    console.log('Expected role:', expectedRole);

    if (role != expectedRole) {
      this.router.navigate(['/notification']);
      this.notificationService.showNotification('You must be logged in to access this page', 'OK');
      return false;
    }

    return true;
  }
}
