import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataShareService {

  private messageSource = new BehaviorSubject<any>({
    mode: "now",
    data: ""
  });
  currentMessage = this.messageSource.asObservable();

  constructor() { }

  getCurrentMessage() {
    return this.currentMessage;
  }

  changeMessage(message: string) {
    this.messageSource.next(message)
  }
}
