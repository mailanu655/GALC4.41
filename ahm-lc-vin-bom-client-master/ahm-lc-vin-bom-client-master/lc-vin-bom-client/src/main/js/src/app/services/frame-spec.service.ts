import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ModelYearCode } from '../models/model-year-code';
import { FrameSpec } from '../models/frame-spec';
import { environment } from 'src/environments/environment';
import { ConfigService } from './config.service';
import { SecurityService } from './security.service';

@Injectable({
  providedIn: 'root'
})
export class FrameSpecService {
  
  API_URL = environment.apiUrl;

  constructor(private httpClient: HttpClient, private config:ConfigService, private security:SecurityService) { }

  findAllModelYearCodes(): Observable<string[]> {ConfigService
        return this.httpClient.get<string[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getAllModelYears',{headers: this.security.headers});
  }

  findModelCodeByYear(modelYearList: string[]): Observable<Object> {
    //return this.httpClient.get<Object>('/RestWeb/v2/VinBom/getModelCodesByModelYearCode');
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/v2/VinBom/getModelCodesByModelYearCode', modelYearList,{headers: this.security.headers});
  }

  findAllByYMTOCWildCard(modelYear: string, modelCode: string): Observable<Object> {
    let data = '['+
      '{"java.lang.String":"' + modelYear + '"},' +
      '{"java.lang.String":"' + modelCode + '"},' +
      '{"java.lang.String":""},' +
      '{"java.lang.String":""},' +
      '{"java.lang.String":""},' +
      '{"java.lang.String":""}' +
    ']';
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/FrameSpecDao/findAllByYMTOCWildCard', JSON.parse(data),{headers: this.security.headers});
  }

  findAllByModelYearCode(modelYear: string): Observable<FrameSpec[]> {
      return this.httpClient.post(this.config.serviceUrl+'/RestWeb/FrameSpecDao/findAllByModelYearCode', { "java.lang.String": modelYear },{headers: this.security.headers})
        .pipe(map((response: any) => response as FrameSpec[]));
  }

  findAllModelTypebyProductType(pruductType: string): Observable<any> {
    return this.httpClient.post(this.config.serviceUrl+'/RestWeb/FrameSpecDao/findAllModelTypeCodes', { "java.lang.String": "FRAME" },{headers: this.security.headers});
  }

  findModelTypebyYMTOC(modelYear: string, modelCode: string, modelType: string, modelOption: string, extColor: string, intColor: string): Observable<string[]> {
    return this.httpClient.get<string[]>(this.config.serviceUrl+'/RestWeb/v2/VinBom/getModelTypeByYMTOC?modelYear=' + modelYear +
                                          '&modelCode=' + modelCode + 
                                          '&modelType=' + modelType + 
                                          '&modelOption=' + modelOption +
                                          '&extColor=' + extColor + 
                                          '&intColor=' + intColor,{headers: this.security.headers});
  }

}
