import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export class Entity<K> {
    id: K;
    objVersion: number;
    createTimestamp: Date;
    updateTimestamp: Date;
}

@Injectable({
    providedIn: 'root'
})
export class RestReadService<T, K> {

    serviceUrl: string = 'http://127.0.1:8080';
    requestMapping: string = '';
    type: any;

    constructor(private _httpClient: HttpClient) {
    }

    // === find === //
    find(id: K): Observable<T> {
        return this.httpClient.get<T>(this.requestUrl + '/' + id);
    }

    findAll(params?: any): Observable<Array<T>> {
        return this.getAll("", params)
    }

    findById(ids: Array<K>): Observable<Array<T>> {
        let options = {};
        options['params'] = ids;
        return this.httpClient.get<Array<T>>(this.requestUrl + '/id', options);
    }

    findByIdPost(ids: Array<K>): Observable<Array<T>> {
        return this.httpClient.post<Array<T>>(this.requestUrl + '/id', ids);
    }

    findPage(pageIx: number, pageSize: number, dir: string, sort: string, params: any): Observable<Array<T>> {
        if (!params) {
            params = {};
        }
        this.putIfNotEmpty(params, 'ix', pageIx)
        this.putIfNotEmpty(params, 'size', pageSize)
        this.putIfNotEmpty(params, 'dir', dir)
        this.putIfNotEmpty(params, 'sort', sort)
        let url: string = "/page";
        let res = this.getAll(url, params);
        return res;
    }

    // === scalar api === //
    count(params?: any): Observable<number> {
        let options = {};
        let url = this.requestUrl + '/count';
        if (params) {
            return this.httpClient.get<number>(url, { params: params });
        } else {
            return this.httpClient.get<number>(url, options);
        }
    }

    // === generic get api === //
    getAll(path: string, params?: any): Observable<Array<T>> {
        let url = this.requestUrl;
        if (path) {
            url += path;
        }
        if (params) {
            return this.httpClient.get<Array<T>>(url, { params: params });
        } else {
            return this.httpClient.get<Array<T>>(url);
        }
    }

    get(path: string, params?: any): Observable<any> {
        let url = this.requestUrl;
        if (path) {
            url += path;
        }
        if (params) {
            return this.httpClient.get<any>(url, { params: params });
        } else {
            return this.httpClient.get<any>(url);
        }
    }

    getByUrlString(path: string, params?: Observable<Array<T>>) {
        let url = this.requestUrl;
        if (path) {
            url += path;
        }
        if (params) {
            url += `?${this.toQueryString(params)}`;
        }
        return this.httpClient.get<Array<T>>(url);
    }

    // === utility === //
    public putIfNotEmpty(params: any, key: string, value: any) {
        if (params && key && (value || value === 0 || value === false)) {
            params[key] = value;
        }
    }

    toQueryString(paramsObject: any) {
        return Object
            .keys(paramsObject)
            .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(paramsObject[key])}`)
            .join('&');
    }
    // === get/set === //
    get requestUrl(): string {
        return this.serviceUrl + this.requestMapping;
    }

    get httpClient() {
        return this._httpClient;
    }
}
