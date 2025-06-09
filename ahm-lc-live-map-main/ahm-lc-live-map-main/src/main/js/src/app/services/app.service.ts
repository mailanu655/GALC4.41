import { EventEmitter, Injectable, Output } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { CookieService } from 'ngx-cookie-service';
import { DepartmentCode } from '../components/line/line-model';
import { ConfigService } from './config.service';


export enum UserEvent {
    SEARCH = 'search', QICS = 'qics', PART = 'part', HOLD = 'hold', STRAGGLER = 'stragglers', LOCATION = 'location', LOCATION_CLICKED = 'location_clicked'
}

const SETTINGS: string = 'settings';

export class Settings {
    displayExtColor: boolean;

    displayDefects: boolean;
    displayWeldDefects: boolean;
    displayPaintDefects: boolean;
    displayAfDefects: boolean;

    displayPartIssues: boolean;
    displayHolds: boolean;
    displayStragglers: boolean;

    displayProcessAreas: boolean;
    displayTeamLeaderAreas: boolean;

    displaySectionLabels: boolean;
    pushLocation: boolean;
    pullSection: boolean;
    pullCrossBuffer: boolean;
    pullLocation: boolean;

    tracking: boolean;
}


@Injectable({
    providedIn: 'root'
})
export class AppService {

    private static DISPLAY_EXT_COLOR_DEFAULT: boolean = true;

    private static DISPLAY_DEFECTS_DEFAULT: boolean = true;
    private static DISPLAY_WELD_DEFECTS_DEFAULT: boolean = false;
    private static DISPLAY_PAINT_DEFECTS_DEFAULT: boolean = false;
    private static DISPLAY_AF_DEFECTS_DEFAULT: boolean = false;

    private static DISPLAY_PART_ISSUES_DEFAULT: boolean = true;
    private static DISPLAY_HOLDS_DEFAULT: boolean = true;
    private static DISPLAY_STRAGGLERS_DEFAULT: boolean = true;

    private static DISPLAY_PROCESS_AREAS_DEFAULT: boolean = false;
    private static DISPLAY_TEAMLEADER_AREAS_DEFAULT: boolean = false;

    private static DISPLAY_SECTION_LABELS_DEFAULT: boolean = false;
    private static PUSH_LOCATION_DEFAULT: boolean = true;
    private static PULL_SECTION_DEFAULT: boolean = true;
    private static PULL_CROSS_BUFFER_DEFAULT: boolean = false;
    private static PULL_LOCATION_DEFAULT: boolean = false;

    private static TRACKING_DEFAULT: boolean = false;

    @Output()
    searchEvent: EventEmitter<string> = new EventEmitter<string>();

    @Output()
    userEvent: EventEmitter<any> = new EventEmitter<any>();

    countDown: number;
    searchValue: string | null;
    watchOn: boolean = false;
    menuItem: string | null;

    settings: Settings = new Settings();

    displaySectionLabels: boolean;
    pushLocation: boolean;
    pullSection: boolean;
    pullCrossBuffer: boolean;
    pullLocation: boolean;
    tracking: boolean;

    headerItems: string[];

    constructor(private config: ConfigService, private cookieService: CookieService) {
        this.resetValues();
        this.loadSettings();
    }

    resetHeader() {
        this.searchValue = null;
        this.watchOn = false;
    }

    resetSettings() {
        let qicsReset: boolean =
            this.displayDefects != AppService.DISPLAY_DEFECTS_DEFAULT
            || this.displayWeldDefects != AppService.DISPLAY_WELD_DEFECTS_DEFAULT
            || this.displayPaintDefects != AppService.DISPLAY_PAINT_DEFECTS_DEFAULT
            || this.displayAfDefects != AppService.DISPLAY_AF_DEFECTS_DEFAULT;

        let partsReset = this.displayPartIssues != AppService.DISPLAY_PART_ISSUES_DEFAULT;
        let holdsReset = this.displayHolds != AppService.DISPLAY_HOLDS_DEFAULT;
        let stragglerReset = this.displayStragglers != AppService.DISPLAY_STRAGGLERS_DEFAULT;
        let locationReset = this.pushLocation != AppService.PUSH_LOCATION_DEFAULT
            || this.pullSection != AppService.PULL_SECTION_DEFAULT
            || this.pullCrossBuffer != AppService.PULL_CROSS_BUFFER_DEFAULT
            || this.pullLocation != AppService.PULL_LOCATION_DEFAULT;

        this.resetValues();

        if (qicsReset) {
            this.userEvent.emit(UserEvent.QICS);
        }
        if (partsReset) {
            this.userEvent.emit(UserEvent.PART);
        }
        if (holdsReset) {
            this.userEvent.emit(UserEvent.HOLD);
        }
        if (stragglerReset) {
            this.userEvent.emit(UserEvent.STRAGGLER);
        }
        if (locationReset) {
            this.userEvent.emit(UserEvent.LOCATION);
        }
        this.saveSettings();
    }

