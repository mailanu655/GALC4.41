import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { configServiceInstance } from 'src/app/constants';
import { unfrozenLotsData, frozenData } from 'src/app/mocks';
import {
  WeldScheduleModel,
  Message,
} from 'src/app/models/weld-schedule.interface';
import { LogService, LotsService } from 'src/app/services';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationPopupComponent } from 'src/app/components/confirmation-popup/confirmation-popup.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import * as _ from 'underscore';
import { ConfigService } from 'src/app/services/config.service';
import { SecurityService } from 'src/app/services/security.service';
@Component({
  selector: 'weld-schedule',
  templateUrl: './weld-schedule.component.html',
  styleUrls: ['./weld-schedule.component.css'],
})
export class WeldScheduleComponent implements OnInit {
  frozenDataSource: WeldScheduleModel[] = frozenData;
  unfrozenLotsDataSource: WeldScheduleModel[] = unfrozenLotsData;
  holdLotsDataSource: WeldScheduleModel[] = [];
  isDownloadInprogress = false;
  isLoadingAllLotData = true;
  isLoadingFrozenData = true;
  isLoadingHoldData = true;
  plantLots: string[] = [];
  processLocations: string[] = [];
  selectedPlanCode: string = '';
  selectedProcessLocation: string = this.config.processLocation;
  commentsOptions: string[] = [];
  productSendStatuses: string[] = [];
  message: Message;
  columns = configServiceInstance.getColumns();
  frozenColumns = _.map(this.columns, _.clone);
  unfrozenColumns = _.map(this.columns, _.clone);
  holdColumns = _.map(this.columns, _.clone);

  downloadMessage: any;

  constructor(
    private config: ConfigService,
    private lotsService: LotsService,
    private logService: LogService,
    private cd: ChangeDetectorRef,
    public dialog: MatDialog,
    public snackBar: MatSnackBar,
    private securityService: SecurityService
  ) {}

  refreshFrozenDataSource(event) {
    // todo: after pagination api is getting ready, call the api to reset the data source
    this.populateDataSource(event);
  }

  refreshUnfrozenLotsDataSource(event) {
    // todo: after pagination api is getting ready, call the api to reset the data source
    this.populateDataSource(event);
  }

  refreshHoldLotsDataSource(event) {
    // todo: after pagination api is getting ready, call the api to reset the data source
    this.populateDataSource(event);
  }

  downloadLots() {
    if (this.selectedPlanCode) {
      const dialogRef = this.dialog.open(ConfirmationPopupComponent, {
        data: {
          message: 'Download Galc Lots',
          confirmationContent: 'Do you confirm to download lots from GALC?',
          buttonText: { ok: 'Ok', cancel: 'Cancel' },
          confirmData: true,
        },
      });

      this.logService.gui('Open download Galc Lots dialog', [{
        method: 'downloadLots',
      }]);

      dialogRef.afterClosed().subscribe((result) => {
        if (result) {
          const startTime: any = new Date();

          this.isDownloadInprogress = true;
          this.isLoadingAllLotData = true;
          this.isLoadingFrozenData = true;
          this.isLoadingHoldData = true;
          this.lotsService.downloadLotData(this.selectedPlanCode,this.selectedProcessLocation).subscribe({
            next: (data: any) => {
              const endTime: any = new Date();
              this.message = data;

              if (this.message.message === 'SUCCESS') {
                this.snackBar.open('Downloaded lots successfully!', 'ok', {
                  duration: 5000,
                });
                this.logService.debug('downloaded lots successfully', [{
                  method: 'downloadLots',
                  duration: endTime - startTime,
                }]);
                this.populateData(this.selectedProcessLocation);
              } else {
                this.logService.fatal('download lots failed', [{
                  method: 'downloadLots',
                  duration: endTime - startTime,
                }]);
                this.snackBar.open('Downloaded lots failed!', 'ok', {
                  duration: 5000,
                });
                this.logService.info('download lots failed', [{
                  method: 'downloadLots',
                  duration: endTime - startTime,
                }]);
              }
            },
            error: (err: any) => {
              const endTime: any = new Date();
              this.logService.fatal('download lots failed' + err, [{
                method: 'downloadLots',
                duration: endTime - startTime,
              }]);
            },
            complete: () => {
              this.isDownloadInprogress = false;
            },
          });
        }
      });
    }
  }

