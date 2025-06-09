import { OverlayContainer } from '@angular/cdk/overlay';
import { Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { VinBomPart } from 'src/app/models/vin-bom-part';
import { VinPartDto } from 'src/app/models/vin-part-dto';
import { VinPartApproval } from 'src/app/models/vin-part-approval';
import { ManualOverrideService } from 'src/app/services/manual-override.service';

export interface DialogData {
  isChangeSuccess: boolean;
  selectRowData: VinPartDto;
}

@Component({
  selector: 'app-change-dc-part-number-dialog',
  templateUrl: './change-dc-part-number-dialog.component.html',
  styleUrls: ['./change-dc-part-number-dialog.component.css']
})
export class ChangeDcPartNumberDialogComponent implements OnInit {

  productIdManualOverrideDlg: string;
  systemNameManualOverrideDlg: string;
  dcPartNoManOverrideDialog: string;
  newDcPartNo: string
  isInterchangeable: boolean;
  isManualEntry: boolean;
  currentDcPartNoManualOverrideDlg: string;
  public vinPartList: VinBomPart[];
  selectedRow: number;
  highlightedRow: Number;
  selectRow: Function;
  highlightRow: Function;
  originalVinPart: VinPartDto = new VinPartDto();
  selectedRowData: VinBomPart = new VinBomPart();
  vinPartApproval: VinPartApproval = new VinPartApproval();
  highlightBomPart: Function;
  displayedColumns = ['dcPartNumber', 'effectiveBeginDate', 'effectiveEndDate'];
  dataSource: MatTableDataSource<VinBomPart>;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    public cdk: OverlayContainer,
    private elementRef: ElementRef,
    public dialogRef: MatDialogRef<ChangeDcPartNumberDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public manualOverrideService: ManualOverrideService
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

    this.originalVinPart = this.data.selectRowData;
    this.productIdManualOverrideDlg = this.originalVinPart.productId;
    this.systemNameManualOverrideDlg = this.originalVinPart.letSystemName;
    this.currentDcPartNoManualOverrideDlg = this.originalVinPart.dcPartNumber
    this.populateData();
    
  }

  populateData(): void {
    this.manualOverrideService.findDistinctPartNumberBySystemName(this.originalVinPart.letSystemName, this.originalVinPart.productId)
      .subscribe((vinPartList: VinBomPart[]) => {
        this.vinPartList = vinPartList;
        this.dataSource = new MatTableDataSource(vinPartList);
        this.dataSource.sort = this.sort;
        
      },(error: any) => {
        
      });
  }

  toTop(id) {
    this.cdk.getContainerElement().childNodes.forEach((x: any) => {
      if (x.innerHTML.indexOf('id="' + id + '"') <= 0)
        x.style["z-index"] = 1000;
      else x.style["z-index"] = 1001;
    });
  }

  cancel(): void {
    this.dialogRef.close({ isChangeSuccess: this.data.isChangeSuccess });
  }

  save(): void {
    this.newDcPartNo = "";
    if(this.isManualEntry != undefined && this.isManualEntry == true 
      && (this.dcPartNoManOverrideDialog === undefined || this.dcPartNoManOverrideDialog.trim().length < 1 )) {
        alert("You have checked Manual Entry. \n Please enter correct DC Part Number.");
        return;
    } else {
      this.newDcPartNo = this.dcPartNoManOverrideDialog;
    }
    if(this.newDcPartNo === undefined || this.newDcPartNo == '') {
      if(this.selectedRowData.id.dcPartNumber === undefined || this.selectedRowData.id.dcPartNumber.trim().length < 1 ) {
        alert('Please select a new DC Part Number or enter manually');
        return;
      } else {
        this.newDcPartNo = this.selectedRowData.id.dcPartNumber;
      }
    }
    if(this.newDcPartNo === undefined || this.newDcPartNo.trim().length < 1 ) {
      alert("Please select a new DC Part from the table");
    } else {
      
      if(this.isInterchangeable === undefined || this.isInterchangeable == null) {
       // this.isInterchangeable = false;
       alert("Please select Interchangeable");
      return;
      }
      console.log("interchangeable - "+this.isInterchangeable);
      this.manualOverrideService.save(this.originalVinPart, this.newDcPartNo, this.isInterchangeable).subscribe(data => {

        this.data.isChangeSuccess = true;
        this.cancel();
        
      },(error: any) => {
        this.data.isChangeSuccess = false;
        alert("Error Occured while Changing Part");
        console.log(error);
        this.cancel();
        return;
      });
    }
  }

}
