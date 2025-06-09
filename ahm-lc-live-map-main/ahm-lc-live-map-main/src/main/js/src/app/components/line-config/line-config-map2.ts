import { LineConfig } from "./line-config";
import { Side } from "../line/line-model";

export class Map2Config extends LineConfig {

    c2cy: number;

    protected set() {
        this.id = 'map1';
        this.c2c = 30;
        this.c2cy = 24;
    }

    protected init() {
        super.init();
        this.endLength = 0.5 * this.c2c;
        this.sectionWidth = 2 * this.c2cy;
    }

    protected getAxis(): any[] {
        let config: any[] = [
            { x: 12 + this.c2c, y: 12, step: 2 * this.c2c, values: this.getXAxisValuesTop() },
            { x: 12 + 4 * this.c2c, y: 541, step: 2 * this.c2c, values: this.getXAxisValuesBottom() },
            { x: 10, y: 6 * this.c2cy, step: 4.75 * this.c2cy, values: this.getYAxisValues(), horizontal: false },
            { x: 10, y: 15.5 * this.c2cy, step: 4.75 * this.c2cy, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 6 * this.c2cy, step: 4.75 * this.c2cy, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 15.5 * this.c2cy, step: 4.75 * this.c2cy, values: this.getYAxisValues(), horizontal: false }
        ];
        return config;
    }

    protected getXAxisValuesTop(): string[] {
        let config: string[] = [];
        for (let i = 2; i <= 54; i = i + 2) {
            config.push(i.toString());
        }
        return config;
    }

    protected getXAxisValuesBottom(): string[] {
        let config: string[] = [];
        for (let i = 50; i <= 98; i = i + 2) {
            config.push(i.toString());
        }
        return config;
    }

    protected getYAxisValues(): string[] {
        let config: string[] = ['A', 'B'];
        return config;
    }

