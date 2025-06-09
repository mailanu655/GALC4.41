import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { KeycloakService, KeycloakAuthGuard } from 'keycloak-angular';
import { ConfigService, RestResource } from './config.service';
import { Observable } from 'rxjs';

const httpOptions = {
    headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
    }),
};

@Injectable({
    providedIn: 'root'
})
export class SecurityService extends KeycloakAuthGuard {
    userDetails: any;

    constructor(protected override router: Router, keycloakService: KeycloakService, private http: HttpClient,
        private configService: ConfigService) {
        super(router, keycloakService);
    }

    isAccessAllowed(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
        if (!this.isUseSecurity()) {
            return Promise.resolve(true);
        }

        return new Promise(async (resolve) => {
            if (!this.authenticated) {
                this.login();
                resolve(false);
            }

            const requiredRoles: string[] = route.data['roles'];
            let granted = false;

            if (!requiredRoles || requiredRoles.length === 0) {
                granted = true;
            } else {
                granted = requiredRoles.some((role) => this.roles.includes(role));
            }

            if (!granted) {
                alert('You have no permission to access this resource.');
            }

            resolve(granted);
        });
    }

    async validateRoles(roles: string[]): Promise<boolean> {
        if (!this.isLoggedIn()) {
            await this.login();
            return false;
        }

        if (!this.roles || this.roles.length === 0) {
            this.roles = await this.keycloakAngular.getUserRoles(); // Ensure roles are fetched
        }

        return roles.some((role) => this.roles.includes(role));
    }


    isTokenExpired(): boolean {
        return this.keycloakAngular.isTokenExpired();
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

    async isLoggedIn() {
        return await this.keycloakAngular.isLoggedIn();
    }

    logout(): void {
        let originUrl = encodeURIComponent(window.location.origin);
        const keycloakInstance = this.keycloakAngular.getKeycloakInstance();
        const idToken = keycloakInstance.idToken;
        const keyCloakConfig = this.configService.getKeyCloakConfig();
        console.log("keycloakconfig", keyCloakConfig);
        const keyCloakUri = `${keyCloakConfig.url}/realms/${keyCloakConfig.realm}/protocol/openid-connect/logout`;
        console.log("keycloakurl", keyCloakUri);
        window.location.href = `${keyCloakUri}?id_token_hint=${idToken}&post_logout_redirect_uri=${originUrl}`;
    }
    login() {
        this.authenticated = true;
        localStorage.setItem('config', 'true');
        return this.keycloakAngular.login();
    }

    async getUser() {
        if (await this.isLoggedIn()) {
            localStorage.setItem('userId', (this.keycloakAngular.getKeycloakInstance() as any).subject);
            localStorage.setItem('userRoles', JSON.stringify(this.keycloakAngular.getUserRoles()));
            const role = this.keycloakAngular.getUserRoles();
            return this.keycloakAngular.getUsername();
        } else {
            return '';
        }
    }

    async getUserProfile() {
        if (await this.isLoggedIn()) {

            this.keycloakAngular.loadUserProfile()
                .then(profile => {
                    this.userDetails = profile;
                    localStorage.setItem('userRoles', JSON.stringify(this.keycloakAngular.getUserRoles()));
                    localStorage.setItem('userDetails', JSON.stringify(this.userDetails));
                })
                .catch(reason => { console.log(reason); });
            //return this.userDetails;
        } else {
            // return '';
        }
    }
    getUserRoles() {
        return this.keycloakAngular.getUserRoles();
    }

    isUseSecurity(): boolean {
        let useSecurity: boolean = this.configService.keycloakUrl != null && this.configService.keycloakUrl != '';
        return useSecurity;
    }

    getToken() {
        return this.keycloakAngular.getKeycloakInstance().token;
    }

}
