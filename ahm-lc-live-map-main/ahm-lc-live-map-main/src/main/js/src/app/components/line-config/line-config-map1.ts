import { LineConfig } from "../line-config/line-config";
import { Side } from "../line/line-model";

export class Map1Config extends LineConfig {

    protected set() {
        this.id = 'map1';
    }

    protected getAxis(): any[] {
        let config: any[] = [
            { x: 12 + this.c2c, y: 12, step: 2 * this.c2c, values: this.getXAxisValuesTop() },
            { x: 12 + this.c2c, y: 541, step: 2 * this.c2c, values: this.getXAxisValuesBottom() },
            { x: 10, y: 4.75 * this.c2c, step: 4 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 10, y: 16.25 * this.c2c, step: 4 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 4.75 * this.c2c, step: 4 * this.c2c, values: this.getYAxisValues(), horizontal: false },
            { x: 1650, y: 16.25 * this.c2c, step: 4 * this.c2c, values: this.getYAxisValues(), horizontal: false }
        ];
        return config;
    }

    protected getXAxisValuesTop(): void {
        let config: string[] = [];
        for (let i = 2; i <= 67; i = i + 2) {
            config.push(i.toString());
        }
        //return config;
    }

    protected getXAxisValuesBottom(): void {
        let config: string[] = [];
        for (let i = 62; i <= 127; i = i + 2) {
            config.push(i.toString());
        }
        //return config;
    }

    protected getYAxisValues(): string[] {
        let config: string[] = ['', ''];
        return config;
    }

