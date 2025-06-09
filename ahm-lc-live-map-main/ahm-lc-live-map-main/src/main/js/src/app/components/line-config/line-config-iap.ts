import { LineConfig } from "../line-config/line-config";
import { Side } from "../line/line-model";

export class IapConfig extends LineConfig {

    protected set() {
        this.id = 'iap';
        this.c2c = 23;
    }

    protected init() {
        super.init();
        this.startLength = 0.5 * this.c2c;
    }

    protected getAxis(): any[] {
        let config: any[] = [
            { x: 64, y: 12, step: 51, values: this.getXAxisValues() },
            { x: 64, y: 541, step: 51, values: this.getXAxisValues() },
            { x: 10, y: 131, step: 5.1 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 131, step: 5.1 * this.c2c, values: this.getYAxisValues(), horizontal: false }
        ];
        return config;
    }

    protected getXAxisValues(): string[] {
        let config: string[] = [];
        for (let i = 3; i <= 32; i++) {
            config.push(i.toString());
        }
        return config;
    }
    protected getYAxisValues(): string[] {
        let config: string[] = [];
        for (let i of ['F', 'E', 'D', 'C']) {
            config.push(i);
        }
        return config;
    }

    protected getSections(): any[] {
        let config: any[] = [
            { id: "AFON", entryx: 66.5 * this.c2c, entryy: 2.5 * this.c2c, locationCount: 1, horizontal: false, ascending: true, length: 2 * this.c2c, entryLength: this.c2c },
            { id: "AB1", locationCount: 1, type: 'non-working' },
            { id: "AB2", locationCount: 1 },
            { id: "AB3", locationCount: 1, type: 'non-working' },
            { id: "AB4", locations: [{ id: 4.1, code: 0 }], type: 'buffer' },
            { id: "AB5", entryx: 65.5 * this.c2c, entryy: 9.125 * this.c2c, locations: [{ id: 4.2, code: 0 }, { id: 4.3, code: 0 }], horizontal: true, ascending: true, width: 1.25 * this.c2c, entryLength: this.c2c, type: 'buffer' },
            { id: "AB6", entryx: 69 * this.c2c, entryy: 8.5 * this.c2c, locations: [{ id: 4.4, code: 0 }], horizontal: false, ascending: true, length: 3.5 * this.c2c, entryLength: 1.6 * this.c2c, type: 'buffer' },

            { id: "B1", entryx: 68 * this.c2c, entryy: 11 * this.c2c, locations: [{ id: 4.5, code: 0 }, { id: 4.6, code: 0 }], horizontal: true, ascending: false, type: 'buffer' },
            { id: "B2", locationCount: 1, type: 'buffer' },
            { id: "B3", locationCount: 3, type: 'non-working' },
            { id: "B4", locationCount: 20 },
            { id: "B5", locationCount: 1, type: 'non-working' },
            { id: "B6", locationCount: 2, type: 'buffer' },
            { id: "B7", locationCount: 29 },
            { id: "B8", locationCount: 3, type: 'inspection' },
            { id: "B9", locationCount: 1, type: 'non-working' },
            { id: "B10U", entryx: 6 * this.c2c, entryy: 10.5 * this.c2c, locations: [{ id: 64.1, code: 0 }], width: this.c2c, connect: false, type: 'buffer' },
            { id: "B10L", entryx: 6 * this.c2c, entryy: 11.5 * this.c2c, locations: [{ id: 64.2, code: 0 }], width: this.c2c, connect: false, type: 'buffer' },
            { id: "B11", entryx: 5 * this.c2c, entryy: 11 * this.c2c, locationCount: 0, type: 'buffer' },

            { id: "BC1", entryx: 3 * this.c2c, entryy: 10 * this.c2c, locations: [{ id: 64.3, code: 0 }, { id: 64.4, code: 0 }, { id: 64.5, code: 0 }], horizontal: false, ascending: true, length: 6 * this.c2c, entryLength: 2 * this.c2c, c2c: 1.5 * this.c2c, type: 'buffer' },
            { id: "BC2", locationCount: 3, length: 6 * this.c2c, entryLength: 1 * this.c2c, c2c: 1.5 * this.c2c, type: 'buffer' },

            { id: "C1", entryx: 4 * this.c2c, entryy: 21 * this.c2c, locationCount: 1, horizontal: true, ascending: true, type: 'buffer' },
            { id: "C2U", entryx: 5 * this.c2c, entryy: 20.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'buffer' },
            { id: "C2L", entryx: 5 * this.c2c, entryy: 21.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'buffer' },
            { id: "C3", entryx: 6 * this.c2c, entryy: 21 * this.c2c, locationCount: 1, type: 'non-working' },
            { id: "C4", locationCount: 26 },
            { id: "C5", locationCount: 3, type: 'inspection' },
            { id: "C6", locationCount: 2, type: 'buffer' },
            { id: "C7", locationCount: 12 },
            { id: "C8", locationCount: 1, type: 'non-working' },
            { id: "C9", locationCount: 1, entryLength: this.c2c, length: 2 * this.c2c, type: 'semi-auto' },
            { id: "C10", locationCount: 1, entryLength: this.c2c, length: 2 * this.c2c },
            { id: "C11", locationCount: 7 },
            { id: "C12", locationCount: 3, type: 'non-working' },

            { id: "CD1", entryx: 66 * this.c2c, entryy: 22 * this.c2c, locations: [{ id: 127.1, code: 0 }], horizontal: false, ascending: false, length: 2.25 * this.c2c, entryLength: 1.75 * this.c2c, type: 'buffer' },
            { id: "CD2", locationCount: 3, length: 4.25 * this.c2c, type: 'buffer' },

            { id: "D1", entryx: 65 * this.c2c, entryy: 16.5 * this.c2c, locationCount: 3, horizontal: true, ascending: false, type: 'non-working' },
            { id: "D2", locationCount: 21 },
            { id: "D3", locationCount: 3, type: 'inspection' },
            { id: "D4", locationCount: 1, type: 'non-working' },
            { id: "D5", locationCount: 2, type: 'buffer' },
            { id: "D6", locationCount: 1, type: 'auto' },
            { id: "D7", locationCount: 1, type: 'non-working' },
            { id: "D8", locationCount: 8 },
            { id: "D9", locationCount: 1, length: 2 * this.c2c, entryLength: this.c2c, type: 'buffer' },
            { id: "D10", locationCount: 1, type: 'buffer' },
            { id: "D11", locationCount: 10 },
            { id: "D12", locationCount: 3, type: 'non-working' },

            { id: "DF1", entryx: 8 * this.c2c, entryy: 17.5 * this.c2c, locations: [{ id: 185.1, code: 0 }, { id: 185.2, code: 0 }], horizontal: false, ascending: false, length: 3.5 * this.c2c, entryLength: 2 * this.c2c, type: 'buffer' },
            { id: "DF2", locationCount: 2, type: 'buffer' },
            { id: "DF3", entryx: 8 * this.c2c, entryy: 10 * this.c2c, locationCount: 3, length: 5.5 * this.c2c, entryLength: 1.5 * this.c2c, type: 'buffer' },

            { id: "F1", entryx: 9 * this.c2c, entryy: 5.5 * this.c2c, locationCount: 3, horizontal: true, ascending: true, type: 'non-working' },
            { id: "F2", locationCount: 9 },
            { id: "F3", locationCount: 3, type: 'non-working' },
            { id: "F4L", entryx: 24 * this.c2c, entryy: 6 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'buffer' },
            { id: "F4U", entryx: 24 * this.c2c, entryy: 5 * this.c2c, locations: [{ id: 206.1, code: 0 }], width: this.c2c, connect: false, type: 'buffer' },
            { id: "F6", entryx: 25 * this.c2c, entryy: 5.5 * this.c2c, locationCount: 14 },
            { id: "F7", locationCount: 2, type: 'inspection' },
            { id: "F8", locationCount: 6 },
            { id: "F9", locationCount: 4, type: 'non-working' },
            { id: "F10", locationCount: 3, type: 'inspection' },
            { id: "F11", locationCount: 4, type: 'non-working' },
            { id: "F12", locationCount: 3, type: 'inspection' },
            { id: "F13", locationCount: 2, type: 'non-working' },
        ];
        return config;
    }

