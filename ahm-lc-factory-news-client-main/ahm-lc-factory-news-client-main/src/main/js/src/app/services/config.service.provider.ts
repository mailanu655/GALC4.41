import { ConfigService } from './config.service';
import { KeycloakService } from 'keycloak-angular';
import { FnConfigServiceService } from './fn-config-service.service';
import Keycloak from 'keycloak-js';

export const homeUrl = 'home';
export const HOME_REDIRECT_URL = '/home';

export const ConfigServiceFactory = () => {
  const service = new ConfigService() as any;

  const browserWindow: any = window || {};
  const browserWindowEnv = browserWindow['__env'] || {};

  for (const key in browserWindowEnv) {
    if (browserWindowEnv.hasOwnProperty(key)) {
      service[key] = (window as any)['__env'][key];
    }
  }
  return service;
};

export const ConfigServiceProvider = {
  provide: ConfigService,
  useFactory: ConfigServiceFactory,
  deps: [],
};

export function keyCloakInitializer(
  keycloakService: KeycloakService,
  configService: ConfigService,
  fnConfig: FnConfigServiceService
): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      try {
        const isLoggedIn = fnConfig.getPlant();
        console.log("data islogin", isLoggedIn);
        const keycloakConfig = {
          url: configService.keycloakUrl,
          realm: configService.realm,
          clientId: configService.clientId,
        };

        await keycloakService.init({
          config: keycloakConfig,
          initOptions: {
            checkLoginIframe: false,
            redirectUri: `${window.location.origin}/${HOME_REDIRECT_URL}`,
          },
          enableBearerInterceptor: true,
          bearerExcludedUrls: ['', '/'],
          loadUserProfileAtStartUp: true,
        });

        resolve('Keycloak initialized');
      } catch (error) {
        console.error('Keycloak initialization failed', error);
        reject(error);
      }
    });
  };
}
