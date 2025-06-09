import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SelectProdLotDialogComponent } from './select-prod-lot-dialog.component';


describe('SelectProdLotDialogComponent', () => {
  let component: SelectProdLotDialogComponent;
  let fixture: ComponentFixture<SelectProdLotDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SelectProdLotDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectProdLotDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
