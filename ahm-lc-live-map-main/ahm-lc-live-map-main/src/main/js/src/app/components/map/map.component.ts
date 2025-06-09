import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { forkJoin, interval, of, timer } from "rxjs";
import { catchError } from 'rxjs/operators'
import { MatTab, MatTabGroup } from "@angular/material/tabs";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ConfigService } from "src/app/services/config.service";
import { LineComponent } from "../line/line.component";
import { ActivatedRoute, Router } from "@angular/router";
import { ProductsComponent } from "../products/products.component";
import { AppService, UserEvent } from "src/app/services/app.service";
import { DataService } from "src/app/services/data-service";
import { DefectsComponent } from "../defects/defects.component";
import { HoldType, InstalledPartStatus, SortDir } from "src/app/lib/constants";
import { PartIssuesComponent } from "../part-issues/part-issues.component";
import { BaseComponent } from "src/app/lib/base-component";
import { HoldsComponent } from "../holds/holds.component";
import { BodyLocation, DepartmentCode, LineCode } from "../line/line-model";
import { MetricsComponent } from "../metrics/metrics.component";
import { UpcomingLotsComponent } from "../upcoming-lots/upcoming-lots.component";
import { StragglersComponent } from "../stragglers/stragglers.component";
import { EXCEL_EXTENSION, Export } from "src/app/lib/common-export";
import { MessageBundle, messages } from "src/app/lib/messages";
import { ObjectUtilities } from "src/app/lib/common-object";
import { CollectionUtilities } from "src/app/lib/common-collection";
import { Defect, Hold, Lot, PartIssue, Product, PMQAIssue } from "src/app/services/model";
import { PartIssueService } from "src/app/services/part-issue.service";
import { PMQAIssueService } from "src/app/services/pmqa-issue.service";
import { PMQAIssuesComponent } from "../pmqa-issues/pmqa-issues.component";

export enum Parameter {
    SITE = "site", VIEW = "view"
}

export enum View {
    LINE = "line", DEFECTS = "defects", PARTS = "parts", HOLDS = "holds", STRAGGLERS = "stragglers", PMQA = "pmqa"
}

export enum Tabs {
    DEFECTS = "QICS Defects", PARTS = "Lot Control Issues", HOLDS = "Holds", STRAGGLERS = "Stragglers", UPCOMING_LOTS = "Upcoming Lots", PRODUCTS = "Products", HISTORY = "History", PMQA ="PMQA Issues"
}

@Component({
    selector: 'map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.css']
})
export class MapComponent extends BaseComponent implements OnInit, AfterViewInit, OnDestroy {

    @ViewChild('lineComponent') lineComponent: LineComponent;
    @ViewChild('metricsComponent') metricsComponent: MetricsComponent;
    @ViewChild('upcomingLotsComponent') upcomingLotsComponent: UpcomingLotsComponent;
    @ViewChild('productsComponent') productsComponent: ProductsComponent;
    @ViewChild('defectsComponent') defectsComponent: DefectsComponent;
    @ViewChild('partIssuesComponent') partIssuesComponent: PartIssuesComponent;
    @ViewChild('PMQAIssuesComponent') PMQAIssuesComponent: PMQAIssuesComponent;
    @ViewChild('holdsComponent') holdsComponent: HoldsComponent;
    @ViewChild('stragglersComponent') stragglersComponent: StragglersComponent;
    @ViewChild('tabs') tabs: MatTabGroup;

    messages: MessageBundle = messages;

    // === model === //
    lineProductIx: Map<string, Product> = new Map<string, Product>();
    defects: Defect[] = [];
    filteredDefects: Defect[] = [];
    partIssues: PartIssue[] = [];
    filteredPartIssues: PartIssue[] = [];
    holds: Hold[] = [];
    filteredHolds: Hold[] = [];
    selectedProducts: Array<Product> = [];
    hoveredLocation: BodyLocation | null;
    filteredPmqaIssue: PMQAIssue[] = [];

    lotIx: Map<string, Lot> = new Map<string, Lot>();
    products: Product[] = [];
    stragglers: Product[] = [];
    filteredStragglers: Product[] = [];
    upcomingLots: Lot[] = [];

    // === controll === //
    view: string;
    selectedTabIx: number = 0;

    // === timer === //
    countDownTimer = interval(1000);
    timerValue: number = 0;
    lastRefreshed: number;

