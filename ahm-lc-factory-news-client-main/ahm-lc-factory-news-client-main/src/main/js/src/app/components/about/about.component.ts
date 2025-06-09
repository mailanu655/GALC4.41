import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ConfigService } from 'src/app/services/config.service';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {

  applications: any[];

    constructor(private httpClient: HttpClient, private config: ConfigService) {
        let buildInfo = window['__build'] || {};
        this.applications = [
            { name: 'Angular Client', info: buildInfo, status: 'UP' },
            { name: 'Spring Boot Service', infoUrl: this.config.url + "/actuator/info", healthUrl: this.config.url + "/actuator/health" },
        ];
        console.log("Applications: "+JSON.stringify(this.applications));
        this.loadAppInfo();
    }

    ngOnInit(): void {
    }

    loadAppInfo(): void {
        for (let app of this.applications) {
            this.loadInfo(app);
        }
        console.log("Applications 2: "+JSON.stringify(this.applications));
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
        console.log("Applications 3: "+JSON.stringify(this.applications));
    }

    findData(url: string) {
        console.log("Applications 4: "+JSON.stringify(this.applications));
        return this.httpClient.get(url);
    }

}
