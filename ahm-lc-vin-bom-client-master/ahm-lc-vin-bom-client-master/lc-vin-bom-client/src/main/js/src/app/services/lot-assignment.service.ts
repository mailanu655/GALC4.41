import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ModelPartApproval } from '../models/model-part-approval';
import { DatePipe, formatDate } from '@angular/common';

import { ModelPartLotDto } from '../models/model-part-lot-dto';
import { PreProductionLot } from '../models/pre-production-lot';
import { ModelLot } from '../models/model-lot';
import { ModelLotId } from '../models/model-lot-id';
import { ConfigService } from './config.service';
import {SecurityService} from './security.service';

@Injectable({
  providedIn: 'root'
})
export class LotAssignmentService {
  lotList: ModelPartLotDto[]
  modelPartApproval: ModelPartApproval = new ModelPartApproval();
  modelLot: ModelLot = new ModelLot();
  id: ModelLotId = new ModelLotId();
  modelPartApprovalList: ModelPartApproval[]=[] ;
  

  constructor(private httpClient: HttpClient,
              private datePipe: DatePipe, private config:ConfigService, private security:SecurityService) { }

  getAvailableLotAssignments(): Observable<ModelPartLotDto[]> {
    return this.httpClient.get<ModelPartLotDto[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getAvailableLotAssignments?pendingOnly=true',{headers: this.security.headers});
  }

  getLines(plantCode: string): Observable<string[]> {
    return this.httpClient.get<string[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getLines?plantCode=' + plantCode,{headers: this.security.headers} );
  }

  getProductionLotsByProcessLocation(processLocation: string): Observable<PreProductionLot[]> {
    return this.httpClient.get<PreProductionLot[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getProductionLotsByProcessLocation',{headers: this.security.headers});
  }

  saveModelLot(item: ModelPartLotDto, selectedProdLot: string, planCode: string): Observable<any> {
    this.id.modelPartId = item.modelPartId;
    this.id.planCode = planCode;
    this.modelLot.id = this.id;
    this.modelLot.startingProductionLot = selectedProdLot;
   
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/saveModelLot', this.modelLot,{headers: this.security.headers});
  }

  saveModelPartApproval(item: ModelPartLotDto, selectedPlanCode:string,selectedProdLot: string, interchangeable: boolean, scrapParts: boolean, reflash: boolean): Observable<any> {
    var date = new Date();
    var beginDate = (this.datePipe.transform(date,"yyyy-MM-dd HH:mm:ss").toString()).replace(' ', 'T');
    console.log("reflash = "+reflash);
    console.log("interchangeable = "+interchangeable);
    console.log("scrapParts = "+scrapParts);
    
    this.modelPartApproval.modelPartApprovalId = null;
    this.modelPartApproval.approveAssociateNumber = null;
    this.modelPartApproval.approveStatus = 'PENDING';
    this.modelPartApproval.approveTimestamp = null;
    this.modelPartApproval.currentInterchangable = item.interchangeable;
    this.modelPartApproval.currentReflash = item.reflash;
    this.modelPartApproval.currentScrapParts = item.scrapParts;
    this.modelPartApproval.currentStartingProductionLot = item.startingProductionLot;
    this.modelPartApproval.modelPartId = item.modelPartId;
    this.modelPartApproval.newInterchangable = interchangeable;
    this.modelPartApproval.newReflash = reflash;
    this.modelPartApproval.newScrapParts = scrapParts;
    this.modelPartApproval.newStartingProductionLot = selectedProdLot;
    this.modelPartApproval.requestAssociateNumber = this.security.getUser();
    this.modelPartApproval.requestTimestamp = beginDate;
   
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/saveModelPartApproval', this.modelPartApproval,{headers: this.security.headers});
  }
  
 saveMultiModelPartApprovals(modelLotList:ModelPartLotDto[],selectedPlanCode:string,selectedProdLot: string): Observable<any>{
   console.log(JSON.stringify(modelLotList));
      this.modelPartApprovalList.length=0;
      var date = new Date();
      //var beginDate = (this.datePipe.transform(date,"yyyy-MM-dd HH:mm:ss").toString()).replace(' ', 'T');
      var beginDate =(this.datePipe.transform((new Date()), "yyyy-MM-dd HH:mm:ss").toString()).replace(' ', 'T');
      modelLotList.map(s=>{
        this.modelPartApproval = new ModelPartApproval();
        this.modelPartApproval.modelPartApprovalId = null;
        this.modelPartApproval.approveAssociateNumber = null;
        this.modelPartApproval.approveStatus = 'PENDING';
        this.modelPartApproval.approveTimestamp = null;
        this.modelPartApproval.currentInterchangable = s.interchangeable;
        this.modelPartApproval.currentReflash = s.reflash;
        this.modelPartApproval.currentScrapParts = s.scrapParts;
        this.modelPartApproval.currentStartingProductionLot = s.startingProductionLot;
       
        this.modelPartApproval.modelPartId = s.modelPartId;
        this.modelPartApproval.newInterchangable = s.interchangeable;
        this.modelPartApproval.newReflash = s.reflash;
        this.modelPartApproval.newScrapParts = s.scrapParts;
        this.modelPartApproval.newStartingProductionLot = selectedProdLot;
        this.modelPartApproval.requestAssociateNumber = this.security.getUser();
        this.modelPartApproval.requestTimestamp = beginDate;
        
        this.modelPartApprovalList.push(this.modelPartApproval);
        
      });
      console.log(this.modelPartApprovalList);
      return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/saveModelPartApprovalList', this.modelPartApprovalList,{headers: this.security.headers});
  }

  saveModelLotsAndModelPartApprovals(modelLotList:ModelPartLotDto[], selectedPlanCode:string,selectedProdLot: string): Observable<any> {
    
       
    let data = '{' +
    '"selectedProductionLot":"'+ selectedProdLot+'",'+
    '"selectedPlanCode": "'+selectedPlanCode+'",'+
    '"modelPartLotDtoList": '+ JSON.stringify(modelLotList)+
    '}';

    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/saveModelLotsAndModelPartApprovals', data,{headers: this.security.headers});
  }
}
