import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FnConfigServiceService, TreeNode } from 'src/app/services/fn-config-service.service';
import { AddConfigComponent } from '../add-config/add-config.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Observable, forkJoin, of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import { EditConfigComponent } from '../edit-config/edit-config.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
  styleUrls: ['./config.component.css']
})
export class ConfigComponent implements OnChanges {
  @Input() node: TreeNode | null = null;
  allRecords: any[] = [];
  nodeColumns: string[] = [];
  isEditMode: boolean = false;
  originalNode: any;
  columnsToHide: string[] = ['id', 'nodetype', 'departmentList', 'children', 'processPointGroupList', 'plantId', 'processPointList', 'type', 'groupTypeList'];

  constructor(private fnConfig: FnConfigServiceService, private dialog: MatDialog, private snackBar: MatSnackBar) { }

  ngOnChanges(changes: SimpleChanges): void {
    this.isEditMode = false;
    if (changes['node'] && this.node) {
      if (this.node.type == 'fourth') {
        // this.refreshData();
      }
      this.updateNodeDetails();
      if (this.node.id) {
        localStorage.setItem('plant_id', this.node.id.toString());
        console.log('Plant ID set:', this.node.id);
      } else {
        console.log('Node has no plant_id:', this.node);
      }
      if (this.node.groupType !== undefined && this.node.groupType !== null) {
        localStorage.setItem('groupType', this.node.groupType.toString());
      } else {
      }
    }
  }

  updateNodeDetails(): void {
    if (this.node) {
      this.nodeColumns = this.getNodeColumns(this.node);
      this.allRecords = this.collectAllRecords(this.node);
    }
  }

  getNodeColumns(node: TreeNode): string[] {
    let nodeColumns: string[] = [];
    const addButton = localStorage.getItem('addButton');
    if (node.plantId && node.sequence && addButton === 'true') {
      localStorage.setItem('addButton', 'false');
      const defaultColumns = ['name', 'departmentId', 'description', 'gpcsPlantCode', 'gpcsLineNumber', 'gpcsProcessLocation', 'sequence', 'calculateDifference', 'groupType', 'metricType'];
      defaultColumns.forEach(column => {
        if (!nodeColumns.includes(column)) {
          nodeColumns.push(column);
        }
      });
    } else {
      nodeColumns = Object.keys(node).filter(key => !this.columnsToHide.includes(key));
    }
    return nodeColumns;
  }

  collectAllRecords(node: TreeNode): any[] {
    let records: any[] = [];
    if (node.records) {
      records = records.concat(node.records);
    }
    if (node.children) {
      for (let child of node.children) {
        records = records.concat(this.collectAllRecords(child));
      }
    }
    return records;
  }
  addNode(node: any): void {
    const nextNode = this.getNextNode();
    if (node) {
      if (node.groupType !== undefined) {
        localStorage.setItem('groupType', node.groupType.toString());
      }
    } else {
      console.error('Group Type is undefined.');
    }   
    if (nextNode) {
      let columns: any;
      this.getNextOfNextNode().subscribe(nextOfNextNode => {
        const addButton = localStorage.getItem('addButton');
        if (addButton === 'false') {
          columns = this.getNodeColumns(nextNode);
        } else {
          localStorage.setItem('addButton', 'true');
          columns = this.getNodeColumns(nextNode);
        }

        localStorage.setItem('addButton', 'false');
        const plantId = this.node?.id ?? parseInt(localStorage.getItem('plant_id') || '0', 10);
        const departmentId = this.node?.id;
        const groupType = localStorage.getItem('groupType');
        if (nextNode) {
          const dialogConfig = new MatDialogConfig();
          dialogConfig.width = '70vw';
          dialogConfig.height = '90vh';
          dialogConfig.autoFocus = true;
          dialogConfig.position = { top: '7vh' };
          dialogConfig.data = { columns, nextOfNextNode, plant_id: plantId, department_id: departmentId, groupType: groupType };
          const dialogRef = this.dialog.open(AddConfigComponent, dialogConfig);
          dialogRef.afterClosed().subscribe(result => {
            if (result) {
              if (this.node?.type === 'third') {
                this.saveNextOfNextNodeRecord(nextNode, result);
              } else {
                this.saveNewRecord(nextNode, result);
              }
            }
          });
        } else {
          console.error('Plant ID or nextOfNextNode data is not available');
        }
      });
    }
  }

