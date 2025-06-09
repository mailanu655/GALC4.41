import { LineConfig } from "./line-config";
import { Side } from "../line/line-model";

export class Aap1Config extends LineConfig {

    protected set() {
        this.id = 'aap1';
        this.c2c = 27;
    }

    protected init() {
        super.init();
    }

    protected getAxis(): any[] {
        let config: any[] = [
            { x: 34, y: 12, step: 1.875 * this.c2c, values: this.getXAxisValues() },
            { x: 34, y: 541, step: 1.875 * this.c2c, values: this.getXAxisValues() },
            { x: 10, y: 3 * this.c2c, step: 4 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 10, y: 18 * this.c2c, step: 4 * this.c2c, values: [this.getYAxisValues2()], horizontal: false },
            { x: 1650, y: 3 * this.c2c, step: 4 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 18 * this.c2c, step: 4 * this.c2c, values: [this.getYAxisValues2()], horizontal: false }
        ];
        return config;
    }

    protected getXAxisValues(): string[] {
        let config: string[] = [];
        for (let i = 1; i <= 32; i++) {
            config.push(i.toString());
        }
        return config;
    }
    protected getYAxisValues(): string[] {
        return ['B', 'C', 'D', 'E'];
    }

    protected getYAxisValues2(): string[] {
        return ['F'];
    }

