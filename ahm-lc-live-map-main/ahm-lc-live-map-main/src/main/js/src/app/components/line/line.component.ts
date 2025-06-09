import { Component, EventEmitter, Input, Output } from "@angular/core";
import { Clipboard } from '@angular/cdk/clipboard';
import { MatSnackBar } from "@angular/material/snack-bar";
import { BodyLocation, Line, Side, Station } from "./line-model";
import { ConfigService } from "src/app/services/config.service";
import { LineFactory } from "./line-factory";
import { AppService, UserEvent } from "src/app/services/app.service";
import { BaseComponent } from "src/app/lib/base-component";
import { LocationView, Model, Shape } from "src/app/lib/constants";
import { Product } from "src/app/services/model";

@Component({
    selector: 'line',
    templateUrl: './line.component.html',
    styleUrls: ['./line.component.css']
})
export class LineComponent extends BaseComponent {

    @Output()
    mouseOverLocation: EventEmitter<number> = new EventEmitter<number>();

    @Output()
    mouseOutLocation: EventEmitter<number> = new EventEmitter<number>();

    @Input()
    products: Product[] = [];

    @Input()
    selectedProducts: Product[] = [];

    // === line === //
    line: Line = new Line();

    constructor(public config: ConfigService, public appService: AppService, private clipboard: Clipboard, snackBar: MatSnackBar) {
        super(snackBar);
    }

    // === init === //
    ngOnInit() {
        this.defineLine();
        this.initModelShapes();
    }

    initModelShapes() {
        let siteConfig = this.config.site;
        let models: any[] = [];
        if (siteConfig && siteConfig['models']) {
            models = Object.values(siteConfig['models']);
        }
        if (models.length > 0) {
            for (let model of models) {
                let name = model.name;
                let viewId = model.viewId;
                if (name && viewId) {
                    let modelView: LocationView | undefined = LocationView.getIfExists(name);
                    let view: Shape = Shape.get(parseInt(viewId))!;
                    if (modelView && view) {
                        modelView.view = view;
                    }
                    if (!modelView) {
                        let label = model.label;
                        if (!label) {
                            label = name;
                        }
                        modelView = new LocationView(new Model(name, label), view);
                    }
                }
            }
        }
    }

    // === init map layout === //
    defineLine() {
        let factory = new LineFactory();
        this.line = factory.create(this.config.siteId);
        let lineComponent = this;
        BodyLocation.prototype.isSelected = function (): boolean {
            return lineComponent.isSelected(this);
        }
    }

    // === data === //
    update(products: Product[], selectedProducts: Product[]) {
        if (!products) {
            products = [];
        }
        this.products = products;
        this.updateSelected(selectedProducts);
        this.assignProductsToLocations(products);
    }

    updateSelected(selectedProducts: Product[]) {
        if (!selectedProducts) {
            selectedProducts = [];
        }
        this.selectedProducts = selectedProducts;
    }

    // === data processing === //
    assignProductsToLocations(products: Product[]) {
        this.line.clearLocations();
        // === set process point location === //
        for (let product of products) {
            if (product.locationId) {
                let ppAtLoc = product.locationProcessPointId;
                ppAtLoc = ppAtLoc + '@' + product.locationId;
                product['ppAtLoc'] = ppAtLoc;
            }
        }
        let blCount = this.line.locationCount;
        if (blCount < 1) {
            return;
        }
        let locations = this.line.locations;
        let locationIx = this.line.locationIx;
        if (this.appService.pushLocation === true) {
            let location: BodyLocation | undefined = locations[0];
            for (let ix = 0; ix < products.length && ix < blCount; ix++) {
                let product = products[ix];
                if (product.locationId && location && location.id < product.locationId) {
                    location = locationIx.get(product.locationId);
                    this.line.setProduct(product.locationId, product);
                    if (this.appService.pullSection === true) {
                        this.pullProducts(product.locationId);
                    }
                } else {
                    if (!location) {
                        return;
                    }
                    this.line.setProduct(location.id, product);
                }
                if (!location) {
                    return;
                }
                location = this.line.getNextLocation(location.id);
            }
        } else {
            for (let ix = 0; ix < products.length && ix < blCount; ix++) {
                let product = products[ix];
                let location = locations[ix];
                this.line.setProduct(location.id, product);
            }
        }
    }

