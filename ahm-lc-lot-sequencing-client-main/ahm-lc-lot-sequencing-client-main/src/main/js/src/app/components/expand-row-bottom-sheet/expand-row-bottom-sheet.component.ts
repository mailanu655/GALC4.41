import { Component, OnInit } from '@angular/core';
import { MatBottomSheetRef } from '@angular/material/bottom-sheet';

@Component({
  selector: 'app-expand-row-bottom-sheet',
  templateUrl: './expand-row-bottom-sheet.component.html',
  styleUrls: ['./expand-row-bottom-sheet.component.css']
})
export class ExpandRowBottomSheetComponent implements OnInit {

  constructor(private bottomSheetRef: MatBottomSheetRef<ExpandRowBottomSheetComponent>) { }

  ngOnInit(): void {
  }

  openLink(event: MouseEvent): void {
    this.bottomSheetRef.dismiss();
    event.preventDefault();
  }
}
