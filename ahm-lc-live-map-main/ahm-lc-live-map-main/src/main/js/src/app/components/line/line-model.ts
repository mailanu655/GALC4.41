import { BODY_COLORS } from "src/app/lib/colors";
import { LocationView } from "src/app/lib/constants";
import { Product } from "src/app/services/model";
import { LineConfig } from "../line-config/line-config";

export enum ProcessPointType {
    STATION = "STATION", LC = "LC", QICS = "QICS", BOS = "BOS"
}

export enum Side { T = 'T', R = 'R', B = 'B', L = 'L' }

export enum SectionType { BUFFER = 'buffer', NON_WORKING = 'non-working', WORKING = 'working', INSPECTION = 'inspection', AUTO = 'auto', SEMI_AUTO = 'semi-auto' }

export enum DepartmentCode {
    WE = 'WE', PA = 'PA', AF = 'AF', VQ = 'VQ', AHM = 'AHM', PC = 'PC'
}

export enum LineCode {
    WE_ON = 'WE_ON', WE_OFF = 'WE_OFF', PA_ON = 'PA_ON', AF_ON = 'AF_ON', AF_OFF = 'AF_OFF', VQ_ON = 'VQ_ON', VQ_OFF = 'VQ_OFF'
}

export class Line {

    config: LineConfig;
    axis: Axis[] = [];
    landMarks: LandMark[] = [];
    labels: Label[] = [];
    sectionIx: Map<string, Section> = new Map<string, Section>();
    public readonly locations: BodyLocation[] = [];
    public readonly locationIx: Map<number, BodyLocation> = new Map<number, BodyLocation>();
    private readonly locationByProductIdIx: Map<string, BodyLocation> = new Map<string, BodyLocation>();
    public readonly locationByProcessPointIdIx: Map<string, BodyLocation> = new Map<string, BodyLocation>();

    // === data operations === //
    addLocation(location: BodyLocation) {
        if (!location) {
            return;
        }
        this.locations.push(location);
    }

    createLocationIx() {
        this.locations.sort((l1, l2) => l1.id - l2.id)
        let ix = 0;
        for (let bl of this.locations) {
            bl.ix = ix;
            this.locationIx.set(bl.id, bl);
            ix++;
        }
    }

    clearLocations() {
        this.locationByProductIdIx.clear();
        for (let bl of this.locations) {
            bl.product = null!;
        }
    }

    getNextLocation(id: number): BodyLocation | undefined {
        let location = this.locationIx.get(id);
        if (!location) {
            return undefined;
        }
        let nextLocation = this.locations[location.ix + 1];
        return nextLocation;
    }

    getBufferLocations(ix: number): BodyLocation[] {
        let locations: BodyLocation[] = [];
        for (let i = ix; i > -1; i--) {
            let location: BodyLocation = this.locations[i];
            if (location.section.buffer === true) {
                locations.push(location);
            }
        }
        return locations;
    }

    getBufferProductCount(ix: number): number {
        let locations: BodyLocation[] = this.getBufferLocations(ix);
        let count: number = 0;
        for (let location of locations) {
            if (location.product) {
                count = count + 1;
            }
        }
        return count;
    }

    setProduct(locationId: number, product: Product) {
        if (locationId === null) {
            return;
        }
        let location = this.locationIx.get(locationId);
        if (!location) {
            //console.log("Product pushed of the line locationId: " + locationId + ", product: " + product?.id);
            return;
        }
        location.product = product;
        if (!product) {
            return;
        }
        product.assignedLocationId = locationId;
        product['locationCode'] = location.code;
        product['locationSequence'] = location.sequence;
        this.locationByProductIdIx.set(product.id, location);
    }

    // === get/set === //
    getLocation(id: number): BodyLocation {
        return this.locationIx.get(id)!;
    }

    getLocationByProductId(id: string): BodyLocation {
        return this.locationByProductIdIx.get(id)!;
    }

    getSection(id: string) {
        return this.sectionIx.get(id);
    }

    get sections(): Section[] {
        let sections: Section[] = [];
        for (let item of Array.from(this.sectionIx.values())) {
            sections.push(item);
        }
        return sections;
    }

    get locationCount(): number {
        return this.locations.length;
    }

