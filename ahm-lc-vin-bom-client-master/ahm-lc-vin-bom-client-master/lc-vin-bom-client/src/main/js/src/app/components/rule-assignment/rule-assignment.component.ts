import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { Title } from '@angular/platform-browser';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

import { AlertService } from 'src/app/alert/alert.service';
import { UtilityService } from 'src/app/services/utility-service';
import { RuleAssignmentService } from 'src/app/services/rule-assignment.service';
import { ConfigService } from 'src/app/services/config.service';
import { FrameSpecService } from 'src/app/services/frame-spec.service';
import { SelectionModel } from '@angular/cdk/collections';
//import { Lot } from 'src/app/models/Lot';
import { ModelYearCode } from 'src/app/models/model-year-code';
import { ConfirmationDialogService } from 'src/app/confirmation-dialog/confirmation-dialog.service';
import { DcmsDto } from 'src/app/models/dcms-dto';
import { Message } from 'src/app/models/message';
import { FormControl } from '@angular/forms';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { ModelPart } from 'src/app/models/model-part';
import { Router } from '@angular/router';
import { DesignChangeRule } from 'src/app/models/design-change-rule';


@Component({
  selector: 'app-rule-assignment',
  templateUrl: './rule-assignment.component.html',
  styleUrls: ['./rule-assignment.component.css']
})
export class RuleAssignmentComponent implements OnInit {

  public loading = false;
  public ymtoCodeList: string[];
  public modelCodeList: string[];
  
  public modelTypeList: string[];
  public dcPartNumberList: string[];
  public dcNumberList: string[];
  public effBeginDateList: string[];
  public effEndDateList: string[];
  public dcClassList: string[];
  public systemNameList: string[];
  public serviceIcCodeList: string[];
  public stragglerIcCodeList: string[];
  public plantCodeFilterList: string[];
  public designChangeRuleList:DesignChangeRule[];
  public designChangeRuleMap = new Map();
  public plantCode:string;
  public effBeginDate: string;
  public effEndDate: string;
  public designChangeClassList: string[];
  public serviceInterChangeCodeList: string[];
  public stragglerInterChangeCodeList: string[];
  public designChangeNumber: string;
  public dcPartNo: string;
  public dcmsModel: string;
  public systemName:string;
  public stragglerInterChangeCode: string;
  public serviceInterChangeCode: string;
  public designChangeClass: string;
  public cbuReflash: boolean;
  public scrapParts: boolean;
  public stragglerInterChangeable: boolean;
  public dcmsList: DcmsDto[];
  public modelCode: string;
  public modelType: string;
  public dcNumber: string;
  public isUnProcessed: boolean;
  public reFlash: boolean;
  public interchangeable: boolean;
  
  selectedRow: number;
  highlightedRow: Number;
  selectRow: Function;
  highlightLot: Function;
  dataSource: MatTableDataSource<DcmsDto>;
  displayedColumns = ['select', 'designChangeNumber', 'modelCode', 'ymtoCode', 'modelType', 'designChangePartNumber', 'designChangeClass','systemName', 'serviceInterchangeableCode', 'stragglerInterchangeableCode', 'effectiveBeginDate', 'effectiveEndDate'];
  selection = new SelectionModel<DcmsDto>(true, []);
  @ViewChild(MatSort) sort: MatSort;
  selectRowData: DcmsDto[];
  modelPart: ModelPart = new ModelPart();
  remFilter:string;
  designChangePartNumberFilter = new FormControl(['all']);
  effectiveBeginDateFilter = new FormControl(['all']);
  effectiveEndDateFilter = new FormControl(['all']);
  designChangeClassFilter = new FormControl(["all"]);
  systemNameFilter = new FormControl(['all']);
  serviceInterchangeableCodeFilter = new FormControl(['all']);
  stragglerInterchangeableCodeFilter = new FormControl(['all']);
  ymtoCodeFilter = new FormControl(['all']);
  modelCodeFilter = new FormControl(['all']);
  modelTypeFilter = new FormControl(['all']);
  designChangeNumberFilter = new FormControl(['all']);
  plantCodeFilter = new FormControl('');
  
  filterValues = {
    designChangePartNumber: ['all'],
    effectiveBeginDate: ['all'],
    effectiveEndDate: ['all'],
    designChangeClass: ['all'],
    systemName:['all'],
    serviceInterchangeableCode: ['all'],
    stragglerInterchangeableCode: ['all'],
    ymtoCode: ['all'],
    modelCode: ['all'],
    modelType: ['all'],
    designChangeNumber: ['all']
  };

  public message: Message = { type: null, message: null };
  messageOptionsAutoClose = {
    autoClose: false,
    keepAfterRouteChange: false
  };
  total: number;
  filteredRows: number;
  selectedRows: number;

