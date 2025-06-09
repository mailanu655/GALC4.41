import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

export class RestResource {
    url!: string;
    methods!: string[];
}
@Injectable({
    providedIn: 'root'
})
export class ConfigService {
    appName: string;
    appShortName: string;
    logoMessage: string;
    url: string;
    refreshInterval: number = 15;
    lastRefreshed: any;
    sites: any = {};
    helpUrl: string;
    releaseNotesUrl: string;
    countDown: number;
    plantName: string;
    feedbackClientUrl: string;
    feedbackBaseUrl: string;
    // === controll === //
    siteId: string;
    isSiteConnected: boolean;
    siteMessage: string;
    galcUrl: string;
    keycloakUrl: string = 'https://keycloak.keycloak.apps.ocpnp.ham.am.honda.com';//http://ahmqxlrincapp01.ham.am.honda.com:9080/auth
    realm: string = 'ahm';//https://keycloak.keycloak.apps.ocpnp.ham.am.honda.com
    clientId: string = 'lc-factorynews';//lc-routing-map
    
    constructor() {
        const config: any = window['__config'] || {};
        Object.assign(this, config);
    }

    // === get/set === //
    get vdbUrl(): string | null {
        let siteConfig = this.sites[this.siteId];
        if (siteConfig) {
            return siteConfig['vdbUrl'];
        }
        return null;
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

    getKeyCloakConfig() {
        const keyCloakConfig = {
            url: this.keycloakUrl,
            realm: this.realm,
            clientId: this.clientId
        };
        return keyCloakConfig;
    }


}