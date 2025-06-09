import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ConfirmationPopupComponent } from '../confirmation-popup/confirmation-popup.component';

@Component({
  selector: 'app-move-behind-confirmation-popup',
  templateUrl: './move-behind-confirmation-popup.component.html',
  styleUrls: ['./move-behind-confirmation-popup.component.css']
})
export class MoveBehindConfirmationPopupComponent {

  message: string = `Are you sure to move lot ${this.data.moveLot} behind the above selected lot?`
  confirmButtonText = "Yes"
  cancelButtonText = "No"
  confirmationContent: 'Please select a behind lot';
  selectedBehindLot: '';

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<ConfirmationPopupComponent>) {}

  onConfirmClick(value?): void {
    if (value == 'close') {
      this.dialogRef.close(false);
    } else {
      this.dialogRef.close(this.selectedBehindLot);
    }
  }
}
