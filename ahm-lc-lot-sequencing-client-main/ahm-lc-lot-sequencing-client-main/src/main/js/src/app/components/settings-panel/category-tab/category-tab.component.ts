import { Component, OnInit, Output, EventEmitter, ViewChild } from '@angular/core';
import { CategoryTableComponent } from './category-table/category-table.component';
import { ApplicationMapComponent } from './application-map/application-map.component';
import Category from 'src/app/entities/Category';

@Component({
  selector: 'app-category-tab',
  templateUrl: './category-tab.component.html',
  styleUrls: ['./category-tab.component.css']
})
export class CategoryTabComponent implements OnInit {
  @ViewChild(CategoryTableComponent) catTable: CategoryTableComponent;
  @ViewChild(ApplicationMapComponent) applicationMap: ApplicationMapComponent;
  @Output() refresh = new EventEmitter();
  
  constructor() { }

  ngOnInit(): void {
  }

  refreshCategories(){
    this.catTable.getCategories();
  }

  setSelectedCategory(categoryId: Category){
    this.applicationMap.categoryId = categoryId.categoryId;
    this.applicationMap.getAppCategories();
  }
}
