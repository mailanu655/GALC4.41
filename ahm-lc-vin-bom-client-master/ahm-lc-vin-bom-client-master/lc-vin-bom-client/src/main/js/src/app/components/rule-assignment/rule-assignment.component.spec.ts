import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RuleAssignmentComponent } from './rule-assignment.component';

describe('RuleAssignmentComponent', () => {
  let component: RuleAssignmentComponent;
  let fixture: ComponentFixture<RuleAssignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RuleAssignmentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RuleAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
