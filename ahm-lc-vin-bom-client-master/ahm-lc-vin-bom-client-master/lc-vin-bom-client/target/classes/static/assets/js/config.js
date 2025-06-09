var config = {

    hidPort: 3000,
    appenv: 'localhost',
    appName: 'LC SUMS VIN BOM',
    logoMessage: 'Line Control - SUMS VIN BOM',
    minPollingInterval: 30,
    restSecurityEnabled: false,
    helpUrl:'https://home.hondaweb.com/w1/hondawiki/pages/VIN-BOM.aspx',
    revisionHistory:'https://hondaweb.com/hra_wiki/display/GALC/VIN-BOM+Release+Notes',
    sites:{
        
        localhost:{
            name: 'AAP1',
            plantName: 'HMA',
            url:'http://localhost:9005/',
            serviceUrl:'http://qhma1was.hma.am.honda.com:8005',
            vdbUrl: 'http://127.0.0.1:8082',
            keyCloakClientId: 'lc-vinbom-map',
            keyCloakUrl: 'http://ahmqxlrincapp01.ham.am.honda.com:9080/auth',
            keyCloakRealm: 'ahm',
        }
    },
    feedbackClientUrl:'http://170.108.47.111:6200/feedback',
    feedbackBaseUrl: 'http://170.108.47.111:7200'
};

(function (window) {
    window.__env = config;
}(this));
