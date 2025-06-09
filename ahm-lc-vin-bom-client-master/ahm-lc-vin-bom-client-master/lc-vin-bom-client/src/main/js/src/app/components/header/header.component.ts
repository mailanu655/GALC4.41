import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SecurityService } from '../../services/security.service';
import { ConfigService } from 'src/app/services/config.service';
import { ActivatedRoute, Router } from '@angular/router';

import { Resource } from 'src/app/services/constants';
import { AboutComponent } from '../about/about.component';
import * as html2canvas from 'html2canvas';
import { Feedback } from '../feedback/feedback.component';

@Component({
  selector: 'header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(public dialog: MatDialog, public configService: ConfigService, private securityService: SecurityService, private route: Router) { }

  ngOnInit(): void {
    console.log("APPENV - "+this.configService.appenv);
    console.log("hostName - "+this.configService.hostname);

    let selectedSite:string;

    if(this.configService.appenv.startsWith('localhost')){
      selectedSite=this.configService.hostname;
    }else{
      selectedSite=this.configService.appenv;
    }
    
    if(this.configService.sitesIds.includes(selectedSite,0)){
      console.log(" siteId found - "+selectedSite);
      this.updateSite(this.configService.appenv);
    }else{
        
      this.updateSite('map');
    }
  }

  login() {
    this.securityService.login();
  }

  logout() {
    this.securityService.logout();
  }

  getUser() {
    return this.securityService.getUser();
  }

  isUserLoggedIn(): boolean {
    if (this.securityService.isUseSecurity() == false) {
        return false;
    }
    return this.securityService.isLoggedIn();
  }

  isUserLoggedOut(): boolean {
    if (this.securityService.isUseSecurity() == false) {
        return false;
    }
    return !this.securityService.isLoggedIn();
  }

  isUserInRole(roles: string): boolean {
    return this.securityService.isUserInRole(roles);
  }

  openAboutDialog() {
    this.dialog.open(AboutComponent)
  }

  openHelp() {
    window.open(this.configService.helpUrl, "_blank");
  }

  openRevisionHistory() {
    window.open(this.configService.revisionHistory, "_blank");
  }

  get menuItems() {
    let menuItems = [Resource.JOB, Resource.PROFILE];
    return menuItems;
  }

  get appName() {
    let name = this.configService.appName;
    if (name) {
        return name;
    } else {
        return "SUMS";
    }
  }
 
  updateSite(value:string){
    
        this.configService.siteId = value;
        this.configService.serviceUrl = this.configService.restUrl;
        
        console.log("site-"+ this.configService.siteId +", rest Url - "+ this.configService.serviceUrl);

        //this.configService.keyCloakClientId = this.configService.keyCloakClient;
        //this.configService.keyCloakUrl = this.configService.keyCloakUrl;
        
       // console.log("site-"+ this.configService.siteId +", keyCloakClient - "+ this.configService.keyCloakClientId );

        this.route.navigate(['/']);
  }

  openMenu(value:string){
    console.log(value);
    let path:string;

    switch(value){
      case 'Part Assignment':
        path='/part-assignment';
        break;
      case 'Rule Assignment':
        path='/ruleassignment';
        break;
      case 'Lot Assignment':
        path='/lot-assignment';
        break;
      case 'Rule/Lot Approval':
        path='/rulelotapproval';
        break;
      case 'Rule/Lot Maintenance':
        path='/rulelotmaintenance';
        break;
      case 'Straggler Int':
        path='/interchangeable';
        break;
      case 'Manual Override':
        path='/manualoverride';
        break;
      case 'Manual Override Approval':
        path='/manualoverrideapproval';
        break; 
      case 'LET Partial Pass':
        path='/category-maint';
        break;   
      default:
        path='/';
    }
      console.log(path);
      this.route.navigate([path]);
   

  }
  async openFeedbackForm() {
    await this.capture();
    this.dialog.open(Feedback, {
      width: '58vw',
      height: '87vh'
    });
  }

  capture()
  {
    html2canvas(document.body).then(canvas => {
      // It open an image in new tab.
      localStorage.setItem('captureImage', canvas.toDataURL());
      //window.open().document.write('<img src="' + canvas.toDataURL() + '" />')
    });    
  }
}
