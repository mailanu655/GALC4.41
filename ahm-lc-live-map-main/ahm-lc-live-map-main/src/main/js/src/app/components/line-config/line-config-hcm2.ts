import { LineConfig } from "../line-config/line-config";
import { Side } from "../line/line-model";

export class Hcm2Config extends LineConfig {

    protected set() {
        this.id = 'hcm2';
        this.c2c = 22.8
    }

    protected getAxis(): any[] {
        let config: any[] = [
            { x: 12 + this.c2c, y: 12, step: 2 * this.c2c, values: this.getXAxisValues() },
            { x: 12 + this.c2c, y: 541, step: 2 * this.c2c, values: this.getXAxisValues() },
            { x: 10, y: 3 * this.c2c, step: 5 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 10, y: 17 * this.c2c, step: 4 * this.c2c, values: this.getYAxisValues2(), horizontal: false },
            { x: 1650, y: 3 * this.c2c, step: 5 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 17 * this.c2c, step: 4 * this.c2c, values: this.getYAxisValues2(), horizontal: false }
        ];
        return config;
    }

    protected getXAxisValues(): string[] {
        let config: string[] = [];
        for (let i = 2; i <= 71; i = i + 2) {
            config.push(i.toString());
        }
        return config;
    }
    protected getYAxisValues(): string[] {
        return ['A', 'B', 'C'];
    }

    protected getYAxisValues2(): string[] {
        return ['D', 'E'];
    }


