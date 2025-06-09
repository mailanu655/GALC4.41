import { Component, OnInit } from '@angular/core';
import { LocationView } from 'src/app/lib/constants';
import { MessageBundle, messages } from 'src/app/lib/messages';
import { AppService } from 'src/app/services/app.service';
import { ConfigService } from 'src/app/services/config.service';
import { BodyLocation } from '../line/line-model';

@Component({
    selector: 'legend',
    templateUrl: './legend.component.html',
    styleUrls: ['./legend.component.css']
})
export class LegendComponent implements OnInit {

    issueTypes: BodyLocation[] = [];
    messages: MessageBundle = messages;

    constructor(public appService: AppService, private config: ConfigService) {
        this.issueTypes.push(Object.assign(new BodyLocation(1), { label: 'QICS Defect', tooltip: this.messages.get('tooltip.qics.defect'), product: { defects: [{ name: "defect1" }] } }));
        this.issueTypes.push(Object.assign(new BodyLocation(2), { label: 'Lot Control Issue', tooltip: this.messages.get('tooltip.part.issue'), product: { partIssues: [{ name: "part1" }] } }));
        this.issueTypes.push(Object.assign(new BodyLocation(3), { label: 'PMQA Issues', tooltip: this.messages.get('tooltip.pmqa.issue'), product: { pmqaIssues: [{ name: "pmqa1" }] } }));
        this.issueTypes.push(Object.assign(new BodyLocation(4), { label: 'Hold', tooltip: this.messages.get('tooltip.product.hold'), product: { holds: [{ name: "hold1" }] } }));
        this.issueTypes.push(Object.assign(new BodyLocation(5), { label: 'Multi Type Issues', product: { defects: [{ name: "defect1" }], partIssues: [{ name: "part1" }], holds: [{ name: "hold1" }] } }));
    }

    ngOnInit() {
    }

    close() {
        this.appService.menuItem = null;
    }

    get models(): LocationView[] {
        if (this.appService.admin === true) {
            return LocationView.values;
        }
        let models: string[] = this.config.modelNames;
        if (models) {
            let views: LocationView[] = [];
            for (let model of models) {
                let view = LocationView.get(model);
                if (view) {
                    views.push(view);
                }
            }
            if (views.length > 0) {
                views.push(LocationView.empty);
                return views;
            }
        }
        return LocationView.values;
    }

    get locationView() {
        return LocationView.honda;
    }
}
