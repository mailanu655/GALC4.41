import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpandRowDialogComponent } from './expand-row-dialog.component';

describe('ExpandRowDialogComponent', () => {
  let component: ExpandRowDialogComponent;
  let fixture: ComponentFixture<ExpandRowDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpandRowDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpandRowDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