    protected getLandMarks(): any[] {
        let config: any[] = [
        ];
        return config;
    }

    protected getLabels(): any[] {
        let firstSection: any = this.sections[0];
        let config: any[] = [
            { text: "AF ON", x: firstSection.entryx + 2.5 * this.c2c, y: firstSection.entryy + this.c2c },
            { text: "AF OFF", x: 59.5 * this.c2c, y: 4 * this.c2c }
        ];
        return config;
    }

    protected getStations(): any[] {
        let config: any[] = [
            { locationId: 61, side: Side.R },
            { locationId: 99, side: Side.R },
            { locationId: 141, side: Side.R },
            { locationId: 221, side: Side.R },
            { locationId: 234, side: Side.R },
            { locationId: 241, side: Side.R },
        ];
        return config;
    }

    protected getProcessAreas(): any[] {
        let config: any[] = [
            { id: "Door Off", location: 3, side: Side.L, offsetx: 6 },
            { id: "Fuel Tank", location: 12, side: Side.L },
            { id: "Sunroof", location: 33, side: Side.L },
            { id: "IPU", location: 49, side: Side.L },
            { id: "Headliner", location: 76, side: Side.L },
            { id: "HVAC", location: 80, side: Side.L },
            { id: "Carpet", location: 86, side: Side.L },
            { id: "Carpet", location: 86, side: Side.L },
            { id: "MM/ETR", location: 116, side: Side.L },
            { id: "Exhaust", location: 122, side: Side.L },
            { id: "Brake Fill", location: 151, side: Side.L },
            { id: "Glass", location: 161, side: Side.L },
            { id: "Battery", location: 164, side: Side.L },
            { id: "FEM", location: 167, side: Side.L },
            { id: "Seats", location: 175, side: Side.L },
            { id: "Tires", location: 180, side: Side.L },
            { id: "Bpr", location: 195, side: Side.L, offsetx: -5 },
            { id: "Door On", location: 196, side: Side.L, offsetx: 20 },
            { id: "Rad Fill", location: 211, side: Side.L },
            { id: "HAC Fill", location: 223, side: Side.L },
            { id: "Gas Fill", location: 226, side: Side.L },
        ];
        return config;
    }

