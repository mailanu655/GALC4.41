import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SortDir } from 'src/app/lib/constants';
import { ConfigService } from 'src/app/services/config.service';
import { LcTableComponent } from '../lc-table/lc-table.component';
import { Hold } from 'src/app/services/model';


@Component({
    selector: 'holds',
    templateUrl: '../lc-table/lc-table.component.html',
    styleUrls: ['../lc-table/lc-table.component.css', './holds.component.css']
})
export class HoldsComponent extends LcTableComponent<Hold, any, any> {

    constructor(private configService: ConfigService, snackBar: MatSnackBar) {
        super(snackBar);
        this.initTable();
    }

    init() {
        this.keepSelectionAfterRefresh = true;
        //TODO ? verify 
        this.equalsByPropertyName = 'id';
        this.defaultSortBy = 'afOnTs';
        this.defaultSortDir = SortDir.DESC.name;
    }

    initTable() {
        let productUrl: string = "/" + this.configService.siteId + "/products/:id.productId";
        this.columns = [
            { header: '#', name: '#' },
            { header: 'VIN', name: 'id.productId', link: productUrl, params: ["id.productId"], target: '_blank' },
            { header: 'AFON Seq', name: 'afOnSequence' },
            { header: 'Model', name: 'model' },
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
            { header: 'Equipment', name: 'equipmentFlag' },
        ];
    }
}
