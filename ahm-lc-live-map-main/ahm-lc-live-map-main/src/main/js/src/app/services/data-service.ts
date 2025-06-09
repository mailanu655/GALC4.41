import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ConfigService } from './config.service';


@Injectable({
    providedIn: 'root'
})
export class DataService {

    departmentCache: Map<string, any> = new Map<string, any>();
    lineCache: Map<string, any> = new Map<string, any>();
    processPointCache: Map<string, any> = new Map<string, any>();

    constructor(private httpClient: HttpClient, public config: ConfigService) {
    }

    // === load cache === //
    loadCache() {
        this.clearCache();
        this.loadProcessPoints();
        this.loadLines();
        this.loadDepartments();
    }

    loadCacheIfEmpty() {
        if (this.departmentCache.size === 0 || this.lineCache.size === 0 || this.processPointCache.size === 0) {
            this.loadCache();
        }
    }

    clearCache() {
        this.departmentCache.clear();
        this.lineCache.clear();
        this.processPointCache.clear();
    }

    // === data api === //
    //TODO - verify
    findProduct(id: string) {
        return this.get(this.serviceUrl + "/products/" + id);
    }

    selectProducts() {
        return this.get(this.serviceUrl + "/products");
    }

    selectHistoryByProductId(productId: string) {
        let url = this.serviceUrl + "/history/productId/" + productId;
        return this.get(url);
    }

    selectLots() {
        return this.get(this.serviceUrl + "/lots");
    }

    selectUpcomingLots() {
        return this.get(this.serviceUrl + "/upcominglots");
    }

    selectDefects(): Observable<any> {
        return this.get(this.serviceUrl + "/defects");
    }

    selectDefectsByProductId(productId: string) {
        return this.get(this.serviceUrl + "/defects/productId/" + productId);
    }

    selectHolds(): Observable<any> {
        return this.get(this.serviceUrl + "/holds");
    }

    selectHoldsByProductId(productId: string) {
        return this.get(this.serviceUrl + "/holds/productId/" + productId);
    }

    selectProcessPoints() {
        return this.get(this.serviceUrl + "/processpoints");
    }

    selectLines() {
        return this.get(this.serviceUrl + "/lines");
    }

    selectDepartments() {
        return this.get(this.serviceUrl + "/departments");
    }

    selectFactoryNews() {
        return this.get(this.serviceUrl + "/factorynews");
    }
    // === rest api === //
    get(url: string, params?: any): Observable<any> {
        if (params) {
            return this.httpClient.get<any>(url, { params: params });
        } else {
            return this.httpClient.get<any>(url);
        }
    }

    // === caching === //
    loadProcessPoints() {
        this.selectProcessPoints().subscribe(res => {
            let list = res;
            for (let item of list) {
                this.processPointCache.set(item.id, item);
            }
        });
    }

    loadLines() {
        this.selectLines().subscribe(res => {
            let list = res;
            for (let item of list) {
                this.lineCache.set(item.id, item);
            }
        });
    }

    loadDepartments() {
        this.selectDepartments().subscribe(res => {
            let list = res;
            for (let item of list) {
                this.departmentCache.set(item.id, item);
            }
        });
    }
    // === get/set === //
    get serviceUrl() {
        let url = this.config.siteServiceUrl;
        return url;
    }

    geLineByLineCode(lineCode: string): any {
        let line: any = null;
        for (let line of Array.from(this.lineCache.values())) {
            if (line && line['code'] === lineCode) {
                return line;
            }
        }
        return line;
    }
}