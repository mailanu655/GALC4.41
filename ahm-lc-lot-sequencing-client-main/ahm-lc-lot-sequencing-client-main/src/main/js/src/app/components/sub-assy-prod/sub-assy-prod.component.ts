import { Component,  Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { FormBuilder } from '@angular/forms';
import { ConfigService } from 'src/app/services/config.service';

@Component({
  selector: 'app-sub-assy-prod',
  templateUrl: './sub-assy-prod.component.html',
  styleUrls: ['./sub-assy-prod.component.css']
})
export class SubAssyProdComponent implements OnInit {

  dialogData: any;
  form: any;
  optionsDate: any = ['productionDate5', 'productionDate6'];
  optionsYear: any = ['productionYear2', 'productionYear4'];

  optionsLineNumber: any = ['LineNum1', 'LineNum2'];

  list = [];

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<SubAssyProdComponent>,
    private fb: FormBuilder,
    private config: ConfigService
    ) {
    if (data) {
      
      this.dialogData = data;
      this.list = this.dialogData.mask ? this.dialogData.mask : [];
      this.createForm();
      this.updateList(this.list);
    }
  }
  updateList(list: any){
    if (this.list) { 
      if (this.list.includes(this.config.productType)) {
        this.list.shift();
      }
      if (this.list.includes('maskID')) {
        this.form.get('maskId').patchValue('maskID');
      }
      if (this.list.includes('partname')) {
        this.form.get('partName').patchValue('partname');
      }
      if (this.list.includes('productionDate5')) {
        this.form.get('date').patchValue('productionDate5');
      }
      if (this.list.includes('productionDate6')) {
        this.form.get('date').patchValue('productionDate6');
      }

      if (this.list.includes('productionYear2')) {
        this.form.get('year').patchValue('productionYear2');
      }

      if (this.list.includes('productionYear4')) {
        this.form.get('year').patchValue('productionYear4');
      }

      if (this.list.includes('LineNum1')) {
        this.form.get('lineNumber').patchValue('LineNum1');
      }

      if (this.list.includes('LineNum2')) {
        this.form.get('lineNumber').patchValue('LineNum2');
      }
      if (this.list.includes('SEQ_')) {
        this.form.get('lineNumber').patchValue('LineNum2');
      }

      for (let i = 0; i < this.list.length; i++) {
        if (this.list[i].includes('SEQ_')) {
          this.form.get('Sequence').patchValue(this.list[i].slice(4));
        }
      }
    }
  }

  ngOnInit(): void {
    this.onChange();
  }

  onChange = () => {
    this.form.get('date').valueChanges.subscribe(val => {
      if (val) {
        if (this.getIsSEQ()) {
          this.list.splice(this.list.length-1, 0, this.form.get('date').value)
        } else {
          this.list.push(this.form.get('date').value);
        }
      }
    });
    this.form.get('year').valueChanges.subscribe(val => {
      if (val) {
        if (this.getIsSEQ()) {
          this.list.splice(this.list.length-1, 0, this.form.get('year').value)
        } else {
          this.list.push(this.form.get('year').value);
        }
      }
    });
    this.form.get('userDefined').valueChanges.subscribe(val => {
      if (val) {
      }
    });
    this.form.get('lineNumber').valueChanges.subscribe(val => {
      if (val) {
        if (this.getIsSEQ()) {
          this.list.splice(this.list.length-1, 0, this.form.get('lineNumber').value)
        } else {
          this.list.push(this.form.get('lineNumber').value);
        }
      }
    });
  }

  getIsSEQ = () => {
    let temp: boolean = false;
    for (let i = 0; i < this.list.length; i++) {
      if (this.list[i].includes('SEQ_')) {
        temp = true;
        break;
      }
    }
    return temp;
  }

  createForm = () => {
    this.form = this.fb.group({
      prefix: [''],
      form2: [''],
      date: [''],
      year: [''],
      maskId: [''],
      userDefined: [''],
      lineNumber: [''],
      partName: [''],
      Sequence: ['', []]
    });
  }


  drop(event: CdkDragDrop<string[]>) {
    let temp: any;
    for (let i = 0; i < this.list.length; i++) {
      if (this.list[i].includes('SEQ_')) {
        temp = true;
        break;
      }
    }
    if (temp) {
      if (this.list?.length-1 !== event.currentIndex) {
        moveItemInArray(this.list, event.previousIndex, event.currentIndex);
      }
    } else {
      moveItemInArray(this.list, event.previousIndex, event.currentIndex);
    }
  }

  add = () => {
    let temp: any;
    if (this.list && this.list.length) {
      this.list.unshift(this.config.productType);
      this.dialogRef.close(this.list);
    }
  }

  cancel = () => {
    this.dialogRef.close('');
  }

  onEnterSequence = () => {
    if (this.form.get('Sequence').value) {
      this.list = this.list.filter((el: any) => !el.includes('SEQ_'))
      this.list.push(`SEQ_${this.form.get('Sequence').value}`);
      this.form.get('Sequence').patchValue('');
    }
  }

  onEnterPrefix = () => {
    if (this.form.get('prefix').value) {
      this.list.push(`PREFIX_${this.form.get('prefix').value}`);
      this.form.get('prefix').patchValue('');
    }
  }

  onEnterUserDefined = () => {
    if (this.form.get('userDefined').value) {
      if (this.getIsSEQ()) {
        this.list.splice(this.list.length-1, 0, this.form.get('userDefined').value)
      } else {
        this.list.push(this.form.get('userDefined').value);
      }

      this.form.get('userDefined').disable();
    }
  }

  remove = (data: any, id: any) => {
    if (data == 'maskID') {
      this.form.get('maskId').patchValue('');
    }
    if (data == 'partname') {
      this.form.get('partName').patchValue('');
    }
    if (data?.length >= 1) {
      this.form.get('userDefined').patchValue('');
      this.form.get('userDefined').enable();
    }
    if (this.optionsDate.includes(data)) {
      this.form.get('date').patchValue('');
    }
    if (this.optionsYear.includes(data)) {
      this.form.get('year').patchValue('');
    }
    this.list.splice(id, 1);
  }

  setmaskId = () => {
    if (!this.form.get('maskId').value) {
      if (this.getIsSEQ()) {
        this.list.splice(this.list.length-1, 0, 'maskID')
      } else {
        this.list.push('maskID');
      }
      this.form.get('maskId').patchValue('maskID');
    }
  }

  setPartName = () => {
    if (!this.form.get('partName').value) {
      if (this.getIsSEQ()) {
        this.list.splice(this.list.length-1, 0, 'partname')
      } else {
        this.list.push(`partname`);
      }
      this.form.get('partName').patchValue('partname');
    }
  }

}
