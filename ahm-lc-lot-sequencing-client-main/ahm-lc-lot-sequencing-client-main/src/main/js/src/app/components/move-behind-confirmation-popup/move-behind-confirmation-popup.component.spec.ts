import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoveBehindConfirmationPopupComponent } from './move-behind-confirmation-popup.component';

describe('MoveBehindConfirmationPopupComponent', () => {
  let component: MoveBehindConfirmationPopupComponent;
  let fixture: ComponentFixture<MoveBehindConfirmationPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MoveBehindConfirmationPopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MoveBehindConfirmationPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
