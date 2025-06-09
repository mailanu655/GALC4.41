import { TestBed } from '@angular/core/testing';

import { ManualOverrideService } from './manual-override.service';

describe('ManualOverrideService', () => {
  let service: ManualOverrideService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManualOverrideService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
