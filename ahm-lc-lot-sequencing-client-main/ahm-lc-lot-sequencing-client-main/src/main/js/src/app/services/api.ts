import { Injectable } from '@angular/core';
import { GalcService } from './galc.service';
import { CheckService } from './check.service';
import { LocationService } from './location.service';

@Injectable({
  providedIn: 'root',
})
export class Api {
  constructor(
    public galc: GalcService,
    public check: CheckService,
    public location: LocationService
  ) {}
}
