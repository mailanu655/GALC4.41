import { Component, OnInit, Inject } from '@angular/core';
import { WeldScheduleModel } from 'src/app/models';
import {
  MAT_DIALOG_DATA,
  MatDialog,
  MatDialogRef,
} from '@angular/material/dialog';
import { LogService, LotsService } from 'src/app/services';
import { getDateString } from 'src/app/utils';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SpecFormat } from 'src/app/models/spec-format';
import { ClientModel } from 'src/app/models/client.model';
import { SpecMaskSelectComponent } from '../spec-mask-select/spec-mask-select.component';
import { lastValueFrom } from 'rxjs';
import { ProductType } from 'src/app/constants';

@Component({
  selector: 'row-form-dialog',
  templateUrl: './row-form-dialog.component.html',
  styleUrls: ['./row-form-dialog.component.css'],
})
export class RowFormDialogComponent implements OnInit {
  
  commentsOptions: string[] = ['Remakes', 'New Model', 'Reschedule', 'Frozen schedule'];
  isEdit: boolean = true;
  selectedProdDate: Date;

  modelYearCodes: string[] = [];
  modelCodes: string[] = [];
  modelTypeCodes: string[] = [];
  modelOptionCodes: string[] = [];
  extColorCodes: string[] = [];
  intColorCodes: string[] = [];

  selectedModelYearCode = 'All';
  selectedModelCode = 'All';
  selectedModelTypeCode = 'All';
  selectedModelOptionCode = 'All';
  selectedExtColorCode = 'All';
  selectedIntColorCode = 'All';

  isSavingRow = false;