    protected getSections(): any[] {
        let config: any[] = [
            { id: "AFON", entryx: 52.5 * this.c2c, entryy: 11 * this.c2c, locationCount: 3, horizontal: true, ascending: false, entryLength: this.c2c },
            { id: "A1", locations: [{ id: 3.1, code: 0 }], type: 'buffer' },
            { id: "A2", locationCount: 2, type: 'buffer' },
            { id: "A3", locationCount: 4, type: 'non-working' },
            { id: "A4", locationCount: 13 },
            { id: "A5", locationCount: 1, type: 'non-working' },
            { id: "A6", locationCount: 2, type: 'buffer' },
            { id: "A7", locationCount: 10, },
            { id: "A8", locationCount: 2, length: 3 * this.c2c, c2c: 1.5 * this.c2c, type: 'non-working' },
            { id: "A9", locations: [{ id: 37.1, code: 0 }, { id: 37.2, code: 0 }, { id: 37.3, code: 0 }, { id: 37.4, code: 0 }], type: 'buffer' },

            { id: "AB1", entryx: 8 * this.c2c, entryy: 12 * this.c2c, locations: [{ id: 37.5, code: 0 }], horizontal: false, ascending: false, length: 3 * this.c2c, entryLength: 2 * this.c2c, type: 'buffer' },
            { id: "AB2", locationCount: 1, length: 3 * this.c2c, entryLength: 1 * this.c2c, type: 'buffer' },

            { id: "B1", entryx: 9 * this.c2c, entryy: 7 * this.c2c, locationCount: 2, horizontal: true, ascending: true, type: 'buffer' },
            { id: "B2", locationCount: 1, type: 'non-working' },
            { id: "B3", locationCount: 12, },
            { id: "B4", locationCount: 1, type: 'auto' },
            { id: "B5", locationCount: 5, },
            { id: "B6", locationCount: 1, type: 'non-working' },
            { id: "B7", locationCount: 2, type: 'buffer' },
            { id: "B8", locationCount: 12, },
            { id: "B9", locationCount: 1, type: 'non-working' },
            { id: "B10", locationCount: 1, type: 'semi-auto' },
            { id: "B11", locationCount: 1, type: 'non-working' },
            { id: "B12", locationCount: 1, },
            { id: "B13", locationCount: 1, type: 'non-working' },
            { id: "B14", locationCount: 1, },
            { id: "B15", locationCount: 2, type: 'non-working' },
            { id: "B16", locations: [{ id: 82.1, code: 0 }, { id: 82.2, code: 0 }], type: 'buffer' },

            { id: "BC1", entryx: 56 * this.c2c, entryy: 8 * this.c2c, locations: [{ id: 82.3, code: 0 }], horizontal: false, ascending: false, length: 3 * this.c2c, entryLength: 2 * this.c2c, type: 'buffer' },
            { id: "BC2", locationCount: 1, length: 3 * this.c2c, entryLength: 1 * this.c2c, type: 'buffer' },

            { id: "C1", entryx: 55 * this.c2c, entryy: 3 * this.c2c, locationCount: 2, horizontal: true, ascending: false, type: 'buffer' },
            { id: "C2", locationCount: 3, type: 'non-working' },
            { id: "C3", locationCount: 14 },
            { id: "C4", locationCount: 1, type: 'non-working' },
            { id: "C5", locationCount: 1, type: 'inspection' },
            { id: "C6", locationCount: 2, type: 'non-working' },
            { id: "C7U", entryx: 32 * this.c2c, entryy: 2.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'buffer' },
            { id: "C7D", entryx: 32 * this.c2c, entryy: 3.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'buffer' },
            { id: "C8", entryx: 31 * this.c2c, entryy: 3 * this.c2c, locationCount: 1, type: 'auto' },
            { id: "C9", locationCount: 1, type: 'non-working' },
            { id: "C10", locationCount: 9 },
            { id: "C11", locationCount: 2, type: 'buffer' },
            { id: "C12", locationCount: 6 },
            { id: "C13", locationCount: 2, type: 'non-working' },
            { id: "C14", locationCount: 1, length: 4 * this.c2c, entryLength: 2 * this.c2c, type: 'non-working' },

            { id: "CD1", entryx: 5 * this.c2c, entryy: 2 * this.c2c, locations: [{ id: 130.1, code: 0 }, { id: 130.2, code: 0 }, { id: 130.3, code: 0 }, { id: 130.4, code: 0 }, { id: 130.5, code: 0 }, { id: 130.6, code: 0 }], horizontal: false, ascending: true, length: 9.25 * this.c2c, entryLength: 2.6 * this.c2c, c2c: 1.2 * this.c2c, type: 'buffer' },
            { id: "CD2", locationCount: 2, length: 4.75 * this.c2c, entryLength: 0.75 * this.c2c, c2c: 1.2 * this.c2c, type: 'buffer' },

            { id: "D1", entryx: 6 * this.c2c, entryy: 15 * this.c2c, locationCount: 6, horizontal: true, ascending: true, entryLength: 1.5 * this.c2c, type: 'buffer' },

            { id: "D2U", entryx: 13 * this.c2c, entryy: 14.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'buffer' },
            { id: "D2D", entryx: 13 * this.c2c, entryy: 15.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'non-working' },
            { id: "D3", entryx: 14 * this.c2c, entryy: 15 * this.c2c, locationCount: 11 },
            { id: "D4", locationCount: 1, type: 'inspection' },

            { id: "D5", locationCount: 8 },
            { id: "D6", locationCount: 1, type: 'inspection' },

            { id: "E1", entryx: 36 * this.c2c, entryy: 15 * this.c2c, locations: [{ id: 162 }, { id: 164 }, { id: 166 }], type: 'non-working' },
            { id: "E2", locations: [{ id: 168 }, { id: 170 }, { id: 172 }, { id: 174 }, { id: 176 }, { id: 178 }], exitLength: this.c2c, end: true, type: 'inspection' },

            { id: "DF1", entryx: 34 * this.c2c, entryy: 15 * this.c2c, locationCount: 0, width: this.c2c, landMark: false },
            { id: "DF2", entryx: 35.3 * this.c2c, entryy: 16 * this.c2c, locationCount: 0, horizontal: false, ascending: true, width: this.c2c, landMark: false },

            { id: "F1", entryx: 36 * this.c2c, entryy: 18 * this.c2c, locations: [{ id: 163 }, { id: 165 }, { id: 167 }], horizontal: true, ascending: true, type: 'non-working' },
            { id: "F2", locations: [{ id: 169 }, { id: 171 }, { id: 173 }, { id: 175 }, { id: 177 }, { id: 179 }], exitLength: this.c2c, type: 'inspection' },
        ];
        return config;
    }

