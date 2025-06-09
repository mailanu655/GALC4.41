import { TestBed } from '@angular/core/testing';

import { PartAssignmentService } from './part-assignment.service';

describe('PartAssignmentService', () => {
  let service: PartAssignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PartAssignmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
