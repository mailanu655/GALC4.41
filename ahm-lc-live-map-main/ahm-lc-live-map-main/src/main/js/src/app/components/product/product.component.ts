import { Component, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTabGroup } from '@angular/material/tabs';
import { ActivatedRoute, Router } from '@angular/router';
import { BaseComponent } from 'src/app/lib/base-component';
import { CollectionUtilities } from 'src/app/lib/common-collection';
import { EXCEL_EXTENSION, Export } from 'src/app/lib/common-export';
import { ObjectUtilities } from 'src/app/lib/common-object';
import { HoldType, InstalledPartStatus, SortDir } from 'src/app/lib/constants';
import { MessageBundle, messages } from 'src/app/lib/messages';
import { ConfigService } from 'src/app/services/config.service';
import { DataService } from 'src/app/services/data-service';
import { Hold, PMQAIssue, PartIssue, Product } from 'src/app/services/model';
import { PartIssueService } from 'src/app/services/part-issue.service';
import { PMQAIssueService } from 'src/app/services/pmqa-issue.service';
import { Parameter, Tabs } from '../map/map.component';

@Component({
    selector: 'product',
    templateUrl: './product.component.html',
    styleUrls: ['./product.component.css']
})
export class ProductComponent extends BaseComponent {

    @ViewChild('tabs') tabs: MatTabGroup;

    messages: MessageBundle = messages;

    product: Product = new Product();
    backUrl: string;

    historyColumns = [
        { header: '#', name: '#' },
        { header: 'Process Point Id', name: 'id.processPointId' },
        { header: 'Process Point Name', name: 'processPoint.name' },
        { header: 'Line Id', name: 'line.id' },
        { header: 'Line Name', name: 'line.name' },
        { header: 'Dept Id', name: 'department.id' },
        { header: 'Dept Name', name: 'department.name' },
        { header: 'Process Time', name: 'id.processTs', formatDate: true }
    ];

    defectcolumns = [
        { header: '#', name: '#' },
        { header: 'Part Name', name: 'partName' },
        { header: 'Type', name: 'defectType' },
        { header: 'Status', name: 'defectStatus' },
        { header: 'Resp Dept', name: 'responsibleDept' },
        { header: 'Resp Zone', name: 'responsibleZone' },
        { header: 'Entry Dept', name: 'entryDeptCode' },
        { header: 'Entry Time', name: 'actualTimestamp' },
        { header: 'Associate No', name: 'associateNo' },
        { header: 'Def Id', name: 'id' }
    ];

    partIssueColumns = [
        { header: '#', name: '#' },
        { header: 'Part Name', name: 'partName' },
        { header: 'Status', name: 'status' },
        { header: 'Process Point Id', name: 'processPointId' },
        { header: 'Process Point Name', name: 'processPoint.name' },
    ];

    holdsColumns = [
        { header: '#', name: '#' },
        { header: 'Reason', name: 'reason' },
        { header: 'Process Point', name: 'processPointId' },
        { header: 'Hold TS', name: 'processTs', formatDate: true },
        { header: 'Type', name: 'type' },
        { header: 'Associate No', name: 'associateId' },
        { header: 'Associate Name', name: 'associateName' },
        { header: 'Associate Phone', name: 'associatePhone' },
        { header: 'Associate Pager', name: 'associatePager' },
        { header: 'Lot Hold', name: 'lotHoldStatusId' },
        { header: 'QSR', name: 'qsrId' },
        { header: 'Equipment', name: 'equipmentFlag' }
    ];

    selectedTabIx: number = 0;

    constructor(protected config: ConfigService, public dataService: DataService, protected partIssueService: PartIssueService, protected route: ActivatedRoute, private router: Router, snackBar: MatSnackBar, protected pmqaIssueService: PMQAIssueService) {
        super(snackBar);
    }
    ngOnInit() {
        let site = this.route.snapshot.paramMap.get(Parameter.SITE);
        this.config.siteId = site!;
        this.dataService.loadCacheIfEmpty();
        let id = this.route.snapshot.paramMap.get('id')!;
        this.findProduct(id);
        this.backUrl = "/" + this.config.siteId;
    }

    // === handlers === //
    print(): void {
        let tabName = this.selectedTabName;
        if (!tabName) {
            return;
        }
        let tableElement = this.getTableElement(tabName);
        if (!tableElement) {
            return;
        }
        let headerLine: string = this.config.site.name;
        headerLine = headerLine + "-" + tabName;
        headerLine = headerLine + "-" + this.product.id;
        Export.printHtml(headerLine, tableElement.outerHTML);
    }

    download(): void {
        let tabName = this.selectedTabName;
        if (!tabName) {
            return;
        }
        let fileName: string = this.config.site.name;
        fileName = fileName + "-" + tabName;
        fileName = fileName + "-" + this.product.id;
        fileName = fileName + "-" + Export.getTsToken();
        fileName = fileName.replace(" ", "_");
        fileName = fileName + EXCEL_EXTENSION;
        let tableElement = this.getTableElement(tabName);
        Export.exportTableToExcel(tableElement, fileName, tabName);
    }

