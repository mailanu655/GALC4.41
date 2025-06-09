import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { Title } from '@angular/platform-browser';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { Message } from 'src/app/models/message';
import { MatCheckboxChange } from '@angular/material/checkbox';

import { AlertService } from 'src/app/alert/alert.service';
import { ConfirmationDialogService } from 'src/app/confirmation-dialog/confirmation-dialog.service';
import { ModelYearCode } from 'src/app/models/model-year-code';
import { FrameSpecService } from 'src/app/services/frame-spec.service';
import { LotAssignmentService } from 'src/app/services/lot-assignment.service';
import { UtilityService } from 'src/app/services/utility-service';
import {SecurityService} from 'src/app/services/security.service';
import { FormControl } from '@angular/forms';
import { ModelPartLotDto } from 'src/app/models/model-part-lot-dto';
import { ModelPartLotFilterDto } from 'src/app/models/model-part-lot-filter-dto';
import { RuleLotMaintenanceService } from 'src/app/services/rule-lot-maintenance.service';
import { ChangeRulesDialogComponent } from './change-rules-dialog/change-rules-dialog.component';
import { SelectProdLotDialogComponent } from '../select-prod-lot-dialog/select-prod-lot-dialog.component';
import { ModelPartApproval } from 'src/app/models/model-part-approval';
import { DatePipe } from '@angular/common';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-rule-lot-maintenance',
  templateUrl: './rule-lot-maintenance.component.html',
  styleUrls: ['./rule-lot-maintenance.component.css']
})
export class RuleLotMaintenanceComponent implements OnInit {

  public loading = false;
  public modelCodeList: string[];
  public modelTypeList: string[];
  public dcNumberList: string[];
  public dcPartNumberList: string[];
  public letSystemNameList: string[];
  public dcClassList: string[];
  public activeList: string[];
  public dcEffBegDateList: string[];
  public startingProductionLotList: string[];

  public dcNumberRuleLotFilterList: string[];
  public dcPartNumberRuleLotFilterList: string[];
  public letSystemNameRuleLotFilterList: string[];

  public lotFilterList: ModelPartLotFilterDto[];

  public lotList: ModelPartLotDto[];
  public modelYearCodeList: ModelYearCode[];
  public designChangeNumber: string="";
  public dcPartNumber: string="";
  public systemName: string="";
  public modelCode: string;
  public modelType: string;
  public dcNumber: string;
  public dcClass: string;
  public active: string;
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
  selectRowData: ModelPartLotDto[];
  selectedFinalRowData: ModelPartLotDto[] = [];
  selectedModelPartApprovalData: ModelPartApproval[] = [];
  remFilter:string;

  dataSource: MatTableDataSource<ModelPartLotDto>;
  displayedColumns = ['select', 'dcNumber', 'model', 'modelType', 'dcPartNumber', 'letSystemName', 'dcClass','active', 'reflash', 'scrapParts', 'interchangeable', 'dcEffBegDate', 'startingProductionLot'];
  selection = new SelectionModel<ModelPartLotDto>(true, []);
  @ViewChild(MatSort) sort: MatSort;

  modelFilter = new FormControl(['all']);
  modelTypeFilter = new FormControl(['all']);
  letSystemNameFilter = new FormControl(['all']);
  dcPartNumberFilter = new FormControl(['all']);
  dcNumberFilter = new FormControl(['all']);
  dcClassFilter = new FormControl(["all"]);
  activeFilter = new FormControl(['all']);
  dcEffBegDateFilter = new FormControl(['all']);
  reflashFilter = new FormControl('');
  scrapPartsFilter = new FormControl('');
  interchangeableFilter = new FormControl('');
  startingProductionLotFilter = new FormControl(['all']);

  letSystemNameRuleLotFilter = new FormControl('');
  dcPartNumberRuleLotFilter = new FormControl('');
  dcNumberRuleLotFilter = new FormControl('');
  tempDcClass: any = [];
  tempModel: any = [];
  tempModelType: any = [];
  tempSystemName: any = [];
  tempDcNumber: any = [];
  tempDcPartNumber: any = [];
  tempActive: any = [];
  tempEffBeginDate: any = [];
  tempStartProdLot: any = [];
  allSelect: boolean = true;

