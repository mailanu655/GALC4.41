import { TestBed } from '@angular/core/testing';

import { LotsService } from './lots-services.service';

describe('LotsService', () => {
  let service: LotsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LotsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