    protected getSections(): any[] {
        let config: any[] = [
            { id: "AFON", entryx: 30 * this.c2c, entryy: 1.75 * this.c2cy, locationCount: 2, horizontal: true, ascending: true, entryLength: this.c2c, length: 3 * this.c2c, width: 0.75 * this.sectionWidth },
            { id: "AB1", locationCount: 1, entryLength: this.c2c, length: 2 * this.c2c, width: 0.75 * this.sectionWidth },
            { id: "AB2", locationCount: 1, width: 0.75 * this.sectionWidth, type: 'non-working' },
            { id: "AB3", locationCount: 3, length: 3 * this.c2c, width: 0.75 * this.sectionWidth },
            { id: "AB4", entryx: 40 * this.c2c - 6, entryy: 1 * this.c2cy, locationCount: 2, horizontal: false, ascending: true, length: 3.25 * this.c2cy, entryLength: 1.125 * this.c2cy, c2c: this.c2cy },

            { id: "B1", entryx: 39 * this.c2c, entryy: 3.5 * this.c2cy, locationCount: 2, horizontal: true, ascending: false, length: 4 * this.c2c, width: 0.75 * this.sectionWidth, entryLength: this.c2c, c2c: 2 * this.c2c, type: 'buffer' },
            { id: "B2", locationCount: 7, width: 0.75 * this.sectionWidth, type: 'buffer' },
            { id: "B3", locationCount: 1, width: 0.75 * this.sectionWidth, type: 'non-working' },
            { id: "B4", locationCount: 4, width: 0.75 * this.sectionWidth },

            { id: "BC1", locationCount: 6, horizontal: true, ascending: false, length: 6 * this.c2c + 6, width: 0.75 * this.sectionWidth, type: 'buffer' },
            { id: "BC2", entryx: 16 * this.c2c, entryy: 2.75 * this.c2cy, locationCount: 2, horizontal: false, ascending: true, length: 6 + 2 * this.c2cy, entryLength: 0.75 * this.c2cy, c2c: this.c2cy, type: 'buffer' },
            { id: "BC3L", entryx: 15.5 * this.c2c + 3, entryy: 7 * this.c2cy, locationCount: 1, horizontal: false, ascending: true, width: this.c2cy, c2c: this.c2cy, entryLength: 0.5 * this.c2cy, exitLength: 0.5 * this.c2cy, connect: false, type: 'buffer' },
            { id: "BC3R", entryx: 16.5 * this.c2c - 3, entryy: 7 * this.c2cy, locations: [{ id: 32.1, code: 0 }], horizontal: false, ascending: true, width: this.c2cy, c2c: this.c2cy, entryLength: 0.5 * this.c2cy, exitLength: 0.5 * this.c2cy, connect: false, type: 'buffer' },
            { id: "BC4", entryx: 16 * this.c2c, entryy: 8 * this.c2cy, locationCount: 4, horizontal: false, ascending: true, length: 4.875 * this.c2cy, c2c: this.c2cy, entryLength: 0.5 * this.c2cy, type: 'buffer' },

            { id: "C1", entryx: 15 * this.c2c + 6, entryy: 12.25 * this.c2cy, locationCount: 11, horizontal: true, ascending: false, width: 0.625 * this.sectionWidth, entryLength: this.entryLength + 6, type: 'buffer' },

            { id: "CD1", entryx: 3 * this.c2c + 6, entryy: 13 * this.c2cy - 3, locationCount: 2, horizontal: false, ascending: false, length: 3.5 * this.c2cy, entryLength: 2 * this.c2cy - 3, c2c: this.c2cy, type: 'buffer' },
            { id: "CD2", locationCount: 3, length: 4.5 * this.c2cy - 3, c2c: this.c2cy },

            { id: "D1", entryx: 4 * this.c2c, entryy: 6 * this.c2cy, locationCount: 1, horizontal: true, ascending: true, length: 2 * this.c2c, entryLength: this.c2c, type: 'non-working' },
            { id: "D2", locationCount: 3 },
            { id: "D3", locationCount: 1, type: 'non-working' },
            { id: "D4", locationCount: 19 },
            { id: "D5", locationCount: 3, type: 'non-working' },
            { id: "D6", locationCount: 19 },

            { id: "DE1", entryx: 53 * this.c2c - 12, entryy: 9.25 * this.c2cy, locationCount: 0, horizontal: false, ascending: true, width: this.c2c, landMark: false },
            { id: "DE2", entryx: 40.5 * this.c2c, entryy: 13.75 * this.c2cy, locationCount: 0, horizontal: true, ascending: false, length: 24 * this.c2c, width: this.c2c, landMark: false },
            { id: "DE3", entryx: 3.5 * this.c2c + 6, entryy: 13.875 * this.c2cy, locationCount: 0, horizontal: false, ascending: true, width: this.c2c, landMark: false },

            { id: "E1", entryx: 6 * this.c2c, entryy: 15.5 * this.c2cy, locationCount: 35, horizontal: true, ascending: true },
            { id: "E2", locationCount: 2, type: 'non-working' },
            { id: "E3", locationCount: 1, type: 'semi-auto' },
            { id: "E4", locationCount: 1, type: 'non-working' },
            { id: "E5", locationCount: 4 },
            { id: "E6", locationCount: 1, length: 2 * this.c2c, type: 'non-working' },

            { id: "EF1", entryx: 52 * this.c2c - 6, entryy: 14.5 * this.c2cy, locationCount: 2, horizontal: false, ascending: true, length: 3.25 * this.c2cy, entryLength: 1.6 * this.c2cy, c2c: this.c2cy },
            { id: "EF2", entryx: 51 * this.c2c, entryy: 17.25 * this.c2cy, locationCount: 1, horizontal: true, ascending: false, width: 1.25 * this.c2cy, entryLength: 1.5 * this.c2c },
            { id: "EF3", locations: [{ id: 145.0146, code: '146' }], width: 1.25 * this.c2cy },
            { id: "EF4", locations: [{ id: 145.0148, code: '148' }], width: 1.25 * this.c2cy, type: 'non-working' },
            { id: "EF5", locations: [{ id: 145.1, code: '1' }, { id: 145.2, code: '2' }], width: 1.25 * this.c2cy, type: 'buffer' },
            { id: "EF6", entryx: 44.25 * this.c2c, entryy: 16.625 * this.c2cy, locationCount: 0, horizontal: false, ascending: true, length: 2.5 * this.c2cy, width: 1.75 * this.c2cy, landMark: true, type: 'buffer' },
            { id: "EF7", entryx: 45 * this.c2c, entryy: 18.5 * this.c2cy, locations: [{ id: 145.3, code: '3' }, { id: 145.4, code: '4' }, { id: 145.5, code: '5' }, { id: 145.6, code: '6' }], horizontal: true, ascending: true, length: 6 * this.c2c, width: 1.25 * this.c2cy, entryLength: 1.5 * this.c2c, type: 'buffer' },
            { id: "EF8", entryx: 52 * this.c2c - 6, entryy: 18 * this.c2cy, locations: [{ id: 145.7, code: '7' }, { id: 145.8, code: '8' }], horizontal: false, ascending: true, length: 3.25 * this.c2cy, entryLength: 0.7 * this.c2cy, c2c: this.c2cy, type: 'buffer' },

            { id: "F1", entryx: 51 * this.c2c, entryy: 20.25 * this.c2cy, locationCount: 3, horizontal: true, ascending: false, entryLength: 1.5 * this.c2c, type: 'non-working' },
            { id: "F2", locationCount: 27 },
            { id: "F3", locationCount: 4, type: 'non-working' },
            { id: "F4", locationCount: 1, type: 'auto' },
            { id: "F5", locationCount: 1, type: 'non-working' },
            { id: "F6", locationCount: 8 },

            { id: "FG1", entryx: 3 * this.c2c + 6, entryy: 17.5 * this.c2cy, locationCount: 0, horizontal: false, ascending: false, width: this.c2c, landMark: false },
            { id: "FG2", entryx: 16.25 * this.c2c, entryy: 13.25 * this.c2cy, locationCount: 0, horizontal: true, ascending: true, length: 24 * this.c2c, width: this.c2c, landMark: false },
            { id: "FG3", entryx: 52.5 * this.c2c - 12, entryy: 13 * this.c2cy, locationCount: 0, horizontal: false, ascending: false, width: this.c2c, landMark: false },

            { id: "G1", entryx: 51 * this.c2c, entryy: 10.75 * this.c2cy, locationCount: 19, horizontal: true, ascending: false },
            { id: "G2", locationCount: 5, type: 'inspection' },
            { id: "G3", locationCount: 8, type: 'non-working' },
        ];
        return config;
    }

