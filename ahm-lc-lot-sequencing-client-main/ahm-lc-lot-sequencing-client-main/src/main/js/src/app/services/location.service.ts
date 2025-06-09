import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RestService } from '../lib/rest.service';
import { ConfigService } from './config.service';

@Injectable({
    providedIn: 'root'
})
export class LocationService extends RestService<any, string> {
    constructor(httpClient: HttpClient, private config: ConfigService) {
        super(httpClient);
        this.serviceUrl = this.config.serviceUrl!;
        this.requestMapping = "/locations"
    }
}