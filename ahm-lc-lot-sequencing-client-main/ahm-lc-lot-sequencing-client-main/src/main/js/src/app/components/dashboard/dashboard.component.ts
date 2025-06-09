import { Component } from '@angular/core';
import { ConfigService } from 'src/app/services/config.service';

@Component({
    selector: 'dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

    constructor(private config: ConfigService) { }

    get logoMessage(): string {
        return this.config.logoMessage;
    }
}
