export class SortDir {
    static readonly ASC = { name: 'asc', label: 'Ascending' };
    static readonly DESC = { name: 'desc', label: "Descending" };
}

export class PropertyDef {
    name: string;
    label: string;
}

export class EntityProperty {
    static readonly ID: PropertyDef = { name: 'id', label: 'Id' };
    static readonly PRODUCT_ID: PropertyDef = { name: 'productId', label: 'Product Id' };
    static readonly NAME: PropertyDef = { name: 'name', label: 'Name' };
}

export enum IssueType {
    QICS, PART
}

export class HoldType {
    id: number;
    name: string;

    toString() {
        return this.name + " (" + this.id + ")";
    }

    private static readonly ixById: Map<number, HoldType> = new Map<number, HoldType>();

    static getById(id: number): HoldType {
        return HoldType.ixById[id];
    }

    static create(id: number, name: string): InstalledPartStatus {
        let item = Object.assign(new HoldType(), { id: id, name: name });
        HoldType.ixById[id] = item;
        return item;
    }

    static readonly GENERIC: HoldType = HoldType.create(-1, 'Hold of any type');
    static readonly NOW: HoldType = HoldType.create(0, 'Hold Now');
    static readonly AT_SHIPPING: HoldType = HoldType.create(1, 'Hold at Shipping');
}

export class InstalledPartStatus {
    id: number;
    name: string;

    toString() {
        return this.name + " (" + this.id + ")";
    }

    private static readonly ixById: Map<number, InstalledPartStatus> = new Map<number, InstalledPartStatus>();

    static getById(id: number) {
        return InstalledPartStatus.ixById[id];
    }

    static create(id: number, name: string): InstalledPartStatus {
        let item = Object.assign(new InstalledPartStatus(), { id: id, name: name });
        InstalledPartStatus.ixById[id] = item;
        return item;
    }

    static readonly REMOVED: InstalledPartStatus = InstalledPartStatus.create(-9, 'REMOVED');
    static readonly NC: InstalledPartStatus = InstalledPartStatus.create(-2, 'NC');
    static readonly BLANK: InstalledPartStatus = InstalledPartStatus.create(-1, 'BLANK');
    static readonly NG: InstalledPartStatus = InstalledPartStatus.create(0, 'NG');
    static readonly OK: InstalledPartStatus = InstalledPartStatus.create(1, 'OK');
    static readonly ACCEPT: InstalledPartStatus = InstalledPartStatus.create(2, 'ACCEPT');
    static readonly REJECT: InstalledPartStatus = InstalledPartStatus.create(3, 'REJECT');
    static readonly MISSING: InstalledPartStatus = InstalledPartStatus.create(4, 'MISSING');
    static readonly PENDING: InstalledPartStatus = InstalledPartStatus.create(5, 'PENDING');
    static readonly REPAIRED: InstalledPartStatus = InstalledPartStatus.create(9, 'REPAIRED');
    static readonly NM: InstalledPartStatus = InstalledPartStatus.create(11, 'NM');
}

export class Model {
    constructor(public name: string, public label: string) {
    }
}

export class Shape {

    private static readonly instanceMap: Map<number, Shape> = new Map<number, Shape>();

    constructor(public id: number, public width: number, public height: number, public rx: number, public ry: number, public strokeDasharray?: string) {
        if (strokeDasharray) {
            this.strokeDasharray = strokeDasharray;
        } else {
            this.strokeDasharray = "0 0";
        }
        Shape.instanceMap.set(id, this);
    }

    public static get(id: number) {
        return Shape.instanceMap.get(id);
    }
}

export class LocationView {

    private static readonly instanceMap: Map<string, LocationView> = new Map<string, LocationView>();

    private model: Model;
    public view: Shape;

    constructor(model: Model, view: Shape) {
        this.model = model;
        this.view = view;
        LocationView.instanceMap.set(model.name, this);
    }

    get name(): string {
        return this.model.name;
    }

    get label(): string {
        return this.model.label;
    }

    get width(): number {
        return this.view.width;
    }
    get height(): number {
        return this.view.height;
    }
    get rx(): number {
        return this.view.rx;
    }
    get ry(): number {
        return this.view.rx;
    }
    get strokeDasharray(): string {
        return this.view.strokeDasharray!;
    }

    equals(other: any) {
        if (other && other.model) {
            if (other.model.name == this.model.name) {
                return true;
            }
        }
        return false;
    }

    public static get values(): LocationView[] {
        return Array.from(LocationView.instanceMap.values());
    }

    public static get empty(): LocationView {
        return LocationView.instanceMap.get('EMPTY_LOCATION')!;
    }

    public static get honda(): LocationView {
        return LocationView.instanceMap.get('HONDA')!;
    }