    protected getSections(): any[] {
        let config: any[] = [
            { id: "AFON", entryx: 44.5 * this.c2c, entryy: 17 * this.c2c, locationCount: 1, horizontal: true, ascending: true, entryLength: this.c2c },
            { id: "A1", locationCount: 4, length: 4.5 * this.c2c, type: 'non-working' },

            { id: "AB1", entryx: 51.5 * this.c2c, entryy: 18 * this.c2c, locationCount: 1, locations: [{ id: 5.1, code: 0 }], horizontal: false, ascending: false, length: 4 * this.c2c, entryLength: 3 * this.c2c, type: 'buffer' },

            { id: "B1", entryx: 52.5 * this.c2c, entryy: 13 * this.c2c, locationCount: 1, horizontal: true, ascending: false, length: 2.5 * this.c2c, entryLength: 2 * this.c2c, type: 'buffer' },
            { id: "B2", locationCount: 1, type: 'non-working' },
            { id: "B3", locationCount: 5 },
            { id: "B4", locationCount: 1, type: 'non-working' },
            { id: "B5", locationCount: 12 },
            { id: "B6", locationCount: 1, type: 'non-working' },
            { id: "B7", locationCount: 2, type: 'buffer' },
            { id: "B8", locationCount: 21 },
            { id: "B9", locationCount: 1, type: 'non-working' },
            { id: "B10D", entryx: 6 * this.c2c, entryy: 13.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'non-working' },
            { id: "B10U", entryx: 6 * this.c2c, entryy: 12.5 * this.c2c, locations: [{ id: 51.1, code: 0 }], width: this.c2c, connect: false, type: 'buffer' },
            { id: "B11", entryx: 5 * this.c2c, entryy: 13 * this.c2c, locations: [{ id: 51.2, code: 0 }], type: 'buffer' },

            { id: "BC1", entryx: 3 * this.c2c, entryy: 14 * this.c2c, locations: [{ id: 51.3, code: 0 }], horizontal: false, ascending: false, length: 4 * this.c2c, entryLength: 2.5 * this.c2c, type: 'buffer' },
            { id: "BC2", locations: [{ id: 51.4, code: 0 }], length: 4 * this.c2c, entryLength: 2 * this.c2c, type: 'buffer' },
            { id: "BC3", locationCount: 1, length: 4 * this.c2c, entryLength: 1.5 * this.c2c, type: 'buffer' },

            { id: "C1", entryx: 4 * this.c2c, entryy: 3 * this.c2c, locationCount: 1, horizontal: true, ascending: true, type: 'buffer' },
            { id: "C2D", entryx: 5 * this.c2c, entryy: 2.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'buffer' },
            { id: "C2U", entryx: 5 * this.c2c, entryy: 3.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'non-working' },
            { id: "C3", entryx: 6 * this.c2c, entryy: 3 * this.c2c, locationCount: 20 },
            { id: "C4", locationCount: 1, type: 'non-working' },
            { id: "C5", locationCount: 2, type: 'buffer' },
            { id: "C6", locationCount: 3 },
            { id: "C7", locationCount: 1, type: 'non-working' },
            { id: "C8", locationCount: 1, length: 2 * this.c2c, entryLength: this.c2c, type: 'semi-auto' },
            { id: "C9", locationCount: 1, type: 'non-working' },
            { id: "C10", locationCount: 26 },
            { id: "C11", locationCount: 1, type: 'inspection' },
            { id: "C12", locations: [{ id: 111.1, code: 0 }], length: 3 * this.c2c, entryLength: this.c2c, type: 'non-working' },
            { id: "C13", locationCount: 1, length: 2 * this.c2c, entryLength: this.c2c, type: 'buffer' },
            { id: "C14", locationCount: 1, type: 'buffer' },

            { id: "CD", entryx: 70 * this.c2c, entryy: 2 * this.c2c, locationCount: 1, horizontal: false, ascending: true, length: 7 * this.c2c, entryLength: 3.5 * this.c2c, type: 'buffer' },

            { id: "D1", entryx: 69 * this.c2c, entryy: 8 * this.c2c, locationCount: 2, horizontal: true, ascending: false, type: 'buffer' },
            { id: "D2", locations: [{ id: 116.1, code: 0 }, { id: 116.2, code: 0 }, { id: 116.3, code: 0 }], type: 'non-working' },
            { id: "D3", locationCount: 18 },
            { id: "D4", locationCount: 1, type: 'semi-auto' },
            { id: "D5", locationCount: 3 },
            { id: "D6", locationCount: 1, type: 'non-working' },
            { id: "D7", locationCount: 2, type: 'buffer' },
            { id: "D8", locationCount: 13 },
            { id: "D9", locationCount: 2, type: 'non-working' },
            { id: "D10", locationCount: 1, type: 'auto' },
            { id: "D11", locationCount: 1, type: 'non-working' },
            { id: "D12", locationCount: 10 },
            { id: "D13", entryx: 12 * this.c2c, entryy: 8 * this.c2c, locationCount: 4, type: 'inspection' },
            { id: "D14D", entryx: 8 * this.c2c, entryy: 8.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'non-working' },
            { id: "D14U", entryx: 8 * this.c2c, entryy: 7.5 * this.c2c, locations: [{ id: 173.1, code: 0 }], width: this.c2c, connect: false, type: 'buffer' },
            { id: "D15", entryx: 7 * this.c2c, entryy: 8 * this.c2c, locations: [{ id: 173.2, code: 0 }], connect: false, type: 'buffer' },

            { id: "DE1", entryx: 5.2 * this.c2c, entryy: 7 * this.c2c, locationCount: 0, horizontal: false, ascending: true, length: 4.5 * this.c2c, width: 1.5 * this.c2c, type: 'buffer' },
            { id: "DE2", entryx: 6 * this.c2c, entryy: 10.5 * this.c2c, locations: [{ id: 173.3, code: 0 }, { id: 173.4, code: 0 }], horizontal: true, ascending: true, type: 'buffer' },

            { id: "DE4", entryx: 9 * this.c2c, entryy: 9.5 * this.c2c, locations: [{ id: 173.5, code: 0 }], horizontal: false, ascending: true, length: 2.5 * this.c2c, entryLength: 2 * this.c2c, type: 'buffer' },
            { id: "DE5", entryx: 9 * this.c2c, entryy: 14 * this.c2c, locations: [{ id: 173.6, code: 0 }, { id: 173.7, code: 0 }], length: 5 * this.c2c, entryLength: 1.5 * this.c2c, c2c: 2 * this.c2c, type: 'buffer' },
            { id: "DE6", entryx: 9 * this.c2c, entryy: 19 * this.c2c, locationCount: 1, length: 3 * this.c2c, entryLength: this.c2c, type: 'buffer' },

            { id: "E1", entryx: 10 * this.c2c, entryy: 21 * this.c2c, locationCount: 1, horizontal: true, ascending: true, type: 'buffer' },
            { id: "E2U", entryx: 11 * this.c2c, entryy: 20.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'buffer' },
            { id: "E2D", entryx: 11 * this.c2c, entryy: 21.5 * this.c2c, locationCount: 1, width: this.c2c, connect: false, type: 'non-working' },
            { id: "E3", entryx: 12 * this.c2c, entryy: 21 * this.c2c, locationCount: 14 },
            { id: "E4", locationCount: 11, type: 'inspection' },
            { id: "E5", locations: [{ id: 202.1, code: 0 }], exitLength: this.c2c, type: 'buffer' },
            { id: "E6", locationCount: 0, landMark: false },
            { id: "E7", locationCount: 5, entryLength: this.c2c, type: 'non-working' },
            { id: "E8", locationCount: 5, type: 'inspection' },
            { id: "E9", locationCount: 1, exitLength: this.c2c, type: 'non-working' },
        ];
        return config;
    }

