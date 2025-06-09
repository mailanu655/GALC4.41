import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpandRowBottomSheetComponent } from './expand-row-bottom-sheet.component';

describe('ExpandRowBottomSheetComponent', () => {
  let component: ExpandRowBottomSheetComponent;
  let fixture: ComponentFixture<ExpandRowBottomSheetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpandRowBottomSheetComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpandRowBottomSheetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
