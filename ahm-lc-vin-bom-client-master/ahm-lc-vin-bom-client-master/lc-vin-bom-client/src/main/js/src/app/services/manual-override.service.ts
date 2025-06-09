import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DatePipe, formatDate } from '@angular/common';

import { ManualOverride } from '../models/manual-override';
import { VinPartApproval } from '../models/vin-part-approval';
import { VinPartApprovalStatus } from '../models/vin-part-approval-status';
import { VinPartDto } from '../models/vin-part-dto';
import { VinPartFilterDto } from '../models/vin-part-filter-dto';
import { VinPartId } from '../models/vin-part-id';
import { ConfigService } from './config.service';
import {SecurityService} from './security.service';
import { VinBomPart } from '../models/vin-bom-part';

@Injectable({
  providedIn: 'root'
})
export class ManualOverrideService {
  vinPartApproval: VinPartApproval = new VinPartApproval();
  vinPartApprovalStatus: VinPartApprovalStatus = new VinPartApprovalStatus();

  constructor(private httpClient: HttpClient,
              private datePipe: DatePipe, private config:ConfigService, private security:SecurityService) { }

  getVinPartAndStatus(): Observable<VinPartDto[]> {
    
    return this.httpClient.get<VinPartDto[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getVinPartAndStatus',{headers: this.security.headers});
  }

  getVinPartFilters(): Observable<VinPartFilterDto[]> {
    
    return this.httpClient.get<VinPartFilterDto[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getVinPartFilters',{headers: this.security.headers});
  }
  
  getFilteredVinParts(productId :string, systemName:string,  partNumber:string, productionLot:string): Observable<VinPartDto[]> {
    
    return this.httpClient.get<VinPartDto[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/filterVinParts?systemName='+systemName+'&productId='+productId+'&partNumber='+partNumber+'&productionLot='+productionLot,{headers: this.security.headers});
  }

  findDistinctPartNumberBySystemName(letSystemName: string, productId: string): Observable<VinBomPart[]> {
    return this.httpClient.get<VinBomPart[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/findDistinctPartNumberBySystemName?letSystemName='+letSystemName+'&productId='+productId,{headers: this.security.headers});
  }

  changeShippableStatus(item: VinPartDto): Observable<any> {
    var currentDateTime = (this.datePipe.transform(new Date(),"yyyy-MM-dd HH:mm:ss").toString()).replace(' ', 'T');

    this.vinPartApproval.approveStatus = 'PENDING';
    this.vinPartApproval.currentDcPartNumber= item.dcPartNumber;
    this.vinPartApproval.currentShipStatus=item.shipStatus;
    this.vinPartApproval.letSystemName=item.letSystemName
    this.vinPartApproval.newDcPartNumber=item.dcPartNumber
    this.vinPartApproval.newShipStatus= !item.shipStatus;
    this.vinPartApproval.productId=item.productId
    this.vinPartApproval.requestAssociateNumber=this.security.getUser(); // Get it from logged is user
    this.vinPartApproval.requestTimestamp=currentDateTime;
    //this.vinPartApproval.interchangeable=true;

    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/saveVinPartApproval', this.vinPartApproval,{headers: this.security.headers});
  }

  save(originalVinPart: VinPartDto, newDcPart: string, isInterchangeable: boolean): Observable<any> {
    var currentDateTime = (this.datePipe.transform(new Date(),"yyyy-MM-dd HH:mm:ss").toString()).replace(' ', 'T');


    this.vinPartApproval.approveStatus= 'PENDING'; 
    this.vinPartApproval.currentDcPartNumber = originalVinPart.dcPartNumber;
    this.vinPartApproval.currentShipStatus = originalVinPart.shipStatus;
    this.vinPartApproval.letSystemName = originalVinPart.letSystemName
    this.vinPartApproval.newDcPartNumber= newDcPart;
    this.vinPartApproval.newShipStatus= originalVinPart.shipStatus;
    this.vinPartApproval.productId = originalVinPart.productId
    this.vinPartApproval.requestAssociateNumber = this.security.getUser(); // Get it from logged is user
    this.vinPartApproval.requestTimestamp= currentDateTime;
    this.vinPartApproval.interchangeable=isInterchangeable;

    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/saveVinPartApproval', this.vinPartApproval,{headers: this.security.headers});
  }

  findDistinctPartNumberBySystemNameAndProductId(letSystemName: string, productId:string): Observable<VinPartId[]> {
    return this.httpClient.get<VinPartId[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getPartNumberByProductIdAndSystemName?systemName='+letSystemName+'&productId='+productId,{headers: this.security.headers});
  }
  delete(item: VinPartDto): Observable<any> {
   
    return this.httpClient.delete(this.config.serviceUrl+'/RestWeb/v2/VinBom/removeVinPart?productId='+item.productId+'&dcPartNumber='+item.dcPartNumber+'&letSystemName='+item.letSystemName, {headers: this.security.headers});
  
  }
  getCurrentDate() {
    return this.reformatDate(new Date());
  }

  reformatDate(date: Date): string {
    let dateString = formatDate(date, 'yyyy-MM-dd HH:MM:SS.sssZ', 'en-US');
    let localDate = this.convertUtcToLocalDate(date).toString();

    if(localDate.indexOf('Central Daylight Time') > 0) {
      dateString = dateString.replace(dateString.substring(dateString.lastIndexOf('-'), dateString.length), ' CDT');
    } else if(localDate.indexOf('Central Standard Time') > 0) {
      dateString = dateString.replace(dateString.substring(dateString.lastIndexOf('-'), dateString.length), ' CST');
    } else if(localDate.indexOf('Eastern Daylight Time') > 0) {
      dateString = dateString.replace(dateString.substring(dateString.lastIndexOf('-'), dateString.length), ' EDT');
    } else if(localDate.indexOf('Eastern Standard Time') > 0) {
      dateString = dateString.replace(dateString.substring(dateString.lastIndexOf('-'), dateString.length), ' EST');
    } else if(localDate.indexOf('Pacific Daylight Time') > 0) {
      dateString = dateString.replace(dateString.substring(dateString.lastIndexOf('-'), dateString.length), ' PDT');
    } else if(localDate.indexOf('Pacific Standard Time') > 0) {
      dateString = dateString.replace(dateString.substring(dateString.lastIndexOf('-'), dateString.length), ' PST');
    } else {
      dateString = dateString.replace(dateString.substring(dateString.lastIndexOf('-'), dateString.length), ' EST');
    }

    return dateString;
  }

  endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
  }

  convertUtcToLocalDate(val : Date) : Date {        
    var d = new Date(val);
    var localOffset = d.getTimezoneOffset() * 60000;
    var localTime = d.getTime() - localOffset;

    d.setTime(localTime);
    return d;
  }

  getProductDetails(seqNo: number): Observable<any> {    
console.log('this.config.prodDetailsUrl--- ', this.config.prodDtilsUrl);
const body: any = {};
body.seqNo=seqNo;
    return this.httpClient.post(this.config.prodDtilsUrl, body,{headers: this.security.headers});
  }
}