    protected getTeamLeaderAreas(): any[] {
        let config = [
            { id: "WT01", locations: [], location: 1, side: Side.R, offsetx: 3 },
            { id: "WT01", locations: [], location: 9, side: Side.R },
            { id: "WT02", locations: [], location: 18, side: Side.R },
            { id: "WT03", locations: [], location: 25, side: Side.R },
            { id: "WT04", locations: [], location: 33, side: Side.R },
            { id: "WT05", locations: [], location: 40, side: Side.R },
            { id: "WT06", locations: [], location: 44, side: Side.R },
            { id: "WT07", locations: [], location: 51, side: Side.R },
            { id: "WT08", locations: [], location: 57, side: Side.R },
            { id: "IN01", locations: [], location: 73, side: Side.R },
            { id: "IN02", locations: [], location: 79, side: Side.R },
            { id: "IN03", locations: [], location: 83, side: Side.R },
            { id: "IN04", locations: [], location: 89, side: Side.R },
            { id: "IN05", locations: [], location: 95, side: Side.R },
            { id: "UB01", locations: [], location: 104, side: Side.R },
            { id: "UB02", locations: [], location: 115, side: Side.R },
            { id: "UB03", locations: [], location: 122, side: Side.R },
            { id: "UB03", locations: [], location: 134, side: Side.R },
            { id: "UB04", locations: [], location: 139, side: Side.R },
            { id: "EX01", locations: [], location: 145, side: Side.R },
            { id: "EX02", locations: [], location: 149, side: Side.R },
            { id: "EX03", locations: [], location: 161, side: Side.R },
            { id: "EX04", locations: [], location: 166, side: Side.R },
            { id: "EX05", locations: [], location: 173, side: Side.R },
            { id: "EX06", locations: [], location: 178, side: Side.R },
            { id: "EX07", locations: [], location: 194, side: Side.R },
            { id: "EX08", locations: [], location: 200, side: Side.R },
            { id: "EX09", locations: [], location: 208, side: Side.R },
            { id: "EX10", locations: [], location: 216, side: Side.R },
            { id: "EX11", locations: [], location: 224, side: Side.R },
        ];
        return config;
    }
}