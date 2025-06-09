import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeDcPartNumberDialogComponent } from './change-dc-part-number-dialog.component';

describe('ChangeDcPartNumberComponent', () => {
  let component: ChangeDcPartNumberDialogComponent;
  let fixture: ComponentFixture<ChangeDcPartNumberDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChangeDcPartNumberDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeDcPartNumberDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
