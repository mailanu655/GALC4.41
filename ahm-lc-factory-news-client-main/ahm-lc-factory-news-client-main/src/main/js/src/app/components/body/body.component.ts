import { Component, OnInit, ViewChild, ViewChildren, QueryList, Input, ChangeDetectorRef, HostListener, Inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { interval, timer } from 'rxjs';
import { DataLoadService } from "src/app/services/data-load.service";
import { MatTableDataSource, MatTable } from '@angular/material/table';
import { MatMenuTrigger } from '@angular/material/menu';
import { ConfigService } from 'src/app/services/config.service';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { ParentComponent } from '../parent/parent.component';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { DataShareService } from 'src/app/services/data-share.service';
import { MatSidenav } from '@angular/material/sidenav';
import { SideNavService } from 'src/app/services/side-nav.service';
import { MAT_SNACK_BAR_DATA, MatSnackBar } from '@angular/material/snack-bar';
import { ConfigContentComponent } from '../config/config-content/config-content.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { KeycloakService } from 'src/app/services/keycloak.service';
import { FnConfigServiceService } from 'src/app/services/fn-config-service.service';
import { SecurityService } from 'src/app/services/security.service';
export interface DepartmentData {
  departmentName: string;
  plan: number;
  target: number;
  totalInventoryByDiv: number;
  processPointGroupData?: ProcessPointGroupData[] | MatTableDataSource<ProcessPointGroupData>;
  iconToggle: boolean;
  visible: boolean;
  departmentSequence: number;
  productType: string;
}
export interface ProcessPointGroupData {
  processPointGroup: string;
  processPointData: string;
  departmentName: string;
  currentInventory: number;
  sequenceNumber: number;
  plan: number;
  target: number;
  actual1st: number;
  actual2nd: number;
  actual3rd: number;
  actualTotal: number;
  difference: number;
  gapToAF: number;
}

export interface Metrics {
  name: string;
  metricsData?: MetricsData[] | MatTableDataSource<MetricsData>;
  iconToggle: boolean;
}

export interface MetricsData {
  deptName: string;
  metricName: string;
  metricType: string;
  countFirst: number;
  countSecond: number;
  countThird: number;
  unit: string;
  tooltip: string;
}

export enum MetricsType {
  STRAGGLER = 'STRAGGLER',
  STRAGGLER_COMBINED = 'STRAGGLER_COMBINED',
  OTS_FRAME = 'OTS_FRAME',
  OTS_FRAME_PERCENT = 'OTS_FRAME_PERCENT',
  RESHIP = 'RESHIP',
  REMAKE = 'REMAKE',
  AHM_RETURN = 'AHM_RETURN'
}

export const MetricsTypeTooltip = {
  [MetricsType.STRAGGLER]: 'Original production VIN passing a department “ON” point out of sequence from their assigned KD Lot.',
  [MetricsType.STRAGGLER_COMBINED]: 'Sum of Stragglers and Remakes.',
  [MetricsType.OTS_FRAME]: 'On Time Ship units - VIN’s shipping within 24 hours of AF OFF.',
  [MetricsType.OTS_FRAME_PERCENT]: 'OTS units/VQ Shipped units',
  [MetricsType.RESHIP]: 'Count of all VIN’s shipping for the 2nd or more time.  Each ship occurrence counts.',
  [MetricsType.REMAKE]: 'VIN’s created to replace original production VIN’s that have been Scrapped.',
  [MetricsType.AHM_RETURN]: 'VIN’s that appear more than once or return a count exceeding one..'
}

@Component({
  selector: 'app-countdown-snackbar',
  template: `<span>{{ data.value }}</span>`,
  styles: [`
      span {
        color: white;
      }
    `]
})
export class CountdownSnackbarComponent {
  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: any) { }
}

