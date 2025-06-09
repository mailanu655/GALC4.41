import { TestBed } from '@angular/core/testing';

import { RuleLotMaintenanceService } from './rule-lot-maintenance.service';

describe('RuleLotMaintenanceService', () => {
  let service: RuleLotMaintenanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RuleLotMaintenanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
