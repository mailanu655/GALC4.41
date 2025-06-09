import { Component, HostListener, Inject, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { AboutComponent } from '../about/about.component';
import { ConfigService } from 'src/app/services/config.service';
import { Feedback } from 'src/app/components/feedback/feedback.component';
import html2canvas from 'html2canvas';
import { DataShareService } from 'src/app/services/data-share.service';
import { FormControl } from '@angular/forms';
import { SideNavService } from 'src/app/services/side-nav.service';
import { DOCUMENT } from '@angular/common';
import { interval, Subscription } from 'rxjs';
import { DataLoadService } from 'src/app/services/data-load.service';
import { Router } from '@angular/router';
import { ConfigContentComponent } from '../config/config-content/config-content.component';
import { KeycloakService } from 'src/app/services/keycloak.service';
import { FnConfigServiceService } from 'src/app/services/fn-config-service.service';
import { SecurityService } from 'src/app/services/security.service';
const headers = new HttpHeaders()
  .set('content-type', 'application/json')
  .set('Access-Control-Allow-Origin', '*');

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  model: any = {};
  factoryNewsMode: string;
  public prodDateControl: FormControl;
  maxDate: Date;
  private updateSubscription: Subscription;
  public dbStatus: string;
  feedbackTitle: string;
  isAuthenticated = false;
  constructor(
    private dialog: MatDialog,
    private security: SecurityService,
    public aboutDialog: MatDialog,
    public config: ConfigService,
    private dataShareService: DataShareService,
    private dataLoadService: DataLoadService,
    private sidenavService: SideNavService,
    private router: Router,
    public configDialog: MatDialog,
    private fnCofig: FnConfigServiceService,
    @Inject(DOCUMENT) private document: Document
  ) {
    this.maxDate = new Date();
    this.prodDateControl = new FormControl(new Date());
    this.factoryNewsMode = "now";
    this.feedbackTitle = this.config.appName;
  }

  ngOnInit(): void {
    this.updateSubscription = interval(1800000).subscribe((val) => {
      if (this.factoryNewsMode === "now") {
        this.prodDateControl.setValue(new Date());
      }
    });
    this.model.theme = 'responsive';
    this.loadStyle(this.theme);
  }

  openHelp() {
    window.open(this.config.helpUrl, "_blank");
  }

  openAboutDialog() {
    this.aboutDialog.open(AboutComponent, {
      width: '50vw',
      height: '45vh'
    });
  }

  async openFeedbackForm() {
    await this.capture();
    this.aboutDialog.open(Feedback, {
      width: '58vw',
      height: '87vh'
    });
  }

  capture() {
    html2canvas(document.body).then((canvas) => {
      localStorage.setItem('captureImage', canvas.toDataURL());
    });
  }

  onSubmit() {
    // Add your form submission logic here if needed
  }

  onProdDateSelected() {
    this.setFactoryNewsMode();
    if (this.factoryNewsMode === "now") {
      this.sendMessage("");
    } else {
      this.sendMessage(this.prodDateControl.value);
    }
  }

  onProdDateReset() {
    this.prodDateControl.setValue(new Date());
    this.setFactoryNewsMode();
    this.sendMessage("");
  }

  setFactoryNewsMode() {
    let prodDate: Date = new Date(this.prodDateControl.value);
    let today: Date = new Date();
    this.factoryNewsMode = prodDate.toDateString() === today.toDateString() ? "now" : "history";
  }

  sendMessage(data) {
    let msg: any = {};
    msg.data = data;
    msg.mode = this.factoryNewsMode;
    this.dataShareService.changeMessage(msg);
  }

  toggleRightSidenav() {
    this.sidenavService.toggle();
  }

  openReleaseNotesDetails() {
    window.open(this.config.releaseNotesUrl, "_blank");
  }

  setTheme(theme: string) {
    this.model.theme = theme;
    this.loadStyle(this.theme);
  }

  loadStyle(styleName: string) {
    this.model.loadedTheme = styleName;
    let styleRef = `./assets/css/${styleName}.css`;
    const head = this.document.getElementsByTagName('head')[0];
    let themeLink = this.document.getElementById('client-theme') as HTMLLinkElement;
    if (themeLink) {
      themeLink.href = styleRef;
    } else {
      const style = this.document.createElement('link') as HTMLLinkElement;
      style.id = 'client-theme';
      style.rel = 'stylesheet';
      style.href = styleRef;
      head.appendChild(style);
    }
  }

  get theme(): string {
    return this.model.theme;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: { target: { innerWidth: any } }) {
    if (this.model.loadedTheme !== this.theme) {
      this.loadStyle(this.theme);
    }
  }

  get appName(): string {
    if (window.innerWidth < 900) {
      return this.config.appShortName;
    } else {
      return this.config.appName;
    }
  }

  async openFeedback(feedbackUrl: string | undefined, appName: string | undefined) {
    if (!feedbackUrl || !appName) {
      return;
    }
    const canvas: HTMLCanvasElement = await html2canvas(document.body);
    const imageData: string = canvas.toDataURL();
    const feedbackWindow = window.open(feedbackUrl + '?' + 'appName=' + appName);

    setTimeout(() => {
      feedbackWindow!.postMessage(imageData, feedbackUrl);
    }, 3000);
  }

  openLogin() {
    localStorage.setItem('showmethod', 'true');
    const showPop = localStorage.getItem('showPop');
    if (showPop == 'true') {
      this.openConfigPopUp();
    }
    // this.login(config);
  }
  openConfigPopUp() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '90vw';
    dialogConfig.height = '98vh';
    dialogConfig.autoFocus = true;
    dialogConfig.position = { top: '7vh' };
    dialogConfig.data = {
    };

    dialogConfig.panelClass = 'hide-dialog-footer';
    const dialogRef = this.configDialog.open(ConfigContentComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(result => {
      localStorage.setItem('config', 'false');
    });
  }
  login(): void {
    console.log("login done");
    localStorage.setItem('showPop', 'true');
    this.security.login();
  }
}
