import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { map, startWith, catchError, tap, timeout,  } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class DataLoadService {

  
  constructor(
    private http: HttpClient,
    private config: ConfigService
    ) { 
       
    }

    public getFactoryNews(productionDate: string) {
      let plantName: string = this.config.site.plantName;
      const params = new HttpParams()
            .set('productionDate', productionDate);
      let API_URL: string = productionDate ? "/getHistoricalFactoryNewsData" : "/getFactoryNewsData";
      let opts = [];
      return this.http.get<any>(this.config.site.vdbUrl + API_URL, {params}).pipe(tap(data=> opts = data));
    }

    public getFactoryNewsCurrentInventory(productionDate: string) {
      let plantName: string = this.config.site.plantName;
      const params = new HttpParams()
            .set('productionDate', productionDate);
      let API_URL: string = productionDate ? "/getHistoricalFactoryNewsInventoryData" : "/getFactoryNewsCurrentInventoryData";
      let opts = [];
      return this.http.get<any>(this.config.site.vdbUrl + API_URL, {params}).pipe(tap(data=> opts = data));
    }

    public getMetricsData(productionDate: string) {
      const params = new HttpParams()
            .set('productionDate', productionDate);
      let API_URL: string = productionDate ? "/getHistoricalMetricsData" : "/getMetricsData";
      let opts = [];
      return this.http.get<any>(this.config.site.vdbUrl + API_URL, {params}).pipe(tap(data=> opts = data));
    }

    public checkSiteStatus() {      
      let API_URL: string = "/db-status";
      let opts = [];
      return this.http.get<any>(this.config.site.vdbUrl + API_URL).pipe(
        timeout(10000),
        tap(data=> opts = data),
        catchError(error => {         
          console.log('Gateway Timeout: ', error);          
          return throwError(error);
        })
      );
    }
   
}