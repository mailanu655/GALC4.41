import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ConfigService } from 'src/app/services/config.service';

@Component({
    selector: 'about',
    templateUrl: './about.component.html',
    styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {

    applications: any[];

    constructor(private httpClient: HttpClient, private config: ConfigService) {
        let buildInfo = window['__build'] || {};
        this.applications = [
            { name: 'Client Angular', info: buildInfo, status: 'UP' },
            { name: 'Client', infoUrl: this.config.url + "/actuator/info", healthUrl: this.config.url + "/actuator/health" },
            { name: 'Service', infoUrl: this.config.serviceUrl + "/actuator/info", healthUrl: this.config.serviceUrl + "/actuator/health" }
        ];
        this.loadAppInfo();
    }

    ngOnInit(): void {
    }

    loadAppInfo(): void {
        for (let app of this.applications) {
            this.loadInfo(app);
        }
    }

    loadInfo(app: any) {
        if (!app.infoUrl) {
            return;
        }
        this.findData(app.infoUrl).subscribe({
            next: res => {
                if (res) {
                    app.info = res['build'];
                } else {
                    if (!app.info) {
                        app.info = { name: app.name };
                    }
                }
            },
            error: ex => {
                console.log("Error: " + JSON.stringify(ex));
                if (!app.info) {
                    app.info = { name: app.name };
                }
            }
        });
    }

    findData(url: string) {
        return this.httpClient.get(url);
    }
}
