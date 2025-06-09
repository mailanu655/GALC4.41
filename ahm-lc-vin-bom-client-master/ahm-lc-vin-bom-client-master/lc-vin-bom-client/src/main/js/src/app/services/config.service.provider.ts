import { ConfigService } from './config.service';
import { KeycloakService } from 'keycloak-angular';
import { homeRedirectUrl } from '../app.routes';

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

export function keyCloakInitializer(keycloak: KeycloakService, configService: ConfigService): () => Promise<any> {
    return (): Promise<any> => {
        return new Promise(async (resolve, reject) => {
            const keycloakConfig = configService.getKeyCloakConfig();
            try {
                let redirectUrl = window.location.origin;
                await keycloak.init({
                    config: keycloakConfig,
                    initOptions: {
                        checkLoginIframe: false,
                        redirectUri: redirectUrl
                    },
                    enableBearerInterceptor: true,
                    bearerExcludedUrls: ['', '/'],
                    loadUserProfileAtStartUp: true
                });
                resolve(true);
            } catch (error) {
                reject(error);
            }
        });
    };
}

