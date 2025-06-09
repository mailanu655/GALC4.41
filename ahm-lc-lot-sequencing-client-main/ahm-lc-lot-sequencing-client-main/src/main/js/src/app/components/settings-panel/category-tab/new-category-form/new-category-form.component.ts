import { Component, OnInit, Output, EventEmitter, ViewChild, TemplateRef } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { CategoryService } from 'src/app/service/data/category.service';
import Category from 'src/app/entities/Category';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-new-category-form',
  templateUrl: './new-category-form.component.html',
  styleUrls: ['./new-category-form.component.css']
})
export class NewCategoryFormComponent implements OnInit {
  @Output() categorySaved = new EventEmitter();
  @ViewChild('addCategoryDialog') addCategoryDialog: TemplateRef<any>;
  submitted = false;
  model = new Category();
  nameFormControl = new FormControl('', [Validators.required]);
  descrFormControl = new FormControl('');

  constructor(private catService: CategoryService,
    private dialog: MatDialog) { }

  ngOnInit(): void {
  }

  onSubmit() { this.submitted = true; }

  newCategory(){
    this.model = new Category();
  };

  saveCategory(model: Category){
    if (model.description == null) model.description = ""; 
    this.catService.insertCategory(model.name, model.description).subscribe((response:null) => {
      this.categorySaved.emit();
    });
    this.dialog.closeAll(); 
  }

  addCategory(){
    this.dialog.open(this.addCategoryDialog);
  }
}
