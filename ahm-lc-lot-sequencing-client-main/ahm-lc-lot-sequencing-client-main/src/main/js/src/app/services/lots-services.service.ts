import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {
  GET_LOTS_URL,
  FREEZE_LOTS_URL,
  HOLD_LOTS_URL,
  RELEASE_LOTS_URL,
  UNFREEZE_LOTS_URL,
  GET_ADD_NEW_CODE_URL,
  FIND_ALL_ACTIVE_PRODUCT_SPEC_CODES_ONLY_URL,
  GET_PRODUCTS_URL,
  GET_BUILD_COMMENTS_URL,
  POST_SAVE_LOTS_URL,
  POST_SAVE_LOT_URL,
  GET_PLAN_CODES_URL,
  GET_HOLD_LOTS_URL,
  GET_PRODUCT_SEND_STATUSES_URL,
  POST_SAVE_PRODUCT_SEND_STATUS_URL,
  DOWNLOAD_LOTS_URL,
  BUILD_AHEAD_LOTS_URL,
  GET_PROCESS_LOCATIONS_URL,
  SEND_TO_WELD_ON_URL,
  MOVE_LOT_BEHINND_LOT_URL
} from '../constants';
import { LotProductModel, WeldScheduleModel } from '../models';
import { allProductSpecData } from '../mocks';
import { Observable, of } from 'rxjs';


