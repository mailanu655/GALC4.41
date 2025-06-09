import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManualOverrideApprovalComponent } from './manual-override-approval.component';

describe('ManualOverrideApprovalComponent', () => {
  let component: ManualOverrideApprovalComponent;
  let fixture: ComponentFixture<ManualOverrideApprovalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManualOverrideApprovalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManualOverrideApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
