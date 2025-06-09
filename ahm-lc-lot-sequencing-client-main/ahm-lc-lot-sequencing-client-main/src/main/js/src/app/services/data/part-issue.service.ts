import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Entity, RestEntityService } from 'src/app/lib/rest-entity.service';
import { ConfigService } from '../config.service';

export class PartIssue extends Entity<number> {
    id: number;
    jobId: number;
    processPointId: string;
    productId: string;
    partName: string;
    statusId: number;
    dismissed: boolean;
}

@Injectable({
    providedIn: 'root'
})
export class PartIssueService extends RestEntityService<PartIssue, number> {

    constructor(httpClient: HttpClient, private config: ConfigService) {
        super(httpClient);
        this.serviceUrl = config.serviceUrl;
        this.requestMapping = '/issue/parts';
    }

    findAllForConfiguration(jobId: number) {
        return this.get('', { jobId: jobId });
    }

    setDismissed(id: number, dismissed: boolean) {
        return this.put('/' + id + '/dismiss/' + dismissed, null);
    }
}
