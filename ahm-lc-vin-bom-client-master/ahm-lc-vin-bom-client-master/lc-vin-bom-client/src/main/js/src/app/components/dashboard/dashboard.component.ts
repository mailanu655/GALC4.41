import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';


@Component({
    selector: 'dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

    constructor(private titleService: Title) { 
        this.titleService.setTitle("SUMS-VIN Dashboard");
    }

    ngOnInit(): void {
        console.log("hello");
    }

    get logoMessage(): string {
        return '';
    }
}
