import { Component, Input, OnInit } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { MessageBundle, messages } from 'src/app/lib/messages';
import { AppService } from 'src/app/services/app.service';

@Component({
    selector: 'settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

    @Input()
    color: ThemePalette = 'primary';

    messages: MessageBundle = messages;

    constructor(public appService: AppService) { }

    ngOnInit() {
    }

    close() {
        this.appService.menuItem = null;
    }
}
