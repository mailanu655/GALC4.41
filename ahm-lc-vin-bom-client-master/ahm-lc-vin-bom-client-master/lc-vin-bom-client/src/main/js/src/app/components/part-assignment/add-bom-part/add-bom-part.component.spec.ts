import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddBomPartComponent } from './add-bom-part.component';

describe('AddBomPartComponent', () => {
  let component: AddBomPartComponent;
  let fixture: ComponentFixture<AddBomPartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddBomPartComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddBomPartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
