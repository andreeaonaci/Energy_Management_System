  import { Component, OnInit, AfterViewInit } from '@angular/core';
  import {  FormBuilder, FormGroup, Validators } from '@angular/forms';
  import { UserService } from '../../services/user.service';
  import { DeviceService } from '../../services/device.service';
  import { User } from '../user.model';
  import { Device } from '../../models/device.model';
  import { AbstractControl, ValidatorFn } from '@angular/forms';
  import { DeviceAssignment } from '../../models/device-assignment.model';
  import { AssignmentService } from '../../services/assignment.service';
  import { EnergyMeasurementService } from '../../services/energy.service';
  import { WebSocketService } from '../../services/web-socket.service';
  import { MessageService } from  '../../services/message.service';

  import { Message } from '../message.model';
  import { Chart } from 'chart.js';

  function passwordsMatchValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const password = control.get('password');
      const confirmPassword = control.get('confirmPassword');
      if (password && confirmPassword && password.value !== confirmPassword.value) {
        return { 'passwordMismatch': true };
      }
      return null;
    };
  }

  @Component({
    selector: 'app-admin-dashboard',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.scss']
  })

  export class AdminDashboardComponent implements OnInit, AfterViewInit  {
    public users: User[] = [];
    clientUsers: User[] = [];
    assignmentForm!: FormGroup;
    userForm!: FormGroup;
    showCreateUserForm: boolean = false; 
    devices: Device[] = [];
    selectedUser: User | null = null;
    confirmPassword: string = '';
    deviceAssignments: DeviceAssignment[] = [];
    selectedDevice: Device | null = null;
    loggedInUserId: string = '';
    messages: Message[] = [];
    responseMessage: string = '';

    typingUsers: { [key: string]: string } = {};
    seenStatus: { [key: string]: string } = {};

    ngAfterViewInit(): void {
      // Access the canvas element after the view is initialized
      const chartElement = document.getElementById('energyChart') as HTMLCanvasElement | null;
      
      if (chartElement) {
        new Chart(chartElement, {
          type: 'line', // Specify the type of the chart (line, bar, etc.)
          data: {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
            datasets: [{
              label: 'My First dataset',
              data: [65, 59, 80, 81, 56, 55, 40],
              borderColor: 'rgba(75, 192, 192, 1)',
              borderWidth: 1
            }]
          },
          options: {
            scales: {
              y: {
                beginAtZero: true
              }
            }
          }
        });
      } else {
        console.error('Chart element not found!');
      }
    }

    measurements: any[] = [];
    notifications: string[] = [];

    newUserName: string = '';
    newUserRole: 'ADMIN' | 'CLIENT' = 'CLIENT'; 

    errorMessage: string | null = null;

    private username: string = '';
    private password: string = '';

    constructor(
      private fb: FormBuilder, 
      private userService: UserService, 
      private deviceService: DeviceService, 
      private assignmentService: AssignmentService,
      private measurementService: EnergyMeasurementService,
      private webSocketService: WebSocketService,
      private messageService: MessageService
    ) 
    {
      this.deviceForm = this.fb.group({
        name: ['', Validators.required],
        description: ['', Validators.required],
        address: ['', Validators.required],
        maxHourlyConsumption: [0, Validators.required]
      });
      this.senderId = this.userService.getLoggedInUser();
      //this.loadMessages();
      //setInterval(() => this.loadMessages(), 10000);
    }

    // messages: Message[] = []; // Messages sent to the admin
    // responseMessage: string = ''; // Response text
    loggedInUser: User | null = null;

    ngOnInit(): void {
      this.initializeForm();
      this.loadUsers();
      this.loadDevices();
      this.loadClientUsers();
      this.loadDeviceAssignments(); 
      this.fetchLoggedInUserId();
      this.username = localStorage.getItem('userName') || '';
      this.password = localStorage.getItem('userPass') || '';
      console.log('Logged in user:', this.loggedInUser);
      //this.senderId = this.userService.getLoggedInUser();
      this.userService.getUserId(this.username, this.password).subscribe({
        next: (response) => {
          console.log('User ID from nginit client:', response.userId);
          this.senderId = response.userId;
          this.loggedInUserId = response.userId;
          localStorage.setItem('userId', response.userId);
          //this.webSocketService.addUser(this.senderId);
        },
        error: (err) => {
          console.error('Error fetching user ID:', err);
        }
      });
      
      console.log('Sender ID:', this.senderId);
    }

    // loadMessages(): void {
    //   this.messageService.getAllMessages(this.senderId).subscribe({
    //     next: (data) => {
    //       console.log('Loaded messages:', data);
    //       // add all data in messages array
    //       this.messages = data;
    //     },
    //     error: (err) => {
    //       console.error('Error fetching messages:', err);
    //     }
    //   });
    // }    
    
    deviceForm!: FormGroup;

    initializeForm(): void {
      this.userForm = this.fb.group({
        name: ['', Validators.required],
        role: ['', Validators.required],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', [Validators.required, Validators.minLength(6)]]
      }, { validator: passwordsMatchValidator() });
    
      this.deviceForm = this.fb.group({
        name: ['', Validators.required],
        description: ['', Validators.required],
        address: ['', Validators.required],
        maxHourlyConsumption: [0, Validators.required]
      });

      this.assignmentForm = this.fb.group({
        userId: ['', Validators.required],
        deviceId: ['', Validators.required]
      });
    }  

    loadClientUsers(): void {
      this.userService.getAllUsers().subscribe(users => {
        this.clientUsers = users.filter(user => user.role === 'CLIENT');
      });
    }

    loadDeviceAssignments(): void {
      this.assignmentService.getDeviceAssignments().subscribe(assignments => {
        this.deviceAssignments = assignments;
      });
    }

    assignDevice(): void {
      if (this.assignmentForm.valid) {
        const newAssignment: DeviceAssignment = {
          userId: this.assignmentForm.value.userId,
          deviceId: this.assignmentForm.value.deviceId
        };

        this.assignmentService.assignDevice(newAssignment).subscribe(() => {
          this.loadDeviceAssignments();
          this.assignmentForm.reset();
        });
      }
    }

    removeAssignment(assignment: DeviceAssignment): void {
      if (confirm('Are you sure you want to remove this assignment?')) {
        this.assignmentService.removeAssignment(assignment).subscribe(() => {
          this.loadDeviceAssignments(); 
        });
      }
    }
    
    loadUsers(): void {
      this.userService.getAllUsers().subscribe(users => {
        this.users = users;
      });
    }

    loadDevices(): void {
      this.deviceService.getDevices().subscribe(devices => {
        this.devices = devices;
      });
    }

    editUser(user: User): void {
      this.selectedUser = { ...user };
    }

    onUserNameChange(event: Event): void {
      if (this.selectedUser) {
        this.selectedUser.name = (event.target as HTMLInputElement).value;
      }
    }

    onUserRoleChange(event: Event): void {
      if (this.selectedUser) {
        this.selectedUser.role = (event.target as HTMLSelectElement).value as 'ADMIN' | 'CLIENT';
      }
    }

    onUserPasswordChange(event: Event): void {
      if (this.selectedUser) {
        this.selectedUser.password = (event.target as HTMLInputElement).value;
      }
    }
    
    updateUser(): void {
      if (this.selectedUser) {
        this.userService.updateUser(this.selectedUser).subscribe(() => {
          this.loadUsers();
          this.selectedUser = null;
        });
      }
    }

    deleteUser(userId: number): void {
      if (confirm('Are you sure you want to delete this user?')) {

        const assignments = this.deviceAssignments.filter(assignment => assignment.userId === userId);

        if (assignments.length > 0) {
          if (confirm('This user has device assignments. Are you sure you want to delete this user?')) {
            assignments.forEach(assignment => {
              this.assignmentService.removeAssignment(assignment).subscribe(() => {
                console.log('Assignment removed:', assignment);
                this.loadDeviceAssignments();
              })});
            }
          else 
          {
            return;
          }
        }

        this.userService.deleteUser(userId).subscribe(() => {
          this.loadUsers();
        });
      }
    }

    editDevice(device: Device): void {
      this.selectedDevice = { ...device };
    }

    onDeviceNameChange(event: Event): void {
      if (this.selectedDevice) {
        this.selectedDevice.name = (event.target as HTMLInputElement).value;
      }
    }

    onDeviceDescriptionChange(event: Event): void {
      if (this.selectedDevice) {
        this.selectedDevice.description = (event.target as HTMLInputElement).value;
      }
    }

    onDeviceAddressChange(event: Event): void {
      if (this.selectedDevice) {
        this.selectedDevice.address = (event.target as HTMLInputElement).value;
      }
    }

    onDeviceMaxConsumptionChange(event: Event): void {
      if (this.selectedDevice) {
        this.selectedDevice.maxhourlyenergyconsumption = +(event.target as HTMLInputElement).value;
      }
    }

    updateDevice(): void {
      if (this.selectedDevice) {
        console.log('Updating device:', this.selectedDevice);
        this.deviceService.updateDevice(this.selectedDevice).subscribe({
          next: (response) => {
            console.log('Device updated successfully:', response);
            this.loadDevices();
            this.selectedDevice = null;
          },
          error: (err) => {
            console.error('Error updating device:', err);
            this.errorMessage = 'Error updating device: ' + err.message;
          }
        });
      }
    }  

    deleteDevice(deviceId: number): void {
      if (confirm('Are you sure you want to delete this device?')) {

        const assignments = this.deviceAssignments.filter(assignment => assignment.deviceId === deviceId);

        if (assignments.length > 0) {
          if (confirm('This device has user assignments. Are you sure you want to delete this device?')) {
            assignments.forEach(assignment => {
              this.assignmentService.removeAssignment(assignment).subscribe(() => {
                console.log('Assignment removed:', assignment);
                this.loadDeviceAssignments();
              })});
            }
          else 
          {
            return;
          }
        }

        this.deviceService.deleteDevice(deviceId).subscribe(() => {
          this.loadDevices();
        });
      }
    }

    createUser(): void {
      if (this.userForm.valid) {
        const newUser: User = {
          id: 0,
          name: this.userForm.value.name,
          role: this.userForm.value.role,
          password: this.userForm.value.password 
        };
    
        this.userService.createUser(newUser).subscribe({
          next: () => {
            this.loadUsers();
            this.userForm.reset();
            this.showCreateUserForm = false;
            this.errorMessage = null;
          },
          error: (err) => {
            this.errorMessage = 'Error creating user: ' + err.message;
          },
        });
      } else {
        this.errorMessage = 'Please fill in all fields correctly.';
      }
      this.loadUsers();
    }
    
    createDevice(): void {
      if (this.deviceForm.valid) {
        const newDevice: Device = {
          id: 0,
          name: this.deviceForm.value.name,
          description: this.deviceForm.value.description,
          address: this.deviceForm.value.address,
          maxhourlyenergyconsumption: this.deviceForm.value.maxHourlyConsumption
        };

        console.log('Creating device:', newDevice.maxhourlyenergyconsumption);
    
        this.deviceService.createDevice(newDevice).subscribe(() => {
          this.loadDevices();
          this.deviceForm.reset();
        });
      }
    }  
    fetchMeasurements(): void {
      const deviceId = '12345';  // Example device ID
      const date = '2024-11-19'; // Example date

      this.measurementService.getMeasurements(deviceId, date).subscribe(data => {
        this.measurements = data;
        this.displayChart();
      });
    }

    displayChart(): void {
      const chartData = this.measurements.map(d => d.measurementValue);
      const labels = this.measurements.map(d => d.timestamp);

      const chartElement = document.getElementById('energyChart') as HTMLCanvasElement | null;
      if (chartElement) {
        new Chart(chartElement, {
          type: 'line', // or any other chart type
          data: {
            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
            datasets: [{
              label: 'My First dataset',
              data: [65, 59, 80, 81, 56, 55, 40],
              borderColor: 'rgba(75, 192, 192, 1)',
              borderWidth: 1
            }]
          },
          options: {
            scales: {
              y: {
                beginAtZero: true
              }
            }
          }
        });
      } else {
        console.error('Chart element not found!');
      }
    }

    senderId: string = '';

    selectUserForChat(user: User): void {
      this.selectedUser = user;
    }

    fetchLoggedInUserId(): void {
      this.loggedInUserId = this.userService.getLoggedInUser();
      console.log("user id", this.loggedInUserId);
    }
  }