    // === data === //
    findProduct(id: string) {
        this.dataService.findProduct(id).subscribe({
            next: res => {
                if (res) {
                    this.product = res;
                    if (this.product && this.product.id) {
                        this.selectHistory(this.product.id);
                        this.selectDefects(this.product.id);
                        this.selectPartIssues(this.product.id);
                        this.selectHolds(this.product.id);
                    }
                }
            },
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }

    selectHistory(productId: string) {
        this.dataService.selectHistoryByProductId(productId).subscribe({
            next: res => {
                if (res) {
                    this.product['history'] = res;
                    this.afterSelectHistory();
                }
            },
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }

    selectDefects(productId: string) {
        this.dataService.selectDefectsByProductId(productId).subscribe({
            next: res => {
                if (res) {
                    this.product.defects = res;
                    this.product.defects.sort(ObjectUtilities.createComparator("partName"));
                    this.afterSelectDefects();
                }
            },
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }

    selectPartIssues(productId: string) {
        this.partIssueService.findAll({ productId: productId }).subscribe({
            next: res => {
                this.afterSelectPartIssues(res);
            },
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }

    selectPmqaIssues(productId: string) {
        this.pmqaIssueService.findAll({ productId: productId }).subscribe({
            next: res => {
                this.afterSelectPmqaIssues(res);
            },
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }

    selectHolds(productId: string) {
        this.dataService.selectHoldsByProductId(productId).subscribe({
            next: res => {
                if (res) {
                    this.product.holds = res;
                    this.afterSelectHolds(this.product.holds);
                }
            },
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }
    // === data assembly === //
    afterSelectHistory() {
        let history = this.product['history'];
        history.sort(ObjectUtilities.createComparator("id.processTs", SortDir.DESC.name));
        for (let item of history) {
            let ppId = item.id.processPointId;
            if (ppId) {
                let pp = this.dataService.processPointCache.get(ppId.trim());
                if (pp && pp.name) {
                    item['processPoint'] = pp;
                }
                if (pp) {
                    let line = this.dataService.lineCache.get(pp.lineId);
                if (line) {
                    item['line'] = line;
                        let department = this.dataService.departmentCache.get(line.departmentId);
                    if (department) {
                        item['department'] = department;
                    }
                }
            }
        }
    }
    }

    afterSelectDefects() {
        this.product.defects.sort(ObjectUtilities.createComparator("partName"));
        for (let defect of this.product.defects) {
            if (defect.entryDept) {
                let entryDep = this.dataService.departmentCache.get(defect.entryDept);
                if (entryDep) {
                    defect['entryDeptCode'] = entryDep.code;
                } else {
                    defect['entryDeptCode'] = defect.entryDept;
                }
            }
        }
    }

    afterSelectPartIssues(issues: PartIssue[]) {
        let filtered: PartIssue[] = CollectionUtilities.filterByProperty(issues, "jobId", this.config.partIssueConfigurationIds);
        filtered = CollectionUtilities.filterOutByProperty(filtered, "statusId", [InstalledPartStatus.OK.id, InstalledPartStatus.REPAIRED.id]);
        filtered = CollectionUtilities.filterOutByProperty(filtered, "dismissed", [true]);
        let deduplicated: PartIssue[] = [];
        for (let pi of filtered) {
            if (deduplicated.find((item: PartIssue) => item.partName === pi.partName && item.productId === pi.productId)) {
                continue;
            }
            deduplicated.push(pi);
        }
        filtered = deduplicated;
        for (let pi of filtered) {
            let status = InstalledPartStatus.getById(pi.statusId);
            pi['status'] = status ? status.name : pi.statusId;
            let pp = this.dataService.processPointCache.get(pi.processPointId);
            if (pp) {
                pi['processPoint'] = pp;
            }
        }
        filtered.sort(ObjectUtilities.createComparator("partName"));
        this.product.partIssues = filtered;
    }

    afterSelectPmqaIssues(issues: PMQAIssue[]) {
        this.product.pmqaIssues = issues;
    }

    afterSelectHolds(holds: Hold[]) {
        for (let hold of holds) {
            let holdType: HoldType = HoldType.getById(hold.id.holdTypeId);
            if (holdType) {
                hold['type'] = holdType.toString();
            } else {
                hold['type'] = hold.id.holdTypeId;
            }
        }
        holds.sort(ObjectUtilities.createComparator("reason"));
    }
    // === event handlers === //
    onRefresh() {
        this.findProduct(this.product.id);
    }

    // === get/set === //
    get historyLabel(): string {
        return Tabs.HISTORY;
    }

    get qicsLabel(): string {
        return Tabs.DEFECTS;
    }

    get partsLabel(): string {
        return Tabs.PARTS;
    }

    get holdsLabel(): string {
        return Tabs.HOLDS;
    }

    get stragglersLabel(): string {
        return Tabs.STRAGGLERS;
    }

    get selectedTabName(): string | null {
        if (!this.tabs) {
            return null;
        }
        let tabArray = this.tabs._tabs.toArray();
        if (tabArray.length < 1) {
            return null;
        }
        let tabName = tabArray[this.selectedTabIx].textLabel;
        return tabName;
    }

    getTableElement(id: string): HTMLTableElement | null {
        let tabElement: HTMLElement | null = null;
        if (id) {
            tabElement = document.getElementById(id);
            if (tabElement) {
                let tableElements = tabElement.getElementsByTagName('table');
                if (tableElements && tableElements.length > 0) {
                    return tableElements.item(0);
                }
            }
        }
        return null;
    }
}
