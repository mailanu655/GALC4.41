import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SortDir } from 'src/app/lib/constants';
import { ConfigService } from 'src/app/services/config.service';
import { LcTableComponent } from '../lc-table/lc-table.component';
import { PartIssue } from 'src/app/services/model';

@Component({
    selector: 'part-issues',
    templateUrl: '../lc-table/lc-table.component.html',
    styleUrls: ['../lc-table/lc-table.component.css', './part-issues.component.css']
})
export class PartIssuesComponent extends LcTableComponent<PartIssue, number, any> {

    constructor(private configService: ConfigService, snackBar: MatSnackBar) {
        super(snackBar);
        this.initTable();
    }

    init() {
        this.keepSelectionAfterRefresh = true;
        this.equalsByPropertyName = 'id';
        this.defaultSortBy = 'afOnTs';
        this.defaultSortDir = SortDir.DESC.name;
    }

    initTable() {
        let productUrl: string = "/" + this.configService.siteId + "/products/:productId";
        this.columns = [
            { header: '#', name: '#' },
            { header: 'VIN', name: 'productId', link: productUrl, params: ["productId"], target: '_blank' },
            { header: 'AFON Seq', name: 'afOnSequence' },
            { header: 'Model', name: 'model' },
            { header: 'Part Name', name: 'partName' },
            { header: 'Status', name: 'status' },
            { header: 'Process Point Id', name: 'processPointId' },
            { header: 'Process Point Name', name: 'processPoint.name' },
        ];
    }
}
