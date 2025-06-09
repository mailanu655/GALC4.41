import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Entity } from 'src/app/lib/rest-entity.service';
import { RestReadService } from 'src/app/lib/rest-read.service';
import { ConfigService } from '../config.service';

export class IssueProductId {
    jobId: number;
    productId: string;
}

export class IssueProduct extends Entity<IssueProductId> {
    id: IssueProductId;
    specCode: string;
    sequence: number;
    onTimestamp: Date;
}

@Injectable({
    providedIn: 'root'
})
export class IssueProductService extends RestReadService<IssueProduct, IssueProductId> {

    constructor(httpClient: HttpClient, private config: ConfigService) {
        super(httpClient);
        this.serviceUrl = config.serviceUrl;
        this.requestMapping = '/products';
    }

    findAllNg(jobId: number) {
        return this.get('/ng', { jobId: jobId });
    }
}

