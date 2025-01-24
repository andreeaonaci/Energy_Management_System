import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardEnergyComponent } from './dashboard-energy.component';

describe('DashboardEnergyComponent', () => {
  let component: DashboardEnergyComponent;
  let fixture: ComponentFixture<DashboardEnergyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardEnergyComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardEnergyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
