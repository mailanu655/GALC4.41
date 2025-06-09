import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-add-config',
  templateUrl: './add-config.component.html',
  styleUrls: ['./add-config.component.css']
})
export class AddConfigComponent implements OnInit {
  newRecord: { [key: string]: any } = {
    departmentId: '',
    groupType: '',
  };
  displayedColumns: string[] = [];
  processPoints: any[] = [];
  selectedProcessPoints: any[] = [];
  selectedProcessPoint: any = null;
  selectedRightProcessPoint: any = null;
  isNextNode: boolean = false;

  availableProcessPointsDataSource = new MatTableDataSource<any>();
  selectedProcessPointsDataSource = new MatTableDataSource<any>();
  nextNode: string | null;

  constructor(
    public dialogRef: MatDialogRef<AddConfigComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { columns: string[], nextOfNextNode: any, plant_id: number, department_id, groupType: any }
  ) { }

  ngOnInit(): void {
    this.displayedColumns = this.data.columns;
    console.log("columns", this.displayedColumns);
    this.nextNode = localStorage.getItem('node');
    console.log('Department ID:', this.data.department_id);
    console.log('GroupType:', this.data.groupType);

    this.displayedColumns.forEach(column => {
      if (this.nextNode === 'third') {
        if (column === 'groupType') {
          this.newRecord[column] = this.data.groupType || '1';
        } else if (column === 'departmentId') {
          this.newRecord[column] = this.data.department_id || '';
        } else {
          this.newRecord[column] = '';
        }
      } else {
        if (column === 'departmentId') {
          this.newRecord[column] = this.data.department_id || '';
        } else {
          this.newRecord[column] = '';
        }
      }
    });

    this.processPoints = this.extractProcessPoints(this.data.nextOfNextNode);
    this.availableProcessPointsDataSource.data = this.processPoints;
  }




  extractProcessPoints(nextOfNextNode: any[]): any[] {
    const points: any[] = [];
    if (nextOfNextNode && Array.isArray(nextOfNextNode)) {
      nextOfNextNode.forEach((element, index) => {
        const processPointName = element.processPointName ? element.processPointName.trim() : null;
        const processPointId = element.processPointId ? element.processPointId.trim() : null;
        if (processPointName && processPointId) {
          points.push({
            processPointName: processPointName,
            processPointId: processPointId
          });
        } else {
          console.log(`No valid processPointName or processPointId found for element at index ${index}:`, element);
        }
      });
    } else {
      console.log('nextOfNextNode is not an array or is undefined.');
    }

    return points;
  }
  onSubmit(): void {
    if (this.isFormValid()) {
      console.log("data saved ", this.newRecord);
      const confirmSave = window.confirm("Are you sure you want to save this record?");
      if (confirmSave) {
        this.dialogRef.close({ newRecord: this.newRecord, selectedProcessPoints: this.selectedProcessPoints });

        window.alert("Record saved successfully!");

        setTimeout(() => {
          window.location.reload();
        }, 500);
      }
    } else {
      window.alert("Please fill in all required fields.");
      console.error('Form is not valid.');
    }
  }
  isFormValid(): boolean {
    let isValid = true;

    this.displayedColumns.forEach(element => {
      let value = this.newRecord[element];
      if (value == null || value == undefined || value == "") {
        isValid = false
      }
    });

    return isValid;
  }


  onCancel(): void {
    this.dialogRef.close();
  }

  selectProcessPoint(processPoint: any): void {
    this.selectedProcessPoint = processPoint;
  }

  selectRightProcessPoint(processPoint: any): void {
    this.selectedRightProcessPoint = processPoint;
  }

  addToRight(): void {
    if (this.selectedProcessPoint) {
      const isAlreadySelected = this.selectedProcessPoints.some(
        (point) => point.processPointId === this.selectedProcessPoint.processPointId
      );

      if (!isAlreadySelected) {
        this.selectedProcessPoints.push({ ...this.selectedProcessPoint });
        this.selectedProcessPointsDataSource.data = [...this.selectedProcessPoints];
        this.availableProcessPointsDataSource.data = this.availableProcessPointsDataSource.data.filter(
          point => point.processPointId !== this.selectedProcessPoint.processPointId
        );
      }

      this.selectedProcessPoint = null;
    }
  }

  moveToLeft(): void {
    if (this.selectedRightProcessPoint) {
      const isAlreadyAvailable = this.availableProcessPointsDataSource.data.some(
        (point) => point.processPointId === this.selectedRightProcessPoint.processPointId
      );

      if (!isAlreadyAvailable) {
        this.availableProcessPointsDataSource.data = [
          { ...this.selectedRightProcessPoint },
          ...this.availableProcessPointsDataSource.data
        ];
      }

      this.selectedProcessPoints = this.selectedProcessPoints.filter(
        point => point.processPointId !== this.selectedRightProcessPoint.processPointId
      );
      this.selectedProcessPointsDataSource.data = [...this.selectedProcessPoints];

      this.selectedRightProcessPoint = null;
    }
  }
}
