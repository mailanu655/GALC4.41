import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { CookieService } from 'ngx-cookie-service';
import { CategoryService } from 'src/app/services/data/category.service';
import { LogService } from 'src/app/services';
import Category from '../../entities/Category';

@Component({
  selector: 'category-filter',
  templateUrl: './category-filter.component.html',
  styleUrls: ['./category-filter.component.css']
})
export class CategoryFilter implements OnInit {
  @Output() disabledCatEmitter = new EventEmitter<Set<number>>();
  disabledCategories = new Set<number>();
  categories: Category[];

  constructor(private cookieService: CookieService, private catService: CategoryService, private logService: LogService) { 
    this.getCategories();
    this.getDisabledCatCookie();
  }

  ngOnInit(): void {

    this.getCategories();
    this.getDisabledCatCookie();
    
  }

  getCategories() {
    this.catService.getAllCategories().subscribe((categories: Category[]) => {
        this.logService.info('categories -- ' + JSON.stringify(categories), [{ method: 'getCategories' }]);
        this.categories=categories;     
      if (categories.length == 0){
        this.logService.error('No records found', [{ method: 'getCategories' }]);
      }
    });
  }

  updateDisabledCategories(event: MatSlideToggleChange, catId: number){
    if (event.checked)
      this.disabledCategories.delete(catId);
    else 
      this.disabledCategories.add(catId);
    this.disabledCatEmitter.emit(this.disabledCategories);
    this.setDisabledCatCookie()
    this.logService.debug('Disabled categories -- ' + JSON.stringify(this.disabledCategories), [{ method: 'updateDisabledCategories' }]);
  }

  setDisabledCatCookie(){
    let cookieValue = "";
    for (let category of this.disabledCategories){
      cookieValue = cookieValue.concat(category.toString() + ",");
    }
    this.cookieService.set('disabled-cat', cookieValue);
    this.logService.debug('Disabled categories cookie -- ' + cookieValue, [{ method: 'setDisabledCatCookie' }]);
  }

  getDisabledCatCookie() {
    let cookieValue = this.cookieService.get('disabled-cat');
    if (cookieValue == null) return;

    let cookieList = cookieValue.split(',');
    this.disabledCategories = new Set<number>();
    for (let cookie of cookieList){
      this.disabledCategories.add(Number(cookie));
    }
  }

  isCategoryDisabled(categoryId: number): boolean{
    return this.disabledCategories.has(categoryId);
  }
}
