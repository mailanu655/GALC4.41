import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ModelPart } from '../models/model-part';
import { ConfigService } from './config.service';
import {SecurityService} from './security.service';

@Injectable({
  providedIn: 'root'
})
export class InterchangeableService {

  constructor(private httpClient: HttpClient, private config:ConfigService, private security:SecurityService) { }

  findDistinctLetSystemName(): Observable<string[]> {
    return this.httpClient.get<string[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/findDistinctLetSystemName',{headers: this.security.headers});
  }

  search(): Observable<ModelPart[]> {
    return this.httpClient.get<ModelPart[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/findAllActiveInterchangeble',{headers: this.security.headers});
  }

  inactive(item: ModelPart): Observable<any> {
    
    return this.httpClient.put(this.config.serviceUrl+'/RestWeb/v2/VinBom/setInterchangableInactive', item.modelPartId,{headers: this.security.headers});
  }
}
