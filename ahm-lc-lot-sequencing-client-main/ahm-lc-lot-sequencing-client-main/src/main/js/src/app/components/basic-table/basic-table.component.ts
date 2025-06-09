import { MatSort } from '@angular/material/sort';
import {
  Component,
  OnInit,
  OnChanges,
  Input,
  ViewChild,
  AfterViewInit,
  ChangeDetectorRef,
  HostListener,
  Output,
  EventEmitter,
  QueryList,
  ViewChildren,
} from '@angular/core';
import { MatRow, MatTable, MatTableDataSource } from '@angular/material/table';
import { MatMenu, MatMenuTrigger } from '@angular/material/menu';
import { SelectionModel } from '@angular/cdk/collections';
import { Observable } from 'rxjs';
import { CdkDragDrop, CdkDragEnd, CdkDragStart } from '@angular/cdk/drag-drop';

import jsPDF from '../jsPDFAutoTable';
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FileDownloadService, LogService, LotsService } from 'src/app/services';
import { FormControl } from '@angular/forms';
import {
  IS_DRAGABLE,
  IS_SEND_TO_WELD_ON_ENABLED,
  WELD_SCHEDULE_COLUMN_HEADER_MAPPING,
} from 'src/app/constants';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { MatDialog } from '@angular/material/dialog';
import { ExpandRowBottomSheetComponent } from '../expand-row-bottom-sheet/expand-row-bottom-sheet.component';
import { ExpandRowDialogComponent } from '../expand-row-dialog/expand-row-dialog.component';
import {
  copyToClipboard,
  getDecimalPart,
  getRandomColor,
  isJson,
} from 'src/app/utils';
import { WeldScheduleModel } from 'src/app/models';
import { RowFormDialogComponent } from '../row-form-dialog/row-form-dialog.component';
import { allProductSpecData } from 'src/app/mocks/mock-all-product-spec';
import { ConfirmationPopupComponent } from '../confirmation-popup/confirmation-popup.component';
import { ConfigService } from 'src/app/services/config.service';
import { MoveBehindConfirmationPopupComponent } from '../move-behind-confirmation-popup/move-behind-confirmation-popup.component';
import { SecurityService } from 'src/app/services/security.service';

@Component({
  selector: 'basic-table',
  templateUrl: './basic-table.component.html',
  styleUrls: ['./basic-table.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition(
        'expanded <=> collapsed',
        animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')
      ),
    ]),
  ],
})
export class BasicTableComponent implements OnInit, AfterViewInit, OnChanges {
  @Input() dataSource: any[] = [];
  @Input() columns: any[] = [];
  @Input() tableTitle: string = null;
  @Input() planCode: string = null;
  @Input() selectedProcessLocation = null;
  @Input() commentsOptions = [
    'Remakes',
    'New Model',
    'Reschedule',
    'Frozen schedule',
  ];
  @Input() productSendStatuses = [];
  @ViewChild('tbMatSort') tbMatSort: MatSort = new MatSort();
  @ViewChild('basicTable') basicTable!: MatTable<any>;
  @ViewChildren(MatRow) basicTableRows: QueryList<MatRow>;
  @Output() onRefreshTable = new EventEmitter<any>();

  lastRowBasicTable: MatRow;
  gap = 100;
  originalColumns = [];
  displayedColumns: string[];
  selectableColumns: string[];
  tableDataSource: MatTableDataSource<any>;
  initialSelection = [];
  allowMultiSelect = true;
  rowSelection = new SelectionModel<any>(
    this.allowMultiSelect,
    this.initialSelection
  );
  columnsSelection: SelectionModel<string>;
  modelsSelection: SelectionModel<string>;
  expandedRow$: Observable<any>;
  sequenceList: number[] = null;
  highlightedRows = [];
  filterText = '';
  isDragable = IS_DRAGABLE;
  isSentToWeldOnEnabled = IS_SEND_TO_WELD_ON_ENABLED
  isCommentEditAllowed = this.config.isCommentEditAllowed;
  isAddEnabled = false;
  totalLots = 0;

  // model filter related variables
  public selectedModelList: string[];
  public modelList: string[];
  public isMovingRows = false;

  // type filter related variables
  public type: string;
  public selectedTypeList: string[];
  public typeList: string[];
  public WELD_SCHEDULE_COLUMN_HEADER_MAPPING =
    WELD_SCHEDULE_COLUMN_HEADER_MAPPING;

  rowOnEditing = null;

  modelFilter = new FormControl();
  typeFilter = new FormControl('');

  filterValues = {
    model: [],
    type: '',
  };

  private rowsToBeMovedAndSaved = [];

  selectedRows: WeldScheduleModel[] = [];

  allProductSpecData = allProductSpecData;

  isSavingRow = false;
  currentIndex: number;
  prevIndex: number;
  prevItem: WeldScheduleModel;

  constructor(
    private lotsService: LotsService,
    private fileDownloadService: FileDownloadService,
    private logService: LogService,
    private cd: ChangeDetectorRef,
    private config: ConfigService,
    public snackBar: MatSnackBar,
    private bottomSheet: MatBottomSheet,
    public dialog: MatDialog,
    private securityService: SecurityService
  ) {}

  ngOnInit(): void {
    this.originalColumns = JSON.parse(JSON.stringify(this.columns));
    this.sequenceList = this.dataSource?.map((item) => item.seqNumber);
    this.tableDataSource = new MatTableDataSource(this.dataSource);
    this.totalLots = 0;
    this.displayedColumns = this.columns
      .filter((c) => c.isShown)
      .map((c) => c.columnDef);
    this.selectableColumns = this.columns
      .map((c) => c.columnDef)
      .slice(2)
      .slice(0, this.columns.length - 3);
    this.dataSource?.forEach((row) => {
      this.totalLots += row.lotSize;
    });

    // type filter
    this.type = '';
    this.search();
  }

