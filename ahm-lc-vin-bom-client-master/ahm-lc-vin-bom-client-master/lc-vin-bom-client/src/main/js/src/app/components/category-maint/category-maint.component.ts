import { OverlayContainer } from '@angular/cdk/overlay';
import { AfterViewInit, Component, Inject, ElementRef, OnInit, ViewChild, Input } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';


import { LetCategoryCode } from 'src/app/models/let-category-code';
import { LetPartialCheck } from 'src/app/models/let-partial-check';
import { Message } from 'src/app/models/message';
import { CategoryMaintService } from 'src/app/services/category-maint.service';
import { AlertService } from 'src/app/alert/alert.service';
import { ConfirmationDialogService } from 'src/app/confirmation-dialog/confirmation-dialog.service';
import { UtilityService } from 'src/app/services/utility-service';
import { UtilityComponent } from '../header/utility.component';
import { Title } from '@angular/platform-browser';
import { AddCategoryDialog } from './add-category-dialog';

@Component({
  selector: 'app-category-maint',
  templateUrl: './category-maint.component.html',
  styleUrls: ['./category-maint.component.css']
})


export class CategoryMaintComponent implements OnInit {

  public loading = false;
  public message: Message = { type: null, message: null };
  public categoryList: LetCategoryCode[];
  public inspectionList: LetPartialCheck[];
  selectRowData: LetCategoryCode= new LetCategoryCode();
  selectedRow: number;
  highlightedRow: Number;
  selectRow: Function;
  highlightCategory: Function;
  isAddedNewCategory : boolean;
  displayedColumns = ['name', 'description', 'inclusive'];
  inspectionDisplayedColumns = ['letInspectionName'];
  dataSource: MatTableDataSource<LetCategoryCode>;
  inspectionDataSource: MatTableDataSource<LetPartialCheck>;
  @ViewChild('catTableSort',{ read: MatSort, static: true }) sort: MatSort;
  @ViewChild('inspTableSort',{ read: MatSort, static: true }) inspTableSort: MatSort;

  messageOptionsAutoClose = {
    autoClose: false,
    keepAfterRouteChange: false
  };

  messageOptions = {
    autoClose: false,
    keepAfterRouteChange: false
  };
  constructor(public dialog: MatDialog,  
    public cdk: OverlayContainer, 
    protected alertService: AlertService,
    private titleService: Title,
    private util: UtilityService,
    private confirmationDialogService: ConfirmationDialogService,
    public categoryMaintService: CategoryMaintService
    
    ) { 
      this.titleService.setTitle("SUMS-VIN BOM Let Category");

      this.selectRow = function(index, row){
        this.alertService.clear();
        this.selectedRow = index;
        this.selectRowData = row;
        this.populateLetInspectionNames();
      }

      this.highlightCategory = function(index){
        this.highlightedRow = index;
      }
    }

    ngOnInit(): void {
      this.populateLetCategoryCodes();
      
    }
    sortColumn($event:Sort) {
         console.log($event);
         this.inspectionList =[];
         this.inspectionDataSource = new MatTableDataSource(this.inspectionList);
         this.selectRow(-1,null);
    }

    sortInspColumn($event:Sort) {
      console.log($event);
      if($event.direction === 'asc'){
        this.inspectionList = this.inspectionDataSource.data;
        this.inspectionList.sort();
        this.inspectionDataSource = new MatTableDataSource(this.inspectionList);
      }else if($event.direction === 'desc'){
        this.inspectionList = this.inspectionDataSource.data;
        this.inspectionList.reverse();
        this.inspectionDataSource = new MatTableDataSource(this.inspectionList);
      }else {
        this.populateLetInspectionNames();
      }
    }

  populateLetCategoryCodes() {
    this.loading = true;
    this.categoryMaintService.findAllCatgeoryCodes()
      .subscribe((categoryList: LetCategoryCode[]) => {

        this.categoryList = categoryList;
        this.dataSource = new MatTableDataSource(categoryList);
        this.dataSource.sortingDataAccessor = (data, sortHeaderId) => data[sortHeaderId].toString().toLocaleLowerCase();
        this.dataSource.sort = this.sort;
        this.loading = false;
      },(error: any) => {
        this.loading = false;
      });
  }
 
  populateLetInspectionNames(){
    if(this.selectRowData === null || this.selectRowData.categoryCodeId === undefined){
      return;
    }
    console.log(" update inspections !!!!");
    this.categoryMaintService.findAssignedInspectionsByCategoryCode(this.selectRowData.categoryCodeId)
    .subscribe((inspectionList: LetPartialCheck[]) => {

      this.inspectionList = inspectionList;
      this.inspectionDataSource = new MatTableDataSource(inspectionList);
      //this.inspectionDataSource.sortingDataAccessor = (data, sortHeaderId) => data[sortHeaderId].toLocaleLowerCase();
      this.inspectionDataSource.sort = this.inspTableSort;
    },(error: any) => {
      
    });
  }

  addCategoryOpenDialog(addNew:boolean) {
    this.alertService.clear();
    
    console.info("addNew -"+addNew);
    if(!addNew){
      if(this.selectRowData === null ){
          alert("Please select a category");
          return;
      }else if(this.selectRowData.categoryCodeId === null || this.selectRowData.categoryCodeId === undefined){
          alert("Please select a category");
        return;
      }else{
          console.info("categoryCodeID- " +this.selectRowData.categoryCodeId);
      }
    }
    

    const dialogRef = this.dialog.open(AddCategoryDialog, {
      width: "50%",
      height: '70%',
      data: { isAddedNewCategory : addNew, selectedLetCategoryCode:this.selectRowData,selectedInspectionList:this.inspectionList,categoryListNames:this.categoryList.map(x=>x.name) },
      id: "parent",
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log("The dialog was closed");
      this.isAddedNewCategory = result.isAddedNewCategory ;
      console.log("Added new Part: " + this.isAddedNewCategory );
      if(this.isAddedNewCategory ) {
        //this.message.type="SUCCESS";
        //this.message.message = "Part added succesfully";
        this.alertService.success('Success!!', this.messageOptionsAutoClose);
        
      }
        this.dialog.closeAll();
        this.refreshList();
      
    });
     dialogRef.afterClosed().subscribe((confirmed: boolean) => {
       if (confirmed) {
         console.log('confirm');
         
         this.dialog.closeAll();
       }
     });
  }
  refreshList() {
    this.populateLetCategoryCodes();
    this.inspectionList =[];
    this.inspectionDataSource = new MatTableDataSource(this.inspectionList);
    this.selectRow(-1,null);
  }

  removeCat(): void {
    if(this.selectRowData && this.selectRowData.categoryCodeId ){
      this.confirmationDialogService.confirm('', 'Are you sure you want to delete select category? ', 'Yes', 'No', 'lg')
          .then((confirmed) => {
            if(confirmed) {
                  this.categoryMaintService.deleteCategory(this.selectRowData.categoryCodeId)
                  .subscribe(data => {
                    console.log("Category Removed");
                    this.refreshList();
                  this.alertService.clear();
                  this.alertService.success('Selected Category data Deleted', this.messageOptionsAutoClose);
                    
                  },(error: any) => {
                    alert("Error Occured while deleting Category");
                    console.log(error);
                    return;
                  });
            }
        });
  }else{
    alert("Please select a part");
  }
}

}

