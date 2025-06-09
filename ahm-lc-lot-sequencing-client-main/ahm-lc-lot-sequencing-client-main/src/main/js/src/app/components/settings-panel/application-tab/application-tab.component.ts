import { Component, Output, ViewChild, EventEmitter } from '@angular/core';
import Application from '../../entities/Application';
import { ApplicationTableComponent } from './application-table/application-table.component';
import { CategoryMapComponent } from './category-map/category-map.component';
import { ApplicationService } from 'src/app/services/data/application.service';
import { ApplicationCategoryService } from 'src/app/services/data/application_category.service';

@Component({
  selector: 'app-application-tab',
  templateUrl: './application-tab.component.html',
  styleUrls: ['./application-tab.component.css']
})
export class ApplicationTabComponent {
  @ViewChild(ApplicationTableComponent) appTable: ApplicationTableComponent;
  @ViewChild(CategoryMapComponent) categoryMap: CategoryMapComponent;
  @Output() refresh = new EventEmitter();

  constructor(  private appService: ApplicationService,
                private appCatService: ApplicationCategoryService) { }

  refreshApplications(){
    this.appTable.getApplications();
  }

  setSelectedApplication(application: Application){
    this.categoryMap.applicationId = application.applicationId;
    this.categoryMap.getAppCategories();
  }
}
