import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ModelPartApproval } from '../models/model-part-approval';
import { DatePipe, formatDate } from '@angular/common';

import { ModelPartLotDto } from '../models/model-part-lot-dto';
import { ModelPartLotFilterDto } from '../models/model-part-lot-filter-dto';
import { PreProductionLot } from '../models/pre-production-lot';
import { ConfigService } from './config.service';
import { SecurityService } from './security.service';

@Injectable({
  providedIn: 'root'
})
export class RuleLotMaintenanceService {

  constructor(private httpClient: HttpClient,
    private datePipe: DatePipe,
    private config:ConfigService,private security:SecurityService) { }

  getAvailableLotAssignments(): Observable<ModelPartLotDto[]> {
    return this.httpClient.get<ModelPartLotDto[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getAvailableLotAssignments',{headers: this.security.headers});
  }

  getLotAssignmentFilters(): Observable<ModelPartLotFilterDto[]> {
    return this.httpClient.get<ModelPartLotFilterDto[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getLotAssignmentFilters',{headers: this.security.headers});
  }

    getFilteredLotAssignments(dcNumber :string, systemName:string,  partNumber:string): Observable<ModelPartLotDto[]> {
    return this.httpClient.get<ModelPartLotDto[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/filterLotAssignments?systemName='+systemName+'&dcNumber='+dcNumber+'&partNumber='+partNumber,{headers: this.security.headers});
  }

  deleteModelPartAssignment(modelLotPart: ModelPartLotDto): Observable<any> {
    let modelPartId = modelLotPart.modelPartId;
    let planCode = modelLotPart.planCode;
    let productionLot = modelLotPart.startingProductionLot;
    
    return this.httpClient.delete<ModelPartLotDto[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/deleteModelPartAssignment?modelPartId='+modelPartId+'&planCode='+planCode+'&productionLot='+productionLot, {headers: this.security.headers});
  }

}
