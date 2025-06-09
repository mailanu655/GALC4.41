import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartAssignmentComponent } from './part-assignment.component';

describe('PartAssignmentComponent', () => {
  let component: PartAssignmentComponent;
  let fixture: ComponentFixture<PartAssignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartAssignmentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
