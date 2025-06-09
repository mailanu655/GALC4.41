import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { SelectionModel } from '@angular/cdk/collections';
import { FormControl } from '@angular/forms';

import { LotAssignmentService } from 'src/app/services/lot-assignment.service';
import { PreProductionLot } from 'src/app/models/pre-production-lot';
import { ModelPartApproval } from 'src/app/models/model-part-approval';
import { StartingProdLotService } from 'src/app/services/starting-prod-lot.service';
import { ConfirmationDialogService } from 'src/app/confirmation-dialog/confirmation-dialog.service';
import { ModelPartLotDto } from 'src/app/models/model-part-lot-dto';
import { ConfigService } from 'src/app/services/config.service';
import { forkJoin } from 'rxjs';
import { DatePipe } from '@angular/common';

export interface DialogData {
  selectedFromDate: string;
  selectedLine: string;
  selectedProdLot: string;
  planCode: string;
  doSave: boolean;
  selectedParentData: ModelPartLotDto[];
  isSuccess: boolean;
}

@Component({
  selector: 'app-select-prod-lot-dialog',
  templateUrl: './select-prod-lot-dialog.component.html',
  styleUrls: ['./select-prod-lot-dialog.component.css']
})
export class SelectProdLotDialogComponent implements OnInit {

  public fromDate: string = "";
  selecteFromDate: string = "";
  public lineList: string[];
  public preProductionLotList: PreProductionLot[];
  public line: string = "";
  public prodSpecCode: string;
  planCode: string;
  displayedColumns = ['productionLot', 'productSpecCode'];
  selectedProdLot: string;
  selectedRow: number;
  selectedRowData: PreProductionLot = new PreProductionLot();
  highlightedRow: Number;
  selectRow: Function;
  highlightRow: Function;
  isImmediateChange: boolean;
  productSpecCodeFilter = new FormControl('');
  doSave: boolean;
  enableSave:boolean=true;

  filterValues = {
    productSpecCode: ''
  };

