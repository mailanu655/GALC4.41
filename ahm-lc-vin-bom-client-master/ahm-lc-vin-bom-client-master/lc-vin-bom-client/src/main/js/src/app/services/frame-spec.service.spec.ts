import { TestBed } from '@angular/core/testing';

import { FrameSpecService } from './frame-spec.service';

describe('FrameSpecService', () => {
  let service: FrameSpecService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FrameSpecService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