  populateData(location: string) {
    const startTime: any = new Date();

    this.lotsService
      .getUnfrozenLots(this.selectedPlanCode, location)
      .subscribe({
        next: (data: WeldScheduleModel[]) => {
          const endTime: any = new Date();

          if (data) {
            this.unfrozenLotsDataSource = data.filter((ele: any) => ele.lotSize > 0);
            this.logService.debug('fetching unfrozen lots successfully', [{
              method: 'populateData',
              duration: endTime - startTime,
            }]);
            this.cd.detectChanges();
          }
        },
        error: (err) => {
          const endTime: any = new Date();

          this.logService.fatal('fetching unfrozen lots failed error: ' + err, [{
            method: 'populateData',
            duration: endTime - startTime,
          }]);
        },
        complete: () => {
          this.isLoadingAllLotData = false;
        },
      });

    this.lotsService.getFrozenLots(this.selectedPlanCode, location).subscribe({
      next: (data: WeldScheduleModel[]) => {
        if (data) {
          const endTime: any = new Date();

          this.frozenDataSource = data.filter((ele: any) => ele.lotSize > 0);
          this.logService.debug('fetching frozen lots successfully', [{
            method: 'populateData',
            duration: endTime - startTime,
          }]);
          this.cd.detectChanges();
        }
      },
      error: (err) => {
        const endTime: any = new Date();

        this.logService.fatal('fetching frozen lots failed' + err, [{
          method: 'populateData',
          duration: endTime - startTime,
        }]);
      },
      complete: () => {
        this.isLoadingFrozenData = false;
      },
    });

    this.lotsService.getHoldLots(this.selectedPlanCode, location).subscribe({
      next: (data: WeldScheduleModel[]) => {
        if (data) {
          const endTime: any = new Date();

          this.holdLotsDataSource =  data.filter((ele: any) => ele.lotSize > 0);
          this.logService.debug('fetching hold lots successfully', [{
            method: 'populateData',
            duration: endTime - startTime,
          }]);
          this.cd.detectChanges();
        }
      },
      error: (err) => {
        const endTime: any = new Date();

        this.logService.fatal('fetching hold lots failed error: ' + err, [{
          method: 'populateData',
          duration: endTime - startTime,
        }]);
      },
      complete: () => {
        this.isLoadingHoldData = false;
      },
    });
  }

  onSelectPlanCode(event) {
    if (event.planCode) {
      this.selectedPlanCode = event.planCode;
    }

    this.logService.gui('Selected plan code', [{
      method: 'onSelectPlanCode',
    }]);

    this.populateDataSource({value: this.selectedProcessLocation});
  }

  populateDataSource(event) {
    this.logService.gui('Populate data source', [{
      method: 'populateDataSource',
    }]);

    if (event.planCode) {
      this.selectedPlanCode = event.planCode;
    }
    if (event.processLocation) {
      this.selectedProcessLocation = event.processLocation;
    }
    if (event.value) {
      this.selectedProcessLocation = event.value;
    }
    this.populateData(this.selectedProcessLocation);
  }

  ngOnInit(): void {
    const startTime: any = new Date();

    this.lotsService.getPlanCodes().subscribe({
      next: (data: string[]) => {
        const endTime: any = new Date();

        this.plantLots = data;
        this.logService.debug('fetching plan codes successfully', [{
          method: 'ngOnInit',
          duration: endTime - startTime,
        }]);
      },
      error: (err) => {
        const endTime: any = new Date();

        this.logService.fatal('fetching plan codes failed' + err, [{
          method: 'ngOnInit',
          duration: endTime - startTime,
        }]);
      },
    });

    this.lotsService.getProcessLocations().subscribe({
      next: (data: string[]) => {
        const endTime: any = new Date();

        this.processLocations = data;
        this.logService.debug('fetching process locations successfully', [{
          method: 'ngOnInit',
          duration: endTime - startTime,
        }]);
      },
      error: (err) => {
        const endTime: any = new Date();

        this.logService.fatal('fetching process locations failed' + err, [{
          method: 'ngOnInit',
          duration: endTime - startTime,
        }]);
      },
    });

    this.lotsService.getBuildComments().subscribe({
      next: (data) => {
        const endTime: any = new Date();

        this.commentsOptions = data;
        this.logService.info('Successfully fetched the build comments data.', [{
          method: 'ngOnInit',
          duration: endTime - startTime,
        }]);
      },
      error: (err) => {
        const endTime: any = new Date();

        this.logService.fatal('Failed to fetch the build comments.' + err, [{
          method: 'ngOnInit',
          duration: endTime - startTime,
        }]);
      },
    });

    this.lotsService.getProductSendStatuses().subscribe({
      next: (data) => {
        const endTime: any = new Date();

        this.productSendStatuses = data;
        this.logService.info('Successfully fetched the send statuses.', [{
          method: 'ngOnInit',
          duration: endTime - startTime,
        }]);
      },
      error: (err) => {
        const endTime: any = new Date();

        this.logService.fatal('Failed to fetch the send statuses.' + err, [{
          method: 'ngOnInit',
          duration: endTime - startTime,
        }]);
      },
    });
  }
  
  isUserInRole(roles: string): boolean {
    return this.securityService.isUserInRole(roles);
  }
}
