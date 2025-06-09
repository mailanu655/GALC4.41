import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { lastValueFrom, Observable } from 'rxjs';

export class Entity<K> {
    id!: K;
    objVersion!: number;
    createTimestamp!: Date;
    updateTimestamp!: Date;
}

@Injectable({
    providedIn: 'root'
})
export class RestService<T, K> {

    serviceUrl: string = 'http://127.0.1:8080';
    requestMapping: string = '';
    type: any;

    constructor(private _httpClient: HttpClient) {
    }

    // === crud === //
    async find(id: K): Promise<T> {
        const result$ = this.httpClient.get<T>(this.requestUrl + '/' + id);
        let result = await lastValueFrom(result$);
        return result;
    }

    async findByPath(path: string, params?: any): Promise<T> {
        let url = this.requestUrl;
        if (path) {
            url = url + path;
        }
        const result$ = this.httpClient.get<T>(url, { params: params });
        let result = await lastValueFrom(result$);
        return result;
    }

    async create(body: T): Promise<any> {
        const result$ = this.httpClient.post<any>(this.requestUrl + "/create", body);
        let result = await lastValueFrom(result$);
        return result;
    }

    async update(id: K, body: T): Promise<any> {
        const result$ = this.httpClient.put<any>(this.requestUrl + '/' + id, body);
        let result = await lastValueFrom(result$);
        return result;
    }

    async delete(id: K): Promise<any> {
        const result$ = this.httpClient.delete<T>(this.requestUrl + '/' + id);
        let result = await lastValueFrom(result$);
        return result;
    }

    // === select === //
    async select(params?: any): Promise<Array<T>> {
        let url = this.requestUrl;
        const result$ = this.httpClient.get<Array<T>>(url, { params: params });
        let result = await lastValueFrom(result$);
        return result;
    }

    async selectByPath(path: string, params?: any): Promise<Array<T>> {
        let url = this.requestUrl;
        if (path) {
            url = url + path;
        }
        const result$ = this.httpClient.get<Array<T>>(url, { params: params });
        let result = await lastValueFrom(result$);
        return result;
    }

    async selectPage(params: any, ix: number, size: number, sort: string, direction: string): Promise<any> {
        if (!params) {
            params = {};
        }
        this.putIfNotEmpty(params, 'ix', ix)
        this.putIfNotEmpty(params, 'size', size)
        this.putIfNotEmpty(params, 'sort', sort)
        this.putIfNotEmpty(params, 'direction', direction)
        let url: string = this.requestUrl + "/page";
        const result$ = this.httpClient.get<Array<T>>(url, { params: params });
        let result = await lastValueFrom(result$);
        return result;
    }

    async selectPagePost(params: any, ix: number, size: number, sort: string, direction: string): Promise<any> {
        if (!params) {
            params = {};
        }
        this.putIfNotEmpty(params, 'ix', ix)
        this.putIfNotEmpty(params, 'size', size)
        this.putIfNotEmpty(params, 'sort', sort)
        this.putIfNotEmpty(params, 'direction', direction)
        let url: string = this.requestUrl + "/page";
        const result$ = this.httpClient.post<any>(url, params);
        let result = await lastValueFrom(result$);
        return result;
    }

    async selectProperty(params?: any): Promise<Array<any>> {
        let url = this.requestUrl + "/property";
        const result$ = this.httpClient.get<Promise<Array<any>>>(url, { params: params });
        let result = await lastValueFrom(result$);
        return result;
    }

    async selectMap(params?: any): Promise<Array<any>> {
        let url = this.requestUrl + "/map";
        const result$ = this.httpClient.get<Promise<Array<any>>>(url, { params: params });
        let result = await lastValueFrom(result$);
        return result;
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

    // === cud validation === //
    async validateCreate(body: T): Promise<any> {
        const result$ = this.httpClient.post<any>(this.requestUrl + "/validate", body);
        let result = await lastValueFrom(result$);
        return result;
    }

    async validateUpdate(id: K, body: T): Promise<any> {
        const result$ = this.httpClient.put<any>(this.requestUrl + '/' + id + "/validate", body);
        let result = await lastValueFrom(result$);
        return result;
    }

    async validateDelete(id: K): Promise<any> {
        const result$ = this.httpClient.delete<any>(this.requestUrl + '/' + id + "/validate");
        let result = await lastValueFrom(result$);
        return result;
    }
    // === utility === //
    addLabel(item: any) {
        if (!item) {
            return;
        }
        let id = item['id'];
        let name = item['name'];
        let label = id;
        if (name) {
            label = name + " (" + id + ")";
        } else {
            label = id;
        }
        item['label'] = label;
    }

    // === generic get api === //
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