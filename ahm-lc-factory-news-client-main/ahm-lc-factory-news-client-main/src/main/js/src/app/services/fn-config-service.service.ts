import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { ConfigService } from './config.service';
import { KeycloakService, KeycloakAuthGuard } from 'keycloak-angular';
import { Router } from '@angular/router';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  }),
};
export interface TreeNode {
  sequence: boolean;
  plantId: any;
  groupTypeLists: any;
  groupType?: number;
  sequenceNumber?: any;
  type?: 'first' | 'second' | 'third' | 'fourth' | 'fifth';
  id: number;
  name: string;
  description?: string;
  children?: TreeNode[];
  records?: any[];
  galcProcessPointName?: string;
  groupTypeName?: string;
  groupId?: number;
  plant_id?: number;
  processPointLists?: any;
}

export interface Plant extends TreeNode {
  groupTypeList?: GroupType[];
}

export interface GroupType extends TreeNode {
  departmentList?: Department[];
  records?: any[];
}

export interface Department extends TreeNode {
  processPointGroupList?: ProcessPointGroup[];
  records?: any[];
}

export interface ProcessPointGroup extends TreeNode {
  processPointList?: ProcessPoint[];
  records?: any[];
}

export interface ProcessPoint extends TreeNode {
  processPointName: any;
  processPointId: any;
  galcProcessPointName: string;
  groupId: number;
  records?: any[];
  galcProcessPointId: any;
}

@Injectable({
  providedIn: 'root',
})
export class FnConfigServiceService {
  userDetails: any;
  authenticated: boolean = false;
  private plant: any;
  constructor(private http: HttpClient, private config: ConfigService, private router: Router, private keycloakAngular: KeycloakService) { }

  getData(): Observable<TreeNode[]> {
    const apiUrl = this.config.url + '/factorynews/listConfigs';
    const showMethod: string = (localStorage.getItem('selectedSite') as string).toUpperCase();
    return this.http.get<Plant[]>(apiUrl).pipe(
      map((plants) => {
        const filteredPlants = plants.filter((plant) => plant.name.toUpperCase() === showMethod);
        if (filteredPlants.length === 0) {
          return [];
        }
        return this.transformData(filteredPlants);
      })
    );
  }

  private transformData(plants: Plant[]): TreeNode[] {
    return plants.map((plant) => ({
      ...plant,
      type: 'first',
      children: plant.groupTypeList?.map((groupType) => ({
        ...groupType,
        type: 'second',
        children: groupType.departmentList?.map((dept) => ({
          ...dept,
          type: 'third',
          children: dept.processPointGroupList?.map((group) => ({
            ...group,
            type: 'fourth',
            children: group.processPointList?.map((point) => ({
              ...point,
              type: 'fifth',
              galcProcessPointName: point.galcProcessPointName || '',
            })),
          })),
        })),
      })),
    }));
  }

  getNodeDetailsByName(name: string): Observable<TreeNode | undefined> {
    return this.getData().pipe(map((nodes) => this.findNodeByName(nodes, name)));
  }

  private findNodeByName(nodes: TreeNode[], name: string): TreeNode | undefined {
    for (let node of nodes) {
      if (node.name === name || node.galcProcessPointName === name) {
        return node;
      }
      if (node.children) {
        const result = this.findNodeByName(node.children, name);
        if (result) {
          return result;
        }
      }
    }
    return undefined;
  }

  getProcessPoints(): Observable<ProcessPoint[]> {
    const apiUrl = this.config.galcUrl + '/RestWeb/ProcessPointDao/findAll';
    return this.http.get<ProcessPoint[]>(`${apiUrl}`);
  }

  saveRecordsForNextNode(nodeType: string, groupId: number, newRecord: any, plantId: number): Observable<any> {
    const apiUrl = this.config.url + '/factorynews/add';
    const payload = {
      nodeType,
      groupId,
      plantId,
      name: newRecord.department?.name || '',
      description: newRecord.department?.description || null,
      ...newRecord,
    };

    return this.http.post<any>(apiUrl, payload);
  }

