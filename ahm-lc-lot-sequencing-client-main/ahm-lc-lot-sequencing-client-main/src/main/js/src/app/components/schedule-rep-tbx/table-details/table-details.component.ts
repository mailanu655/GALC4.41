import { Component, EventEmitter, Input, Output, SimpleChange } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SpecFormat } from 'src/app/models/spec-format';
import { GlobalServiceService } from 'src/app/services/global-service.service';
import { ConfirmationPopupComponent } from '../../confirmation-popup/confirmation-popup.component';
import { ProductType } from '../../../constants/common.constants';
import { SpecMaskSelectComponent } from '../../spec-mask-select/spec-mask-select.component';
import { User, UserColumns } from './data-interface';
import { lastValueFrom } from 'rxjs';
import { ClientModel } from 'src/app/models/client.model';
import { SubAssyProdComponent } from '../../sub-assy-prod/sub-assy-prod.component';
import { LogService } from 'src/app/services/log.service';

@Component({
  selector: 'app-table-details',
  templateUrl: './table-details.component.html',
  styleUrls: ['./table-details.component.css']
})
export class TableDetailsComponent {
  @Input() public dataSource;
  @Input() public showMessage;
  @Input() public message;
  @Output() resetFilter = new EventEmitter<any>();
  @Output() getRefreshData = new EventEmitter<any>();
  @Output() displayMessage = new EventEmitter<any>();
  
  displayedColumns: string[] = UserColumns.map((col) => col.key)
  columnsSchema: any = UserColumns
  valid: any = {};
  selectedModelOptionCode = 'All';
  selectedIntColorCode = 'All';
  isEdit: any;
  data: any;

  constructor(
    private logService: LogService,
    public dataLoadService: GlobalServiceService,
    public dialog: MatDialog,
    public model: ClientModel,
  ) {
  }

  editRow(row: User) {
    this.logService.gui('edit row', [{ method: 'editRow' }]);

    if (row.id === 0) {
      this.addEditData(row, 'add');
    } else {
      this.addEditData(row);
    }
  }

  cancel = (element: any) => {
    this.logService.gui('cancel', [{ method: 'cancel' }]);

    if (element.id === 0) {
      let temp = [...this.dataSource.data];
      temp.splice(0, 1);
      this.dataSource.data = [...temp];
    } else {
      element.isEdit = false;
    }
  }

  addRow() {
    const newRow: User = {
      id: 0,
      sourceProcLoc: '',
      destProcLoc: '',
      destSpecCode: '',
      subAssyProdIdFormat: '',
      prodDateOffset: '',
      isEdit: true
    }
    this.dataSource.data = [newRow, ...this.dataSource.data]
    this.logService.gui('add row', [{ method: 'addRow' }]);
  }

  inputHandler(e: any, id: number, key: string) {
    if (!this.valid[id]) {
      this.valid[id] = {}
    }
   this.valid[id][key] = e.target.validity.valid
   
  }

  disableSubmit(id: number) {
    if (this.valid[id]) {
       return Object.values(this.valid[id]).some((item) => item === false)
    }
    return false;
   }

  removeRow(row: any) {
    this.deleteRow(row);
    this.logService.gui('remove row', [{ method: 'removeRow' }]);
  }

  addEditData = (data: any, type?: any) => {
    if (type == 'add') {
      this.logService.gui('add data', [{ method: 'addEditData' }]);
      this.openDialog(`Confirm Add?`, { ok: 'Ok', cancel: 'Cancel' }, 'add', data);
    } else {
      this.logService.gui('edit data', [{ method: 'addEditData' }]);
      this.openDialog(`Confirm Edit?`, { ok: 'Ok', cancel: 'Cancel' }, 'edit', data);
    }
  }

