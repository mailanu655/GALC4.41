import { Component, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-show',
  templateUrl: './show.component.html',
  styleUrls: ['./show.component.css']
})
export class ShowComponent implements OnInit {
  
  name = 'Set iframe source';
  url = 'https://angular.io/guide/two-way-binding';
  urlSafe: SafeResourceUrl;

  constructor(public sanitizer: DomSanitizer, private route: ActivatedRoute, private router: Router) {
    this.route.paramMap.subscribe(params => {
      this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    });
  }

  ngOnInit(): void {
    this.url = this.route.snapshot.queryParamMap.get('url');
    this.urlSafe = this.sanitizer.bypassSecurityTrustResourceUrl(this.url);  
  }
  
}

