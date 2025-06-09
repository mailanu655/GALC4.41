import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import html2canvas from 'html2canvas';
import { MessageBundle, messages } from 'src/app/lib/messages';
import { AppService, UserEvent } from 'src/app/services/app.service';
import { ConfigService } from 'src/app/services/config.service';
import { AboutComponent } from '../about/about.component';

@Component({
    selector: 'header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

    messages: MessageBundle = messages;

    constructor(public dialog: MatDialog, public config: ConfigService, public appService: AppService) { }

    ngOnInit() {
    }

    search(text: string, watch: boolean) {
        text = text.trim();
        this.appService.searchValue = text;
        this.appService.userEvent.emit(UserEvent.SEARCH);
        if (watch === true && this.appService.searchValue.length > 0) {
            this.appService.watchOn = true;
        } else {
            this.appService.watchOn = false;
        }
    }

    menuSelected(item: string) {
        this.appService.menuItem = item;
    }

    openHelp() {
        window.open(this.config.helpUrl, "_blank");
    }

    async openFeedback(feedbackUrl: string | undefined, appName: string | undefined) {
        if (!feedbackUrl || !appName) {
            return;
        }
        const canvas: HTMLCanvasElement = await html2canvas(document.body);
        const imageData: string = canvas.toDataURL();
        const feedbackWindow = window.open(feedbackUrl + '?' + 'appName=' + appName, 'feedback-' + appName);
        setTimeout(() => {
            feedbackWindow!.postMessage(imageData, feedbackUrl);
        }, 2000);
    }

    openInfo() {
        window.open(this.config.infoUrl, "_blank");
    }

    openAboutDialog() {
        this.dialog.open(AboutComponent)
    }

    get appName(): string {
        if (this.portrait) {
            return this.config.appShortName;
        } else {
            return this.config.appName;
        }
    }

    get portrait() {
        if (window.innerHeight > window.innerWidth) {
            return true;
        } else {
            return false;
        }
    }
}
