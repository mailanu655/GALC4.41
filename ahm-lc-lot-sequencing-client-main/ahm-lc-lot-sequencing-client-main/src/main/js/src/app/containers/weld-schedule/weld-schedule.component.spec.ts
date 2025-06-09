import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WeldScheduleComponent } from './weld-schedule.component';

describe('WeldScheduleComponent', () => {
  let component: WeldScheduleComponent;
  let fixture: ComponentFixture<WeldScheduleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WeldScheduleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WeldScheduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
