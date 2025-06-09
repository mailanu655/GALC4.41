import { OverlayContainer } from '@angular/cdk/overlay';
import { AfterViewInit, Component, Inject, ElementRef, OnInit, ViewChild, Input } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';

import { LetCategoryCode } from 'src/app/models/let-category-code';
import { LetPartialCheckId, LetPartialCheck } from 'src/app/models/let-partial-check';
import { CategoryMaintService } from 'src/app/services/category-maint.service';
import { MatCheckbox } from '@angular/material/checkbox';
import { DragDropModule } from '@angular/cdk/drag-drop';



export interface DialogData {
    isAddedNewCategory: boolean;
    selectedLetCategoryCode : LetCategoryCode
}

  @Component({
    selector: "add-category-dialog",
    templateUrl: "add-category-dialog.html",
    styleUrls: ['./category-maint.component.css']
  })
  export class AddCategoryDialog implements OnInit {
   
    addCatForm: FormGroup;
    submitted = false;
    selectedRow: number;
    highlightedRow: Number;
    selectRowData: LetPartialCheck= new LetPartialCheck();
    selectRow: Function;
    highlightInspection: Function;
    public letCategoryCode : LetCategoryCode  = new LetCategoryCode ();
    public letPartialCheck: LetPartialCheck = new LetPartialCheck();
    public letPartialCheckId: LetPartialCheckId = new LetPartialCheckId();
    public inspectionList: LetPartialCheck[];
    public selectedInspectionList: LetPartialCheck[];
    public categoryListNames:string[];
    
    public catId:string;
    public catNameInDialog: string;
    public descriptionInDialog: string;
    public inclusionInDialog: boolean;
    public inspectionNameInDialog: string;
    
    public selectedCatNameInDialog: string;
    public selectedDescriptionInDialog: string;
    public selectedInclusionInDialog: boolean;
    public selectedInspectionNameInDialog: string;

    public isAddNew:boolean=true;


    inspectionDisplayedColumns = ['letInspectionName'];
    inspectionDataSource: MatTableDataSource<LetPartialCheck>;

    constructor(
      private formBuilder: FormBuilder,
      public cdk: OverlayContainer,
      public dialogRef: MatDialogRef<AddCategoryDialog>,
      @Inject(MAT_DIALOG_DATA) public data: any,
      public categoryMaintService: CategoryMaintService
      
    ) {
        this.isAddNew = this.data.isAddedNewCategory;
        this.categoryListNames = this.data.categoryListNames;
        if(!this.isAddNew){

          console.info("categoryCodeName - "+this.data.selectedLetCategoryCode.name);
          this.letCategoryCode = this.data.selectedLetCategoryCode;
          this.inspectionList = this.data.selectedInspectionList;
          this.catId = this.data.selectedLetCategoryCode.categoryCodeId;
         
        }

        this.addCatForm = this.formBuilder.group({
          catNameInDialog:  ['', [Validators.required]],
          descriptionInDialog:['', [Validators.required]],
          inclusionInDialog:[''],
          inspectionNameInDialog:['']
      });

        this.selectRow = function(index, row){
        this.selectedRow = index;
        this.selectRowData = row;
        this.populateLetInspectionNames();
      }

      this.highlightInspection = function(index){
        this.highlightedRow = index;
      }}

    ngOnInit(): void {
              
       
        if(!this.isAddNew){
          console.info("categoryCodeName - "+this.letCategoryCode.name);
          this.addCatForm.get('catNameInDialog').setValue( this.letCategoryCode.name);
          this.addCatForm.get('descriptionInDialog').setValue( this.letCategoryCode.description);
          this.addCatForm.get('inclusionInDialog').setValue(this.letCategoryCode.inclusive);
          this.categoryListNames.forEach((element,index)=>{
            if(element === this.letCategoryCode.name) this.categoryListNames.splice(index,1);
          }); 
        }
          this.populateLetInspectionNames();
      }

    populateLetInspectionNames(){
         this.inspectionDataSource = new MatTableDataSource(this.inspectionList);
    }

    cancel(): void {
        this.dialogRef.close({ isAddedNewCategory: this.data.isAddedNewCategory });
    }

    updateInclusion(event: any){
      console.info("categoryCodeInclusive - "+this.addCatForm.get('inclusionInDialog').value)
      //this.letCategoryCode.inclusive = isChecked;
    }
    addToList(inspectionName:string,catName:string,description:string): void {
     
      console.info("categoryCodeNameAdd - "+catName);

       this.letCategoryCode.name = catName;
       this.letCategoryCode.description = description;
       this.letCategoryCode.inclusive = this.addCatForm.get('inclusionInDialog').value;
      
        if(inspectionName){
          console.info("inspectionNameAdd - "+inspectionName);
            inspectionName = inspectionName.trim();
            let tempLetPartialCheck: LetPartialCheck = new LetPartialCheck();
            let tempLetPartialCheckId: LetPartialCheckId = new LetPartialCheckId();
            tempLetPartialCheckId.categoryCodeId = this.letCategoryCode.categoryCodeId;
            tempLetPartialCheckId.letInspectionName = inspectionName;
            tempLetPartialCheck.id=tempLetPartialCheckId;
 
            
            var present = this.inspectionList && this.inspectionList.length > 0 ?this.inspectionList.map(x=>x.id.letInspectionName).indexOf(inspectionName) != -1 :false;
            console.log(present +"-present");
            if(!present){
             this.inspectionDataSource.data.push(tempLetPartialCheck);
             this.inspectionList = this.inspectionDataSource.data;
             console.log(this.inspectionDataSource.data.map(x=>x.id.letInspectionName));
              console.log(this.inspectionList.map(x=>x.id.letInspectionName));
             this.ngOnInit();
            }else{
              alert("Inspection Name - "+inspectionName +" already added.")
              console.log("already exists---"+tempLetPartialCheckId);
            }
        }
    }
    removeFromList(): void {
        console.info("selected Name - "+this.selectRowData.id.letInspectionName);
        console.info("row index - "+this.selectedRow);
        if(this.inspectionDataSource.data.length === 1){
          alert("Atleast one inspection required per Category ");
          return;
        }
        //this.inspectionDataSource.data.splice(this.selectedRow) ;
        this.inspectionDataSource.data.forEach((element,index)=>{
          if(element === this.selectRowData) this.inspectionDataSource.data.splice(index,1);
       });
        this.inspectionList =  this.inspectionDataSource.data;
        console.log(this.inspectionDataSource.data.map(x=>x.id.letInspectionName));
        console.log(this.inspectionList.map(x=>x.id.letInspectionName));
        this.ngOnInit();
    }

    save(): void {
      this.submitted = true;
      
      if (this.addCatForm.invalid) {
        return;
      }
      if(this.inspectionDataSource.data.length <=0){
        alert("Atleast one inspection required for Category ");
        return;
      } 
     
      
      if(this.categoryListNames.indexOf(this.addCatForm.get('catNameInDialog').value) !== -1 ){
        alert("Category Name already exists. ");
        return;
      }
      this.selectedInspectionList = this.inspectionDataSource.data;
     
      this.letCategoryCode.categoryCodeId = this.catId;
      this.letCategoryCode.name = this.addCatForm.get('catNameInDialog').value;
      this.letCategoryCode.description = this.addCatForm.get('descriptionInDialog').value;
      this.letCategoryCode.inclusive = this.addCatForm.get('inclusionInDialog').value;
      
      this.categoryMaintService.saveCategory(this.letCategoryCode,this.selectedInspectionList.map(x=>x.id.letInspectionName))
        .subscribe((letCategoryCode: any) => {
          console.log("Category Added")
          this.data.isAddedNewCategory = true;
          this.cancel();
        },(error: any) => {
          alert("Error Occured while adding Category");
          this.data.isAddedNewCategory = false;
          console.log(error);
          return;
        });

    }

    toTop(id) {
        this.cdk.getContainerElement().childNodes.forEach((x: any) => {
            if (x.innerHTML.indexOf('id="' + id + '"') <= 0)
            x.style["z-index"] = 1000;
            else x.style["z-index"] = 1001;
        });
    }
       
  get f() { return this.addCatForm.controls; }

}
