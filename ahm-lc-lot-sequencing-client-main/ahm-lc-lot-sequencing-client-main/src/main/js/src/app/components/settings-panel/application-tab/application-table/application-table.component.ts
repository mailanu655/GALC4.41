import { Component, OnInit, Output, ViewChild, EventEmitter, SimpleChanges, AfterViewInit } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator} from '@angular/material/paginator';
import { ApplicationService } from 'src/app/service/data/application.service';
import { ApplicationCategoryService } from 'src/app/service/data/application_category.service';
import Application from 'src/app/entities/Application';
import { MatDialog } from '@angular/material/dialog';
import { EditComponent } from 'src/app/dialog/edit/edit.component';

@Component({
  selector: 'app-application-table',
  templateUrl: './application-table.component.html',
  styleUrls: ['./application-table.component.css']
})
export class ApplicationTableComponent implements OnInit, AfterViewInit {
  @ViewChild(MatSort) sort: MatSort;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @Output() selectedApplication = new EventEmitter<Application>();

  dataSource = new MatTableDataSource<Application>();
  selectedRow: Application = new Application();

  columns = [
    {columnDef: 'name', header: 'Name', cell: (element: Application) => `${element.name}`},
    {columnDef: 'description', header: 'Description', cell: (element: Application) => `${element.description}`},
    {columnDef: 'url', header: 'URL', cell: (element: Application) => `${element.url}`},
    {columnDef: 'clientId', header: 'Keycloak id', cell: (element: Application) => `${element.client}`},
    {columnDef: 'editButton', header: '', cell: (element: null) => ``},
    {columnDef: 'button', header: '', cell: (element: null) => ``}
  ]; 

  constructor(  private appService: ApplicationService,
                private appCatService: ApplicationCategoryService,
                public dialog: MatDialog) { }

  ngOnInit(): void {
    this.getApplications();
  }

  ngAfterViewInit(): void {
    
  }

  getApplications() {
    this.appService.getAllApplications(2).subscribe((applications: Application[]) => {
      if (applications.length == 0) return;
      this.dataSource = new MatTableDataSource<Application>(applications);    
      this.dataSource.paginator = this.paginator;          
      setTimeout(() => this.dataSource.sort = this.sort, 2000);
    });
  }

  getDisplayedColumns(): string[] {
    return this.columns.map(c => c.columnDef);
  }

  deleteApplication(row: Application){
    this.appService.deleteApplication(row.applicationId, row.client).subscribe((response: null)=> {
      this.appCatService.deleteByApplicationId(row.applicationId).subscribe();
      this.getApplications();
    });
  }

  editApplication(row: Application){
    this.openDialog('Edit','Application', row);
  }

  openDialog(action,type,obj) {
    obj.action = action;
    obj.type=type;
    const dialogRef = this.dialog.open(EditComponent, {
      width: '550px',
      data:obj
    });

    dialogRef.beforeClosed().subscribe(result => {  
      setTimeout(() => {
        this.getApplications();  
    }, 500);   
    });
  }

  onRowSelected(application: Application){
    this.selectedRow = application;
    this.selectedApplication.emit(application);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
}

refreshApplications(){
  this.getApplications();
}

}
