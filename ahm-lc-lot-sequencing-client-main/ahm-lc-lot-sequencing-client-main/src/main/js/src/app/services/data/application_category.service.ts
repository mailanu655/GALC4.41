import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import Application from '../../entities/Application';
import { ConfigService } from '../config.service';

@Injectable({
  providedIn: 'root',
})
export class ApplicationCategoryService {
  applications: Application[];

  constructor(private http: HttpClient, private config: ConfigService) {}


  getAll() {
    return this.http.get(`${this.config.baseUrl}/applicationCategory/findAll`);
  }

  insertApplicationCategory(applicationId: number, categoryId: number){
    return this.http.get(`${this.config.baseUrl}/applicationCategory/insert/${applicationId},${categoryId}`);
  }

  deleteApplicationCategory(applicationId: number, categoryId: number){
    return this.http.get(`${this.config.baseUrl}/applicationCategory/deleteById/${applicationId},${categoryId}`);
  }

  deleteByCategoryId(categoryId: number){
    return this.http.get(`${this.config.baseUrl}/applicationCategory/deleteByCategoryId/${categoryId}`);
  }

  deleteByApplicationId(applicationId: number){
    return this.http.get(`${this.config.baseUrl}/applicationCategory/deleteByApplicationId/${applicationId}`);
  }
}