    pullProducts(locationId: number) {
        let location = this.line.locationIx.get(locationId);
        if (!location || location.section.buffer === true) {
            return;
        }
        let locationIx = location.ix;
        let pushLocationIx = locationIx - 1;
        if (pushLocationIx < 0) {
            return;
        }
        let pushLocation = this.line.locations[pushLocationIx];
        if (!pushLocation) {
            return;
        }
        let pullLocation = this.getPullLocation(pushLocation);
        while (pushLocation && pullLocation) {
            if (!pushLocation || pushLocation.product) {
                return;
            }
            if (pushLocation.section.buffer === false) {
                this.line.setProduct(pushLocation.id, pullLocation.product);
                this.line.setProduct(pullLocation.id, null!);
            }
            pushLocationIx = pushLocationIx - 1;
            pushLocation = this.line.locations[pushLocationIx];
            pullLocation = this.getPullLocation(pushLocation);
        }
    }

    getPullLocation(location: BodyLocation): BodyLocation | null {
        if (!location) {
            return null!;
        }
        let pullIx = location.ix - 1;
        let pullLocation = this.line.locations[pullIx];
        let prevPullLocation: BodyLocation | null = pullLocation;
        while (pullLocation) {
            if (this.appService.pullLocation === true) {

            } else if (this.appService.pullCrossBuffer === true) {
                if (this.line.getBufferProductCount(pullLocation.ix) < 1 && prevPullLocation.section.buffer === true && pullLocation.section.buffer === false) {
                    return null;
                }
            } else if (this.appService.pullSection === true) {
                if (prevPullLocation.section.buffer === true && pullLocation.section.buffer === false) {
                    return null;
                }
            }
            if (pullLocation.product) {
                return pullLocation;
            }
            prevPullLocation = pullLocation;
            pullIx--;
            pullLocation = this.line.locations[pullIx];
        }
        return null;
    }

    // === ui control === //
    isSelected(location: BodyLocation): boolean {
        if (!location.product) {
            return false;
        }
        let ps = this.selectedProducts.find(p => p.id === location.product.id);
        if (ps) {
            return true;
        } else {
            return false;
        }
    }

    // === event handlers === //
    onClickLocation(id: number) {
        let bl = this.line.getLocation(id);
        let product = bl.product;
        if (!product) {
            return;
        }
        let ix = this.selectedProducts.findIndex((pr) => pr.id === product.id);
        this.deselectAll();
        if (ix < 0) {
            this.selectedProducts.push(product);
        }
        this.clipboard.copy(product.id);
        this.appService.userEvent.emit(UserEvent.LOCATION_CLICKED);
    }

    onRightClickLocation(id: number, event: Event) {
        event.preventDefault();
        let bl = this.line.getLocation(id);
        let product = bl.product;
        if (!product) {
            return;
        }
        let ix = this.selectedProducts.findIndex((pr) => pr.id === product.id);
        if (ix > -1) {
            this.deselect(product);
        }
        else {
            this.selectedProducts.push(product);
        }
        this.clipboard.copy(JSON.stringify(product));
        this.appService.userEvent.emit(UserEvent.LOCATION_CLICKED);
    }

    deselectAll() {
        if (!this.selectedProducts || this.selectedProducts.length < 1) {
            return;
        }
        this.selectedProducts.splice(0, this.selectedProducts.length);
    }

    deselect(product: Product) {
        let ix = this.selectedProducts.findIndex((pr) => pr.id === product.id);
        if (ix > -1) {
            this.selectedProducts.splice(ix, 1);
        }
    }

    // === to locate defects in defect table === //
    onMouseOverLocation(id: number) {
        this.mouseOverLocation.emit(id);
    }

    onMouseOutLocation(id: number) {
        this.mouseOutLocation.emit(id);
    }

    // === get/set === //
    getStationx(station: Station): number {
        let x: number = 0;
        let offset: number = station['offsetx']
        let location = this.line.locationIx.get(station.locationId);
        if (!location) {
            return 0;
        }
        if (location.horizontal) {
            x = location.cx - 0.5 * this.line.config.stationWidth;
        } else {
            let absoluteSide = location.getAbsoluteSide(station.side);
            if (absoluteSide === Side.L) {
                x = location.cx - 0.5 * this.line.config.sectionWidth - this.line.config.stationWidth;
            } else {
                x = location.cx + 0.5 * this.line.config.sectionWidth;
            }
        }
        if (offset) {
            x = x + offset;
        }
        return x;
    }

