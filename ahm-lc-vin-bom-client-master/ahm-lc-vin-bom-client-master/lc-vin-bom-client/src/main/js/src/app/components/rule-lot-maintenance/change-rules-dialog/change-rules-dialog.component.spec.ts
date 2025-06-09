import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeRulesDialogComponent } from './change-rules-dialog.component';

describe('ChangeRulesDialogComponent', () => {
  let component: ChangeRulesDialogComponent;
  let fixture: ComponentFixture<ChangeRulesDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChangeRulesDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangeRulesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
