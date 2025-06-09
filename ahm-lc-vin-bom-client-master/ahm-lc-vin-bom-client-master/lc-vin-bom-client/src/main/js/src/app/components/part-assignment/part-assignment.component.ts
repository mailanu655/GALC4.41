import { OverlayContainer } from '@angular/cdk/overlay';
import { SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, Inject, ElementRef, OnInit, ViewChild, Input } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { FormControl } from '@angular/forms';
import { ModelPart } from 'src/app/models/model-part';

import { VinBomPartDto } from 'src/app/models/vin-bom-part-dto';
import { VinBomPartSetDto } from 'src/app/models/vin-bom-part-set-dto';
import { FrameSpec } from 'src/app/models/frame-spec';
import { ModelYearCode } from 'src/app/models/model-year-code';
import { Message } from 'src/app/models/message';
import { FrameSpecService } from 'src/app/services/frame-spec.service';
import { PartAssignmentService } from 'src/app/services/part-assignment.service';
import { AlertService } from 'src/app/alert/alert.service';
import { ConfirmationDialogService } from 'src/app/confirmation-dialog/confirmation-dialog.service';
import { SecurityService } from 'src/app/services/security.service';
import { UtilityService } from 'src/app/services/utility-service';
import { UtilityComponent } from '../header/utility.component';
import { Title } from '@angular/platform-browser';
//import { AddBomPartDialog } from './add-bom-part-dialog';
import { AddBomPartComponent } from './add-bom-part/add-bom-part.component';

export interface DialogData {
  isAddedNewBomPart: boolean;
}

@Component({
  selector: 'app-part-assignment',
  templateUrl: './part-assignment.component.html',
  styleUrls: ['./part-assignment.component.css']
})
export class PartAssignmentComponent implements OnInit, AfterViewInit {

  public loading = false;
  public message: Message = { type: null, message: null };
  public bomPartList: VinBomPartDto[];
  public modelYearList: string[];
  public selecteModelYearCodeList: string[];
  public modelCodeList: string[];
  public modelTypeList: string[];
  public modelYearCodeList: string[];
  public modelTypeCodeList: string[];
  public dcPartNumberList: string[];
  public letSystemNameList: string[];
  public modelYearCodeString: string[];
  public activeList: string[];
  public frameSpecList: FrameSpec[]
  public modelYearCode: string;
  public modelCode: string;
  public modelTypeCode: string;
  public dcPartNumber: string;
  public letSystemName: string;
  public active: string;
  remFilter:string;

  isAddedNewBomPart: boolean;
  selectRowData: VinBomPartDto = new VinBomPartDto();
  selectedRow: number;
  date: Date;
  highlightedRow: Number;
  selectRow: Function;
  highlightBomPart: Function;
  displayedColumns = ['select','modelYearCode', 'modelCode', 'modelTypeCode', 'dcPartNumber', 'letSystemName', 'active'];
  dataSource: MatTableDataSource<VinBomPartDto>;
  selection = new SelectionModel<VinBomPartDto>(true, []);
  @ViewChild(MatSort) sort: MatSort;

  modelYearCodeFilter = new FormControl(['all']);
  modelCodeFilter = new FormControl(['all']);
  modelTypeCodeFilter = new FormControl(['all']);
  dcPartNumberFilter = new FormControl(['all']);
  letSystemNameFilter = new FormControl(['all']);
  activeFilter = new FormControl(['all']);

  tempModelYear: any = [];
  tempModel: any = [];
  tempModelType: any = [];
  tempSystemName: any = [];
  tempActive: any = [];
  tempDcPartNumber: any = [];

  filterValues = {
    modelYearCode: ['all'],
    modelCode: ['all'],
    modelTypeCode: ['all'],
    dcPartNumber: ['all'],
    letSystemName: ['all'],
    active: ['all']
  };
  messageOptionsAutoClose = {
    autoClose: false,
    keepAfterRouteChange: false
  };

  messageOptions = {
    autoClose: false,
    keepAfterRouteChange: false
  };

  constructor(
      public dialog: MatDialog,
      public cdk: OverlayContainer,
      protected alertService: AlertService,
      private titleService: Title,
      private util: UtilityService,
      public partAssignmentService: PartAssignmentService,
      public frameSpecService: FrameSpecService,
      private confirmationDialogService: ConfirmationDialogService,
      private securityService: SecurityService
    ) { 

      this.titleService.setTitle("SUMS-VIN BOM Part Assignment");

      this.selectRow = function(index, row){
        this.alertService.clear();
        this.selectedRow = index;
        this.selectRowData = row;

        if(this.selectRowData.active != '*NOT SET*'){                     
            this.confirmationDialogService.alert("WARNING:","The selected row, the rule is already created!" );                        
        } else if(this.selectRowData.letSystemName == '*NOT SET*'){
            this.confirmationDialogService.alert("WARNING:","The selected row, not able to create rule!" );           
        }
      }

      this.highlightBomPart = function(index){
        this.highlightedRow = index;
      }

  }

