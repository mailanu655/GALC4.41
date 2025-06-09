import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DatePipe, formatDate } from '@angular/common';

import { VinBomPartSetDto } from '../models/vin-bom-part-set-dto';
import { VinBomPartDto } from '../models/vin-bom-part-dto';
import { VinBomPart } from '../models/vin-bom-part';
import { VinBomPartId } from '../models/vin-bom-part-id';
import { BeamPartInputDto } from '../models/beam-part-input-dto';
import { ConfigService } from './config.service';
import { SecurityService } from './security.service';

@Injectable({
  providedIn: 'root'
})
export class PartAssignmentService {

  vinBomPartDto: VinBomPartDto = new VinBomPartDto();
  vinBomPart: VinBomPart = new VinBomPart();
  beamPartInputDto: BeamPartInputDto = new BeamPartInputDto();
  id: VinBomPartId = new VinBomPartId();

  constructor(private httpClient: HttpClient,
              private datePipe: DatePipe, private config:ConfigService,private security:SecurityService) { }

  search(modelYear: string, modelCode: string, modelType: string, dcPartNo: string, systemName: string): Observable<any> {
    this.vinBomPartDto = new VinBomPartDto();
    this.vinBomPartDto.modelYearCode = modelYear;
    this.vinBomPartDto.modelCode = modelCode;
    this.vinBomPartDto.modelTypeCode = modelType;
    this.vinBomPartDto.dcPartNumber = dcPartNo;
    this.vinBomPartDto.letSystemName = systemName;
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/findAllPartsByFilter',this.vinBomPartDto,{headers: this.security.headers});
  }

  createVinBomRules(vinBomPartSetDto : VinBomPartSetDto):Observable<any>{
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/createVinBomRules', vinBomPartSetDto, {headers: this.security.headers});
  }

  removeBomPart(bomPart: VinBomPartDto): Observable<any> {
    this.id = new VinBomPartId();
    this.id.productSpecCode = bomPart.modelYearCode + bomPart.modelCode + bomPart.modelTypeCode;
    this.id.letSystemName = bomPart.letSystemName;
    this.id.dcPartNumber = bomPart.dcPartNumber;

    const header: HttpHeaders = this.security.headers;
    const httpOptions = {
        headers: header,
        body: this.id
    };

    return this.httpClient.delete<any>(this.config.serviceUrl+'/RestWeb/v2/VinBom/removePart', httpOptions);
  }

  addBomPart(modelYearInDialog: string, modelCodeInDialog: string, modelTypeInDialog: string, dcPartNoInDialog: string, systemNameInDialog: string): Observable<any> {
    const endDateString = '2099-12-31T00:00:00';

    var date = new Date();
    var beginDate = (this.datePipe.transform(date,"yyyy-MM-dd HH:mm:ss").toString()).replace(' ', 'T');

    this.vinBomPart = new VinBomPart();
    this.id = new VinBomPartId();
    this.id.productSpecCode = modelYearInDialog + modelCodeInDialog + modelTypeInDialog;
    this.id.letSystemName = systemNameInDialog;
    this.vinBomPart.id = new VinBomPartId();
    this.vinBomPart.id.productSpecCode = modelYearInDialog + modelCodeInDialog + modelTypeInDialog;
    this.vinBomPart.id.letSystemName = systemNameInDialog;
    this.vinBomPart.id.dcPartNumber = dcPartNoInDialog;
    this.vinBomPart.basePartNumber = dcPartNoInDialog;
    this.vinBomPart.description = dcPartNoInDialog;
    this.vinBomPart.effectiveBeginDate = beginDate;
    this.vinBomPart.effectiveEndDate = endDateString;

    
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/savePart', this.vinBomPart,{headers: this.security.headers});
  }

  updateBeamPartData(): Observable<any> {

    this.beamPartInputDto.plantLocCode=this.config.sites[this.config.siteId].gmqaplantName;
    this.beamPartInputDto.division='VQ';
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/updateBeamPartData', this.beamPartInputDto,{headers: this.security.headers});

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
}