    public static get(name: string): LocationView {
        let bv = LocationView.instanceMap.get(name);
        if (bv) {
            return bv;
        } else {
            return LocationView.honda;
        }
    }

    public static getIfExists(name: string): LocationView | undefined {
        let bv = LocationView.instanceMap.get(name);
        return bv;
    }

    public static startsWith(model: string): LocationView {
        if (!model) {
            return LocationView.honda;
        }
        let name: string;
        for (let key of Array.from(LocationView.instanceMap.keys())) {
            if (model.startsWith(key)) {
                name = key;
                break;
            }
        }
        return LocationView.get(name!)!;
    }

    public static init() {
        new LocationView(new Model('CIVIC', 'Civic'), new Shape(1, 20, 20, 10, 10));
        new LocationView(new Model('INSIGHT', 'Insight'), new Shape(2, 20, 17, 10, 10));
        new LocationView(new Model('CRV', 'CRV'), new Shape(3, 15, 15, 3, 3));
        new LocationView(new Model('HRV', 'HRV'), new Shape(4, 20, 15, 2, 2));
        new LocationView(new Model('PILOT', 'Pilot'), new Shape(5, 20, 20, 3, 3));
        new LocationView(new Model('RIDGELINE', 'Ridgeline'), new Shape(6, 20, 20, 5, 15));
        new LocationView(new Model('ODYSSEY', 'Odyssey'), new Shape(7, 20, 20, 15, 5));
        new LocationView(new Model('RDX', 'RDX'), new Shape(8, 20, 15, 5, 5));
        new LocationView(new Model('MDX', 'MDX'), new Shape(9, 20, 20, 5, 5));
        new LocationView(new Model('NSX', 'NSX'), new Shape(10, 20, 10, 3, 5));
        new LocationView(new Model('HONDA', 'Honda'), new Shape(0, 20, 20, 1, 1));
        new LocationView(new Model('EMPTY_LOCATION', 'Empty Location'), new Shape(-1, 15, 10, 0, 0, "2 1"));
    }
}
LocationView.init();


export const BODY_COLORS = {
    //TODO finish it
    //RED: 'rgb(255, 179, 179)',
    RED: 'rgb(255,99,71)',
    //RED: 'rgb(255,0,0)',
    'BASQUE RED': 'rgb(184, 15, 10)',
    BEIGE: 'rgb(245,245,220)',
    'BIL SILVER': 'rgb(220,220,220)',
    BLAZING: 'rgb(255, 103, 0)',
    BRONZE: 'rgb(205, 127, 50)',
    'BRONZE M': 'rgb(205, 127, 50)',
    BROWN: 'BROWN',
    BURGANDY: 'rgb(128, 0, 32)',

    CARNELIAN: 'rgb(215, 96, 53)',
    'DEEP RED': 'rgb(255,99,71)',
    ORANGE: 'ORANGE',
    TANGERINE: 'rgb(242, 133, 0)',
    PURPLE: 'PURPLE',

    GREEN: 'rgb(153, 209, 143)',
    KIWI: 'rgb(142, 229, 63)',
    MAUV: 'rgb(224, 176, 255)',
    MAUVE: 'rgb(224, 176, 255)',
    'MAU GUN ML': 'rgb(224, 176, 255)',
    'OMNI BLUE': 'rgb(135, 206, 250)',
    SAGE: 'rgb(178, 172, 136)',
    TAN: 'rgb(210,180,140)',

    BLUE: 'rgb(135, 206, 250)',
    'BALI BLUE': 'rgb(159, 177, 187)',
    BLUEISH: 'rgb(135, 206, 250)',
    'BRIGH BLUE': 'rgb(135, 206, 250)',
    'CANYON BLU': 'rgb(95, 123, 142)',
    'DEEP BLUE': 'rgb(0, 0, 139)',
    'DYNO BLUE': 'rgb(0, 150, 214)',
    ROYAL: 'rgb(65, 105, 225)',

    CITRUS: 'rgb(159,183,10)',
    COPPER: 'rgb(184, 115, 51)',
    GOLD: 'rgb(255,215,0)',

    GRAY: 'rgb(211,211,211)',
    GREY: 'rgb(211,211,211)',
    'DARK GRAY': 'rgb(169,169,169)',
    'DARK GREY': 'rgb(169,169,169)',
    'GLO SILVER': 'rgb(220,220,220)',
    METAL: 'rgb(170, 169, 173)',
    SILVER: 'rgb(220,220,220)',
    'BUR SILVER': 'rgb(220,220,220)',

    TITANIUM: 'rgb(135, 134, 129)',

    //BLACK: 'rgb(60, 76, 59)',
    BLACK: 'rgb(0, 0, 0)',
    WHITE: 'rgb(255, 255, 255)'
    //GRAPH LUSG
}