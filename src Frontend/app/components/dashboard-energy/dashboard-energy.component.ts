// energy-consumption.component.ts
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { EnergyMeasurementService } from '../../services/energy.service';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-energy-consumption',
  templateUrl: './dashboard-energy.component.html',
  styleUrls: ['./dashboard-energy.component.scss']
})
export class EnergyConsumptionComponent  {
  // @ViewChild('energyChart') energyChart!: ElementRef;
  // chart: any;
  // selectedDate: string = '';

  // constructor(private EnergyMeasurementService: EnergyService) {}

  // ngOnInit(): void {
  //   // Optionally, load the default date's data when the component loads
  //   this.fetchEnergyData(new Date().toISOString().split('T')[0]);
  // }

  // onDateChange(event: any): void {
  //   this.fetchEnergyData(event.target.value);
  // }

  // fetchEnergyData(date: string): void {
  //   this.EnergyMeasurementService.getEnergyConsumption(date).subscribe(data => {
  //     this.renderChart(data);
  //   }, error => {
  //     console.error('Error fetching energy consumption data:', error);
  //   });
  // }

  // renderChart(data: any): void {
  //   const hours = data.map((entry: any) => entry.hour);
  //   const consumption = data.map((entry: any) => entry.consumption);

  //   if (this.chart) {
  //     this.chart.destroy();
  //   }

  //   this.chart = new Chart(this.energyChart.nativeElement, {
  //     type: 'line',
  //     data: {
  //       labels: hours,
  //       datasets: [{
  //         label: 'Energy Consumption (kWh)',
  //         data: consumption,
  //         borderColor: 'rgba(75, 192, 192, 1)',
  //         fill: false
  //       }]
  //     },
  //     options: {
  //       scales: {
  //         x: {
  //           title: {
  //             display: true,
  //             text: 'Hour of Day'
  //           }
  //         },
  //         y: {
  //           title: {
  //             display: true,
  //             text: 'Energy (kWh)'
  //           }
  //         }
  //       }
  //     }
  //   });
  // }
}
