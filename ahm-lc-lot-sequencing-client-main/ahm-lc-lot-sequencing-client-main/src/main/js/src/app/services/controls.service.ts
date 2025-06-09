import { Injectable } from '@angular/core';
import { Subject  } from 'rxjs';

@Injectable()
export class ControlsService {

  private stateSource = new Subject<Boolean>();
  state$ = this.stateSource.asObservable();

  constructor() { }

  openPanel(state: boolean) {
    this.stateSource.next(state);
  }

}