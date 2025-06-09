import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AboutComponent } from '../dialog/about/about.component';
import { NgxCaptureService } from 'ngx-capture';
import { DomSanitizer } from '@angular/platform-browser';
import html2canvas from 'html2canvas';
import Application from '../entities/Application';
import { LineChartComponent } from '../graphs/linegraph/linegraph.component';
import { BarChartComponent } from '../graphs/bargraph/bargraph.component';
import { ConfigService } from 'src/app/services/config.service';
import { SecurityService } from 'src/app/services/security.service';
import { ApplicationService } from 'src/app/services/data/application.service';
import { ControlsService } from 'src/app/services/controls.service';
import { LogService } from 'src/app/services/log.service';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  }),
};

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
  @Output() public sidenavToggle = new EventEmitter();
  @Output() public screenShot = new EventEmitter();

  imgSrc: string;
  message: string;
  feedbackTitle: string;
  userDetails: any;
  applications: Application[];

  constructor(
    //private ipcService: IpcService,
    private securityService: SecurityService,
    private controlsService: ControlsService,
    private logService: LogService,
    public router: Router,
    public aboutDialog: MatDialog,
    public config: ConfigService,
    public captureService: NgxCaptureService,
    public sanitizer: DomSanitizer,
    public http: HttpClient,
    public appService: ApplicationService
  ) {
    this.feedbackTitle = this.config.appName;
  }

  public openSidenav = () => {
    this.controlsService.openPanel(true);
  };

  public openDialog() {
    this.logService.gui('Started openDialog()', [{ method: 'openDialog' }]);
    this.receive();
  }

  public openNotepad() {
    this.logService.gui('Started openNotepad()', [{ method: 'openNotepad' }]);
  }

  public receive() {
    this.logService.gui('Started receive()', [{ method: 'receive' }]);
  }

  login() {
    this.logService.gui('Started login()', [{ method: 'login' }]);
    this.securityService.login();
  }

  logout() {
    this.logService.gui('Started logout()', [{ method: 'logout' }]);

    if (localStorage.getItem('userDetails')) {
      localStorage.removeItem('userDetails');
    }
    if (localStorage.getItem('userclients')) {
      localStorage.removeItem('userclients');
    }
    this.securityService.logout();
  }

  getUser() {
    let user;
    if (localStorage.getItem('userDetails')) {
      this.userDetails = JSON.parse(localStorage.getItem('userDetails'));
      user =
        this.userDetails.firstName +
        ' ' +
        this.userDetails.lastName +
        '(' +
        this.securityService.getUser() +
        ')';
    }
    return user;
  }

  isUserLoggedIn(): boolean {
    if (!this.securityService.isUseSecurity()) {
      return false;
    }
    return this.securityService.isLoggedIn();
  }

  isUserLoggedOut(): boolean {
    if (!this.securityService.isUseSecurity()) {
      return false;
    }
    return !this.securityService.isLoggedIn();
  }

  isUserInRole(roles: string): boolean {
    return this.securityService.isUserInRole(roles);
  }
  openHelp() {
    this.logService.gui('Started openHelp()', [{ method: 'openHelp' }]);
    window.open(this.config.helpUrl, '_blank');
  }

  openRevisionHistory() {
    this.logService.gui('Started openRevisionHistory()', [
      { method: 'openRevisionHistory' },
    ]);
    window.open(this.config.revisionHistoryUrl, '_blank');
  }

  openAboutDialog() {
    this.logService.gui('Started openAboutDialog()', [
      { method: 'openAboutDialog' },
    ]);
    this.aboutDialog.open(AboutComponent, {
      width: '50vw',
      height: '45vh',
    });
  }

  async getProfile() {
    await this.securityService.getUserProfile();
  }

  async openFeedback(
    feedbackUrl: string | undefined,
    appName: string | undefined
  ) {
    if (!feedbackUrl || !appName) {
      return;
    }
    const canvas: HTMLCanvasElement = await html2canvas(document.body);
    const imageData: string = canvas.toDataURL();
    const feedbackWindow = window.open(
      feedbackUrl + '?' + 'appName=' + appName
    );

    setTimeout(() => {
      feedbackWindow.postMessage(imageData, feedbackUrl);
      this.logService.gui('Sent screenshot to feedback window', [
        { method: 'openFeedback' },
      ]);
    }, 3000);
  }

  getApplicationName(appName) {
    return appName.split(' ')[1];
  }

  openSite(appUrl) {
    this.logService.gui('Started openSite()', [{ method: 'openSite' }]);
    window.open(appUrl, '_blank');
  }

  openLineGraph() {
    this.logService.gui('Started openLineGraph()', [
      { method: 'openLineGraph' },
    ]);
    this.aboutDialog.open(LineChartComponent, {
      width: '60vw',
      height: '70vh',
    });
  }

  openBarGraph() {
    this.logService.gui('Started openBarGraph()', [{ method: 'openBarGraph' }]);
    this.aboutDialog.open(BarChartComponent, {
      width: '60vw',
      height: '70vh',
    });
  }

  openMenu(value: string) {
    this.logService.gui('Started openMenu()' + value, [{ method: 'openMenu' }]);
    this.getProfile();
    let path: string;

    switch (value) {
      case 'Sched Rep Maint':
        path = '/schedule-rep-tbx';
        break;
      case 'Lot Sequencing':
        path = '/containers/weld-schedule';
        break;
      default:
        path = '/';
    }

    this.logService.gui('Started navigate to path:' + path, [
      { method: 'openMenu' },
    ]);
    this.router.navigate([path]);
  }
}
