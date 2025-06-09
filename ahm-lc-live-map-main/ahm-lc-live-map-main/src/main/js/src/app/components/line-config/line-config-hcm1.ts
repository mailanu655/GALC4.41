import { LineConfig } from "../line-config/line-config";
import { Side } from "../line/line-model";

export class Hcm1Config extends LineConfig {

    protected set() {
        this.id = 'hcm1';
        this.c2c = 19;
    }

    protected getAxis(): any[] {
        let config: any[] = [
            { x: 12 + this.c2c, y: 12, step: 2 * this.c2c, values: this.getXAxisValues() },
            { x: 12 + this.c2c, y: 541, step: 2 * this.c2c, values: this.getXAxisValues() },
            { x: 10, y: 13 * this.c2c, step: 8 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 13 * this.c2c, step: 8 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 10, y: 9 * this.c2c, step: 1 * this.c2c, values: ['B'], horizontal: false },
            { x: 1650, y: 9 * this.c2c, step: 1 * this.c2c, values: ['B'], horizontal: false },
        ];
        return config;
    }

    protected getXAxisValues(): string[] {
        let config: string[] = [];
        for (let i = 2; i <= 86; i = i + 2) {
            config.push(i.toString());
        }
        return config;
    }
    protected getYAxisValues(): string[] {
        return ['C', 'D'];
    }

    protected getSections(): any[] {
        let c2cXl = 1.05 * this.c2c;
        let c2cXxl = 1.755 * this.c2c;
        let config: any[] = [
            { id: "AFON", entryx: 73 * this.c2c, entryy: 5.75 * this.c2c, locationCount: 1, ascending: true, entryLength: 1.5 * this.c2c, exitLength: 0.75 * this.c2c, type: 'buffer' },
            { id: "B1", locationCount: 4, c2c: 1.5 * this.c2c, horizontal: true, entryx: 72 * this.c2c, entryy: 9 * this.c2c, entryLength: this.c2c, type: 'buffer' },
            { id: "B2", locationCount: 5 },
            { id: "B3", locationCount: 1, type: 'non-working' },
            { id: "BC", entryx: 85 * this.c2c, entryy: 8 * this.c2c, locationCount: 1, c2c: 2 * this.c2c, horizontal: false, ascending: true, length: 6 * this.c2c, entryLength: 3 * this.c2c, type: 'buffer' },
            { id: "C1", entryx: 84 * this.c2c, entryy: 13 * this.c2c, locationCount: 3, horizontal: true, ascending: false },
            { id: "C2", locationCount: 3, type: 'non-working' },
            { id: "C3", locationCount: 3 },
            { id: "C4", locationCount: 1, type: 'non-working' },
            { id: "C5", locationCount: 37 },
            { id: "C6", c2c: c2cXl, locationCount: 1, type: 'non-working' },
            { id: "C7", c2c: c2cXl, locationCount: 10 },
            { id: "C8", c2c: c2cXl, locationCount: 3, type: 'buffer' },
            { id: "C9", c2c: c2cXl, locationCount: 1, type: 'semi-auto' },
            { id: "C10", c2c: c2cXl, locationCount: 1, type: 'buffer' },
            { id: "C11", c2c: 1.02 * c2cXl, locationCount: 15, exitLength: 1.42 * this.c2c },
            { id: "CD", entryx: 2.5 * this.c2c, entryy: 12 * this.c2c, locationCount: 4, c2c: 2 * this.c2c, horizontal: false, ascending: true, entryLength: 2 * this.c2c, exitLength: 2 * this.c2c, type: 'buffer' },
            { id: "D1", entryx: 3.5 * this.c2c, entryy: 21 * this.c2c, locationCount: 7, horizontal: true, ascending: true, entryLength: 0.5 * this.c2c },
            { id: "D2", locationCount: 1, type: 'buffer' },
            { id: "D3", locationCount: 21 },
            { id: "D4", c2c: c2cXl, locationCount: 2, entryLength: 0.7 * this.c2c, exitLength: 0.7 * this.c2c, type: 'non-working' },
            { id: "D5", c2c: c2cXl, locationCount: 3 },
            { id: "D6", locationCount: 1, type: 'non-working' },
            { id: "D7", locationCount: 1 },
            { id: "D8", locationCount: 2, c2c: c2cXl, entryLength: 0.75 * this.c2c, exitLength: 0.75 * this.c2c, type: 'buffer' },
            { id: "D9", locationCount: 2, c2c: 1.25 * c2cXxl, entryLength: 1.125 * this.c2c, exitLength: 1.125 * this.c2c, type: 'auto' },
            { id: "D10", locationCount: 2, c2c: 1.25 * c2cXxl, entryLength: 1.125 * this.c2c, exitLength: 1.125 * this.c2c, type: 'semi-auto' },
            { id: "D11", locationCount: 2, c2c: 1.25 * c2cXxl, entryLength: 1.125 * this.c2c, exitLength: 1.125 * this.c2c, type: 'buffer' },
            { id: "D12", locationCount: 2, c2c: c2cXxl, entryLength: 1 * this.c2c, exitLength: 1 * this.c2c, type: 'buffer' },
            { id: "D13", locationCount: 1, entryLength: 1 * this.c2c, exitLength: 1 * this.c2c },
            { id: "D14", locationCount: 3, c2c: c2cXxl, entryLength: 0.75 * this.c2c, exitLength: 0.75 * this.c2c, type: 'semi-auto' },
            { id: "D15", locationCount: 2, c2c: c2cXxl, entryLength: 0.75 * this.c2c, exitLength: 0.75 * this.c2c, type: 'semi-auto' },
            { id: "D16", locationCount: 6, c2c: 1.55 * this.c2c, entryLength: 0.75 * this.c2c, exitLength: 1 * this.c2c },
        ];
        return config;
    }

