import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ModelPartApproval } from '../models/model-part-approval';
import { DatePipe, formatDate } from '@angular/common';
import { ConfigService } from './config.service';
import {SecurityService} from './security.service';

@Injectable({
  providedIn: 'root'
})
export class RuleLotApprovalService {

  modelPartApproval: ModelPartApproval = new ModelPartApproval();
  modelPartApprovalList:ModelPartApproval[];
  idList:number[];

  constructor(private httpClient: HttpClient,
    private datePipe: DatePipe, private config:ConfigService, private security:SecurityService) { }

  getPendingModelPartApprovals(): Observable<ModelPartApproval[]> {
    return this.httpClient.get<ModelPartApproval[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getPendingModelPartApprovals',{headers: this.security.headers});
  }

  
  approveMultiPleModelPartChange(modelPartApprovalList:ModelPartApproval[]): Observable<any> {
    this.idList=[];
    modelPartApprovalList.forEach(s=>{
      this.idList.push(s.modelPartApprovalId);
    });
    //console.log(modelPartApprovalId);
    //this.modelPartApproval.modelPartApprovalId = modelPartApprovalId;
    //this.modelPartApproval.approveAssociateNumber = this.security.getUser();
    
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/approveMultiModelPartChange', this.idList,{headers: this.security.headers});
  }

   approveModelPartChange1(modelPartApprovalId: number) {
    console.log(modelPartApprovalId);
    this.modelPartApproval.modelPartApprovalId = modelPartApprovalId;
    this.modelPartApproval.approveAssociateNumber = this.security.getUser();
    
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/approveModelPartChange', this.modelPartApproval,{headers: this.security.headers}).toPromise();
  }
 
  denyMultipleModelPartChange(modelPartApprovalList:ModelPartApproval[]): Observable<any> {
    this.idList=[];
    modelPartApprovalList.forEach(s=>{
      this.idList.push(s.modelPartApprovalId);
    });
    //this.modelPartApproval.modelPartApprovalId = modelPartApprovalId;
    //this.modelPartApproval.approveAssociateNumber = this.security.getUser();
    
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/denyMultiModelPartChange', this.idList,{headers: this.security.headers});
  }

  

  denyModelPartChange1(modelPartApprovalId: number) {
    this.modelPartApproval.modelPartApprovalId = modelPartApprovalId;
    this.modelPartApproval.approveAssociateNumber = this.security.getUser();
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/denyModelPartChange', this.modelPartApproval,{headers: this.security.headers}).toPromise();
  }

}
