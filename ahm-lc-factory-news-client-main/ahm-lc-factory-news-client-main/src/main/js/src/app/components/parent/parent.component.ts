import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Parameter } from '../enums/Parameter';
import { ConfigService } from 'src/app/services/config.service';
import { DataLoadService } from 'src/app/services/data-load.service';

@Component({
  selector: 'app-parent',
  templateUrl: './parent.component.html',
  styleUrls: ['./parent.component.css']
})
export class ParentComponent implements OnInit {

  private subscription: Subscription;

  constructor(
    public config: ConfigService,
    public route: ActivatedRoute,
    public router: Router,
    public dataLoadService: DataLoadService) { 
      this.setParameters();
    }

  ngOnInit(): void {
  }

  setParameters() {
    this.subscription = this.route.params.subscribe(val => {
      if(val.site) {
        let site = this.route.snapshot.paramMap.get(Parameter.SITE);
        this.config.siteId = site!;
        this.router.routeReuseStrategy.shouldReuseRoute = () => false;
      }
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  getDataLoadService(): DataLoadService {
    return this.dataLoadService;
  }

}