  selectionModelChange(value) {
    console.log('dc model change value --', value);
    this.modelCodeFilter.setValue(value);
  }
  selectionModelTypeChange(value) {
    console.log('dc model type value --', value);
    this.modelTypeCodeFilter.setValue(value);
  }
  selectionSystemNameChange(value) {
    console.log('dc systemname value --', value);
    this.letSystemNameFilter.setValue(value);
  }
  selectionActiveChange(value) {
    console.log('active value --', value);
    this.activeFilter.setValue(value);
  }
  selectionDcPartNumberChange(value) {
    console.log('dc part number value --', value);
    this.dcPartNumberFilter.setValue(value);
  }

  selectionModelYearChange(value) {
    console.log('model year value --', value);
    this.modelYearCodeFilter.setValue(value);
  }

  ngOnInit(): void {
    this.modelYearCode = '';
    this.modelCode = '';
    this.modelTypeCode = '';
    this.dcPartNumber = '';
    this.letSystemName = '';
    this.active= '';
    
    //this.populateModelYear();
    this.search();


    this.modelYearCodeFilter.valueChanges
    .subscribe(
      modelYearCode => {
        this.selection.clear();
        
        if(modelYearCode.includes('all')){
          console.log('inside if block of per select ');
          if(modelYearCode[0]=='all' && modelYearCode.length>1 && this.tempModelYear?.length==0){
            console.log(' inside---  ',modelYearCode[0]);
            modelYearCode.splice(0,1);
            this.tempModelYear=modelYearCode;
          }
          else if(this.tempModelYear.length>0 && modelYearCode.includes('all')){
            modelYearCode.splice(1,modelYearCode.length);
            this.tempModelYear=[];
          }
        }                
        this.filterValues.modelYearCode = modelYearCode;        
        this.dataSource.filter = JSON.stringify(this.filterValues);       
      }      
    )

    // this.modelYearCodeFilter.valueChanges
    // .subscribe(
    //   modelYearCode => {
    //     this.selection.clear();
    //     this.filterValues.modelYearCode = modelYearCode;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    this.modelCodeFilter.valueChanges
    .subscribe(
      modelCode => {
        this.selection.clear();
        
        if(modelCode.includes('all')){
          console.log('inside if block of per select ');
          if(modelCode[0]=='all' && modelCode.length>1 && this.tempModel?.length==0){
            console.log(' inside---  ',modelCode[0]);
            modelCode.splice(0,1);
            this.tempModel=modelCode;
          }
          else if(this.tempModel.length>0 && modelCode.includes('all')){
            modelCode.splice(1,modelCode.length);
            this.tempModel=[];
          }
        }                
        this.filterValues.modelCode = modelCode;        
        this.dataSource.filter = JSON.stringify(this.filterValues);       
      }      
    )

    // this.modelCodeFilter.valueChanges
    // .subscribe(
    //   modelCode => {
    //     this.selection.clear();
    //     this.filterValues.modelCode = modelCode;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    this.modelTypeCodeFilter.valueChanges
    .subscribe(
      modelTypeCode => {
        this.selection.clear();
        
        if(modelTypeCode.includes('all')){
          console.log('inside if block of per select ');
          if(modelTypeCode[0]=='all' && modelTypeCode.length>1 && this.tempModelType?.length==0){
            console.log(' inside---  ',modelTypeCode[0]);
            modelTypeCode.splice(0,1);
            this.tempModelType=modelTypeCode;
          }
          else if(this.tempModelType.length>0 && modelTypeCode.includes('all')){
            modelTypeCode.splice(1,modelTypeCode.length);
            this.tempModel=[];
          }
        }        
        
        this.filterValues.modelTypeCode = modelTypeCode;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )

    // this.modelTypeCodeFilter.valueChanges
    // .subscribe(
    //   modelTypeCode => {
    //     this.selection.clear();
    //     this.filterValues.modelTypeCode = modelTypeCode;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    this.dcPartNumberFilter.valueChanges
    .subscribe(
      dcPartNumber => {
        this.selection.clear();
        
        if(dcPartNumber.includes('all')){
          console.log('inside if block of per select ');
          if(dcPartNumber[0]=='all' && dcPartNumber.length>1 && this.tempDcPartNumber?.length==0){
            console.log(' inside---  ',dcPartNumber[0]);
            dcPartNumber.splice(0,1);
            this.tempDcPartNumber=dcPartNumber;
          }
          else if(this.tempDcPartNumber.length>0 && dcPartNumber.includes('all')){
            dcPartNumber.splice(1,dcPartNumber.length);
            this.tempDcPartNumber=[];
          }
        }        
        
        this.filterValues.dcPartNumber = dcPartNumber;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )

    // this.dcPartNumberFilter.valueChanges
    // .subscribe(
    //   dcPartNumber => {
    //     this.selection.clear();
    //     this.filterValues.dcPartNumber = dcPartNumber;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    this.letSystemNameFilter.valueChanges
    .subscribe(
      letSystemName => {
        this.selection.clear();
        
        if(letSystemName.includes('all')){
          console.log('inside if block of per select ');
          if(letSystemName[0]=='all' && letSystemName.length>1 && this.tempSystemName?.length==0){
            console.log(' inside---  ',letSystemName[0]);
            letSystemName.splice(0,1);
            this.tempSystemName=letSystemName;
          }
          else if(this.tempSystemName.length>0 && letSystemName.includes('all')){
            letSystemName.splice(1,letSystemName.length);
            this.tempSystemName=[];
          }
        }        
        
        this.filterValues.letSystemName = letSystemName;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )

    // this.letSystemNameFilter.valueChanges
    // .subscribe(
    //   letSystemName => {
    //     this.selection.clear();
    //     this.filterValues.letSystemName = letSystemName;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    this.activeFilter.valueChanges
    .subscribe(
      active => {
        this.selection.clear();
        
        if(active.includes('all')){
          console.log('inside if block of per select ');
          if(active[0]=='all' && active.length>1 && this.tempActive?.length==0){
            console.log(' inside---  ',active[0]);
            active.splice(0,1);
            this.tempActive=active;
          }
          else if(this.tempActive.length>0 && active.includes('all')){
            active.splice(1,active.length);
            this.tempActive=[];
          }
        }        
        
        this.filterValues.active = active;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )

    // this.activeFilter.valueChanges
    // .subscribe(
    //   active => {
    //     this.selection.clear();
    //     this.filterValues.active = active;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

  }

  // populateAllModelType() {
  //   this.frameSpecService.findAllModelTypebyProductType('FRAME')
  //     .subscribe((modelTypeList: string[]) => {

  //       this.modelTypeList = modelTypeList;
  //     },(error: any) => {
        
  //     });
    
  // }

  // populateModelTypebyYMTOC(modelYearCode: string, modelCode: string) {
  //   this.frameSpecService.findModelTypebyYMTOC(modelYearCode, modelCode, '', '', '', '')
  //     .subscribe((modelTypeList: string[]) => {

  //       this.modelTypeList = modelTypeList;
  //     },(error: any) => {
        
  //     });
    
  // }

  // populateModelYear() {
  //   this.frameSpecService.findAllModelYearCodes()
  //     .subscribe((modelYearList: string[]) => {
  //       this.modelYearList = modelYearList;
  //       this.populateAllModelCode();
  //       this.populateModelTypebyYMTOC(this.modelYearCode, this.modelCode);
  //     },(error: any) => {
        
  //     });
  // }
  
  // populateModelCodeByYear(modelYear: string) {
  //   this.selecteModelYearCodeList = [];
  //   this.selecteModelYearCodeList.push(modelYear);
  //   this.frameSpecService.findModelCodeByYear(this.selecteModelYearCodeList)
  //     .subscribe((modelCodeList: string[]) => {
  //       this.modelCodeList = modelCodeList;
  //       console.log(this.modelCodeList);
  //     },(error: any) => {
      
  //   });
  //   this.populateModelTypebyYMTOC(modelYear, '');
  // }

  // populateModelTypeByYearAndCode(modelYear: string, modelCode: string) {
  //   this.alertService.clear();
  //   if(modelYear == '' && modelCode == '') {
  //     this.populateAllModelType();
  //   } else {
  //     this.modelTypeList = [];
  //     this.frameSpecService.findAllByYMTOCWildCard(modelYear, modelCode)
  //     .subscribe((frameSpecList: FrameSpec[]) => {
  //       this.frameSpecList = frameSpecList;
  //       console.log(this.frameSpecList);
  //       for (const frameSpec of this.frameSpecList) {
  //         let mt = frameSpec.modelTypeCode;
  //         if(this.modelTypeList.includes(mt)) {

  //         } else {
  //           this.modelTypeList.push(mt);
  //         }
  //       }
  //     },(error: any) => {
  //       console.log(error);
  //     });
  //   }
  // }

  // populateAllModelCode() {
  //   this.frameSpecService.findModelCodeByYear(this.modelYearList)
  //       .subscribe((modelCodeList: string[]) => {

  //         this.modelCodeList = modelCodeList;
  //         console.log(this.modelCodeList);
  //       },(error: any) => {
          
  //       });
  // }

  search() {
    this.loading = true;
    this.partAssignmentService.search(this.modelYearCode, this.modelCode, this.modelTypeCode, this.dcPartNumber, this.letSystemName)
      .subscribe((bomPartList: VinBomPartDto[]) => {
        this.bomPartList = bomPartList;
        this.modelYearCodeList = (Array.from(new Set(this.bomPartList.map(ele=>ele.modelYearCode)))).sort();
        this.modelCodeList = (Array.from(new Set(this.bomPartList.map(ele=>ele.modelCode)))).sort();
        this.modelTypeCodeList = (Array.from(new Set(this.bomPartList.map(ele=>ele.modelTypeCode)))).sort();
        this.dcPartNumberList = (Array.from(new Set(this.bomPartList.map(ele=>ele.dcPartNumber)))).sort();
        this.letSystemNameList = (Array.from(new Set(this.bomPartList.map(ele=>ele.letSystemName)))).sort();
        this.activeList = (Array.from(new Set(this.bomPartList.map(ele=>ele.active)))).sort();
        
        this.dataSource = new MatTableDataSource(bomPartList);
        this.dataSource.sort = this.sort;
        this.dataSource.filterPredicate = this.createFilter(); 
        this.loading = false;
        if(this.remFilter){
          this.filterValues = JSON.parse(this.remFilter);
          this.dataSource.filter = this.remFilter;
        }
      },(error: any) => {
        this.loading = false;
      });
  }

  reload() {
    this.alertService.clear();
    this.confirmationDialogService.confirm('', 'Are you sure you want to Reload Vin Bom Parts? ', 'Yes', 'No', 'lg')
    .then((confirmed) => {
      if(confirmed) {
        this.loading = true;
        this.partAssignmentService.updateBeamPartData()
          .subscribe(() => {
            this.search();
            this.alertService.success('Reload success!');
            this.loading = false;
          },(error: any) => {
            alert("Error");
            console.log(error);
            this.loading = false;
          });
      }
    });
  }

  createActiveRule(){
    this.alertService.clear();
 
    if(this.selection.selected.length <= 0) {
      alert("Please select at least one row from above grid");
      return;
    }

    if(this.selection.selected.length > 250) { 
      alert("Resultset exceeds max size : 250, please select additional criteria to filter data. "); 
      return;
    }

    const response =  this.doCreateRules(this.selection.selected);
    this.loading = false;

  }

  doCreateRules(selected: VinBomPartDto[]) {
    this.confirmationDialogService.confirm('', 'Are you sure you want to Create ' + this.selection.selected.length +' Active Rule? ', 'OK', 'No', 'lg')
    .then((confirmed) => {
      if(confirmed) {
        let vinBomPartSetDto = new VinBomPartSetDto();
        vinBomPartSetDto.associate = this.securityService.getUser();
        vinBomPartSetDto.vinBomPartList = selected;
        this.loading = true;
        this.remFilter = JSON.stringify(this.filterValues);
        this.partAssignmentService.createVinBomRules(vinBomPartSetDto).subscribe(data => {
            console.log(data);
            this.search();  
            this.loading = true; 
            this.selection.clear();         
          },(error: any) => {
            alert("Error creating Rules");
            this.loading = true;
          });
        }
    }).catch(() => {
       alert("Error creating Rules");  
       this.loading = true;
    });

  }

  isVisible(): boolean {
    return this.securityService.isUserInRole('override_approval');
  }

  isActive(dto:VinBomPartDto) : boolean{
     if(dto.active != '*NOT SET*'){
      this.selection.deselect(dto);     
     } else if(dto.letSystemName == '*NOT SET*') {
      this.selection.deselect(dto); 
     }

     return dto.active != '*NOT SET*' || dto.letSystemName =='*NOT SET*';
  }

  addBomPartOpenDialog() {
    this.alertService.clear();
    const dialogRef = this.dialog.open(AddBomPartComponent, {
      width: "60%",
      height: '50%',
      data: { isAddedNewBomPart: false },
      id: "parent",
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log("The dialog was closed");
      this.isAddedNewBomPart = result.isAddedNewBomPart;
      console.log("Added new Part: " + this.isAddedNewBomPart);
      if(this.isAddedNewBomPart) {
        this.alertService.success('Part added succesfully!', this.messageOptionsAutoClose);
        this.search();
      }
      this.dialog.closeAll();
    });
  }

  removeBomPart() {
    this.alertService.clear();
    this.remFilter = JSON.stringify(this.filterValues);
    this.confirmationDialogService.confirm('', 'Are you sure you want to delete part? ', 'Yes', 'No', 'lg')
    .then((confirmed) => {
      if(confirmed) {
          this.partAssignmentService.removeBomPart(this.selectRowData).subscribe(data => {
            console.log(data);
            this.search(); 
            this.alertService.success('Removed', this.messageOptionsAutoClose);
          },(error: any) => {
            alert("Error removing Part");
          });
        }
      else {
              
      }
    }).catch(() => {
      
    });
  

  }

  ngAfterViewInit() {

  }
  
  // checkModelCodeExistence(modelCode: string): boolean {
  //   return this.modelYearCodeList.some(r => r.modelCode === modelCode);
  // }

  // checkModelTypeExistence(modelTypeCode: string): boolean {
  //   return this.modelTypeList.some(r => r.modelTypeCode === modelTypeCode);
  // }
  //displayedColumns = ['modelYearCode', 'modelCode', 'modelTypeCode', 'dcPartNumber', 'letSystemName'];



  createFilter(): (data: any, filter: string) => boolean {
    let filterFunction = function(data, filter): boolean {
      let searchTerms = JSON.parse(filter);

      let systemNameArray = searchTerms.letSystemName as Array<String>;
      let modelTypeArray = searchTerms.modelTypeCode as Array<String>;
      let dcPartNumberArray = searchTerms.dcPartNumber as Array<String>;
      let modelCodeArray = searchTerms.modelCode as Array<String>;      
      let modelYearArray = searchTerms.modelYearCode as Array<String>;      
	  let activeArray = searchTerms.active as Array<String>;

    let modelTypeFlag=(modelTypeArray.includes('all') ) ? true : modelTypeArray.includes(data.modelTypeCode.toLowerCase());
let systemNameFlag=(systemNameArray.includes('all') ) ? true : systemNameArray.includes(data.letSystemName.toLowerCase());
let dcPartNumberFlag=(dcPartNumberArray.includes('all') ) ? true : dcPartNumberArray.includes(data.dcPartNumber.toLowerCase());
let modelCodeFlag=(modelCodeArray.includes('all')) ? true : modelCodeArray.includes(data.modelCode.toLowerCase());
let activeFlag=(activeArray.includes('all') ) ? true : activeArray.includes(data.active);
let modelYearFlag=(modelYearArray.includes('all') ) ? true : modelYearArray.includes(data.modelYearCode.toLowerCase());


      return modelYearFlag
      //data.modelYearCode.toLowerCase().indexOf(searchTerms.modelYearCode.toLowerCase()) !== -1
        && modelTypeFlag  
      //&& data.modelTypeCode.toLowerCase().indexOf(searchTerms.modelTypeCode.toLowerCase()) !== -1
        && modelCodeFlag
      //&& data.modelCode.toLowerCase().indexOf(searchTerms.modelCode.toLowerCase()) !== -1
        && dcPartNumberFlag  
      //&& data.dcPartNumber.toLowerCase().indexOf(searchTerms.dcPartNumber.toLowerCase()) !== -1
        //&& data.letSystemName.toLowerCase().indexOf(searchTerms.letSystemName.toLowerCase()) !== -1
        && systemNameFlag
        //&& (searchTerms.letSystemName.toLowerCase() === 'all' || searchTerms.letSystemName.toLowerCase()==='') ? true : data.letSystemName.toLowerCase()===searchTerms.letSystemName.toLowerCase()
        && activeFlag;
        //&& (data.active.toLowerCase() == searchTerms.active.toLowerCase() || searchTerms.active == '') 
       
    }
    return filterFunction;
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    let totalCount = 0;
    this.dataSource.filteredData.forEach(element => {
      if(element.active == '*NOT SET*' && element.letSystemName !='*NOT SET*'){
         totalCount++;
      }
    });

    return numSelected == totalCount;
  }
  masterToggle() {
    if(this.isAllSelected()) {
        this.selection.clear() 
    }else{
       this.dataSource.filteredData.forEach(row => {
        if(row.active == '*NOT SET*') {
          this.selection.select(row)}
        });
    }
  }

}





