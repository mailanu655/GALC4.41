import { LineConfig } from "../line-config/line-config";
import { Side } from "../line/line-model";

export class ElpConfig extends LineConfig {

    protected set() {
        this.id = 'elp';
        this.c2c = 25.5;
    }

    protected init() {
        super.init();
        this.startLength = 0.75 * this.c2c;
    }

    protected getAxis(): any[] {
        let config: any[] = [
            { x: 54, y: 12, step: 53, values: this.getXAxisValues() },
            { x: 54, y: 541, step: 53, values: this.getXAxisValues() },
            { x: 10, y: 7 * this.c2c, step: 64, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 7 * this.c2c, step: 64, values: this.getYAxisValues(), horizontal: false }
        ];
        return config;
    }

    protected getXAxisValues(): string[] {
        let config: string[] = [];
        for (let i = 2; i <= 31; i++) {
            config.push(i.toString());
        }
        return config;
    }
    protected getYAxisValues(): string[] {
        return ['C', 'D', 'F', 'G', 'H', 'I/J'];
    }

    protected getSections(): any[] {
        let config: any[] = [
            { id: "AFON", entryx: 52 * this.c2c, entryy: 11 * this.c2c, locationCount: 2, horizontal: false, ascending: false, entryLength: this.c2c },
            { id: "A1", locationCount: 1, exitLength: 2 * this.c2c, type: 'buffer' },

            { id: "B1", entryx: 51 * this.c2c, entryy: 7 * this.c2c, locations: [{ id: 3.1, code: 0 }], horizontal: true, ascending: false, entryLength: this.c2c, length: 2.5 * this.c2c, type: 'buffer' },
            { id: "B2", locations: [{ id: 3.2, code: 0 }], type: 'buffer' },
            { id: "B3U", entryx: 47.5 * this.c2c, entryy: 6.5 * this.c2c, locations: [{ id: 3.3, code: 0 }], width: this.c2c, type: 'buffer', connect: false },
            { id: "B3D", entryx: 47.5 * this.c2c, entryy: 7.5 * this.c2c, locationCount: 1, width: this.c2c, type: 'non-working', connect: false },
            { id: "B4", entryx: 46.5 * this.c2c, entryy: 7 * this.c2c, locationCount: 4, type: 'non-working' },
            { id: "B5", locationCount: 27 },
            { id: "B6", locationCount: 3, type: 'non-working' },

            { id: "BC", entryx: 11.5 * this.c2c, entryy: 8 * this.c2c, locationCount: 0, horizontal: false, ascending: false, length: 4.5 * this.c2c, type: 'buffer' },

            { id: "C1", entryx: 12.5 * this.c2c, entryy: 4.5 * this.c2c, locationCount: 3, horizontal: true, ascending: true, type: 'buffer' },
            { id: "C2", locationCount: 2, type: 'non-working' },
            { id: "C3", locationCount: 18 },
            { id: "C4", locationCount: 1, type: 'inspection' },
            { id: "C5", locationCount: 4 },
            { id: "C6", locationCount: 2, type: 'non-working' },
            { id: "C7", locationCount: 2, type: 'buffer' },

            { id: "CD", entryx: 45.5 * this.c2c, entryy: 5.5 * this.c2c, locationCount: 1, horizontal: false, ascending: false, entryLength: 2.25 * this.c2c, length: 4.5 * this.c2c, type: 'buffer' },

            { id: "D1", entryx: 44.5 * this.c2c, entryy: 2 * this.c2c, locationCount: 2, horizontal: true, ascending: false, type: 'buffer' },
            { id: "D2", locationCount: 1, type: 'non-working' },
            { id: "D3", locationCount: 24 },
            { id: "D4", locationCount: 2, type: 'non-working' },
            { id: "D5", locationCount: 3, length: 6 * this.c2c, type: 'buffer' },

            { id: "DE", entryx: 8.5 * this.c2c, entryy: 1 * this.c2c, locationCount: 0, horizontal: false, ascending: true, length: 7 * this.c2c, type: 'buffer' },

            { id: "E1", entryx: 7.5 * this.c2c, entryy: 7 * this.c2c, locationCount: 3, horizontal: true, ascending: false, type: 'buffer' },

            { id: "EF", entryx: 3.5 * this.c2c, entryy: 6 * this.c2c, locationCount: 1, horizontal: false, ascending: true, entryLength: 2.25 * this.c2c, length: 4.5 * this.c2c, type: 'buffer' },

            { id: "F1", entryx: 4.5 * this.c2c, entryy: 9.5 * this.c2c, locationCount: 1, horizontal: true, ascending: true, type: 'buffer' },
            { id: "F2", locationCount: 3, type: 'non-working' },
            { id: "F3", locationCount: 23 },
            { id: "F4", locationCount: 1, type: 'inspection' },
            { id: "F5", locationCount: 4 },
            { id: "F6", locationCount: 1, c2c: 2 * this.c2c, type: 'non-working' },
            { id: "F7", locationCount: 1, entryLength: 1.5 * this.c2c, length: 3 * this.c2c, type: 'semi-auto' },
            { id: "F8", locationCount: 3, type: 'working' },
            { id: "F9", locationCount: 2, type: 'non-working' },

            { id: "FG1", entryx: 45.5 * this.c2c, entryy: 9.5 * this.c2c, locationCount: 3, horizontal: true, ascending: true, type: 'buffer' },
            { id: "FG2", entryx: 49.5 * this.c2c, entryy: 8.5 * this.c2c, locationCount: 1, horizontal: false, ascending: true, entryLength: 2.25 * this.c2c, length: 4.5 * this.c2c, type: 'buffer' },
            { id: "FG3", entryx: 48.5 * this.c2c, entryy: 12 * this.c2c, locationCount: 1, horizontal: true, ascending: false, type: 'buffer' },

            { id: "G1", entryx: 47.5 * this.c2c, entryy: 12 * this.c2c, locationCount: 5, horizontal: true, ascending: false, type: 'non-working' },
            { id: "G2", locationCount: 29 },
            { id: "G3", locationCount: 1, type: 'inspection' },
            { id: "G4", locationCount: 2 },
            { id: "G5", locationCount: 2, type: 'non-working' },
            { id: "G6", locationCount: 4 },

            { id: "GH1", entryx: 4.5 * this.c2c, entryy: 12 * this.c2c, locationCount: 1, horizontal: true, ascending: false, type: 'buffer' },
            { id: "GH2", entryx: 2.5 * this.c2c, entryy: 11 * this.c2c, locationCount: 1, horizontal: false, ascending: true, entryLength: 2.25 * this.c2c, length: 4.5 * this.c2c, type: 'buffer' },
            { id: "GH3", entryx: 3.5 * this.c2c, entryy: 14.5 * this.c2c, locationCount: 1, horizontal: true, ascending: true, type: 'buffer' },

            { id: "H1", entryx: 4.5 * this.c2c, entryy: 14.5 * this.c2c, locationCount: 4, horizontal: true, ascending: true },
            { id: "H2", locationCount: 2, type: 'non-working' },
            { id: "H3", locationCount: 47 },
            { id: "H4", locationCount: 4, type: 'non-working' },

            { id: "HI", entryx: 62.5 * this.c2c, entryy: 13.5 * this.c2c, locationCount: 2, horizontal: false, ascending: true, entryLength: 1.75 * this.c2c, length: 4.5 * this.c2c, type: 'buffer' },

            { id: "I1", entryx: 61.5 * this.c2c, entryy: 17 * this.c2c, locationCount: 4, horizontal: true, ascending: false, type: 'non-working' },
            { id: "I2", locationCount: 7 },
            { id: "I3", locationCount: 1, type: 'non-working' },
            { id: "I4", locations: [{ id: 268.1, code: 0 }, { id: 268.2, code: 0 }], length: 2 * this.c2c, type: 'buffer' },
            { id: "I5D", entryx: 47.5 * this.c2c, entryy: 17.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'non-working' },
            { id: "I5U", entryx: 47.5 * this.c2c, entryy: 16.5 * this.c2c, locations: [{ id: 269.1, code: 0 }], width: this.c2c, connect: false, type: 'buffer' },
            { id: "I6", entryx: 46.5 * this.c2c, entryy: 17 * this.c2c, locationCount: 1, type: 'auto' },
            { id: "I7D", entryx: 45.5 * this.c2c, entryy: 17.5 * this.c2c, locationCount: 1, width: this.c2c, type: 'non-working', connect: false },
            { id: "I7U", entryx: 45.5 * this.c2c, entryy: 16.5 * this.c2c, locationCount: 1, width: this.c2c, type: 'buffer', connect: false },
            { id: "I8", entryx: 44.5 * this.c2c, entryy: 17 * this.c2c, locationCount: 1, type: 'buffer' },
            { id: "I9", locationCount: 1, type: 'non-working' },
            { id: "I10", locationCount: 24 },
            { id: "I11", locationCount: 2, type: 'inspection' },
            { id: "I12", locationCount: 3 },
            { id: "I13", locationCount: 1, type: 'non-working' },
            { id: "I14", locationCount: 2, entryLength: 1.5 * this.c2c, length: 3.5 * this.c2c, type: 'buffer' },

            { id: "IJ1", entryx: 8 * this.c2c, entryy: 16 * this.c2c, locationCount: 1, horizontal: false, ascending: true, entryLength: 2.25 * this.c2c, length: 4.5 * this.c2c, type: 'buffer' },
            { id: "J12", entryx: 9 * this.c2c, entryy: 19.5 * this.c2c, locationCount: 9, horizontal: true, ascending: true, entryLength: this.c2c, type: 'buffer' },
            { id: "IJ3", entryx: 18.5 * this.c2c, entryy: 19.5 * this.c2c, locationCount: 1, horizontal: true, ascending: true, type: 'non-working' },

            { id: "J1", entryx: 19.5 * this.c2c, entryy: 19.5 * this.c2c, locationCount: 14, horizontal: true, ascending: true },
            { id: "J2", locationCount: 5, type: 'inspection' },
            { id: "J3", locationCount: 12, type: 'non-working' },
            { id: "J4", locationCount: 1, exitLength: 2 * this.exitLength, type: 'inspection' },

        ];
        return config;
    }

