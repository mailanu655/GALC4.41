import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Comments } from './comments.component';

describe('NotesComponent', () => {
  let component: Comments;
  let fixture: ComponentFixture<Comments>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Comments ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Comments);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
