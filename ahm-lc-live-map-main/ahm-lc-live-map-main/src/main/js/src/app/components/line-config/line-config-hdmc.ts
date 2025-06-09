
import { LineConfig } from "../line-config/line-config";
import { Side } from "../line/line-model";

export class HdmcConfig extends LineConfig {

    protected set() {
        this.id = 'hdmc';
        this.c2c = 27;
    }

    protected init() {
        super.init();
        this.startLength = 12;
        this.endLength = 12;
    }

    protected getAxis(): any[] {
        let config: any[] = [
            { x: 12 + this.c2c, y: 12, step: 2 * this.c2c, values: this.getXAxisValues() },
            { x: 12 + this.c2c, y: 541, step: 2 * this.c2c, values: this.getXAxisValues() },
            { x: 10, y: 3 * this.c2c, step: 4.5 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 10, y: 18 * this.c2c, step: this.c2c, values: this.getYAxisValues2(), horizontal: false },
            { x: 1650, y: 3 * this.c2c, step: 4.5 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 18 * this.c2c, step: this.c2c, values: this.getYAxisValues2(), horizontal: false }
        ];
        return config;
    }

    protected getXAxisValues(): string[] {
        let config: string[] = [];
        for (let i = 2; i <= 60; i = i + 2) {
            config.push(i.toString());
        }
        return config;
    }
    protected getYAxisValues(): string[] {
        return ['A', 'B', 'C'];
    }

    protected getYAxisValues2(): string[] {
        return ['D'];
    }

    protected getSections(): any[] {
        let config: any[] = [
            { id: "AFON", entryx: 9 * this.c2c, entryy: 18.125 * this.c2c, locationCount: 2, horizontal: false, ascending: false },
            { id: "AB1", locationCount: 1, type: 'non-working' },
            { id: "AB2", locations: [{ id: 3.1, code: 0 }], type: 'buffer' },
            { id: "AB3", entryx: 10 * this.c2c, entryy: 13.5 * this.c2c, locations: [{ id: 3.2, code: 0 }, { id: 3.3, code: 0 }], horizontal: true, ascending: false, width: 1.25 * this.c2c, entryLength: this.c2c, type: 'buffer' },
            { id: "AB4", entryx: 6.5 * this.c2c, entryy: 14.125 * this.c2c, locations: [{ id: 3.4, code: 0 }], horizontal: false, ascending: false, length: 2.75 * this.c2c, entryLength: 1.36 * this.c2c, type: 'buffer' },

            { id: "B1", entryx: 7.5 * this.c2c, entryy: 12 * this.c2c, locations: [{ id: 3.5, code: 0 }], horizontal: true, ascending: true, width: 1.25 * this.c2c, type: 'buffer' },
            { id: "B2", locationCount: 1, length: 2.5 * this.c2c, width: 1.25 * this.c2c, type: 'buffer' },
            { id: "B3", locationCount: 3, type: 'non-working' },
            { id: "B4", locationCount: 4 },
            { id: "B5", locationCount: 1, type: 'non-working' },
            { id: "B6", locationCount: 23 },
            { id: "B7", locationCount: 1, type: 'non-working' },
            { id: "B8", locationCount: 3 },
            { id: "B9", locationCount: 3, type: 'non-working' },
            { id: "B10", locationCount: 2 },

            { id: "BC", entryx: 52 * this.c2c, entryy: 13 * this.c2c, locationCount: 3, horizontal: false, ascending: false, length: 6.5 * this.c2c, entryLength: 2.25 * this.c2c },

            { id: "C1", entryx: 51 * this.c2c, entryy: 7.5 * this.c2c, locationCount: 2, horizontal: true, ascending: false },
            { id: "C2", locations: [{ id: 49.1, code: 0 }], type: 'non-working' },
            { id: "C3", locationCount: 3, type: 'non-working' },
            { id: "C4", locationCount: 18 },
            { id: "C5", locationCount: 1, type: 'non-working' },
            { id: "C6", locationCount: 9 },
            { id: "C7", locationCount: 1, type: 'non-working' },
            { id: "C8", locationCount: 5 },
            { id: "C9", locationCount: 3, type: 'non-working' },
            { id: "C10", locations: [{ id: 89.1, code: 0 }], type: 'buffer' },

            { id: "CD1", entryx: 6 * this.c2c, entryy: 8.5 * this.c2c, locations: [{ id: 89.2, code: 0 }], horizontal: false, ascending: false, length: 3.25 * this.c2c, entryLength: 2.25 * this.c2c, type: 'buffer' },
            { id: "CD2", locationCount: 1, length: 3.25 * this.c2c, entryLength: 1 * this.c2c, type: 'buffer' },

            { id: "D1", entryx: 7 * this.c2c, entryy: 3 * this.c2c, locationCount: 2, horizontal: true, ascending: true, length: 4 * this.c2c, entryLength: this.c2c, c2c: 2 * this.c2c, type: 'buffer' },
            { id: "D2", locationCount: 1, length: 2 * this.c2c, entryLength: this.c2c, type: 'non-working' },
            { id: "D3", locationCount: 1, type: 'semi-auto' },
            { id: "D4", locationCount: 1, type: 'non-working' },
            { id: "D5", locationCount: 12 },
            { id: "D6", locationCount: 1, type: 'non-working' },
            { id: "D7", locationCount: 22 },
            { id: "D8", locationCount: 3, type: 'non-working' },
            { id: "D9", locations: [{ id: 133.1, code: 0 }], length: 2 * this.c2c, entryLength: this.c2c, type: 'buffer' },

            { id: "DE1", entryx: 56 * this.c2c, entryy: 2 * this.c2c, locations: [{ id: 133.2, code: 0 }, { id: 133.3, code: 0 }, { id: 133.4, code: 0 }], horizontal: false, ascending: true, length: 8 * this.c2c, entryLength: 3 * this.c2c, c2c: 2 * this.c2c, type: 'buffer' },
            { id: "DE2", locations: [{ id: 133.5, code: 0 }, { id: 133.6, code: 0 }], type: 'buffer' },
            { id: "DE3", locationCount: 1, type: 'buffer' },
            { id: "DE4", locationCount: 2, length: 4 * this.c2c, entryLength: 1.25 * this.c2c, c2c: 1.5 * this.c2c, type: 'buffer' },

            { id: "E1", entryx: 57 * this.c2c, entryy: 18 * this.c2c, locationCount: 2, horizontal: true, ascending: false, entryLength: 2.5 * this.c2c, type: 'buffer' },
            { id: "E2U", entryx: 53 * this.c2c, entryy: 17.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'buffer' },
            { id: "E2D", entryx: 53 * this.c2c, entryy: 18.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'non-working' },
            { id: "E3", entryx: 52 * this.c2c, entryy: 18 * this.c2c, locationCount: 1, type: 'non-working' },
            { id: "E4", locationCount: 7 },
            { id: "E5", locationCount: 1, type: 'semi-auto' },
            { id: "E6", locationCount: 1 },
            { id: "E7", locationCount: 1, type: 'semi-auto' },
            { id: "E8", locationCount: 16 },
            { id: "E9", locationCount: 2, type: 'non-working' },
            { id: "E10", locationCount: 1, type: 'inspection' },
            { id: "E11", locationCount: 9, type: 'non-working' },
        ];
        return config;
    }

