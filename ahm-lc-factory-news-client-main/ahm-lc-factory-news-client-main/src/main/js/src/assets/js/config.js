var config = {
    appName: 'LC Factory News - Dev',
    appShortName: 'LC FN - Dev',
    logoMessage: 'Line Control - Factory News - AHM Dev@Docker',
    url:'http://127.0.0.1:7420',
    helpUrl: "https://home.hondaweb.com/w1/hondawiki/pages/Factory-News.aspx",
    releaseNotesUrl: "https://hondaweb.com/hra_wiki/display/GALC/Factory+News+Release+Notes",
    refreshInterval: 30,
    lastRefreshed: '',
    feedbackClientUrl: 'http://170.108.47.111:6200/feedback',
    feedbackBaseUrl: 'http://170.108.47.111:7200',
    galcUrl: 'http://pelp1was.ham.am.honda.com:8005',//https://keycloak.keycloak.apps.ocpnp.ham.am.honda.com
    keycloakUrl: 'https://keycloak.keycloak.apps.ocpnp.ham.am.honda.com',//http://keycloak.keycloak.apps.ocpnp.ham.am.honda.com//"https://keycloak.keycloak.apps.sbxocp.amerhonda.com"
    realm: 'ahm',//http://ahmqxlrincapp01.ham.am.honda.com:9080/auth
    clientId: 'lc-factorynews',//lc-routing-map  //lc-factorynews
    sites: {
        aap1: {
            name: 'AAP1',
            vdbUrl: 'http://localhost:7420/aap1',
            plantName: 'Frame'
        },
        aap2: {
            name: 'AAP2',
            vdbUrl: 'http://localhost:7420/aap2',
            plantName: 'Frame'
        },
        elp: {
            name: 'ELP',
            vdbUrl: 'http://localhost:7420/elp',
            plantName: 'Frame3'
        },
        hcl: {
            name: 'HCL',
            vdbUrl: 'http://localhost:7420/hcl',
            plantName: 'FR1'
        },
        hcm1: {
            name: 'HCM1',
            vdbUrl: 'http://localhost:7420/hcm1',
            plantName: 'FRAME1'
        },
        hcm2: {
            name: 'HCM2',
            vdbUrl: 'http://localhost:7420/hcm2',
            plantName: 'FRAME2'
        },
        hcm3: {
            name: 'HCM3',
            vdbUrl: 'http://localhost:7420/hcm3',
            plantName: 'Engine'
        },
        iap: {
            name: 'IAP',
            vdbUrl: 'http://localhost:7420/iap',
            plantName: 'Frame'
        },
        map1: {
            name: 'MAP1',
            vdbUrl: 'http://localhost:7420/map1',
            plantName: 'Frame1'
        },
        map2: {
            name: 'MAP2',
            vdbUrl: 'http://localhost:7420/map2',
            plantName: 'Frame2'
        },
        pmc: {
            name: 'PMC',
            vdbUrl: 'http://localhost:7420/pmc',
            plantName: 'FRAME'
        },
        aepvl5: {
            name: 'AEP VL5',
            vdbUrl: 'http://localhost:7420/aepvl5',
            plantName: 'Valve'
        },
        aepae2: {
            name: 'AEP AE2',
            vdbUrl: 'http://localhost:7420/aepae2',
            plantName: 'Engine'
        },
        aepae4: {
            name: 'AEP AE4',
            vdbUrl: 'http://localhost:7420/aepae4',
            plantName: 'Engine'
        },
        aepae5: {
            name: 'AEP AE5',
            vdbUrl: 'http://localhost:7420/aepae5',
            plantName: 'ENGINE'
        },
        aap3: {
            name: 'AAP3',
            vdbUrl: 'http://localhost:7420/aap3',
            plantName: 'Engine'
        }
    }
};

(function (window) {
    window.__config = config;
}(this));
