import { A11yModule } from '@angular/cdk/a11y';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { CdkTreeModule } from '@angular/cdk/tree';
import { MatMenuModule } from '@angular/material/menu';
import { NgModule } from '@angular/core';
import { MatNativeDateModule } from '@angular/material/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSortModule } from '@angular/material/sort';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatBottomSheetModule } from '@angular/material/bottom-sheet';
import { MatListModule } from '@angular/material/list';
import { MatDialogModule } from '@angular/material/dialog';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDividerModule } from '@angular/material/divider';
import { MatRadioModule } from '@angular/material/radio';

@NgModule({
  exports: [
    MatNativeDateModule,
    A11yModule,
    CdkStepperModule,
    MatTableModule,
    CdkTreeModule,
    DragDropModule,
    MatExpansionModule,
    MatIconModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatSortModule,
    MatInputModule,
    MatMenuModule,
    MatCheckboxModule,
    MatSelectModule,
    MatSnackBarModule,
    MatBottomSheetModule,
    MatListModule,
    MatToolbarModule,
    MatButtonModule,
    MatTooltipModule,
    MatDatepickerModule,
    MatDividerModule,
    MatRadioModule,
    MatDialogModule,
  ],
})
export class WeldScheduleMaterialModule {}