    // === subscriptions === //
    countDownSubscription: any;
    userEventSubscription: any;
    dataSubscription: any;

    constructor(public appService: AppService, protected dataService: DataService, protected partIssueService: PartIssueService, public config: ConfigService, private route: ActivatedRoute, private router: Router, snackBar: MatSnackBar, protected pmqaIssueService: PMQAIssueService) {
        super(snackBar);
        this.router.routeReuseStrategy.shouldReuseRoute = function () {
            return false;
        };
        this.setParameters();
        if (!this.config.site) {
            this.router.navigateByUrl('');
            return;
        }
        this.dataService.loadCache();
        let headerItems: string[] = [];
        headerItems.push('countdown');
        if (!this.view || this.view == '' || this.view === View.LINE) {
            headerItems.push('search');
            headerItems.push('legend');
            headerItems.push('settings');
            headerItems.push('qics');
            headerItems.push('part');
            headerItems.push('hold');
            headerItems.push('straggler');
            headerItems.push('line');
            headerItems.push('pmqa');
        } else if (this.view === View.DEFECTS) {
            headerItems.push('settings');
            headerItems.push('qics');
        }
        this.appService.headerItems = headerItems;
    }

    // === init === //
    setParameters() {
        let site = this.route.snapshot.paramMap.get(Parameter.SITE);
        this.config.siteId = site!;
        this.view = this.route.snapshot.paramMap.get(Parameter.VIEW)!;
    }

    // === life cycle === //
    ngOnInit() {
        this.appService.resetHeader();
        this.countDownSubscription = this.countDownTimer.subscribe((n) => this.updateCountDown(n));
        this.userEventSubscription = this.appService.userEvent.subscribe((event: any) => {
            this.onUserEvent(event);
        });
        let delay = 1000 * this.config.refreshInterval;
        this.dataSubscription = timer(0, delay).subscribe(res => {
            this.selectProducts();
        });
    }

    ngAfterViewInit(): void {
        //this.selectedTabIx = this.tabs._tabs.length;
        this.selectedTabIx = 0;
    }

    ngOnDestroy() {
        this.appService.headerItems = [];
        this.countDownSubscription?.unsubscribe();
        this.dataSubscription?.unsubscribe();
        this.userEventSubscription?.unsubscribe();
    }

    // === ui === //
    get openSidePanel(): boolean {
        return this.appService.isVisible(this.appService.menuItem!);
    }

    // === update components with data === //
    updateProductData() {
        this.updateSelectedProducts();
        this.updateProductTable(this.selectedProducts);
        if (this.lineComponent) {
            this.lineComponent.update(this.lineProducts, this.selectedProducts);
        }
        if (this.metricsComponent) {
            this.metricsComponent.update(this.lineProducts, this.products);
        }
    }

    setSelectedProducts() {
        let searchString = this.appService.searchValue;
        if (!searchString || searchString.length < 1) {
            return;
        }
        let found: Product[] = this.appService.filter(this.lineProducts, searchString);
        this.selectedProducts.splice(0, this.selectedProducts.length);
        for (let pr of found) {
            this.selectedProducts.push(pr);
        }
    }

    //TODO split on line and table parts and move to component if appropriate
    updateSelectedProducts() {
        let selectedProducts = this.selectedProducts.slice();
        this.selectedProducts.splice(0, this.selectedProducts.length);
        for (let sp of selectedProducts) {
            let pr = this.lineProducts.find((p) => p.id === sp.id);
            if (pr) {
                this.selectedProducts.push(pr);
            }
        }
    }

    updateProductTable(products: Product[]) {
        if (this.productsComponent) {
            if (!products) {
                products = [];
            }
            let sorted = products.slice();
            sorted.sort(ObjectUtilities.createComparator("afOnTs", SortDir.DESC.name));
            this.productsComponent.tableData = sorted;
        }
    }

    updateCountDown(val: number) {
        this.timerValue = val;
        let countDown = this.config.refreshInterval - (this.timerValue - this.lastRefreshed);
        this.appService.countDown = countDown;
    }

