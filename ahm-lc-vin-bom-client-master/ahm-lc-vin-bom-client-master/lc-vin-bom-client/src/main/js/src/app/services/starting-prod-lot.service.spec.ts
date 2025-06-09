import { TestBed } from '@angular/core/testing';

import { StartingProdLotService } from './starting-prod-lot.service';

describe('StartingProdLotService', () => {
  let service: StartingProdLotService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StartingProdLotService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
