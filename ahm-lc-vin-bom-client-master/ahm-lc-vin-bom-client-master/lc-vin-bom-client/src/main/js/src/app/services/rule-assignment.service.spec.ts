import { TestBed } from '@angular/core/testing';

import { RuleAssignmentService } from './rule-assignment.service';

describe('RuleAssignmentService', () => {
  let service: RuleAssignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RuleAssignmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
