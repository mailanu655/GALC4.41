import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppService } from 'src/app/services/app.service';
import { PartIssueService } from 'src/app/services/part-issue.service';
import { PMQAIssueService } from 'src/app/services/pmqa-issue.service';
import { ConfigService } from 'src/app/services/config.service';
import { MapComponent } from '../map/map.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DataService } from 'src/app/services/data-service';

@Component({
    selector: 'composite-view',
    templateUrl: './composite-view.component.html',
    styleUrls: ['./composite-view.component.css']
})
export class CompositeViewComponent extends MapComponent {

    constructor(appService: AppService, protected dataService: DataService, partIssueService: PartIssueService, config: ConfigService, route: ActivatedRoute, router: Router, snackBar: MatSnackBar, protected pmqaIssueService : PMQAIssueService) {
         super(appService, dataService, partIssueService, config, route, router, snackBar, pmqaIssueService);
    }

    setParameters() {
        super.setParameters();
    }

    ngAfterViewInit() {
        super.ngAfterViewInit();
        setTimeout(() => this.onResize(), 0);
    }

    onResize() {
        let windowHeight: number = window.innerHeight;
        var lineElement = document.getElementById('assemblySvg');
        var rect = lineElement!.getBoundingClientRect();
        //var tabs = document.getElementById('tabs');
        // tabs!.style.height = ((windowHeight - rect.height - 75) + "px");
        document.getElementById('tabSplit')!.style.flex = "0 0 " + ((windowHeight - (rect.height + 48) - 72 - 11) + "px");
    }
}
