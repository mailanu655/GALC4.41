var config = {
    appName: 'LC Live Map',
    appShortName: 'LM',
    env: 'DEV',
    logoMessage: 'Line Control Live Map DEV',
    url: 'http://127.0.0.1:8080',
    serviceUrl: 'http://127.0.0.1:8081',
    helpUrl: "http://home.hondaweb.com/w1/hondawiki/Pages/LIVE-MAP--Sites-and-Views.aspx",
    feedbackUrl: "http://lc-feedback-client-linectrl-feedback-dev.apps.npocp.hna.honda.com/feedback",
    infoUrl: "https://hondaweb.com/hra_wiki/display/GALC/Live+Map+Release+Notes",
    refreshInterval: 15,
    sites: {
        aap1: {
            name: 'AAP1',
            partIssueServiceUrl: 'http://127.0.0.1:9781',
            partIssueConfigurationIds: '[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27]',
            models: [
                { name: 'ODYSSEY', label: 'Odyssey JS', viewId: 1 },
                { name: 'RIDGELINE', label: 'Ridgeline', viewId: 6 }
            ]
        },
        aap2: {
            name: 'AAP2',
            partIssueServiceUrl: 'http://127.0.0.1:9881',
            partIssueConfigurationIds: '[38]',
            models: [
                { name: 'PASSPORT', label: 'Passport', viewId: 8 },
                { name: 'PILOT', 'label': 'Pilot', viewId: 5 }
            ]
        },
        elp: {
            name: 'ELP',
            partIssueServiceUrl: 'http://127.0.0.1:9181',
            partIssueConfigurationIds: '[9]',
            models: [
                { name: 'CRV', viewId: 1 },
                { name: 'CRV FHEV', viewId: 2 },
                { name: 'MDX', viewId: 9 },
                { name: 'MDX TYPE S', viewId: '0' },
                { name: 'RDX', viewId: 10 }]
        },
        hcm1: {
            name: 'HCM1',
            partIssueServiceUrl: 'http://127.0.0.1:9581',
            partIssueConfigurationIds: '[1, 2, 3, 4, 5, 6, 7]',
            models: [
                { name: 'CIVIC HYBRID', label: 'Civic Hybrid', viewId: 1 },
                { name: 'CIVIC', label: 'Civic', viewId: 6 },
                { name: 'CIVIC TURBO', label: 'Civic Turbo', viewId: 12 },
                { name: 'CIVIC SI', label: 'Civic SI', viewId: 10 }
            ]
        },
        hcm2: {
            name: 'HCM2',
            partIssueServiceUrl: 'http://127.0.0.1:9681',
            partIssueConfigurationIds: '[1]',
            models: [
                { name: 'CRV PETROL 4WD', label: 'CRV Petrol 4WD', viewId: 1 },
                { name: 'CRV PETROL 2WD', label: 'CRV Petrol 2WD', viewId: 2 },
                { name: 'CRV FHEV 4WD', label: 'CRV FHEV 4WD', viewId: 9 },
                { name: 'CRV FHEV 2WD', label: 'CRV FHEV 2WD', viewId: 12 },
            ]
        },
        hdmc: {
            name: 'HDMC',
            partIssueConfigurationIds: '[1]',
            models: [{ name: 'FRAME', label: 'HR-V', viewId: 9 },]
        },
        iap: {
            name: 'IAP',
            partIssueServiceUrl: 'http://127.0.0.1:9281',
            partIssueConfigurationIds: '[12]',
            models: [
                { name: 'CIVIC', label: 'Civic', viewId: 6 },
                { name: 'CRV', label: 'CRV', viewId: 1 },
                { name: 'INSIGHT', label: 'Insight', viewId: 4 }
            ]
        },
        map1: {
            name: 'MAP1',
            partIssueServiceUrl: 'http://127.0.0.1:9381',
            partIssueConfigurationIds: '[1, 3]',
            models: [
                { name: 'ACCORD', label: 'Accord', viewId: 1 },
                { name: 'ACCORD FHEV', label: 'Accord FHEV', viewId: 2 },
                { name: 'INTEGRA', label: 'Integra', viewId: 4 },
                { name: 'INTEGRA S', label: 'Integra S', viewId: 11 },
                { name: 'TLX', label: 'TLX', viewId: 9 },
                { name: 'TLX S', label: 'TLX S', viewId: 8 },
            ]
        },
        map2: {
            name: 'MAP2',
            partIssueServiceUrl: 'http://127.0.0.1:9381',
            partIssueConfigurationIds: '[82]',
            models: [
                { name: 'ACCORD 4DR FHEV', label: 'Accord 4DR FHEV', viewId: 10 },
                { name: 'ACCORD 4DR L4', label: 'Accord 4DR L4', viewId: 8 },
                { name: 'ACCORD 4DR V6', label: 'Accord 4DR V6', viewId: 9 }
            ]
        },
        ipusub: {
            name: 'IPUSUB(Beta)',
            partIssueServiceUrl: 'http://127.0.0.1:9381',
            partIssueConfigurationIds: '[82]',
            models: [
                { name: 'ACCORD 4DR FHEV', label: 'Accord 4DR FHEV', viewId: 10 },
                { name: 'ACCORD 4DR L4', label: 'Accord 4DR L4', viewId: 8 },
                { name: 'ACCORD 4DR V6', label: 'Accord 4DR V6', viewId: 9 }
            ]
        }
    }
};

(function (window) {
    window.__config = config;
}(this));
