import { LogEntry } from '../services';
import { Observable, of, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';  // RxJS v6
import { SecurityService } from '../services/security.service';
import { BASE_URL } from '../constants';


export abstract class LogPublisher {
  location: string = '';
  securityService!: SecurityService;
  abstract log(record: LogEntry): Observable<boolean>;
  abstract clear(): Observable<boolean>;
}

export class LogConsole extends LogPublisher {
  constructor(public override securityService: SecurityService) {
    super();
  }

  log(entry: LogEntry): Observable<boolean> {
    console.log(entry.buildLogString(this.securityService));
    return of(true);
  }

  clear(): Observable<boolean> {
    console.clear();
    return of(true);
  }
}

export class LogLocalStorage extends LogPublisher {
  constructor() {
    // Must call `super()`from derived classes
    super();

    // Set location
    this.location = 'logging';
  }

  // Append log entry to local storage
  log(entry: LogEntry): Observable<boolean> {
    let ret: boolean = false;
    let values: LogEntry[];

    try {
      // Get previous values from local storage
      values = JSON.parse(localStorage.getItem(this.location) ?? '[]');

      // Add new log entry to array
      values.push(entry);

      // Store array into local storage
      localStorage.setItem(this.location, JSON.stringify(values));

      // Set return value
      ret = true;
    } catch (ex) {
      // Display error in console
      console.log(ex);
    }

    return of(ret);
  }

  // Clear all log entries from local storage
  clear(): Observable<boolean> {
    localStorage.removeItem(this.location);
    return of(true);
  }
}

export class LogWebApi extends LogPublisher {
    constructor(private httpClient: HttpClient) {
        super();
        
        // Set location
        // TODO: Change URL to real Web API URL, when it's ready
        this.location = `${BASE_URL}/${BASE_URL}/schedule/api/log`;
    }
    
    // Add log entry to back end data store
    log(entry: LogEntry): Observable<boolean> {
        const httpOptions = {
            headers: new HttpHeaders({
              'Content-Type': 'application/json',
              'Access-Control-Allow-Origin': '*',
              Accept: '*/*',
            }),
          };

        const requestJson = {
          ...entry,
          message: entry.buildLogString(this.securityService)
        }
        
        return this.httpClient.post(this.location, requestJson, httpOptions)
            .pipe(map((response: any) => response ? response.json() : {}))
            .pipe(catchError(this.handleErrors));
    }
    
    // Clear all log entries from local storage
    clear(): Observable<boolean> {
        // TODO: Call Web API to clear all values
        return of(true);
    }
    
    private handleErrors(error: any): Observable<any> {
        let errors: string[] = [];
        let msg: string = "";
        
        if (error.status) {
            msg = "[ERROR_STATUS] = " + error.status + '\n';
        }
        if (error.message) {
            msg += "[ERROR_MESSAGE] = " + error.message + '\n';
        }
        errors.push(msg);
        
        return throwError(() => errors);
    }
}

export interface LogPublisherConfig {
    loggerName: string;
    loggerLocation: string;
    isActive: boolean;
}
