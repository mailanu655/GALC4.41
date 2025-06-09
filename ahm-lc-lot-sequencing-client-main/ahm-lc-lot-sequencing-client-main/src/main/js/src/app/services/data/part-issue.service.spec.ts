import { TestBed } from '@angular/core/testing';

import { PartIssueService } from './part-issue.service';

describe('PartIssueService', () => {
    let service: PartIssueService;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        service = TestBed.inject(PartIssueService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
});