    protected getLandMarks(): any[] {
        return [];
    }

    protected getLabels(): any[] {
        let firstSection: any = this.sections[0];
        let config: any[] = [
            { text: "AF ON", x: firstSection.entryx, y: firstSection.entryy - 3 * this.c2c },
            { text: "AF OFF", x: 83 * this.c2c, y: 23 * this.c2c }
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
            { id: "Door Off", location: 3, side: Side.L, offsetx: 12 },
            { id: "Rotate", location: 5, side: Side.L, offsetx: 8 },
            { id: "Fuel Tank", location: 20, side: Side.R },
            { id: "Sunroof", location: 34, side: Side.R },
            { id: "HVAC", location: 48, side: Side.R, offsetx: 10 },
            { id: "Roof Lining", location: 51, side: Side.R, offsetx: 10, offsety: -15 },
            { id: "Cert Label", location: 53, side: Side.R },
            { id: "IP", location: 58, side: Side.R },
            { id: "GSMURF", location: 74, side: Side.R },
            { id: "FEM", location: 102, side: Side.L },
            { id: "Battery", location: 112, side: Side.L },
            { id: "Seats", location: 120, side: Side.L, offsetx: -3 },
            { id: "Fr. Bumper", location: 122, side: Side.L, offsetx: 15 },
            { id: "Rotate", location: 132, side: Side.R },
            { id: "Glass", location: 133, side: Side.L, offsetx: 15 },
            { id: "Tire Tightening", location: 136, side: Side.L, offsetx: -15 },
            { id: "Door Install", location: 138, side: Side.L, offsetx: -15, offsety: -15 },
            { id: "Triple Fill", location: 143, side: Side.L },
            { id: "Gas/Washer", location: 146, side: Side.L },
            { id: "Immobi", location: 149, side: Side.L, offsetx: -10 },
            { id: "Qtr Glass", location: 150, side: Side.R, offsetx: -15 }
        ];
        return config;
    }
    protected getTeamLeaderAreas(): any[] {
        let config: any[] = [
            { id: "WT01", location: 8, side: Side.L },
            { id: "WT02", location: 19, side: Side.L },
            { id: "WT03", location: 26, side: Side.L },
            { id: "WT04", location: 33, side: Side.L },
            { id: "WT05", location: 39, side: Side.L },
            { id: "IN01", location: 45, side: Side.L },
            { id: "IN02", location: 51, side: Side.L },
            { id: "IN03", location: 58, side: Side.L },
            { id: "IN04", location: 65, side: Side.L },
            { id: "UB01", location: 74, side: Side.L },
            { id: "UB02", location: 82, side: Side.L },
            { id: "DL2", location: 90, side: Side.R, offsetx: -20 },
            { id: "DL3", location: 95, side: Side.L, offsetx: -5 },
            { id: "EX01", location: 95, side: Side.R },
            { id: "EX02", location: 103, side: Side.R },
            { id: "EX03", location: 108, side: Side.R },
            { id: "EX04", location: 114, side: Side.R },
            { id: "EX05", location: 121, side: Side.R, offsetx: 10 },
            { id: "EX06", location: 135, side: Side.R },
            { id: "BT1", location: 139, side: Side.L, offsetx: 15 },
            { id: "EX07", location: 140, side: Side.R },
            { id: "EX08", location: 146, side: Side.R, offsetx: -5 },
            { id: "AAI1", location: 152, side: Side.R },
        ];
        return config;
    }
}