    resetValues() {
        this.displayExtColor = AppService.DISPLAY_EXT_COLOR_DEFAULT;
        this.displayDefects = AppService.DISPLAY_DEFECTS_DEFAULT;
        this.displayWeldDefects = AppService.DISPLAY_WELD_DEFECTS_DEFAULT;
        this.displayPaintDefects = AppService.DISPLAY_PAINT_DEFECTS_DEFAULT;
        this.displayAfDefects = AppService.DISPLAY_AF_DEFECTS_DEFAULT;
        this.displayPartIssues = AppService.DISPLAY_PART_ISSUES_DEFAULT;
        this.displayHolds = AppService.DISPLAY_HOLDS_DEFAULT;
        this.displayStragglers = AppService.DISPLAY_STRAGGLERS_DEFAULT;
        this.displayProcessAreas = AppService.DISPLAY_PROCESS_AREAS_DEFAULT;
        this.displayTeamLeaderAreas = AppService.DISPLAY_TEAMLEADER_AREAS_DEFAULT;
        this.displaySectionLabels = AppService.DISPLAY_SECTION_LABELS_DEFAULT;
        this.pushLocation = AppService.PUSH_LOCATION_DEFAULT;
        this.pullSection = AppService.PULL_SECTION_DEFAULT;
        this.pullCrossBuffer = AppService.PULL_CROSS_BUFFER_DEFAULT;
        this.pullLocation = AppService.PULL_LOCATION_DEFAULT;
        this.tracking = AppService.TRACKING_DEFAULT;
    }

    loadSettings() {
        let value: string = this.cookieService.get(SETTINGS);
        if (value) {
            let profile = JSON.parse(value);
            Object.assign(this.settings, profile);
        }
    }

    saveSettings() {
        let options = { expires: 365 * 20 };
        let value = JSON.stringify(this.settings);
        this.cookieService.set(SETTINGS, value, options);
    }

    // === layers controll === //
    isDisplayDefects() {
        return this.displayDefects || this.displayWeldDefects || this.displayPaintDefects || this.displayAfDefects;
    }

    extColorChange(event: MatSlideToggleChange) {
        this.displayExtColor = event.checked;
        this.saveSettings();
    }

    defectChange(event: MatSlideToggleChange) {
        this.displayDefects = event.checked;
        if (this.displayDefects) {
            this.displayWeldDefects = false;
            this.displayPaintDefects = false;
            this.displayAfDefects = false;
        }
        this.userEvent.emit(UserEvent.QICS);
        this.saveSettings();
    }

    weldDefectChange(event: MatSlideToggleChange) {
        this.displayWeldDefects = event.checked;
        if (this.displayWeldDefects) {
            this.displayDefects = false;
        }
        this.userEvent.emit(UserEvent.QICS);
        this.saveSettings();
    }

    paintDefectChange(event: MatSlideToggleChange) {
        this.displayPaintDefects = event.checked;
        if (this.displayPaintDefects) {
            this.displayDefects = false;
        }
        this.userEvent.emit(UserEvent.QICS);
        this.saveSettings();
    }

    afDefectChange(event: MatSlideToggleChange) {
        this.displayAfDefects = event.checked;
        if (this.displayAfDefects) {
            this.displayDefects = false;
        }
        this.userEvent.emit(UserEvent.QICS);
        this.saveSettings();
    }

    partIssueChange(event: MatSlideToggleChange) {
        this.displayPartIssues = event.checked;
        this.userEvent.emit(UserEvent.PART);
    }

    holdChange(event: MatSlideToggleChange) {
        this.displayHolds = event.checked;
        this.userEvent.emit(UserEvent.HOLD);
        this.saveSettings();
    }

    stragglerChange(event: MatSlideToggleChange) {
        this.displayStragglers = event.checked;
        this.userEvent.emit(UserEvent.STRAGGLER);
        this.saveSettings();
    }

    processAreaChange(event: MatSlideToggleChange) {
        this.displayProcessAreas = event.checked;
        this.saveSettings();
    }

    teamLeaderAreaChange(event: MatSlideToggleChange) {
        this.displayTeamLeaderAreas = event.checked;
        this.saveSettings();
    }

    trackingChanged(event: MatSlideToggleChange) {
        this.tracking = event.checked;
    }

    sectionLabelChange(event: MatSlideToggleChange) {
        this.displaySectionLabels = event.checked;
    }

    adjustLocation(event: MatSlideToggleChange) {
        if (event.checked) {
            this.pushLocation = AppService.PUSH_LOCATION_DEFAULT;
            this.pullSection = AppService.PULL_SECTION_DEFAULT;
            this.pullCrossBuffer = AppService.PULL_CROSS_BUFFER_DEFAULT;
            this.pullLocation = AppService.PULL_LOCATION_DEFAULT;
        } else {
            this.pushLocation = false;
            this.pullSection = false;
            this.pullCrossBuffer = false;
            this.pullLocation = false;
        }
        this.userEvent.emit(UserEvent.LOCATION);
    }

