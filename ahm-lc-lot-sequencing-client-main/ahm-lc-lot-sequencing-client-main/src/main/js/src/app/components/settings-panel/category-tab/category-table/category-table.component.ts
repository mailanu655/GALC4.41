import {
  Component,
  OnInit,
  ViewChild,
  Output,
  EventEmitter,
} from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import Category from 'src/app/components/entities/Category';
import { CategoryService } from 'src/app/services/data/category.service';
import { ApplicationCategoryService } from 'src/app/services/data/application_category.service';
import { EditComponent } from 'src/app/components/dialog/edit/edit.component';
import { LogService } from 'src/app/services/log.service';

@Component({
  selector: 'app-category-table',
  templateUrl: './category-table.component.html',
  styleUrls: ['./category-table.component.css'],
})
export class CategoryTableComponent implements OnInit {
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @Output() selectedCategory = new EventEmitter<Category>();
  dataSource = new MatTableDataSource<Category>();
  selectedRow: Category = new Category();

  columns = [
    {
      columnDef: 'name',
      header: 'Name',
      cell: (element: Category) => `${element.name}`,
    },
    {
      columnDef: 'description',
      header: 'Description',
      cell: (element: Category) => `${element.description}`,
    },
    { columnDef: 'editButton', header: '', cell: (element: null) => `` },
    { columnDef: 'button', header: '', cell: (element: null) => `` },
  ];

  constructor(
    private catService: CategoryService,
    private appCatService: ApplicationCategoryService,
    private logService: LogService,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.getCategories();
  }

  getCategories() {
    this.catService.getAllCategories().subscribe((categories: Category[]) => {
      if (categories.length == 0) {
        this.logService.error('No records found', [
          { method: 'getCategories' },
        ]);
        return;
      }
      this.dataSource = new MatTableDataSource<Category>(categories);
      this.dataSource.paginator = this.paginator;
      setTimeout(() => (this.dataSource.sort = this.sort), 2000);
    });
  }

  getDisplayedColumns(): string[] {
    return this.columns.map((c) => c.columnDef);
  }

  deleteCategory(row: Category) {
    const startTime: any = new Date();
    this.catService
      .deleteCategory(row.categoryId)
      .subscribe((response: null) => {
        this.appCatService.deleteByCategoryId(row.categoryId).subscribe();
        this.getCategories();
        const endTime: any = new Date();
        this.logService.debug('Category deleted -- ' + JSON.stringify(row), [
          { method: 'deleteCategory', duration: endTime - startTime },
        ]);
      });
  }

  editCategory(row: Category) {
    this.openDialog('Edit', 'Category', row);
    this.logService.gui('edit category', [{ method: 'editCategory' }]);
  }

  openDialog(action, type, obj) {
    obj.action = action;
    obj.type = type;
    const dialogRef = this.dialog.open(EditComponent, {
      width: '550px',
      data: obj,
    });

    dialogRef.beforeClosed().subscribe((result) => {
      setTimeout(() => {
        this.getCategories();
      }, 500);
    });
  }

  onRowSelected(category: Category) {
    this.selectedRow = category;
    this.selectedCategory.emit(category);
    this.logService.gui('select category', [{ method: 'onRowSelected' }]);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    this.logService.debug('Filter applied -- ' + filterValue, [
      { method: 'applyFilter' },
    ]);
  }

  refreshCategories() {
    this.getCategories();
    this.logService.gui('refresh categories', [
      { method: 'refreshCategories' },
    ]);
  }
}
