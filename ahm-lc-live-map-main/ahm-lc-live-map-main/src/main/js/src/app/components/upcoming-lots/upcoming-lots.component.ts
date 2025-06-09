import { Component } from '@angular/core';
import { SortDir } from 'src/app/lib/constants';
import { LcTableComponent } from '../lc-table/lc-table.component';
import { Lot } from 'src/app/services/model';

@Component({
    selector: 'upcoming-lots',
    templateUrl: '../lc-table/lc-table.component.html',
    styleUrls: ['../lc-table/lc-table.component.css', './upcoming-lots.component.css']
})
export class UpcomingLotsComponent extends LcTableComponent<Lot, string, any> {
    init() {
        this.columns = [
            { header: '#', name: '#' },
            { header: 'Prod Lot', name: 'id' },
            { header: 'Kd Lot', name: 'kdLot' },
            { header: 'Sequence', name: 'lotSequence' },
            { header: 'Model', name: 'model' },
            { header: 'Spec Code', name: 'specCode' },
            { header: 'Lot Size', name: 'size' },
            { header: 'Kd Lot Size', name: 'kdLotSize' },
            { header: 'Remake', name: 'remake' }
        ];
        this.keepSelectionAfterRefresh = true;
        this.equalsByPropertyName = 'id';
        this.defaultSortBy = 'lotSequence';
        this.defaultSortDir = SortDir.DESC.name;
    }

    getCssClass(element: any, propertyName: string): string {
        if (element && element['current'] === true) {
            return 'current-lot';
        } else {
            return '';
        }
    }
}
