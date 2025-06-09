import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import Keycloak, { KeycloakInstance } from 'keycloak-js';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root',
})
export class KeycloakService {
//   private keycloaks: KeycloakInstance;
//   userDetails: any;
//   userName: any;

//   constructor(private router: Router, private config :ConfigService) {
//     this.keycloaks = new Keycloak({
//       url:   this.config.keycloakUrl,
//       realm: this.config.realm,           
//       clientId: this.config.clientId 
//     });
//   }
//   private initialized = false;
//   private authenticated = false;
//   // constructor(private router: Router) {}
//   initializeKeycloak(): Promise<boolean> {
//     if (this.initialized) {
//       console.warn('Keycloak is already initialized.');
//       return Promise.resolve(true);
//     }
  
//     return new Promise((resolve, reject) => {
//       this.keycloaks
//         .init({  onLoad: 'login-required',
//         checkLoginIframe: false,
//         pkceMethod: 'S256', // Recommended for modern Keycloak versions
//         responseMode: 'query' })
//         .then((authenticated) => {
//           this.initialized = true;
//           this.authenticated = authenticated;
  
//           if (authenticated) {
//             console.log('User authenticated, loading user profile...');
//             this.keycloaks
//               .loadUserProfile()
//               .then((profile) => {
//                 this.userDetails = profile;
//                 localStorage.setItem('userDetails', JSON.stringify(this.userDetails));
  
//                 // Set the userName
//                 this.userName = profile?.username || 'visitor';
//                 console.log('User Name:', this.userName);
//                 localStorage.setItem('userName', this.userName);
  
//                 // Redirect to home after successful authentication
//                 const redirectUrl = '/home';
//                 console.log('Redirecting to:', redirectUrl);
//                 this.router.navigateByUrl(redirectUrl);
//               })
//               .catch((error) => console.error('Error loading user profile:', error));
//           } else {
//             console.warn('User is not authenticated.');
//           }
  
//           resolve(authenticated);
//         })
//         .catch((err) => {
//           console.error('Keycloak initialization failed:', err);
//           reject(err);
//         });
//     });
//   }
  
  
  
//   login(): void {
//     if (this.keycloaks) {
//       // Store the intended redirect path in localStorage
//       localStorage.setItem('redirectUrl', '/home');
  
//       // Redirect to Keycloak login
//       this.keycloaks.login({ redirectUri: window.location.origin });
//     } else {
//       console.error('Keycloak instance is not initialized.');
//     }
//   }
  

//   logout(): void {
//     const currentUrl = this.router.url;
//     if (this.keycloaks) {
//       localStorage.setItem('showPop', 'false');
//       this.keycloaks.logout();
//     } else {
//       console.error('Keycloak instance is not initialized.');
//     }
//   }


//   isAuthenticated(): boolean {
//     return !!this.keycloaks && !!this.keycloaks.token;
//   }


//   async loadUserProfile() {
//     if (this.keycloaks) {
//       this.keycloaks.loadUserProfile()
//         .then(profile => {
//           this.userDetails = profile;
//           console.log(this.userDetails);
//           localStorage.setItem('userDetails', JSON.stringify(this.userDetails));
//         })
//         .catch(reason => {
//           console.log('Error loading user profile: ' + reason);
//         });
//       return this.userDetails;
//     } else {
//       return '';
//     }
//   }

//   getToken(): string {
//     return this.keycloaks.token || '';
//   }

}