    pushLocationChanged(event: MatSlideToggleChange) {
        this.pushLocation = event.checked;
        if (this.pushLocation === false) {
            this.pullSection = false;
            this.pullCrossBuffer = false;
            this.pullLocation = false;
        }
        this.userEvent.emit(UserEvent.LOCATION);
    }

    pullSectionChanged(event: MatSlideToggleChange) {
        this.pullSection = event.checked;
        if (this.pullSection === true) {
            this.pushLocation = true;
        } else {
            this.pullCrossBuffer = false;
            this.pullLocation = false;
        }
        this.userEvent.emit(UserEvent.LOCATION);
    }

    pullCrossBufferChanged(event: MatSlideToggleChange) {
        this.pullCrossBuffer = event.checked;
        if (this.pullCrossBuffer === true) {
            this.pushLocation = true;
            this.pullSection = true;
        } else {
            this.pullLocation = false;
        }
        this.userEvent.emit(UserEvent.LOCATION);
    }

    pullLocationChanged(event: MatSlideToggleChange) {
        this.pullLocation = event.checked;
        if (this.pullLocation === true) {
            this.pushLocation = true;
            this.pullSection = true;
            this.pullCrossBuffer = true;
        }
        this.userEvent.emit(UserEvent.LOCATION);
    }

    // === product serach === //
    filter(items: any[], searchValue: string): any[] {
        let found: any[] = [];
        for (let item of items) {
            let str = JSON.stringify(item);
            let ix = str.search(new RegExp(searchValue, "i"));
            if (ix > -1) {
                found.push(item);
            }
        }
        return found;
    }

    // === control === //
    isVisible(item: string): boolean {
        if (item && this.headerItems && this.headerItems.length > 0) {
            if (this.headerItems.indexOf(item) > -1) {
                return true;
            }
        }
        return false;
    }

    // === get === //
    get admin(): boolean {
        if (this.config['admin'] === true) {
            return true;
        } else {
            return false;
        }
    }

    get qicsDepartments(): string[] {
        let departments: string[] = [];
        if (this.displayWeldDefects) {
            departments.push(DepartmentCode.WE);
        }
        if (this.displayPaintDefects) {
            departments.push(DepartmentCode.PA);
        }
        if (this.displayAfDefects) {
            departments.push(DepartmentCode.AF);
        }
        return departments;
    }

    // === persistable settings === //
    get displayExtColor(): boolean {
        return this.settings.displayExtColor;
    };

    set displayExtColor(displayExtColor: boolean) {
        this.settings.displayExtColor = displayExtColor;
    }

    get displayDefects(): boolean {
        return this.settings.displayDefects;
    };

    set displayDefects(displayDefects: boolean) {
        this.settings.displayDefects = displayDefects;
    }

    get displayWeldDefects(): boolean {
        return this.settings.displayWeldDefects;
    }

    set displayWeldDefects(displayWeldDefects: boolean) {
        this.settings.displayWeldDefects = displayWeldDefects;
    }

    get displayPaintDefects(): boolean {
        return this.settings.displayPaintDefects;
    }

    set displayPaintDefects(displayPaintDefects: boolean) {
        this.settings.displayPaintDefects = displayPaintDefects;
    }

    get displayAfDefects(): boolean {
        return this.settings.displayAfDefects;
    }

    set displayAfDefects(displayAfDefects: boolean) {
        this.settings.displayAfDefects = displayAfDefects;
    }

    get displayPartIssues(): boolean {
        return this.settings.displayPartIssues;
    }

    set displayPartIssues(displayPartIssues: boolean) {
        this.settings.displayPartIssues = displayPartIssues;
    }

    get displayHolds(): boolean {
        return this.settings.displayHolds;
    }

    set displayHolds(displayHolds: boolean) {
        this.settings.displayHolds = displayHolds;
    }

    get displayStragglers(): boolean {
        return this.settings.displayStragglers;
    }

    set displayStragglers(displayStragglers: boolean) {
        this.settings.displayStragglers = displayStragglers;
    }

    get displayProcessAreas(): boolean {
        return this.settings.displayProcessAreas;
    }

    set displayProcessAreas(displayProcessAreas: boolean) {
        this.settings.displayProcessAreas = displayProcessAreas;
    }

    get displayTeamLeaderAreas(): boolean {
        return this.settings.displayTeamLeaderAreas;
    }

    set displayTeamLeaderAreas(displayTeamLeaderAreas: boolean) {
        this.settings.displayTeamLeaderAreas = displayTeamLeaderAreas;
    }
}