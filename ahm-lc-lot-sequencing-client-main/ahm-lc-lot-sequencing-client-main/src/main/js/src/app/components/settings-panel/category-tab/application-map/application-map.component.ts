import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { ApplicationService } from 'src/app/services/data/application.service';
import { ApplicationCategoryService } from 'src/app/services/data/application_category.service';
import Application from 'src/app/components/entities/Application';
import ApplicationCategory from 'src/app/components/entities/ApplicationCategory';


@Component({
  selector: 'app-application-map',
  templateUrl: './application-map.component.html',
  styleUrls: ['./application-map.component.css']
})
export class ApplicationMapComponent implements OnInit {
  @Input() categoryId: number;
  applications: Map<string,number> = new Map();
  appCategories: Map<number,number> = new Map();
  availableApplications:string[] = [];
  assignedApplications:string[] = [];
  
  constructor(  private appService: ApplicationService,
    private appCatService: ApplicationCategoryService) { }

  ngOnInit(): void {
    this.getCategories();
  }

  ngOnChanges(changes: SimpleChanges) {
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          case 'categoryId': {
            if (this.categoryId == null) return;
            this.getAppCategories();
          }
        }
      }
    }
  }

  getCategories(){
    this.appService.getAllApplications(0).subscribe((applications: Application[]) => {
      for (let application of applications){
        this.applications.set(application.name,application.applicationId);
      }
    });
  }

  getAppCategories(){
    this.appCategories = new Map();
    this.assignedApplications = [];
    this.availableApplications= [];
    this.appCatService.getAll().subscribe((applicationCategories: ApplicationCategory[]) => {
      for (let appCategory of applicationCategories){
        if (appCategory.id.categoryId == this.categoryId) 
          this.appCategories.set(appCategory.id.applicationId,appCategory.id.categoryId);
      }
      this.populateLists()
    });
  }

  populateLists() {
    for (let application of this.applications){
      if (this.appCategories.has(application[1]))
        this.assignedApplications.push(application[0]);
      else 
        this.availableApplications.push(application[0]);
    }
  }

  drop(event: CdkDragDrop<string[]>, assign: boolean) {
    var move = move;
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
                        event.container.data,
                        event.previousIndex,
                        event.currentIndex);
      this.saveState(event, assign);
      this.sortTable();
    }
  }

  saveState(event, assign: boolean){
    var applicationName = event.container.data[event.currentIndex];
    if (assign)
      this.appCatService.insertApplicationCategory(this.applications.get(applicationName), this.categoryId).subscribe();
    else
      this.appCatService.deleteApplicationCategory(this.applications.get(applicationName), this.categoryId).subscribe();
  }

  sortTable(){
    this.assignedApplications.sort((a, b) => a.localeCompare(b));
    this.availableApplications.sort((a, b) => a.localeCompare(b));
  }

}
