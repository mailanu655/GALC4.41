import { Injectable } from '@angular/core';

export class RestResource {
  url: string;
  methods: string[];
}

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  appName: string = 'Lot Sequencing';
  appShortName: string = 'LS';
  siteId: string = 'ahm';
  siteName: string = 'DEV AHM';
  logoMessage: string | undefined;
  logoShortMessage: string | undefined;
  url: string | undefined;
  serviceUrl: string | undefined;
  galcUrl: string;
  helpUrl: string | undefined;
  feedbackUrl: string | undefined;
  infoUrl: string | undefined;
  securityServiceUrl: string = '';
  securityRealm: string = '';
  securityResource: string = '';
  hidPort?: number;
  vdbUrl?: string;
  keyCloakUrl?: string;
  keyCloakRealm?: string;
  keyCloakClientId?: string;
  debug?: boolean = false;
  restSecurityEnabled?: boolean;
  restSecurityResources?: RestResource[];
  minPollingInterval?: number;
  emailGrps?: any;
  feedbackTypes?: any;
  feedbackClientUrl?: string;
  feedbackBaseUrl?: string;
  revisionHistoryUrl?: string;
  baseUrl?: string;
  scheduleUrl?: string;
  columns?: any;
  count: number;
  productType: string;
  isCommentEditAllowed: boolean;
  processLocation?: string;
  isSentToWeldOnEnabled?: boolean;
  default?: string;

  constructor() {
    const config: any = (window as any)['__config'] || {};
    Object.assign(this, config);
  }

  getColumns() {
    this.columns = [
      { columnDef: 'select', header: 'Select All/None', isShown: true },
      { columnDef: 'position', header: 'Position', isShown: true },
      { columnDef: 'seqNumber', header: 'Sequence', isShown: false },
      { columnDef: 'productionLot', header: 'Production Lot', isShown: true },
      { columnDef: 'kdLotNumber', header: 'KD Lot', isShown: true },
      { columnDef: 'lotSize', header: 'Lot Size', isShown: true },
      {
        columnDef: 'productionDate',
        header: 'Production Date',
        isShown: false,
      },
      { columnDef: 'productSpecCode', header: 'SPEC', isShown: false },
      { columnDef: 'modelCode', header: 'Model', isShown: true },
      { columnDef: 'modelType', header: 'Type', isShown: true },
      { columnDef: 'comments', header: 'Comments Options', isShown: true },
      { columnDef: 'notes', header: 'Notes', isShown: true },
      { columnDef: 'editing', header: 'Editing', isShown: true },
    ];

    return this.columns;
  }
}
