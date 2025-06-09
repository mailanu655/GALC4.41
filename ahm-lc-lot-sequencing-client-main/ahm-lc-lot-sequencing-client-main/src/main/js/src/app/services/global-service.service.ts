import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';
import { ConfigService } from './config.service';



@Injectable({
  providedIn: 'root'
})
export class GlobalServiceService {

  id: any;
  constructor(
    private http: HttpClient,
    private config: ConfigService
  ) { }

  setId = (value: string) => {
    this.id = value
  }

  getId = () => {
    return this.id
  }

  // Search Filter Drop Down
  public getFilterData() {
    let API_URL: string = `${this.config.galcUrl}/RestWeb/ScheduleReplicationDao/findAllDistinctSrcLocDestSpecCodeDestProcLoc`;
    return this.http.get<any>(API_URL, {}).pipe(tap(data => data));
  }

  //Save Records
  public addEditData(data) {
    let API_URL: string = `${this.config.galcUrl}/RestWeb/ScheduleReplicationDao/save`;
    return this.http.post<any>(API_URL, data).pipe(tap(data));
  }

  // Delete Records
  public removeData(data) {
    let API_URL: string = `${this.config.galcUrl}/RestWeb/ScheduleReplicationDao/remove`;
    let opts = [];
    return this.http.post<any>(API_URL, data).pipe(tap(data));
  }

  //Filter Button
  public getFilterTableData(value1: any, value2: any) {
    let API_URL: string = `${this.config.galcUrl}/RestWeb/ScheduleReplicationDao/findBySourceLocAndDestLoc?${value1}&${value2}&${this.config.count}`;
    if (!value1 && !value2) {
      API_URL = `${this.config.galcUrl}/RestWeb/ScheduleReplicationDao/findBySourceLocAndDestLoc`;
    }
    return this.http.get<any>(API_URL, {}).pipe(tap(data => data));
  }

  public getIsDuplicateData(value1: any, value2: any, value3: any) {
    let API_URL: string = `${this.config.galcUrl}/RestWeb/ScheduleReplicationDao/isScheduleReplicationExist?${value1}&${value2}&${value3}`;
    return this.http.get<any>(API_URL, {}).pipe(tap(data => data));
  }
}
