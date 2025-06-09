import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubAssyProdComponent } from './sub-assy-prod.component';

describe('SubAssyProdComponent', () => {
  let component: SubAssyProdComponent;
  let fixture: ComponentFixture<SubAssyProdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubAssyProdComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubAssyProdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
