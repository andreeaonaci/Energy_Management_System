import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceUpdateComponent } from './device-update.component';

describe('DeviceUpdateComponent', () => {
  let component: DeviceUpdateComponent;
  let fixture: ComponentFixture<DeviceUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeviceUpdateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeviceUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
