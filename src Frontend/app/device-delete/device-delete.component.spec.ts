import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceDeleteComponent } from './device-delete.component';

describe('DeviceDeleteComponent', () => {
  let component: DeviceDeleteComponent;
  let fixture: ComponentFixture<DeviceDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeviceDeleteComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeviceDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
