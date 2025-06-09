import { TestBed } from '@angular/core/testing';

import { RuleLotApprovalService } from './rule-lot-approval.service';

describe('RuleLotApprovalService', () => {
  let service: RuleLotApprovalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RuleLotApprovalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