  tempDcClass: any = [];
  tempModel: any = [];
  tempModelType: any = [];
  tempSystemName: any = [];
  tempDcNumber: any = [];
  tempDcPartNumber: any = [];
  tempEffBeginDate: any = [];
  tempYmto: any = [];
  tempEffEndDate: any = [];
  tempStraglrInt: any =[];
  tempServiceInt: any =[];
  atleastOneClassConfirm: boolean= false;

  constructor(
    private router: Router,
    public dialog: MatDialog,  
    public cdk: OverlayContainer, 
    protected alertService: AlertService,
    private confirmationDialogService: ConfirmationDialogService,
    private titleService: Title,
    private util: UtilityService,
    public ruleAssignmentService: RuleAssignmentService,
    public configService: ConfigService,
    public frameSpecService: FrameSpecService
  ) { 

    this.titleService.setTitle("SUMS-VIN BOM Rule Assignment");

    this.selectRow = function(index, row){
      this.selectedRow = index;
    }

    this.highlightLot = function(index){
      this.highlightedRow = index;
    }
    
  }

  
  selectionymtoCodeChange(value) {
    console.log('dc class change value --', value);
    this.ymtoCodeFilter.setValue(value);
  }

  selectionDcClassChange(value) {
    console.log('dc class change value --', value);
    this.designChangeClassFilter.setValue(value);
  }
  
  selectionModelChange(value) {
    console.log('dc model change value --', value);
    this.modelCodeFilter.setValue(value);
  }
  selectionModelTypeChange(value) {
    console.log('dc model type value --', value);
    this.modelTypeFilter.setValue(value);
  }
  selectionSystemNameChange(value) {
    console.log('dc systemname value --', value);
    this.systemNameFilter.setValue(value);
  }
  selectionDcNumberChange(value) {
    console.log('dc number value --', value);
    this.designChangeNumberFilter.setValue(value);
  }
  selectionDcPartNumberChange(value) {
    console.log('dc part number value --', value);
    this.designChangePartNumberFilter.setValue(value);
  }
  selectionEffBeginDateChange(value) {
    console.log('dc eff begin dt value --', value);
    this.effectiveBeginDateFilter.setValue(value);
  }
  selectionEffectiveEndDateChange(value) {
    console.log('dc start prod lot value --', value);
    this.effectiveEndDateFilter.setValue(value);
  }
  selectionStraglrIntChange(value) {
    
    this.stragglerInterchangeableCodeFilter.setValue(value);
  }

  selectionServiceIntChange(value){
this.serviceInterchangeableCodeFilter.setValue(value);
  }

