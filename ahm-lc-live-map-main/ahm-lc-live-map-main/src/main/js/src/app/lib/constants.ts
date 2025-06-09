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

    public static match(model: string): LocationView {
        if (!model) {
            return LocationView.honda;
        }
        let bv = LocationView.instanceMap.get(model);
        if (bv) {
            return bv;
        }
        for (let key of Array.from(LocationView.instanceMap.keys())) {
            if (model.indexOf(key) > -1 || key.indexOf(model) > -1) {
                let view: LocationView = LocationView.get(key);
                if (view) {
                    LocationView.instanceMap.set(model, view);
                    return view;
                }
            }
        }
        return LocationView.honda;
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

    public static create(model: Model, shape: Shape) {
        let locationView = new LocationView(model, shape);
        return locationView;
    }

    public static init() {
        LocationView.create(new Model('EMPTY_LOCATION', 'Empty Location'), new Shape(-1, 15, 10, 0, 0, "2 1"));
        LocationView.create(new Model('HONDA', 'Honda'), new Shape(0, 20, 20, 0, 0));
        LocationView.create(new Model('CIRCLE', 'Circle'), new Shape(1, 20, 20, 10, 10));
        LocationView.create(new Model('ELLIPSE', 'Ellipse'), new Shape(2, 20, 15, 10, 10));
        LocationView.create(new Model('RECTANGLE3', 'Rectangle3'), new Shape(3, 15, 15, 3, 3));
        LocationView.create(new Model('RECTANGLE4', 'Rectangle4'), new Shape(4, 20, 15, 0.5, 0.5));
        LocationView.create(new Model('SQUARE5', 'Square5'), new Shape(5, 20, 20, 3, 3));
        LocationView.create(new Model('SQUARE6', 'Square6'), new Shape(6, 20, 20, 5, 15));
        LocationView.create(new Model('SQUARE7', 'Square7'), new Shape(7, 20, 20, 15, 5));
        LocationView.create(new Model('RECTANGLE8', 'Rectangle8'), new Shape(8, 20, 15, 5, 5));
        LocationView.create(new Model('SQUARE9', 'Square8'), new Shape(9, 20, 20, 5, 5));
        LocationView.create(new Model('RECTANGLE10', 'Rectangle10'), new Shape(10, 20, 10, 4, 4));
        LocationView.create(new Model('RECTANGLE11', 'Rectangle11'), new Shape(11, 20, 10, 0.5, 0.5));
        LocationView.create(new Model('SQUARE12', 'Square12'), new Shape(12, 20, 20, 0.5, 0.5));
    }
}
LocationView.init();