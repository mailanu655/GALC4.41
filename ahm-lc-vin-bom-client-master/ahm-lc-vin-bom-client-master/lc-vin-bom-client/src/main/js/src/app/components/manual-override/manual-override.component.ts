import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Title } from '@angular/platform-browser';
import { FormControl } from '@angular/forms';

import { AlertService } from 'src/app/alert/alert.service';
import { ConfirmationDialogService } from 'src/app/confirmation-dialog/confirmation-dialog.service';
import { Message } from 'src/app/models/message';
import { VinPartFilterDto } from 'src/app/models/vin-part-filter-dto';
import { VinPartDto } from 'src/app/models/vin-part-dto';
import { VinPartId } from 'src/app/models/vin-part-id';
import { FrameSpecService } from 'src/app/services/frame-spec.service';
import { ManualOverrideService } from 'src/app/services/manual-override.service';
import { UtilityService } from 'src/app/services/utility-service';
import { ChangeDcPartNumberDialogComponent } from './change-dc-part-number-dialog/change-dc-part-number-dialog.component';

@Component({
  selector: 'app-manual-override',
  templateUrl: './manual-override.component.html',
  styleUrls: ['./manual-override.component.css']
})
export class ManualOverrideComponent implements OnInit {

  public loading = false;
  public productIdManualOverride: string = '';
  public productionLotManualOverride: string = '';
  public prodSpecCodeManualOverride: string = '';
  public systemNameManualOverride: string = '';

  productIdFilterList: string[];
  productionLotFilterList: string[];
  dcPartNumberFilterList: string[];
  letSystemNameFilterList: string[];

  productId: string="";
  productionLot:string="";
  systemName:string="";
  dcPartNumber:string="";

  letSystemNameList: string[];
  productIdList: string[];
  productSpecCodeList: string[];
  productionLotList: string[];
  dcPartNumberList: string[];

  remFilter:string; 

  public vinPartFilterList: VinPartFilterDto[];
  public vinPartList: VinPartDto[];
  public vinPartListFilter: VinPartDto[] = [];
  public vinPartByProductIdList: VinPartId[];
  public message: Message = { type: null, message: null };
  selectRowData: VinPartDto = new VinPartDto();
  selectedRow: number;
  highlightedRow: Number;
  selectRow: Function;
  highlightBomPart: Function;
  displayedColumns = ['productId', 'productionLot', 'productSpecCode', 'letSystemName', 'dcPartNumber', 'shipStatus'];
  dataSource: MatTableDataSource<VinPartDto>;
  @ViewChild(MatSort) sort: MatSort;
  isChangeSuccess: boolean;
  seqNo: number;
  selectedProductId: string;

  @ViewChild('seqNumberModal') seqNumberModal: TemplateRef<any>;
  private seqNumberDialogRef: MatDialogRef<TemplateRef<any>>;


  productIdVinPartFilter = new FormControl('');
  productionLotVinPartFilter = new FormControl('');
  letSystemNameVinPartFilter = new FormControl('');
  dcPartNumberVinPartFilter = new FormControl('');
  public seqNoControl = new FormControl('');
  
  productIdFilter = new FormControl(['all']);
  productionLotFilter = new FormControl(['all']);
  productSpecCodeFilter = new FormControl(['all']);
  letSystemNameFilter = new FormControl(['all']);
  dcPartNumberFilter = new FormControl(['all']);
  shipStatusFilter = new FormControl('');

  tempProductId: any = [];
  tempProdLot: any = [];
  tempProdSpecCode: any = [];
  tempSystemName: any = [];  
  tempDcPartNumber: any = [];

  filterValues = {
    productId: ['all'],
    productionLot: ['all'],
    productSpecCode: ['all'],
    letSystemName: ['all'],
    dcPartNumber: ['all'],
    shipStatus: ''
  };

  messageOptionsAutoClose = {
    autoClose: false,
    keepAfterRouteChange: false
  };