    protected getSections(): any[] {
        let config: any[] = [
            { id: "AFON", entryx: 8 * this.c2c, entryy: 1.75 * this.c2c, locationCount: 3, horizontal: true, ascending: true, width: 0.75 * this.sectionWidth, type: 'non-working' },
            { id: "A1", locationCount: 1, width: 0.75 * this.sectionWidth },
            { id: "A2", locationCount: 2, width: 0.75 * this.sectionWidth },
            { id: "A3", locationCount: 1, width: 0.75 * this.sectionWidth, type: 'non-working' },
            { id: "A4", locationCount: 2, width: 0.75 * this.sectionWidth },
            { id: "A5", locationCount: 2, width: 0.75 * this.sectionWidth, type: 'non-working' },
            { id: "A6", locationCount: 3, width: 0.75 * this.sectionWidth },
            { id: "AB1", locationCount: 11, width: 0.75 * this.sectionWidth, horizontal: true, ascending: true, connect: true, type: 'buffer'},

            { id: "CD1", entryx: 35 * this.c2c, entryy: 1.75 * this.c2c, locationCount: 0, horizontal: false, ascending: true, width: this.c2c, landMark: false },
            { id: "CD2", entryx: 34.5 * this.c2c, entryy: 3.25 * this.c2c, locationCount: 0, horizontal: true, ascending: false, length: 1 * this.c2c, width: this.c2c, landMark: false },

            { id: "AB2", entryx: 33.15 * this.c2c, entryy: 3.25 * this.c2c, locationCount: 2, width: 0.75 * this.sectionWidth, connect: false},
            { id: "AB3", locationCount: 1, width: 0.75 * this.sectionWidth },
            { id: "AB4", locationCount: 3, width: 0.75 * this.sectionWidth },
            { id: "AB5", locationCount: 1, width: 0.75 * this.sectionWidth },//Body Loader
            { id: "AB6", entryx: 25.25 * this.c2c, entryy: 2.75 * this.c2c, locationCount: 4, width: 0.75 * this.sectionWidth, horizontal: false, ascending: true, type: 'non-working' },
            { id: "AB7", entryx: 25.25 * this.c2c, entryy: 8.25 * this.c2c, locationCount: 3,  width: 0.75 * this.sectionWidth, horizontal: false, ascending: true, type: 'non-working' },
            { id: "AB8", entryx: 26 * this.c2c, entryy: 12 * this.c2c, locationCount: 9, horizontal: true, ascending: false, length: 9.5 * this.c2c, entryLength: 0.75 * this.c2c, width: 0.75 * this.sectionWidth, type: 'non-working' },
            { id: "AB10", locationCount: 4, width: 0.75 * this.sectionWidth, length: 4.25 * this.c2c, entryLength: 0.75 * this.c2c, },

            { id:"BA1", entryx: 11.5 * this.c2c, entryy: 11.25 * this.c2c, locationCount: 2, horizontal: false, ascending: true, width: 0.75 * this.sectionWidth,  length: 2.25 * this.c2c, entryLength: 0.75 * this.c2c, type: 'non-working'  },
            { id:"BA2", entryx: 12 * this.c2c, entryy: 14.25 * this.c2c, locationCount: 2, horizontal: true, ascending: false, width: 0.75 * this.sectionWidth, type: 'non-working' },
            { id:"BA3", locationCount: 8, horizontal: true, ascending: false, width: 0.75 * this.sectionWidth, length: 8.5 * this.c2c, entryLength: 0.75 * this.c2c, },

            { id: "BA5", entryx: 2.25 * this.c2c, entryy: 13.5 * this.c2c, locationCount: 4, length: 6.75 * this.c2c, entryLength: 3 * this.c2c, horizontal: false, ascending: false, type: 'non-working', width: 0.75 * this.sectionWidth },
             { id: "B1", entryx: 3 * this.c2c, entryy: 7.5 * this.c2c, locationCount: 3, horizontal: true, ascending: true, length: 3.5 * this.c2c, width: 0.75 * this.sectionWidth, type: 'non-working'},
             { id: "B2", locationCount: 4, length: 5 * this.c2c, entryLength: 0.75 * this.c2c, width: 0.75 * this.sectionWidth },
             { id: "B3", locationCount: 1, length: 1.5 * this.c2c, entryLength: 0.75 * this.c2c, width: 0.75 * this.sectionWidth, type: 'inspection' },
             { id: "B4", locationCount: 1, length: 1.5 * this.c2c, entryLength: 0.75 * this.c2c, width: 0.75 * this.sectionWidth, type: 'non-working' },

             { id: "B5", entryx: 15.25 * this.c2c, entryy: 6.75 * this.c2c, locationCount: 1, length: 4.5 * this.c2c, entryLength: 0.75 * this.c2c, width: 0.75 * this.sectionWidth,  horizontal: false }, //W3

             { id: "B6", entryx: 16 * this.c2c, entryy: 7.5 * this.c2c, locationCount: 2,  width: 0.75 * this.sectionWidth, type: 'non-working',  horizontal: true },
             { id: "B7", locationCount: 3,  width: 0.75 * this.sectionWidth },
             { id: "B8", locationCount: 1,  width: 0.75 * this.sectionWidth, type: 'non-working' },
             { id: "B9", locationCount: 2,  width: 0.75 * this.sectionWidth },
             { id: "B10", locationCount: 3,  width: 0.75 * this.sectionWidth },
             { id: "B11", locationCount: 1,  width: 0.75 * this.sectionWidth },
             { id: "B12", locationCount: 3,  width: 0.75 * this.sectionWidth },
             { id: "B13", locationCount: 4,  width: 0.75 * this.sectionWidth },
             { id: "B14", locationCount: 2,  width: 0.75 * this.sectionWidth },
             { id: "B15", locationCount: 1,  width: 0.75 * this.sectionWidth, type: 'non-working' },
             { id: "B16", locationCount: 1,  width: 0.75 * this.sectionWidth, type: 'inspection' },
             { id: "B17", locationCount: 2,  width: 0.75 * this.sectionWidth },
             { id: "B18", locationCount: 3,  width: 0.75 * this.sectionWidth, type: 'non-working' },
             { id: "B19", locationCount: 1,  width: 0.75 * this.sectionWidth },
             { id: "B20", locationCount: 4,  width: 0.75 * this.sectionWidth },
             { id: "B21", locationCount: 1,  width: 0.75 * this.sectionWidth },
             { id: "B22", locationCount: 4,  width: 0.75 * this.sectionWidth },
             { id: "B23", locationCount: 6,  width: 0.75 * this.sectionWidth },
             { id: "B24", locationCount: 3,  width: 0.75 * this.sectionWidth },
             { id: "B25", locationCount: 1,  width: 0.75 * this.sectionWidth, type: 'inspection' },

              { id: "CD3", entryx: 67 * this.c2c, entryy: 8.5 * this.c2c, locationCount: 0, horizontal: false, ascending: true, width: this.c2c, landMark: false },
              { id: "CD4", entryx: 64 * this.c2c, entryy: 9.25 * this.c2c, locationCount: 0, horizontal: true, ascending: false, length: 20 * this.c2c, width: this.c2c, landMark: false },
              { id: "CD5", entryx: 28.5 * this.c2c, entryy: 10 * this.c2c, locationCount: 0, horizontal: false, ascending: true, length: this.c2c, width: this.c2c, landMark: false },
              
              { id: "C1", entryx: 29.25 * this.c2c, entryy: 11 * this.c2c, horizontal: true, ascending: true, locationCount: 4, width: 0.75 * this.sectionWidth },
              { id: "C2", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "C3", locationCount: 3, width: 0.75 * this.sectionWidth },
              { id: "C4", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "C5", locationCount: 4, width: 0.75 * this.sectionWidth },
              { id: "C6", locationCount: 1, width: 0.75 * this.sectionWidth, type: 'inspection' },
              { id: "C7", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "C8", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "C9", locationCount: 4, width: 0.75 * this.sectionWidth },
              { id: "C10", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "C11", locationCount: 7, width: 0.75 * this.sectionWidth },
              { id: "C12", locationCount: 1, width: 0.75 * this.sectionWidth, type: 'non-working' },
              { id: "C13", locationCount: 1, width: 0.75 * this.sectionWidth, type: 'semi-auto' },
              { id: "C14", locationCount: 1, width: 0.75 * this.sectionWidth, type: 'non-working' },
              { id: "C15", locationCount: 3, width: 0.75 * this.sectionWidth },
              { id: "C16", locationCount: 2, width: 0.75 * this.sectionWidth },

              { id: "CD6", entryx: 67 * this.c2c, entryy: 12 * this.c2c, locationCount: 0, horizontal: false, ascending: true, width: this.c2c, landMark: false },
              { id: "CD7", entryx: 64 * this.c2c, entryy: 13 * this.c2c, locationCount: 0, horizontal: true, ascending: false, length: 40 * this.c2c, width: this.c2c, landMark: false },
              { id: "CD8", entryx: 28 * this.c2c, entryy: 13.75 * this.c2c, locationCount: 0, horizontal: false, ascending: true, length: this.c2c, width: this.c2c, landMark: false },

              { id: "D1", entryx: 28.5 * this.c2c, entryy: 14.75 * this.c2c, locationCount: 2, width: 0.75 * this.sectionWidth, horizontal: true, ascending: true },
              { id: "D2", locationCount: 5, width: 0.75 * this.sectionWidth },
              { id: "D3", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "D4", locationCount: 8, width: 0.75 * this.sectionWidth },
              { id: "D5", locationCount: 16, width: 0.75 * this.sectionWidth, type: 'buffer' },
              { id: "D6", locationCount: 3, width: 0.75 * this.sectionWidth },
              { id: "D7", locationCount: 3, width: 0.75 * this.sectionWidth },

              { id: "DE", entryx: 68.5 * this.c2c, entryy: 15 * this.c2c, locationCount: 0, horizontal: false, ascending: true, width: this.c2c, landMark: false },
              { id: "DE2", entryx: 67.5 * this.c2c, entryy: 17.5 * this.c2c, locationCount: 0, horizontal: true, ascending: false, length: 1 * this.c2c, width: this.c2c, landMark: false },

              { id: "E1",  entryx: 67 * this.c2c, entryy: 17.5 * this.c2c, locationCount: 9, width: 0.75 * this.sectionWidth, horizontal: true, ascending: false, type: 'non-working' },
              { id: "E2", locationCount: 1, width: 0.75 * this.sectionWidth, type: 'semi-auto' },
              { id: "E3", locationCount: 2, width: 0.75 * this.sectionWidth, type: 'non-working' },
              { id: "E4", locationCount: 6, width: 0.75 * this.sectionWidth },  
              { id: "E5", locationCount: 8, width: 0.75 * this.sectionWidth },
              { id: "E6", locationCount: 3, width: 0.75 * this.sectionWidth, type: 'non-working' },
              { id: "E7", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "E8", locationCount: 2, width: 0.75 * this.sectionWidth },
              { id: "E9", locationCount: 9, width: 0.75 * this.sectionWidth, type: 'buffer' },
              { id: "E10", locationCount: 2, width: 0.75 * this.sectionWidth },
              { id: "F1", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "F2", locationCount: 2, width: 0.75 * this.sectionWidth },
              { id: "F3", locationCount: 5, width: 0.75 * this.sectionWidth },
              { id: "F4", locationCount: 2, width: 0.75 * this.sectionWidth },
              { id: "F5", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "F6", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "F7", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "F8",  locationCount: 2, entryLength: 0.75 * this.c2c, width: 0.75 * this.sectionWidth },
              { id: "F9", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "F10", locationCount: 2, width: 0.75 * this.sectionWidth },
              { id: "F11", locationCount: 1, width: 0.75 * this.sectionWidth },

              { id: "FG1", entryx: 2 * this.c2c, entryy: 17.5 * this.c2c, locationCount: 0, horizontal: false, ascending: true, width: this.c2c, landMark: false },
              { id: "FG2", entryx: 50 * this.c2c, entryy: 19 * this.c2c, locationCount: 0, horizontal: true, ascending: true, length: 1 * this.c2c, width: this.c2c, landMark: false },
              { id: "FG3", entryx: 67 * this.c2c, entryy: 19 * this.c2c, locationCount: 0, horizontal: false, ascending: true, length: this.c2c, width: this.c2c, landMark: false },
              { id: "FG4", entryx: 67 * this.c2c, entryy: 20 * this.c2c, locationCount: 0, horizontal: false, ascending: true, length: this.c2c, width: this.c2c, landMark: false },

              { id: "G1", entryx: 66 * this.c2c, entryy: 21 * this.c2c, horizontal: true, ascending: false, locationCount: 4, width: 0.75 * this.sectionWidth },
              { id: "G2", locationCount: 2, width: 0.75 * this.sectionWidth, type: 'buffer' },
              { id: "G3",  locationCount: 5, width: 0.75 * this.sectionWidth, type: 'non-working' },
              { id: "G4", locationCount: 1, width: 0.75 * this.sectionWidth, type: 'auto' },
              { id: "G5", locationCount: 2, width: 0.75 * this.sectionWidth, type: 'non-working' },
              { id: "G6", locationCount: 3, width: 0.75 * this.sectionWidth },
              { id: "G7", locationCount: 2, width: 0.75 * this.sectionWidth },
              { id: "G8", locationCount: 6, width: 0.75 * this.sectionWidth },
              { id: "G9", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "G10", locationCount: 4, width: 0.75 * this.sectionWidth },
              { id: "H1", locationCount: 4, width: 0.75 * this.sectionWidth }, 
              { id: "H2", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "H3", locationCount: 3, width: 0.75 * this.sectionWidth },
              { id: "H4", locationCount: 1, width: 0.75 * this.sectionWidth },
              { id: "H5", locationCount: 19, width: 0.75 * this.sectionWidth, type: 'inspection' },
              { id: "H6", locationCount: 2, width: 0.75 * this.sectionWidth, type: 'non-working' },
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
            { text: "AF OFF", x: 2.5 * this.c2c, y: 21 * this.c2c }
        ];
        return config;
    }