  ngOnInit(): void {
    //this.designChangeClassFilter.setValue(['all']);
    //this.defaultSelectedClass=['all'];
    this.search();
    this.isAllSelected();
   this.ruleAssignmentService.getPlantCodeList().subscribe((plantCodeList: string[]) => {
      if(plantCodeList){
        this.plantCodeFilterList=plantCodeList;
        this.plantCode= this.configService.sites[this.configService.siteId]?.name;
      }else{
        this.plantCodeFilterList.fill(this.configService.sites[this.configService.siteId]?.name);
        this.plantCode= this.configService.sites[this.configService.siteId]?.name;
      }
    },(error: any) => {
      alert("error connecting to server");
    });

    this.designChangePartNumberFilter.valueChanges
    .subscribe(
      designChangePartNumber => {
        this.selection.clear();
        
        if(designChangePartNumber.includes('all')){
          console.log('inside if block of per select ');
          if(designChangePartNumber[0]=='all' && designChangePartNumber.length>1 && this.tempDcPartNumber?.length==0){
            console.log(' inside---  ',designChangePartNumber[0]);
            designChangePartNumber.splice(0,1);
            this.tempDcPartNumber=designChangePartNumber;
          }
          else if(this.tempDcPartNumber.length>0 && designChangePartNumber.includes('all')){
            designChangePartNumber.splice(1,designChangePartNumber.length);
            this.tempDcPartNumber=[];
          }
        }        
        
        this.filterValues.designChangePartNumber = designChangePartNumber;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )

    // this.designChangePartNumberFilter.valueChanges
    // .subscribe(
    //   designChangePartNumber => {
    //     this.selection.clear();
    //     this.filterValues.designChangePartNumber = designChangePartNumber;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    this.effectiveBeginDateFilter.valueChanges
    .subscribe(
      effectiveBeginDate => {
        this.selection.clear();
        if(effectiveBeginDate.includes('all')){
          console.log('inside if block of per select ');
          if(effectiveBeginDate[0]=='all' && effectiveBeginDate.length>1 && this.tempEffBeginDate?.length==0){
            console.log(' inside---  ',effectiveBeginDate[0]);
            effectiveBeginDate.splice(0,1);
            this.tempEffBeginDate=effectiveBeginDate;
          }
          else if(this.tempEffBeginDate.length>0 && effectiveBeginDate.includes('all')){
            effectiveBeginDate.splice(1,effectiveBeginDate.length);
            this.tempEffBeginDate=[];
          }
        }        
        this.filterValues.effectiveBeginDate = effectiveBeginDate;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }      
    )

    // this.effectiveBeginDateFilter.valueChanges
    // .subscribe(
    //   effectiveBeginDate => {
    //     this.selection.clear();
    //     this.filterValues.effectiveBeginDate = effectiveBeginDate;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )


    this.effectiveEndDateFilter.valueChanges
    .subscribe(
      effectiveEndDate => {
        this.selection.clear();
        if(effectiveEndDate.includes('all')){
          console.log('inside if block of per select ');
          if(effectiveEndDate[0]=='all' && effectiveEndDate.length>1 && this.tempEffEndDate?.length==0){
            console.log(' inside---  ',effectiveEndDate[0]);
            effectiveEndDate.splice(0,1);
            this.tempEffEndDate=effectiveEndDate;
          }
          else if(this.tempEffEndDate.length>0 && effectiveEndDate.includes('all')){
            effectiveEndDate.splice(1,effectiveEndDate.length);
            this.tempEffEndDate=[];
          }
        }        
        this.filterValues.effectiveEndDate = effectiveEndDate;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }      
    )

    // this.effectiveEndDateFilter.valueChanges
    // .subscribe(
    //   effectiveEndDate => {
    //     this.selection.clear();
    //     this.filterValues.effectiveEndDate = effectiveEndDate;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    this.designChangeClassFilter.valueChanges
    .subscribe(
      designChangeClass => {
        this.selection.clear();        
        if(designChangeClass.includes('all')){
          console.log('inside if block of per select ');
          if(designChangeClass[0]=='all' && designChangeClass.length>1 && this.tempDcClass?.length==0){
            console.log(' inside---  ',designChangeClass[0]);
            designChangeClass.splice(0,1);
            this.tempDcClass=designChangeClass;
          }
          else if(this.tempDcClass.length>0 && designChangeClass.includes('all')){
            designChangeClass.splice(1,designChangeClass.length);
            this.tempDcClass=[];
          }
        }                
        this.filterValues.designChangeClass = designChangeClass;        
        this.dataSource.filter = JSON.stringify(this.filterValues);        
      }      
    )

    // this.designChangeClassFilter.valueChanges
    // .subscribe(
    //   designChangeClass => {
    //     this.selection.clear();
    //     this.filterValues.designChangeClass = designChangeClass;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    this.systemNameFilter.valueChanges
    .subscribe(
      systemName => {
        this.selection.clear();        
        if(systemName.includes('all')){
          console.log('inside if block of per select ');
          if(systemName[0]=='all' && systemName.length>1 && this.tempSystemName?.length==0){
            console.log(' inside---  ',systemName[0]);
            systemName.splice(0,1);
            this.tempSystemName=systemName;
          }
          else if(this.tempSystemName.length>0 && systemName.includes('all')){
            systemName.splice(1,systemName.length);
            this.tempSystemName=[];
          }
        }                
        this.filterValues.systemName = systemName;        
        this.dataSource.filter = JSON.stringify(this.filterValues);       
      }      
    )

    // this.systemNameFilter.valueChanges
    // .subscribe(
    //   systemName => {
    //     this.selection.clear();
    //     this.filterValues.systemName = systemName;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //     console.log('systemname filter -- ',JSON.stringify(this.filterValues));
    //   }
    // )

    this.serviceInterchangeableCodeFilter.valueChanges
    .subscribe(
      serviceInterchangeableCode => {
        this.selection.clear();        
        if(serviceInterchangeableCode.includes('all')){
          console.log('inside if block of per select ');
          if(serviceInterchangeableCode[0]=='all' && serviceInterchangeableCode.length>1 && this.tempServiceInt?.length==0){
            console.log(' inside---  ',serviceInterchangeableCode[0]);
            serviceInterchangeableCode.splice(0,1);
            this.tempServiceInt=serviceInterchangeableCode;
          }
          else if(this.tempServiceInt.length>0 && serviceInterchangeableCode.includes('all')){
            serviceInterchangeableCode.splice(1,serviceInterchangeableCode.length);
            this.tempServiceInt=[];
          }
        }                
        this.filterValues.serviceInterchangeableCode = serviceInterchangeableCode;        
        this.dataSource.filter = JSON.stringify(this.filterValues);       
      }      
    )

    //this.serviceInterchangeableCodeFilter.setValue('All');
    // this.serviceInterchangeableCodeFilter.valueChanges
    // .subscribe(
    //   serviceInterchangeableCode => {
    //     this.selection.clear();
    //     this.filterValues.serviceInterchangeableCode = serviceInterchangeableCode;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //     console.log(JSON.stringify(this.filterValues));
    //   }
    // )

    this.stragglerInterchangeableCodeFilter.valueChanges
    .subscribe(
      stragglerInterchangeableCode => {
        this.selection.clear();        
        if(stragglerInterchangeableCode.includes('all')){
          console.log('inside if block of per select ');
          if(stragglerInterchangeableCode[0]=='all' && stragglerInterchangeableCode.length>1 && this.tempStraglrInt?.length==0){
            console.log(' inside---  ',stragglerInterchangeableCode[0]);
            stragglerInterchangeableCode.splice(0,1);
            this.tempStraglrInt=stragglerInterchangeableCode;
          }
          else if(this.tempStraglrInt.length>0 && stragglerInterchangeableCode.includes('all')){
            stragglerInterchangeableCode.splice(1,stragglerInterchangeableCode.length);
            this.tempStraglrInt=[];
          }
        }                
        this.filterValues.stragglerInterchangeableCode = stragglerInterchangeableCode;        
        this.dataSource.filter = JSON.stringify(this.filterValues);       
      }      
    )

    // this.stragglerInterchangeableCodeFilter.valueChanges
    // .subscribe(
    //   stragglerInterchangeableCode => {
    //     this.selection.clear();
    //     this.filterValues.stragglerInterchangeableCode = stragglerInterchangeableCode;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )

    this.ymtoCodeFilter.valueChanges
    .subscribe(
      ymtoCode => {
        this.selection.clear();        
        if(ymtoCode.includes('all')){
          console.log('inside if block of per select ');
          if(ymtoCode[0]=='all' && ymtoCode.length>1 && this.tempYmto?.length==0){
            console.log(' inside---  ',ymtoCode[0]);
            ymtoCode.splice(0,1);
            this.tempYmto=ymtoCode;
          }
          else if(this.tempYmto.length>0 && ymtoCode.includes('all')){
            ymtoCode.splice(1,ymtoCode.length);
            this.tempYmto=[];
          }
        }                
        this.filterValues.ymtoCode = ymtoCode;        
        this.dataSource.filter = JSON.stringify(this.filterValues);       
      }      
    )

    // this.ymtoCodeFilter.valueChanges
    // .subscribe(
    //   ymtoCode => {
    //     this.selection.clear();
    //     this.filterValues.ymtoCode = ymtoCode;
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
    //     console.log('modeltype filter -- ',JSON.stringify(this.filterValues));
    //   }
    // )


    this.designChangeNumberFilter.valueChanges
    .subscribe(
      designChangeNumber => {
        this.selection.clear();
        if(designChangeNumber.includes('all')){
          console.log('inside if block of per select ');
          if(designChangeNumber[0]=='all' && designChangeNumber.length>1 && this.tempDcNumber?.length==0){
            console.log(' inside---  ',designChangeNumber[0]);
            designChangeNumber.splice(0,1);
            this.tempDcNumber=designChangeNumber;
          }
          else if(this.tempDcNumber.length>0 && designChangeNumber.includes('all')){
            designChangeNumber.splice(1,designChangeNumber.length);
            this.tempDcNumber=[];
          }
        }        
        this.filterValues.designChangeNumber = designChangeNumber;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }      
    )


    // this.designChangeNumberFilter.valueChanges
    // .subscribe(
    //   designChangeNumber => {
    //     this.selection.clear();
    //     this.filterValues.designChangeNumber = designChangeNumber;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )
    this.populateDesignChangeRuleMap();
  }

  search(): void {
   // this.designChangeNumber = 'A2100008';   
   
    this.modelTypeFilter.setValue(['all']);
  
    this.systemNameFilter.setValue(['all']);
  
    this.designChangeNumberFilter.setValue(['all']);
  
    this.designChangeClassFilter.setValue(['all']);
  
    this.modelCodeFilter.setValue(['all']);
  
    this.ymtoCodeFilter.setValue(['all']);
  
    this.designChangePartNumberFilter.setValue(['all']);
  
    this.serviceInterchangeableCodeFilter.setValue(['all']);
  
    this.stragglerInterchangeableCodeFilter.setValue(['all']);
  
    this.effectiveBeginDateFilter.setValue(['all']);
  
    this.effectiveEndDateFilter.setValue(['all']);
  
   this.alertService.clear();
   this.dcmsList = [];
   this.dataSource = new MatTableDataSource(this.dcmsList);
    if(this.designChangeNumber === undefined || this.designChangeNumber.trim().length == 0) {
      this.dataSource = new MatTableDataSource(this.dcmsList);
      this.dataSource.sort = this.sort;
      this.dataSource.filterPredicate = this.createFilter();
      console.log('this.dataSource.data after create filter --- ',this.dataSource.data);
    }else if(this.designChangeNumber.trim().length < 8) {
      console.log("Design Change Number cannot be less than 8 characters");
      alert("Design Change Number cannot be less than 8 characters");
      return;
    }else if(this.plantCode === undefined || this.plantCode.trim().length == 0){
      console.log("Plant code Required");
      alert("Please select Plant Code");
      return;
    }else {
      this.loading = true;
      var dash = /-/gi; 
      var space = / /gi; 
      var dcn =String(this.designChangeNumber).replace(space,'').replace(dash,'');
      console.log('dcn', dcn);
      this.ruleAssignmentService.getDesignChange(dcn.toUpperCase(),this.plantCode)
        .subscribe((dcmsList: DcmsDto[]) => {
          if(dcmsList){
            this.loading = false;
          }
          if(dcmsList === undefined || dcmsList === null){
            console.log("No results retrieved ");
            alert("No results retrieved");
            this.loading = false;
            return;
          }else if(dcmsList.length === 0){
            console.log("No results retrieved ");
            alert("No results retrieved");
            this.loading = false;
            return;
          }
          var replaedNull = JSON.stringify(dcmsList).replace(/null/g, '""');
          var newArray = JSON.parse(replaedNull);
          this.dcmsList = newArray;
          this.modelCodeList = (Array.from(new Set(this.dcmsList.map(ele=>ele.modelCode)))).sort();
          this.modelTypeList = (Array.from(new Set(this.dcmsList.map(ele=>ele.modelType)))).sort();
          this.dcPartNumberList = (Array.from(new Set(this.dcmsList.map(ele=>ele.designChangePartNumber)))).sort();
          this.ymtoCodeList = (Array.from(new Set(this.dcmsList.map(ele=>ele.ymtoCode)))).sort();
          this.dcNumberList = (Array.from(new Set(this.dcmsList.map(ele=>ele.designChangeNumber)))).sort();
          this.effBeginDateList = (Array.from(new Set(this.dcmsList.map(ele=>ele.effectiveBeginDate)))).sort();
          this.effEndDateList = (Array.from(new Set(this.dcmsList.map(ele=>ele.effectiveEndDate)))).sort();
          this.dcClassList = (Array.from(new Set(this.dcmsList.map(ele=>ele.designChangeClass)))).sort();
          this.systemNameList = (Array.from(new Set(this.dcmsList.map(ele=>ele.letSystemName)))).sort();
          this.serviceIcCodeList = (Array.from(new Set(this.dcmsList.map(ele=>ele.serviceInterchangeableCode)))).sort();
          this.stragglerIcCodeList = (Array.from(new Set(this.dcmsList.map(ele=>ele.stragglerInterchangeableCode)))).sort();

          this.dataSource = new MatTableDataSource(this.dcmsList);
          this.dataSource.sort = this.sort;
          this.dataSource.filterPredicate = this.createFilter();
          console.log('this.dataSource.data after filter --- ',this.dataSource.data);
          this.total=this.dataSource.data.length;
          if(this.remFilter){
            console.log("setting filter to - "+this.remFilter);
            this.filterValues=JSON.parse(this.remFilter);
            this.dataSource.filter = this.remFilter;
            } 

        },(error: any) => {
          alert("error connecting to server");
        });
    }
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.filteredData.length;
    this.filteredRows=this.dataSource.filteredData.length;
    this.selectedRows=this.selection.selected.length;
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
    console.log(this.selection);
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
 
  

  saveModelPart(item: DcmsDto) {
    this.modelPart.modelPartId = null;
    this.modelPart.productSpecWildcard = item.ymtoCode+item.modelType;
    this.modelPart.letSystemName = item.letSystemName;
    this.modelPart.dcPartNumber = item.designChangePartNumber;
    this.modelPart.active = 'INACTIVE';
    this.modelPart.dcClass = item.designChangeClass;
    this.modelPart.dcEffBegDate = item.effectiveBeginDate;
    this.modelPart.dcNumber = item.designChangeNumber;    
    this.modelPart.interchangeable = this.stragglerInterChangeable;
    this.modelPart.reflash = this.cbuReflash;
    this.modelPart.scrapParts = this.scrapParts;
    this.ruleAssignmentService.saveModelPart(this.modelPart).subscribe(data => {     
      console.log("saved...");
      //this.search();
      this.alertService.clear();
      this.alertService.success('Rule assigned successfully. ', this.messageOptionsAutoClose);
    },(error: any) => {
      alert("Error in Rule assignment");
    });
  }

  makeModelPart(item: DcmsDto) {
    this.modelPart.modelPartId = null;
    this.modelPart.productSpecWildcard = item.ymtoCode+item.modelType;
    this.modelPart.letSystemName = item.letSystemName;
    this.modelPart.dcPartNumber = item.designChangePartNumber;
    this.modelPart.active = 'INACTIVE';
    this.modelPart.dcClass = item.designChangeClass;
    this.modelPart.dcEffBegDate = item.effectiveBeginDate;
    this.modelPart.dcNumber = item.designChangeNumber;    
    this.modelPart.interchangeable = this.stragglerInterChangeable;
    this.modelPart.reflash = this.cbuReflash;
    this.modelPart.scrapParts = this.scrapParts;
    
    return this.modelPart;
  }

  createFilter(): (data: any, filter: string) => boolean {
   
    let filterFunction = function(data, filter): boolean {
      let searchTerms = JSON.parse(filter);
      let systemNameArray = searchTerms.systemName as Array<String>;
      let modelTypeArray = searchTerms.modelType as Array<String>;
      let dcPartNumberArray = searchTerms.designChangePartNumber as Array<String>;
      let modelCodeArray = searchTerms.modelCode as Array<String>;
      let ymtoCodeArray = searchTerms.ymtoCode as Array<String>;
      let dcChangeClassArray = searchTerms.designChangeClass as Array<String>;
      let dcNumberArray = searchTerms.designChangeNumber as Array<String>;
      let stragglerInterchangeableCodeArray = searchTerms.stragglerInterchangeableCode as Array<String>;
      let serviceInterchangeableCodeArray = searchTerms.serviceInterchangeableCode as Array<String>;
      let effectiveBeginDateArray = searchTerms.effectiveBeginDate as Array<String>;
      let effectiveEndDateArray = searchTerms.effectiveEndDate as Array<String>;
      console.log(data.designChangeClass.toLowerCase());
      console.log(searchTerms.designChangeClass);
      
     // console.log(String(searchTerms.serviceInterchangeableCode));
      let serviceIntMatch: boolean= true;
      if(serviceInterchangeableCodeArray.length === 0){
        serviceIntMatch = serviceInterchangeableCodeArray.includes(data.serviceInterchangeableCode);
        console.log("serviceInt - "+String(data.serviceInterchangeableCode) +"-"+String(data.serviceInterchangeableCode).length +"-"+serviceIntMatch);
      }else if(serviceInterchangeableCodeArray.includes('All')){
        serviceIntMatch = true;
       } else{
        serviceIntMatch = serviceInterchangeableCodeArray.includes(data.serviceInterchangeableCode?.toLowerCase());
        console.log("serviceInt - "+String(data.serviceInterchangeableCode) +"-"+String(data.serviceInterchangeableCode).length +"-"+serviceIntMatch);
      }

      console.log("searchTerms -- "+JSON.stringify(searchTerms));



let modelTypeFlag=(modelTypeArray.includes('all') ) ? true : modelTypeArray.includes(data.modelType.toLowerCase());
let systemNameFlag=(systemNameArray.includes('all') ) ? true : systemNameArray.includes(data.letSystemName.toLowerCase());
let dcPartNumberFlag=(dcPartNumberArray.includes('all') ) ? true : dcPartNumberArray.includes(data.designChangePartNumber?.toLowerCase());
let modelCodeFlag=(modelCodeArray.includes('all') ) ? true : modelCodeArray.includes(String(data.modelCode).toLowerCase());
let ymtoCodeFlag=(ymtoCodeArray.includes('all') ) ? true : ymtoCodeArray.includes(String(data.ymtoCode).toLowerCase());
//let dcChangeClassFlag=(dcChangeClassArray.includes('all') || dcChangeClassArray.includes('')) ? true : dcChangeClassArray.includes(data.designChangeClass?.trim().toLowerCase());
let dcChangeClassFlag=(dcChangeClassArray.includes('all')) ? true : dcChangeClassArray.includes(data.designChangeClass?.toLowerCase());
let dcNumberFlag=(dcNumberArray.includes('all') ) ? true : dcNumberArray.includes(data.designChangeNumber?.trim().toLowerCase());
let stragglerInterchangeableCodeFlag=(stragglerInterchangeableCodeArray.includes('all') ) ? true : stragglerInterchangeableCodeArray.includes(data.stragglerInterchangeableCode?.trim());
let serviceInterchangeableCodeFlag=(serviceInterchangeableCodeArray.includes('all') ) ? true : serviceInterchangeableCodeArray.includes(data.serviceInterchangeableCode?.trim());
let effectiveBeginDateFlag=(effectiveBeginDateArray.includes('all') ) ? true : effectiveBeginDateArray.includes(data.effectiveBeginDate?.toString().slice(0,10));
let effectiveEndDateFlag=(effectiveEndDateArray.includes('all') ) ? true : effectiveEndDateArray.includes(data.effectiveEndDate?.toString().slice(0,10));

      console.log(String(data.model)+"-"+String(data.modelType)+"-"+String(data.letSystemName)+"-"+String(data.active) + "data - " + JSON.stringify(data) );
      return dcPartNumberFlag
      //data.designChangePartNumber?.toLowerCase().indexOf(searchTerms.designChangePartNumber.toLowerCase()) !== -1
        && effectiveBeginDateFlag
        && effectiveEndDateFlag
       // && data.effectiveBeginDate?.toString().slice(0,10).indexOf(searchTerms.effectiveBeginDate) !== -1
        //&& data.effectiveEndDate?.toString().slice(0,10).indexOf(searchTerms.effectiveEndDate) !== -1
        //&& data.designChangeClass?.trim().toLowerCase().indexOf(searchTerms.designChangeClass.trim().toLowerCase()) !== -1
        && dcChangeClassFlag
        //&& data.systemName?.trim().toLowerCase().indexOf(searchTerms.systemName.trim().toLowerCase()) !== -1
        //&& data.letSystemName.toLowerCase().indexOf(searchTerms.systemName.toLowerCase()) !== -1        
        //&& (modelTypeArray.includes('all') || modelTypeArray.includes('')) ? true : modelTypeArray.findIndex(data.modelType.toLowerCase())!==-1
        && modelTypeFlag
        && systemNameFlag
        && serviceInterchangeableCodeFlag
        //&& data.serviceInterchangeableCode?.toLowerCase().indexOf(searchTerms.serviceInterchangeableCode.toLowerCase()) !== -1
        && stragglerInterchangeableCodeFlag
        //&& data.stragglerInterchangeableCode?.toLowerCase().indexOf(searchTerms.stragglerInterchangeableCode.toLowerCase()) !== -1
        //&& String(data.ymtoCode).toLowerCase().indexOf(searchTerms.ymtoCode.toLowerCase()) !== -1
        && ymtoCodeFlag
        //&& String(data.modelCode).toLowerCase().indexOf(searchTerms.modelCode.toLowerCase()) !== -1
        && modelCodeFlag
        //&& String(data.modelType).toLowerCase().indexOf(searchTerms.modelType.toLowerCase()) !== -1
       
        && dcNumberFlag;
    }
   this.isAllSelected();
    return filterFunction;
  }



  public setRules() {
    this.alertService.clear();
    if(this.selection.selected.length <= 0) {
      alert("Please select at least one row from above grid");
      return;
    }
    if(this.cbuReflash === undefined || this.scrapParts === undefined || this.stragglerInterChangeable === undefined) {
      alert("Please select Rules");
      return;
    }
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);
    let count = 0;
    let invalid = 0;
    let cancelledCount = 0;
    let errorMap = new Map();
    let cbuReflasherrorMap = new Map();
    let stragglerInterrorMap = new Map();
    let scrapParterrorMap = new Map();
    this.selection.selected.forEach(async (eachRow, index) => {
      const isValid = await this.ruleAssignmentService.validateRuleSelection(eachRow.designChangeClass, this.cbuReflash, this.stragglerInterChangeable, this.scrapParts).toPromise();
      if (!isValid) {
        console.log('Selected Rule does not match expected rules for: ' + eachRow.designChangeClass);
        invalid++;
        count++;
        console.log(invalid);
        
        if(errorMap.has(eachRow.designChangeClass)){
          let c = errorMap.get(eachRow.designChangeClass);
          if(c.indexOf(eachRow.designChangeNumber) !== -1 ){
            console.log(eachRow.designChangeNumber +"- already exists");
          }else{
            c.push(eachRow.designChangeNumber);
          }
         
          errorMap.set(eachRow.designChangeClass,c);
        
        }else{
          let d : string[]=[];
          d.push(eachRow.designChangeNumber);
          errorMap.set(eachRow.designChangeClass,d);
        }
        cbuReflasherrorMap.set(eachRow.designChangeNumber,this.cbuReflash ?'Y':'N');
        stragglerInterrorMap.set(eachRow.designChangeNumber,this.stragglerInterChangeable?'Y':'N');
        scrapParterrorMap.set(eachRow.designChangeNumber,this.scrapParts?'Y':'N');

        if(count > this.selection.selected.length || count === this.selection.selected.length ){
        
            console.log(index);
            
            this.update(count,invalid,errorMap, cbuReflasherrorMap, stragglerInterrorMap,scrapParterrorMap);
       
        } 
      } else {
        console.log('Selected rule matches expected rules for: ' + eachRow.designChangeClass);
        this.atleastOneClassConfirm=true;
        await this.ruleAssignmentService.saveModelPart(this.makeModelPart(eachRow)).toPromise();
        count++;
        this.showMessage(this.selection.selected.length, count, cancelledCount);
        
      }
    });

  }

  async update(recCount:number,errorCount:number, errorMap:Map<string,string[]>, cbuReflasherrorMap:Map<string,string>, stragglerInterrorMap:Map<string,string>,scrapParterrorMap:Map<string,string>){
    let count = 0;
    let cancelledCount = 0;
    if(errorCount > 0){ 
      if(this.designChangeRuleMap.size === 0){
        this.populateDesignChangeRuleMap();
      }
     
        console.log("invalid rules count - "+ errorCount);
        errorMap.forEach(async(values: string[], key: string) => {
          let designChangeRule:DesignChangeRule = this.designChangeRuleMap.get(key);
          let standardReflash:string = '';
          let standardInt:string ='' ;
          let standardScrap:string = '';
          let msg:string='';

          if(designChangeRule){
            standardReflash = this.getVinBomRuleRequiredValue(designChangeRule.reflash);
            standardInt = this.getVinBomRuleRequiredValue(designChangeRule.interchangable);
            standardScrap = this.getVinBomRuleRequiredValue(designChangeRule.scrapParts);
          }else{
            console.log("design change Rule not found for class -"+key);
          }
          values.forEach(async function (value) {
            
            //NALC-1084
          msg = msg + 
          '< '+value+'> ' + key + '- Class line items do not match standard rule guideline. <br>'+
          'CBU Reflash (Selected = '+ cbuReflasherrorMap.get(value)+', Standard = '+standardReflash +' ) <br>'+
          'Straggler-New (Selected = '+ stragglerInterrorMap.get(value)+', Standard = '+standardInt+' ) <br>'+
          'Interchangeable (Selected = '+ scrapParterrorMap.get(value)+', Standard = '+standardScrap+' ) <br>';
          });
     
          console.log(msg);

        const isConfirmed = await this.confirmationDialogService.confirmLarge('Please confirm..  Selected Design Change Rules do not match rules for ', '<b>'+ msg+' </b> <br> Do you want to continue? ', 'Yes', 'No', 'lg');
        if(isConfirmed) {
          console.log("User selected: Yes");

          this.selection.selected.forEach(async(eachRow, index) => {
          console.log("User selected: Yes");
          console.log(index);
          if(eachRow.designChangeClass===key){
            this.atleastOneClassConfirm=true;
            count++;
          await this.ruleAssignmentService.saveModelPart(this.makeModelPart(eachRow)).toPromise();
        }
         
                       
          });
          
          this.showMessage(this.selection.selected.length, count, cancelledCount); 
        } else {  
          count++;
          cancelledCount++;
          console.log("User selected: No");
          this.showMessage(this.selection.selected.length, count, cancelledCount);
        }
      });
    }else{
      console.log(" no invalid rules count  ");
      this.selection.selected.forEach(async(eachRow, index) => {
        console.log('Selected rule matches expected rules for: ' + eachRow.designChangeClass);
        this.atleastOneClassConfirm=true;
        await this.ruleAssignmentService.saveModelPart(this.makeModelPart(eachRow)).toPromise();
        count++;
        this.showMessage(this.selection.selected.length, count, cancelledCount);      
        });
        
    }
    
  }

  showMessage(totalRows: number, processedRows: number, cancelledCount: number) {
    if (totalRows > 0 && totalRows === cancelledCount) {
      console.log("Cancelled All selected..."+ totalRows +"---"+ cancelledCount);
    } else if (totalRows === processedRows && this.atleastOneClassConfirm) {
      console.log(totalRows + " - " + processedRows);
      this.alertService.success('Rule assigned successfully. ', this.messageOptionsAutoClose)
      //this.resetPage();NALC-1085
      this.confirmationDialogService.confirm('', 'Do you want to navigate to Lot Assignment screen? ', 'Yes', 'No', 'lg')
          .then((confirmed) => {
            if(confirmed) {
              this.resetPage();//NALC-1085
              this.router.navigate(['/lot-assignment']);
            } else {
              this.resetPage();//NALC-1085
              this.selection.clear();
   
            }
            this.atleastOneClassConfirm=false;
          }).catch(() => {
            
          });
    }
     
  }

  async validateRuleSelection2(designChangeClass: string, cbuReflash: boolean, stragglerInterChangeable: boolean, scrapParts: boolean) {
    let isRuleMatched = await this.ruleAssignmentService.validateRuleSelection2(designChangeClass, cbuReflash, stragglerInterChangeable, scrapParts);
    return isRuleMatched;
   }

    populateDesignChangeRuleMap(){
     let designchangeRules = this.ruleAssignmentService.getDesignChangeRules().subscribe((designChangeRuleList: DesignChangeRule[]) => {
      this.designChangeRuleList = designChangeRuleList;
      designChangeRuleList.forEach((value:DesignChangeRule,index:number)=>{
        console.log(value);
      this.designChangeRuleMap.set(value.dcClass,value);
        index++;
      });
     },(error: any) => {
          
    });
   


  }
  getVinBomRuleRequiredValue(value:string):string{
    console.log('value-' + value);
    return value.toUpperCase().indexOf('NOT_APPLIED')!== -1?'N':value.toUpperCase().indexOf('REQUIRED') !== -1?'Y':value.toUpperCase().indexOf('OPTIONAL')!== -1 ?'Optional':value.toString();
   
  }
  resetPage(){
    //if(this.isAllSelected){
      this.remFilter = "";
      this.dataSource.filter = this.remFilter;
      console.log.apply("reset filters");
    //}
    this.cbuReflash=undefined;
    this.scrapParts=undefined;
    this.stragglerInterChangeable=undefined;
    this.designChangePartNumberFilter.setValue(['all']);
  this.filterValues.designChangeClass=["all"];
    this.effectiveBeginDateFilter.setValue(['all']);
    this.effectiveEndDateFilter.setValue(['all']);
    this.designChangeClassFilter.setValue(["all"]);
    this.serviceInterchangeableCodeFilter.setValue(['all']);
    this.stragglerInterchangeableCodeFilter.setValue(['all']);
    this.ymtoCodeFilter.setValue(['all']);
    this.modelCodeFilter.setValue(['all']);
    this.modelTypeFilter.setValue(['all']);
    this.designChangeNumberFilter.setValue(['all']);
     this.search();
  }
}
