import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { FnConfigServiceService } from 'src/app/services/fn-config-service.service';

@Component({
  selector: 'app-edit-config',
  templateUrl: './edit-config.component.html',
  styleUrls: ['./edit-config.component.css'],
})
export class EditConfigComponent implements OnInit {
  leftColumns: string[] = [];
  rightColumns: string[] = [];
  nodeData: any = {};
  processPoints: any[] = [];
  selectedProcessPoints: any[] = [];
  selectedProcessPoint: any = null;
  selectedRightProcessPoint: any = null;
  isFormChanged: boolean = false; // Flag to track changes

  availableProcessPointsDataSource = new MatTableDataSource<any>();
  selectedProcessPointsDataSource = new MatTableDataSource<any>();

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: { columns: string[]; nextOfNextNode: any[]; nodeData: any; groupid: number },
    private fnConfig: FnConfigServiceService,
    private dialogRef: MatDialogRef<EditConfigComponent>
  ) {
    const columns = data.columns || [];
    this.nodeData = { ...data.nodeData };

    const midIndex = Math.ceil(columns.length / 2);
    this.leftColumns = columns.slice(0, midIndex);
    this.rightColumns = columns.slice(midIndex);
  }

  ngOnInit(): void {
    this.processPoints = this.extractProcessPoints(this.data.nextOfNextNode);
    this.fetchProcessPoints(this.data.groupid).subscribe((points) => {
      this.selectedProcessPoints = [...points];
      this.selectedProcessPointsDataSource.data = [...this.selectedProcessPoints];

      this.filterAvailableProcessPoints();
    });

    this.trackFormChanges();
  }

  filterAvailableProcessPoints(): void {
    const selectedIds = this.selectedProcessPoints.map((point) => point.processPointId);

    this.availableProcessPointsDataSource.data = this.processPoints.filter(
      (point) => !selectedIds.includes(point.processPointId)
    );
  }

  fetchProcessPoints(id: number): Observable<any[]> {
    return this.fnConfig.getAvailableProcessPoints(id).pipe(
      map((processPoints) =>
        processPoints
          .filter((point) => point.galcProcessPointName && point.galcProcessPointId)
          .map((point) => ({
            processPointName: point.galcProcessPointName.trim(),
            processPointId: point.galcProcessPointId.trim(),
          }))
      ),
      catchError((error) => {
        console.error('Error fetching process points:', error);
        return of([]);
      })
    );
  }

  extractProcessPoints(nextOfNextNode: any[]): any[] {
    if (!Array.isArray(nextOfNextNode)) {
      console.error('Invalid process points data:', nextOfNextNode);
      return [];
    }

    return nextOfNextNode
      .filter((element) => element.processPointName && element.processPointId)
      .map((element) => ({
        processPointName: element.processPointName.trim(),
        processPointId: element.processPointId.trim(),
      }));
  }

  getPlaceholder(column: string): string {
    return column === 'departmentId' ? 'Department ID' : `Enter ${column}`;
  }

  save(): void {
    this.nodeData.processPointList = this.selectedProcessPoints;
    this.dialogRef.close(this.nodeData);
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
        this.selectedProcessPoints.unshift(this.selectedProcessPoint);
        this.selectedProcessPointsDataSource.data = [...this.selectedProcessPoints];
        this.availableProcessPointsDataSource.data = this.availableProcessPointsDataSource.data.filter(
          (point) => point.processPointId !== this.selectedProcessPoint.processPointId
        );
        this.isFormChanged = true;
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
          this.selectedRightProcessPoint,
          ...this.availableProcessPointsDataSource.data,
        ];
      }

      this.selectedProcessPoints = this.selectedProcessPoints.filter(
        (point) => point.processPointId !== this.selectedRightProcessPoint.processPointId
      );
      this.selectedProcessPointsDataSource.data = [...this.selectedProcessPoints];
      this.isFormChanged = true;

      this.selectedRightProcessPoint = null;
      this.availableProcessPointsDataSource.filter = '';
    }
  }

  trackFormChanges(): void {
    const originalNodeData = JSON.stringify(this.nodeData);
    setInterval(() => {
      if (
        JSON.stringify(this.nodeData) !== originalNodeData ||
        this.selectedProcessPoints.length !== this.selectedProcessPointsDataSource.data.length
      ) {
        this.isFormChanged = true;
      }
    }, 300);
  }
}
