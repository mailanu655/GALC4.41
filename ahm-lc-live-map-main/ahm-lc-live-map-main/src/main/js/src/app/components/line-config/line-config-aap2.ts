import { LineConfig } from "../line-config/line-config";
import { Side } from "../line/line-model";

export class Aap2Config extends LineConfig {

    protected set() {
        this.id = 'aap2';
    }

    protected getAxis(): any[] {
        let config: any[] = [
            { x: 1624, y: 12, step: -1.9 * this.c2c, values: this.getXAxisValues() },
            { x: 1624, y: 541, step: -1.9 * this.c2c, values: this.getXAxisValues() },
            { x: 10, y: 3 * this.c2c, step: 6 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 10, y: 18 * this.c2c, step: 2.5 * this.c2c, values: this.getYAxisValues2(), horizontal: false },
            { x: 1650, y: 3 * this.c2c, step: 6 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 18 * this.c2c, step: 2.5 * this.c2c, values: this.getYAxisValues2(), horizontal: false },
        ];
        return config;
    }

    protected getXAxisValues(): string[] {
        let config: string[] = [];
        for (let i = 3; i <= 38; i++) {
            config.push(i.toString());
        }
        return config;
    }

    protected getYAxisValues(): string[] {
        return ['B', 'C', 'D'];
    }

    protected getYAxisValues2(): string[] {
        return ['E', 'F'];
    }

