import { Component, ViewChild } from '@angular/core';
import Chart from 'chart.js/auto';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import ApplicationUsage from '../../entities/ApplicationUsage';
import { DatePipe } from '@angular/common'
import { ConfigService } from 'src/app/services/config.service';
import { LogService } from 'src/app/services/log.service';
const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*',
  }),
};

@Component({
  selector: 'app-line-chart',
  templateUrl: './linegraph.component.html',
  styleUrls: ['./linegraph.component.css']
})
export class LineChartComponent {
  applicationsUsage: ApplicationUsage[];
  formattedUsageDetails: ApplicationUsage[];
  dataSource: Object;
  title: string;
  public chart: any;
  usageDetails: ApplicationUsage[];
  dateSet: any;
  appSet: any;
  usageMap: any;
  frmDatePick: Date;
  toDatePick: Date;

  constructor(private http: HttpClient, private config: ConfigService, private logService: LogService,
    private datePipe: DatePipe) {
    this.frmDatePick = new Date();
    this.toDatePick=new Date();
    this.frmDatePick.setDate(this.frmDatePick.getDate()-30);
    this.toDatePick.setDate(this.toDatePick.getDate());
    this.getApplicationsUsageDetails();
  }

  createChart(usageDetails: any) {
    this.logService.info(`
      usageDetails.labels ${usageDetails.labels},
      userDetails.dataSets ${usageDetails.dataSets},`, 
      [{ method: 'createChart' }]);
    

    this.chart = new Chart("MyChart", {
      type: 'line', //this denotes tha type of chart

      data: {// values on X-Axis
        labels: usageDetails.labels,
        datasets: usageDetails.dataSets
      },
      options: {
        aspectRatio: 2.4
      }

    });
  }

  ngOnInit() {
    this.getApplicationsUsageDetails();

  }

  getApplicationsUsageDetails() {

    this.http.get(this.config.baseUrl + '/applicationUsage/findAllApplicationUsageDetails')
      .subscribe(
        response => {
          this.usageDetails = JSON.parse(JSON.stringify(response));
          this.logService.info('usage details: ' + JSON.stringify(response), [{ method: 'getApplicationsUsageDetails' }]);
          this.createChart(JSON.parse(JSON.stringify(response)));
        },
      );
  }

  getApplicationsUsageDetailsByDate(frmDate,toDate) {
    let requestData: any = {};
    requestData = {
      
      'fromDate': frmDate,
      'toDate': toDate
      
    };

    this.http.post(this.config.baseUrl + '/applicationUsage/findAllApplicationUsageDetailsByDate',
     JSON.stringify(requestData), httpOptions)
      .subscribe(
        response => {
          this.usageDetails = JSON.parse(JSON.stringify(response));
          this.logService.info('usage details: ' + JSON.stringify(response), [{ method: 'getApplicationsUsageDetailsByDate' }]);
          let chartStatus = Chart.getChart("MyChart"); // <canvas> id
          if (chartStatus != undefined) {
            chartStatus.destroy();
          }
          this.createChart(JSON.parse(JSON.stringify(response)));
        },
      );
  }

  selectFromDate(fromDate) {
    this.frmDatePick = fromDate;
    this.getApplicationsUsageDetailsByDate(fromDate, this.toDatePick);
    this.logService.info('select from date ' + fromDate, [{ method: 'selectFromDate' }]);
  }

  selectToDate(toDate) {
    this.toDatePick = toDate;
    this.logService.info('select to date ' + toDate, [{ method: 'selectToDate' }]);
    this.getApplicationsUsageDetailsByDate(this.frmDatePick, toDate);
  }

}