    get stations(): Station[] {
        let stations: Station[] = [];
        for (let stationConfig of this.config.stations) {
            let station = new Station();
            Object.assign(station, stationConfig);
            stations.push(station);
        }
        return stations;
    }

    // === computed values === //
    get path(): string {
        let radius = LineConfig.RADIUS;
        let x0 = this.getStartPointX();
        let y0 = this.getStartPointY();
        let xn = this.getEndPointX();
        let yn = this.getEndPointY();
        let path = "";
        let sections: Section[] = this.connectableSections;
        path = "M" + x0 + " " + y0 + " ";
        path = path + " " + this.getStartMarker();
        for (let ix = 0; ix < sections.length; ix++) {
            let section = sections[ix];
            let nextSection = sections[ix + 1];
            path = path + " L"
            if (section.end === true) {
                path = path + " " + section.exitx + "  " + section.exity;
                let x = this.getEndPointX(section);
                let y = this.getEndPointY(section);
                path = path + " L " + x + " " + y;
                path = path + " " + this.getEndMarker();
                path = path + " M"
            }
            if (section.break === true) {
                path = path + " " + section.exitx + "  " + section.exity;
                path = path + " M"
            }
            if (section.horizontal) {
                if (nextSection) {
                    if (nextSection.horizontal) {
                        path = path + " " + nextSection.entryx + "  " + nextSection.entryy;
                    } else {
                        path = path + " " + (nextSection.entryx - section.signum * radius) + "  " + section.entryy;
                        path = path + " " + this.getConnector(section, nextSection);
                    }
                } else {
                    path = path + " " + section.exitx + "  " + section.exity;
                }
            } else {
                if (nextSection) {
                    if (nextSection.horizontal) {
                        path = path + " " + section.entryx + "  " + (nextSection.entryy - section.signum * radius);
                        path = path + " " + this.getConnector(section, nextSection);
                    } else {
                        path = path + " " + nextSection.entryx + "  " + nextSection.entryy;
                    }
                } else {
                    path = path + " " + section.exitx + "  " + section.exity;
                }
            }
        }
        path = path + " L " + xn + " " + yn;
        path = path + " " + this.getEndMarker();
        return path;
    }

    getStartPointX(): number {
        if (!this.sections || this.sections.length < 1) {
            return 0;
        }
        let section = this.sections[0];
        let x: number = 0;
        if (section.horizontal) {
            x = section.entryx - section.signum * this.config.startLength;
        } else {
            x = section.entryx;
        }
        return x;
    }

    getStartPointY(): number {
        if (!this.sections || this.sections.length < 1) {
            return 0;
        }
        let section = this.sections[0];
        let y: number = 0;
        if (section.horizontal) {
            y = section.entryy;
        } else {
            y = section.entryy - section.signum * this.config.startLength;
        }
        return y;
    }

    getEndPointX(section?: Section): number {
        if (!this.sections || this.sections.length < 1) {
            return 0;
        }
        if (!section) {
            section = this.sections[this.sections.length - 1];
        }
        let x: number = 0;
        if (section.horizontal) {
            x = section.exitx + section.signum * this.config.endLength;
        } else {
            x = section.exitx;
        }
        return x;
    }

    getEndPointY(section?: Section): number {
        if (!this.sections || this.sections.length < 1) {
            return 0;
        }
        if (!section) {
            section = this.sections[this.sections.length - 1];
        }
        let y: number = 0;
        if (section.horizontal) {
            y = section.exity;
        } else {
            y = section.exity + section.signum * this.config.endLength;
        }
        return y;
    }

    getStartMarker(): string {
        if (!this.sections || this.sections.length < 1) {
            return '';
        }
        let section = this.sections[0];
        if (section.horizontal) {
            if (section.ascending) {
                return LineConfig.LEFT_FULL_ANG;
            } else {
                return LineConfig.RIGHT_FULL_ANG;
            }
        } else {
            if (section.ascending) {
                return LineConfig.UP_FULL_ANG;
            } else {
                return LineConfig.DOWN_FULL_ANG;
            }
        }
    }

    getEndMarker(): string {
        if (!this.sections || this.sections.length < 1) {
            return '';
        }
        let section = this.sections[this.sections.length - 1];
        if (section.horizontal) {
            if (section.ascending) {
                return LineConfig.RIGHT_FULL_ANG;
            } else {
                return LineConfig.LEFT_FULL_ANG;
            }
        } else {
            if (section.ascending) {
                return LineConfig.DOWN_FULL_ANG;
            } else {
                return LineConfig.UP_FULL_ANG;
            }
        }
    }