    protected getLandMarks(): any[] {
        return [];
    }

    protected getLabels(): any[] {
        let firstSection: any = this.sections[0];
        let config: any[] = [
            { text: "AF ON", x: firstSection.entryx - 1.2 * this.c2c, y: firstSection.entryy + 2 * this.c2c },
            { text: "AF OFF", x: 47 * this.c2c, y: 16.6 * this.c2c }
        ];
        return config;
    }

    protected getStations(): any[] {
        let stations: any[] = [
            { locationId: 35, side: Side.R },
            { locationId: 63, side: Side.L },
            { locationId: 104, side: Side.R },
            { locationId: 152, side: Side.L },
            { locationId: 176, side: Side.L },
            { locationId: 177, side: Side.L }
        ];
        return stations;
    }

    protected getProcessAreas(): any[] {
        let config: any[] = [
            { id: "Door Off", location: 3, side: Side.L, offsetx: 16 },
            { id: "Sunroof", location: 28, side: Side.L },
            { id: "T-gate", location: 33, side: Side.L },
            { id: "Bed Liner", location: 35, side: Side.L },
            { id: "Mid Carpet Headliner", location: 45, side: Side.L },
            { id: "HVAC", location: 50, side: Side.L },
            { id: "IP", location: 54, side: Side.L },
            { id: "Cor Glass", location: 59, side: Side.L },
            { id: "Qtr Glass", location: 65, side: Side.L },
            { id: "Fuel Tank", location: 73, side: Side.L },
            { id: "SMURF", location: 76, side: Side.L },
            { id: "Brake Fill", location: 99, side: Side.L },
            { id: "Glass", location: 109, side: Side.L },
            { id: "Battery", location: 115, side: Side.L },
            { id: "Seats", location: 118, side: Side.L },
            { id: "Bumper", location: 123, side: Side.L },
            { id: "Tires", location: 127, side: Side.L },
            { id: "Rf Rail", location: 141, side: Side.L },
            { id: "Door On", location: 143, side: Side.L },
            { id: "Immobi", location: 146, side: Side.L },
            { id: "Rad Fill", location: 153, side: Side.L, offsetx: 10 },
            { id: "Gas/HAC Fill", location: 157, side: Side.L },
            { id: "AAI", location: 161, side: Side.L },
            { id: "AAI/LET", location: 170, side: Side.L },
        ];
        return config;
    }
    protected getTeamLeaderAreas(): any[] {
        let config = [
            { id: "WT01", location: 1, side: Side.R },
            { id: "WT02", location: 10, side: Side.R },
            { id: "WT03", location: 17, side: Side.R },
            { id: "WT04", location: 26, side: Side.R },
            { id: "WT05", location: 31, side: Side.R },
            { id: "IN01", location: 42, side: Side.R },
            { id: "IN02", location: 49, side: Side.R },
            { id: "IN03", location: 53, side: Side.R },
            { id: "IN04", location: 63, side: Side.R },
            { id: "UB01", location: 67, side: Side.R },
            { id: "UB02", location: 72, side: Side.R },
            { id: "UB03", location: 79, side: Side.R, },
            { id: "EX01", location: 99, side: Side.R },
            { id: "EX02", location: 111, side: Side.R },
            { id: "EX03", location: 114, side: Side.R },
            { id: "EX04", location: 122, side: Side.R },
            { id: "EX05", location: 141, side: Side.R },
            { id: "EX06", location: 145, side: Side.R },
            { id: "EX07", location: 150, side: Side.R },
        ];
        return config;
    }
}