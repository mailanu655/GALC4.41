import { TestBed } from '@angular/core/testing';

import { PartIssueService } from './part-issue.service';

describe('PartIssueService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: PartIssueService = TestBed.get(PartIssueService);
        expect(service).toBeTruthy();
    });
});
