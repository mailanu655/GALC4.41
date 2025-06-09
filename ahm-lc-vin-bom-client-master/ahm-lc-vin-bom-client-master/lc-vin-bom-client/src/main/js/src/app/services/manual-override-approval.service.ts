import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { VinPartApproval } from '../models/vin-part-approval';
import { ConfigService } from './config.service';
import {SecurityService} from './security.service';


@Injectable({
  providedIn: 'root'
})
export class ManualOverrideApprovalService {

  constructor(private httpClient: HttpClient, private config:ConfigService, private security:SecurityService) { }

  
  
  findAllPending(): Observable<VinPartApproval[]> {
    return this.httpClient.get<VinPartApproval[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/findAllPendingVinPartApprovals',{headers: this.security.headers});
  }

  approveChange(item: VinPartApproval): Observable<any> {
    let approveStatus = '['+
      '{"com.honda.galc.entity.enumtype.VinPartApprovalStatus":"' + item.currentDcPartNumber + '"}'  +
    ']';

    // let data = '{' +
    // '"com.honda.galc.entity.lcvinbom.VinBomPart":' +
    //     '{'+
    //         '"id":{"productSpecCode": "' + bomPart.modelYearCode + bomPart.modelCode + bomPart.modelTypeCode + '","letSystemName":"' + bomPart.letSystemName + '"},' +
    //         '"basePartNumber": "' + bomPart.dcPartNumber + '",' +
    //         '"dcPartNumber": "' + bomPart.dcPartNumber + '",' +
    //         '"description": "' + bomPart.dcPartNumber + '",' +
    //         '"effectiveBeginDate": "' + bomPart.effectiveBeginDate + '",' +
    //         '"effectiveEndDate": "' + bomPart.effectiveEndDate + '"' +
    //     '}' +
    // '}';

    // let data = '['+
    //   '{"java.lang.Long":"' + item.vinPartApprovalId + '"},' +
    //   '{"approveStatus":"' + item.approveAssociateNumber + '"},' +
    //   '{"java.lang.String":"' + item.currentDcPartNumber + '"}'  +
    // ']';

    /*let data = '{' +
    '"com.honda.galc.entity.lcvinbom.VinPartApproval":' +
        '{'+
            '"approveStatus":{"id": "' + item.approveStatus + '"},' +
            '"vinPartApprovalId": "' + item.vinPartApprovalId + '",' +
            '"approveAssociateNumber": "' + item.approveAssociateNumber + '",' +
            '"approveTimestamp": "' + item.approveTimestamp + '",' +
            '"currentDcPartNumber": "' + item.currentDcPartNumber + '",' +
            '"currentShipStatus": "' + item.currentShipStatus + '",' +
            '"letSystemName": "' + item.letSystemName + '",' +
            '"newDcPartNumber": "' + item.newDcPartNumber + '",' +
            '"newShipStatus": "' + item.newShipStatus + '",' +
            '"productId": "' + item.productId + '",' +
            '"requestAssociateNumber": "' + item.requestAssociateNumber + '",' +
            '"requestTimestamp": "' + item.requestTimestamp + '",' +
            '"interchangeable": "' + item.interchangeable + '"' +
        '}' +
    '}';*/
    item.approveAssociateNumber = this.security.getUser(); // Get from logged in user
    
    return this.httpClient.put(this.config.serviceUrl+'/RestWeb/v2/VinBom/approveVinPartChange', item,{headers: this.security.headers});
  }


  denyChange(item: VinPartApproval): Observable<any> {
    /*let approveStatus = '['+
      '{"com.honda.galc.entity.enumtype.VinPartApprovalStatus":"' + item.currentDcPartNumber + '"}'  +
    ']';

    let data = '['+
      '{"java.lang.Long":"' + item.vinPartApprovalId + '"},' +
      '{"approveStatus":"' + item.approveAssociateNumber + '"},' +
      '{"java.lang.String":"' + item.currentDcPartNumber + '"}'  +
    ']';
    return this.httpClient.post('/RestWeb/VinPartApprovalDao/denyChange', JSON.parse(data)); */
    item.approveAssociateNumber = this.security.getUser(); // Get from logged in user
    console.log(item);
  

    return this.httpClient.put(this.config.serviceUrl+'/RestWeb/v2/VinBom/denyVinPartChange', item,{headers: this.security.headers});

  }

}
