import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConfigService } from 'src/app/services/config.service';

@Component({
    selector: 'dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

    constructor(private config: ConfigService, private route: ActivatedRoute) { }

    ngOnInit() {
        this.config.siteId = null!;
        if (this.route.routeConfig?.path === 'it') {
            this.config['admin'] = true;
        } else {
            this.config['admin'] = false;
        }
    }

    get logoMessage(): string {
        return this.config.logoMessage;
    }
}