  messageOptions = {
    autoClose: false,
    keepAfterRouteChange: false
  };
  productList: any;


  constructor(
      public dialog: MatDialog,  
      public cdk: OverlayContainer, 
      protected alertService: AlertService,
      private titleService: Title,
      private util: UtilityService,
      public manualOverrideService: ManualOverrideService,
      public frameSpecService: FrameSpecService,
      private confirmationDialogService: ConfirmationDialogService
  ) { 

    this.titleService.setTitle("SUMS-VIN BOM Manual Override");

    this.selectRow = function(index, row){
      this.alertService.clear();
      this.selectedRow = index;
      this.selectRowData = row;
    }
    
    
    this.highlightBomPart = function(index){
      this.highlightedRow = index;
    }

  }

  clearSelection():void {
    this.selectRow(-1,null);
  }

  selectionSystemNameChange(value) {
    console.log('dc systemname value --', value);
    this.letSystemNameFilter.setValue(value);
  }

  selectionDcPartNumberChange(value) {
    console.log('dc part number value --', value);
    this.dcPartNumberFilter.setValue(value);
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


  ngOnInit(): void {
    this.searchAll();
    
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
    //   }
    // )

    this.dcPartNumberFilter.valueChanges
    .subscribe(
      dcPartNumber => {
        this.clearSelection();
        
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
    //     this.clearSelection();
    //     this.filterValues.dcPartNumber = dcPartNumber;
    //     this.dataSource.filter = JSON.stringify(this.filterValues);
    //   }
    // )
    this.shipStatusFilter.valueChanges
    .subscribe(
      shipStatus => {
        this.clearSelection();
        this.filterValues.shipStatus = shipStatus;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )

  }

  search(): void {
    this.clearSelection();

    console.log('inside the search of manual override screen');

    if(undefined!=this.dataSource && this.dataSource ){
      //this.dataSource.filter=JSON.stringify(newfilterValues);
      this.dcPartNumberFilter.setValue(['all']);
      this.productIdFilter = new FormControl(['all']);
        this.productSpecCodeFilter = new FormControl(['all']);
        this.letSystemNameFilter = new FormControl(['all']);
        this.productionLotFilter = new FormControl(['all']);
        
        }
    if(this.shipStatusFilter.value){
      this.shipStatusFilter.setValue('');
    }
    if((this.productId === undefined || this.productId.trim().length == 0|| this.productId.trim().length < 11)&&(this.productionLot === undefined || this.productionLot.trim().length == 0)) {
      
      alert('Please Filter Data by Product Id (atleast 11 characters) or Production Lot to continue... ')
      return;
    }else{
      this.loadData();
    }
   
  }
  loadData(){
    this.loading = true;

    this.manualOverrideService.getFilteredVinParts(this.productId.toUpperCase(),this.systemName.toUpperCase(),this.dcPartNumber.toUpperCase(),this.productionLot.toUpperCase())
      .subscribe((vinPartList: VinPartDto[]) => {
        //this.vinPartList = vinPartList;
        var replacedNull = JSON.stringify(vinPartList).replace(/null/g, '""');
        var newArray = JSON.parse(replacedNull);
        this.vinPartList = newArray;
     
        this.letSystemNameList = (Array.from(new Set(this.vinPartList.map(ele=>ele.letSystemName)))).sort();
        this.productIdList = (Array.from(new Set(this.vinPartList.map(ele=>ele.productId)))).sort();
        this.productionLotList = (Array.from(new Set(this.vinPartList.map(ele=>ele.productionLot)))).sort();
        this.productSpecCodeList = (Array.from(new Set(this.vinPartList.map(ele=>ele.productSpecCode)))).sort();
        this.dcPartNumberList = (Array.from(new Set(this.vinPartList.map(ele=>ele.dcPartNumber)))).sort();
        
        //'productId', 'productionLot', 'productSpecCode', 'letSystemName', 'dcPartNumber', 'shipStatus
//NALC-1093
        let sortedList = this.vinPartList.sort((a,b) => (a.dcPartNumber < b.dcPartNumber) ? -1 : 1).
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
        this.loading = false;
      });
  }

  searchAll() {
    this.clearSelection();
    this.loading = true;

    this.manualOverrideService.getVinPartFilters()
      .subscribe((vinPartFilterList: VinPartFilterDto[]) => {
        //this.vinPartList = vinPartList;
        var replacedNull = JSON.stringify(vinPartFilterList).replace(/null/g, '""');
        var newArray = JSON.parse(replacedNull);
        this.vinPartFilterList = newArray;
        this.productIdFilterList = (Array.from(new Set(this.vinPartFilterList.map(ele=>ele.productId)))).sort();
        this.productionLotFilterList = (Array.from(new Set(this.vinPartFilterList.map(ele=>ele.productionLot)))).sort();
        this.dcPartNumberFilterList = (Array.from(new Set(this.vinPartFilterList.map(ele=>ele.dcPartNumber)))).sort();
        this.letSystemNameFilterList = (Array.from(new Set(this.vinPartFilterList.map(ele=>ele.letSystemName)))).sort();
        
        this.loading = false;
      },(error: any) => {
        this.loading = false;
      });
  }

  changeShippableStatus() {
    if(this.selectRowData === null) {
      alert("Please select data ");
      return;
    }
    if(this.selectRowData.letSystemName === undefined || this.selectRowData.letSystemName == null || this.selectRowData.letSystemName == '') {
      alert("Please select data");
    } else {
      this.alertService.clear();
       console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);
    
      this.manualOverrideService.changeShippableStatus(this.selectRowData).subscribe(data => {
        console.log(data);
        this.searchAll(); 
        this.alertService.success('Change of Status has been sent for Approval', this.messageOptionsAutoClose);
      },(error: any) => {
        alert("Error changing status");
      });
    }
  }

  changeDcPartNumberDialog() {
    if(this.selectRowData === null) {
      alert("Please select data ");
      return;
    }
    console.log("filterValues - "+JSON.stringify(this.filterValues));
    this.remFilter = JSON.stringify(this.filterValues);

    if(this.selectRowData && this.selectRowData.letSystemName) {
      const dialogRef = this.dialog.open(ChangeDcPartNumberDialogComponent, {
        width: "60%",
        height: '60%',
        data: { isChangeSuccess: false, selectRowData: this.selectRowData },
        id: "parent",
        disableClose: true
      });

      dialogRef.afterClosed().subscribe(result => {
        console.log("The dialog was closed");
        this.isChangeSuccess = result.isChangeSuccess;
        console.log("Changed Part: " + this.isChangeSuccess);
        if(this.isChangeSuccess) {
          this.alertService.success('Success!!', this.messageOptionsAutoClose);
          this.searchAll();
        }
        this.dialog.closeAll();
      });
    } else {
      alert("Please select a part");
    }

  }

  async delete(){
    if(this.selectRowData && this.selectRowData.productId) {
     
        console.log("filterValues - "+JSON.stringify(this.filterValues));
        this.remFilter = JSON.stringify(this.filterValues);

        this.manualOverrideService.findDistinctPartNumberBySystemNameAndProductId(this.selectRowData.letSystemName,this.selectRowData.productId)
        .subscribe((vinPartByProductIdList: VinPartId[]) => {
          this.vinPartByProductIdList = vinPartByProductIdList;
          console.log(vinPartByProductIdList);   
          if(this.vinPartByProductIdList.length > 1){
            this.confirmationDialogService.confirm('', 'Are you sure you want to delete part? ', 'Yes', 'No', 'lg')
            .then((confirmed) => {
              if(confirmed) {
                  this.manualOverrideService.delete(this.selectRowData).subscribe(data => {
                    console.log(data);
                    this.searchAll(); 
                    this.alertService.success('Successfully Removed', this.messageOptionsAutoClose);
                  },(error: any) => {
                    alert("Error Deleting");
                  });
              }
            });
          }else{
            alert("Atleast one record for product and System required - cannot delete");
          }
        },(error: any) => {
          
        });
        
  }else {
    alert("Please select a part");
  }
  }
//['productId', 'productionLot', 'productSpecCode', 'letSystemName', 'dcPartNumber', 'shipStatus'];
  createFilter(): (data: any, filter: string) => boolean {
    let filterFunction = function(data, filter): boolean {
      let searchTerms = JSON.parse(filter);
      let systemNameArray = searchTerms.letSystemName as Array<String>;
      let dcPartNumberArray = searchTerms.dcPartNumber as Array<String>;
      let productIdArray = searchTerms.productId as Array<String>;
        let productionLotArray = searchTerms.productionLot as Array<String>;
        let productSpecCodeArray = searchTerms.productSpecCode as Array<String>; 


        let systemNameFlag=(systemNameArray.includes('all') ) ? true : systemNameArray.includes(data.letSystemName.toLowerCase());
	  let dcPartNumberFlag=(dcPartNumberArray.includes('all') ) ? true : dcPartNumberArray.includes(data.dcPartNumber.toLowerCase());
	  let productIdFlag=(productIdArray.includes('all') ) ? true : productIdArray.includes(data.productId.toLowerCase());
	  let productionLotFlag=(productionLotArray.includes('all') ) ? true : productionLotArray.includes(data.productionLot.toLowerCase());
	  let productSpecCodeFlag=(productSpecCodeArray.includes('all') ) ? true : productSpecCodeArray.includes(data.productSpecCode.toLowerCase());
	  


      return productIdFlag
      //data.productId.toLowerCase().indexOf(searchTerms.productId.toLowerCase()) !== -1
      && productionLotFlag  
      //&& data.productionLot.toLowerCase().indexOf(searchTerms.productionLot.toLowerCase()) !== -1
      && productSpecCodeFlag
      //&& data.productSpecCode.toLowerCase().indexOf(searchTerms.productSpecCode.toLowerCase()) !== -1
      && dcPartNumberFlag  
      //&& data.dcPartNumber.toLowerCase().indexOf(searchTerms.dcPartNumber.toLowerCase()) !== -1
        //&& data.letSystemName.toLowerCase().indexOf(searchTerms.letSystemName.toLowerCase()) !== -1
      && systemNameFlag
        //&& (searchTerms.letSystemName.toLowerCase() === 'all' || searchTerms.letSystemName.toLowerCase()==='') ? true : data.letSystemName.toLowerCase()===searchTerms.letSystemName.toLowerCase()
      && String(data.shipStatus).toLowerCase().indexOf(searchTerms.shipStatus.toLowerCase()) !== -1
    }
    return filterFunction;
  }

  searchProdIdFromSeqDialog(): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width='800px'
    dialogConfig.height='flex'
    dialogConfig.role = 'dialog';

    this.seqNumberDialogRef = this.dialog.open(this.seqNumberModal, dialogConfig);

    this.seqNumberDialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed...');
    });
  }

  onSeqSearchSubmit(){
    console.log('sequence number submitted --',this.seqNo);
    
    this.manualOverrideService.getProductDetails(this.seqNo).subscribe(data => {
      console.log('product details -- ',data);

      this.productList=data.productFormatData;
      //this.searchAll(); 
      //this.alertService.success('Change of Status has been sent for Approval', this.messageOptionsAutoClose);
    },(error: any) => {
      alert("Error fetching product details from the seqno");
    });
  }

  closeDialog(prodId: string){
    console.log('selectedProductId -- , ',this.selectedProductId);
    console.log('prodId-- ',prodId);
    this.productId=prodId;
    this.productList=[];
    this.seqNo=null; 
this.dialog.closeAll();

  }

}
