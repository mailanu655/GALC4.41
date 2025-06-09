import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Title } from '@angular/platform-browser';
import { AlertService } from 'src/app/alert/alert.service';
import { ManualOverrideApproval } from 'src/app/models/manual-override-approval';
import { Message } from 'src/app/models/message';
import { VinPartApproval } from 'src/app/models/vin-part-approval';
import { ManualOverrideApprovalService } from 'src/app/services/manual-override-approval.service';

@Component({
  selector: 'app-manual-override-approval',
  templateUrl: './manual-override-approval.component.html',
  styleUrls: ['./manual-override-approval.component.css']
})
export class ManualOverrideApprovalComponent implements OnInit {

  public loading = false;
  public manualOverrideList: VinPartApproval[];
  public message: Message = { type: null, message: null };
  selectedRow: number;
  selectedRowData: VinPartApproval;
  newVinPartApproval: VinPartApproval = new VinPartApproval();
  highlightedRow: Number;
  selectRow: Function;
  highlightBomPart: Function;
  displayedColumns = ['productId','productSpecCode', 'productionLot', 'letSystemName', 'currentDcPartNumber', 'newDcPartNumber', 'interchangeable', 'currentShipStatus', 'newShipStatus', 'requestAssociateName', 'requestTimestamp'];
  dataSource: MatTableDataSource<VinPartApproval>;
  @ViewChild(MatSort) sort: MatSort;

  public productIdList: string[];
  public productSpecCodeList: string[];
  public productionLotList: string[];
  public letSystemNameList: string[];
  public currentDcPartNumberList: string[];
  public newDcPartNumberList: string[];
  public requestAssociateNameList: string[];
  remFilter:string;

  productIdFilter = new FormControl(['all']);
  productSpecCodeFilter = new FormControl(['all']);
  productionLotFilter = new FormControl(['all']);
  letSystemNameFilter = new FormControl(['all']);
  currentDcPartNumberFilter = new FormControl(['all']);
  newDcPartNumberFilter = new FormControl(['all']);
  dcNumberFilter = new FormControl('');
  interchangeableFilter = new FormControl('');
  currentShipStatusFilter = new FormControl('');
  newShipStatusFilter = new FormControl('');
  requestAssociateNameFilter = new FormControl(['all']);
  requestTimestampFilter = new FormControl('');

  tempProductId: any = [];
  tempProdLot: any = [];
  tempProdSpecCode: any = [];
  tempSystemName: any = [];  
  tempCurrentDcPartNumber: any = [];
  tempNewDcPartNumber: any = [];
  tempRequestor: any = [];

  filterValues = {
    productId: ['all'],
    productSpecCode:['all'],
    productionLot:['all'],
    letSystemName: ['all'],
    currentDcPartNumber: ['all'],
    newDcPartNumber: ['all'],
    interchangeable: '',
    currentShipStatus: '',
    newShipStatus: '',
    requestAssociateName: ['all'],
    requestTimestamp: '',
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
    protected alertService: AlertService,
    private titleService: Title,
    public manualOverrideApprovalService: ManualOverrideApprovalService,
  ) { 
    this.titleService.setTitle("SUMS-Manual Override Approval");

    this.selectRow = function(index, row){
      this.selectedRow = index;
      this.selectedRowData = row;
    }

    this.highlightBomPart = function(index){
      this.highlightedRow = index;
    }
  }

  clearSelection():void {
    this.selectRow(-1,null);
  }
  selectionProdIdChange(value) {
    console.log('dc part number value --', value);
    this.productIdFilter.setValue(value);
  }

  selectionProdLotChange(value) {
    console.log('dc part number value --', value);
    this.productionLotFilter.setValue(value);
  }

  selectionProdSpecChange(value) {
    console.log('dc part number value --', value);
    this.productSpecCodeFilter.setValue(value);
  }
  selectionSystemNameChange(value) {
    console.log('dc systemname value --', value);
    this.letSystemNameFilter.setValue(value);
  }
  selectionRequestorChange(value) {
    console.log('dc requestor change value --', value);
    this.requestAssociateNameFilter.setValue(value);
  }
  selectionCurrentDcPartNumberChange(value) {
    console.log('dc part number value --', value);
    this.currentDcPartNumberFilter.setValue(value);
  }
  selectionNewDcPartNumberChange(value) {
    console.log('dc part number value --', value);
    this.newDcPartNumberFilter.setValue(value);
  }





