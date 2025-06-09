import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { ConfigService } from './config.service';
import { GALC_URL, ProductType } from '../constants';

@Injectable({
    providedIn: 'root'
})
export class GalcService {

    // serviceUrl: string = 'http://127.0.1:8082';
    serviceUrl: string ;
    requestMapping: string = '';

    constructor(private httpClient: HttpClient, private config: ConfigService) {
        this.serviceUrl = GALC_URL! || this.serviceUrl;
        this.requestMapping = "/RestWeb"
    }

    async selectDepartments() {
        let result = await this.select("/DivisionDao/findAll");
        return result;
    }

    async selectLines() {
        let result = await this.select("/LineDao/findAll");
        return result;
    }

    async selectProcessPoints() {
        let result = await this.select("/ProcessPointDao/findAll");
        return result;
    }

    async selectFrameSpecs() {
        let result = await this.select("/FrameSpecDao/findAllActiveProductSpecCodesOnly?PRODUCT_TYPE_NOT_USED");
        let specCodes: any[] = [];
        result.forEach((item: any) => {
            specCodes.push(item.productSpecCode);
        });
        return specCodes;
    }

    async selectEngineSpecs() {
        let result = await this.select("/EngineSpecDao/findAllProductSpecCodes");
        return result;
    }

    async selectMpbnSpecs() {
        let result = await this.select("/MbpnDao/findAllProdSpecCode");
        return result;
    }

    async findFrame(id: string) {
        let result = await this.select("/FrameDao/findByKey?" + id);
        return this.assembleFrame(result);
    }

    async findEngine(id: string) {
        let result = await this.select("/EngineDao/findByKey?" + id);
        return this.assembleEngine(result);
    }

    async findMbpn(id: string) {
        let result = await this.select("/MbpnProductDao/findByKey?" + id);
        return this.assembleMbpn(result);
    }

    // === generic api === //
    async select(path: string = '', params?: any): Promise<any> {
        let url = this.requestUrl;
        if (path) {
            url += path;
        }
        const result$ = this.httpClient.get<any>(url, { params: params });
        let result = await lastValueFrom(result$);
        return result;
    }

    // === assembly === //
    assembleFrame(source: any) {
        if (!source) {
            return source;
        }
        let product = {
            id: source.productId.trim(),
            specCode: source.productSpecCode.trim(),
            kdLot: source.kdLotNumber.trim(),
            seqNumber: source.afOnSequenceNumber,
            processPointId: source.lastPassingProcessPointId.trim(),
            hold: source.autoHoldStatus,
            productType: ProductType.FRAME
        }
        return product;
    }

    assembleEngine(source: any) {
        if (!source) {
            return source;
        }
        let product = {
            id: source.productId.trim(),
            specCode: source.productSpecCode.trim(),
            kdLot: source.kdLotNumber.trim(),
            processPointId: source.lastPassingProcessPointId.trim(),
            hold: source.autoHoldStatus,
            productType: ProductType.ENGINE
        }
        return product;
    }

    assembleMbpn(source: any) {
        if (!source) {
            return source;
        }
        let product = {
            id: source.productId.trim(),
            specCode: source.currentProductSpecCode.trim(),
            productType: ProductType.MBPN
        }
        return product;
    }

    // === get/set === //
    get requestUrl(): string {
        return this.serviceUrl + this.requestMapping;
    }
}
