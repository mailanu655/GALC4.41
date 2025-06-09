import { TestBed } from '@angular/core/testing';

import { LotAssignmentService } from './lot-assignment.service';

describe('LotAssignmentService', () => {
  let service: LotAssignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LotAssignmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