  ngOnInit(): void {
    //this.findAllPending();
    this.search();

    this.productIdFilter.valueChanges
    .subscribe(
      productId => {
        this.clearSelection();
        
        if(productId.includes('all')){
          console.log('inside if block of per select ');
          if(productId[0]=='all' && productId.length>1 && this.tempProductId?.length==0){
            console.log(' inside---  ',productId[0]);
            productId.splice(0,1);
            this.tempProductId=productId;
          }
          else if(this.tempProductId.length>0 && productId.includes('all')){
            productId.splice(1,productId.length);
            this.tempProductId=[];
          }
        }        
        
        this.filterValues.productId = productId;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )

    // this.productIdFilter.valueChanges
    // .subscribe(
    //   productId => {
    //     this.clearSelection();
    //     this.filterValues.productId = productId;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //     console.log(this.filterValues);
    //   }
    // )

    this.productSpecCodeFilter.valueChanges
    .subscribe(
      productSpecCode => {
        this.clearSelection();
        
        if(productSpecCode.includes('all')){
          console.log('inside if block of per select ');
          if(productSpecCode[0]=='all' && productSpecCode.length>1 && this.tempProdSpecCode?.length==0){
            console.log(' inside---  ',productSpecCode[0]);
            productSpecCode.splice(0,1);
            this.tempProdSpecCode=productSpecCode;
          }
          else if(this.tempProdSpecCode.length>0 && productSpecCode.includes('all')){
            productSpecCode.splice(1,productSpecCode.length);
            this.tempProdSpecCode=[];
          }
        }        
        
        this.filterValues.productSpecCode = productSpecCode;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )

    // this.productSpecCodeFilter.valueChanges
    // .subscribe(
    //   productSpecCode => {
    //     this.clearSelection();
    //     this.filterValues.productSpecCode = productSpecCode;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //     console.log(this.filterValues);
    //   }
    // )
   
    this.productionLotFilter.valueChanges
    .subscribe(
      productionLot => {
        this.clearSelection();
        
        if(productionLot.includes('all')){
          console.log('inside if block of per select ');
          if(productionLot[0]=='all' && productionLot.length>1 && this.tempProdLot?.length==0){
            console.log(' inside---  ',productionLot[0]);
            productionLot.splice(0,1);
            this.tempProdLot=productionLot;
          }
          else if(this.tempProdLot.length>0 && productionLot.includes('all')){
            productionLot.splice(1,productionLot.length);
            this.tempProdLot=[];
          }
        }        
        
        this.filterValues.productionLot = productionLot;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )

    // this.productionLotFilter.valueChanges
    // .subscribe(
    //   productionLot => {
    //     this.clearSelection();
    //     this.filterValues.productionLot = productionLot;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //     console.log(this.filterValues);
    //   }
    // )

    this.letSystemNameFilter.valueChanges
    .subscribe(
      letSystemName => {
        this.clearSelection();
        
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
    //     this.clearSelection();
    //     this.filterValues.letSystemName = letSystemName;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //     console.log(this.filterValues);
    //   }
    // )

    this.currentDcPartNumberFilter.valueChanges
    .subscribe(
      currentDcPartNumber => {
        this.clearSelection();
        
        if(currentDcPartNumber.includes('all')){
          console.log('inside if block of per select ');
          if(currentDcPartNumber[0]=='all' && currentDcPartNumber.length>1 && this.tempCurrentDcPartNumber?.length==0){
            console.log(' inside---  ',currentDcPartNumber[0]);
            currentDcPartNumber.splice(0,1);
            this.tempCurrentDcPartNumber=currentDcPartNumber;
          }
          else if(this.tempCurrentDcPartNumber.length>0 && currentDcPartNumber.includes('all')){
            currentDcPartNumber.splice(1,currentDcPartNumber.length);
            this.tempCurrentDcPartNumber=[];
          }
        }        
        
        this.filterValues.currentDcPartNumber = currentDcPartNumber;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )


    // this.currentDcPartNumberFilter.valueChanges
    // .subscribe(
    //   currentDcPartNumber => {
    //     this.clearSelection();
    //     this.filterValues.currentDcPartNumber = currentDcPartNumber;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //     console.log(this.filterValues);
    //   }
    // )

    this.newDcPartNumberFilter.valueChanges
    .subscribe(
      newDcPartNumber => {
        this.clearSelection();
        
        if(newDcPartNumber.includes('all')){
          console.log('inside if block of per select ');
          if(newDcPartNumber[0]=='all' && newDcPartNumber.length>1 && this.tempNewDcPartNumber?.length==0){
            console.log(' inside---  ',newDcPartNumber[0]);
            newDcPartNumber.splice(0,1);
            this.tempNewDcPartNumber=newDcPartNumber;
          }
          else if(this.tempNewDcPartNumber.length>0 && newDcPartNumber.includes('all')){
            newDcPartNumber.splice(1,newDcPartNumber.length);
            this.tempNewDcPartNumber=[];
          }
        }        
        
        this.filterValues.newDcPartNumber = newDcPartNumber;        
        this.dataSource.filter = JSON.stringify(this.filterValues);
       
      }      
    )

    // this.newDcPartNumberFilter.valueChanges
    // .subscribe(
    //   newDcPartNumber => {
    //     this.clearSelection();
    //     this.filterValues.newDcPartNumber = newDcPartNumber;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //     console.log(this.filterValues);
    //   }
    // )
   
    this.interchangeableFilter.valueChanges
    .subscribe(
      interchangeable => {
        this.clearSelection();
        this.filterValues.interchangeable = interchangeable;
        this.dataSource.filter = JSON.stringify(this.filterValues);
        console.log(this.filterValues);
      }
    )
    this.currentShipStatusFilter.valueChanges
    .subscribe(
      currentShipStatus => {
        this.clearSelection();
        this.filterValues.currentShipStatus = currentShipStatus;
        this.dataSource.filter = JSON.stringify(this.filterValues);
        console.log(this.filterValues);
      }
    )
    this.newShipStatusFilter.valueChanges
    .subscribe(
      newShipStatus => {
        this.clearSelection();
        this.filterValues.newShipStatus = newShipStatus;
        this.dataSource.filter = JSON.stringify(this.filterValues);
        console.log(this.filterValues);
      }
    )
    
    this.requestAssociateNameFilter.valueChanges
    .subscribe(
      requestAssociateName => {
        this.clearSelection();
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
    //     this.clearSelection();
    //     this.filterValues.requestAssociateName = requestAssociateName;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //     console.log(this.filterValues);
    //   }
    // )
    
    
  }
  search(): void {
    this.clearSelection();
    this.loading = true;
    this.manualOverrideApprovalService.findAllPending()
    .subscribe((manualOverrideList: VinPartApproval[]) => {
      var replaedNull = JSON.stringify(manualOverrideList).replace(/null/g, '""');
    
              var newArray = JSON.parse(replaedNull);
              this.manualOverrideList = newArray;
  
      this.letSystemNameList = (Array.from(new Set(this.manualOverrideList.map(ele=>ele.letSystemName)))).sort();
      this.currentDcPartNumberList = (Array.from(new Set(this.manualOverrideList.map(ele=>ele.currentDcPartNumber)))).sort();
      this.newDcPartNumberList = (Array.from(new Set(this.manualOverrideList.map(ele=>ele.newDcPartNumber)))).sort();
      this.productIdList = (Array.from(new Set(this.manualOverrideList.map(ele=>ele.productId)))).sort();
      this.productSpecCodeList = (Array.from(new Set(this.manualOverrideList.map(ele=>ele.productSpecCode)))).sort();
      this.productionLotList = (Array.from(new Set(this.manualOverrideList.map(ele=>ele.productionLot)))).sort();
      this.requestAssociateNameList = (Array.from(new Set(this.manualOverrideList.map(ele=>this.abbreviatedName(ele.requestAssociateName))))).sort();

//NALC-1093
      let sortedList = this.manualOverrideList.sort((a,b) => (a.currentDcPartNumber < b.currentDcPartNumber) ? -1 : 1).
      sort((a,b) => (a.productId < b.productId) ? -1 : 1).        
      sort((a, b) => (a.productSpecCode < b.productSpecCode) ? -1 : 1)
      ;

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
      console.log("error loading data");
      this.loading = false;
    });

  }

 /* findAllPending() {
    this.manualOverrideApprovalService.findAllPending()
    .subscribe((manualOverrideList: VinPartApproval[]) => {
      this.manualOverrideList = manualOverrideList;
      this.dataSource = new MatTableDataSource(manualOverrideList);
      this.dataSource.sort = this.sort;
      
    },(error: any) => {
      
    });
  }*/
  approveChange() {
    this.alertService.clear();
    if(this.selectedRowData === null || this.selectedRowData.vinPartApprovalId === undefined) {
      alert("Please select data ");
      return;
    }
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);

    this.newVinPartApproval.vinPartApprovalId = this.selectedRowData.vinPartApprovalId;
    this.manualOverrideApprovalService.approveChange(this.newVinPartApproval).subscribe(data => {
      this.search(); 
      this.alertService.success('Approval Success', this.messageOptionsAutoClose);
    },(error: any) => {
      alert("Error removing Part");
    });
  }

  denyChange() {
    this.alertService.clear();
    if(this.selectedRowData === null  || this.selectedRowData.vinPartApprovalId === undefined) {
      alert("Please select data ");
      return;
    }
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);
    
    this.newVinPartApproval.vinPartApprovalId = this.selectedRowData.vinPartApprovalId;
    this.manualOverrideApprovalService.denyChange(this.newVinPartApproval).subscribe(data => {
      this.search();  
      this.alertService.success('Deny Change Success', this.messageOptionsAutoClose);
    },(error: any) => {
      alert("Error removing Part");
    });
  }

  createFilter(): (data: any, filter: string) => boolean {
    let filterFunction = function(data, filter): boolean {
      let searchTerms = JSON.parse(filter);

      let systemNameArray = searchTerms.letSystemName as Array<String>;	  
      let productIdArray = searchTerms.productId as Array<String>;
        let productionLotArray = searchTerms.productionLot as Array<String>;
        let productSpecCodeArray = searchTerms.productSpecCode as Array<String>; 
      let currentDcPartNumberArray = searchTerms.currentDcPartNumber as Array<String>;
      let newDcPartNumberArray = searchTerms.newDcPartNumber as Array<String>;
      let requestAssociateNameArray = searchTerms.requestAssociateName as Array<String>;	  
   
      
      let systemNameFlag=(systemNameArray.includes('all') ) ? true : systemNameArray.includes(data.letSystemName.toLowerCase());
      let currentDcPartNumberFlag=(currentDcPartNumberArray.includes('all') ) ? true : currentDcPartNumberArray.includes(data.currentDcPartNumber.toLowerCase());
      let newDcPartNumberFlag=(newDcPartNumberArray.includes('all') ) ? true : newDcPartNumberArray.includes(data.newDcPartNumber.toLowerCase());
      let productIdFlag=(productIdArray.includes('all') ) ? true : productIdArray.includes(data.productId.toLowerCase());
      let productionLotFlag=(productionLotArray.includes('all') ) ? true : productionLotArray.includes(data.productionLot.toLowerCase());
      let productSpecCodeFlag=(productSpecCodeArray.includes('all') ) ? true : productSpecCodeArray.includes(data.productSpecCode.toLowerCase());
      
      let index = data.requestAssociateName.toLowerCase().indexOf(' ');
      
      let requestAssociateNameFlag=(requestAssociateNameArray.includes('all') ) ? true : requestAssociateNameArray.includes(index > 1 ? data.requestAssociateName.toLowerCase().slice(0,index+2):data.requestAssociateName.toLowerCase());
      


      console.log(searchTerms.requestAssociateName);
      console.log(data.requestAssociateName.toLowerCase());
      return productIdFlag
      //data.productId.toLowerCase().indexOf(searchTerms.productId.toLowerCase()) !== -1
        && productSpecCodeFlag  
      //&& data.productSpecCode.toLowerCase().indexOf(searchTerms.productSpecCode.toLowerCase()) !== -1
        && productionLotFlag
      //&& data.productionLot.toLowerCase().indexOf(searchTerms.productionLot.toLowerCase()) !== -1
        //&& data.letSystemName.toLowerCase().indexOf(searchTerms.letSystemName.toLowerCase()) !== -1
        && systemNameFlag
        //&& (searchTerms.letSystemName.toLowerCase() === 'all' || searchTerms.letSystemName.toLowerCase()==='') ? true : data.letSystemName.toLowerCase()===searchTerms.letSystemName.toLowerCase()
        && currentDcPartNumberFlag
        //&& data.currentDcPartNumber.toLowerCase().indexOf(searchTerms.currentDcPartNumber.toLowerCase()) !== -1
        && newDcPartNumberFlag
        //&& data.newDcPartNumber.toLowerCase().indexOf(searchTerms.newDcPartNumber.toLowerCase()) !== -1
        && String(data.interchangeable).toLowerCase().indexOf(searchTerms.interchangeable.toLowerCase()) !== -1
        && String(data.currentShipStatus).toLowerCase().indexOf(searchTerms.currentShipStatus.toLowerCase()) !== -1
        && String(data.newShipStatus).toLowerCase().indexOf(searchTerms.newShipStatus.toLowerCase()) !== -1
        && requestAssociateNameFlag;
        //&& data.requestAssociateName.toLowerCase().indexOf(searchTerms.requestAssociateName.toLowerCase()) !== -1; 
    }
    return filterFunction;
  }

  abbreviatedName(value:string):string{
    let index = value.indexOf(' ');

    if(index > 1){
      return value.slice(0,index+2);
    }else{
      return value;
    }
  }
}
