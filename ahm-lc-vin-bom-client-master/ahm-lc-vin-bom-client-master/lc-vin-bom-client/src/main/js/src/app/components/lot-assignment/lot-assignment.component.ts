import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { Title } from '@angular/platform-browser';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';

import { AlertService } from 'src/app/alert/alert.service';
import { ModelYearCode } from 'src/app/models/model-year-code';
import { FrameSpecService } from 'src/app/services/frame-spec.service';
import { LotAssignmentService } from 'src/app/services/lot-assignment.service';
import { UtilityService } from 'src/app/services/utility-service';
import { FormControl } from '@angular/forms';
import { ModelPartLotDto } from 'src/app/models/model-part-lot-dto';
import { Message } from 'src/app/models/message';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { SelectProdLotDialogComponent } from '../select-prod-lot-dialog/select-prod-lot-dialog.component';

@Component({
  selector: 'app-lot-assignment',
  templateUrl: './lot-assignment.component.html',
  styleUrls: ['./lot-assignment.component.css']
})
export class LotAssignmentComponent implements OnInit {

  public loading = false;
  public modelCodeList: string[];
  public modelTypeList: string[];
  public dcNumberList: string[];
  public dcEffDateList: string[];
  public systemNameList: string[];
  public dcClassList: string[];
  public startProdLotList: string[];
  public dcPartNumberList: string[];

  public lotList: ModelPartLotDto[];
  public modelYearCodeList: ModelYearCode[];
  public modelCode: string;
  public modelType: string;
  public dcNumber: string;
  public effBeginDate: string;
  public isUnProcessed: boolean;
  public reFlash: boolean;
  public interchangeable: boolean;
  public scrapParts: boolean;
  selectedRow: number;
  highlightedRow: Number;
  selectRow: Function;
  highlightLot: Function;
  selectedStartingProdLot: string;
  selectedLineNo: string;
  selectedFromDate: string;
  planCode: string;
  selectRowData: ModelPartLotDto[];
  remFilter:string;

  dataSource: MatTableDataSource<ModelPartLotDto>;
  displayedColumns = ['select', 'dcNumber', 'model', 'modelType', 'dcPartNumber', 'letSystemName', 'dcClass', 'reflash', 'scrapParts', 'interchangeable', 'dcEffBegDate', 'startingProductionLot'];
  selection = new SelectionModel<ModelPartLotDto>(true, []);
  @ViewChild(MatSort) sort: MatSort;

  modelFilter = new FormControl(['all']);
  modelTypeFilter = new FormControl(['all']);
  letSystemNameFilter = new FormControl(['all']);
  dcPartNumberFilter = new FormControl(['all']);
  dcNumberFilter = new FormControl(['all']);
  dcClassFilter = new FormControl(['all']);
  dcEffBegDateFilter = new FormControl(['all']);
  reflashFilter = new FormControl('');
  scrapPartsFilter = new FormControl('');
  interchangeableFilter = new FormControl('');
  startingProductionLotFilter = new FormControl(['all']);
  dcClassAllSelect: boolean = true;
  filterValues = {
    model: ['all'],
    modelType: ['all'],
    letSystemName: ['all'],
    dcPartNumber: ['all'],
    dcNumber: ['all'],
    dcClass: ['all'],
    dcEffBegDate: ['all'],
    reflash: '',
    scrapParts: '',
    interchangeable: '',
    startingProductionLot: ['all']
  };

  public message: Message = { type: null, message: null };
  messageOptionsAutoClose = {
    autoClose: false,
    keepAfterRouteChange: false
  };
  tempDcClass: any = [];
  tempModel: any = [];
  tempModelType: any = [];
  tempSystemName: any = [];
  tempDcNumber: any = [];
  tempDcPartNumber: any = [];
  tempEffBeginDate: any = [];
  tempStartProdLot: any = [];

  constructor(
    public dialog: MatDialog,  
    public cdk: OverlayContainer, 
    protected alertService: AlertService,
    private titleService: Title,
    public lotAssignmentService: LotAssignmentService,
    public frameSpecService: FrameSpecService
  ) { 

    this.titleService.setTitle("SUMS-VIN BOM Lot Assignment");


    this.selectRow = function(index, row){
      this.selectedRow = index;
    }

    this.highlightLot = function(index){
      this.highlightedRow = index;
    }
    
  }

  
  selectionDcClassChange(value) {
    console.log('dc class change value --', value);
    this.dcClassFilter.setValue(value);
  }
  