    protected getSections(): any[] {
        let config: any[] = [
            { id: "AFON", entryx: 13 * this.c2c, entryy: 20.5 * this.c2c, locationCount: 1, horizontal: true, ascending: true, entryLength: 1.5 * this.c2c, type: 'auto' },
            { id: "A1", locationCount: 5 },

            { id: "AB", entryx: 21 * this.c2c, entryy: 21.5 * this.c2c, locationCount: 2, horizontal: false, ascending: false, length: 4.5 * this.c2c, entryLength: 1.75 * this.c2c, type: 'non-working' },

            { id: "B1", entryx: 20 * this.c2c, entryy: 18 * this.c2c, locationCount: 6, horizontal: true, ascending: false, exitLength: 1.5 * this.c2c },

            { id: "BC1", entryx: 12 * this.c2c, entryy: 19 * this.c2c, locationCount: 1, horizontal: false, ascending: false, entryLength: this.c2c, type: 'non-working' },
            { id: "BC2", locationCount: 2 },
            { id: "BC3", locationCount: 1, type: 'buffer' },
            { id: "BC4", entryx: 13 * this.c2c, entryy: 13.5 * this.c2c, locationCount: 3, horizontal: true, ascending: false, length: 4 * this.c2c, entryLength: this.c2c, type: 'buffer' },
            { id: "BC5", entryx: 10 * this.c2c, entryy: 12.5 * this.c2c, locationCount: 3, horizontal: false, ascending: false, type: 'buffer' },
            { id: "BC6", entryx: 10 * this.c2c, entryy: 7.5 * this.c2c, locationCount: 1, horizontal: false, ascending: false, length: 1.5 * this.c2c, entryLength: 0.75 * this.c2c, type: 'buffer' },
            { id: "BC7", entryx: 10 * this.c2c, entryy: 4 * this.c2c, locationCount: 1, horizontal: false, ascending: false, length: 2.5 * this.c2c, entryLength: 0.65 * this.c2c, type: 'buffer' },
            { id: "BC8", entryx: 11 * this.c2c, entryy: 2.5 * this.c2c, locationCount: 2, horizontal: true, ascending: true, length: 3 * this.c2c, c2c: 1.5 * this.c2c, type: 'buffer' },
            { id: "BC9", entryx: 15 * this.c2c, entryy: 1.5 * this.c2c, locationCount: 1, horizontal: false, ascending: true, length: 4.5 * this.c2c, entryLength: 2.25 * this.c2c, type: 'buffer' },

            { id: "C1", entryx: 14 * this.c2c, entryy: 5 * this.c2c, locationCount: 1, horizontal: true, ascending: false, type: 'buffer' },
            { id: "C2", locationCount: 1, type: 'non-working' },
            { id: "C3", locationCount: 9 },

            { id: "CD", entryx: 2 * this.c2c, entryy: 4 * this.c2c, locationCount: 2, horizontal: false, ascending: true, length: 5.5 * this.c2c, c2c: 1.5 * this.c2c, entryLength: 2 * this.c2c },

            { id: "D1", entryx: 3 * this.c2c, entryy: 8.5 * this.c2c, locationCount: 8, horizontal: true, ascending: true },
            { id: "D2", locationCount: 1, type: 'non-working' },
            { id: "D3", locationCount: 2, type: 'buffer' },

            { id: "DE", entryx: 15 * this.c2c, entryy: 7.5 * this.c2c, locationCount: 3, horizontal: false, ascending: true, length: 8.5 * this.c2c, entryLength: 2.75 * this.c2c, c2c: 1.5 * this.c2c, type: 'buffer' },

            { id: "E1", entryx: 16 * this.c2c, entryy: 15 * this.c2c, locationCount: 5, horizontal: true, ascending: true, entryLength: this.c2c, type: 'buffer' },
            { id: "E2", locationCount: 1, type: 'non-working' },
            { id: "E3", locationCount: 5 },
            { id: "E4", locationCount: 1, type: 'inspection' },
            { id: "E5", locationCount: 11 },
            { id: "E6", locationCount: 1, type: 'non-working' },
            { id: "E7", locationCount: 2, type: 'buffer' },
            { id: "E8", locationCount: 1, type: 'auto' },
            { id: "E9", locationCount: 1, type: 'non-working' },
            { id: "E10", locationCount: 16 },
            { id: "E11", locationCount: 1, type: 'inspection' },
            { id: "E12", locationCount: 1, type: 'non-working' },
            { id: "E13", locationCount: 1, length: 2 * this.c2c, entryLength: this.c2c, type: 'buffer' },

            { id: "EF1", entryx: 63.5 * this.c2c, entryy: 14 * this.c2c, locations: [{ id: 105 }, { id: 106 }, { id: 107 }, { id: 108 }], horizontal: false, ascending: false, type: 'buffer' },
            { id: "EF2", entryx: 63.5 * this.c2c, entryy: 8 * this.c2c, locationCount: 4, type: 'buffer' },

            { id: "F1", entryx: 64.5 * this.c2c, entryy: 3 * this.c2c, locationCount: 1, horizontal: true, ascending: false, length: 2 * this.c2c, entryLength: 1 * this.c2c, type: 'buffer' },
            { id: "F2", locationCount: 1, type: 'auto' },
            { id: "F3", locationCount: 1, type: 'non-working' },
            { id: "F4", locationCount: 10 },
            { id: "F5", locationCount: 1, type: 'auto' },
            { id: "F6", locationCount: 1, type: 'non-working' },
            { id: "F7", locationCount: 7 },
            { id: "F8", locationCount: 1, type: 'inspection' },
            { id: "F9", locationCount: 2, type: 'buffer' },
            { id: "F10", locationCount: 7 },
            { id: "F11", locationCount: 1, type: 'semi-auto' },
            { id: "F12", locationCount: 7 },
            { id: "F13", locationCount: 2, type: 'non-working' },
            { id: "F14", locationCount: 2 },
            { id: "F15", locationCount: 1, type: 'non-working' },

            { id: "FG1", entryx: 17.5 * this.c2c, entryy: 2 * this.c2c, locations: [{ id: 157.1, code: 0 }, { id: 157.2, code: 0 }], horizontal: false, ascending: true, length: 3 * this.c2c, entryLength: 1.5 * this.c2c, type: 'buffer' },
            { id: "FG2", locationCount: 4, length: 5 * this.c2c, type: 'buffer' },

            { id: "G1", entryx: 18.5 * this.c2c, entryy: 9 * this.c2c, locationCount: 1, horizontal: true, ascending: true, type: 'non-working' },
            { id: "G2", locationCount: 2 },
            { id: "G3", locationCount: 2, type: 'non-working' },
            { id: "G4", locationCount: 2 },
            { id: "G5", locationCount: 1, type: 'inspection' },
            { id: "G6", locationCount: 7 },
            { id: "G7", locationCount: 1, type: 'inspection' },
            { id: "G8", locationCount: 5 },
            { id: "G9", locationCount: 1, type: 'non-working' },
            { id: "G10", locationCount: 2, type: 'buffer' },
            { id: "G11", locationCount: 1, type: 'auto' },
            { id: "G12", locationCount: 1, type: 'non-working' },
            { id: "G13", locationCount: 5 },
            { id: "G14", locationCount: 1, type: 'non-working' },
            { id: "G15", locationCount: 1, type: 'auto' },
            { id: "G16", locationCount: 4 },
            { id: "G17", locationCount: 1, type: 'non-working' },
            { id: "G18", locationCount: 2, type: 'buffer' },
            { id: "G19", locationCount: 5 },
            { id: "G20", locationCount: 2, type: 'non-working' },
            { id: "G21", locationCount: 1, length: 2.5 * this.c2c, type: 'buffer' },

            { id: "GH", entryx: 67 * this.c2c, entryy: 10 * this.c2c, locationCount: 6, horizontal: false, ascending: true, length: 7 * this.c2c, entryLength: 1.25 * this.c2c, type: 'buffer' },

            { id: "H1", entryx: 68 * this.c2c, entryy: 18 * this.c2c, locationCount: 1, horizontal: true, ascending: false, entryLength: 2 * this.c2c, type: 'buffer' },
            { id: "H2U", entryx: 65.5 * this.c2c, entryy: 17.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'buffer' },
            { id: "H2D", entryx: 65.5 * this.c2c, entryy: 18.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'non-working' },
            { id: "H3", entryx: 64.5 * this.c2c, entryy: 18 * this.c2c, locationCount: 18 },
            { id: "H4", locationCount: 3, type: 'inspection' },
            { id: "H5", locationCount: 4 },
            { id: "H6", locationCount: 1, type: 'inspection' },
            { id: "H7", locationCount: 1, type: 'buffer' },

            { id: "I1", entryx: 36 * this.c2c, entryy: 18 * this.c2c, locations: [{ id: 246 }, { id: 248 }, { id: 250 }], horizontal: true, ascending: false, type: 'non-working' },
            { id: "I2", locations: [{ id: 252 }, { id: 254 }, { id: 256 }, { id: 258 }, { id: 260 }, { id: 262 }], end: true, exitLength: this.c2c, type: 'inspection' },

            { id: "HJ1", entryx: 38 * this.c2c, entryy: 18 * this.c2c, locationCount: 0, horizontal: true, ascending: false, width: this.c2c, landMark: false },
            { id: "HJ2", entryx: 36.7 * this.c2c, entryy: 18.6 * this.c2c, locationCount: 0, horizontal: false, ascending: true, width: this.c2c, landMark: false },

            { id: "J1", entryx: 36 * this.c2c, entryy: 20.5 * this.c2c, locations: [{ id: 247 }, { id: 249 }, { id: 251 }], horizontal: true, ascending: false, type: 'non-working' },
            { id: "J2", locations: [{ id: 253 }, { id: 255 }, { id: 257 }, { id: 259 }, { id: 261 }, { id: 263 }], exitLength: this.c2c, type: 'inspection' },
        ];
        return config;
    }