    protected getLandMarks(): any[] {
        return [];
    }

    protected getLabels(): any[] {
        let firstSection: any = this.sections[0];
        let config: any[] = [
            { text: "AF ON", x: firstSection.entryx - 3.5 * this.c2c, y: firstSection.entryy + 0.05 * this.c2c },
            { text: "AF OFF", x: 34.0 * this.c2c, y: 19.5 * this.c2c }
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
            { id: "Fuel Tank", location: 16, side: Side.L },
            { id: "Door Off", location: 31, side: Side.L },
            { id: "Headliner", location: 48, side: Side.L, offsety: -15 },
            { id: "Carpet", location: 59, side: Side.L },
            { id: "HVAC", location: 61, side: Side.L },
            { id: "IP", location: 73, side: Side.L },
            { id: "Flex Mnt", location: 83, side: Side.L, offsetx: -8 },
            { id: "Rr Dmpr", location: 85, side: Side.L, offsetx: 16 },
            { id: "FEM", location: 105, side: Side.R },
            { id: "Bumper", location: 123, side: Side.L },
            { id: "Tires", location: 135, side: Side.L },
            { id: "Pre-Vac", location: 151, side: Side.L, offsetx: 10 },
            { id: "HFO", location: 153, side: Side.L },
            { id: "Glass", location: 157, side: Side.L },
            { id: "Brake Fill", location: 159, side: Side.L, offsetx: -10 },
            { id: "Rad Fill", location: 162, side: Side.L, offsetx: -4 },
            { id: "Qtr Glass", location: 165, side: Side.L },
            { id: "Roof Rail", location: 168, side: Side.L },
            { id: "Seats", location: 178, side: Side.L },
            { id: "Door On", location: 183, side: Side.L },
            { id: "Gas/HAC Fill", location: 189, side: Side.L },
        ];
        return config;
    }
    protected getTeamLeaderAreas(): any[] {
        let config = [
            { id: "WT01", location: 1, side: Side.R },
            { id: "WT02", location: 13, side: Side.R },
            { id: "WT03", location: 21, side: Side.R },
            { id: "DL1", location: 26, side: Side.L },
            { id: "WT04", location: 29, side: Side.R },
            { id: "WT05", location: 35, side: Side.R, offsetx: 10 },
            { id: "WT06", location: 42, side: Side.R, offsetx: 10 },
            { id: "TTL1", location: 50, side: Side.L },
            { id: "TTL2", location: 54, side: Side.L, offsety: 10 },
            { id: "IN01", location: 56, side: Side.R },
            { id: "IN02", location: 62, side: Side.R },
            { id: "IN03", location: 71, side: Side.R },
            { id: "DL2", location: 76, side: Side.R },
            { id: "UB01", location: 79, side: Side.R },
            { id: "UB02", location: 81, side: Side.R },
            { id: "UB03", location: 86, side: Side.R },
            { id: "UB04", location: 90, side: Side.R },
            { id: "UB05", location: 93, side: Side.R },
            { id: "UB06", location: 101, side: Side.R },
            { id: "EX01", location: 116, side: Side.R, offsetx: -12 },
            { id: "EX02", location: 127, side: Side.R },
            { id: "EX03", location: 133, side: Side.R },
            { id: "DL3", location: 139, side: Side.R },
            { id: "EX04", location: 142, side: Side.R },
            { id: "EX05", location: 150, side: Side.R, offsetx: 10 },
            { id: "EX06", location: 157, side: Side.R },
            { id: "FTL3", location: 172, side: Side.R, offsetx: -40 },
            { id: "FTL4", location: 175, side: Side.R, offsetx: -30 },
            { id: "EX07", location: 178, side: Side.R },
            { id: "EX08", location: 185, side: Side.R, offsetx: -15 },
            { id: "AAI1", location: 199, side: Side.R, offsetx: -10 },
        ];
        return config;
    }
}