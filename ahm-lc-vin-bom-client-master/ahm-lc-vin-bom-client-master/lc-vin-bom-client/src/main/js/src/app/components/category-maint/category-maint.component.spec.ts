import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CategoryMaintComponent } from './category-maint.component';

describe('CategoryMaintComponent', () => {
  let component: CategoryMaintComponent;
  let fixture: ComponentFixture<CategoryMaintComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CategoryMaintComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CategoryMaintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
