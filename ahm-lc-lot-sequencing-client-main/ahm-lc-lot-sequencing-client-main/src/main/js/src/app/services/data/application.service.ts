import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ConfigService } from '../config.service';
import { SecurityService } from '../security.service'; 
import Application from 'src/app/components/entities/Application';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  }),
};

@Injectable({
  providedIn: 'root',
})
export class ApplicationService {
  applications: Application[];
  keycloakclients: any;
  userDetails: any;
  userId: any;

  constructor(private http: HttpClient, private config: ConfigService, private securityService: SecurityService) {
   
  }

  ngOnInit(): void {        
   
    
  }

  getAllApplications(applicationId) {
    return this.http.get(`${this.config.baseUrl}/application/findAllWithClients`);
  }

  getAllApplicationsByUsageOrder(user: string) {    
    if(user==null){
      return this.http.get(`${this.config.baseUrl}/applicationUsage/findAllApplicationsByUsage`);
    }
    else {
    return this.http.get(`${this.config.baseUrl}/applicationUsage/findApplicationsByUsage/${user}`); 
  }
  }
  
  
  insertApplication(name: string, description: string, url: string, client: string, icon: string, iconName: string){
    const params: object[] = [
      { 'name': `${name}`,
        'description': `${description}`,
        'url': `${url}`,
        'client': `${client}`,
        'icon': `${icon}`,
        'iconName': `${iconName}`
      }
    ];
    return this.http.post(`${this.config.baseUrl}/application/insert?`,
      params,
      httpOptions
    );
  }

  deleteApplication(applicationId: number, client: string){
    const params:object[]=[{      
      'id':`${applicationId}`,
      'client': `${client}`
  }];
    return this.http.post(`${this.config.baseUrl}/application/deleteById?`,params,httpOptions);
  }

  modifyApplication(applicationId: number, name: string, description: string, url: string, client: string, icon: string, iconName: string){
    const params:object[]=[{
        'name': `${name}`,
        'description': `${description}`,
        'url': `${url}`,
        'id':`${applicationId}`,
        'client': `${client}`,
        'icon': `${icon}`,
        'iconName': `${iconName}`
    }];
    return this.http.post(`${this.config.baseUrl}/application/modify?`,
    params,
    httpOptions
  );
    //return this.http.get(`${this.config.baseUrl}/application/modify/${applicationId},${name},${description},${url}`);
  }
}