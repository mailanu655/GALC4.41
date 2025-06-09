import { Axis } from "../line/line-model";

export class LineConfig {

    public static readonly UP_RIGHT_ANG = "a10, 10 0 0 1 10, -10";
    public static readonly UP_LEFT_ANG = "a10, 10 0 0 0 -10,-10";
    public static readonly DOWN_RIGHT_ANG = "a10, 10 0 0 0 10, 10";
    public static readonly DOWN_LEFT_ANG = "a10, 10 0 0 1 -10, 10";
    public static readonly RIGHT_UP_ANG = "a10, 10 0 0 0 10, -10";
    public static readonly RIGHT_DOWN_ANG = "a10, 10 0 0 1 10, 10";
    public static readonly LEFT_UP_ANG = "a10, 10 0 0 1 -10, -10";
    public static readonly LEFT_DOWN_ANG = "a10, 10 0 0 0 -10, 10";
    public static readonly UP_FULL_ANG = LineConfig.LEFT_UP_ANG + LineConfig.UP_RIGHT_ANG + LineConfig.RIGHT_DOWN_ANG + LineConfig.DOWN_LEFT_ANG;
    public static readonly RIGHT_FULL_ANG = LineConfig.UP_RIGHT_ANG + LineConfig.RIGHT_DOWN_ANG + LineConfig.DOWN_LEFT_ANG + LineConfig.LEFT_UP_ANG;
    public static readonly DOWN_FULL_ANG = LineConfig.RIGHT_DOWN_ANG + LineConfig.DOWN_LEFT_ANG + LineConfig.LEFT_UP_ANG + LineConfig.UP_RIGHT_ANG;
    public static readonly LEFT_FULL_ANG = LineConfig.DOWN_LEFT_ANG + LineConfig.LEFT_UP_ANG + LineConfig.UP_RIGHT_ANG + LineConfig.RIGHT_DOWN_ANG;

    public stationHeight = 15;
    public stationWidth = 15;

    public static readonly RADIUS: number = 10;
    public static readonly ARROW_LENGTH: number = 10;
    public id: string;
    public locationStartId: number = 1;
    public c2c: number = 24;
    public entryLength: number;
    public exitLength: number;
    public startLength: number;
    public endLength: number;
    public sectionWidth: number;
    public shapeScale = 1;

    axis: Axis[] = [];
    sections: any[] = [];
    landMarks: any[] = [];
    labels: any[] = [];
    processPoints: any[] = [];
    stations: any[] = [];
    processAreas: any[] = [];
    teamLeaderAreas: any[] = [];

    constructor() {
        this.set();
        this.init();
        this.axis = this.getAxis();
        this.sections = this.getSections();
        this.landMarks = this.getLandMarks();
        this.labels = this.getLabels();
        this.stations = this.getStations();
        this.processAreas = this.getProcessAreas();
        this.teamLeaderAreas = this.getTeamLeaderAreas();
    }

    protected set() {
    }

    protected init() {
        this.entryLength = 0.5 * this.c2c;
        this.exitLength = 0.5 * this.c2c;
        this.startLength = this.c2c;
        this.endLength = this.c2c;
        this.sectionWidth = 2 * this.c2c;
        if (this.c2c < 24) {
            this.shapeScale = this.c2c / 24;
        }
    }

    protected getAxis(): Axis[] {
        return [];
    }

    protected getSections(): any[] {
        return [];
    }

    protected getDirectionIndicators(): any[] {
        return [];
    }
    protected getLandMarks(): any[] {
        return [];
    }

    protected getLabels(): any[] {
        let config: any[] = [];
        config.push({ text: "TODO: DEFINE LINE", x: 800, y: 250 });
        return config;
    }

    protected getStations(): string[] {
        return [];
    };

    protected getProcessAreas(): any[] {
        return [];
    }

    protected getTeamLeaderAreas(): any[] {
        return [];
    }
}