import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SortDir } from 'src/app/lib/constants';
import { ConfigService } from 'src/app/services/config.service';
import { LcTableComponent } from '../lc-table/lc-table.component';
import { Product } from 'src/app/services/model';

@Component({
    selector: 'stragglers',
    templateUrl: '../lc-table/lc-table.component.html',
    styleUrls: ['../lc-table/lc-table.component.css', './stragglers.component.css']
})
export class StragglersComponent extends LcTableComponent<Product, string, any> {

    constructor(private configService: ConfigService, snackBar: MatSnackBar) {
        super(snackBar);
        this.initTable();
    }

    // === override === //
    init() {
        this.keepSelectionAfterRefresh = true;
        this.equalsByPropertyName = 'id';
        this.defaultSortBy = 'afOnTs';
        this.defaultSortDir = SortDir.DESC.name;
    }

    initTable() {
        let productUrl: string = "/" + this.configService.siteId + "/products/:id";
        this.columns = [
            { header: '#', name: '#' },
            { header: 'VIN', name: 'id', link: productUrl, params: ["id"], target: '_blank' },
            { header: 'AFON Seq', name: 'afOnSequence' },
            { header: 'AFON Time', name: 'afOnTs', formatDate: true },
            { header: 'Status', name: 'lineInfo' },
            { header: 'WEON Time', name: 'weOnTs', formatDate: true },
            { header: 'Model', name: 'model' },
            { header: 'Spec Code', name: 'specCode' },
            { header: 'Prod Lot', name: 'prodLot' },
            { header: 'Kd Lot', name: 'kdLot' },
            { header: 'Remake', name: 'remake' },
            { header: 'EIN', name: 'ein' },
            { header: 'Ex Color', name: 'extColor' },
            { header: 'Lot Size', name: 'lotSize' },
            { header: 'Kd Lot Size', name: 'kdLotSize' }
        ];
    }
}