@Component({
  selector: 'app-body',
  templateUrl: './body.component.html',
  styleUrls: ['./body.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class BodyComponent extends ParentComponent implements OnInit {

  ACT: string = "ACT:";
  INV: string = "INV:";

  afDepartmentSeq: number = 0;

  prodDateInput: string = "";
  dataMode: string = "";
  isInventoryVisible: boolean = true;

  isActualSpinnerVisible: boolean = true;
  isInvSpinnerVisible: boolean = true;
  isMetricSpinnerVisible: boolean = true;
  //Actuals
  departmentData: (DepartmentData)[] = [];
  currentData: (DepartmentData)[] = [];
  dataSource: MatTableDataSource<DepartmentData>;
  @ViewChildren('innerTables') innerTables: QueryList<MatTable<ProcessPointGroupData>>;
  ppGroupDataSource: MatTableDataSource<ProcessPointGroupData>;
  ppGroupDataList: ProcessPointGroupData[] = [];

  expandedElement: DepartmentData | null;
  // reference to the MatMenuTrigger in the DOM 
  @ViewChild(MatMenuTrigger, { static: true }) matMenuTrigger: MatMenuTrigger;


  // === timer === //
  countDownTimer = interval(1000);
  timerValue: number = 0;
  lastRefreshed: number;
  // === subscriptions === //
  lastRefreshSubscription: any;
  countDownSubscription: any;

  //Table data
  innerColumnsToDisplay = ['Line', 'Actual 3rd', 'Gap 3rd', 'Actual 1st', 'Gap 1st', 'Actual 2nd', 'Gap 2nd', 'Actual Total', 'Difference', 'Gap To AF'];
  //column add/delete data
  masterCopyOfInnerColumn = ['Actual 3rd', 'Gap 3rd', 'Actual 1st', 'Gap 1st', 'Actual 2nd', 'Gap 2nd', 'Actual Total', 'Difference', 'Gap To AF'];
  //groupBy Columns
  groupByColumns: string[] = ['expandSymbol', 'department', 'plan', 'target'];

  colMapSeq = new Map<string, number>();

  //Current Inventory
  invDepartmentData: (DepartmentData)[] = [];
  invDataSource: MatTableDataSource<DepartmentData>;
  @ViewChildren('invInnerTables') invInnerTables: QueryList<MatTable<ProcessPointGroupData>>;
  invPpGroupDataSource: MatTableDataSource<ProcessPointGroupData>;
  invPpGroupDataList: ProcessPointGroupData[] = [];

  invExpandedElement: DepartmentData | null;
  // reference to the MatMenuTrigger in the DOM 
  @ViewChild(MatMenuTrigger, { static: true }) invMatMenuTrigger: MatMenuTrigger;

  //Table data
  invInnerColumnsToDisplay = ['line', 'current inventory'];
  //column add/delete data
  invMasterCopyOfInnerColumn = ['current inventory'];
  //groupBy Columns
  invGroupByColumns: string[] = ['expandSymbol', 'department', 'totalInventory']


  //Metrics
  metricsData: (Metrics)[] = [];
  showMetrics: boolean = false;
  metricsDataSource: MatTableDataSource<Metrics>;
  //Table data
  metricsInnerColumnsToDisplay = ['metricName', 'countFirst', 'countSecond', 'countThird'];
  //groupBy Columns
  metricsGroupByColumns: string[] = ['expandSymbol', 'title']
  @ViewChildren('metricsInnerTables') metricsInnerTables: QueryList<MatTable<MetricsData>>;
  metricsExpandedElement: Metrics | null;



  @Input() dataset: any = [];
  selectedSite: string = '';

  @ViewChild('sidenav') public sidenav: MatSidenav;
  userName: any;

  constructor(public router: Router,
    route: ActivatedRoute, public dataLoadService: DataLoadService,
    public config: ConfigService,
    public security: SecurityService,
    private dataShareService: DataShareService,
    private sidenavService: SideNavService,
    private cd: ChangeDetectorRef,
    public configDialog: MatDialog,
    private keycloakService: KeycloakService,
    private fnConfig: FnConfigServiceService,
    private snackBar: MatSnackBar) {
    super(config, route, router, dataLoadService);
    this.dataShareService.getCurrentMessage().subscribe(message => {
      this.prodDateInput = message.data;
      this.dataMode = message.mode;
      this.isActualSpinnerVisible = true;
      this.isInvSpinnerVisible = true;
      this.isMetricSpinnerVisible = true;
      this.loadData();
    });
  }

  async getFactoryNews() {
    let prodDate: string = this.formatDate(this.prodDateInput);
    this.dataLoadService.getFactoryNews(prodDate).subscribe(
      (result) => {
        this.departmentData = result;
        this.currentData = Object.assign([], result);
        var tmp = (window.localStorage.getItem(this.selectedSite) != null && window.localStorage.getItem(this.selectedSite) != undefined)
          ? window.localStorage.getItem(this.selectedSite) : '';
        this.departmentData.forEach(groupData => {
          if (groupData.departmentName.toLowerCase().includes("af")
            || groupData.departmentName.toLowerCase().includes("assembly")) {
            this.afDepartmentSeq = groupData.departmentSequence;
          }
          let visibleVar: string = window.localStorage.getItem(this.ACT + groupData.departmentName) as string;

          if (visibleVar) {
            groupData.visible = visibleVar.toLocaleLowerCase() == "true";
          } else {
            window.localStorage.setItem(this.ACT + groupData.departmentName, String(groupData.visible));
          }
          if (groupData.processPointGroupData && Array.isArray(groupData.processPointGroupData) && groupData.processPointGroupData.length) {
            groupData.processPointGroupData.forEach(tmpLineData => {
              this.ppGroupDataList.push(tmpLineData);
            });

            if (tmp!.indexOf(this.ACT + groupData.departmentName) >= 0) {
              groupData.iconToggle = false;
            } else {
              groupData.iconToggle = true;
            }
          } else {
            groupData.iconToggle = false;
          }
        });
        this.dataSource = new MatTableDataSource(this.departmentData);
        this.ppGroupDataSource = new MatTableDataSource(this.ppGroupDataList);
        this.isActualSpinnerVisible = false;
      });
  }

  async getFactoryNewsCurrentInventory() {
    let prodDate: string = this.formatDate(this.prodDateInput);
    this.dataLoadService.getFactoryNewsCurrentInventory(prodDate).subscribe(
      (result) => {
        this.invDepartmentData = result;
        var tmp = (window.localStorage.getItem(this.selectedSite) != null && window.localStorage.getItem(this.selectedSite) != undefined)
          ? window.localStorage.getItem(this.selectedSite) : '';
        this.invDepartmentData.forEach(groupData => {
          let visibleVar: string = window.localStorage.getItem(this.INV + groupData.departmentName) as string;

          if (visibleVar) {
            groupData.visible = visibleVar.toLocaleLowerCase() == "true";
          } else {
            window.localStorage.setItem(this.INV + groupData.departmentName, String(groupData.visible));
          }
          if (groupData.processPointGroupData && Array.isArray(groupData.processPointGroupData) && groupData.processPointGroupData.length) {
            groupData.processPointGroupData.forEach(tmpLineData => {
              this.invPpGroupDataList.push(tmpLineData);
            });

            if (tmp!.indexOf(this.INV + groupData.departmentName) >= 0) {
              groupData.iconToggle = false;
            } else {
              groupData.iconToggle = true;
            }
          } else {
            groupData.iconToggle = false;
          }
        });
        this.invDataSource = new MatTableDataSource(this.invDepartmentData);
        this.invPpGroupDataSource = new MatTableDataSource(this.invPpGroupDataList);
        this.isInvSpinnerVisible = false;
      });
  }

  async getMetricsData() {
    let prodDate: string = this.formatDate(this.prodDateInput);
    this.dataLoadService.getMetricsData(prodDate).subscribe(
      (result) => {
        this.metricsData = result;
        this.metricsData.forEach(data => {
          if (data.metricsData && Array.isArray(data.metricsData) && data.metricsData.length) {
            this.showMetrics = true;
            data.metricsData.forEach(metric => {
              if (metric.metricName.includes('%') || metric.metricName.toUpperCase().includes('PERCENT')) {
                if (metric.metricType === MetricsType.OTS_FRAME) {
                  metric.tooltip = MetricsTypeTooltip[MetricsType.OTS_FRAME_PERCENT];
                }
                metric.unit = '%';
              } else {
                metric.tooltip = MetricsTypeTooltip[metric.metricType];
              }
            });
          } else {
            this.showMetrics = false;
          }
          data.iconToggle = true;
        });
        this.metricsDataSource = new MatTableDataSource(this.metricsData);
        this.isMetricSpinnerVisible = false;
      });
  }

  async ngOnInit(): Promise<void> {
    const userDetailsString = localStorage.getItem('userDetails');
    if (userDetailsString) {
      try {
        const userDetails = JSON.parse(userDetailsString);
        this.userName = userDetails?.username ?? 'visitor';
      } catch (e) {
        console.error('Failed to parse user details from localStorage:', e);
        this.userName = 'visitor';
      }
    } else {
      this.userName = 'visitor';
    }
    this.getUserName();
    const config = localStorage.getItem('config');
    console.log("login vlaues",this.security.isLoggedIn());
    if (await this.security.isLoggedIn() && config === 'true') {
      this.openConfigPopUp();
    }

    this.selectedSite = this.config.siteId ? this.config.siteId : '';
    console.log("plant name", this.selectedSite);
    this.fnConfig.setPlant(this.selectedSite);
    localStorage.setItem('selectedSite', this.selectedSite);
    let delay = 1000 * this.config.refreshInterval;
    this.ppGroupDataList = [];
    this.invPpGroupDataList = [];
    this.lastRefreshSubscription = timer(0, delay).subscribe(res => {
      if (this.dataMode === "now") {
        this.loadData();
      }
    });
    this.countDownSubscription = this.countDownTimer.subscribe((n) => this.updateCountDown(n));
    let seq: number = 0;
    this.innerColumnsToDisplay.forEach(val => {
      this.colMapSeq.set(val, seq);
      seq++;
    })
    this.innerColumnsToDisplay.splice(1, 2);
  }

  async openConfigPopUp() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '90vw';
    dialogConfig.height = '98vh';
    dialogConfig.autoFocus = true;
    dialogConfig.position = { top: '7vh' };
    dialogConfig.data = {
    };
    const roles = ['factorynews_admin', 'factorynews_user']; 
    const canAccess =  this.security.validateRoles(roles);

    if (await canAccess) {
    const dialogRef = this.configDialog.open(ConfigContentComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(() => {
      localStorage.setItem('config', 'false');
    });
  }  else {
    alert('You do not have permission to access this configuration.');
  }
  }
  ngAfterViewInit(): void {
    this.sidenavService.setSidenav(this.sidenav);
  }

  updateCountDown(val: number) {
    this.timerValue = val;
    let countDown = this.config.refreshInterval - (this.timerValue - this.lastRefreshed);
    this.config.countDown = isNaN(countDown) ? 0 : countDown;
  }

  ngOnDestroy() {
    this.lastRefreshSubscription.unsubscribe();
    this.countDownSubscription.unsubscribe();
  }

  toggleRow(element: DepartmentData) {
    this.expandedElement = element;
    if (this.expandedElement.iconToggle)
      this.expandedElement.iconToggle = false;
    else
      this.expandedElement.iconToggle = true;
    this.cd.detectChanges();
    var expandedList: any;
    let tmp = window.localStorage.getItem(this.config.siteId);
    if (tmp == null || tmp == undefined) {
      window.localStorage.setItem(this.config.siteId, this.ACT + this.expandedElement.departmentName);
    } else {
      if (tmp.indexOf(this.ACT + element.departmentName) == -1) {
        if (!this.expandedElement.iconToggle) {
          tmp += "," + this.ACT + element.departmentName;
          window.localStorage.setItem(this.config.siteId, tmp);
        }
      } else {
        var tmpArray = tmp.split(',');
        var index = tmpArray.indexOf(this.ACT + this.expandedElement.departmentName);
        tmpArray.splice(index, 1);
        window.localStorage.setItem(this.config.siteId, String(tmpArray));
      }
    }
  }

  invToggleRow(element: DepartmentData) {
    this.invExpandedElement = element;
    if (this.invExpandedElement.iconToggle)
      this.invExpandedElement.iconToggle = false;
    else this.invExpandedElement.iconToggle = true;
    this.cd.detectChanges();
    var expandedList: any;
    let tmp = window.localStorage.getItem(this.config.siteId);
    if (tmp == null || tmp == undefined) {
      window.localStorage.setItem(this.config.siteId, this.INV + this.invExpandedElement.departmentName);
    } else {
      if (tmp.indexOf(this.INV + element.departmentName) == -1) {
        if (!this.invExpandedElement.iconToggle) {
          tmp += "," + this.INV + element.departmentName;
          window.localStorage.setItem(this.config.siteId, tmp);
        }
      } else {
        var tmpArray = tmp.split(',');
        var index = tmpArray.indexOf(this.INV + this.invExpandedElement.departmentName);
        tmpArray.splice(index, 1);
        window.localStorage.setItem(this.config.siteId, String(tmpArray));
      }
    }
  }

  metricsToggleRow(element: Metrics) {
    this.metricsExpandedElement = element;
    if (this.metricsExpandedElement.iconToggle)
      this.metricsExpandedElement.iconToggle = false;
    else this.metricsExpandedElement.iconToggle = true;
    this.cd.detectChanges();
  }

  menuTopLeftPosition = { x: '0px', y: '0px' };

  onRightClick(event: MouseEvent, item) {
    // preventDefault avoids to show the visualization of the right-click menu of the browser 
    event.preventDefault();
    // we record the mouse position in our object 
    this.menuTopLeftPosition.x = event.clientX + 'px';
    this.menuTopLeftPosition.y = event.clientY + 'px';
    // we pass to the menu the information about our object 
    this.matMenuTrigger.menuData = { item: item };
    // we open the menu 
    this.matMenuTrigger.openMenu();
  }

  onContextMenuAction(item: string) {
    var index = this.innerColumnsToDisplay.indexOf(item);
    //remove the column
    if (index != -1) {
      this.innerColumnsToDisplay.splice(index, 1);
    } else {
      //insert the column
      index = this.masterCopyOfInnerColumn.indexOf(item);
      this.innerColumnsToDisplay.splice(index + 1, 0, item);
      this.innerColumnsToDisplay.sort((n1, n2) => {
        let val1: number = this.colMapSeq.get(n1) as number;
        let val2: number = this.colMapSeq.get(n2) as number;

        if (val1 > val2) {
          return 1;
        }

        if (val1 < val2) {
          return -1;
        }
        return 0;
      });
    }
  }

  showDepartment(event: MatCheckboxChange, type: string): void {
    let deptName: string = event.source.value;
    let isChecked: string = String(event.checked);
    window.localStorage.setItem(type + deptName, isChecked);
  }

  @HostListener('document:keydown', ['$event'])
  keydownHandler(event: KeyboardEvent): void {
    if (event && event.ctrlKey && event.keyCode === 82) {
      window.localStorage.clear();
    }
  }

  loadData() {
    this.dataLoadService.checkSiteStatus().subscribe(
      (result) => {
        if (result) {
          this.snackBar.dismiss();
          this.getFactoryNews();
          this.getFactoryNewsCurrentInventory();
          this.getMetricsData();
        } else {
          this.handleSiteStatusError();
        }
        //countdown and last refresh
        this.lastRefreshed = this.timerValue;
        this.config.lastRefreshed = new Date();
        this.config.isSiteConnected = result;
        this.config.siteMessage = result ? 'Site is online' : 'Site is offline';
      },
      (error) => {
        console.log('Error:', error);
        this.handleSiteStatusError();
        //countdown and last refresh
        this.lastRefreshed = this.timerValue;
        this.config.lastRefreshed = new Date();
        this.config.isSiteConnected = false;
        this.config.siteMessage = 'Site is offline';
      }
    );
  }

  private handleSiteStatusError() {
    this.snackBar.openFromComponent(CountdownSnackbarComponent, {
      duration: 60000,
      data: { value: 'This site is currently not available. Trying to reconnect...' },
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: ['snackbar-view']
    });
    if (this.dataSource?.data?.length !== undefined && this.dataSource.data.length > 0) {
      this.dataSource.data = [];
    }
    if (this.invDataSource?.data?.length !== undefined && this.invDataSource.data.length > 0) {
      this.invDataSource.data = [];
    }
    if (this.metricsDataSource?.data?.length !== undefined && this.metricsDataSource.data.length > 0) {
      this.metricsDataSource.data = [];
    }
    this.isActualSpinnerVisible = false;
    this.isInvSpinnerVisible = false;
    this.isMetricSpinnerVisible = false;
  }

  formatDate(date) {
    if (date) {
      var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

      if (month.length < 2) month = '0' + month;
      if (day.length < 2) day = '0' + day;

      return [year, month, day].join('-');
    } else {
      return "";
    }
  }
  private loadUserProfile(): void {
    // this.keycloakService.loadUserProfile().then(profile => {
    //   console.log('User profile loaded:', this.userName);
    // }).catch(err => {
    //   console.error('Failed to load user profile after token refresh:', err);
    // });

  }
  getUserName(): void {
    const userDetailsString = localStorage.getItem('userDetails');

    if (userDetailsString) {
      try {
        const userDetails = JSON.parse(userDetailsString);
        this.userName = userDetails?.username ?? 'visitor';
      } catch (e) {
        console.error('Failed to parse user details from localStorage:', e);
        this.userName = 'visitor';
      }
    } else {
      this.userName = 'visitor';
    }
  }


}