    getConnector(section: Section, nextSection: Section): string {
        if (!section || !nextSection) {
            return '';
        }
        if (section.horizontal) {
            if (section.ascending) {
                if (section.exity < nextSection.exity) {
                    return LineConfig.RIGHT_DOWN_ANG;
                } else {
                    return LineConfig.RIGHT_UP_ANG;
                }
            } else {
                if (section.exity < nextSection.exity) {
                    return LineConfig.LEFT_DOWN_ANG;
                } else {
                    return LineConfig.LEFT_UP_ANG;
                }
            }
        } else {
            if (section.ascending) {
                if (section.exitx < nextSection.exitx) {
                    return LineConfig.DOWN_RIGHT_ANG;
                } else {
                    return LineConfig.DOWN_LEFT_ANG;
                }
            } else {
                if (section.exitx < nextSection.exitx) {
                    return LineConfig.UP_RIGHT_ANG;
                } else {
                    return LineConfig.UP_LEFT_ANG;
                }
            }
        }
    }

    get connectableSections(): Section[] {
        let sections: Section[] = [];
        for (let section of this.sections) {
            if (section.connect === true) {
                sections.push(section);
            }
        }
        return sections;
    }

    get directionIndicators(): any[] {
        let directionIndicators: any[] = [];
        let radius = LineConfig.RADIUS;
        let sections: Section[] = this.connectableSections;
        for (let ix = 0; ix < sections.length; ix++) {
            let section = sections[ix];
            let previousSection = sections[ix - 1];
            let nextSection = sections[ix + 1];
            if (section.horizontal) {
                if (previousSection) {
                    if (previousSection.horizontal === false) {
                        if (section.ascending === true && previousSection.exitx > section.entryx) {
                            directionIndicators.push({ id: directionIndicators.length, d: "M" + previousSection.exitx + "  " + (previousSection.exity + 0.5 * previousSection.signum * radius) + " v" + previousSection.signum });
                        } else if (section.ascending === false && previousSection.exitx < section.entryx) {
                            directionIndicators.push({ id: directionIndicators.length, d: "M" + previousSection.exitx + "  " + (previousSection.exity + 0.5 * previousSection.signum * radius) + " v" + previousSection.signum });
                        } else {
                            directionIndicators.push({ id: directionIndicators.length, d: "M" + (section.entryx - section.signum * radius) + "  " + (section.entryy) + " h" + section.signum });
                        }
                    }
                }
                if (nextSection) {
                    if (nextSection.horizontal === false) {
                        if (section.ascending === true && nextSection.entryx < section.exitx) {
                            directionIndicators.push({ id: directionIndicators.length, d: "M" + nextSection.entryx + "  " + (nextSection.entryy + nextSection.signum * radius) + " v" + nextSection.signum });
                        } else if (section.ascending === false && nextSection.entryx > section.exitx) {
                            directionIndicators.push({ id: directionIndicators.length, d: "M" + nextSection.entryx + "  " + (nextSection.entryy - nextSection.signum * radius) + " v" + nextSection.signum });
                        } else {
                            directionIndicators.push({ id: directionIndicators.length, d: "M" + (section.exitx + section.signum * (0.5) * radius) + "  " + (section.exity) + " h" + section.signum });
                        }
                    }
                }
            }
            if (section.locations.length === 0) {
                directionIndicators.push({ id: directionIndicators.length, d: "M" + section.cx + "  " + section.cy + " " + section.direction + section.signum });
            }
        }
        if (sections.length > 0) {
            directionIndicators = directionIndicators.concat(this.getStartDirectionIndicators(sections));
            directionIndicators = directionIndicators.concat(this.getEndDirectionIndicators(sections));
        }
        return directionIndicators;
    }