  ngOnChanges(): void {
    this.tableDataSource = new MatTableDataSource(this.dataSource);
  }

  ngAfterViewInit() {
    this.tableDataSource.sort = this.tbMatSort;
    this.columnsSelection = new SelectionModel<string>(
      this.allowMultiSelect,
      this.displayedColumns
        ?.slice(2)
        .slice(0, this.displayedColumns?.length - 4)
    );

    this.typeFilter.valueChanges.subscribe((type) => {
      this.filterValues.type = type;
      this.tableDataSource.filter = JSON.stringify(this.filterValues);
    });

    this.modelFilter.valueChanges.subscribe((model) => {
      this.filterValues.model = model.map((m) => m.trim().toLowerCase());
      this.tableDataSource.filter = JSON.stringify(this.filterValues);
    });
  }

  refreshTable() {
    this.columns = JSON.parse(JSON.stringify(this.originalColumns));
    this.filterText = '';
    this.ngOnInit();
    this.ngAfterViewInit();
    this.cd.detectChanges();
    this.onRefreshTable.emit('refresh-' + this.tableTitle);
    this.logService.gui('Refreshed table: ' + this.tableTitle, [{
      method: 'refreshTable',
    }]);
  }

  exportCsv() {
    this.fileDownloadService.downloadCsv(
      this.tableDataSource.data,
      this.tableTitle
    );
    this.logService.gui('Exported to CSV: ' + this.tableTitle, [{
      method: 'exportCsv',
    }]);
  }

  downloadExcel() {
    this.fileDownloadService.downloadExcel(
      this.tableDataSource.data,
      this.tableTitle
    );
    this.logService.gui('Exported to Excel: ' + this.tableTitle, [{
      method: 'downloadExcel',
    }]);
  }

  printTable() {
    window.print();
    this.logService.info('Printed table: ' + this.tableTitle, [{
      method: 'printTable',
    }]);
  }

  downloadPdf(){
   const doc = new jsPDF();
   const rows =[];
   
   const col = ['Production Lot','KD Production Lot', 'Lot Size', 'Model','Type','Comment','Notes'];
   this.tableDataSource.data.forEach(element => {
      const temp = [element.productionLot,element.kdLotNumber, element.lotSize, element.modelCode, element.modelType, element.comments, element.notes, element.rowBackgroundColor, element.rowBackgroundColor];
      rows.push(temp);
   });
    const currentDate = new Date().toLocaleDateString();
    doc.setFillColor(255,255,255);
    const addHeader = (data) =>{
    doc.setFontSize(11);     
    doc.setDrawColor(183,183,183);
    
    doc.roundedRect(12.5,5,185,40,1,1);
    
    doc.text(this.tableTitle, doc.internal.pageSize.width/2,15,{align:'center'});
    doc.text('Plant Code: ' +  this.planCode, data.settings.margin.left,25);
    doc.text('Process Location: ' + this.selectedProcessLocation,150,25);
    doc.text('Total Units: ' + this.totalLots, data.settings.margin.left,35);
    doc.text('Date: '+ currentDate, 150, 35);
    
    };
  
    function addFooters(doc) {
      const pageCount = doc.internal.getNumberOfPages();
      doc.setFontSize(10);
      for (var i = 1; i <= pageCount; i++) {
        doc.setPage(i);
        doc.text( String(i) , doc.internal.pageSize.width / 2, 287, {
          align: 'center'
        })
      }
    }
   
    doc.autoTable({ 
         margin:{
          top : 50
         },
          styles: {
            lineWidth: 0.2,
            cellWidth: 'auto',
            fontSize: 10,
            fillColor:[255,255,255]
        },
        headStyles: { 
          fillColor:[255,255,255],
          textColor: [0,0,0],
        },
        head : [col],
        body: rows,
        theme:'plain',
        didDrawPage: function(data){
          doc.setDrawColor(211,211,211);
          addHeader(data);
          addFooters(doc);
        }, 
       didParseCell: function(data){
        const {cell,row, column} = data;
        if(data.section === 'body' && data.row.raw[7] != null){
          cell.styles.fillColor = data.row.raw[7];
        }
      }
      
    });
    
        doc.autoPrint();
        doc.output('dataurlnewwindow');
    }
  

  getBgColor(str: string, index: number = 0): string {
    if (!str) return null;

    if (index % 2 === 1) {
      return 'rgba(161, 198, 175, 0.3)';
    } else {
      return null;
    }
  }

 
  getCommentBgColor(str: string): string {
    if (!str) return null;
    return getRandomColor(str);
  }

  displayModelOptions(): string {
    const selectedModels = this.modelsSelection.selected;
    if (selectedModels === null || selectedModels.length === 0) {
      return 'None ';
    }

    if (
      selectedModels.length >= 1 &&
      selectedModels.length < this.modelList.length
    ) {
      return `${selectedModels.length} of  ${this.modelList.length} `;
    }

    return 'All ';
  }

  onDragStart(event: CdkDragStart, row: any) {
    this.selectedRows = this.tableDataSource.data.filter((r) =>
      this.rowSelection.isSelected(r)
    );
  }

  onDragEnd(event: CdkDragEnd) {
    this.selectedRows = [];
  }

  toggleColumnSelection(column: string) {
    this.columnsSelection.toggle(column);
    const toggledColumn = this.columns.find((c) => c.columnDef === column);
    if (this.columnsSelection.selected.includes(column)) {
      toggledColumn.isShown = true;
    } else {
      toggledColumn.isShown = false;
    }

    this.displayedColumns = this.columns
      .filter((c) => c.isShown)
      .map((c) => c.columnDef);
  }

  toggleModelSelection(model: string) {
    this.modelsSelection.toggle(model);
    this.selectedModelList = this.modelsSelection.selected;
  }