  getNextOfNextNode(): Observable<any[]> {
    if (this.node && this.node.type === 'third' && this.node.id) {
      localStorage.setItem('node', 'third');
      localStorage.setItem('addButton', 'true');
      return this.fnConfig.getProcessPoints().pipe(
        map(processPoints => {
          return processPoints.map(point => ({
            processPointName: point.processPointName?.trim() || null,
            processPointId: point.processPointId?.trim() || null
          })).filter(point => point.processPointName && point.processPointId);
        }),
        catchError(error => {
          return of([]);
        })
      );
    }
    return of([]);
  }

  getAvailableProcess(): Observable<any[]> {
    console.log("nextNode ", this.node);
    return this.fnConfig.getProcessPoints().pipe(
      map(processPoints => {
        return processPoints.map(point => ({
          processPointName: point.processPointName?.trim() || null,
          processPointId: point.processPointId?.trim() || null
        })).filter(point => point.processPointName && point.processPointId);
      }),
      catchError(error => {
        return of([]);
      })
    );
    return of([]);
  }


  getNextNode(): TreeNode | null {
    if (this.node && this.node.children && this.node.children.length > 0) {
      localStorage.setItem('node', 'second');
      if (this.node.type === 'first' || this.node.type === 'third') {
        localStorage.setItem('addButton', 'false');
        if (this.node.children.length >= 1 && this.node.type === 'first') {
       const firstChild = this.node.children[0];
       if (firstChild.children && firstChild.children.length > 0) {
         return firstChild.children[0];
       } else {
         return null;
       }
     }else   if (this.node.children.length >= 1 && this.node.type === 'third') {
      console.log("entering in first if condition nextnode method");
   const firstChild = this.node.children[0];
   console.log("third child present",firstChild);
   if (firstChild.children && firstChild.children.length > 0) {
    const second =firstChild.children[0];
    console.log("second chindren data",firstChild);
     return firstChild;
   } else {
     return null;
   }
 }
     if (this.node.children.length >= 3) {
          const thirdChild = this.node.children[2];
          if (thirdChild.children && thirdChild.children.length > 0) {
            return thirdChild.children[0];
          } else {
            return null;
          }
        } else {
          return null;
        }
      } else {
        if (this.node.children.length > 1) {
          localStorage.setItem('addButton', 'true');
          return this.node.children[1];
        } else {
          return null;
        }
      }
    }
    return this.node;
  }

  saveNewRecord(node: TreeNode, newData: { newRecord: any; selectedProcessPoints: any[] }): void {
    const plantId = parseInt(localStorage.getItem('plant_id') || '0', 10);
    if (plantId) {
      this.fnConfig.saveRecordsForNextNode(node.type || '', node.groupId || 0, newData.newRecord, plantId)
        .subscribe(
          response => {
            this.updateNodeDetails();
          },
          error => console.error('Error saving next node records:', error)
        );
    } else {
      console.error('plant_id is not valid');
    }
  }

  saveNextOfNextNodeRecord(node: TreeNode, newData: { newRecord: any; selectedProcessPoints: any[] }): void {
    const plantId = parseInt(localStorage.getItem('plant_id') || '0', 10);
    if (plantId) {
      this.fnConfig.saveRecordsForNextOfNextNode(node.type || '', node.groupId || 0, newData.newRecord, newData.selectedProcessPoints, plantId)
        .subscribe(
          response => {
            this.updateNodeDetails();
          },
          error => console.error('Error saving next of next node records:', error)
        );
    } else {
      console.error('plant_id is not valid');
    }
  }

  getNodeLabel(node: TreeNode): string {
    switch (node.type) {
      case 'first':
        return 'Plant -';
      case 'second':
        return 'Group Type -';
      case 'third':
        return 'Dept -';
      case 'fourth':
        return 'Group -';
      case 'fifth':
        return 'PP -';
      default:
        return 'Unknown Type -';
    }
  }