    protected getStations(): any[] {
        let stations: any[] = [
            { locationId: 4, side: Side.R },
            { locationId: 90, side: Side.R },
            { locationId: 107, side: Side.R, offsety: -9 },
            { locationId: 126, side: Side.R, offsety: -9 },
            { locationId: 135, side: Side.R, offsety: -9 },
            { locationId: 142, side: Side.R, offsety: -9 },
            { locationId: 156, side: Side.R, offsety: -9 },
            { locationId: 254, side: Side.L, offsety: -9 },
            { locationId: 267, side: Side.L, offsetx: -13, offsety: -9 },
            { locationId: 297, side: Side.L, offsety: -9 },
            { locationId: 301, side: Side.L, offsety: -9 },
        ];
        return stations;
    }

    protected getProcessAreas(): any[] {
        let config: any[] = [
            { id: "Roof Glass", location: 10, side: Side.R },
            { id: "Door Off", location: 28, side: Side.L, offsetx: 4, offsety: -9 },
            { id: "BHD", location: 32, side: Side.L },
            { id: "Fuel Tank", location: 107, side: Side.R },
            { id: "Sunroof", location: 112, side: Side.R, offsety: -9 },
            { id: "Headliner", location: 126, side: Side.R },
            { id: "HVAC", location: 131, side: Side.R },
            { id: "Carpet", location: 135, side: Side.R },
            { id: "IP", location: 142, side: Side.R },
            { id: "FUM", location: 147, side: Side.R, offsety: -7 },
            { id: "SMURF", location: 156, side: Side.R },
            { id: "Damper", location: 161, side: Side.R, offsetx: 15, offsety: -7 },
            { id: "FEM", location: 170, side: Side.R, offsety: -7 },
            { id: "IPU Mount", location: 210, side: Side.L, offsety: -7 },
            { id: "Bumper", location: 230, side: Side.L, offsety: -7 },
            { id: "Exhaust", location: 244, side: Side.L, offsety: -9 },
            { id: "Console", location: 256, side: Side.L, offsety: -9 },
            { id: "Seats", location: 260, side: Side.L, offsetx: 15, offsety: -9 },
            { id: "Battery", location: 261, side: Side.L, offsetx: -4, offsety: -9 },
            { id: "Tires", location: 267, side: Side.L, offsetx: -13 },
            { id: "Glass", location: 274, side: Side.L },
            { id: "Mult Fill", location: 278, side: Side.L },
            { id: "Door On", location: 288, side: Side.L },
            { id: "HFO/Gas Fill", location: 297, side: Side.L },
            { id: "Drive Off", location: 310, side: Side.L },
        ];
        return config;
    }
    protected getTeamLeaderAreas(): any[] {
        let config: any[] = [
            { id: "WT1", location: 1, side: Side.L },
            { id: "WT2", location: 26, side: Side.R, offsetx: 25, offsety: 15 },
            { id: "WT3", location: 49, side: Side.R, offsetx: 20, offsety: 10 },
            { id: "WT4", location: 57, side: Side.R  },
            { id: "WT5", location: 65, side: Side.L, offsetx: 10},
            { id: "WT6", location: 72, side: Side.L },
            { id: "WT3", location: 78, side: Side.R, offsetx: 40, offsety: -29 },
            { id: "WT7", location: 81, side: Side.L  },
            { id: "WT8", location: 89 },
            { id: "WT9", location: 94, side: Side.L },
            { id: "WT10", location: 98, side: Side.L, offsetx: -8 },
            { id: "IN1", location: 107, side: Side.L, offsetx: -19 },
            { id: "IN2", location: 112, side: Side.L  },
            { id: "IN3", location: 117, side: Side.L },
            { id: "IN4", location: 123, side: Side.L },
            { id: "IN5", location: 132, side: Side.L, offsetx: -7 },
            { id: "IN6", location: 137, side: Side.L, offsety: 5 },
            { id: "IN7", location: 142, side: Side.L,  offsetx: -25  },
            { id: "UB1", location: 147, side: Side.L },
            { id: "UB2", location: 158, side: Side.L },
            { id: "UB3", location: 165, side: Side.L, offsety: 9 },
            { id: "UB4", location: 171, side: Side.L, offsetx: 15, offsety: 9 },
            { id: "UB5", location: 195, side: Side.L, offsetx: 3, offsety: 9 },
            { id: "UB6", location: 198, side: Side.L, offsetx: 9, offsety: 9 },
            { id: "UB7", location: 213, side: Side.R },
            { id: "UB8", location: 219, side: Side.R },
            { id: "UB9", location: 243, side: Side.R, offsetx: 13 },
            { id: "UB10", location: 247, side: Side.R, offsetx: 13 },
            { id: "EX1", location: 253, side: Side.R },
            { id: "EX2", location: 259, side: Side.R },
            { id: "EX3", location: 263, side: Side.R },
            { id: "EX4", location: 277, side: Side.R, offsetx: 13 },
            { id: "EX5", location: 282, side: Side.R },
            { id: "EX6", location: 289, side: Side.R, offsetx: -14 },
            { id: "EX7", location: 293, side: Side.R },
        ];
        return config;
    }
}