    protected getLandMarks(): any[] {
        return [];
    }

    protected getLabels(): any[] {
        let firstSection: any = this.sections[0];
        let config: any[] = [
            { text: "AF ON", x: firstSection.entryx - 3 * this.c2c, y: firstSection.entryy + 0.05 * this.c2cy },
            { text: "AF OFF", x: 20.5 * this.c2c, y: 12.25 * this.c2cy }
        ];
        return config;
    }

    protected getStations(): any[] {
        let stations: any[] = [
        ];
        return stations;
    }

    protected getProcessAreas(): any[] {
        let config: any[] = [
            { id: "Door Off", location: 8, side: Side.L, offsetx: 2 },
            { id: "FEM Off", location: 9, side: Side.L, offsetx: 2 },
            { id: "Fuel Tank", location: 81, side: Side.L, offsety: 15 },
            { id: "Sunroof", location: 87, side: Side.L, offsety: 15 },
            { id: "Headliner", location: 98, side: Side.L, offsetx: -15 },
            { id: "HVAC", location: 104, side: Side.L },
            { id: "FEM On", location: 106, side: Side.L },
            { id: "Carpet", location: 108, side: Side.L, offsetx: 17 },
            { id: "IP", location: 115, side: Side.L },
            { id: "Bumper", location: 132, side: Side.L },
            { id: "SMURF", location: 136, side: Side.L },
            { id: "Exhaust", location: 151, side: Side.L },
            { id: "Tires", location: 164, side: Side.L },
            { id: "Console", location: 166, side: Side.L },
            { id: "Battery", location: 174, side: Side.L },
            { id: "Glass", location: 180, side: Side.L },
            { id: "Seats", location: 183, side: Side.L },
            { id: "Pre-Vac/Multi Fill", location: 186, side: Side.L },
            { id: "Seats", location: 189, side: Side.L },
            { id: "Qtr Glass", location: 192, side: Side.L },
            { id: "Door On", location: 196, side: Side.L },
            { id: "Rad Fill", location: 200, side: Side.L },
            { id: "Immobi", location: 203, side: Side.L },
            { id: "Gas/HAC", location: 206, side: Side.L },
        ];
        return config;
    }
    protected getTeamLeaderAreas(): any[] {
        let config: any[] = [
            { id: "WT1", location: 54, side: Side.R },
            { id: "WT2", location: 58, side: Side.R },
            { id: "WT3", location: 62, side: Side.R },
            { id: "WT4", location: 68, side: Side.R },
            { id: "WT5", location: 80, side: Side.R },
            { id: "IN1", location: 87, side: Side.R },
            { id: "IN2", location: 90, side: Side.R },
            { id: "IN3", location: 95, side: Side.R },
            { id: "IN4", location: 101, side: Side.R },
            { id: "IN5", location: 107, side: Side.R },
            { id: "UB1", location: 117, side: Side.R },
            { id: "UB2", location: 124, side: Side.R },
            { id: "UB4", location: 136, side: Side.R, offsetx: -15 },
            { id: "UB5", location: 149, side: Side.R, offsety: 15 },
            { id: "UB6", location: 152, side: Side.R, offsetx: -15 },
            { id: "UB7", location: 157, side: Side.R },
            { id: "EX1", location: 163, side: Side.R },
            { id: "EX2", location: 170, side: Side.R },
            { id: "EX3", location: 182, side: Side.R },
            { id: "EX4", location: 185, side: Side.R },
            { id: "EX5", location: 190, side: Side.R },
            { id: "EX6", location: 198, side: Side.R },
        ];
        return config;
    }
}