import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DatePipe, formatDate } from '@angular/common';
import { DcmsDto } from '../models/dcms-dto';
import { ModelPart } from '../models/model-part';
import { DesignChangeRule } from '../models/design-change-rule';
import { ConfigService } from './config.service';
import { SecurityService } from './security.service';

@Injectable({
  providedIn: 'root'
})
export class RuleAssignmentService {
  public modelPart: ModelPart = new ModelPart();

  constructor(private httpClient: HttpClient,
              private datePipe: DatePipe,
              private config:ConfigService, private security:SecurityService) { }

  getDesignChange(designChangeNo: String, plantLocCode: String): Observable<DcmsDto[]> {
    return this.httpClient.get<DcmsDto[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getDesignChange?plantLocCode='+plantLocCode+'&designChangeNumber='+designChangeNo,{headers: this.security.headers});
  }

  //saveModelPart(item: ModelPart, cbuReflash: boolean, scrapParts: boolean, stragglerInterChangeable: boolean): Observable<any> {
  saveModelPart(item: ModelPart): Observable<any> {
/*    this.modelPart.modelPartId = null;
    this.modelPart.productSpecWildcard = item.dcmsModelCode;
    this.modelPart.letSystemName = item.letSystemName;
    this.modelPart.dcPartNumber = item.designChangePartNumber;
    this.modelPart.active = 'INACTIVE';
    this.modelPart.dcClass = item.designChangeClass;
    this.modelPart.dcEffBegDate = item.effectiveBeginDate;
    this.modelPart.dcNumber = item.designChangeClass;    
    this.modelPart.interchangeable = stragglerInterChangeable;
    this.modelPart.reflash = cbuReflash;
    this.modelPart.scrapParts = scrapParts;*/
    
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/saveModelPart', item,{headers: this.security.headers});
  }

  validateRuleSelection(dcClass: string, reflash: boolean, interchangable: boolean, scrapParts: boolean): Observable<boolean> {
    let reflashValue = 0;
    let interchangableValue = 0;
    let scrapPartsValue = 0;
    if(reflash == true) {
      reflashValue = 1;
    }
    if(interchangable == true) {
      interchangableValue = 1;
    }
    if(scrapParts == true) {
      scrapPartsValue = 1;
    }
    return this.httpClient.get<boolean>(this.config.serviceUrl+'/RestWeb/v2/VinBom/validateRuleSelection?dcClass='+dcClass+'&reflash='+reflashValue+'&interchangable='+interchangableValue+'&scrapParts='+scrapPartsValue,{headers: this.security.headers});
  }

  validateRuleSelection2(dcClass: string, reflash: boolean, interchangable: boolean, scrapParts: boolean): Promise<boolean> {
    let reflashValue = 0;
    let interchangableValue = 0;
    let scrapPartsValue = 0;
    if(reflash == true) {
      reflashValue = 1;
    }
    if(interchangable == true) {
      interchangableValue = 1;
    }
    if(scrapParts == true) {
      scrapPartsValue = 1;
    }
    let data = this.httpClient.get<boolean>(this.config.serviceUrl+'/RestWeb/v2/VinBom/validateRuleSelection?dcClass='+dcClass+'&reflash='+reflashValue+'&interchangable='+interchangableValue+'&scrapParts='+scrapPartsValue,{headers: this.security.headers})
                                .toPromise();
    console.log(data);
    return data;
  }

  validateRuleSelection1(dcClass: string, reflash: boolean, interchangable: boolean, scrapParts: boolean) {
    let reflashValue = 0;
    let interchangableValue = 0;
    let scrapPartsValue = 0;
    if(reflash == true) {
      reflashValue = 1;
    }
    if(interchangable == true) {
      interchangableValue = 1;
    }
    if(scrapParts == true) {
      scrapPartsValue = 1;
    }
    return this.httpClient.get<boolean>(this.config.serviceUrl+'/RestWeb/v2/VinBom/validateRuleSelection?dcClass='+dcClass+'&reflash='+reflashValue+'&interchangable='+interchangableValue+'&scrapParts='+scrapPartsValue,{headers: this.security.headers}).toPromise();
  }

  getDesignChangeRules(): Observable<DesignChangeRule[]>{
    return this.httpClient.get<DesignChangeRule[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getAvailableDesignChangeRules',{headers: this.security.headers});
  }

  getPlantCodeList():Observable<String[]>{
    return this.httpClient.get<String[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getDcmsPlantCodes',{headers: this.security.headers})
  }
 }
