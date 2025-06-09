import { Injectable } from "@angular/core";
import { Api } from "../services/api";
import { SessionModel } from "./session.model";
import { ProductType } from "../constants";

@Injectable({
    providedIn: 'root'
})
export class ClientModel {

    preferences: any = {};

    checks: string[] = [];
    locationTypes: string[] = ['PROCESS_POINT', 'REPAIR_AREA', 'STORAGE', 'PARKING'];
    productTypes: string[] = ['FRAME', 'ENGINE', 'MBPN'];
    locations: any[] = [];

    departmentIx: Map<string, any> = new Map<string, any>();
    lineIx: Map<string, any> = new Map<string, any>();
    processPointIx: Map<string, any> = new Map<string, any>();
    specIx: Map<string, string[]> = new Map<string, string[]>();

    session: SessionModel = new SessionModel();

    constructor(private api: Api) {
        this.loadData();
    }

    // === load cache ===//
    async loadData() {
        await this.loadProductSpecs();
    }

    async loadProductSpecs() {
        let mbpnSpecs = await this.api.galc.selectMpbnSpecs();
        let engineSpecs = await this.api.galc.selectEngineSpecs();
        let frameSpecs = await this.api.galc.selectFrameSpecs();
        this.specIx.set(ProductType.MBPN, mbpnSpecs);
        this.specIx.set(ProductType.ENGINE, engineSpecs);
        this.specIx.set(ProductType.FRAME, frameSpecs);
    }
}