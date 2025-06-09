import { TestBed } from '@angular/core/testing';

import { IssueProductService } from './issue-product.service';

describe('IssueProductService', () => {
    let service: IssueProductService;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        service = TestBed.inject(IssueProductService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
});
