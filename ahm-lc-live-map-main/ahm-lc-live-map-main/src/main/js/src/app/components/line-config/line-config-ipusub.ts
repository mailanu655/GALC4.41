import { LineConfig } from "./line-config";
import { Side } from "../line/line-model";

export class IpuSubConfig extends LineConfig {

    c2cy: number;

    protected set() {
        this.id = 'ipusub';
        this.c2c = 30;
        this.c2cy = 20;
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
            { x: 1650, y: 16 * this.c2cy, step: 4.75 * this.c2cy, values: this.getYAxisValues(), horizontal: false }
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
        // for (let i = 50; i <= 98; i = i + 2) {
        //     config.push(i.toString());
        // }
        return config;
    }

    protected getYAxisValues(): string[] {
        let config: string[] = ['', ''];
        return config;
    }

    protected getSections(): any[] {
        let config: any[] = [
            { id: "AFON", entryx: 7.5 * this.c2c, entryy:5 * this.c2c, locationCount: 15, horizontal: true, ascending: true, entryLength: this.c2c },
            { id: "A1", entryx: 24 * this.c2c,entryy:5 * this.c2c, locationCount: 2, horizontal: false, ascending: false,  },

            { id: "A3L", entryx:32 * this.c2c,entryy:2 * this.c2c, locations: [{ id: 18 }, { id: 20 }, { id: 22 }, { id: 24 }, { id: 26 }, { id: 28 }, {id: 30}, { id: 32 }, { id: 34 }, { id: 36 }, { id: 38 }, { id: 40 }], horizontal: true, ascending: true, connect: false},
            { id: "CD01", entryx: 38 * this.c2c, entryy: 2.75*this.c2c, locationCount: 0,  horizontal: true, ascending: true,  landMark: false },

            { id: "A3U", entryx: 32 * this.c2c, entryy:3.5 * this.c2c, locations: [{ id: 19 }, { id: 21 }, { id: 23 }, { id: 25 }, { id: 27 }, { id: 29 },{ id: 31 }, { id: 33 }, { id: 35 }, { id: 37 }, { id: 39 }, { id: 41 }], horizontal: true, ascending: true , connect: false},

            { id: "CD1", entryx: 50 * this.c2c, entryy: 3.5*this.c2c, locationCount: 0,  horizontal: false, ascending: true,  landMark: false },

  
            { id: "CD2", entryx: 35 * this.c2c, entryy:4.5* this.c2c, locationCount: 0, horizontal: true, ascending: false, landMark: false },
            { id: "CD3", entryx: 26 * this.c2c, entryy:5* this.c2c, locationCount: 0, horizontal: false,length: this.c2c, ascending: true, landMark: false },

            { id: "F4L", entryx: 28 * this.c2c, entryy: 5.5 * this.c2c, locationCount: 19, width: this.c2c, connect: false,horizontal: true, ascending: true },
            { id: "F4U", entryx: 28 * this.c2c, entryy: 6.5 * this.c2c, locationCount: 19, horizontal: true, ascending: true, width: this.c2c, connect: false },
            { id: "CD4", entryx: 38 * this.c2c, entryy: 6 *this.c2c, locationCount: 0, horizontal: true, ascending: true, length: this.c2c, landMark: false },

            { id: "F5L", entryx: 50 * this.c2c, entryy: 8 * this.c2c, locationCount: 12, width: this.c2c, connect: false,horizontal: false, ascending: true },
            { id: "F5U", entryx: 51 * this.c2c, entryy: 8 * this.c2c, locationCount: 12, horizontal: false, ascending: true, width: this.c2c, connect: false },
            { id: "CD6", entryx: 50.5 * this.c2c, entryy: 15 *this.c2c, locationCount: 0, horizontal: false, ascending: true, length: 3 * this.c2c, landMark: false },
            { id: "CD7", entryx: 49 * this.c2c, entryy: 21 *this.c2c, locationCount: 0, horizontal: true, ascending: false, length: 2 * this.c2c, landMark: false },

            { id: "F6L", entryx: 47 * this.c2c, entryy: 20 * this.c2c, locationCount: 12, width: this.c2c, connect: false,horizontal: false, ascending: false },
            { id: "F6U", entryx: 48 * this.c2c, entryy: 20 * this.c2c, locationCount: 12, horizontal: false, ascending: false, width: this.c2c, connect: false },
            { id: "CD8", entryx: 47.5 * this.c2c, entryy: 10 *this.c2c, locationCount: 0, horizontal: false, ascending: false, length: 3 * this.c2c, landMark: false },
            { id: "A7", entryx: 46.2 * this.c2c,entryy:7.6 * this.c2c, locationCount: 1, horizontal: true, ascending: false  },
            { id: "A8", entryx: 44 * this.c2c,entryy:8 * this.c2c, locationCount: 12, horizontal: false, ascending: true },
            { id: "CD9", entryx: 43 * this.c2c, entryy: 20.5 *this.c2c, locationCount: 0, horizontal: true, ascending: false, length:   this.c2c, landMark: false },
            { id: "F7L", entryx: 42 * this.c2c, entryy: 20 * this.c2c, locationCount: 10, width: this.c2c, connect: false,horizontal: true, ascending: false },
            { id: "F7U", entryx: 42 * this.c2c, entryy: 21 * this.c2c, locationCount: 10, horizontal: true, ascending: false, width: this.c2c, connect: false },
            { id: "A9", entryx: 31 * this.c2c,entryy:20.5 * this.c2c, locationCount: 6, horizontal: true, ascending: false },
            { id: "CD10", entryx: 22 * this.c2c, entryy: 20.5 *this.c2c, locationCount: 0, horizontal: false, ascending: false, length:  this.c2c, landMark: false },
            { id: "A10", entryx: 26 * this.c2c,entryy:18 * this.c2c, locationCount: 5, horizontal: true, ascending: true },
            { id: "A10L", entryx:34 * this.c2c,entryy:17.3 * this.c2c, locations: [{ id: 172 }, { id: 174 }, { id: 176 }, { id: 178 }, ], horizontal: true, ascending: true, connect: false},
            { id: "A10R", entryx:34 * this.c2c,entryy:18.7 * this.c2c, locations: [{ id: 173 }, { id: 175}, { id: 177 }, { id: 179 }], horizontal: true, ascending: true, connect: false},
            { id: "CD11", entryx: 32 * this.c2c, entryy: 18 *this.c2c, locationCount: 0, horizontal: true, ascending: true, length:  this.c2c, landMark: false },

            { id: "A11", entryx: 41 * this.c2c,entryy:17 * this.c2c, locationCount: 8, horizontal: false, ascending: false },
            { id: "CD12", entryx: 40 * this.c2c, entryy: 8 *this.c2c, locationCount: 0, horizontal: true, ascending: false, length:  this.c2c, landMark: false },
            { id: "CD13", entryx: 38 * this.c2c, entryy: 8 *this.c2c, locationCount: 0, horizontal: false, ascending: true, length:  this.c2c, landMark: false },

            { id: "A12", entryx: 36 * this.c2c,entryy:10 * this.c2c, locationCount: 5, horizontal: true, ascending: false },


        ];
        return config;
    }

    protected getLandMarks(): any[] {
        return [];
    }

    protected getLabels(): any[] {
        let firstSection: any = this.sections[0];
        let config: any[] = [
            { text: "Case Transfer", x: firstSection.entryx - 5 * this.c2c, y: firstSection.entryy + 0.05 * this.c2cy },
            { text: "Assy Transfer", x: 27 * this.c2c, y: 10 * this.c2c }
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
          
        ];
        return config;
    }
    protected getTeamLeaderAreas(): any[] {
        let config: any[] = [
            
        ];
        return config;
    }
}