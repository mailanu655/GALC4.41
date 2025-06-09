import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SortDir } from 'src/app/lib/constants';
import { AppService } from 'src/app/services/app.service';
import { LcTableComponent } from '../lc-table/lc-table.component';
import { Product } from 'src/app/services/model';

@Component({
    selector: 'products',
    templateUrl: '../lc-table/lc-table.component.html',
    styleUrls: ['../lc-table/lc-table.component.css', 'products.component.css']
})
export class ProductsComponent extends LcTableComponent<Product, string, any> {
    constructor(snackBar: MatSnackBar, private appService: AppService) {
        super(snackBar);
        if (this.appService.admin) {
            this.columns.push(
                { header: 'Prod Seq', name: 'sequence' },
                { header: 'Loc Seq', name: 'locationSequence' },
                { header: 'Loc Id', name: 'assignedLocationId' },
                { header: 'Process Point@Loc', name: 'ppAtLoc' },
                { header: 'Last Process Point', name: 'lastProcessPointId' }
            );
        }
    }

    init() {
        this.columns = [
            { header: '#', name: '#' },
            { header: 'VIN', name: 'id', link: "products/:id", params: ["id"], target: '_blank' },
            { header: 'AFON Seq', name: 'afOnSequence' },
            { header: 'AFON Time', name: 'afOnTs', formatDate: true },
            { header: 'Model', name: 'model' },
            { header: 'Spec Code', name: 'specCode' },
            { header: 'Prod Lot', name: 'prodLot' },
            { header: 'Kd Lot', name: 'kdLot' },
            { header: 'EIN', name: 'ein' },
            { header: 'Ex Color', name: 'extColor' },
            { header: 'Lot Size', name: 'lotSize' },
            { header: 'Kd Lot Size', name: 'kdLotSize' },
            { header: 'Location', name: 'locationCode' }
        ];
        this.keepSelectionAfterRefresh = true;
        this.equalsByPropertyName = 'id';
        this.defaultSortBy = 'afOnTs';
        this.defaultSortDir = SortDir.DESC.name;
    }
}