  dataSource: MatTableDataSource<PreProductionLot>;
  selection = new SelectionModel<PreProductionLot>(true, []);
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    public cdk: OverlayContainer,
    private elementRef: ElementRef,
    private datePipe: DatePipe,
    public dialogRef: MatDialogRef<SelectProdLotDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private confirmationDialogService: ConfirmationDialogService,
    public lotAssignmentService: LotAssignmentService,
    public startingProdLotService: StartingProdLotService,
    public configService:ConfigService
  ) {

    
    this.selectRow = function(index, row){
      this.selectedRow = index;
      this.selectedRowData = row;      
    }

    this.highlightRow = function(index){
      this.highlightedRow = index;
    }

   }

  ngOnInit(): void {
    console.log(this.data.doSave);
    this.doSave = this.data.doSave;
    this.getLines();
    //this.getProductionLotsByProcessLocation();

    this.productSpecCodeFilter.valueChanges
    .subscribe(
      productSpecCode => {
        console.log("productSpec- "+productSpecCode);
        this.filterValues.productSpecCode = productSpecCode;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
  }


  getLines() {
    this.lotAssignmentService.getLines(this.configService.sites[this.configService.siteId]?.gpcsplantName)
    .subscribe((lineList: string[]) => {
      this.lineList = lineList;
    },(error: any) => {
      
    });
  }

  search() {
    console.log(this.line +'----'+ this.selecteFromDate);
    if(this.isImmediateChange != true){
      if(this.line.length === 0 && this.selecteFromDate.length === 0){
        alert("Please Select From Date and Line");
      }else if(this.line.length === 0){
        alert("Please Select Line");
        return;
      } else if(this.selecteFromDate.length === 0){
        alert("Please Select From Date");
        return;
      }
    }
    this.getProductionLotsByProcessLocation();
  }

  getProductionLotsByProcessLocation() {
    let startDate = this.datePipe.transform(this.selecteFromDate, "yyyy-MM-dd");
    this.startingProdLotService.getProductionLotsByProcessLocation('AF', startDate, this.line)
    .subscribe((preProductionLotList: PreProductionLot[]) => {
      this.preProductionLotList = preProductionLotList;
      
      this.dataSource = new MatTableDataSource(this.preProductionLotList);
      this.dataSource.sort = this.sort;
      this.dataSource.filterPredicate = this.createFilter();
      this.productSpecCodeFilter.setValue('');
      this.selectRow(-1,null);
    },(error: any) => {
      
    });
  }

  createFilter(): (data: any, filter: string) => boolean {
    let filterFunction = function(data, filter): boolean {
      let searchTerms = JSON.parse(filter);
      //console.log("productSpecfilter- "+searchTerms.productSpecCode);
      return data.productSpecCode.toLowerCase().indexOf(searchTerms.productSpecCode.toLowerCase()) !== -1;
    }
    return filterFunction;
  }

  toTop(id) {
    this.cdk.getContainerElement().childNodes.forEach((x: any) => {
      if (x.innerHTML.indexOf('id="' + id + '"') <= 0)
        x.style["z-index"] = 1000;
      else x.style["z-index"] = 1001;
    });
  }

  ok(): void {
    if(this.isImmediateChange === undefined || this.isImmediateChange == false) {
      if(this.selectedRowData === undefined || this.selectedRowData.productionLot === undefined || this.selectedRowData.productionLot.trim().length < 1 ) {
        alert("Please Select a Production Lot \n or \n select Immediate Change");
        return;
      }
    }

    if(this.line === undefined || this.line === "" ) {
      alert("Please select Line");
      return;
    }

    if(this.isImmediateChange == true) {
      this.selectedProdLot = "IMMEDIATE";
      this.planCode = this.line.toString().substring(1,2);
    } else {
      this.selectedProdLot = this.selectedRowData.productionLot;
      this.planCode = this.selectedRowData.planCode;
    }
    this.data.selectedProdLot = this.selectedProdLot;
    this.data.selectedLine = this.line;
    this.data.selectedFromDate = this.fromDate;
    this.data.planCode = this.planCode;

    this.dialogRef.close(this.data);
  }

  save() {
    let validFlag:boolean=true;
    if(this.isImmediateChange === undefined || this.isImmediateChange == false) {
      if(this.selectedRowData === undefined || this.selectedRowData.productionLot === undefined || this.selectedRowData.productionLot.trim().length < 1 ) {
        alert("Please Select a Production Lot \n or \n select Immediate Change");
        return;
      }
    }
    console.log('immediate chnage -- ',this.isImmediateChange);
    if(this.isImmediateChange != true) {
      if(this.selecteFromDate === undefined || this.selecteFromDate.trim().length < 1  ) {
        alert("Please select From Date");
        return;
      }
    }
    if(this.line === undefined || this.line === "" ) {
      alert("Please select Line");
      return;
    }

    if(this.isImmediateChange == true) {
      this.selectedProdLot = "IMMEDIATE";
      this.planCode = this.line.toString().substring(1,2);
    } else {
      this.selectedProdLot = this.selectedRowData.productionLot;
      this.planCode = this.selectedRowData.planCode;
    }
  
    if(validFlag){
console.log('inside the save approval');
      this.saveMultiModelPartApproval();
    }
   

  }

  saveMultiModelPartApproval(){
    this.enableSave=false;
    console.log("selected Parent Data-"+JSON.stringify(this.data.selectedParentData));
    this.lotAssignmentService.saveMultiModelPartApprovals(this.data.selectedParentData,this.planCode,this.selectedProdLot).subscribe(
      data => {
        console.log('saved...');
        console.log('saved data --  ',this.data, data);
        this.data.isSuccess = true;
        this.dialogRef.close(this.data);
        this.enableSave=true;
      },
      (error: any) => {
        alert('save:: Error');
        this.data.isSuccess = false;
        this.dialogRef.close(this.data);
      }
    );
  }
 /* saveModelPartApproval() {
        
    forkJoin(
      this.data.selectedParentData.map(s => this.lotAssignmentService.saveModelPartApproval(s, this.planCode,this.selectedProdLot, s.interchangeable, s.scrapParts, s.reflash))
    ).subscribe(
      data => {
        console.log('saved...');
        this.data.isSuccess = true;
        this.dialogRef.close(this.data);
      },
      (error: any) => {
        alert('save:: Error');
        this.data.isSuccess = false;
        this.dialogRef.close(this.data);
      }
    );
  }*/

  cancel(): void {
    this.dialogRef.close({ });
  }

  getDate(event: any) {
    const data = event;
    this.selecteFromDate = data.toLocaleDateString();
    //const formattedDate = data.getDate() + '-' + (data.getMonth() + 1) + '-' + data.getFullYear();
  }

}