    getStartDirectionIndicators(sections: Section[]): any[] {
        let directionIndicators: any[] = [];
        let section0 = sections[0];
        let x0 = this.getStartPointX();
        let y0 = this.getStartPointY();
        if (section0.horizontal) {
            directionIndicators.push({ id: directionIndicators.length, d: "M " + (x0 + section0.signum * LineConfig.ARROW_LENGTH / 2) + "  " + y0 + section0.direction + section0.signum });
        } else {
            directionIndicators.push({ id: directionIndicators.length, d: "M " + x0 + "  " + (y0 + section0.signum * LineConfig.ARROW_LENGTH / 2) + section0.direction + section0.signum });
        }
        return directionIndicators;
    }

    getEndDirectionIndicators(sections: Section[]) {
        let directionIndicators: any[] = [];
        let lastSection = sections[sections.length - 1];
        let endSections = sections.filter(sec => sec.end === true || sec.id === lastSection.id);
        for (let section of endSections) {
            let xn = this.getEndPointX(section);
            let yn = this.getEndPointY(section);
            if (section.horizontal) {
                directionIndicators.push({ id: directionIndicators.length, d: "M " + (xn - section.signum * LineConfig.ARROW_LENGTH) + "  " + yn + section.direction + section.signum });
            } else {
                directionIndicators.push({ id: directionIndicators.length, d: "M " + xn + "  " + (yn - section.signum * LineConfig.ARROW_LENGTH) + section.direction + section.signum });
            }
        }
        return directionIndicators;
    }
}

export class Axis {
    horizontal: boolean = true;
    ascending: boolean = true;
    x: number = 0;
    y: number = 0;
    values: string[] = [];
    step: number = 30;
}

export class Station {
    locationId: number;
    side: Side;
}

export class Label {
    public readonly x: number;
    public readonly y: number;
    public readonly text: string;

    constructor(text: string, x: number, y: number) {
        this.x = x;
        this.y = y;
        this.text = text;
    }
}

export class LandMark {

    readonly id: string;

    readonly rx: number = 5;
    readonly ry: number = 5;
    readonly landMark: boolean = true;
    horizontal: boolean;
    ascending: boolean;

    entryx: number;
    entryy: number;

    length: number;
    width: number;

    entryLength: number;
    exitLength: number;

    fill: string;

    constructor(config: any) {
        Object.assign(this, config);
    }

    // === get/set === //

    get name() {
        return this.id;
    }

    get x(): number {
        if (this.horizontal) {
            if (this.ascending) {
                return this.entryx;
            } else {
                return this.entryx - this.length;
            }
        } else {
            return this.entryx - this.width / 2;
        }
    }

    get y(): number {
        if (this.horizontal) {
            return this.entryy - this.width / 2;
        } else {
            if (this.ascending) {
                return this.entryy;
            } else {
                return this.entryy - this.length;
            }
        }
    }

    get cx() {
        if (this.horizontal) {
            return this.entryx + this.signum * this.length / 2;
        } else {
            return this.entryx;
        }
    }

    get cy() {
        if (this.horizontal) {
            return this.entryy;
        } else {
            return this.entryy + this.signum * this.length / 2;
        }
    }

    get exitx() {
        if (this.horizontal) {
            return this.entryx + this.signum * this.length;
        } else {
            return this.entryx;
        }
    }

    get exity() {
        if (this.horizontal) {
            return this.entryy;
        } else {
            return this.entryy + this.signum * this.length;
        }
    }

    get signum() {
        if (this.ascending === true) {
            return 1;
        } else {
            return -1;
        }
    }

    get direction() {
        if (this.horizontal === true) {
            return 'h';
        } else {
            return 'v';
        }
    }
}

export class Section extends LandMark {

    connect: boolean = true;
    break: boolean = false;
    end: boolean = false;
    type: SectionType;
    locations: BodyLocation[] = [];

    constructor(config: any) {
        super({});
        Object.assign(this, config);
    }

    get buffer(): boolean {
        if (this.type === SectionType.BUFFER) {
            return true;

        } else {
            return false;
        }
    };

    get tooltip() {
        let str = "";
        if (this.id) {
            str = str + "ID: " + this.id + "\n";
        }
        if (this.name) {
            str = str + "Name: " + this.name + "\n";
        }
        if (this.locations) {
            str = str + "BL Count: " + this.locations.length;
        } else {
            str = str + "BL Count: 0";
        }
        return str;
    }
}

export class BodyLocation {
    scale: number = 1;
    id: number;
    _code: any;
    ix: number;
    cx: number;
    cy: number;
    section: Section;

