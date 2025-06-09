import { SelectionModel } from '@angular/cdk/collections';
import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Title } from '@angular/platform-browser';

import { Message } from 'src/app/models/message';
import { AlertService } from 'src/app/alert/alert.service';
import { ConfirmationDialogService } from 'src/app/confirmation-dialog/confirmation-dialog.service';

import { ModelPartApproval } from 'src/app/models/model-part-approval';
import { FrameSpecService } from 'src/app/services/frame-spec.service';
import { RuleLotApprovalService } from 'src/app/services/rule-lot-approval.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-rule-lot-approval',
  templateUrl: './rule-lot-approval.component.html',
  styleUrls: ['./rule-lot-approval.component.css']
})
export class RuleLotApprovalComponent implements OnInit {

  public loading = false;
  modelPartApprovalList: ModelPartApproval[];
  modelList: string[];
  modelTypeList: string[];
  letSystemNameList: string[];
  dcPartNumber: string;
  dcNumber: string;
  dcClassList: string[];
  dcNumberList: string[];
  dcPartNumberList: string[];
  dcEffBegDateList: string[];
  currentStartingProductionLotList: string[];
  currentStartingProductionDateList: string[];
  newStartingProductionLotList: string[];
  newStartingProductionDateList: string[];
  requestAssociateNameList: string[];
  requestTimestampList: string[];
  remFilter:string;
  selectedRow: number;
  highlightedRow: Number;
  selectRow: Function;
  highlightRow: Function;
  dataSource: MatTableDataSource<ModelPartApproval>;
  displayedColumns = ['select', 'dcNumber', 'model', 'modelType', 'dcPartNumber', 'letSystemName', 'dcClass', 'currentReflash', 'newReflash', 'currentScrapParts', 'newScrapParts', 'currentInterchangable', 'newInterchangable', 'dcEffBegDate', 'currentStartingProductionLot', 'newStartingProductionLot', 'requestAssociateName', 'requestTimestamp'];
  displayedGroupColumns = ['header-row-group-select','header-row-group-dcNumber','header-row-group-model','header-row-group-type','header-row-group-dcPartNo','header-row-group-systemName','header-row-group-dcClass','header-row-group-reflash','header-row-group-scrap','header-row-group-strglr','header-row-group-eff-begin-date','header-row-group-start-prod','header-row-group-requester','header-row-group-requestTime'];
  selection = new SelectionModel<ModelPartApproval>(true, []);
  @ViewChild(MatSort) sort: MatSort;

  modelFilter = new FormControl(['all']);
  modelTypeFilter = new FormControl(['all']);
  letSystemNameFilter = new FormControl(['all']);
  dcPartNumberFilter = new FormControl(['all']);
  dcNumberFilter = new FormControl(['all']);
  dcClassFilter = new FormControl(['all']);
  dcEffBegDateFilter = new FormControl(['all']);
  currentReflashFilter = new FormControl('');
  newReflashFilter = new FormControl('');
  currentScrapPartsFilter = new FormControl('');
  newScrapPartsFilter = new FormControl('');
  currentInterchangableFilter = new FormControl('');
  newInterchangableFilter = new FormControl('');
  currentStartingProductionLotFilter = new FormControl('');
  currentStartingProductionDateFilter = new FormControl('');
  newStartingProductionLotFilter = new FormControl('');
  newStartingProductionDateFilter = new FormControl('');
  requestAssociateNameFilter = new FormControl(['all']);
  requestTimestampFilter = new FormControl('');

