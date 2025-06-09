import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { lastValueFrom } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class CheckService {

    // serviceUrl: string = 'http://127.0.1:8080';
    serviceUrl: string = 'http://qmap1was.ham.am.honda.com:8005';
    requestMapping: string = '';

    constructor(private httpClient: HttpClient, private config: ConfigService) {
        this.serviceUrl = this.config.serviceUrl!;
        this.requestMapping = "/checks"
    }


    async select(path: string = '', params?: any): Promise<Array<string>> {
        let url = this.requestUrl;
        if (path) {
            url += path;
        }
        const result$ = this.httpClient.get<Array<string>>(url, { params: params });
        let result = await lastValueFrom(result$);
        return result;
    }

    // === get/set === //
    get requestUrl(): string {
        return this.serviceUrl + this.requestMapping;
    }
}