  createFilter(): (data: any, filter: string) => boolean {
    let filterFunction = (data: any, filter: string): boolean => {
      if (isJson(filter)) {
        let searchTerms = JSON.parse(filter);
        const selectedModels = searchTerms?.model?.map((m) =>
          m ? m.trim().toLowerCase() : null
        );
        return (
          selectedModels.includes(
            data.productSpecCode.trim().toLowerCase().substring(0, 4)
          ) &&
          data.productSpecCode
            .trim()
            .toLowerCase()
            .substring(4, 7)
            .indexOf(searchTerms.type.trim().toLowerCase()) !== -1
        );
      } else {
        return (
          data.productSpecCode
            .trim()
            .toLowerCase()
            .indexOf(filter.trim().toLowerCase()) !== -1 ||
          data.productionLot
            .trim()
            .toLowerCase()
            .indexOf(filter.trim().toLowerCase()) !== -1 ||
          data.kdLotNumber
            .trim()
            .toLowerCase()
            .indexOf(filter.trim().toLowerCase()) !== -1 ||
          data.notes
            .trim()
            .toLowerCase()
            .indexOf(filter.trim().toLowerCase()) !== -1 ||
          data.comments
            .trim()
            .toLowerCase()
            .indexOf(filter.trim().toLowerCase()) !== -1
        );
      }
    };
    return filterFunction;
  }

  search() {
    this.modelList = Array.from(
      new Set(this.dataSource?.map((ele) => ele.modelCode))
    ).sort((code1: string, code2: string) => code1.localeCompare(code2));
    this.modelsSelection = new SelectionModel<string>(
      this.allowMultiSelect,
      this.modelList.map((m) => m.toLocaleLowerCase())
    );
    this.modelFilter.setValue(
      this.modelList.map((m) => m.trim().toLowerCase())
    );

    this.typeList = Array.from(
      new Set(this.dataSource?.map((ele) => ele.modelType))
    ).sort((type1: string, type2: string) => type1.localeCompare(type2));

    this.tableDataSource.filterPredicate = this.createFilter();
  }

  freezeSelectedRows(): void {
    const selectedRows = this.rowSelection.selected;
    const startTime:any = new Date();

    this.lotsService.postFreezeLots(selectedRows).subscribe({
      next: (result) => {
        this.isMovingRows = false;
        this.snackBar.open(
          'The selected lot rows were frozen successfully!',
          'ok',
          {
            duration: 5000,
          }
        );
        const endTime:any = new Date();
        this.logService.debug('Freezing rows successfully', [{
          method: 'freezeSelectedRows',
          duration: endTime - startTime,
        }]);
        this.onRefreshTable.emit({
          planCode: this.planCode,
          processLocation: this.selectedProcessLocation,
        });
        this.cd.detectChanges();
      },
      error: (err) => {
        console.warn('err:', err);
        const endTime:any = new Date();
        if (err?.status === 200) {
          this.snackBar.open(
            'The selected lot rows were frozen successfully!',
            'ok',
            {
              duration: 5000,
            }
          );
          this.logService.info('Freezing rows successfully', [{
            method: 'freezeSelectedRows',
            duration: endTime - startTime,
          }]);
        } else {
          this.logService.fatal('Failed to freeze the selected lot rows with error:' + err, [{
            method: 'freezeSelectedRows',
            duration: endTime - startTime,
          }]);
          this.isMovingRows = false;
          this.snackBar.open('Failed to freeze the selected lot rows!', 'ok', {
            duration: 5000,
          });
        }
      },
    });
  }

  onHoldLotAction(item: WeldScheduleModel): void {
    const startTime:any = new Date();

    this.lotsService.postHoldLot(item).subscribe({
      next: (result) => {
        this.isMovingRows = false;
        this.snackBar.open('The row was held successfully!', 'ok', {
          duration: 5000,
        });
        const endTime:any = new Date();
        this.logService.debug('Hold row successfully', [{
          method: 'onHoldLotAction',
          duration: endTime - startTime,
        }]);
        this.onRefreshTable.emit({
          planCode: this.planCode,
          processLocation: this.selectedProcessLocation,
        });
        this.cd.detectChanges();
      },
      error: (err) => {
        const endTime:any = new Date();

        if (err?.status === 200) {
          this.snackBar.open('The row was held successfully!', 'ok', {
            duration: 5000,
          });
          this.logService.debug('Hold row successfully', [{
            method: 'onHoldLotAction',
            duration: endTime - startTime,
          }]);
        } else {
          this.logService.fatal('Failed to hold the row error: ' + err, [{
            method: 'onHoldLotAction',
            duration: endTime - startTime,
          }]);
          this.isMovingRows = false;
          this.snackBar.open('Failed to hold the row!', 'ok', {
            duration: 5000,
          });
        }
      },
    });
  }

  onReleaseLotAction(item: WeldScheduleModel): void {
    const startTime:any = new Date();
    this.lotsService.postReleaseLot(item).subscribe({
      next: (_) => {
        const endTime:any = new Date();

        this.isMovingRows = false;
        this.snackBar.open('The row was released successfully!', 'ok', {
          duration: 5000,
        });

        this.logService.debug('Release row successfully', [{
          method: 'onReleaseLotAction',
          duration: endTime - startTime,
        }]);
        this.onRefreshTable.emit({
          planCode: this.planCode,
          processLocation: this.selectedProcessLocation,
        });
        this.cd.detectChanges();
      },
      error: (err) => {
        const endTime:any = new Date();

        if (err?.status === 200) {
          this.snackBar.open('The row was released successfully!', 'ok', {
            duration: 5000,
          });
          this.logService.debug('Release row successfully', [{
            method: 'onReleaseLotAction',
            duration: endTime - startTime,
          }]);
        } else {
          this.logService.fatal('Failed to release the row error: ' + err, [{
            method: 'onReleaseLotAction',
            duration: endTime - startTime,
          }]);
          this.isMovingRows = false;
          this.snackBar.open('Failed to release the row!', 'ok', {
            duration: 5000,
          });
        }
      },
    });
  }