  filterValues = {
    model: ['all'],
    modelType: ['all'],
    letSystemName: ['all'],
    dcPartNumber: ['all'],
    dcNumber: ['all'],
    dcClass: ['all'],
    dcEffBegDate: ['all'],
    currentReflash: '',
    newReflash: '',
    currentScrapParts: '',
    newScrapParts: '',
    currentInterchangable: '',
    newInterchangable: '',
    currentStartingProductionLot: '',
    currentStartingProductionDate: '',
    newStartingProductionLot: '',
    newStartingProductionDate: '',
    requestAssociateName: ['all'],
    requestTimestamp: '',
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
  tempRequestor: any = [];

  constructor(
    public dialog: MatDialog,  
    public cdk: OverlayContainer, 
    protected alertService: AlertService,
    private confirmationDialogService: ConfirmationDialogService,
    private titleService: Title,
    public ruleLotApprovalService: RuleLotApprovalService,
    public frameSpecService: FrameSpecService
  ) { 

    this.titleService.setTitle("SUMS-VIN BOM Rule Assignment");

    this.titleService.setTitle("SUMS-VIN BOM Rule Lot Assignment");

    this.selectRow = function(index, row){
      this.selectedRow = index;
    }

    this.highlightRow = function(index){
      this.highlightedRow = index;
    }

  }

  selectionRequestorChange(value) {
    console.log('dc requestor change value --', value);
    this.requestAssociateNameFilter.setValue(value);
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
     
        this.filterValues.dcClass = dcClass;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )


    // this.dcClassFilter.valueChanges
    // .subscribe(
    //   dcClass => {
    //     this.selection.clear();
    //     this.filterValues.dcClass = dcClass;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

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
    this.currentReflashFilter.valueChanges
    .subscribe(
      currentReflash => {
        this.selection.clear();
        this.filterValues.currentReflash = currentReflash;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.newReflashFilter.valueChanges
    .subscribe(
      newReflash => {
        this.selection.clear();
        this.filterValues.newReflash = newReflash;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.currentScrapPartsFilter.valueChanges
    .subscribe(
      currentScrapParts => {
        this.selection.clear();
        this.filterValues.currentScrapParts = currentScrapParts;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.newScrapPartsFilter.valueChanges
    .subscribe(
      newScrapParts => {
        this.selection.clear();
        this.filterValues.newScrapParts = newScrapParts;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.currentInterchangableFilter.valueChanges
    .subscribe(
      currentInterchangable => {
        this.selection.clear();
        this.filterValues.currentInterchangable = currentInterchangable;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.newInterchangableFilter.valueChanges
    .subscribe(
      newInterchangable => {
        this.selection.clear();
        this.filterValues.newInterchangable = newInterchangable;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    //this.currentStartingProductionLotFilter.setValue('All');
    this.currentStartingProductionLotFilter.valueChanges
    .subscribe(
      currentStartingProductionLot => {
        this.selection.clear();
        this.filterValues.currentStartingProductionLot = currentStartingProductionLot;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
   // this.currentStartingProductionDateFilter.setValue('All');
    this.currentStartingProductionDateFilter.valueChanges
    .subscribe(
      currentStartingProductionDate => {
        this.selection.clear();
        this.filterValues.currentStartingProductionDate = currentStartingProductionDate;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.newStartingProductionLotFilter.valueChanges
    .subscribe(
      newStartingProductionLot => {
        this.selection.clear();
        this.filterValues.newStartingProductionLot = newStartingProductionLot;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.newStartingProductionDateFilter.valueChanges
    .subscribe(
      newStartingProductionDate => {
        this.selection.clear();
        this.filterValues.newStartingProductionDate = newStartingProductionDate;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )

    this.requestAssociateNameFilter.valueChanges
    .subscribe(
      requestAssociateName => {
        this.selection.clear();
        if(requestAssociateName.includes('all')){
          console.log('inside if block of per select ');
          if(requestAssociateName[0]=='all' && requestAssociateName.length>1 && this.tempRequestor?.length==0){
            console.log(' inside---  ',requestAssociateName[0]);
            requestAssociateName.splice(0,1);
            this.tempRequestor=requestAssociateName;
          }
          else if(this.tempRequestor.length>0 && requestAssociateName.includes('all')){
            requestAssociateName.splice(1,requestAssociateName.length);
            this.tempRequestor=[];
          }
        }        
        this.filterValues.requestAssociateName = requestAssociateName;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }      
    )

    // this.requestAssociateNameFilter.valueChanges
    // .subscribe(
    //   requestAssociateName => {
    //     this.selection.clear();
    //     this.filterValues.requestAssociateName = requestAssociateName;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )
    this.requestTimestampFilter.valueChanges
    .subscribe(
      requestTimestamp => {
        this.selection.clear();
        this.filterValues.requestTimestamp = requestTimestamp;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )

  }

  search(): void {
    this.loading = true;
    
    this.ruleLotApprovalService.getPendingModelPartApprovals()
    .subscribe((modelPartApprovalList: ModelPartApproval[]) => {
      var replaedNull = JSON.stringify(modelPartApprovalList).replace(/null/g, '""');
      var newArray = JSON.parse(replaedNull);
      this.modelPartApprovalList = newArray;
      this.modelList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.model)))).sort();
      this.modelTypeList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.modelType)))).sort();
      this.letSystemNameList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.modelPart.letSystemName)))).sort();
      this.dcNumberList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.modelPart.dcNumber)))).sort();
      this.dcPartNumberList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.modelPart.dcPartNumber)))).sort();
      this.dcClassList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.modelPart.dcClass)))).sort();
      this.dcEffBegDateList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.modelPart.dcEffBegDate)))).sort();
      this.requestAssociateNameList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.requestAssociateName)))).sort();
      this.newStartingProductionLotList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.newStartingProductionLot)))).sort();
      this.newStartingProductionDateList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.newStartingProductionDate)))).sort();
      this.currentStartingProductionLotList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.currentStartingProductionLot)))).sort();
      this.currentStartingProductionDateList = (Array.from(new Set(this.modelPartApprovalList.map(ele=>ele.currentStartingProductionDate)))).sort();

      //NALC-1093
      let sortedList = this.modelPartApprovalList.sort((a,b) => (a.modelPart.dcPartNumber < b.modelPart.dcPartNumber) ? -1 : 1).
      sort((a,b) => (a.modelType < b.modelType) ? -1 : 1).
      sort((a, b) => (a.model < b.model) ? -1 : 1);
      console.log('sortedList  ---- ',JSON.stringify(sortedList));



      this.dataSource = new MatTableDataSource(sortedList);
      this.dataSource.sort = this.sort;
      this.dataSource.filterPredicate = this.createFilter();
    console.log('lot approval datasource -- ', this.dataSource);
      if(this.remFilter){
        console.log("setting filter to - "+this.remFilter);
        this.filterValues=JSON.parse(this.remFilter);
        this.dataSource.filter = this.remFilter;
        } 
        this.alertService.clear();
        this.loading = false;
    },(error: any) => {
      this.loading = false;
    });
  }

  
  async approve() {
    if(this.selection.selected.length <= 0) {
      alert("Please select at least one row from above grid");
      return;
    }
    //NALC--1083
    if(this.selection.selected.length>250){
      alert("Resultset exceeds max size : 250, please select additional criteria to filter data.");
      return;
    }
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);
    this.loading = true;
    console.log('lot approval -- ', this.selection.selected);
    const response =  this.ruleLotApprovalService.approveMultiPleModelPartChange(this.selection.selected).subscribe(data => {
      console.log('data ---- ',data);
      this.alertService.clear();
      this.alertService.success('Approve success ', this.messageOptionsAutoClose);
      this.selection.clear();
      this.loading = false;
      this.search(); 

      },(error: any) => {
        this.alertService.clear();
        alert("Error Approving");
        this.loading = false;
      });

   /* const responses = await Promise.all(
      this.selection.selected.map(s => this.ruleLotApprovalService.approveModelPartChange1(s.modelPartApprovalId))
     
    );
    console.log(" approval response -"+responses);
    this.alertService.clear();
    this.alertService.success('Approve success ', this.messageOptionsAutoClose);
    this.search();
    this.selection.clear();
    this.loading = false;*/
  }

  

  async deny() {

    if(this.selection.selected.length <= 0) {
      alert("Please select at least one row from above grid");
      return;
    }
    //NALC-1083
    if(this.selection.selected.length>250){
      alert("Resultset exceeds max size : 250, please select additional criteria to filter data.");
      return;
    }
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);
    
    this.loading = true;
    const response =  this.ruleLotApprovalService.denyMultipleModelPartChange(this.selection.selected).subscribe(data => {
      console.log(data);
      this.alertService.clear();
      this.alertService.success('Deny success ', this.messageOptionsAutoClose);
     
      this.selection.clear();
      this.loading = false;
      this.search(); 
      },(error: any) => {
        this.alertService.clear();
        alert("Error Denying");
        this.loading = false;
      });
      
    /*const responses = await Promise.all(
      
      this.selection.selected.map(s => this.ruleLotApprovalService.denyModelPartChange1(s.modelPartApprovalId))
    );
    this.alertService.clear();
    this.alertService.success('Deny success');
    this.search();
    this.selection.clear();
    this.loading = false;*/

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



  createFilter(): (data: any, filter: string) => boolean {
    let filterFunction = function(data, filter): boolean {
      let searchTerms = JSON.parse(filter);

      let systemNameArray = searchTerms.letSystemName as Array<String>;
      let modelTypeArray = searchTerms.modelType as Array<String>;
      let dcPartNumberArray = searchTerms.dcPartNumber as Array<String>;
      let modelCodeArray = searchTerms.model as Array<String>;      
      let dcChangeClassArray = searchTerms.dcClass as Array<String>;
      let dcNumberArray = searchTerms.dcNumber as Array<String>;
      let requestAssociateNameArray = searchTerms.requestAssociateName as Array<String>;      
      let effectiveBeginDateArray = searchTerms.dcEffBegDate as Array<String>;
      

      console.log(searchTerms.requestAssociateName);
      console.log(data.requestAssociateName.toLowerCase());
      
      let currentStartingProductionLotMatch: boolean=true;
      if(String(searchTerms.currentStartingProductionLot).length === 0){
        currentStartingProductionLotMatch = String(data.currentStartingProductionLot) === searchTerms.currentStartingProductionLot;
        console.log("currentStartingProductionLot - "+String(data.currentStartingProductionLot) +"-"+String(data.currentStartingProductionLot).length +"-"+currentStartingProductionLotMatch);
      }else if(String(searchTerms.currentStartingProductionLot).toLowerCase() === 'all'){
        currentStartingProductionLotMatch = true;
       } else{
        currentStartingProductionLotMatch = String(data.currentStartingProductionLot).toLowerCase().indexOf(searchTerms.currentStartingProductionLot.toLowerCase()) !== -1;
        console.log("currentStartingProductionLot - "+String(data.currentStartingProductionLot) +"-"+String(data.currentStartingProductionLot).length +"-"+currentStartingProductionLotMatch);
      }

      let currentStartingProductionDateMatch: boolean=true;
      if(String(searchTerms.currentStartingProductionDate).length === 0){
        currentStartingProductionDateMatch = String(data.currentStartingProductionDate) === searchTerms.currentStartingProductionDate;
        console.log("currentStartingProductionDate - "+String(data.currentStartingProductionDate) +"-"+String(data.currentStartingProductionDate).length +"-"+currentStartingProductionDateMatch);
      }else if(String(searchTerms.currentStartingProductionDate).toLowerCase() === 'all'){
        currentStartingProductionDateMatch = true;
       } else{
        currentStartingProductionDateMatch = String(data.currentStartingProductionDate).toLowerCase().indexOf(searchTerms.currentStartingProductionDate.toLowerCase()) !== -1;
        console.log("currentStartingProductionDate - "+String(data.currentStartingProductionDate) +"-"+String(data.currentStartingProductionDate).length +"-"+currentStartingProductionDateMatch);
      }
console.log('systemNameArray--', systemNameArray);
console.log('data.modelPart.letSystemName---', data.modelPart.letSystemName);
      let modelTypeFlag=(modelTypeArray.includes('all') ) ? true : modelTypeArray.includes(data.modelType.toLowerCase());
      let systemNameFlag=(systemNameArray.includes('all') ) ? true : systemNameArray.includes(data.modelPart.letSystemName.toLowerCase());
      let dcPartNumberFlag=(dcPartNumberArray.includes('all') ) ? true : dcPartNumberArray.includes(data.modelPart.dcPartNumber.toLowerCase());
      let modelCodeFlag=(modelCodeArray.includes('all') ) ? true : modelCodeArray.includes(data.model.toLowerCase());     
      let dcChangeClassFlag=(dcChangeClassArray.includes('all') ) ? true : dcChangeClassArray.includes(data.modelPart.dcClass?.toLowerCase());
      let dcNumberFlag=(dcNumberArray.includes('all') ) ? true : dcNumberArray.includes(data.modelPart.dcNumber?.trim().toLowerCase());
     // let stragglerInterchangeableCodeFlag=(stragglerInterchangeableCodeArray.includes('all') || stragglerInterchangeableCodeArray.includes('')) ? true : stragglerInterchangeableCodeArray.includes(data.stragglerInterchangeableCode?.trim());
      let requestAssociateNameFlag=(requestAssociateNameArray.includes('all') ) ? true : requestAssociateNameArray.includes(data.requestAssociateName?.trim().toLowerCase());
      let effectiveBeginDateFlag=(effectiveBeginDateArray.includes('all') ) ? true : effectiveBeginDateArray.includes(data.modelPart.dcEffBegDate.toString().slice(0,10));//NALC-1172
     
      


      return modelCodeFlag
      //data.model.toLowerCase().indexOf(searchTerms.model.toLowerCase()) !== -1
        && modelTypeFlag
        //&& data.modelType.toLowerCase().indexOf(searchTerms.modelType.toLowerCase()) !== -1
        //&& data.modelPart.letSystemName.toLowerCase().indexOf(searchTerms.letSystemName.toLowerCase()) !== -1
        && systemNameFlag
        //&& (searchTerms.letSystemName.toLowerCase() === 'all' || searchTerms.letSystemName.toLowerCase()==='') ? true : data.letSystemName.toLowerCase()===searchTerms.letSystemName.toLowerCase()
        && dcPartNumberFlag
        //&& data.modelPart.dcPartNumber.toLowerCase().indexOf(searchTerms.dcPartNumber.toLowerCase()) !== -1
        && dcNumberFlag
        //&& data.modelPart.dcNumber.toLowerCase().indexOf(searchTerms.dcNumber.toLowerCase()) !== -1
        && dcChangeClassFlag
        //&& data.modelPart.dcClass.toLowerCase().indexOf(searchTerms.dcClass.toLowerCase()) !== -1
        && effectiveBeginDateFlag
        //&& data.modelPart.dcEffBegDate.toLowerCase().indexOf(searchTerms.dcEffBegDate.toLowerCase()) !== -1
        && String(data.currentReflash).toLowerCase().indexOf(searchTerms.currentReflash.toLowerCase()) !== -1
        && String(data.newReflash).toLowerCase().indexOf(searchTerms.newReflash.toLowerCase()) !== -1
        && String(data.currentScrapParts).toLowerCase().indexOf(searchTerms.currentScrapParts.toLowerCase()) !== -1
        && String(data.newScrapParts).toLowerCase().indexOf(searchTerms.newScrapParts.toLowerCase()) !== -1
        && String(data.currentInterchangable).toLowerCase().indexOf(searchTerms.currentInterchangable.toLowerCase()) !== -1
        && String(data.newInterchangable).toLowerCase().indexOf(searchTerms.newInterchangable.toLowerCase()) !== -1
        && String(data.newStartingProductionLot).toLowerCase().indexOf(searchTerms.newStartingProductionLot.toLowerCase()) !== -1
        && String(data.newStartingProductionDate).toLowerCase().indexOf(searchTerms.newStartingProductionDate.toLowerCase()) !== -1
        && String(data.currentStartingProductionLot).toLowerCase().indexOf(searchTerms.currentStartingProductionLot.toLowerCase()) !== -1
        && String(data.currentStartingProductionDate).toLowerCase().indexOf(searchTerms.currentStartingProductionDate.toLowerCase()) !== -1
        && requestAssociateNameFlag
        //&& data.requestAssociateName.toLowerCase().indexOf(searchTerms.requestAssociateName.toLowerCase()) !== -1; 
    }
    return filterFunction;
  }



/*NALC-1079 */

  getPreviousFlashValue(value) {
    if (value == null || value == undefined) {
      return '-';
    }
    else if (value) {
      return 'Yes';
    } else {
      return 'No';
    }
  }

  getPreviousScrapPartsValue(value) {
    if (value == null || value == undefined) {
      return '-';
    }
    else if (value) {
      return 'Yes';
    } else {
      return 'No';
    }
  }

  getPreviousInterchangableValue(value) {
    if (value == null || value == undefined) {
      return '-';
    }
    else if (value) {
      return 'Yes';
    } else {
      return 'No';
    }
  }

/*NALC-1079 */

}