  selectionModelChange(value) {
    console.log('dc model change value --', value);
    this.modelFilter.setValue(value);
  }
  selectionModelTypeChange(value) {
    console.log('dc model type value --', value);
    this.modelTypeFilter.setValue(value);
  }
  selectionSystemNameChange(value) {
    console.log('dc systemname value --', value);
    this.letSystemNameFilter.setValue(value);
  }
  selectionDcNumberChange(value) {
    console.log('dc number value --', value);
    this.dcNumberFilter.setValue(value);
  }
  selectionDcPartNumberChange(value) {
    console.log('dc part number value --', value);
    this.dcPartNumberFilter.setValue(value);
  }
  selectionEffBeginDateChange(value) {
    console.log('dc eff begin dt value --', value);
    this.dcEffBegDateFilter.setValue(value);
  }
  selectionStartProdLotChange(value) {
    console.log('dc start prod lot value --', value);
    this.startingProductionLotFilter.setValue(value);
  }

  ngOnInit(): void {
    
    this.search();   

    this.modelFilter.valueChanges
    .subscribe(
      model => {
        this.selection.clear();
        
        if(model.includes('all')){
          console.log('inside if block of per select ');
          if(model[0]=='all' && model.length>1 && this.tempModel?.length==0){
            console.log(' inside---  ',model[0]);
            model.splice(0,1);
            this.tempModel=model;
          }
          else if(this.tempModel.length>0 && model.includes('all')){
            model.splice(1,model.length);
            this.tempModel=[];
          }
        }        
        
        this.filterValues.model = model;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
        
       
      }      
    )

    // this.modelFilter.valueChanges
    // .subscribe(
    //   model => {
    //     this.selection.clear();
    //     this.filterValues.model = model;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    this.modelTypeFilter.valueChanges
    .subscribe(
      modelType => {
        this.selection.clear();
        
        if(modelType.includes('all')){
          console.log('inside if block of per select ');
          if(modelType[0]=='all' && modelType.length>1 && this.tempModelType?.length==0){
            console.log(' inside---  ',modelType[0]);
            modelType.splice(0,1);
            this.tempModelType=modelType;
          }
          else if(this.tempModelType.length>0 && modelType.includes('all')){
            modelType.splice(1,modelType.length);
            this.tempModel=[];
          }
        }        
        
        this.filterValues.modelType = modelType;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )

    // this.modelTypeFilter.valueChanges
    // .subscribe(
    //   modelType => {
    //     this.selection.clear();
    //     this.filterValues.modelType = modelType;
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

    this.dcNumberFilter.valueChanges
    .subscribe(
      dcNumber => {
        this.selection.clear();
        if(dcNumber.includes('all')){
          console.log('inside if block of per select ');
          if(dcNumber[0]=='all' && dcNumber.length>1 && this.tempDcNumber?.length==0){
            console.log(' inside---  ',dcNumber[0]);
            dcNumber.splice(0,1);
            this.tempDcNumber=dcNumber;
          }
          else if(this.tempDcNumber.length>0 && dcNumber.includes('all')){
            dcNumber.splice(1,dcNumber.length);
            this.tempDcNumber=[];
          }
        }        
        this.filterValues.dcNumber = dcNumber;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }      
    )

    // this.dcNumberFilter.valueChanges
    // .subscribe(
    //   dcNumber => {
    //     this.selection.clear();
    //     this.filterValues.dcNumber = dcNumber;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )
    this.dcClassFilter.valueChanges
    .subscribe(
      dcClass => {
        this.selection.clear();
        console.log(' beforee splice selected dcClass---  ',dcClass);
        if(dcClass.includes('all')){
          console.log('inside if block of per select ');
          if(dcClass[0]=='all' && dcClass.length>1 && this.tempDcClass?.length==0){
            console.log(' inside---  ',dcClass[0]);
            dcClass.splice(0,1);
            this.tempDcClass=dcClass;
          }
          else if(this.tempDcClass.length>0 && dcClass.includes('all')){
            dcClass.splice(1,dcClass.length);
            this.tempDcClass=[];
          }
        }        
        console.log('after splice selected dcClass---  ',dcClass);
        this.filterValues.dcClass = dcClass;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
        console.log('after splice temp dcClass---  ',this.tempDcClass);
       
      }      
    )

    this.dcEffBegDateFilter.valueChanges
    .subscribe(
      dcEffBegDate => {
        this.selection.clear();
        if(dcEffBegDate.includes('all')){
          console.log('inside if block of per select ');
          if(dcEffBegDate[0]=='all' && dcEffBegDate.length>1 && this.tempEffBeginDate?.length==0){
            console.log(' inside---  ',dcEffBegDate[0]);
            dcEffBegDate.splice(0,1);
            this.tempEffBeginDate=dcEffBegDate;
          }
          else if(this.tempEffBeginDate.length>0 && dcEffBegDate.includes('all')){
            dcEffBegDate.splice(1,dcEffBegDate.length);
            this.tempEffBeginDate=[];
          }
        }        
        this.filterValues.dcEffBegDate = dcEffBegDate;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }      
    )

    // this.dcEffBegDateFilter.valueChanges
    // .subscribe(
    //   dcEffBegDate => {
    //     this.selection.clear();
    //     this.filterValues.dcEffBegDate = dcEffBegDate;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )
    this.reflashFilter.valueChanges
    .subscribe(
      reflash => {
        this.selection.clear();
        this.filterValues.reflash = reflash;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.scrapPartsFilter.valueChanges
    .subscribe(
      scrapParts => {
        this.selection.clear();
        this.filterValues.scrapParts = scrapParts;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.interchangeableFilter.valueChanges
    .subscribe(
      interchangeable => {
        this.selection.clear();
        this.filterValues.interchangeable = interchangeable;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    //this.startingProductionLotFilter.setValue('All');

    this.startingProductionLotFilter.valueChanges
    .subscribe(
      startingProductionLot => {
        this.selection.clear();
        if(startingProductionLot.includes('all')){
          console.log('inside if block of per select ');
          if(startingProductionLot[0]=='all' && startingProductionLot.length>1 && this.tempStartProdLot?.length==0){
            console.log(' inside---  ',startingProductionLot[0]);
            startingProductionLot.splice(0,1);
            this.tempStartProdLot=startingProductionLot;
          }
          else if(this.tempStartProdLot.length>0 && startingProductionLot.includes('all')){
            startingProductionLot.splice(1,startingProductionLot.length);
            this.tempStartProdLot=[];
          }
        }        
        this.filterValues.startingProductionLot = startingProductionLot;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }      
    )

    // this.startingProductionLotFilter.valueChanges
    // .subscribe(
    //   startingProductionLot => {
    //     this.selection.clear();
    //     this.filterValues.startingProductionLot = startingProductionLot;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    
    this.modelCode = '0';
    this.modelType = '0';
   // this.populateAllModelType();
    
  }

  populateAllModelType() {
    this.frameSpecService.findAllModelTypebyProductType('FRAME')
      .subscribe((modelTypeList: string[]) => {
        this.modelTypeList = modelTypeList;
      },(error: any) => {
        
      });
  }

  search(): void {
    this.selection.clear();
    this.loading = true;
    this.lotAssignmentService.getAvailableLotAssignments()
      .subscribe((lotList: ModelPartLotDto[]) => {
        var replaedNull = JSON.stringify(lotList).replace(/null/g, '""');
        var newArray = JSON.parse(replaedNull);
        this.lotList = newArray;
        this.modelCodeList = (Array.from(new Set(this.lotList.map(ele=>ele.model)))).sort();  
        this.modelTypeList = (Array.from(new Set(this.lotList.map(ele=>ele.modelType)))).sort();  
        this.dcNumberList = (Array.from(new Set(this.lotList.map(ele=>ele.dcNumber)))).sort();  
        this.dcEffDateList = (Array.from(new Set(this.lotList.map(ele=>ele.dcEffBegDate)))).sort();  
        this.systemNameList = (Array.from(new Set(this.lotList.map(ele=>ele.letSystemName)))).sort();  
        this.dcClassList = (Array.from(new Set(this.lotList.map(ele=>ele.dcClass)))).sort();  
        this.startProdLotList = (Array.from(new Set(this.lotList.map(ele=>ele.startingProductionLot)))).sort();  
        this.dcPartNumberList = (Array.from(new Set(this.lotList.map(ele=>ele.dcPartNumber)))).sort();  

        console.log('this.lotList lot assignment -----', this.lotList);

        //NALC-1093
        let sortedList = this.lotList.sort((a,b) => (a.dcPartNumber < b.dcPartNumber) ? -1 : 1).
        sort((a,b) => (a.modelType < b.modelType) ? -1 : 1).
        sort((a,b) => (a.model < b.model) ? -1 : 1).
        sort((a, b) => (a.startingProductionLot < b.startingProductionLot) ? -1 : 1)
        ;
        console.log('sortedList statprodlot ---- ',JSON.stringify(sortedList));

        this.dataSource = new MatTableDataSource(sortedList);
        this.dataSource.sort = this.sort;
        this.dataSource.filterPredicate = this.createFilter();
        if(this.remFilter){
          console.log("setting filter to - "+this.remFilter);
          this.filterValues=JSON.parse(this.remFilter);
          this.dataSource.filter = this.remFilter;
          } 
          this.loading = false;
      },(error: any) => {
        this.loading = false;
      });
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.filteredData.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    if(this.isAllSelected()) {
        this.selection.clear() 
    }else{
       this.dataSource.filteredData.forEach(row => this.selection.select(row));
    }
  }


  toggle(item,event: MatCheckboxChange) {
    if (event.checked) {
     this.selectRowData.push(item);
    } else {
     const index = this.selectRowData.indexOf(item);
     if (index >= 0) {
       this.selectRowData.splice(index, 1);
     }
    }
    console.log(item + "<>", event.checked);
  }

  selectProdLotDialog() {
    if(this.selection.selected.length <= 0) {
      alert("Please select Part data from above grid");
      return;
    }
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);

    const dialogRef = this.dialog.open(SelectProdLotDialogComponent, {
      width: "75%",
      height: '75%',
      data: { isProdlotSelected: false, doSave: false },
      id: "parent",
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log("The dialog was closed");
      this.selectedStartingProdLot = result.selectedProdLot;
      this.selectedLineNo = result.selectedLine;
      this.selectedFromDate = result.fromDate;
      this.planCode = result.planCode;
      console.log("Selected Starting Ptod Lot: " + this.selectedStartingProdLot);
      this.dialog.closeAll();
    });
  }

  linkLot() {
    this.alertService.clear();
    if(this.selection.selected.length <= 0) {
      alert("Please select Part data from above grid");
      return;
    }
    if(this.selectedStartingProdLot === undefined || this.selectedStartingProdLot == null || this.selectedStartingProdLot.trim().length < 1) {
      alert("Please click on 3 dots and select a new Lot");
      return;
    }
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);
    

    //NALC-1083
    // if(this.selection.selected.length>50){
    //   alert("Resultset exceeds max size : 50, please select additional criteria to filter data.");
    //   return;
    // }
    this.loading=true;
    let i = 0;

    /*
    this.selection.selected.forEach(s => {
      console.log(s.dcPartNumber);
      if(this.planCode === undefined || this.planCode == '') {
        this.planCode = s.planCode;
      }
      
      console.log("modelLot - reflash,scrapParts,interchangeable = "+s.reflash, s.scrapParts, s.interchangeable);
      this.lotAssignmentService.saveModelLot(s, this.selectedStartingProdLot, this.planCode).subscribe(data => {
        this.lotAssignmentService.saveModelPartApproval(s, this.planCode, this.selectedStartingProdLot,  s.interchangeable, s.scrapParts,s.reflash).subscribe(data => {
          console.log(data);
          this.alertService.clear();
          this.alertService.success('Link Lot request has been sent for approval', this.messageOptionsAutoClose);
          console.log("count -"+i);
          console.log("selected -"+this.selection.selected.length);
          if(i === (this.selection.selected.length -1)){
            this.alertService.clear();
            this.alertService.success('All Link Lot requests for selected rows '+i +' sent for approval', this.messageOptionsAutoClose);
            this.search(); 
            this.selectedStartingProdLot = '';
            this.loading = false;
          }else{
            i++;
          }
          
        },(error: any) => {
          this.alertService.clear();
          alert("Error Linking Lot");
          this.loading = false;
        });
      
      },(error: any) => {
        this.alertService.clear();
        alert("Error Linking Lot");
        this.loading = false;
      });
     
      
    });*/
    
    
    this.lotAssignmentService.saveModelLotsAndModelPartApprovals(this.selection.selected, this.planCode, this.selectedStartingProdLot).subscribe(data => {
      console.log(data);
      this.alertService.clear();
      this.alertService.success('All Link Lot requests for selected rows sent for approval', this.messageOptionsAutoClose);
      this.search(); 
      this.selectedStartingProdLot = '';
      this.loading = false;

      },(error: any) => {
        this.alertService.clear();
        alert("Error Linking Lot");
        this.loading = false;
      });
      
    
  
  }

  createFilter(): (data: any, filter: string) => boolean {  
      
    
    let filterFunction = function(data, filter): boolean {
      let searchTerms = JSON.parse(filter);
      console.log('searchTerms----',searchTerms);
      let systemNameArray = searchTerms.letSystemName as Array<String>;
      let modelTypeArray = searchTerms.modelType as Array<String>;
      let dcPartNumberArray = searchTerms.dcPartNumber as Array<String>;
      let modelCodeArray = searchTerms.model as Array<String>;      
      let dcChangeClassArray = searchTerms.dcClass as Array<String>; 
      let effectiveBeginDateArray = searchTerms.dcEffBegDate as Array<String>;
	  let startingProductionLotArray = searchTerms.startingProductionLot as Array<String>;
	  let dcNumberArray = searchTerms.dcNumber as Array<String>;


      let startingProductionLotMatch: boolean=true;
      if(startingProductionLotArray.length === 0){
        startingProductionLotMatch = startingProductionLotArray.includes(data.startingProductionLot);
        console.log("startingProductionLot - "+String(data.startingProductionLot) +"-"+String(data.startingProductionLot).length +"-"+startingProductionLotMatch);
      }else if(startingProductionLotArray.includes('All')){
        startingProductionLotMatch = true;
       } else{
        startingProductionLotMatch = startingProductionLotArray.includes(data.startingProductionLot);
        console.log("startingProductionLot - "+String(data.startingProductionLot) +"-"+String(data.startingProductionLot).length +"-"+startingProductionLotMatch);
      }


      let modelTypeFlag=(modelTypeArray.includes('all') ) ? true : modelTypeArray.includes(data.modelType.toLowerCase());
      let systemNameFlag=(systemNameArray.includes('all') ) ? true : systemNameArray.includes(data.letSystemName.toLowerCase());
      let dcPartNumberFlag=(dcPartNumberArray.includes('all') ) ? true : dcPartNumberArray.includes(data.dcPartNumber.toLowerCase());
      let modelCodeFlag=(modelCodeArray.includes('all') ) ? true : modelCodeArray.includes(data.model.toLowerCase());
      let dcChangeClassFlag=(dcChangeClassArray.includes('all')) ? true : dcChangeClassArray.includes(data.dcClass.toLowerCase());
      let dcNumberFlag=(dcNumberArray.includes('all') ) ? true : dcNumberArray.includes(data.dcNumber?.toLowerCase());
      let startingProductionLotFlag=(startingProductionLotArray.includes('all')) ? true : startingProductionLotArray.includes(String( data.startingProductionLot));
      let effectiveBeginDateFlag=(effectiveBeginDateArray.includes('all')) ? true : effectiveBeginDateArray.includes(data.dcEffBegDate.toString().slice(0,10));
      


      return modelCodeFlag
      //data.model.toLowerCase().indexOf(searchTerms.model.toLowerCase()) !== -1
        && modelTypeFlag
      //&& data.modelType.toLowerCase().indexOf(searchTerms.modelType.toLowerCase()) !== -1
        //&& data.letSystemName.toLowerCase().indexOf(searchTerms.letSystemName.toLowerCase()) !== -1
        && systemNameFlag
        //&& (searchTerms.letSystemName.toLowerCase() === 'all' || searchTerms.letSystemName.toLowerCase()==='') ? true : data.letSystemName.toLowerCase()===searchTerms.letSystemName.toLowerCase()
        && dcPartNumberFlag
        //&& data.dcPartNumber.toLowerCase().indexOf(searchTerms.dcPartNumber.toLowerCase()) !== -1
        && dcNumberFlag
        //&& (data.dcNumber != null && data.dcNumber.toLowerCase().indexOf(searchTerms.dcNumber.toLowerCase()) !== -1)
        && dcChangeClassFlag
        //&& data.dcClass.toLowerCase().indexOf(searchTerms.dcClass.toLowerCase()) !== -1
        && effectiveBeginDateFlag
        //&& data.dcEffBegDate.toString().slice(0,10).indexOf(searchTerms.dcEffBegDate.toLowerCase()) !== -1
        && String(data.reflash).toLowerCase().indexOf(searchTerms.reflash.toLowerCase()) !== -1
        && String(data.scrapParts).toLowerCase().indexOf(searchTerms.scrapParts.toLowerCase()) !== -1
        && String(data.interchangeable).toLowerCase().indexOf(searchTerms.interchangeable.toLowerCase()) !== -1
        && startingProductionLotFlag;
        //&& String( data.startingProductionLot).toLowerCase().indexOf(searchTerms.startingProductionLot.toLowerCase()) !== -1;
    }
    return filterFunction;
  }

}