  saveRecordsForNextOfNextNode(nodeType: string, groupId: number, newRecord: any, selectedProcessPoints: any[], plantId: number): Observable<any> {
    const apiUrl = this.config.url + '/factorynews/addProcessPointGroup';
    const processPointList = selectedProcessPoints.map(point => ({
      galcProcessPointId: point.processPointId || '',
      galcProcessPointName: point.processPointName || '',

    }));

    const payload = {
      nodeType,
      groupId,
      plantId,
      name: newRecord.ProcessPointGroup?.name || '',
      description: newRecord.ProcessPointGroup?.description || null,
      processPointList,
      ...newRecord,
    };

    return this.http.post<any>(apiUrl, payload);
  }

  updateRecord(nodeId: number, updatedRecord: any): Observable<any> {
    const apiUrl = `${this.config.url}/factorynews/update/${nodeId}`;
    const payload = {
      nodetype: updatedRecord.type,
      name: updatedRecord.name || '',
      description: updatedRecord.description || null,
      ...updatedRecord
    };

    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': 'Bearer your-token-here', 
        'Custom-Header': 'CustomValue' 
      }),
      params: { queryParam1: 'value1', queryParam2: 'value2' }
    };

    return this.http.put<any>(apiUrl, payload, httpOptions);
  }

  getAvailableProcessPoints(id: number): Observable<ProcessPoint[]> {
    const url = `${this.config.url}/factorynews/getAvailableProcessPoints/${id}`;
    return this.http.get<ProcessPoint[]>(url).pipe(
      map((processPoints) => {
        return processPoints;
      })
    );
  }
  saveNode(groupid: number, updatedData: any): Observable<any> {
    const apiUrl = this.config.url + '/factorynews/updateProcessPointGroup';
    const processPointList = Array.isArray(updatedData.processPointList)
      ? updatedData.processPointList.map(point => ({
        galcProcessPointId: point?.processPointId || '',
        galcProcessPointName: point?.processPointName || '',
      }))
      : [];
    const payload = {
      groupid,
      name: updatedData.ProcessPointGroup?.name || '',
      description: updatedData.ProcessPointGroup?.description || null,
      processPointList,
      ...updatedData,
    };
    return this.http.post<any>(apiUrl, payload).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error("Error occurred while sending payload:", error);
        return throwError(() => new Error(error.message));
      })
    );
  }


  async isLoggedIn() {
    return await this.keycloakAngular.isLoggedIn();
  }
  setPlant(plant: any) {
    this.plant = plant;
    localStorage.setItem('plant', JSON.stringify(plant));
    console.log("Plant set:", plant);
  }

  getPlant() {
    const plant = localStorage.getItem('plant');
    if (plant) {
      this.plant = JSON.parse(plant);
    }
    console.log("Plant retrieved:", this.plant);
    return this.plant;
  }


  logout() {
    let originUrl = window.location.origin;
    let redirectUrl = originUrl + this.router.createUrlTree(['']);
    // const currentUrl = this.router.url;
    this.keycloakAngular.logout(redirectUrl);
  }

  login() {
    this.authenticated = true;
    
    localStorage.setItem('config', 'true');
    return this.keycloakAngular.login();
  }

  async getUser() {
    if (await this.isLoggedIn()) {
      localStorage.setItem('userId', (this.keycloakAngular.getKeycloakInstance() as any).subject);
      localStorage.setItem('userRoles', JSON.stringify(this.keycloakAngular.getUserRoles()));
      return this.keycloakAngular.getUsername();
    } else {
      return '';
    }
  }

  async getUserProfile() {
    if (await this.isLoggedIn()) {
      this.keycloakAngular.loadUserProfile()
        .then(profile => {
          this.userDetails = profile;
          console.log("user details", this.userDetails);
          localStorage.setItem('userRoles', JSON.stringify(this.keycloakAngular.getUserRoles()));
          localStorage.setItem('userDetails', JSON.stringify(this.userDetails));
        })
        .catch(reason => { console.log(reason); });
      return this.userDetails;
    } else {
      return '';
    }
  }
}
