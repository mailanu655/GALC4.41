import { TestBed } from '@angular/core/testing';

import { FnConfigServiceService } from './fn-config-service.service';

describe('FnConfigServiceService', () => {
  let service: FnConfigServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FnConfigServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
