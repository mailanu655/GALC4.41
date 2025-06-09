import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';

@Injectable({
    providedIn: 'root'
})
export class AppService {
    applications: any[];

    constructor(private httpClient: HttpClient, private config: ConfigService) {
        let buildInfo = window['__build'] || {};
        this.applications = [
            { name: 'Client Angular', info: buildInfo, status: 'UP' },
            { name: 'Client', infoUrl: this.config.url + "/actuator/info", healthUrl: this.config.url + "/actuator/health" },
            { name: 'Service', infoUrl: this.config.serviceUrl + "/actuator/info", healthUrl: this.config.serviceUrl + "/actuator/health" },
            { name: 'VDB', infoUrl: this.config.vdbUrl + "/actuator/info", healthUrl: this.config.vdbUrl + "/actuator/health" }
        ];
        if (this.config.hidPort) {
            let hidUrl = "http://localhost:" + this.config.hidPort;
            this.applications.push({ name: 'LC HID Service', infoUrl: hidUrl + "/info", healthUrl: hidUrl + "/health" });
            this.applications.push({ name: 'LC HID Device', info: { artifact: "Delcom USB VSI", name: "LC HID Device" }, healthUrl: hidUrl + "/connect", dataConverter: this.hidStatusConverter });
        }
        this.loadAppInfo();
    }

    // === build info === //
    findData(url: string) {
        return this.httpClient.get(url);
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

    hidStatusConverter(data: any) {
        if (data && data['path']) {
            data['status'] = 'UP';
        }
        return data;
    }

    // === get/set === //
    getApplications() {
        for (let app of this.applications) {
            if (!app['artifact']) {
                this.loadInfo(app);
            }
        }
        return this.applications;
    }
}
