import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RuleLotApprovalComponent } from './rule-lot-approval.component';

describe('RuleLotApprovalComponent', () => {
  let component: RuleLotApprovalComponent;
  let fixture: ComponentFixture<RuleLotApprovalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RuleLotApprovalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RuleLotApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