    protected getLandMarks(): any[] {
        return [];
    }

    protected getLabels(): any[] {
        let firstSection: any = this.sections[0];
        let config: any[] = [
            { text: "AF ON", x: firstSection.entryx - 2 * this.c2c, y: firstSection.entryy + 0.9 * this.c2c },
            { text: "AF OFF", x: 26.5 * this.c2c, y: 16 * this.c2c }
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
            { id: "Door Off", location: 2, side: Side.L },
            { id: "Sunroof", location: 29, side: Side.L },
            { id: "Qtr Glass", location: 34, side: Side.L },
            { id: "Headliner", location: 53, side: Side.L, offsetx: 6 },
            { id: "Carpet", location: 55, side: Side.L },
            { id: "HVAC", location: 59, side: Side.L },
            { id: "IP", location: 65, side: Side.L },
            { id: "MM", location: 94, side: Side.L },
            { id: "Fuel Tank", location: 97, side: Side.L },
            { id: "Exhaust", location: 103, side: Side.L },
            { id: "Brake Fill", location: 111, side: Side.L },
            { id: "Rr Seat", location: 118, side: Side.L },
            { id: "Fr Seat", location: 122, side: Side.L },
            { id: "Bumper", location: 125, side: Side.L },
            { id: "Tires", location: 129, side: Side.L },
            { id: "Roof Rail", location: 142, side: Side.L },
            { id: "Fr Glass", location: 149, side: Side.L, offsetx: 6 },
            { id: "Rr Glass", location: 151, side: Side.L, offsetx: 0 },
            { id: "Battery", location: 152, side: Side.L, offsetx: -10, offsety: -15 },
            { id: "SUVA Fill", location: 153, side: Side.L, offsetx: -20 },
            { id: "Door On", location: 159, side: Side.L },
            { id: "HFO/Gas Fill", location: 165, side: Side.L }
        ];
        return config;
    }
    protected getTeamLeaderAreas(): any[] {
        let config: any[] = [
            { id: "WT01", location: 1, side: Side.R, offsetx: -8 },
            { id: "WT02", location: 13, side: Side.R },
            { id: "WT03", location: 20, side: Side.R },
            { id: "WT04", location: 28, side: Side.R },
            { id: "WT05", location: 37, side: Side.R },
            { id: "IN01", location: 53, side: Side.R },
            { id: "IN02", location: 58, side: Side.R },
            { id: "IN03", location: 64, side: Side.R },
            { id: "IN04", location: 69, side: Side.R },
            { id: "IN05", location: 76, side: Side.R },
            { id: "UB01", location: 96, side: Side.R },
            { id: "UB02", location: 101, side: Side.R },
            { id: "EX01", location: 109, side: Side.R },
            { id: "EX02", location: 115, side: Side.R },
            { id: "EX03", location: 123, side: Side.R },
            { id: "EX04", location: 142, side: Side.R },
            { id: "EX05", location: 150, side: Side.R },
            { id: "CP01", location: 159, side: Side.R },
            { id: "CP02", location: 167, side: Side.R },
            { id: "CP03", location: 170, side: Side.R },
        ];
        return config;
    }
}