  originalData = { ...this.data };

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: WeldScheduleModel,
    public dialogRef: MatDialogRef<RowFormDialogComponent>,
    private lotsService: LotsService,
    public snackBar: MatSnackBar,
    private logService: LogService,
    public model: ClientModel,
    public dialog: MatDialog
  ) {
    if (data) {
      this.isEdit = data.isEdit;
      this.commentsOptions = data.commentsOptions;
    }
  }

  ngOnInit(): void {
    this.modelYearCodes = this.lotsService.getYearData();
    this.modelCodes =
      this.lotsService.getAllProductData('modelCode').modelCodes;
    this.modelTypeCodes =
      this.lotsService.getAllProductData('modelTypeCode').modelTypeCodes;
    this.modelOptionCodes =
      this.lotsService.getAllProductData('modelOptionCode').modelOptionCodes;
    this.extColorCodes =
      this.lotsService.getAllProductData('extColorCode').extColorCodes;
    this.intColorCodes =
      this.lotsService.getAllProductData('intColorCode').intColorCodes;

      this.data.lotSize = this.data.lotSize || 30;

    if (this.data.productionLot) {
      const productionLot = this.data.productionLot;
      let firstNumberIndex = productionLot.search(/\d/);
      const mm = productionLot.slice(firstNumberIndex + 4, firstNumberIndex + 6);
      const dd = productionLot.slice(firstNumberIndex + 6, firstNumberIndex + 8);
      const yyyy = productionLot.slice(firstNumberIndex, firstNumberIndex + 4);
      const dateStr = `${mm}/${dd}/${yyyy}`;
      
      this.selectedProdDate = new Date(dateStr);
    } 
    
    if (!this.isEdit) {
      const today = new Date();
      const yyyy = today.getFullYear();
      let mm = today.getMonth() + 1; // Months start at 0!
      let dd = today.getDate();
      const dateStr = `${mm}/${dd}/${yyyy}`;
      
      this.selectedProdDate = new Date(dateStr);
    }

    if (this.data.productSpecCode) {
      const productSpecCode = this.data.productSpecCode;

      this.selectedModelYearCode = productSpecCode[0];
      this.selectedModelCode = productSpecCode.slice(1, 4);
      this.selectedModelTypeCode = productSpecCode.slice(4, 7);
      this.selectedModelOptionCode = productSpecCode.slice(7, 10);
      this.selectedExtColorCode = productSpecCode.slice(10, 20);
      this.selectedIntColorCode = productSpecCode.slice(20, 22);
    }
  }

  setComments(event: string) {
    this.data.comments = event;
  }

  onSelectProdDate(event) {
    const prodDateStr = getDateString(event.value);
    const productionLotStr = this.data.productionLot;
    const pre = productionLotStr.slice(0, 8);
    const rear = productionLotStr.slice(16);
    const newProductionLotStr = pre + prodDateStr + rear;

    this.data.productionDate = prodDateStr.slice(0, 4) + '-' + prodDateStr.slice(4, 6) + '-' + prodDateStr.slice(6) + 'T00:00:00.000Z';
    this.data.productionLot = newProductionLotStr;
  }

  onSelectModelYearCode(): void {
    this.modelCodes = this.lotsService.getModelData(this.selectedModelYearCode);
  }

  onSelectModelCode(): void {
    this.modelTypeCodes = this.lotsService.getModelTypeData(
      this.selectedModelYearCode,
      this.selectedModelCode
    );
  }

  onSelectModelTypeCode(): void {
    this.modelOptionCodes = this.lotsService.getOptionData(
      this.selectedModelYearCode,
      this.selectedModelCode,
      this.selectedModelTypeCode
    );
  }

  onSelectModelOptionCode(): void {
    this.extColorCodes = this.lotsService.getExtColorData(
      this.selectedModelYearCode,
      this.selectedModelCode,
      this.selectedModelTypeCode,
      this.selectedModelOptionCode
    );
  }

  onSelectExtColorCode(): void {
    this.intColorCodes = this.lotsService.getIntColorCodeData(
      this.selectedModelYearCode,
      this.selectedModelCode,
      this.selectedModelTypeCode,
      this.selectedModelOptionCode,
      this.selectedExtColorCode
    );
  }

  onSelectIntColorCode(): void {}

  onNoClick(): void {
    this.data = { ...this.originalData };
    this.dialogRef.close(undefined);
  }

  onSaveClick(): void {
    if (!this.data.productSpecCode) return;

    const startTime: any = new Date();

    this.isSavingRow = true;

    this.isEdit = this.data.isEdit;

    this.lotsService.postLotSave(this.data).subscribe({
      next: (result) => {
        this.snackBar.open(
          this.isEdit
            ? 'The row was updated successfully!'
            : 'The new row has been added',
          'ok',
          {
            duration: 5000,
          }
        );
        this.dialogRef.close(this.data);
        this.logService.gui(
          this.isEdit
            ? 'Updating row successfully!'
            : 'Adding a new row successfully', [{
              method: 'onSaveClick',
            }]
        );
      },
      error: (err) => {
        const endTime: any = new Date();

        if (err?.status === 200) {
          this.snackBar.open(
            this.isEdit
              ? 'The row was updated successfully!'
              : 'The new row has been added',
            'ok',
            {
              duration: 5000,
            }
          );
          this.dialogRef.close(this.data);
          this.logService.debug(
            this.isEdit
              ? 'Updating row successfully!'
              : 'Adding a new row successfully', [{
                method: 'onSaveClick',
                duration: endTime - startTime,
              }]
          );
        } else {
          this.logService.fatal(
            this.isEdit ? 'Updating a row failed!' + err : 'Adding a new row failed' + err, [{
              method: 'onSaveClick',
              duration: endTime - startTime,
            }]
          );
          this.snackBar.open(
            this.isEdit ? 'Updating row failed!' : 'Adding a new row failed',
            'ok',
            {
              duration: 5000,
            }
          );
        }
      },
      complete: () => {
        this.isSavingRow = false;
        delete this.data.isEdit;
      },
    });
  }

  async setSpecMask() {
    try {
      let specMask = await this.displaySelectSpecMaskDialog();
      if (specMask) {
        this.data.productSpecCode = specMask as string;
        this.data.modelCode = this.data.productSpecCode.slice(1, 4);
        this.data.modelType = this.data.productSpecCode.slice(4, 7);
        this.selectedModelOptionCode = this.data.productSpecCode.slice(7, 10);
        this.data.extColor = this.data.productSpecCode.slice(10, 20);
        this.selectedIntColorCode = this.data.productSpecCode.slice(20, 22);
        this.data.isRemake = "N";
      }
    } finally {
      this.logService.gui('Set spec mask', [{
        method: 'setSpecMask'
      }])
    }
  }

  async displaySelectSpecMaskDialog() {
    let productType = ProductType.FRAME;
    let dialogRef = null;
    let dialogConfig: any = {
      disableClose: true,
      data: {
        mask: this.data.productSpecCode,
        specFormat: SpecFormat.get(productType),
        specs: this.model.specIx.get(productType),
      },
    };
    dialogRef = this.dialog.open(SpecMaskSelectComponent, dialogConfig);
    const result$ = dialogRef.afterClosed();
    let result = await lastValueFrom(result$);
    this.logService.gui('Display select spec mask dialog', [{
      method: 'displaySelectSpecMaskDialog'
    }]);
    
    return result;
  }
}
