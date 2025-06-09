import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { AppService } from 'src/app/services/app.service';
import { ConfigService } from 'src/app/services/config.service';
import { DataService } from 'src/app/services/data-service';
import { PartIssueService } from 'src/app/services/part-issue.service';
import { PMQAIssueService } from 'src/app/services/pmqa-issue.service';
import { CompositeViewComponent } from '../composite-view/composite-view.component';
import { View } from '../map/map.component';

@Component({
    selector: 'line-view',
    templateUrl: './line-view.component.html',
    styleUrls: ['./line-view.component.css']
})
export class LineViewComponent extends CompositeViewComponent {

    constructor(appService: AppService, protected dataService: DataService, partIssueService: PartIssueService, config: ConfigService, route: ActivatedRoute, router: Router, snackBar: MatSnackBar, protected pmqaIssueService : PMQAIssueService) {
        super(appService, dataService, partIssueService, config, route, router, snackBar, pmqaIssueService);
    }

    setParameters() {
        super.setParameters();
        this.view = View.LINE;
    }
}