@Injectable()
export class LotsService {
  constructor(private http: HttpClient) {}

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'text/plain',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods':'*',
      'Accept': '*/*',
      'username': JSON.parse(localStorage.getItem('userDetails'))['username'],
    }),
  };

  httpPostOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods':'*',
      'Accept': '*/*',
      'username': JSON.parse(localStorage.getItem('userDetails'))['username'],
    }),
  };

  getUnfrozenLots(selectedPlanCode: string, selectedLocation: string) {
    return this.http.get<any>(GET_LOTS_URL, {
      params: {
        isFrozenSchedule: 'false',
        planCode: selectedPlanCode,
        processLocation: selectedLocation
      },
      ...this.httpOptions 
    });
  }

  getFrozenLots(selectedPlanCode: string, selectedLocation: string) {
    return this.http.get<any>(GET_LOTS_URL, {
      params: {
        isFrozenSchedule: 'true',
        planCode: selectedPlanCode,
        processLocation: selectedLocation
      },
      ...this.httpOptions
    });
  }

  getHoldLots(selectedPlanCode: string, selectedLocation: string) {
    return this.http.get<any>(GET_HOLD_LOTS_URL, {
      params: {
        planCode: selectedPlanCode,
        processLocation: selectedLocation
      },
      ...this.httpOptions
    });
  }

  getAddNewCode(selectedPlanCode: string, selectedLocation: string) {
    return this.http.get<any>(GET_ADD_NEW_CODE_URL, {
      params: {
        planCode: selectedPlanCode,
        processLocation: selectedLocation
      },
      ...this.httpOptions
    });
  }

  getAllActiveProductSpecCodesOnly() {
    const criteria = [{ 'java.lang.String': 'FRAME' }];

    return this.http.post<any>(
      FIND_ALL_ACTIVE_PRODUCT_SPEC_CODES_ONLY_URL,
      criteria,
      this.httpOptions
    );
  }

  getLotsByProductionLot(productionLotId: string, lotSequence: number) {
    return this.http.get<any>(GET_PRODUCTS_URL, {
      params: {
        lot: productionLotId,
        seq: lotSequence
      },
      ...this.httpOptions
    });
  }

  getBuildComments() {
    return this.http.get<any>(GET_BUILD_COMMENTS_URL, {
      ...this.httpOptions
    });
  }

  getProductSendStatuses() {
    return this.http.get<any>(GET_PRODUCT_SEND_STATUSES_URL, {
      ...this.httpOptions
    });
  }

  getPlanCodes(): Observable<string[]> {
    return this.http.get<any>(GET_PLAN_CODES_URL, {
      ...this.httpOptions
    });
  }

  getProcessLocations(): Observable<string[]> {
    return this.http.get<any>(GET_PROCESS_LOCATIONS_URL, {
      ...this.httpOptions
    });
  }

  downloadLotData(selectedPlanCode: string,selectedProcessLocation: string) {
    return this.http.get<any>(DOWNLOAD_LOTS_URL, {
      params: {
        planCode: selectedPlanCode,
        processLocation: selectedProcessLocation
      },
      ...this.httpOptions
    });
  }

  postLotSaveAll(products: WeldScheduleModel[]) {
    return this.http.post<LotProductModel[]>(
      POST_SAVE_LOTS_URL,
      products,
      this.httpPostOptions
    );
  }

  postLotSave(product: WeldScheduleModel) {
    return this.http.post<LotProductModel[]>(
      POST_SAVE_LOT_URL,
      product,
      this.httpPostOptions
    );
  }

  postSaveProductSendStatus(lotProduct: LotProductModel) {
    return this.http.post<LotProductModel[]>(
      POST_SAVE_PRODUCT_SEND_STATUS_URL,
      lotProduct,
      this.httpPostOptions
    );
  }

  postFreezeLots(products: WeldScheduleModel[]) {
    return this.http.post<LotProductModel[]>(
      FREEZE_LOTS_URL,
      products,
      this.httpPostOptions
    );
  }

  postBuildAheadLot(product: WeldScheduleModel) {
    return this.http.post<LotProductModel>(
      BUILD_AHEAD_LOTS_URL,
      product,
      this.httpPostOptions
    );
  }
  
  postHoldLot(product: WeldScheduleModel) {
    return this.http.post<LotProductModel>(
      HOLD_LOTS_URL,
      product,
      this.httpPostOptions
    );
  }

  getMoveLotBehindLot(moveLotId: string, behindLotId: string) {
    return this.http.get<any>(MOVE_LOT_BEHINND_LOT_URL, {
      params: {
        moveLot: moveLotId,
        behindLot: behindLotId
      },
      ...this.httpOptions 
    });
  }

  postReleaseLot(product: WeldScheduleModel) {
    return this.http.post<LotProductModel>(
      RELEASE_LOTS_URL,
      product,
      this.httpPostOptions
    );
  }

  postUnfreezeLots(products: WeldScheduleModel[]) {
    return this.http.post<LotProductModel[]>(
      UNFREEZE_LOTS_URL,
      products,
      this.httpPostOptions
    );
  }

  postSendToWeldOn(products: any) {
    return this.http.post<LotProductModel[]>(
      SEND_TO_WELD_ON_URL,
      products,
      this.httpPostOptions
    );
  }


  getYearData(): string[] {
    const yearSet = new Set();
    allProductSpecData.forEach((item) => {
      if (item.modelYearCode) {
        yearSet.add(item.modelYearCode);
      }
    });
    const optionsArr = Array.from(yearSet);
    optionsArr.sort();
    const dataArr = ['All', ...optionsArr];
    return dataArr as string[];
  }

  getAllProductData(optionKey: string) {
    const result = {} as any;

    const tmpSet = new Set();
    allProductSpecData.forEach((item) => {
      if (item[optionKey]) {
        tmpSet.add(item[optionKey]);
      }
    });
    const optionsArr = Array.from(tmpSet);
    optionsArr.sort();
    const allArr = ['All', ...optionsArr];
    result[optionKey + 's'] = allArr;
    return result;
  }

  getModelData(modelYearCode: string): string[] {
    if (!modelYearCode) return [];

    const modelCodeSet = new Set();
    const filteredAllProductSpecData =
      modelYearCode !== 'All'
        ? allProductSpecData.filter(
            (item) => item.modelYearCode === modelYearCode
          )
        : [];
    filteredAllProductSpecData.forEach((item) => {
      if (item.modelCode) {
        modelCodeSet.add(item.modelCode);
      }
    });
    const optionsArr = Array.from(modelCodeSet);
    optionsArr.sort();
    const dataArr = ['All', ...optionsArr];
    return dataArr as string[];
  }

  getModelTypeData(modelYearCode: string, modelCode: string): string[] {
    if (!modelYearCode || !modelCode) return [];

    const modelTypeCodeSet = new Set();
    let filteredAllProductSpecData =
      !!modelYearCode &&
      modelYearCode !== 'All' &&
      !!modelCode &&
      modelCode !== 'All'
        ? allProductSpecData.filter(
            (item) =>
              item.modelYearCode === modelYearCode &&
              item.modelCode === modelCode
          )
        : [];
    filteredAllProductSpecData.forEach((item) => {
      if (item.modelTypeCode) {
        modelTypeCodeSet.add(item.modelTypeCode);
      }
    });
    const optionsArr = Array.from(modelTypeCodeSet);
    optionsArr.sort();
    const dataArr = ['All', ...optionsArr];
    return dataArr as string[];
  }

  getOptionData(modelYearCode: string, modelCode: string, modelTypeCode: string): string[] {
    if (!modelYearCode || !modelCode || !modelTypeCode) return [];

    const modelOptionCodeSet = new Set();
    let filteredAllProductSpecData =
      modelYearCode !== 'All' &&
      modelCode !== 'All' && modelTypeCode !== 'All'
        ? allProductSpecData.filter(
            (item) =>
              item.modelYearCode === modelYearCode &&
              item.modelCode === modelCode &&
              item.modelTypeCode === modelTypeCode
          )
        : [];
    filteredAllProductSpecData.forEach((item) => {
      if (item.modelOptionCode) {
        modelOptionCodeSet.add(item.modelOptionCode);
      }
    });
    const optionsArr = Array.from(modelOptionCodeSet);
    optionsArr.sort();
    const dataArr = ['All', ...optionsArr];
    return dataArr as string[];
  }

  getExtColorData(modelYearCode: string, modelCode: string, modelTypeCode: string, modelOptionCode: string): string[] {
    if (!modelYearCode || !modelCode || !modelTypeCode || !modelOptionCode) return [];

    const extColorCodeSet = new Set();
    let filteredAllProductSpecData =
      modelYearCode !== 'All' &&
      modelCode !== 'All' && modelTypeCode !== 'All' && modelOptionCode !== 'All'
        ? allProductSpecData.filter(
            (item) =>
              item.modelYearCode === modelYearCode &&
              item.modelCode === modelCode &&
              item.modelTypeCode === modelTypeCode && 
              item.modelOptionCode === modelOptionCode
          )
        : [];
    filteredAllProductSpecData.forEach((item) => {
      if (item.extColorCode) {
        extColorCodeSet.add(item.extColorCode);
      }
    });
    const optionsArr = Array.from(extColorCodeSet);
    optionsArr.sort();
    const dataArr = ['All', ...optionsArr];
    return dataArr as string[];
  }

  getIntColorCodeData(modelYearCode: string, modelCode: string, modelTypeCode: string, modelOptionCode: string, extColorCode: string): string[] {
    if (!modelYearCode || !modelCode || !modelTypeCode || !modelOptionCode || !extColorCode) return [];

    const intColorCodeSet = new Set();
    let filteredAllProductSpecData =
      modelYearCode !== 'All' &&
      modelCode !== 'All' && modelTypeCode !== 'All' && modelOptionCode !== 'All' && extColorCode !== 'All'
        ? allProductSpecData.filter(
            (item) =>
              item.modelYearCode === modelYearCode &&
              item.modelCode === modelCode &&
              item.modelTypeCode === modelTypeCode && 
              item.modelOptionCode === modelOptionCode &&
              item.extColorCode === extColorCode
          )
        : [];
    filteredAllProductSpecData.forEach((item) => {
      if (item.intColorCode) {
        intColorCodeSet.add(item.intColorCode);
      }
    });
    const optionsArr = Array.from(intColorCodeSet);
    optionsArr.sort();
    const dataArr = ['All', ...optionsArr];
    return dataArr as string[];
  }
}
