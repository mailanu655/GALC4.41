import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { KeycloakService, KeycloakAuthGuard } from 'keycloak-angular';
import { ConfigService, RestResource } from './config.service';

@Injectable({
    providedIn: 'root'
})
export class SecurityService extends KeycloakAuthGuard implements CanActivate {

    constructor(protected router: Router, keycloakService: KeycloakService, private configService: ConfigService) {
        super(router, keycloakService);
    }

    isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {

        if (this.isUseSecurity() == false) {
            return Promise.resolve(true);
        }

        return new Promise(async (resolve, reject) => {
            if (!this.authenticated) {
                this.login();
                return;
            }
            const requiredRoles: string[] = route.data.roles;
            let granted: boolean = false;
            if (!requiredRoles || requiredRoles.length === 0) {
                granted = true;
            } else {
                for (const requiredRole of requiredRoles) {
                    if (this.roles.indexOf(requiredRole) > -1) {
                        granted = true;
                        break;
                    }
                }
            }

            if (granted === false) {
                //this.router.navigate(['']);
                alert("You have no permission to access this resource !");
            }
            resolve(granted);
        });
    }

    isUserInRole(roles: string): boolean {
        if (this.isUseSecurity() == false) {
            return true;
        }

        if (!roles) {
            return true;
        }
        if (!this.isLoggedIn()) {
            return false;
        }
        let roleArray: string[] = [];
        if (Array.isArray(roles)) {
            roleArray = roles;
        } else {
            roleArray = roles.split(",");
        }
        for (let role of roleArray) {
            if (this.keycloakAngular.isUserInRole(role)) {
                return true;
            }
        }
        return false;
    }

    isLoggedIn() {
        return this.authenticated;
    }

    logout() {
        let originUrl = window.location.origin;
        let redirectUrl = originUrl + this.router.createUrlTree(['']);
        this.keycloakAngular.logout(redirectUrl);
    }

    login() {
        return this.keycloakAngular.login();
    }

    getUser() {
        if (this.isLoggedIn()) {
            return this.keycloakAngular.getUsername();
        } else {
            return '';
        }
    }

    isUseSecurity(): boolean {
        let useSecurity: boolean = this.configService.keyCloakSiteUrl != null && this.configService.keyCloakSiteUrl != '';
        return useSecurity;
    }

    isRestSecurityEnabled(): boolean {
        let enabled: any = this.configService.restSecurityEnabled;
        if (enabled && (enabled === 'true' || enabled === true)) {
            return true;
        } else {
            return false;
        }
    }

    getToken() {
        return this.keycloakAngular.getKeycloakInstance().token;
    }

    get secureRestResources(): RestResource[] {
        return this.configService.restSecurityResources;
    }

    get headers():HttpHeaders{
        return new HttpHeaders().set('Content-Type', 'application/json').set('userId', this.getUser()).set('siteId', this.configService.siteId).set('messageId', this.configService.guid.toString()).set('applicationName',this.configService.appName);
    }
}
