import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduleRepTbxComponent } from './schedule-rep-tbx.component';

describe('ScheduleRepTbxComponent', () => {
  let component: ScheduleRepTbxComponent;
  let fixture: ComponentFixture<ScheduleRepTbxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScheduleRepTbxComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScheduleRepTbxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