  moveBehindLot(moveLotId: string): void {
    const dialogRef = this.dialog.open(MoveBehindConfirmationPopupComponent, {
      data: {
        behindLots: this.tableDataSource.data.map((row) => row.productionLot),
        moveLot: moveLotId,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      const startTime:any = new Date();

      if (result) {
        this.lotsService.getMoveLotBehindLot(moveLotId, result).subscribe({
          next: (_) => {
            const endTime:any = new Date();

            this.snackBar.open(`Move ${moveLotId} behind ${result} successfully!`, 'ok', {
              duration: 5000,
            });
            this.logService.info(`Move ${moveLotId} behind ${result} successfully`, [{
              method: 'moveBehindLot',
              duration: endTime - startTime,
            }]);
            this.onRefreshTable.emit({
              planCode: this.planCode,
              processLocation: this.selectedProcessLocation,
            });
            this.cd.detectChanges();
          },
          error: (err) => {
            const endTime:any = new Date();

            if (err?.status === 200) {
              this.snackBar.open(`Move ${moveLotId} behind ${result} successfully!`, 'ok', {
                duration: 5000,
              });
              this.logService.debug(`Move ${moveLotId} behind ${result} successfully`, [{
                method: 'moveBehindLot',
                duration: endTime - startTime,
              }]);
            } else {
              this.logService.fatal(`Fail to move ${moveLotId} behind ${result}: ` + err, [{
                method: 'moveBehindLot',
                duration: endTime - startTime,
              }]);

              this.isMovingRows = false;
              this.snackBar.open(`Fail to move ${moveLotId} behind ${result}!`, 'ok', {
                duration: 5000,
              });
            }
          },
        });
      }
    });
    
  }

  onBuildAheadLotAction(item: WeldScheduleModel): void {
    const startTime:any = new Date();

    this.currentIndex = this.tableDataSource.data.indexOf(item);

    if (this.currentIndex === 0) {
      this.snackBar.open('No Rows to Build Ahead', 'ok', {
        duration: 5000,
      });
      return;
    }

    this.lotsService.postBuildAheadLot(item).subscribe({
      next: (result) => {
        const endTime:any = new Date();

        this.isMovingRows = false;
        this.snackBar.open('The build ahead request successfully!', 'ok', {
          duration: 5000,
        });

        this.logService.info('build ahead row successfully', [{
          method: 'onBuildAheadLotAction',
          duration: endTime - startTime,
        }]);
        this.onRefreshTable.emit({
          planCode: this.planCode,
          processLocation: this.selectedProcessLocation,
        });
        this.cd.detectChanges();
      },
      error: (err) => {
        const endTime:any = new Date();

        if (err?.status === 200) {
          this.snackBar.open('The row was build ahead successfully!', 'ok', {
            duration: 5000,
          });
          this.logService.info('build ahead row successfully', [{
            method: 'onBuildAheadLotAction',
            duration: endTime - startTime,
          }]);
        } else {
          this.logService.fatal('Failed to build ahead the row error: ' + err, [{
            method: 'onBuildAheadLotAction',
            duration: endTime - startTime,
          }]);
          this.isMovingRows = false;
          this.snackBar.open('Failed to build ahead the row!', 'ok', {
            duration: 5000,
          });
        }
      },
    });
  }

  unfreezeSelectedRows(): void {
    const selectedRows = this.rowSelection.selected;
    const startTime:any = new Date();

    this.lotsService.postUnfreezeLots(selectedRows).subscribe({
      next: (result) => {
        const endTime:any = new Date();

        this.isMovingRows = false;
        this.snackBar.open(
          'The selected lot rows were unfrozen successfully!',
          'ok',
          {
            duration: 5000,
          }
        );
        this.logService.info('Unfreezing rows successfully', [{
          method: 'unfreezeSelectedRows',
          duration: endTime - startTime,
        }]);
        this.onRefreshTable.emit({
          planCode: this.planCode,
          processLocation: this.selectedProcessLocation,
        });
        this.cd.detectChanges();
      },
      error: (err) => {
        const endTime:any = new Date();

        if (err?.status === 200) {
          this.snackBar.open(
            'The selected lot rows were unfrozen successfully!',
            'ok',
            {
              duration: 5000,
            }
          );
          this.logService.debug('Unfreezing rows successfully', [{
            method: 'unfreezeSelectedRows',
            duration: endTime - startTime,
          }]);
        } else {
          this.logService.fatal(
            'Failed to unfreeze the selected lot rows!' + err, [{
              method: 'unfreezeSelectedRows',
              duration: endTime - startTime,
            }]
          );
          this.isMovingRows = false;
          this.snackBar.open(
            'Failed to unfreeze the selected lot rows!',
            'ok',
            {
              duration: 5000,
            }
          );
        }
      },
    });
  }

  sendToWeldHighlighted(item: any): void {
    const dialogRef = this.dialog.open(ConfirmationPopupComponent, {
      data: {
        message: 'Send the highlighted to weld on',
        confirmationContent: `Are you sure you want to send Vin - ${item.productId} to weld on??`,
        buttonText: { ok: 'Yes', cancel: 'No' },
        confirmData: true,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      const startTime:any = new Date();

      if (result) {
        // user clicked 'Yes'
        this.lotsService
          .postSendToWeldOn(item)
          .subscribe({
            next: (result) => {
              const endTime:any = new Date();

              this.isMovingRows = false;
              this.snackBar.open(
                `Selected Vin - ${ item.productId } successfully sent to weld on`,
                'ok',
                {
                  duration: 5000,
                }
              );
              this.logService.debug('Unfreezing rows successfully', [{
                method: 'sendToWeldHighlighted',
                duration: endTime - startTime,
              }]);
              this.onRefreshTable.emit({
                planCode: this.planCode,
                processLocation: this.selectedProcessLocation,
              });
              this.cd.detectChanges();
            },
            error: (err) => {
              const endTime:any = new Date();

              if (err?.status === 200) {
                this.snackBar.open(
                  'The selected lot rows were unfrozen successfully!',
                  'ok',
                  {
                    duration: 5000,
                  }
                );
                this.logService.info('Unfreezing rows successfully', [{
                  method: 'sendToWeldHighlighted',
                  duration: endTime - startTime,
                }]);
              } else {
                this.logService.fatal(
                  'Failed to unfreeze the selected lot rows error: ' + err, [{
                    method: 'sendToWeldHighlighted',
                    duration: endTime - startTime,
                  }]
                );
                this.isMovingRows = false;
                this.snackBar.open(
                  'Failed to unfreeze the selected lot rows!',
                  'ok',
                  {
                    duration: 5000,
                  }
                );
              }
            },
          });
      } else {

      }
    });
    
  }

  moveSelectedRows(offset: number): void {
    const selectedRows = this.rowSelection.selected;
    const data = this.tableDataSource.data;
    const startTime:any = new Date();

    this.rowsToBeMovedAndSaved = [];

    selectedRows.forEach((row) => this.moveSelectedRow(row, offset));

    this.lotsService.postLotSaveAll(this.rowsToBeMovedAndSaved).subscribe({
      next: (result) => {
        const endTime:any = new Date();

        this.isMovingRows = false;
        this.snackBar.open('The rows were moved successfully!', 'ok', {
          duration: 5000,
        });
        this.tableDataSource.data = data;
        this.logService.debug('Moving rows successfully', [{
          method: 'moveSelectedRows',
          duration: endTime - startTime,
        }]);
      },
      error: (err) => {
        const endTime:any = new Date();

        if (err?.status === 200) {
          this.snackBar.open('The rows were moved successfully!', 'ok', {
            duration: 5000,
          });
          this.tableDataSource.data = data;
          this.logService.debug('Moving rows successfully', [{
            method: 'moveSelectedRows',
            duration: endTime - startTime,
          }]);
        } else {
          this.logService.fatal('Failed to move the rows: ' + err, [{
            method: 'moveSelectedRows',
            duration: endTime - startTime,
          }]);
          this.isMovingRows = false;
          this.snackBar.open('Failed to move the rows!', 'ok', {
            duration: 5000,
          });
        }
      },
    });
  }

  moveSelectedRow(selectedRow, offset: number): void {
    if (offset === 0) {
      return;
    }

    this.isMovingRows = true;

    const selectedRows = this.rowSelection.selected;
    const data = this.tableDataSource.data;

    if (selectedRows.length === 0) {
      return;
    }

    const currentIndex = data.indexOf(selectedRow);
    const newIndex = currentIndex + offset;

    if (Math.abs(offset) === 1) {
      // Only move to one row down or up
      const swappedRow = data[newIndex];
      if (newIndex >= 0 && newIndex < data.length) {
        data.splice(currentIndex, 1);
        data.splice(newIndex, 0, selectedRow);
      }
      let tmpSeq = selectedRow.seqNumber;
      selectedRow.seqNumber = +(
        Math.trunc(swappedRow.seqNumber) +
        '.' +
        getDecimalPart(selectedRow.seqNumber)
      );
      swappedRow.seqNumber = +(
        Math.trunc(tmpSeq) +
        '.' +
        getDecimalPart(swappedRow.seqNumber)
      );

      this.rowsToBeMovedAndSaved.push(selectedRow);
      this.rowsToBeMovedAndSaved.push(swappedRow);
    } else {
      if (newIndex === 0) {
        // Move to the top
        const firstRow = data[0];
        data.splice(currentIndex, 1);
        data.unshift(selectedRow);
        selectedRow.seqNumber = +(
          Math.trunc(firstRow.seqNumber) -
          this.gap +
          '.' +
          getDecimalPart(selectedRow.seqNumber)
        );
        this.rowsToBeMovedAndSaved.push(selectedRow);
      } else if (newIndex >= data.length - 1) {
        // Move to the bottom
        const lastRow = data[data.length - 1];
        data.splice(currentIndex, 1);
        data.push(selectedRow);
        selectedRow.seqNumber = +(
          Math.trunc(lastRow.seqNumber) +
          this.gap +
          '.' +
          getDecimalPart(selectedRow.seqNumber)
        );
        this.rowsToBeMovedAndSaved.push(selectedRow);
      } else {
        data.splice(currentIndex, 1);
        data.splice(newIndex, 0, selectedRow);
        selectedRow.seqNumber = +(
          Math.trunc(
            (Math.trunc(data[newIndex - 1].seqNumber) +
              Math.trunc(data[newIndex + 1].seqNumber)) /
              2
          ) +
          '.' +
          getDecimalPart(selectedRow.seqNumber)
        );
        this.rowsToBeMovedAndSaved.push(selectedRow);
      }

      this.logService.gui('Moving rows', [{
        method: 'moveSelectedRow',
      }]);
    }
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.tableDataSource.filter = filterValue.trim().toUpperCase();
    this.logService.gui('Applied filter', [{
      method: 'applyFilter',
    }]);
  }

  @ViewChild(MatMenuTrigger) menuTrigger: MatMenuTrigger;
  @ViewChild('rowContextMenu') rowContextMenu: MatMenu;
  @ViewChild('headerMenu') headerMenu: MatMenu;
  @ViewChild('expandedRowContextMenu') expandedRowContextMenu: MatMenu;

  rowContextMenuPosition = { x: '0px', y: '0px' };
  headerMenuPosition = { x: '0px', y: '0px' };
  expandedRowContextMenuPosition = { x: '0px', y: '0px' };
  expandedRow = null as any;
  editingRow = null as any;
  editingProductRow = null as any;
  originalEditingRow = null as any;
  originalEditingProductRow = null as any;
  toggleRow(event, row: any) {
    event.stopPropagation();
    row.expanded = !row.expanded;
    if (this.expandedRow === row) {
      this.expandedRow = null;
      this.cd.detectChanges();
      return;
    }

    this.lotsService
      .getLotsByProductionLot(row.productionLot, row.lotSequence)
      .subscribe((data) => {
        row.details = data;
        this.expandedRow = row;
      });
  }

  editRow(event, row: any) {
    event.stopPropagation();
    
    const startTime:any = new Date();

    if (this.editingRow === row) {
      this.lotsService.postLotSave(row).subscribe({
        next: (result) => {
          const endTime:any = new Date();

          this.snackBar.open('The row was updated successfully!', 'ok', {
            duration: 5000,
          });
          this.logService.debug('Updating row successfully!', [{
            method: 'editRow',
            duration: endTime - startTime,
          }]);
          this.editingRow = null;
          this.cd.detectChanges();
        },
        error: (err) => {
          const endTime:any = new Date();

          if (err?.status === 200) {
            this.snackBar.open('The row was updated successfully!', 'ok', {
              duration: 5000,
            });
            this.logService.debug('Updating row successfully!', [{
              method: 'editRow',
              duration: endTime - startTime,
            }]);
            this.editingRow = null;
            this.cd.detectChanges();
          } else {
            this.logService.fatal('Updating a row failed error:' + err, [{
              method: 'editRow',
              duration: endTime - startTime,
            }]);
            this.snackBar.open('Updating row failed!', 'ok', {
              duration: 5000,
            });
          }
        },
        complete: () => {
          this.isSavingRow = false;
        },
      });
    } else {
      this.editingRow = row;
      this.originalEditingRow = JSON.parse(JSON.stringify(row));
    }
  }

  resetRow(event, row: any) {
    event.stopPropagation();
    Object.assign(row, this.originalEditingRow);
    this.editingRow = null;
    this.logService.gui('Reset row', [{
      method: 'resetRow',
    }]);
  }

  editProductRow(event, row: any) {
    event.stopPropagation();
    this.logService.gui('Edit product row', [{
      method: 'editProductRow',
    }]);

    const startTime:any = new Date();

    if (this.editingProductRow === row) {
      let dialogRef: any;
      const data = {
        message: 'Do you want to update the status from SENT to WAITING?',
        confirmationContext: 'Update status',
        buttonText: { ok: 'Ok', cancel: 'Cancel' },
        confirmData: true,
      }

      if (this.originalEditingProductRow.sendStatus === 'SENT') {
        dialogRef = this.dialog.open(ConfirmationPopupComponent, { data });
      }
      else {
        dialogRef = this.dialog.open(ConfirmationPopupComponent, {
          data: { ...data, message: 'Save the change', confirmationContent: 'Do you confirm to save the changes?' }
        });
      }

      dialogRef.afterClosed().subscribe((result) => {
        if (result) {
          // user clicked 'Yes'
          this.lotsService.postSaveProductSendStatus(row).subscribe({
            next: (result) => {
              const endTime:any = new Date();

              this.snackBar.open(
                'The product send status was updated successfully!',
                'ok',
                {
                  duration: 5000,
                }
              );
              this.logService.debug(
                'Updating a product send status successfully!', [{
                  method: 'editProductRow',
                }]
              );
              this.editingProductRow = null;
              this.cd.detectChanges();
            },
            error: (err) => {
              const endTime:any = new Date();

              if (err?.status === 200) {
                this.snackBar.open(
                  'The product send status was updated successfully!',
                  'ok',
                  {
                    duration: 5000,
                  }
                );
                this.logService.debug(
                  'Updating a product send status successfully!', [{
                    method: 'editProductRow',
                    duration: endTime - startTime,
                  }]
                );
                this.editingProductRow = null;
                this.cd.detectChanges();
              } else {
                this.logService.fatal(
                  'Updating a product send status failed error: ' + err,
                  [{
                    method: 'editProductRow',
                    duration: endTime - startTime,
                  }]
                );
                this.snackBar.open(
                  'Updating a product send status failed!',
                  'ok',
                  {
                    duration: 5000,
                  }
                );
              }
            },
            complete: () => {
              this.isSavingRow = false;
            },
          });
        } else {
          // user clicked 'No' or clicked outside the dialog
          this.resetProductRow(event, row);
        }
      });
    } else {
      this.editingProductRow = row;
      this.originalEditingProductRow = JSON.parse(JSON.stringify(row));
      this.logService.gui('Edit product row', [{
        method: 'editProductRow',
      }]);
    }
  }

  resetProductRow(event, row: any) {
    event.stopPropagation();
    Object.assign(row, this.originalEditingProductRow);
    this.editingProductRow = null;
    this.logService.gui('Reset product row', [{
      method: 'resetProductRow',
    }]);
  }

  onSendStatusChange(event) {
    this.editingProductRow.sendStatus = event.value;
  }

  isExpanded(index: number, row: any) {
    return this.expandedRow === row;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    this.highlightedRows = [];
  }

  setMenuAndOpen(menu: MatMenu, event: MouseEvent, item: any) {
    if (this.isUserInRole('lot_sequence_admin')) {
      event.preventDefault();
      this.highlightedRows = [item];
      this.rowContextMenuPosition.x = event.clientX + 'px';
      this.rowContextMenuPosition.y = event.clientY + 'px';
      this.menuTrigger.menuData = { item };
      if (menu) {
        this.menuTrigger.menu = menu;
        this.menuTrigger.menu.focusFirstItem('mouse');
      }
      this.menuTrigger.openMenu();
    }
  }

  onMoveUpToTopAction() {
    if (this.rowSelection.isEmpty()) {
      return;
    }
    const firstSelectedItem = this.rowSelection.selected[0];
    const previousIndex = this.tableDataSource.data.findIndex(
      (row) => row === firstSelectedItem
    );
    if (previousIndex === 0) {
      return;
    }
    this.moveSelectedRows(-previousIndex);
    this.tableDataSource.data = [...this.tableDataSource.data];
    this.basicTable.renderRows();
    this.logService.gui('Move up to top', [{
      method: 'onMoveUpToTopAction',
    }]);
  }

  onMoveDownToBottomAction() {
    if (this.rowSelection.isEmpty()) {
      return;
    }
    const lastSelectedItem =
      this.rowSelection.selected[this.rowSelection.selected.length - 1];
    const previousIndex = this.tableDataSource.data.findIndex(
      (row) => row === lastSelectedItem
    );
    if (previousIndex === this.tableDataSource.data.length - 1) {
      return;
    }
    this.moveSelectedRows(this.tableDataSource.data.length - previousIndex);
    this.tableDataSource.data = [...this.tableDataSource.data];
    this.basicTable.renderRows();
    this.logService.gui('Move down to bottom', [{
      method: 'onMoveDownToBottomAction',
    }]);
  }

  onMoveUpAction(event) {
    event.stopPropagation();
    if (this.rowSelection.isEmpty()) {
      return;
    }
    const firstSelectedItem = this.rowSelection.selected[0];
    const previousIndex = this.tableDataSource.data.findIndex(
      (row) => row === firstSelectedItem
    );
    if (previousIndex === 0) {
      return;
    }
    this.moveSelectedRows(-1);
    this.tableDataSource.data = [...this.tableDataSource.data];
    this.basicTable.renderRows();
    this.logService.gui('Move up', [{
      method: 'onMoveUpAction',
    }]);
  }

  onMoveDownAction(event) {
    event.stopPropagation();
    if (this.rowSelection.isEmpty()) {
      return;
    }
    const lastSelectedItem =
      this.rowSelection.selected[this.rowSelection.selected.length - 1];
    const previousIndex = this.tableDataSource.data.findIndex(
      (row) => row === lastSelectedItem
    );
    if (previousIndex === this.tableDataSource.data.length - 1) {
      return;
    }
    this.moveSelectedRows(1);
    this.tableDataSource.data = [...this.tableDataSource.data];
    this.basicTable.renderRows();
    this.logService.gui('Move down', [{
      method: 'onMoveDownAction',
    }]);
  }

  onCopyContentAction(item: any) {
    copyToClipboard(JSON.stringify(item));
    this.logService.gui('Copy content', [{
      method: 'onCopyContentAction',
    }]);
  }

  onCutAction(item: any) {
    this.rowSelection.clear();
    this.rowSelection.select(item);
    this.logService.gui('Cut', [{
      method: 'onCutAction',
    }]);
  }

  // The argument positionOffset,
  // if its value is -1, it will calculate the offset to the row above the target row
  // if its value is 1, it will calculate the offset to the row below the target row
  // by default, it will calculate the offset to the row below the target row
  calculatePasteOffset(targetRow: any, positionOffset: number = 1) {
    const targetRowIndex = this.tableDataSource.data.indexOf(targetRow);
    const lengthOfSelectedRows = this.rowSelection.selected.length;
    let firstSelectedRowIndex = Number.POSITIVE_INFINITY;

    this.rowSelection.selected.forEach((row) => {
      const currentSelectedRowIndex = this.tableDataSource.data.indexOf(row);

      if (currentSelectedRowIndex < firstSelectedRowIndex) {
        firstSelectedRowIndex = currentSelectedRowIndex;
      }
    });

    if (
      lengthOfSelectedRows === 0 ||
      firstSelectedRowIndex === Number.POSITIVE_INFINITY
    ) {
      return 0;
    }

    let offset = targetRowIndex - firstSelectedRowIndex + positionOffset;

    if (offset === 1) {
      offset -= 1;
    }

    if (offset === -1) {
      offset += 1;
    }

    if (offset > 1 && positionOffset === 1) {
      offset--;
    }

    if (offset < -1 && positionOffset === -1) {
      offset++;
    }

    return offset;
  }

  onPasteAboveAction(item: any) {
    const offset = this.calculatePasteOffset(item, -1);
    this.moveSelectedRows(offset);
    this.logService.gui('Paste above', [{
      method: 'onPasteAboveAction',
    }]);
  }

  onPasteBelowAction(item: any) {
    const offset = this.calculatePasteOffset(item, 1);
    this.moveSelectedRows(offset);
    this.logService.gui('Paste below', [{
      method: 'onPasteBelowAction',
    }]);
  }

  onFreezeLotsAction() {
    this.freezeSelectedRows();
    this.logService.gui('Freeze lots', [{
      method: 'onFreezeLotsAction',
    }]);
  }

  onMoveBehindLotAction(item: any) {
    this.moveBehindLot(item.productionLot);
    this.logService.gui('Move behind lot: ' + item, [{
      method: 'onMoveBehindLotAction',
    }]);
  }

  onUnfreezeLotsAction() {
    this.unfreezeSelectedRows();
    this.logService.gui('Unfreeze lots', [{
      method: 'onUnfreezeLotsAction',
    }]);
  }

  onSendToWeldOnAction(item: WeldScheduleModel) {
    this.sendToWeldHighlighted(item);
    this.logService.gui('Send to weld on', [{
      method: 'onSendToWeldOnAction',
    }]);
  }

  onCreateANewLotAction(event) {
    if (this.planCode) {
      this.logService.gui('Create a new lot', [{
        method: 'onCreateANewLotAction',
      }]);

      this.lotsService
        .getAddNewCode(this.planCode, this.selectedProcessLocation)
        .subscribe((item) => {
          const newItem = Object.assign({}, item);
          newItem.createTimestamp = new Date().toLocaleDateString();
          newItem.productionDate = new Date();
          newItem.createTimestamp = new Date().toLocaleDateString();
          newItem.productionDate = new Date();
          const dialogRef = this.dialog.open(RowFormDialogComponent, {
            width: '850px',
            data: { ...newItem, commentsOptions: this.commentsOptions },
          });

          dialogRef.afterClosed().subscribe((result) => {
            if (result) {
              this.onRefreshTable.emit({
                planCode: this.planCode,
                processLocation: this.selectedProcessLocation,
              });
            }
          });
        });
    }
  }

  onSelectLastLotAction() {
    this.rowSelection.select(
      this.tableDataSource.data[this.dataSource.length - 1]
    );
    this.logService.gui('Select last lot', [{
      method: 'onSelectLastLotAction',
    }]);
  }

  onPopUpBottomSheetAction(event, item: any) {
    this.bottomSheet.open(ExpandRowBottomSheetComponent);
    this.logService.gui('Pop up bottom sheet', [{
      method: 'onPopUpBottomSheetAction',
    }]);
  }

  onExpandRowDialogAction(event, item: any) {
    const dialogRef = this.dialog.open(ExpandRowDialogComponent, {
      width: '650px',
      data: item,
    });

    dialogRef.afterClosed().subscribe((result) => {
      this.logService.gui('The dialog was closed, the result is ' + result, [{
        method: 'onExpandRowDialogAction'
      }]);
    });
    
    this.logService.gui('Expand row dialog', [{
      method: 'onExpandRowDialogAction',
    }]);
  }

  onEditRowFormDialogAction(event, item: any) {
    const dialogRef = this.dialog.open(RowFormDialogComponent, {
      width: '850px',
      data: { ...item, commentsOptions: this.commentsOptions },
    });
    
    this.rowOnEditing = item;

    dialogRef.afterClosed().subscribe((result) => {
      Object.assign(this.rowOnEditing, result);
    });

    this.logService.gui('Edit row form dialog', [{
      method: 'onEditRowFormDialogAction',
    }]);
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllRowsSelected() {
    const numSelected = this.rowSelection.selected.length;
    const numRows = this.tableDataSource.data.length;
    return numSelected == numRows;
  }

  isAllColumnsSelected() {
    const numSelected = this.columnsSelection.selected.length;
    const numColumns = this.selectableColumns.length;
    return numSelected === numColumns;
  }

  isAllModelsSelected() {
    const numSelected = this.modelsSelection.selected.length;
    const numModels = this.modelList.length;

    if (numSelected === numModels) {
      this.filterValues.model = [...this.modelsSelection.selected];
    }

    return numSelected === numModels;
  }

  /** Selects all rows if they are not all selected; otherwise clear rowSelection. */
  toggleAllRows() {
    this.isAllRowsSelected()
      ? this.rowSelection.clear()
      : this.tableDataSource.data.forEach((row) =>
          this.rowSelection.select(row)
        );
    this.logService.gui('Toggle all rows', [{
      method: 'toggleAllRows',
    }]);
  }

  toggleAllColumns() {
    if (this.isAllColumnsSelected()) {
      this.columnsSelection.clear();
      this.columns.forEach((column, i) => {
        if ([0, 1, 12].includes(i)) {
          column.isShown = true;
        } else {
          column.isShown = false;
        }
      });
      this.displayedColumns = [...this.columns.map((c) => c.columnDef)].slice(
        0,
        2
      );
      this.displayedColumns = ['select', 'position', 'editing'];
    } else {
      this.displayedColumns = [...this.columns.map((c) => c.columnDef)];
      this.selectableColumns.forEach((column) =>
        this.columnsSelection.select(column)
      );
    }

    this.logService.gui('Toggle all columns', [{
      method: 'toggleAllColumns',
    }]);
  }

  toggleAllModels() {
    if (this.isAllModelsSelected()) {
      this.modelsSelection.clear();
      this.selectedModelList = [
        ...this.modelList.map((m) => m.trim().toLowerCase()),
      ];
      this.modelFilter.setValue([]);
    } else {
      this.selectedModelList = [
        ...this.modelList.map((m) => m.trim().toLowerCase()),
      ];
      this.selectedModelList.forEach((model) =>
        this.modelsSelection.select(model)
      );
      this.modelFilter.setValue(this.selectedModelList);
    }

    this.logService.gui('Toggle all models', [{
      method: 'toggleAllModels',
    }]);
  }

  drop(event: CdkDragDrop<any[]>): void {
    const previousIndex = this.tableDataSource.data.findIndex(
      (row) => row === event.item.data
    );
    const newIndex = event.currentIndex;
    const offset = newIndex - previousIndex;
    this.moveSelectedRows(offset);

    this.logService.gui('Drop', [{
      method: 'drop',
    }]);
  }

  private isUserInRole(roles: string): boolean {
    return this.securityService.isUserInRole(roles);
  }

  setExpandedMenuAndOpen(menu: MatMenu, event: MouseEvent, item: any) {
    if ((this.isUserInRole('lot_sequence_admin') || this.isUserInRole('lot_sequence_sendtoweon')) && this.tableTitle === 'Frozen Lots') {
      event.preventDefault();
      this.highlightedRows = [item];
      this.rowContextMenuPosition.x = event.clientX + 'px';
      this.rowContextMenuPosition.y = event.clientY + 'px';
      this.menuTrigger.menuData = { item };
      if (menu) {
        this.menuTrigger.menu = menu;
        this.menuTrigger.menu.focusFirstItem('mouse');
      }
      this.menuTrigger.openMenu();
    }
  }
}
