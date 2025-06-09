import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { LotProductModel } from 'src/app/models';

@Component({
  selector: 'expand-row-dialog',
  templateUrl: './expand-row-dialog.component.html',
  styleUrls: ['./expand-row-dialog.component.css']
})
export class ExpandRowDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ExpandRowDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: LotProductModel) { }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