    getStationy(station: Station): number {
        let y: number = 0;
        let offset: number = station['offsety']
        let location = this.line.locationIx.get(station.locationId);
        if (!location) {
            return 0;
        }
        if (location.horizontal) {
            let absoluteSide = location.getAbsoluteSide(station.side);
            if (absoluteSide === Side.T) {
                y = location.section.cy - 0.5 * this.line.config.sectionWidth - this.line.config.stationHeight;
            } else {
                y = location.section.cy + 0.5 * this.line.config.sectionWidth;
            }
        } else {
            y = location.cy - 0.5 * this.line.config.stationHeight;
        }
        if (offset) {
            y = y + offset;
        }
        return y;
    }

    getLocationTooltip(bl: BodyLocation): string {
        let str = "BL: " + bl.code;
        if (this.appService.admin) {
            str = str + " { id: " + bl.id + ", ";
            str = str + "sequence: " + bl.sequence + " }";
        }
        if (bl.product) {
            str = str + "\n";
            str = str + "AFON Seq: " + (bl.product ? bl.product.afOnSequence : '') + "\n";
            str = str + "VIN: " + (bl.product ? bl.product.id : '') + "\n";
            str = str + "SPEC: " + (bl.product ? bl.product.specCode : '') + "\n";
            str = str + "KD LOT: " + (bl.product ? bl.product.kdLot : '') + "\n";
        }
        return str;
    }

    getAreaTooltip(area: any): string {
        let tooltip = "";
        if (area?.locations?.length > 0) {
            tooltip = tooltip + area.locations;
        }
        return tooltip;
    }

    getLabelx(locationId: number, side: Side, offset?: number) {
        let bl: BodyLocation = this.line.locationIx.get(locationId)!;
        if (!bl) {
            return null;
        }
        let sectionWidth = this.line.config.sectionWidth;
        let x = 0;
        if (bl.horizontal) {
            x = bl.cx;
        } else {
            let sideSignum = this.getSideSignum(bl, side);
            if (sideSignum > 0) {
                x = bl.cx + sectionWidth;
            } else {
                x = bl.cx - sectionWidth;
            }
        }
        if (offset) {
            x = x + offset;
        }
        return x;
    }

    getLabely(locationId: number, side: Side, offset?: number) {
        let bl: BodyLocation = this.line.locationIx.get(locationId)!;
        if (!bl) {
            return null;
        }
        let sectionWidth = this.line.config.sectionWidth;
        let y = 0;
        if (bl.horizontal) {
            let sideSignum = this.getSideSignum(bl, side);
            if (sideSignum > 0) {
                y = bl.cy + 9 + 0.5 * sectionWidth;
            } else {
                y = bl.cy - 7 - 0.5 * sectionWidth;
            }
        } else {
            y = bl.cy;
        }
        if (offset) {
            y = y + offset;
        }
        return y;
    }

    getInnerLabelx(locationId: number, side: Side, offset?: number) {
        let bl: BodyLocation = this.line.locationIx.get(locationId)!;
        if (!bl) {
            return null;
        }
        let sectionWidth = this.line.config.sectionWidth;
        let x = 0;
        if (bl.horizontal) {
            x = bl.cx;
        } else {
            let sideSignum = this.getSideSignum(bl, side);
            if (sideSignum > 0) {
                x = bl.cx + 0.5 * sectionWidth;
            } else {
                x = bl.cx - 0.5 * sectionWidth;
            }
        }
        if (offset) {
            x = x + offset;
        }
        return x;
    }

    getInnerLabely(locationId: number, side: Side, offset?: number) {
        let bl: BodyLocation = this.line.locationIx.get(locationId)!;
        if (!bl) {
            return null;
        }
        let blHeight = 20;
        let sectionWidth = this.line.config.sectionWidth;
        let y = 0;
        if (bl.horizontal) {
            let sideSignum = this.getSideSignum(bl, side);
            if (sideSignum > 0) {
                y = bl.cy + 0.5 * blHeight + 0.5 * (0.5 * sectionWidth - 0.5 * blHeight);
            } else {
                y = bl.cy - 0.5 * blHeight - 0.5 * (0.5 * sectionWidth - 0.5 * blHeight);
            }
        } else {
            y = bl.cy;
        }
        if (offset) {
            y = y + offset;
        }
        return y;
    }

    getSideSignum(bl: BodyLocation, side: Side): number {
        if (bl.horizontal) {
            if (side == Side.R) {
                return bl.signum;
            } else {
                return -bl.signum;
            }
        } else {
            if (side == Side.L) {
                return bl.signum;
            } else {
                return -bl.signum;
            }
        }
    }

    get sideR(): Side {
        return Side.R;
    }

    get sideL(): Side {
        return Side.L;
    }
}