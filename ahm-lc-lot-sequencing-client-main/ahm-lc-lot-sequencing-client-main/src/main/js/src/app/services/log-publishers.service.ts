import { Injectable } from '@angular/core';
import {
  LogConsole,
  LogPublisher,
  LogLocalStorage,
  LogWebApi,
  LogPublisherConfig,
} from '../shared/log-publishers';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { SecurityService } from './security.service';

const PUBLISHERS_FILE = '/assets/log-publishers.json';

@Injectable()
export class LogPublishersService {
  constructor(private httpClient: HttpClient, public securityService: SecurityService) {
    // Build publishers arrays
    this.buildPublishers();
  }

  // Public properties
  publishers: LogPublisher[] = [];

  private handleErrors(error: any): Observable<any> {
    let errors: string[] = [];
    let msg: string = '';

    msg = 'Status: ' + error.status;
    msg += ' - Status Text: ' + error.statusText;
    if (error.message) {
      msg += ' - Exception Message: ' + error.message;
    }
    errors.push(msg);
    console.error('An error occurred', errors);
    return throwError(() => errors);
  }

  // Build publishers array
  buildPublishers(): void {
    let logPub: LogPublisher;

    this.getLoggers().subscribe((response) => {
      for (let pub of response.filter((p) => p.isActive)) {
        switch (pub.loggerName.toLowerCase()) {
          case 'console':
            logPub = new LogConsole(this.securityService);
            break;
          case 'localstorage':
            logPub = new LogLocalStorage();
            break;
          case 'webapi':
            logPub = new LogWebApi(this.httpClient);
            break;
        }

        // Set location of logging
        logPub.location = pub.loggerLocation;

        // Add publisher to array
        this.publishers.push(logPub);
      }
    });
  }

  getLoggers(): Observable<LogPublisherConfig[]> {
    return this.httpClient
      .get(PUBLISHERS_FILE)
      .pipe(map((response: any) => {
        return response;
      }))
      .pipe(catchError(this.handleErrors));
  }
}
