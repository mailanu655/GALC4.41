import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SortDir } from 'src/app/lib/constants';
import { ConfigService } from 'src/app/services/config.service';
import { Defect } from 'src/app/services/model';
import { LcTableComponent } from '../lc-table/lc-table.component';

@Component({
    selector: 'defects',
    templateUrl: '../lc-table/lc-table.component.html',
    styleUrls: ['../lc-table/lc-table.component.css', './defects.component.css']
})
export class DefectsComponent extends LcTableComponent<Defect, number, any> {

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
            { header: 'Type', name: 'defectType' },
            { header: 'Status', name: 'defectStatus' },
            { header: 'Resp Dept', name: 'responsibleDept' },
            { header: 'Resp Zone', name: 'responsibleZone' },
            { header: 'Entry Dept', name: 'entryDeptCode' },
            { header: 'Entry Time', name: 'actualTimestamp', formatDate: true },
            { header: 'Associate No', name: 'associateNo' }
        ];
    }
}