  openDialog(msg: string, actions: any, confirmData: any, row?: any) {
    const dialogRef = this.dialog.open(ConfirmationPopupComponent, {
      data: {
        message: msg,
        buttonText: actions,
        confirmData: confirmData
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: any) => {
      let data: any = {
        'com.honda.galc.entity.product.ScheduleReplication': {
          id: {
            "sourceProcLoc": row?.sourceProcLoc,
            "destProcLoc": row?.destProcLoc,
            "destSpecCode": row?.destSpecCode,
          },
          "subAssyProdIdFormat": row?.subAssyProdIdFormat.toString(),
          "prodDateOffset": row?.prodDateOffset
        }
      }
      if (confirmed == 'delete') {
        this.dataLoadService.removeData(data).subscribe(
          (result) => {
            this.dataSource.data = this.dataSource.data.filter(
              (u: User) => u.id !== row.id,
            )
            this.resetFilter.emit();
            this.displayMessage.emit('Deleted Sucessfully');
          });
      } else if (confirmed == 'edit') {
        this.addRecord(data, row, confirmed);
      } else if (confirmed == 'add') {
        this.checkRecord(data, row, confirmed);
      }
    });
  }

  checkRecord = (data: any, row: any, confirmed: any) => {
    this.dataLoadService.getIsDuplicateData(row?.sourceProcLoc, row?.destProcLoc, row?.destSpecCode).subscribe(
      (result) => {
        if (result) {
          let msg: any = 'The SOURCE_PROC_LOC, DEST_PROC_LOC, and DEST_SPEC_CODE fields are not unique in the table'
          this.openDialog(msg, { ok: 'Ok' }, 'check');
        } else {
          this.addRecord(data, row, confirmed);
        }
      });
  }

  addRecord = (data: any, row: any, confirmed: any) => {
    this.dataLoadService.addEditData(data).subscribe(
      (result) => {
        this.resetFilter.emit();
        row.isEdit = false;
        if (confirmed == 'add') {
          row.id = this.dataSource.data.length + 1
          row.isEdit = false;
          row.status = 'add';
              this.getRefreshData.emit(row);
        } else if (confirmed == 'edit') {
          row.status = 'edit'
        }
         this.displayMessage.emit(confirmed == 'edit' ? 'Updated Sucessfully' : 'Added Sucessfully')
      });
  }

  deleteRow = (row: any) => {
    this.logService.gui('delete row', [{ method: 'deleteRow' }]);
    if (row) {
      this.openDialog(`Confirm Delete?`, { ok: 'Ok', cancel: 'Cancel' }, 'delete', row);
    }
  }

  
  async setSpecMask(element: any) {
    this.logService.gui('set spec mask', [{ method: 'setSpecMask' }]);

    let specMask = await this.displaySelectSpecMaskDialog(element);
    if (specMask) {
      element['destSpecCode'] = specMask as string;
    }
   
  }

  async displaySelectSpecMaskDialog(element: any) {
    this.logService.gui('display select spec mask dialog', [{ method: 'displaySelectSpecMaskDialog' }]);

    let productType = ProductType.MBPN;
    let dialogRef = null;
    let dialogConfig: any = {
      disableClose: true,
      data: {
        mask: element['destSpecCode'],
        specFormat: SpecFormat.get(productType!),
        specs: this.model.specIx.get(productType!),
      },
    };
    dialogRef = this.dialog.open(SpecMaskSelectComponent, dialogConfig);
    const result$ = dialogRef.afterClosed();
    let result = await lastValueFrom(result$);
    return result;
  }


  openSubDialog(element: any) {
    this.logService.gui('open sub dialog', [{ method: 'openSubDialog' }]);
    
    if (element['subAssyProdIdFormat'] &&  typeof element['subAssyProdIdFormat'] !== 'object') {
      element['subAssyProdIdFormat'] = element['subAssyProdIdFormat'].split(',');
    }
    let dialogConfig: any = {
      data: {
        mask: element['subAssyProdIdFormat'] 
      }
    }
    const dialogRef = this.dialog.open(SubAssyProdComponent, dialogConfig);
    dialogRef.afterClosed().subscribe((data: any) => {
      if (data) {
        element['subAssyProdIdFormat'] = String(data)
      }
    });
  }

}
