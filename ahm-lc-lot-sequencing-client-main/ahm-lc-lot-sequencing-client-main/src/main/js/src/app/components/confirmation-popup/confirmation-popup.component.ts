import { Component, Inject } from '@angular/core';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';


@Component({
  selector: 'app-confirmation-popup',
  templateUrl: './confirmation-popup.component.html',
  styleUrls: ['./confirmation-popup.component.css']
})
export class ConfirmationPopupComponent {
  message: string = "Are you sure?"
  confirmButtonText = ""
  cancelButtonText = ""
  confirmData: any = '';
  confirmationContent: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<ConfirmationPopupComponent>) {
    if (data) {
      this.message = data.message || this.message;
      this.confirmationContent = data?.confirmationContent || this.confirmationContent;
      this.confirmData = data.confirmData || this.confirmData;
      if (data.buttonText) {
        this.confirmButtonText = data.buttonText.ok;
        this.cancelButtonText = data.buttonText.cancel;
      }
    }
  }

  onConfirmClick(value?): void {
    if (value == 'close') {
      this.dialogRef.close(false);
    } else {
      this.dialogRef.close(this.confirmData);
    }
  }

}