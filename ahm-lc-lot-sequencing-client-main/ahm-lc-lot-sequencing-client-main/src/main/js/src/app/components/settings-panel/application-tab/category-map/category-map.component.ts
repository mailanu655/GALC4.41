import { Component, OnInit, Input, SimpleChanges } from '@angular/core';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { CategoryService} from '../../../service/data/category.service';
import { ApplicationCategoryService} from '../../../service/data/application_category.service';
import Category from '../../../entities/Category'
import ApplicationCategory from '../../../entities/ApplicationCategory'

@Component({
  selector: 'app-category-map',
  templateUrl: './category-map.component.html',
  styleUrls: ['./category-map.component.css']
})
export class CategoryMapComponent implements OnInit {
  @Input() applicationId: number;
  categories: Map<string,number> = new Map();
  appCategories: Map<number,number> = new Map();
  availableCategories:string[] = [];
  assignedCategories:string[] = [];

  constructor(private catService: CategoryService, private appCatService: ApplicationCategoryService) { }

  ngOnInit(): void {
    this.getCategories();
  }

  ngOnChanges(changes: SimpleChanges) {
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          case 'applicationId': {
            if (this.applicationId == null) return;
            this.getAppCategories();
          }
        }
      }
    }
  }

  getCategories(){
    this.catService.getAllCategories().subscribe((categories: Category[]) => {
      for (let category of categories){
        this.categories.set(category.name,category.categoryId);
      }
    });
  }

  getAppCategories(){
    this.appCategories = new Map();
    this.assignedCategories = [];
    this.availableCategories= [];
    this.appCatService.getAll().subscribe((applicationCategories: ApplicationCategory[]) => {
      for (let appCategory of applicationCategories){
        if (appCategory.id.applicationId == this.applicationId) 
          this.appCategories.set(appCategory.id.categoryId,appCategory.id.applicationId);
      }
      this.populateLists()
    });
  }

  populateLists() {
    for (let category of this.categories){
      if (this.appCategories.has(category[1]))
        this.assignedCategories.push(category[0]);
      else 
        this.availableCategories.push(category[0]);
    }
  }

  drop(event: CdkDragDrop<string[]>, assign: boolean) {
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
    var categoryName = event.container.data[event.currentIndex];
    if (assign)
      this.appCatService.insertApplicationCategory(this.applicationId, this.categories.get(categoryName)).subscribe();
    else
      this.appCatService.deleteApplicationCategory(this.applicationId, this.categories.get(categoryName)).subscribe();
  }

  sortTable(){
    this.assignedCategories.sort((a, b) => a.localeCompare(b));
    this.availableCategories.sort((a, b) => a.localeCompare(b));
  }
}
