import { TestBed } from '@angular/core/testing';

import { PMQAIssueService } from './pmqa-issue.service';

describe('PMQAIssueService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: PMQAIssueService = TestBed.get(PMQAIssueService);
        expect(service).toBeTruthy();
    });
});