  shouldShowAddButton(): boolean {
    if (this.node) {
      return this.node.type !== 'second' && this.node.type !== 'fourth' && this.node.type !== 'fifth';
    }
    return false;
  }
  getAddButtonLabel(): string {
    if (this.node) {
      if (this.node.type === 'first') {
        return 'Add Dept';
      } else if (this.node.type === 'third') {
        return 'Add Group';
      }
    }
    return 'Add';
  }

  saveEditedNode(updatedNode: TreeNode): void {
    if (updatedNode && this.node) {
      this.fnConfig.updateRecord(this.node.id || 0, updatedNode).subscribe(
        response => {
          console.log('Node updated successfully:', response);
          Object.assign(this.node!, updatedNode); // Update the current node with new data
          this.updateNodeDetails();
        },
        error => console.error('Error updating node:', error)
      );
    }
  }

  saveChanges(): void {
    if (this.node) {
      this.fnConfig.updateRecord(this.node.id || 0, this.node).subscribe(
        response => {
          this.node = { ...this.node, ...response };
          // this.refreshData();
          // this.changeDetector.detectChanges();
          this.isEditMode = false;
          this.snackBar.open('Node saved successfully!', 'Close', { duration: 3000 });
        },
        error => {
          console.error('Error saving node:', error);
          this.snackBar.open('Failed to save node. Please try again.', 'Close', { duration: 3000 });
        }
      );
    }
  }

  editNode(): void {
    if (this.node?.type?.toLowerCase() === 'fourth') {
      this.getAvailableProcess().subscribe(nextOfNextNode => {
        console.log("Process data:", nextOfNextNode);

        if (this.node) {
          const nodeData = this.node;
          const groupid = this.node.id as number;
          const nodeColumns = this.getNodeColumns(this.node);
          const dialogConfig = new MatDialogConfig();
          dialogConfig.width = '70vw';
          dialogConfig.height = '95vh';
          dialogConfig.autoFocus = true;
          dialogConfig.position = { top: '7vh' };
          dialogConfig.data = { columns: nodeColumns, nextOfNextNode, groupid, nodeData: nodeData };
          const dialogRef = this.dialog.open(EditConfigComponent, dialogConfig);
          dialogRef.afterClosed().subscribe(result => {
            if (result) {
              this.saveUpdatedNode(groupid, result);
            } else {
              console.log('Dialog closed without changes');
            }
          });
        }
      });
    } else {
      this.isEditMode = !this.isEditMode;

      if (this.isEditMode && this.node) {
        this.originalNode = { ...this.node };
      }
    }
  }

  saveUpdatedNode(groupid: number, updatedData: any): void {
    const processPointList = Array.isArray(updatedData.processPointList)
      ? updatedData.processPointList.map(point => ({
        galcProcessPointId: point?.processPointId || '',
        galcProcessPointName: point?.processPointName || '',
      }))
      : [];

    const updatedPayload = {
      ...updatedData,
      processPointList,
    };

    this.fnConfig.saveNode(groupid, updatedPayload).subscribe({
      next: (response) => {
        this.node = { ...this.node, ...response };
        this.refreshData();
        // this.updateNodeDetails();
        this.snackBar.open('Node saved successfully!', 'Close', { duration: 6000 });
      },
      error: (error) => {
        console.error('Error saving node:', error);
        this.snackBar.open('Failed to save node. Please try again.', 'Close', { duration: 4000 });
      },
    });
  }

  refreshData(): void {
    if (this.node?.name) {
      this.fnConfig.getNodeDetailsByName(this.node.name).subscribe({
        next: (updatedNode) => {
          if (updatedNode) {
            this.node = { ...updatedNode };
          } else {
            // console.warn('Node not found with the given name:', this.node.name);
          }
        },
        error: (error) => {
          console.error('Error fetching updated node data:', error);
        },
      });
    }
  }

  getChildNode(parentNode: any): any {
    return parentNode.children?.find((child: any) => child.type === 'fifth');
  }
  cancelEdit(): void {
    if (this.originalNode) {
      this.node = { ...this.originalNode };
    }
    this.isEditMode = false;
  }
}