  filterValues = {
    model: ['all'],
    modelType: ['all'],
    letSystemName: ['all'],
    dcPartNumber: ['all'],
    dcNumber: ['all'],
    dcClass: ['all'],
    active:['all'],
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

  constructor(
    public dialog: MatDialog,  
    public cdk: OverlayContainer,
    private datePipe: DatePipe,
    protected alertService: AlertService,
    private titleService: Title,
    private confirmationDialogService: ConfirmationDialogService,
    public ruleLotMaintenanceService: RuleLotMaintenanceService,
    public lotAssignmentService: LotAssignmentService,
    public frameSpecService: FrameSpecService, public securityService:SecurityService
  ) { 

    this.titleService.setTitle("SUMS-VIN BOM Rule Lot Assignment");

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

  selectionActiveChange(value) {
    console.log('active value --', value);
    this.activeFilter.setValue(value);
  }

  ngOnInit(): void {
    this.searchAll();

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
    //     console.log(' inside rule lot maintenance systemName filter -- ',JSON.stringify(this.filterValues));
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
    //    }
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

    // this.dcClassFilter.valueChanges
    // .subscribe(
    //   dcClass => {
    //     this.selection.clear();
    //     this.filterValues.dcClass = dcClass;
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

    //this.startingProductionLotFilter.setValue('All');
    // this.startingProductionLotFilter.valueChanges
    // .subscribe(
    //   startingProductionLot => {
    //     this.selection.clear();
    //     this.filterValues.startingProductionLot = startingProductionLot;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

  }

  searchAll() {
    this.selection.clear();
   
    this.loading = true;

    this.ruleLotMaintenanceService.getLotAssignmentFilters()
    .subscribe((lotFilterList: ModelPartLotFilterDto[]) => {
      var replaedNull = JSON.stringify(lotFilterList).replace(/null/g, '""');

      var newArray = JSON.parse(replaedNull);
      this.lotFilterList = newArray;
     
      this.letSystemNameRuleLotFilterList = (Array.from(new Set(this.lotFilterList.map(ele=>ele.letSystemName)))).sort();
      this.dcNumberRuleLotFilterList = (Array.from(new Set(this.lotFilterList.map(ele=>ele.dcNumber)))).sort();
      this.dcPartNumberRuleLotFilterList = (Array.from(new Set(this.lotFilterList.map(ele=>ele.dcPartNumber)))).sort();
     
        
        this.loading = false;
      },(error: any) => {
        this.loading = false;
      });
  }

  search(): void {
    this.selection.clear();
    console.log("inside search when filtered");
  
    console.log('inside rule lot maintenance search', this.modelTypeFilter);
    
    const newfilterValues = {
      model: ['all'],
      modelType: ['all'],
      letSystemName: ['all'],
      dcPartNumber: ['all'],
      dcNumber: ['all'],
      dcClass: ['all'],
      active:['all'],
      dcEffBegDate: ['all'],
      reflash: '',
      scrapParts: '',
      interchangeable: '',
      startingProductionLot: ['all']
    };

//this.dcPartNumberFilter = new FormControl(['all']);
if(undefined!=this.dataSource && this.dataSource ){
this.dataSource.filter=JSON.stringify(newfilterValues);
this.dcPartNumberFilter.setValue(['all']);
this.modelFilter = new FormControl(['all']);
  this.modelTypeFilter = new FormControl(['all']);
  this.letSystemNameFilter = new FormControl(['all']);
  this.startingProductionLotFilter = new FormControl(['all']);
  this.dcNumberFilter = new FormControl(['all']);
  this.dcClassFilter = new FormControl(["all"]);
  this.activeFilter = new FormControl(['all']);
  this.dcEffBegDateFilter = new FormControl(['all']);
  }

    if(this.reflashFilter.value){
      this.reflashFilter.setValue('');
    }
    if(this.scrapPartsFilter.value){
      this.scrapPartsFilter.setValue('');
    }
    if(this.interchangeableFilter.value){
      this.interchangeableFilter.setValue('');
    }

    if((this.designChangeNumber === undefined || this.designChangeNumber.trim().length == 0)&&(this.dcPartNumber === undefined || this.dcPartNumber.trim().length == 0)&&(this.systemName === undefined || this.systemName.trim().length == 0)) {
      
      this.confirmationDialogService.confirm('Please Confirm...','Retrieving data  with no filters selected can take longer time...  </b> <br> Do you want to continue? ', 'Yes', 'No', 'lg')
      .then((confirmed) => {
        if(confirmed) {
          this.loadData();
        }else{}
      })
    }else{
      this.loadData();
    }
  
      
  }

  loadData(){
    this.loading = true;
       
    this.ruleLotMaintenanceService.getFilteredLotAssignments(this.designChangeNumber.toUpperCase(),this.systemName.toUpperCase(),this.dcPartNumber.toUpperCase())
      .subscribe((lotList: ModelPartLotDto[]) => {
        var replaedNull = JSON.stringify(lotList).replace(/null/g, '""');
//        replaedNull = replaedNull.replace(/true/g, '"Yes"');
//        replaedNull = replaedNull.replace(/false/g, '"No"');
        var newArray = JSON.parse(replaedNull);
        this.lotList = newArray;
        this.modelCodeList = (Array.from(new Set(this.lotList.map(ele=>ele.model)))).sort();  
        this.modelTypeList = (Array.from(new Set(this.lotList.map(ele=>ele.modelType)))).sort();
        this.letSystemNameList = (Array.from(new Set(this.lotList.map(ele=>ele.letSystemName)))).sort();
        this.dcNumberList = (Array.from(new Set(this.lotList.map(ele=>ele.dcNumber)))).sort();
        this.dcPartNumberList = (Array.from(new Set(this.lotList.map(ele=>ele.dcPartNumber)))).sort();
        this.dcClassList = (Array.from(new Set(this.lotList.map(ele=>ele.dcClass)))).sort();  
        this.activeList = (Array.from(new Set(this.lotList.map(ele=>ele.active)))).sort();  
        this.dcEffBegDateList = (Array.from(new Set(this.lotList.map(ele=>ele.dcEffBegDate)))).sort();  
        this.startingProductionLotList = (Array.from(new Set(this.lotList.map(ele=>ele.startingProductionLot)))).sort();  

//NALC-1093
        let sortedList = this.lotList.sort((a,b) => (a.dcPartNumber < b.dcPartNumber) ? -1 : 1).
        sort((a,b) => (a.modelType < b.modelType) ? -1 : 1).
        sort((a, b) => (a.model < b.model) ? -1 : 1)                
        ;

        this.dataSource = new MatTableDataSource(sortedList);
        this.dataSource.sort = this.sort;
        console.log('search result -- ', this.dataSource);
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

  changeRulesOpenDialog() {
    this.alertService.clear();
    console.log(this.selection.selected);
    if(this.selection.selected.length <= 0) {
      alert("Please select part data from above grid");
      return;
    }
    //NALC-1083
    // if(this.selection.selected.length>50){
    //   alert("Resultset exceeds max size : 50, please select additional criteria to filter data.");
    //   return;
    // }
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);
    const dialogRef = this.dialog.open(ChangeRulesDialogComponent, {
      width: "60%",
      height: '50%',
      data: { isChangeSuccess: false, isSuccess: false, selectRowData: this.selection.selected, doSave: true },
      id: "parent",
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log("The dialog was closed");
      console.log(result);
      if(result.isChangeRuleSuccess) {
        this.alertService.clear();
        this.search();
        this.alertService.success('Change Rule(s) has sent been sent for approval', this.messageOptionsAutoClose);
      }
      this.dialog.closeAll();
    });
  }

  changeStartProdLot() {
    this.selectedFinalRowData= [];
    if(this.selection.selected.length <= 0) {
      alert("Please select Part data from above grid");
      return;
    } else {
      let startProdLotNotAssignedCount = 0;
      this.selection.selected.forEach(s => {
        if(s.startingProductionLot == '-') {
          startProdLotNotAssignedCount++;
        } else {
          console.log("adding to dataList-"+s.modelPartId)
          this.selectedFinalRowData.push(s);
        }
      });
      console.log(startProdLotNotAssignedCount);
      if(startProdLotNotAssignedCount == this.selection.selected.length) {
        console.log(this.selectedFinalRowData);
        console.log(this.selection.selected);
        this.confirmationDialogService.alert('Please note..', 'Start Production Lot has not been assigned for any of the selected Rules. ', 'OK', 'lg');
      } else if(startProdLotNotAssignedCount == 0) {
        this.changeStartProdLotDialog(startProdLotNotAssignedCount);
      } else if(startProdLotNotAssignedCount > 0) {
        console.log(this.selectedFinalRowData);
        console.log(this.selection.selected);
        this.confirmationDialogService.confirm('Please confirm..', 'Start Production Lot has not been assigned for <b>' + startProdLotNotAssignedCount + '</b> Rules. \n Do you want to continue? ', 'Yes', 'No', 'lg')
        .then((confirmed) => {
          if(confirmed) {
            console.log("User selected: Yes");
            console.log(this.selectedModelPartApprovalData);
            this.changeStartProdLotDialog(startProdLotNotAssignedCount);
          } else {
            console.log("User selected: No");
          }
        }).catch(() => {
          console.log("User selected: No");
        });
        
      }
    }
    //this.convertToModelPartApproval();
  }

  changeStartProdLotDialog(startProdLotNotAssignedCount: number) {
    if(this.selection.selected.length <= 0) {
      alert("Please select data from above grid");
      return;
    } 
    //NALC-1083
    // if(this.selection.selected.length > 50){
    //   alert("Resultset exceeds max size : 50, please select additional criteria to filter data.");
    //   return;
    // }
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);
    console.log("SelectedFinalRowData-"+ JSON.stringify(this.selectedFinalRowData));
    const dialogRef = this.dialog.open(SelectProdLotDialogComponent, {
      width: "75%",
      height: '75%',
      //data: { isProdlotSelected: false, selectedData: this.selectedModelPartApprovalData, doSave: true },
      data: { isProdlotSelected: false, selectedParentData: this.selectedFinalRowData, doSave: true },
      id: "parent",
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log("The dialog was closed");
      if(result.isSuccess) {
        this.alertService.clear();
        this.search();
        this.alertService.success('Change Starting Production Lot has sent been sent for approval', this.messageOptionsAutoClose);
      }
      this.dialog.closeAll();
    });          
  }

  delete() {
    if(this.selection.selected.length <= 0) {
      alert("Please select Part data from above grid");
      return;
    } 
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);
    if(this.selection.selected.length > 1 && !this.isColumnFilterSet() && this.isAllSelected() ){
      console.log("no filter set");
      alert("Please filter data to delete assignment");
      return;
    }
    //NALC-1083
    if(this.selection.selected.length > 250){
      alert("Resultset exceeds max size : 250, please select additional criteria to filter data.");
      return;
    }
    this.confirmationDialogService.confirm('', 'Are you sure you want to delete '+this.selection.selected.length +' assignment? ', 'Yes', 'No', 'lg')
          .then((confirmed) => {
            if(confirmed) {
              this.loading=true;
              forkJoin(
                this.selection.selected.map(s => this.ruleLotMaintenanceService.deleteModelPartAssignment(s))
              ).subscribe(
                data => {
                  console.log('deleted...');
                  this.search();
                  this.alertService.clear();
                  this.alertService.success('Selected Rule-Lot maintenance data Deleted', this.messageOptionsAutoClose);
                  this.loading=false;
                },
                (error: any) => {
                  alert('Error while deleting');
                }
              );
            } else {
              
            }
          }).catch(() => {
            
          });
        
            
  }

  

  createFilter(): (data: any, filter: string) => boolean {
    let filterFunction = function(data, filter): boolean {
      console.log(String(data.reflash));
      let searchTerms = JSON.parse(filter);

      let systemNameArray = searchTerms.letSystemName as Array<String>;
      let modelTypeArray = searchTerms.modelType as Array<String>;
      let dcPartNumberArray = searchTerms.dcPartNumber as Array<String>;
      let modelCodeArray = searchTerms.model as Array<String>;      
      let dcChangeClassArray = searchTerms.dcClass as Array<String>;
      let dcNumberArray = searchTerms.dcNumber as Array<String>;      
	    let activeArray = searchTerms.active as Array<String>;
      let effectiveBeginDateArray = searchTerms.dcEffBegDate as Array<String>;
	    let startingProductionLotArray = searchTerms.startingProductionLot as Array<String>;

      let startingProductionLotMatch: boolean=true;
      if(startingProductionLotArray.length === 0){
        startingProductionLotMatch = startingProductionLotArray.includes(data.startingProductionLot);
        console.log("startingProductionLot - "+String(data.startingProductionLot) +"-"+String(data.startingProductionLot).length +"-"+startingProductionLotMatch);
      }else if(startingProductionLotArray.includes('All')){
        startingProductionLotMatch = true;
       } else{
        startingProductionLotMatch = startingProductionLotArray.includes(data.startingProductionLot.toLowerCase());
        console.log("startingProductionLot - "+String(data.startingProductionLot) +"-"+String(data.startingProductionLot).length +"-"+startingProductionLotMatch);
      }
      console.log(String(data.model)+"-"+String(data.modelType)+"-"+String(data.letSystemName)+"-"+String(data.active));
  
      let modelTypeFlag=(modelTypeArray.includes('all') ) ? true : modelTypeArray.includes(data.modelType.toLowerCase());
      let systemNameFlag=(systemNameArray.includes('all') ) ? true : systemNameArray.includes(data.letSystemName.toLowerCase());
      let dcPartNumberFlag=(dcPartNumberArray.includes('all') ) ? true : dcPartNumberArray.includes(data.dcPartNumber.toLowerCase());
      let modelCodeFlag=(modelCodeArray.includes('all') ) ? true : modelCodeArray.includes(data.model.toLowerCase());
      let activeFlag=(activeArray.includes('all') ) ? true : activeArray.includes(String(data.active).toLowerCase());
      let dcChangeClassFlag=(dcChangeClassArray.includes('all')) ? true : dcChangeClassArray.includes(data.dcClass.toLowerCase());
      let dcNumberFlag=(dcNumberArray.includes('all') ) ? true : dcNumberArray.includes(data.dcNumber.toLowerCase());
      let startingProductionLotFlag=(startingProductionLotArray.includes('all') ) ? true : startingProductionLotArray.includes(data.startingProductionLot.toLowerCase());
      let effectiveBeginDateFlag=(effectiveBeginDateArray.includes('all') ) ? true : effectiveBeginDateArray.includes(data.dcEffBegDate?.toString().slice(0,10));


      return modelCodeFlag
        //data.model.toLowerCase().indexOf(searchTerms.model.toLowerCase()) !== -1
        && modelTypeFlag  
        //&& data.modelType.toLowerCase().indexOf(searchTerms.modelType.toLowerCase()) !== -1
        && systemNameFlag
        //&& data.letSystemName.toLowerCase().indexOf(searchTerms.letSystemName.toLowerCase()) !== -1    
        //&& (searchTerms.letSystemName.toLowerCase() === 'all' || searchTerms.letSystemName.toLowerCase()==='') ? true : data.letSystemName.toLowerCase()===searchTerms.letSystemName.toLowerCase()
        //&& systemNameAll
        && dcPartNumberFlag
        //&& data.dcPartNumber.toLowerCase().indexOf(searchTerms.dcPartNumber.toLowerCase()) !== -1
        && dcNumberFlag
        //&& (data.dcNumber != null && data.dcNumber.toLowerCase().indexOf(searchTerms.dcNumber.toLowerCase()) !== -1)
        && dcChangeClassFlag
        //&& data.dcClass.toLowerCase().indexOf(searchTerms.dcClass.toLowerCase()) !== -1
        && activeFlag
        //&& String(data.active).toLowerCase().startsWith(searchTerms.active.toLowerCase()) 
        && effectiveBeginDateFlag
        //&& data.dcEffBegDate.toLowerCase().indexOf(searchTerms.dcEffBegDate.toLowerCase()) !== -1
        && String(data.reflash).toLowerCase().indexOf(searchTerms.reflash.toLowerCase()) !== -1
        && String(data.scrapParts).toLowerCase().indexOf(searchTerms.scrapParts.toLowerCase()) !== -1
        && String(data.interchangeable).toLowerCase().indexOf(searchTerms.interchangeable.toLowerCase()) !== -1
        && startingProductionLotFlag;
        //&& data.startingProductionLot.toLowerCase().indexOf(searchTerms.startingProductionLot.toLowerCase()) !== -1;
    }
    return filterFunction;
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.filteredData.length;
    console.log('all selected -- ',numSelected === numRows);
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

  isUserInRole(roles: string): boolean {
    return !this.securityService.isUserInRole(roles);
   }

  isColumnFilterSet(): boolean {

    if(this.filterValues.model.length>0 || 
      this.filterValues.modelType.length>0 ||
      this.filterValues.letSystemName.length>0 ||
      this.filterValues.dcPartNumber.length>0 ||
      this.filterValues.dcNumber.length>0 ||
      this.filterValues.dcClass.length>0 ||
      this.filterValues.active.length>0 ||
      this.filterValues.dcEffBegDate.length>0 ||
      this.filterValues.reflash.length>0 ||
      this.filterValues.scrapParts.length>0 ||
      this.filterValues.interchangeable.length>0 ||
      this.filterValues.startingProductionLot.length>0 ) return true;
      
      else  return false;
  }

  
}