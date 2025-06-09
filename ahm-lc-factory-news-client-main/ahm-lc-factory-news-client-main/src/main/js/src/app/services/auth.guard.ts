import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { KeycloakService } from './keycloak.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard {
  // constructor(private keycloakService: KeycloakService, private router: Router) {}

  // canActivate(): boolean {
  //   if (this.keycloakService.isAuthenticated()) {
  //     localStorage.setItem('showPop', 'true');
  //     return true;
  //   } else {
  //     this.keycloakService.login();
  //     return false;
  //   }
  // }
}
