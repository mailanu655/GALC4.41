import { Injectable } from '@angular/core';
import { KeycloakConfig } from 'keycloak-js';
import { Guid } from 'guid-typescript';

export class RestResource {
    url: string;
    methods: string[];
}

@Injectable({
    providedIn: 'root'
})
export class ConfigService {
    appenv: string;
    appName: string;
    logoMessage: string;
    url: string;
    serviceUrl: string;
    prodDetailsUrl: string;
    vdbUrl: string;
    feedbackClientUrl: string;
    feedbackBaseUrl: string;
    keyCloakUrl: string;
    keyCloakRealm: string;
    keyCloakClientId: string;
    hidPort: number;
    debug: boolean = false;
    restSecurityEnabled: boolean;
    restSecurityResources: RestResource[];
    minPollingInterval: number;
    sites: any = {};
    siteId: string;
    guid:Guid;
    hostname: string;
    hostport:string;
    helpUrl:string;
    revisionHistory:string;
    temp:string[];

    constructor() {
      const config = window['__config'] || {};
      this.hostname = window.location.hostname;
      this.hostport = window.location.port;
      console.log("window host and port - "+ this.hostname +'-'+this.hostport);
      Object.assign(this, config);
    
      this.guid = Guid.create();
      this.temp = this.hostname.split('.');
      if(this.temp.length > 0){
        this.hostname=this.temp[0];
      }
     
    }

    getKeyCloakConfig() {
        if(this.appenv.startsWith('localhost')){
            this.siteId=this.hostname;
        }else{
            this.siteId = this.appenv;
        }
        let siteConfig = this.sites[this.siteId];
        
        console.log("key cloak siteId - "+ this.siteId);
        
        this.keyCloakClientId = siteConfig['keyCloakClientId'];
        this.keyCloakUrl = siteConfig['keyCloakUrl'];
        this.keyCloakRealm = siteConfig['keyCloakRealm'];
        
        console.log("site-"+ this.siteId +", keyCloakClient - "+ this.keyCloakClientId );
            const keyCloakConfig: KeycloakConfig = {
               // url: this.keyCloakUrl,
               //realm: this.keyCloakRealm,
               //clientId: this.keyCloakClientId
                //, "credentials": { "secret": "86851850-7c85-4e48-9926-413a9ae5771c" }
               url: siteConfig['keyCloakUrl'],
               realm: siteConfig['keyCloakRealm'],
               clientId: siteConfig['keyCloakClientId']
            }
            return keyCloakConfig;
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
            return this.sites['map'];
        }
    }

    get restUrl(): string | null {
        let siteConfig = this.sites[this.siteId];
        if (siteConfig) {
            return siteConfig['serviceUrl'];
        }
        return null;
    }

    get prodDtilsUrl(): string | null {
        let siteConfig = this.sites[this.siteId];
        if (siteConfig) {
            return siteConfig['prodDetailsUrl'];
        }
        return null;
    }

    get keyCloakClient():string{
        let siteConfig = this.sites[this.siteId];
        if (siteConfig) {
            return siteConfig['keyCloakClientId'];
        }
        return this.keyCloakClientId;
    }

    get keyCloakSiteUrl():string{
        let siteConfig = this.sites[this.siteId];
        if (siteConfig) {
            return siteConfig['keyCloakUrl'];
        }
        return this.keyCloakUrl;
    }
    get hostUrl():string{
        let siteConfig = this.sites[this.siteId];
        if(siteConfig){
            this.url = siteConfig['url'];
        }
        return this.url;
    }
}
