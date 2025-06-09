import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ConfigService } from '../config.service';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {

  constructor(private http: HttpClient, private config: ConfigService) {}


  getAllCategories() {
    return this.http.get(`${this.config.baseUrl}/category/findAll`);
  }

  insertCategory(name: string, description: string){
    return this.http.get(`${this.config.baseUrl}/category/insert/${name},${description}`);
  }

  modifyCategory(name: string, description: string, categoryId: number){
    return this.http.get(`${this.config.baseUrl}/category/modify/${categoryId},${name},${description}`);
  }

  deleteCategory(categoryId: number){
    return this.http.get(`${this.config.baseUrl}/category/deleteById?id=${categoryId}`);
  }
}