    product: Product;

    constructor(id: number) {
        this.id = id;
    }

    get sequence() {
        return this.ix + 1;
    }

    get code(): string {
        if (this._code != null) {
            return this._code;
        } else {
            return "" + this.id;
        }
    }

    set code(code: string) {
        this._code = code;
    }

    get horizontal() {
        return this.section.horizontal
    }

    get ascending() {
        return this.section.ascending;
    }

    get signum() {
        return this.section.signum;
    }

    get x(): number {
        return this.cx - this.width / 2;
    }

    get y(): number {
        return this.cy - this.height / 2;
    }

    get width(): number {
        if (this.horizontal) {
            return this.scale * this.view.width;
        } else {
            return this.scale * this.view.height;
        }
    }

    get height(): number {
        if (this.horizontal) {
            return this.scale * this.view.height;
        } else {
            return this.scale * this.view.width;
        }
    }

    get rx(): number {
        if (this.horizontal) {
            return this.view.rx;
        } else {
            return this.view.ry;
        }
    }

    get ry(): number {
        if (this.horizontal) {
            return this.view.ry;
        } else {
            return this.view.rx;
        }
    }

    get view(): LocationView {
        if (this.product) {
            return LocationView.match(this.product.model);
        } else {
            return LocationView.empty;
        }
    }

    get color(): string {
        if (!this.product || !this.product.extColor) {
            return "lightgrey";
        }
        let colorCode = this.product.extColor.trim().toUpperCase();
        let rgb = BODY_COLORS[colorCode];
        if (rgb) {
            return rgb;
        }
        for (const colorKey in BODY_COLORS) {
            if (colorCode.indexOf(colorKey) > -1 || colorKey.indexOf(colorCode) > -1) {
                rgb = BODY_COLORS[colorKey];
                if (rgb) {
                    BODY_COLORS[colorCode] = rgb;
                    return rgb;
                }
            }
        }
        return "magenta";
    }

    get strokeWidth(): number {
        if (this.isSelected()) {
            return 3.5;
        } else {
            return 2;
        }
    }

    get strokeColor(): string {
        if (this.isSelected()) {
            return 'blue';
        } else {
            return 'rgb(54, 69, 79)';
        }
    }

    get animation(): any {
        let colors: string[] = [];
        if (this.product) {
            if (this.product.defects && this.product.defects.length > 0) {
                colors.push('red');
            }
            if (this.product.partIssues && this.product.partIssues.length > 0) {
                colors.push('black');
            }
            if (this.product.holds && this.product.holds.length > 0) {
                colors.push('rgb(153, 102, 51)');
            }
        }
        if (colors.length === 0) {
            return {};
        }
        let colorValues = "";
        let firstColor = colors[0];
        for (let color of colors) {
            colorValues = colorValues + color + ";" + "white;"
        }
        colorValues = colorValues + firstColor;
        let animation = {};
        animation['stroke'] = { values: colorValues, duration: colors.length + 's' };
        animation['fill'] = { values: colorValues, duration: colors.length + 's' };
        animation['opacity'] = { values: "1; 0.9; 1", duration: "1s" };
        animation['stroke-width'] = { values: "5; 2; 5", duration: "1s" };
        return animation;
    }

    isSelected(): boolean {
        //REMARK: implemented dynamically in LineComponent
        return false;
    }

    getAbsoluteSide(side: Side): Side {
        let shiftValue: number = this.sideOffset;
        let length = Object.values(Side).length;
        let maxValue = length - 1;
        if (!side) {
            return side;
        }
        if (!shiftValue || shiftValue > maxValue || shiftValue < -1 * maxValue || shiftValue === 0) {
            return side;
        }
        if (shiftValue < 0) {
            shiftValue = length + shiftValue;
        }
        let ordinal: number = Object.values(Side).indexOf(side);
        ordinal = (ordinal + shiftValue) % 4;
        let shiftedSide = Object.values(Side)[ordinal];
        return shiftedSide;
    }

    get sideOffset(): number {
        if (this.horizontal === true && this.ascending === true) {
            return 1;
        }
        if (this.horizontal === false && this.ascending === true) {
            return 2;
        }
        if (this.horizontal === true && this.ascending === false) {
            return 3;
        }
        return 0;
    }
}