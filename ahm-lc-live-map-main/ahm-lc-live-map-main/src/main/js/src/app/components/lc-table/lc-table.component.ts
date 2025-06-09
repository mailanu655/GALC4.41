import { OnInit, Input, ViewChild, Output, EventEmitter, Component, Inject, ElementRef } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BaseComponent } from 'src/app/lib/base-component';
import { RestReadService } from 'src/app/lib/rest-read.service';
import { ObjectUtilities } from 'src/app/lib/common-object';
import { EntityProperty, SortDir } from 'src/app/lib/constants';

export class ColumnDef {
    header: string;
    name: string;
    sort?: boolean;
    formatDate?: boolean;
    format?: string | null;
    link?: string | null;
    params?: string[] | null;
    href?: string | null;
    target?: string | null;
}

@Component({
    selector: 'lc-table',
    templateUrl: './lc-table.component.html',
    styleUrls: ['./lc-table.component.css']
})
export class LcTableComponent<T, K, S extends RestReadService<T, K>> extends BaseComponent implements OnInit {

    @ViewChild('tableContainer')
    tableContainer: ElementRef<HTMLElement>;

    @ViewChild(MatSort, { static: true })
    sort: MatSort;

    @Input()
    columns: ColumnDef[] = [
        { header: "#", name: "#" },
        { header: "Id", name: "id" },
        { header: "Name", name: "name" },
        { header: "Obj Version", name: "objVersion" },
        { header: "Created", name: "createTimestamp", formatDate: true },
        { header: "Updated", name: "updateTimestamp", formatDate: true }
    ];

    private _service: S;

    @Input()
    set service(service: S) {
        this._service = service;
    }

    get service(): S {
        return this._service;
    }

    @Input()
    set tableData(data: Array<T>) {
        if (!data) {
            data = [];
        }
        if (this.dataSource) {
            this.dataSource.data = data;
            this.setSelectedItem();
        }
    }

    get tableData() {
        if (this.dataSource && this.dataSource.data) {
            return this.dataSource.data;
        } else {
            return [];
        }
    }

    @Output()
    onSelectItem: EventEmitter<T> = new EventEmitter<T>();

    @Output()
    mouseOverRow: EventEmitter<Event> = new EventEmitter<Event>();

    @Output()
    mouseOutRow: EventEmitter<Event> = new EventEmitter<Event>();

    @Input()
    /**
     {colName: {value1: class1, value2: class2, default: defaultClass}[, colName2: {v1: c1, default: defaultClass}, ...] } 
     */
    colValueClassMapping: any;

    @Input()
    equalsByPropertyName: string;

    @Input()
    defaultSortBy: string;

    @Input()
    defaultSortDir: string;

    @Input()
    keepSelectionAfterRefresh: boolean;

    comparator: any;

    dataSource: MatTableDataSource<T>;

    selectedItem: T;

    constructor(snackBar: MatSnackBar) {
        super(snackBar);
        this.dataSource = new MatTableDataSource<T>();
        this.dataSource.sortingDataAccessor = (item, propertName) => {
            return this.getProperty(item, propertName);
        };
        this.init();
    }

    // === init === //
    init() {
        this.equalsByPropertyName = 'id';
        this.defaultSortBy = EntityProperty.ID.name;
        this.defaultSortDir = SortDir.ASC.name;
    }

    ngOnInit() {
        //TODO investigate
        this.dataSource.sort = this.sort;
        this.createComparator();
        // REMARK , if service is provided then data is loaded by service
        // if service is not provided then data is expected to be provided by parent by inptData parameter
        if (this.service != null) {
            this.findData();
        }
    }

    createComparator() {
        this.comparator = ObjectUtilities.createComparator(this.defaultSortBy, this.defaultSortDir);
    }

    isDataExists(): boolean {
        if (this.dataSource.data && this.dataSource.data.length > 0) {
            return true;
        } else {
            return false;
        }
    }

    // === action handlers === //
    onRefresh(): void {
        this.dataSource.filter = null!;
        if (this.service) {
            this.findData();
        }
    }

    onSelectedRow(item: any): void {
        if (this.selectedItem === item) {
            this.selectedItem = null!;
        } else {
            this.selectedItem = item;
        }
        this.onSelectItem.emit(this.selectedItem);
    }

    setSelectedItem(selectedItem?: T) {
        if (selectedItem) {
            this.selectedItem = selectedItem;
        }
        if (this.keepSelectionAfterRefresh && this.selectedItem) {
            let item = null;
            if (this.tableData) {
                item = this.tableData.find(item => ObjectUtilities.equalsByProperty(item, this.selectedItem, this.equalsByPropertyName));
            }
            this.selectedItem = item!;
        } else {
            this.selectedItem = null!;
        }
        this.onSelectItem.emit(this.selectedItem);
    }

    onMouseOverRow(event: Event) {
        this.mouseOverRow.emit(event);
    }

    onMouseOutRow(event: Event) {
        this.mouseOutRow.emit(event);
    }

    // === data === //
    findData(params?: any): void {
        this.beforeExecute();
        this.service.findAll().subscribe({
            next: res => this.afterFindData(res),
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }

    afterFindData(data: Array<T>) {
        if (!data) {
            data = [];
        }
        if (this.getComparator()) {
            data.sort(this.getComparator());
        }
        this.tableData = data;
    }

    getCssClass(element: any, propertyName: string): string {
        let cellClass = null;
        if (element && element[propertyName + 'Class']) {
            cellClass = element[propertyName + 'Class'];
            return cellClass;
        }
        if (propertyName && this.colValueClassMapping) {
            let colMapping = this.colValueClassMapping[propertyName];
            if (colMapping) {
                let value: any = this.getProperty(element, propertyName);
                cellClass = colMapping[value];
                if (!cellClass) {
                    cellClass = colMapping['default'];
                }
            }
        }
        return cellClass;
    }

    filter(filterValue: string) {
        //this.dataSource.filter = filterValue.trim().toLowerCase();
        this.dataSource.filter = filterValue.toLowerCase();
    }

    // === href utility === //
    isLink(link: string, params: string[], item?: T | null): boolean {
        if (link || (params && params.length > 0)) {
            return true;
        } else {
            return false;
        }
    }

    getUrl(item: any, link: string, params: string[]): string {
        let url = '';
        if (link) {
            url = link;
        }
        let queryParams = {};
        if (params && params.length > 0 && item) {
            for (let param of params) {
                let value = this.getProperty(item, param);
                if (url.indexOf(":" + param) > -1) {
                    url = url.replace(":" + param, value);
                } else if (param) {
                    queryParams[param] = value;
                }
            }
            if (Object.keys(queryParams).length > 0) {
                item['queryParams'] = queryParams;
            }
        }
        return url;
    }

    // === get/set === //
    getComparator() {
        return this.comparator;
    }

    getColumnNames(): string[] {
        let colNames: string[] = [];
        for (let col of this.columns) {
            colNames.push(col.name);
        }
        return colNames;
    }

    get tableElement(): HTMLTableElement {
        return this.tableContainer.nativeElement.getElementsByTagName('table').item(0)!;
    }
}