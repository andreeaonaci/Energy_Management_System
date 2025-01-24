import { Component } from '@angular/core';
import { UserService } from '../../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DeviceService } from '../../services/device.service';
import { Device } from '../../models/device.model';
import { HttpClient } from '@angular/common/http';
import { response } from 'express';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  errorMessage: string = '';
  devices: Device[] = [];
  userId: string = '';

  constructor(
    private userService: UserService,
    private router: Router,
    private fb: FormBuilder,
    private http: HttpClient,
    private route: ActivatedRoute,
    private deviceService: DeviceService
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  login() {
    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;
      this.userService.authenticateUser(username, password).subscribe({
        next: (response) => {
          console.log('Login successful', response);
          if (response && response.role) {
            this.userService.getUserId(username, password).subscribe({
              next: (response) => {
                console.log('User ID:', response.userId);
                localStorage.setItem('userId', response.userId);
              },
              error: (err) => {
                console.error('Error fetching user ID:', err);
              }
            });
            localStorage.setItem('userRole', response.role);
            localStorage.setItem('userName', username);
            localStorage.setItem('userPass', password);
            console.log('Role:', response.role);
            
            const redirectUrl = response.role === 'ADMIN' ? '/admin/dashboard' : '/client/dashboard';
            this.router.navigate([redirectUrl]);
          } else {
            this.errorMessage = 'Role is missing in response';
          }
        },
        error: () => {
          this.errorMessage = 'Invalid username or password';
        }
      });
    }
  }
}
