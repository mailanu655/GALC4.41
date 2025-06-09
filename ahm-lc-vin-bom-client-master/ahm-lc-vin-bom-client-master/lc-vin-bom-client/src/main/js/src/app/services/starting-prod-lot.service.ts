import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ModelPartApproval } from '../models/model-part-approval';
import { DatePipe, formatDate } from '@angular/common';
import { PreProductionLot } from '../models/pre-production-lot';
import { ConfigService } from './config.service';
import { SecurityService } from './security.service';

@Injectable({
  providedIn: 'root'
})
export class StartingProdLotService {

  constructor(private httpClient: HttpClient,
    private datePipe: DatePipe,
    private config:ConfigService,private security:SecurityService) { }

    getProductionLotsByProcessLocation(processLocation: string, selectedFromDate: string, line: string): Observable<PreProductionLot[]> {
      return this.httpClient.get<PreProductionLot[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getProductionLotsByProdDateAndLine?productionDate='+selectedFromDate+'&lineNo='+line,{headers: this.security.headers});
    }
  

}
