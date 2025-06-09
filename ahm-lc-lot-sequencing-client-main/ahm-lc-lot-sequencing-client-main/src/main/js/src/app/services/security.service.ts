import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {
  CanActivate,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from '@angular/router';
import { KeycloakService, KeycloakAuthGuard } from 'keycloak-angular';
import { ConfigService, RestResource } from './config.service';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  }),
};

@Injectable({
  providedIn: 'root',
})
export class SecurityService extends KeycloakAuthGuard implements CanActivate {
  userDetails: any;

  constructor(
    protected router: Router,
    keycloakService: KeycloakService,
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(router, keycloakService);
    this.getUserProfile();
  }

  isAccessAllowed(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean> {
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
        alert('You have no permission to access this resource !');
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
      roleArray = roles.split(',');
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
    let originUrl = encodeURIComponent(window.location.origin);
    const keycloakInstance = this.keycloakAngular.getKeycloakInstance();
    const idToken = keycloakInstance.idToken;
    const keyCloakUri = `${this.configService.keyCloakUrl}/realms/${this.configService.keyCloakRealm}/protocol/openid-connect/logout`;
    window.location.href = `${keyCloakUri}?id_token_hint=${idToken}&post_logout_redirect_uri=${originUrl}`;
  }

  login() {
    return this.keycloakAngular.login();
  }

  getUser() {
    if (this.isLoggedIn()) {
      localStorage.setItem(
        'userId',
        this.keycloakAngular.getKeycloakInstance().subject
      );
      localStorage.setItem(
        'userRoles',
        JSON.stringify(this.keycloakAngular.getUserRoles())
      );
      return this.keycloakAngular.getUsername();
    } else {
      return '';
    }
  }

  async getUserProfile() {
    if (this.isLoggedIn()) {
      this.keycloakAngular
        .loadUserProfile()
        .then((profile) => {
          this.userDetails = profile;
          localStorage.setItem(
            'userRoles',
            JSON.stringify(this.keycloakAngular.getUserRoles())
          );
          localStorage.setItem('userDetails', JSON.stringify(this.userDetails));
        })
        .catch((reason) => {
          console.log(reason);
        });
      //return this.userDetails;
    } else {
      // return '';
    }
  }

  isUseSecurity(): boolean {
    let useSecurity: boolean =
      this.configService.keyCloakUrl != null &&
      this.configService.keyCloakUrl != '';
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

  getUserGrps() {
    const sendAppUSageData = {
      name: this.keycloakAngular.getKeycloakInstance().subject,
    };

    this.http
      .post(
        this.configService.feedbackBaseUrl + '/feedback/getUserGroups',
        JSON.stringify(sendAppUSageData),
        httpOptions
      )
      .subscribe((response) => {
        let userGrpsList = new Array();
        userGrpsList = JSON.parse(JSON.stringify(response)).data;
        localStorage.setItem(
          'userGroups',
          JSON.parse(JSON.stringify(response)).data
        );
      });
  }

  getUserKeycloakClients() {
    const sendAppUSageData = {
      name: this.keycloakAngular.getKeycloakInstance().subject,
    };

    this.http
      .post(
        this.configService.baseUrl + '/feedback/getKeycloakClientsForUser',
        JSON.stringify(sendAppUSageData),
        httpOptions
      )
      .subscribe((response) => {
        let userGrpsList = new Array();
        userGrpsList = JSON.parse(JSON.stringify(response)).data;
        localStorage.setItem(
          'userclients',
          JSON.parse(JSON.stringify(response)).data
        );
      });
  }

  get secureRestResources(): RestResource[] {
    return this.configService.restSecurityResources;
  }
}
