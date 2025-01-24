import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { DeviceService } from '../../services/device.service';
import { Device } from '../../models/device.model';
import { UserService } from '../../services/user.service';
import { WebSocketService } from '../../services/web-socket.service';
import { Chart } from 'chart.js';
import { EnergyMeasurementService } from '../../services/energy.service';
import { HttpClient } from '@angular/common/http';
import { Message } from '../message.model';
import { User } from '../user.model';

import { MessageService } from '../../services/message.service';

import {
  LineController,
  LineElement,
  PointElement,
  LinearScale,
  Title,
  CategoryScale,
  Tooltip,
  Legend,
} from 'chart.js';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.scss']
})

export class ClientComponent implements OnInit {
  loggedInUserId: string = '';
  public users: User[] = [];
  showChart: boolean = false;
  devices: Device[] = [];
  private username: string = '';
  private password: string = '';
  private userId: number = 0;
  notifications: string[] = [];
  energyData: any[] = [];
  deviceId: number = 1;
  private chart: Chart | null = null;
  selectedDate: string = '';

  messages: Message[] = []; 
  newMessage: string = ''; 
  selectedUserId: string = ''; 

  typingUsers: { [key: string]: string } = {};
  seenStatus: { [key: string]: string } = {};

  adminMessages: Message[] = [];
  otherMessages: Message[] = []; 
  newAdminMessage: string = ''; 
  newOtherMessage: string = '';

  constructor(private deviceService: DeviceService, private userService: UserService,
    private energyService: EnergyMeasurementService, private httpClient: HttpClient, private webSocketService: WebSocketService,
    private messageService: MessageService
  ) {
    Chart.register(LineController, LineElement, PointElement, LinearScale, Title, CategoryScale, Tooltip, Legend);
    this.senderId = this.userService.getLoggedInUser();
    //this.loadMessages();
    console.log('Loaded senderId:', this.senderId);
    //setInterval(() => this.loadMessages(), 10000);
  }



  fetchLoggedInUserId(): void {
    this.loggedInUserId = this.userService.getLoggedInUser();
    console.log("user id", this.loggedInUserId);
  }

