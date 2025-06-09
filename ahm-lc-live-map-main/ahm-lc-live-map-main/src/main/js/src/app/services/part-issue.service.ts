import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { RestReadService } from '../lib/rest-read.service';
import { ConfigService } from './config.service';
import { PartIssue } from './model';

@Injectable({
    providedIn: 'root'
})
export class PartIssueService extends RestReadService<PartIssue, number>{

    constructor(httpClient: HttpClient, private config: ConfigService) {
        super(httpClient);
        this.serviceUrl = this.config.partIssueServiceUrl!;
        this.requestMapping = '/issue/parts';
    }

    // === override if IssueNotificaiton is not installed === //
    findAll(params?: any): Observable<Array<PartIssue>> {
        if (!this.config.partIssueServiceUrl) {
            return of([]);
        } else {
            return this.getAll("", params)
        }
    }

    // === override for multipe sites === //
    get requestUrl(): string {
        return this.config.partIssueServiceUrl + this.requestMapping;
    }


    getIssues(params?: any): Observable<any> {
        let url = this.config.serviceUrl + '/issues/' + this.config.siteId + '/parts';
    
        if (params && params.jobId) {
            url += '?Id=' + params.jobId;
        }
    
        return this.httpClient.get<any>(url);
    }
    
}
