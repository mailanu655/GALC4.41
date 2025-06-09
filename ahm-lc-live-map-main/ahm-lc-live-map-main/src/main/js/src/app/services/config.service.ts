import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class ConfigService {
    appName: string;
    appShortName: string;
    env: string;
    logoMessage: string;
    url: string;
    serviceUrl: string;
    refreshInterval: number = 15;
    sites: any = {};
    helpUrl: string;
    feedbackUrl: string;
    infoUrl: string;

    // === controll === //
    siteId: string;

    constructor() {
        const config: any = window['__config'] || {};
        Object.assign(this, config);
    }

    // === get/set === //
    get siteServiceUrl(): string | null {
        if (this.siteId) {
                return this.serviceUrl + "/" + this.siteId;
        }
        return null;
    }

    get partIssueServiceUrl(): string | null {
        let siteConfig = this.sites[this.siteId];
        if (siteConfig) {
            return siteConfig['partIssueServiceUrl'];
        }
        return null;
    }

    get partIssueConfigurationIds(): number[] {
        let siteConfig = this.site;
        let configIds: number[] = [];
        if (siteConfig && siteConfig['partIssueConfigurationIds']) {
            configIds = JSON.parse(siteConfig['partIssueConfigurationIds']);
        }
        return configIds;
    }

    get sitesIds(): string[] {
        let siteIds: string[] = Object.keys(this.sites);
        siteIds?.sort();
        return siteIds;
    }

    get site(): any {
        if (this.siteId) {
            return this.sites[this.siteId];
        } else {
            return null;
        }
    }

    get modelNames(): string[] {
        let siteConfig = this.site;
        let names: string[] = [];
        let models: any[] = [];
        if (siteConfig && siteConfig['models']) {
            models = Object.values(siteConfig['models']);
        }
        if (models.length > 0) {
            for (let model of models) {
                let name = model.name;
                if (name) {
                    names.push(name);
                }
            }
        }
        return names;
    }
}