    protected getLandMarks(): any[] {
        return [];
    }

    protected getLabels(): any[] {
        let firstSection: any = this.sections[0];
        let config: any[] = [
            { text: "AF ON", x: firstSection.entryx + 2 * this.c2c, y: firstSection.entryy + 1.2 * this.c2c },
            { text: "AF OFF", x: 55.5 * this.c2c, y: 19.6 * this.c2c }
        ];
        return config;
    }

    protected getStations(): any[] {
        let stations: any[] = [
            { locationId: 1, side: Side.L, offsetx: 5 },
            { locationId: 1, side: Side.R, offsetx: -5 },
            { locationId: 302, side: Side.L, offsety: -2 },
            { locationId: 302, side: Side.R, offsety: 2 },
            { locationId: 349, side: Side.L, offsety: 5 },
            { locationId: 349, side: Side.R, offsety: -5 }
        ];
        return stations;
    }

    protected getProcessAreas(): any[] {
        let config = [
            { id: "HEADLINER", location: 77, side: Side.L, offsety: -15 },
            { id: "IPA ARM", location: 95, side: Side.L, offsety: -15 },
            { id: "SMURF", location: 141, side: Side.L, offsety: 15 },
            { id: "RECOMP CART", location: 156, side: Side.L, offsety: -15 },
            { id: "EXHAUST", location: 183, side: Side.L, offsety: -15 },
            { id: "SPARE TIRE", location: 199, side: Side.L, offsetx: 12, offsety: 15 },
            { id: "RR SEATS", location: 222, side: Side.L, offsety: 15 },
            { id: "FEM", location: 231, side: Side.L, offsety: 15 },
            { id: "BATTERY", location: 241, side: Side.L, offsety: 15 },
            { id: "GLASS", location: 270, side: Side.L, offsety: -2 },
            { id: "BRAKE FILL", location: 285, side: Side.L, offsety: -15 },
            { id: "DOORS ON", location: 292, side: Side.L, offsety: -15 },
            { id: "TIRE INST", location: 302, side: Side.L, offsety: -15 },
            { id: "GAS FILL", location: 322, side: Side.L, offsety: 15 },
        ];
        return config;
    }
    protected getTeamLeaderAreas(): any[] {
        let config = [
            { id: "WT02", location: 10, side: Side.R, offsety: 15 },
            { id: "WT05", location: 34, side: Side.R, offsety: 15 },
            { id: "WT06", location: 45, side: Side.R, offsety: -15 },
            { id: "WT09", location: 65, side: Side.R, offsety: -15 },
            { id: "IN01", location: 77, side: Side.R, offsety: 15 },
            { id: "IN03", location: 97, side: Side.R, offsety: 15 },
            { id: "IN04", location: 115, side: Side.R, offsety: -15 },
            { id: "UB01", location: 137, side: Side.R, offsety: -15 },
            { id: "UB02", location: 158, side: Side.R, offsety: 15 },
            { id: "UB04", location: 184, side: Side.R, offsety: 15 },
            { id: "EX01", location: 192, side: Side.R, offsety: 15 },
            { id: "UB04", location: 206, side: Side.R, offsety: -15 },
            { id: "EX02", location: 210, side: Side.R, offsety: -15 },
            { id: "EX05", location: 242, side: Side.R, offsety: -15 },
            { id: "BL01", location: 247, side: Side.R, offsety: -15 },
            { id: "UB05", location: 263, side: Side.R, offsety: 15 },
            { id: "EX06", location: 276, side: Side.R, offsety: 15 },
            { id: "EX08", location: 302, side: Side.R, offsetx: 3, offsety: 15 },
            { id: "EX09", location: 321, side: Side.R, offsety: -15 },
        ];
        return config;
    }
}