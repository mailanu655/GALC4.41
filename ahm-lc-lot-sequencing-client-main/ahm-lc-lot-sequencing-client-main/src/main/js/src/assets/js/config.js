const config = {
    siteId:'MAP',
    url: 'http://170.108.47.111:5200',
    serviceUrl: 'http://lc-lot-sequencing-service-linectrl-lotsequencing-mapdev.apps.ocpnp.ham.am.honda.com',
    //serviceUrl:'http://localhost:9005',
    galcUrl: 'http://qpmc1was.ham.am.honda.com:8005',
    vdbUrl: 'http://170.108.47.111:7450',
    baseUrl: "http://localhost:7450",
    keyCloakUrl: 'https://keycloak.keycloak.apps.ocpnp.ham.am.honda.com',
    keyCloakRealm: 'ahm',
    keyCloakClientId: 'lc-lotsequence-map',
    default: 'default',
    hidPort: 3000,
    appName: 'LC Lot Sequencing Client',
    logoMessage: 'Line Control Lot Sequencing Client',
    helpUrl: "https://home.hondaweb.com/w1/hondawiki/pages/Lot-Sequencing.aspx",
    revisionHistoryUrl: "https://hondaweb.com/hra_wiki/display/GALC/Lot+Sequencing+Release+Notes",
    minPollingInterval: 30,
    restSecurityEnabled: false,
    feedbackClientUrl: 'http://170.108.47.111:6200/feedback',
    feedbackBaseUrl: 'http://170.108.47.111:7200',
    restSecurityResources: [
        { url: 'http://127.0.0.1:8081/configurations/**', methods: ['POST', 'PUT', 'DELETE'] },
        { url: 'http://127.0.0.1:8081/jobs/**', methods: ['PUT'] },
        { url: 'http://127.0.0.1:8081/profiles/**', methods: ['DELETE'] }
    ],
    columns: "hello",
    isDragable: true,
    count: '5',
    productType: 'PREFIX_RAD',
    isCommentEditAllowed: true,
    processLocation: 'AF',
    isSentToWeldOnEnabled: true,
};

(function (window) {
    window.__env = config;
}(this));
