import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigContentComponent } from './config-content.component';

describe('ConfigContentComponent', () => {
  let component: ConfigContentComponent;
  let fixture: ComponentFixture<ConfigContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfigContentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
