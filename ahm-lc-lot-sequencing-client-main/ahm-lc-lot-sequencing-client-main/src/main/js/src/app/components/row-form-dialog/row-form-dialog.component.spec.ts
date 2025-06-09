import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RowFormDialogComponent } from './row-form-dialog.component';

describe('RowFormDialogComponent', () => {
  let component: RowFormDialogComponent;
  let fixture: ComponentFixture<RowFormDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RowFormDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RowFormDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
