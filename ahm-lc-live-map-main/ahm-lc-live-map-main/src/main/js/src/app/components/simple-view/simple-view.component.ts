import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppService } from 'src/app/services/app.service';
import { PartIssueService } from 'src/app/services/part-issue.service';
import { ConfigService } from 'src/app/services/config.service';
import { MapComponent } from '../map/map.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DataService } from 'src/app/services/data-service';
import { PMQAIssueService } from 'src/app/services/pmqa-issue.service';

@Component({
    selector: 'simple-view',
    templateUrl: './simple-view.component.html',
    styleUrls: ['../map/map.component.css', './simple-view.component.css']
})
export class SimpleViewComponent extends MapComponent {
    constructor(appService: AppService, protected dataService: DataService, partIssueService: PartIssueService, config: ConfigService, route: ActivatedRoute, router: Router, snackBar: MatSnackBar, pmqaIssueService: PMQAIssueService) {
        super(appService, dataService, partIssueService, config, route, router, snackBar, pmqaIssueService);
    }

    setParameters() {
        super.setParameters();
    }
}