  ngOnInit(): void {
    this.fetchLoggedInUserId();
    this.fetchDevices();
    this.loadUsers();
    this.username = localStorage.getItem('userName') || '';
    this.password = localStorage.getItem('userPass') || '';
    this.userService.getUserId(this.username, this.password).subscribe({
      next: (response) => {
        console.log('User ID from nginit client:', response.userId);
        this.senderId = response.userId;
        this.loggedInUserId = response.userId;
        //this.webSocketService.addUser(this.senderId);
        localStorage.setItem('userId', response.userId);
      },
      error: (err) => {
        console.error('Error fetching user ID:', err);
      }
    });
    // = this.userService.getLoggedInUser();
    const ctx = document.getElementById('energyChart') as HTMLCanvasElement;
    const energyChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: ['12AM', '1AM', '2AM', '3AM'],
        datasets: [
          {
            label: 'Energy Consumption (kWh)',
            data: [5, 10, 15, 20],
            borderColor: '#3e95cd',
            fill: false
          }
        ]
      }
    }
  );

    this.loadEnergyDataForDay(new Date().toISOString().split('T')[0]);
  }

  responseMessage: string = '';
  loggedInUser: User | null = null;

  loadUsers(): void {
    this.userService.getAllUsers().subscribe(users => {
      this.users = users;
    });
  }

  // loadMessages(): void {
  //   this.messageService.getAllMessages(this.senderId).subscribe({
  //     next: (data) => {
  //       console.log('Loaded messages:', data);
  //       this.messages = data;
  //     },
  //     error: (err) => {
  //       console.error('Error fetching messages:', err);
  //     }
  //   });
  // }

  fetchDevices(): void {
    this.username = localStorage.getItem('userName') || '';
    this.password = localStorage.getItem('userPass') || '';

    this.userService.getUserId(this.username, this.password).subscribe({
      next: (response) => {
        if (response.userId && !isNaN(Number(response.userId))) {
          this.userId = parseInt(response.userId, 10);

          this.deviceService.getDevicesForUser(this.userId).subscribe({
            next: (devices) => {
              this.devices = devices;
            },
            error: (err) => {
              console.error('Error fetching devices:', err);
            }
          });
        } else {
          console.error('Invalid userId:', response.userId);
        }
      },
      error: (err) => {
        console.error('Error fetching user ID:', err);
      }
    });
  }

  onDateChange(event: any): void {
    this.selectedDate = event.target.value; // Store the selected date
    //this.loadEnergyDataForDay(this.selectedDate); // Load data for selected date
  }

  loadEnergyDataForDay(selectedDate: string) {
    this.energyService.getEnergyDataForDay(this.deviceId, selectedDate).subscribe(
      (data) => {
        console.log('Energy data fetched:', data); // Log fetched data
        this.energyData = data as any[];
        this.updateChart(); // Update the chart with the fetched data
      },
      (error) => {
        console.error('Error fetching energy data:', error);
      }
    );
  }

  updateChart() {
    if (this.chart) {
      this.chart.destroy();
    }
    this.chart = null;

    const labels = this.energyData.map((entry: any) => {
      if (entry.timestamp) {
        try {
          const date = new Date(entry.timestamp);
          const hours = date.getHours().toString().padStart(2, '0');
          const minutes = date.getMinutes().toString().padStart(2, '0');
          return `${hours}:${minutes}`;
        } catch (error) {
          console.warn('Error parsing timestamp:', entry.timestamp, error);
          return 'Invalid';
        }
      } else {
        console.warn('Missing timestamp:', entry);
        return 'Invalid';
      }
    });

    const data = this.energyData.map((entry: any) => {
      return entry.measurement_value ?? 0; // Default to 0 for missing values
    });

    const canvas = document.getElementById('energyChart') as HTMLCanvasElement;

    if (canvas) {
      this.chart = new Chart(canvas, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [
            {
              label: 'Energy Consumption (kWh)',
              data: data,
              borderColor: '#3e95cd',
              fill: false,
            },
          ],
        },
      });
    }
  }

  viewChart(deviceId: number): void {
    this.deviceId = deviceId; // Set the selected device ID
    this.showChart = true;
    this.energyService.getEnergyData(this.deviceId, this.selectedDate).subscribe(
      (data) => {
        this.energyData = data;
        this.updateChart(); // Update the chart with the fetched data
      },
      (error) => {
        console.error('Error fetching energy data:', error);
      }
    );
  }

  // respondToMessage(senderId: string, receiverId: string, responseMessage: string): void {
  //   if (responseMessage.trim() && this.selectedUser) {
  //     const message: Message = {
  //       message: this.responseMessage,
  //       senderId: this.loggedInUserId.toString(),
  //       receiverId: this.selectedUser.id.toString(),
  //       type: 'TEXT',
  //       tempResponse: ''
  //     };

  //     this.webSocketService.addUser(message.receiverId.toString());
  //     this.webSocketService.sendMessage(message);
  //   }
  // };

  senderId: string = '';
  selectedUser: User | null = null;

  // sendMessage(): void {
  //   if (this.selectedUser) {
  //     const message: Message = {
  //       message: this.responseMessage,
  //       senderId: this.loggedInUserId.toString(),
  //       receiverId: this.selectedUser.id.toString(),
  //       type: 'TEXT',
  //       tempResponse: '',
  //       currentPage: window.location.pathname,
  //     };
  //     this.webSocketService.sendMessage(message);
  //     //this.webSocketService.addUser(message.receiverId.toString());
  //   }
  // }
  selectUserForChat(user: User): void {
    console.log("username", user.name);
    this.selectedUser = user;
  }
}
