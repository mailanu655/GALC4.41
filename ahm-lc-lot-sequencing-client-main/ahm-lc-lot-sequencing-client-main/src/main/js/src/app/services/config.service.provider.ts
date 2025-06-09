import { ConfigService } from './config.service';
import { KeycloakService } from 'keycloak-angular';

export const homeUrl = 'home';
export const HOME_REDIRECT_URL = '/#/home';

export const ConfigServiceFactory = () => {
  const service = new ConfigService();

  const browserWindow = window || {};
  const browserWindowEnv = browserWindow['__env'] || {};

  for (const key in browserWindowEnv) {
    if (browserWindowEnv.hasOwnProperty(key)) {
      service[key] = window['__env'][key];
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
  keycloak: KeycloakService,
  configService: ConfigService
): () => Promise<any> {
  return (): Promise<any> => {
    return new Promise(async (resolve, reject) => {
      //const keycloakConfig = configService.getKeyCloakConfig();
      try {
        let redirectUrl = window.location.origin + HOME_REDIRECT_URL;
        await keycloak.init({
          config: {
            url: configService.keyCloakUrl,
            realm: configService.keyCloakRealm,
            clientId: configService.keyCloakClientId
          },
          initOptions: {
            //onLoad: 'login-required',
            //onLoad: 'check-sso',
            checkLoginIframe: false,
            //,promiseType: 'native'
            redirectUri: redirectUrl,
          },
          enableBearerInterceptor: true,
          bearerExcludedUrls: ['', '/'],
          loadUserProfileAtStartUp: true,
        });
        resolve('');
      } catch (error) {
        reject(error);
      }
    });
  };
}
