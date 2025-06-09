import { Component, createPlatform, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { GlobalServiceService } from 'src/app/services/global-service.service';
import { User } from './table-details/data-interface';
import { ConfigService } from 'src/app/services/config.service';

@Component({
  selector: 'app-schedule-rep-tbx',
  templateUrl: './schedule-rep-tbx.component.html',
  styleUrls: ['./schedule-rep-tbx.component.css']
})
export class ScheduleRepTbxComponent implements OnInit {

  selectFilterForm: FormGroup;
  tableDetailsForm: FormGroup;
  filterOptions: any = [];
  dataSource = new MatTableDataSource<User>()
  showMessage: boolean = false;
  message: any  = '';

  constructor(
    private fb: FormBuilder,
    public dialog: MatDialog,
    public dataLoadService: GlobalServiceService,
    private config: ConfigService
  ) { }

  ngOnInit(): void {
    this.createForm();
    this.getFilterData();
  }

  getFilterData = (key?: any, data?: any) => {
    this.dataLoadService.getFilterData().subscribe(
      (result) => {
       if (result && result.length) {
         result[0].unshift('All');
         result[1].unshift('All');
         this.filterOptions = result;
         this.selectFilterForm.get('sourceProcLocation').patchValue('All');
         this.selectFilterForm.get('destProcLocation').patchValue('All');
         this.handleFilter()
       }
       if (key == 'new') {
        this.selectFilterForm.get('sourceProcLocation').patchValue(data.sourceProcLoc);
        this.selectFilterForm.get('destProcLocation').patchValue(data.destProcLoc);
       }
       
    });
  }

  createForm = () => {
    this.selectFilterForm = this.fb.group({
      sourceProcLocation: ['', [Validators.required]],
      destProcLocation: ['', [Validators.required]],
    });
  }

  handleFilter = (e?: any) => {
    if (!this.selectFilterForm.invalid) {
      let data: any = this.selectFilterForm.value;
      this.dataLoadService.getFilterTableData(data.sourceProcLocation, data.destProcLocation).subscribe(
        (result) => {
          let temp: any = [];
          for (let i = 0; i < result.length; i++) {
            temp.push({
              "id": i+1,
              "sourceProcLoc": result[i]?.id?.sourceProcLoc,
              "destProcLoc": result[i]?.id?.destProcLoc,
              "destSpecCode": result[i]?.id?.destSpecCode,
              "subAssyProdIdFormat": result[i]?.subAssyProdIdFormat ? result[i]?.subAssyProdIdFormat.split(',') : [],
              "prodDateOffset": result[i]?.prodDateOffset,
              "isEdit": false
            })
          }
          if (temp?.length) {
            this.dataSource.data = temp;
            this.displayMessage('Limited the records to ' + this.config);
          } else {
            this.dataSource.data = [];
            this.displayMessage('No records found');
          }
      });
    } else { }
  }

  resetFilter = () => {
    this.selectFilterForm.reset();
    this.getFilterData();
  }

  getRefreshData = (data: any) => {
    this.getFilterData('new', data);
    this.dataLoadService.getFilterTableData(data?.sourceProcLoc, data?.destProcLoc).subscribe(
      (result) => {
        let temp: any = [];
        for (let i = 0; i < result.length; i++) {
          let status: any = '';
          if ( result[i]?.id?.sourceProcLoc == data?.sourceProcLoc && result[i]?.id?.destProcLoc == data?.destProcLoc && result[i]?.id?.destSpecCode.includes(data?.destSpecCode)) {
            status = 'add';
          }
          temp.push({
            "id": i+1,
            "sourceProcLoc": result[i]?.id?.sourceProcLoc,
            "destProcLoc": result[i]?.id?.destProcLoc,
            "destSpecCode": result[i]?.id?.destSpecCode,
            "subAssyProdIdFormat": result[i]?.subAssyProdIdFormat ? result[i]?.subAssyProdIdFormat.split(',') : [],
            "prodDateOffset": result[i]?.prodDateOffset,
            "isEdit": false,
            "status": status
          })
        }
        if (temp?.length) {
          this.dataSource.data = temp;
        } else {
          this.dataSource.data = [];
          this.displayMessage('No records found');
        }
    });
  }

  displayMessage = (message: any) => {
    this.showMessage = true;
    this.message = message;
    setTimeout(() => { this.showMessage = false; this.message = '' }, 2500);
  }

}
