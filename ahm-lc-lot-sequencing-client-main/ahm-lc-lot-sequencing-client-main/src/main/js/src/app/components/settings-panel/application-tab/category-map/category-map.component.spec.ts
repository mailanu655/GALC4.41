import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryMapComponent } from './category-map.component';

describe('CategoryMapComponent', () => {
  let component: CategoryMapComponent;
  let fixture: ComponentFixture<CategoryMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CategoryMapComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CategoryMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