    protected getLandMarks(): any[] {
        return [];
    }

    protected getLabels(): any[] {
        let firstSection: any = this.sections[0];
        let config: any[] = [
            { text: "AF ON", x: firstSection.entryx - 3.5 * this.c2c, y: firstSection.entryy + 0.1 * this.c2c },
            { text: "AF OFF", x: 25 * this.c2c, y: 19.5 * this.c2c }
        ];
        return config;
    }

    protected getStations(): any[] {
        let stations: any[] = [
            { locationId: 68, side: Side.R },
            { locationId: 101, side: Side.L },
            { locationId: 159, side: Side.R },
            { locationId: 238, side: Side.L },
            { locationId: 258, side: Side.L },
            { locationId: 259, side: Side.L }
        ];
        return stations;
    }

    protected getProcessAreas(): any[] {
        let config = [
            { id: "Door Off", location: 1, side: Side.L, offsetx: -10, offsety: 3 },
            { id: "Fuel Tank", location: 41, side: Side.L, offsetx: 6, offsety: 6 },
            { id: "Sunroof", location: 83, side: Side.L },
            { id: "S Chair", location: 92, side: Side.L },
            { id: "Carpet Load", location: 100, side: Side.L, offsetx: -25 },
            { id: "P Roof", location: 114, side: Side.L, offsetx: -10, offsety: 14 },
            { id: "Roof Liner", location: 116, side: Side.L },
            { id: "Carpet", location: 118, side: Side.L, offsety: 14 },
            { id: "HVAC", location: 119, side: Side.L },
            { id: "IP", location: 126, side: Side.L },
            { id: "MM", location: 145, side: Side.L },
            { id: "Exhaust", location: 168, side: Side.L },
            { id: "Brake Fill", location: 178, side: Side.L },
            { id: "Rr Bpr", location: 182, side: Side.L },
            { id: "Glass", location: 186, side: Side.L },
            { id: "Qtr Glass", location: 189, side: Side.L },
            { id: "3 Seat", location: 194, side: Side.L, offsetx: -5 },
            { id: "Battery", location: 196, side: Side.L },
            { id: "Seats", location: 198, side: Side.L },
            { id: "Fr Bpr", location: 202, side: Side.L, offsetx: 10 },
            { id: "Tires", location: 205, side: Side.L, offsetx: -6 },
            { id: "Rf Rail", location: 219, side: Side.L },
            { id: "Door On", location: 222, side: Side.L },
            { id: "S Chair", location: 224, side: Side.L, offsetx: -6 },
            { id: "Immobi", location: 230, side: Side.L },
            { id: "Pre-Vac", location: 231, side: Side.L, offsety: 14 },
            { id: "Rad Fill", location: 233, side: Side.L },
            { id: "AAI1", location: 237, side: Side.L },
            { id: "Gas/HAC Fill", location: 242, side: Side.L, offsetx: 12 },
        ];
        return config;
    }
    protected getTeamLeaderAreas(): any[] {
        let config = [
            { id: "WT01", location: 1, side: Side.R, offsetx: -10 },
            { id: "WT02", location: 9, side: Side.R, offsetx: -10, offsety: 2 },
            { id: "WT03", location: 32, side: Side.R, offsetx: -10, offsety: 15 },
            { id: "WT04", location: 63, side: Side.R },
            { id: "WT05", location: 71, side: Side.R },
            { id: "WT06", location: 85, side: Side.R },
            { id: "WT07", location: 89, side: Side.R },
            { id: "WT08", location: 94, side: Side.R },
            { id: "IN01", location: 116, side: Side.R },
            { id: "IN02", location: 120, side: Side.R },
            { id: "IN03", location: 127, side: Side.R },
            { id: "UB01", location: 138, side: Side.R },
            { id: "UB02", location: 146, side: Side.R },
            { id: "UB03", location: 167, side: Side.R },
            { id: "UB04", location: 173, side: Side.R },
            { id: "EX01", location: 186, side: Side.R },
            { id: "EX02", location: 195, side: Side.R },
            { id: "EX03", location: 202, side: Side.R },
            { id: "EX04", location: 219, side: Side.R },
            { id: "EX05", location: 225, side: Side.R },
            { id: "EX06", location: 231, side: Side.R, offsetx: 12 },
        ];
        return config;
    }
}