    // === load data === //
    selectProducts() {
        this.lastRefreshed = this.timerValue;
        this.beforeExecute();
        if (this.config.siteServiceUrl === null) {
            return;
        }
        this.dataService.selectProducts().subscribe({
            next: res => {
                this.products = res;
                if (this.config.siteServiceUrl === null) {
                    return;
                }
                this.dataService.loadCacheIfEmpty();
                this.dataService.selectLots().subscribe({
                    next: res => {
                        this.setLots(res);
                        this.setProducts();
                        this.setLineProducts();
                        this.setStragglers();

                        this.selectUpcomingLots();
                        this.selectProductData();
                    },
                    error: this.onError.bind(this),
                    complete: this.afterExecute.bind(this)
                });
            },
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }

    selectProductData() {
        let partIssueParams;
        if (this.config.partIssueConfigurationIds.length === 1) {
            partIssueParams = { jobId: this.config.partIssueConfigurationIds[0] };
        }
        
        let data = forkJoin({
            defects: this.dataService.selectDefects(),
            holds: this.dataService.selectHolds(),
            pmqaIssues: this.pmqaIssueService.getIssues(partIssueParams).pipe(catchError((error: any) => {
                this.displayErrorMessage('Failed to select PMQA Issues !', 'OK');
                return of([]);
            })),
            partIssues: this.partIssueService.getIssues(partIssueParams).pipe(catchError((error: any) => {
                this.displayErrorMessage('Failed to select Lot Control Issues !', 'OK');
                return of([]);
            }))
        });
        data.subscribe({
            next:
                (result) => {
                    this.defects = CollectionUtilities.filterByProperty(result.defects, 'productId', Array.from(this.lineProductIx.keys()));
                    this.filteredDefects = [];
                    this.setDefects(this.defects);

                    this.partIssues = result.partIssues || [];
                    this.filteredPartIssues = [];
                    this.setPartIssues(this.partIssues);

                    //this.pmqaIssues = result.pmqaIssues
                    this.filteredPmqaIssue = result.pmqaIssues || [];
                    //this.setPm

                    this.holds = CollectionUtilities.filterByProperty(result.holds, 'id.productId', Array.from(this.lineProductIx.keys()));
                    this.filteredHolds = [];
                    this.setHolds(this.holds);

                    this.updateProductData();
                },
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }

    selectUpcomingLots() {
        this.dataService.selectUpcomingLots().subscribe({
            next: res => {
                this.setUpcomingLots(res);
            },
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }
    // === data processing === //
    setLots(lots: Lot[]) {
        this.lotIx.clear();
        for (let lot of lots) {
            this.lotIx.set(lot.id, lot);
        }
    }

    setProducts() {
        for (let product of this.products) {
            let lot = this.lotIx.get(product.prodLot);
            if (lot) {
                product['lotSize'] = lot.size;
                product['kdLotSize'] = lot['kdLotSize'];
                product['remake'] = lot['remake'];
            }
            let lineId = product['trackingStatus'];
            let line = this.dataService.lineCache.get(lineId);
            let trackingInfo = null;
            let departmentInfo = null;
            if (line) {
                let departmentId = line.departmentId;
                let department = this.dataService.departmentCache.get(departmentId);
                if (department) {
                    if (department.code) {
                        departmentInfo = department.code;
                        product['departmentCode'] = department.code;
                    } else {
                        departmentInfo = department.name;
                    }
                } else {
                    departmentInfo = departmentId;
                }
                trackingInfo = line.name;
                product['lineCode'] = line['code'];
            } else {
                trackingInfo = lineId;
            }
            product['lineInfo'] = trackingInfo;
            product['departmentInfo'] = departmentInfo;
        }
    }

    setLineProducts() {
        let lineProducts: Product[] = CollectionUtilities.filterByProperty(this.products, 'departmentCode', [DepartmentCode.AF])
        lineProducts = CollectionUtilities.filterByProperty(lineProducts, 'afStatus', [LineCode.AF_ON])
        lineProducts.sort(ObjectUtilities.createComparator('afOnTs', SortDir.DESC.name));
        this.lineProductIx.clear();
        if (!lineProducts) {
            return;
        }
        for (let product of lineProducts) {
            product.sequence = this.lineProductIx.size + 1;
            this.lineProductIx.set(product.id, product);
        }
        if (this.appService.watchOn === true) {
            this.onSearch();
        }
    }

    setStragglers() {
        this.stragglers = [];
        this.filteredStragglers = [];
        let stragglers: Product[] = [];
        for (let product of this.products) {
            if (product['straggler'] === true) {
                stragglers.push(product);
            }
        }
        stragglers.sort(ObjectUtilities.createComparator("afOnTs", SortDir.DESC.name));
        for (let p of stragglers) {
            if (!p.trackingStatus && p['remake'] === 'Y') {
                p['lineInfo'] = 'Planned';
            }
        }
        this.stragglers = stragglers;
        if (this.displayStragglers) {
            this.filteredStragglers = this.stragglers;
        }
    }

    setUpcomingLots(lots: Lot[]) {
        let upcomingLots: Lot[] = lots;
        upcomingLots.sort(ObjectUtilities.createComparator("lotSequence", SortDir.DESC.name))
        let product: Product;
        let lineProducts = this.lineProducts;
        if (lineProducts && lineProducts.length > 0) {
            product = lineProducts[0];
            if (product) {
                for (let lot of upcomingLots) {
                    if (lot.kdLot === product.kdLot) {
                        lot['current'] = true;
                        break;
                    }
                }
            }
        }
        this.upcomingLots = upcomingLots;
        if (this.upcomingLotsComponent) {
            this.upcomingLotsComponent.tableData = this.upcomingLots;
        }
    }

    setDefects(defects: Defect[]) {
        let products: Product[] = this.lineProducts;
        if (!products || products.length == 0) {
            return;
        }
        for (let product of products) {
            product.defects = [];
        }
        for (let defect of defects) {
            if (defect.entryDept) {
                let entryDep = this.dataService.departmentCache.get(defect.entryDept);
                if (entryDep) {
                    defect['entryDeptCode'] = entryDep.code;
                } else {
                    defect['entryDeptCode'] = defect.entryDept;
                }
            }
        }
        let filtered = this.filterDefects(defects);
        for (let defect of filtered) {
            let product = this.lineProductIx.get(defect.productId);
            if (product) {
                product.defects.push(defect);
                defect['afOnSequence'] = product.afOnSequence;
                defect['afOnTs'] = product.afOnTs;
                defect['model'] = product.model;
            }
        }
        filtered.sort(ObjectUtilities.createComparator("afOnTs", SortDir.DESC.name));
        this.filteredDefects = filtered;
        setTimeout(() => { this.setDefectTableForProduct(this.hoveredLocation?.product), 0 });
    }

    filterDefects(defects: Defect[]): Defect[] {
        if (!defects) {
            return [];
        }
        let filtered: Defect[] = [];
        if (this.appService.displayDefects) {
            filtered = defects.slice();
            return filtered;
        }
        let departments = this.appService.qicsDepartments;
        filtered = CollectionUtilities.filterByProperty(defects, "responsibleDept", departments);
        return filtered;
    }

    setPartIssues(issues: PartIssue[]) {
        let products: Product[] = this.lineProducts;
        if (!products || products.length == 0) {
            return;
        }
        for (let product of products) {
            product.partIssues = [];
        }
        let filtered = this.filterPartIssues(issues);
        for (let pi of filtered) {
            let product = this.lineProductIx.get(pi.productId);
            if (product) {
                product.partIssues.push(pi);
                pi['afOnSequence'] = product.afOnSequence;
                pi['afOnTs'] = product.afOnTs;
                pi['model'] = product.model;
                let status = InstalledPartStatus.getById(pi.statusId);
                pi['status'] = status ? status.name : pi.statusId;
                let pp = this.dataService.processPointCache.get(pi.processPointId);
                if (pp) {
                    pi['processPoint'] = pp;
                }
            }
        }
        filtered.sort(ObjectUtilities.createComparator("afOnTs", SortDir.DESC.name));
        this.filteredPartIssues = filtered;
        setTimeout(() => this.setPartsTableForProduct(this.hoveredLocation?.product), 0);
    }

    filterPartIssues(issues: PartIssue[]): PartIssue[] {
        if (!issues || !this.displayPartIssues) {
            return [];
        }
        let filtered: PartIssue[] = issues.slice(0);
        if (this.config.partIssueConfigurationIds && this.config.partIssueConfigurationIds.length > 0) {
            filtered = CollectionUtilities.filterByProperty(filtered, "jobId", this.config.partIssueConfigurationIds);
        }
        filtered = CollectionUtilities.filterOutByProperty(filtered, "statusId", [InstalledPartStatus.OK.id, InstalledPartStatus.REPAIRED.id]);
        filtered = CollectionUtilities.filterOutByProperty(filtered, "dismissed", [true]);
        let deduplicated: PartIssue[] = [];
        for (let pi of filtered) {
            if (deduplicated.find((item: PartIssue) => item.partName === pi.partName && item.productId === pi.productId)) {
                continue;
            }
            deduplicated.push(pi);
        }
        filtered = deduplicated;
        return filtered;
    }

    setHolds(holds: Hold[]) {
        let products: Product[] = this.lineProducts;
        if (!products || products.length == 0) {
            return;
        }
        for (let product of products) {
            product.holds = [];
        }

        let filtered = this.filterHolds(holds);
        for (let hold of filtered) {
            let product = this.lineProductIx.get(hold.id.productId);
            if (product) {
                product.holds.push(hold);
                hold['afOnSequence'] = product.afOnSequence;
                hold['afOnTs'] = product.afOnTs;
                hold['model'] = product.model;
                //TODO verify
                //hold['id'] = JSON.stringify({ productId: hold.productId, holdTypeId: hold.holdTypeId, processTs: hold.processTs });
                //hold.id = JSON.stringify({ productId: hold.productId, holdTypeId: hold.holdTypeId, processTs: hold.processTs });
                let holdType: HoldType = HoldType.getById(hold.id.holdTypeId);
                if (holdType) {
                    hold['type'] = holdType.toString();
                } else {
                    hold['type'] = hold.id.holdTypeId;
                }
            }
        }
        filtered.sort(ObjectUtilities.createComparator("afOnTs", SortDir.DESC.name));
        this.filteredHolds = filtered;
        setTimeout(() => this.setHoldsTableForProduct(this.hoveredLocation?.product), 0);
    }

    filterHolds(holds: Hold[]): Hold[] {
        if (!holds || !this.displayHolds) {
            return [];
        }
        let filtered: Hold[] = holds.slice();
        return filtered;
    }
    // === event handlers/callbacks === //
    onUserEvent(event: string) {
        switch (event) {
            case UserEvent.SEARCH:
                this.onSearch();
                break;
            case UserEvent.QICS:
                this.onQics();
                break;
            case UserEvent.PART:
                this.onParts();
                break;
            case UserEvent.HOLD:
                this.onHolds();
                break;
            case UserEvent.STRAGGLER:
                this.onStraggler();
                break;
            case UserEvent.LOCATION:
                this.onAdjustLocation();
                break;
            case UserEvent.LOCATION_CLICKED:
                this.onClickLocation();
                break;
        }
    }

    onSearch() {
        this.selectedProducts.splice(0, this.selectedProducts.length);
        if (this.appService.searchValue && this.appService.searchValue.length > 0) {
            this.setSelectedProducts();
        }
        this.updateProductTable(this.selectedProducts);
        setTimeout(() => this.selectTab(Tabs.PRODUCTS), 0);
    }

    onQics() {
        this.setDefects(this.defects.slice());
        if (this.filteredDefects.length > 0) {
            setTimeout(() => this.selectTab(Tabs.DEFECTS), 0);
        }
    }

    onParts() {
        this.setPartIssues(this.partIssues.slice());
        if (this.filteredPartIssues.length > 0) {
            setTimeout(() => this.selectTab(Tabs.PARTS), 0);
        }
    }

    onHolds() {
        this.setHolds(this.holds.slice());
        if (this.filteredHolds.length > 0) {
            setTimeout(() => this.selectTab(Tabs.HOLDS), 0);
        }
    }

    onStraggler() {
        if (this.appService.displayStragglers === true) {
            this.filteredStragglers = this.stragglers;
        } else {
            this.filteredStragglers = [];
        }
        if (this.filteredStragglers.length > 0) {
            setTimeout(() => this.selectTab(Tabs.STRAGGLERS), 0);
        }
    }

    onAdjustLocation() {
        this.setLineProducts();
        this.updateProductData();
    }

    onClickLocation() {
        this.appService.resetHeader();
        this.updateProductTable(this.selectedProducts);
        if (this.selectedProducts?.length > 0) {
            setTimeout(() => this.selectTab(Tabs.PRODUCTS), 0);
        }
    }

    onMouseOverRow(event: Event) {
        if (!this.lineComponent) {
            return;
        }
        let target = event.target || event.currentTarget;
        let cell = target as HTMLTableCellElement;
        let row = cell.closest("tr");
        let productId = row?.cells[1].innerText.trim();
        let location = this.lineComponent.line.getLocationByProductId(productId!);
        if (!location) {
            return;
        }
        let element = document.getElementById("" + location.id) as HTMLElement;
        if (element === null) {
            return;
        }
        this.highLightLocation(element);
    }

    onMouseOutRow(event: Event) {
        if (!this.lineComponent) {
            return;
        }
        const defectFocusElement = this.getFocusRect();
        defectFocusElement.style.opacity = "0";
    }

    protected highLightLocation(element: HTMLElement) {
        let blId = element.id;
        let location = this.lineComponent.line.getLocation(Number(blId));
        let width = this.lineComponent.line.config.c2c;
        let height = this.lineComponent.line.config.c2c;
        if (location.horizontal) {
            height = this.lineComponent.line.config.sectionWidth;
        } else {
            width = this.lineComponent.line.config.sectionWidth;
        }
        let x = location.cx - width / 2;
        let y = location.cy - height / 2;
        let defectFocusElement = this.getFocusRect();
        defectFocusElement.style.opacity = "1";
        defectFocusElement.setAttribute("x", x.toString());
        defectFocusElement.setAttribute("y", y.toString());
        defectFocusElement.style.width = width.toString();
        defectFocusElement.style.height = height.toString();
    }

    onMouseOverLocation(id: number) {
        let location = this.lineComponent.line.getLocation(id);
        this.hoveredLocation = location;
        setTimeout(() => {
            if (this.hoveredLocation) {
                this.setTableForProduct(this.hoveredLocation.product);
            }
        }, 700);
    }

    onMouseOutLocation(id: number) {
        if (!this.hoveredLocation) {
            this.hoveredLocation = null;
            return;
        }
        if (this.defectsComponent && this.selectedTabName === Tabs.DEFECTS && this.defectsComponent.tableData != this.filteredDefects) {
            this.defectsComponent.tableData = this.filteredDefects;
        } else if (this.partIssuesComponent && this.selectedTabName === Tabs.PARTS && this.partIssuesComponent.tableData != this.filteredPartIssues) {
            this.partIssuesComponent.tableData = this.filteredPartIssues;
        } else if (this.holdsComponent && this.selectedTabName === Tabs.HOLDS && this.holdsComponent.tableData != this.filteredHolds) {
            this.holdsComponent.tableData = this.filteredHolds;
        }
        this.hoveredLocation = null;
    }

    setTableForProduct(product: Product) {
        if (product) {
            if (this.defectsComponent && this.selectedTabName === Tabs.DEFECTS && product.defects?.length > 0) {
                this.defectsComponent.tableData = product.defects;
            } else if (this.partIssuesComponent && this.selectedTabName === Tabs.PARTS && product.partIssues?.length > 0) {
                this.partIssuesComponent.tableData = product.partIssues;
            } else if (this.holdsComponent && this.selectedTabName === Tabs.HOLDS && product.holds?.length > 0) {
                this.holdsComponent.tableData = product.holds;
            } else if (this.defectsComponent && product.defects?.length > 0) {
                this.defectsComponent.tableData = product.defects;
                this.selectTab(Tabs.DEFECTS);
            } else if (this.partIssuesComponent && product.partIssues?.length > 0) {
                this.partIssuesComponent.tableData = product.partIssues;
                this.selectTab(Tabs.PARTS);
            } else if (this.holdsComponent && product.holds?.length > 0) {
                this.holdsComponent.tableData = product.holds;
                this.selectTab(Tabs.HOLDS);
            }
        }
    }

    setDefectTableForProduct(product: Product | undefined) {
        if (product && this.selectedTabName === Tabs.DEFECTS && product.defects?.length > 0) {
            this.defectsComponent.tableData = product.defects;
        }
    }

    setPartsTableForProduct(product: Product | undefined) {
        if (product && this.selectedTabName === Tabs.PARTS && product.partIssues?.length > 0) {
            this.partIssuesComponent.tableData = product.partIssues;
        }
    }

    setHoldsTableForProduct(product: Product | undefined) {
        if (product && this.selectedTabName === Tabs.HOLDS && product.holds?.length > 0) {
            this.holdsComponent.tableData = product.holds;
        }
    }

    selectTab(name: string) {
        if (!this.tabs) {
            return;
        }
        let tabIx = null;
        let tab = this.tabs._tabs.toArray().find((tabItem: MatTab) => tabItem.textLabel === name);
        if (tab && !tab.disabled) {
            tabIx = this.tabs._tabs.toArray().indexOf(tab);
            if (tabIx < 0) {
                return;
            }
        } else {
            return;
        }
        this.selectedTabIx = tabIx;
        this.tabs.selectedIndex = tabIx;
    }

    print(): void {
        let tabName = this.selectedTabName;
        if (!tabName) {
            return;
        }
        let tableElement = this.getTableElement(tabName);
        if (!tableElement) {
            return;
        }
        let headerLine: string = this.config.site.name;
        headerLine = headerLine + "-" + tabName;
        Export.printHtml(headerLine, tableElement.outerHTML);
    }

    download(): void {
        let tabName = this.selectedTabName;
        if (!tabName) {
            return;
        }
        let fileName: string = this.config.site.name;
        fileName = fileName + "-" + tabName;
        fileName = fileName + "-" + Export.getTsToken();
        fileName = fileName.replace(" ", "_");
        fileName = fileName + EXCEL_EXTENSION;
        let tableElement = this.getTableElement(tabName);
        Export.exportTableToExcel(tableElement, fileName, tabName);
    }
    // === get/set === //
    isProductOk(product: Product): boolean {
        if (product.defects?.length > 0) {
            return false;
        } else if (product.partIssues?.length > 0) {
            return false;
        } else if (product.holds?.length > 0) {
            return false;
        }
        return true;
    }

    get selectedTabName(): string | null {
        if (!this.tabs) {
            return null;
        }
        let tabArray = this.tabs._tabs.toArray();
        if (tabArray.length < 1) {
            return null;
        }
        let tabName = tabArray[this.selectedTabIx].textLabel;
        return tabName;
    }

    getFocusRect(): HTMLElement {
        return document.getElementById("focusRect") as HTMLElement;
    }

    get displayPartIssues() {
        if (this.displayLineView) {
            return this.appService.displayPartIssues;
        } else {
            return true;
        }
    }

    get displayHolds() {
        if (this.displayLineView) {
            return this.appService.displayHolds;
        } else {
            return true;
        }
    }

    get displayStragglers() {
        if (this.displayLineView) {
            return this.appService.displayStragglers;
        } else {
            return true;
        }
    }

    get displayLineView(): boolean {
        return this.view === View.LINE || !this.view || this.view === '';
    }

    get displayDefectView(): boolean {
        return this.view === View.DEFECTS || (!this.view || this.view === '') && this.filteredDefects.length > 0;
    }

    get displayPartIssueView(): boolean {
        return this.view === View.PARTS || (!this.view || this.view === '') && this.filteredPartIssues.length > 0;
    }

    get displayPmqaIssueView(): boolean {
        return this.view === View.PMQA || (!this.view || this.view === '') && this.filteredPmqaIssue.length > 0;
    }

    get displayHoldView(): boolean {
        return this.view === View.HOLDS || (!this.view || this.view === '') && this.filteredHolds.length > 0;
    }

    get displayStragglerView(): boolean {
        return this.view === View.STRAGGLERS || (!this.view || this.view === '') && this.filteredStragglers.length > 0;
    }

    get lineProducts() {
        return Array.from(this.lineProductIx.values());
    }

    get processPointIx() {
        return this.dataService.processPointCache;
    }

    get qicsLabel(): string {
        return Tabs.DEFECTS;
    }

    get partsLabel(): string {
        return Tabs.PARTS;
    }

    get pmqaLabel(): string {
        return Tabs.PMQA;
    }

    get holdsLabel(): string {
        return Tabs.HOLDS;
    }

    get stragglersLabel(): string {
        return Tabs.STRAGGLERS;
    }

    get upcomingLotsLabel(): string {
        return Tabs.UPCOMING_LOTS;
    }

    get productsLabel(): string {
        return Tabs.PRODUCTS;
    }

    getTableElement(id: string): HTMLTableElement | null {
        let tabElement: HTMLElement | null = null;
        if (id) {
            tabElement = document.getElementById(id);
            if (tabElement) {
                let tableElements = tabElement.getElementsByTagName('table');
                if (tableElements && tableElements.length > 0) {
                    return tableElements.item(0);
                }
            }
        }
        return null;
    }
}
