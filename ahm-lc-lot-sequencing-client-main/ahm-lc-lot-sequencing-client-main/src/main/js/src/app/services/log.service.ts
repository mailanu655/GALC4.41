import { Injectable } from '@angular/core';
import { v4 as uuidv4 } from 'uuid';

import { LogLevel } from '../constants';
import { LogPublishersService } from './log-publishers.service';
import { LogPublisher } from '../shared/log-publishers';
import { formatDate } from '../lib/ui-utils';
import { SecurityService } from './security.service';

@Injectable({
  providedIn: 'root',
})
export class LogService {
  level: LogLevel = LogLevel.CHECK;
  logWithDate: boolean = true;
  publishers: LogPublisher[];

  constructor(private publishersService: LogPublishersService, private securityService: SecurityService) {
    this.publishers = this.publishersService.publishers;
  }

  private shouldLog(level: LogLevel): boolean {
    let ret: boolean = false;
    if (
      (level <= this.level) ||
      this.level === LogLevel.CHECK
    ) {
      ret = true;
    }
    return ret;
  }

  private writeToLog(msg: string, level: LogLevel, params: any[]) {
    if (this.shouldLog(level)) {
      let entry: LogEntry = new LogEntry();
      entry.message = msg;
      entry.level = level;
      entry.extraInfo = params;
      entry.logWithDate = this.logWithDate;

      for (let logger of this.publishers) {
        logger.log(entry).subscribe((response: any) => true);
      }
    }
  }

  check(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.CHECK, optionalParams);
  }

  gui(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.GUI, optionalParams);
  }

  debug(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.DEBUG, optionalParams);
  }

  info(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.INFO, optionalParams);
  }

  warning(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.WARNING, optionalParams);
  }

  error(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.ERROR, optionalParams);
  }

  fatal(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.FATAL, optionalParams);
  }

  log(msg: string, ...optionalParams: any[]) {
    this.writeToLog(msg, LogLevel.CHECK, optionalParams);
  }
}

export class LogEntry {
  // Public Properties
  entryDate: Date = new Date();
  message: string = '';
  level: LogLevel = LogLevel.CHECK;
  extraInfo: any[] = [];
  logWithDate: boolean = true;

  constructor() {
    
  }

  getUserId(securityService: SecurityService) {
    let userDetails: any = {};
    if (localStorage.getItem('userDetails')) {
      userDetails = JSON.parse(
        localStorage.getItem('userDetails') || '{}'
      );
    }
    return userDetails.username || securityService.getUser();
  }

  buildLogString(securityService: SecurityService): string {
    let ret: string = '\n ';

    const uuid = uuidv4();

    ret += '[CORRELATION_ID] = ' + uuid + ' \n ';
    ret += '[TIMESTAMP] = ' + formatDate((new Date()).toString()) + ' \n ';
    ret += '[SERVICE_NAME] = ' + 'Product Service \n ';
    ret += '[IP_ADDRESS] = ' + '10.61.174.237' + ' \n '; // Placeholder
    ret += '[APPLICATION_ID] = ' + 'ProductClient' + ' \n ';
    ret += '[MESSAGE_TYPE] = ' + LogLevel[this.level] + ' \n ';
    ret += '[MESSAGE] = ' + this.message + ' \n ';
    ret += '[USER_ID] = ' + this.getUserId(securityService) + ' \n ';

    if (this.extraInfo.length) {
      this.extraInfo.forEach((info) => {
        for (const [key, value] of Object.entries(info[0])) {
          ret += `[${key.toUpperCase()}] = ${value} \n `;
        }
      });
    }

    return ret;
  }
}
