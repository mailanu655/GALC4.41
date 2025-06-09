import { Component, OnInit } from '@angular/core';
import { ConfigService } from 'src/app/services/config.service';
import { ConfigContentComponent } from '../config/config-content/config-content.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { FnConfigServiceService } from 'src/app/services/fn-config-service.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private config: ConfigService, public configDialog: MatDialog, private fnConfig: FnConfigServiceService, private router: Router) { }

  async ngOnInit(): Promise<void> {
    this.config.siteId = null!;
    const userDetailsString = localStorage.getItem('userDetails');

    if (await this.fnConfig.isLoggedIn()) {
      const plant = localStorage.getItem('plant')?.replace(/['"]/g, ''); // Removes quotation marks
      if (plant) {
        this.router.navigate([`/${plant}`]);
      } else {
        console.error('Plant not found in localStorage');
      }
    }
    // if (await this.fnConfig.isLoggedIn()) {
    //   this.openConfigPopUp();
    // }
  }
  openConfigPopUp() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '90vw';
    dialogConfig.height = '98vh';
    dialogConfig.autoFocus = true;
    dialogConfig.position = { top: '7vh' };
    dialogConfig.data = {
    };
    const dialogRef = this.configDialog.open(ConfigContentComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(() => {
      localStorage.setItem('showmethod', 'false');
      localStorage.setItem('config', 'false');
      console.log('Dialog closed. MethodCall set to false.');
    });
  }

  get logoMessage(): string {
    return this.config.logoMessage;
  }

}