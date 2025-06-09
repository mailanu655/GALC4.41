import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import {LetCategoryCode}from '../models/let-category-code';
import { LetPartialCheck } from '../models/let-partial-check';
import { ConfigService } from './config.service';
import {SecurityService} from './security.service';

@Injectable({
  providedIn: 'root'
})
export class CategoryMaintService {

  constructor(private httpClient: HttpClient, private config:ConfigService, private security:SecurityService) { }

  findAllCatgeoryCodes(): Observable<LetCategoryCode[]> {
    
    return this.httpClient.get<LetCategoryCode[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getAllCategoryCodes',{headers: this.security.headers});
  }


  findAssignedInspectionsByCategoryCode(categoryCodeId: string): Observable<LetPartialCheck[]> {
    
    return this.httpClient.get<LetPartialCheck[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/findAssignedInspectionsByCategoryCode'+'?categoryCodeId='+categoryCodeId,{headers: this.security.headers});
  }

  saveCategory(letCategoryCode:LetCategoryCode,inspectionNames:string[]):any{
    var inclusive:boolean = false;
    var catCodeId:string = '-1'; 
    if(letCategoryCode.inclusive)
    inclusive = true
    
    if(letCategoryCode.categoryCodeId)
      catCodeId = letCategoryCode.categoryCodeId;

    let data = '{' +
            '"categoryCodeId":'+ catCodeId+','+
            '"name": "'+letCategoryCode.name+'",'+
            '"description": "'+letCategoryCode.description+'",'+
            '"inclusive":'+ inclusive+','+
            '"inspectionNames": '+ JSON.stringify(inspectionNames)+
        '}';
       console.log(data);
      
       return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/updateCategoryInspections',data, {headers: this.security.headers});
  }

  deleteCategory(categoryCodeId: string):any{
        
    const httpOptions = {
        headers: this.security.headers,
        body: categoryCodeId
    };
    return this.httpClient.delete(this.config.serviceUrl+'/RestWeb/v2/VinBom/removeLetCategory',httpOptions);
  }

}
