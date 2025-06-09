import { TestBed } from '@angular/core/testing';

import { InterchangeableService } from './interchangeable.service';

describe('InterchangeableService', () => {
  let service: InterchangeableService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InterchangeableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
