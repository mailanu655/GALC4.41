import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RuleLotMaintenanceComponent } from './rule-lot-maintenance.component';

describe('RuleLotMaintenanceComponent', () => {
  let component: RuleLotMaintenanceComponent;
  let fixture: ComponentFixture<RuleLotMaintenanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RuleLotMaintenanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RuleLotMaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
