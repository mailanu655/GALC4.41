import { Component, OnInit } from '@angular/core';
import { DataShareService } from 'src/app/services/data-share.service';

@Component({
    selector: 'app-footer',
    templateUrl: './footer.component.html',
    styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {

    date: number;
    factoryNewsMode: string;

    constructor(private dataShareService: DataShareService,) {
        this.dataShareService.getCurrentMessage().subscribe(message => {
            this.factoryNewsMode = message.mode;
        });
    }

    ngOnInit() {
        this.date = new Date().getFullYear();
    }
}
