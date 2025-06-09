import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterchangeableComponent } from './interchangeable.component';

describe('InterchangeableComponent', () => {
  let component: InterchangeableComponent;
  let fixture: ComponentFixture<InterchangeableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InterchangeableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InterchangeableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
