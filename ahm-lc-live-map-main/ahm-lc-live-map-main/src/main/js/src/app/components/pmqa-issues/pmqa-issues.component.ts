import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SortDir } from 'src/app/lib/constants';
import { ConfigService } from 'src/app/services/config.service';
import { LcTableComponent } from '../lc-table/lc-table.component';
import { PMQAIssue, PartIssue } from 'src/app/services/model';

@Component({
    selector: 'pmqa-issues',
    templateUrl: '../lc-table/lc-table.component.html',
    styleUrls: ['../lc-table/lc-table.component.css', './pmqa-issues.component.css']
})
export class PMQAIssuesComponent extends LcTableComponent<PMQAIssue, number, any> {

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
            { header: 'Part Name', name: 'partName' },
            { header: 'Status', name: 'status' },
            { header: 'Process Point', name: 'processPointId' },
        ];
    }
}
