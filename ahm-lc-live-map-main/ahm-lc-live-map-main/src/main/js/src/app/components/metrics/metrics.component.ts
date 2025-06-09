import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BaseComponent } from 'src/app/lib/base-component';
import { DataService } from 'src/app/services/data-service';
import { LineCode } from '../line/line-model';
import { Product } from 'src/app/services/model';

@Component({
    selector: 'metrics',
    templateUrl: './metrics.component.html',
    styleUrls: ['./metrics.component.css']
})
export class MetricsComponent extends BaseComponent {

    now: Date;
    shift: string;
    plan: number;
    actual: number;

    productCount: number;
    productWithDefectCount: number;
    productWithPartIssueCount: number;
    productOnHoldCount: number;
    stragglerCount: number;
    pendingStragglerCount: number;

    constructor(private dataService: DataService, snackBar: MatSnackBar) {
        super(snackBar);
    }

    ngOnInit(): void {
    }

    // === update data api === //
    update(lineProducts: Product[], products: Product[]) {
        this.now = new Date();
        this.selectFactoryNews();

        let productWithDefectCount = 0;
        let productWithPartIssueCount = 0;
        let productOnHoldCount = 0;

        let stragglerCount = 0;
        let pendingStragglerCount = 0;

        if (lineProducts) {
            for (let product of lineProducts) {
                if (product && product.defects && product.defects.length > 0) {
                    productWithDefectCount++;
                }
                if (product && product.partIssues && product.partIssues.length > 0) {
                    productWithPartIssueCount++;
                }
                if (product && product.holds && product.holds.length > 0) {
                    productOnHoldCount++;
                }
            }
        }
        for (let product of products) {
            if (product && product['straggler'] === true) {
                if (product['afStatus'] === LineCode.AF_ON) {
                    stragglerCount++;
                } else {
                    pendingStragglerCount++;
                }
            }
        }
        this.productCount = lineProducts.length;
        this.productWithDefectCount = productWithDefectCount;
        this.productWithPartIssueCount = productWithPartIssueCount;
        this.productOnHoldCount = productOnHoldCount;
        this.stragglerCount = stragglerCount;
        this.pendingStragglerCount = pendingStragglerCount;
    }

    // === data api === //
    selectFactoryNews() {
        if (this.dataService.config.siteServiceUrl === null) {
            return;
        }
        this.dataService.selectFactoryNews().subscribe({
            next: res => {
                this.processFactoryNews(res);
            },
            error: this.onError.bind(this),
            complete: this.afterExecute.bind(this)
        });
    }

    processFactoryNews(news: any[]) {
        let afOnNews;
        let afOffNews;
        let afOnLine = this.dataService.geLineByLineCode(LineCode.AF_ON);
        let afOffLine = this.dataService.geLineByLineCode(LineCode.AF_OFF);
        if (afOnLine && afOffLine) {
            for (let item of news) {
                let shiftStartTs: Date = new Date(item['shiftStart']);
                let shiftEndTs: Date = new Date(item['shiftEnd']);
                if (this.now >= shiftStartTs && this.now <= shiftEndTs) {
                    if (item['lineId'] === afOnLine.id) {
                        afOnNews = item;
                    } else if (item['lineId'] === afOffLine.id) {
                        afOffNews = item;
                    }
                }
            }
        }
        let onShift;
        let onPlan;
        let onActual;
        let offShift;
        let offPlan;
        let offActual;

        if (afOnNews) {
            onShift = afOnNews['shift'];
            onPlan = afOnNews['shiftPlan'];
            onActual = afOnNews['shiftActual'];
        }

        if (afOffNews) {
            offShift = afOffNews['shift'];
            offPlan = afOffNews['shiftPlan'];
            offActual = afOffNews['shiftActual'];
        }

        if (offShift) {
            this.shift = offShift;
        } else {
            this.shift = onShift;
        }
        if (offPlan && offPlan > 0) {
            this.plan = offPlan;
        } else {
            this.plan = onPlan;
        }
        if (offActual && offActual > 0) {
            this.actual = offActual;
        } else {
            this.actual = onActual;
        }

        if (!this.shift) {
            this.shift = '00';
        }
        if (!this.plan) {
            this.plan = 0;
        }
        if (!this.actual) {
            this.actual = 0;
        }
    }

    //TODO adjust by plan = 'Y' periods 
    calculateTarget(now: Date, endTs: Date, startTs: Date, plan: number) {
        if (now >= startTs && now >= endTs) {
            return plan;
        }
        if (now >= startTs && now < endTs) {
            let target = plan * (now.getTime() - startTs.getTime()) / (endTs.getTime() - startTs.getTime());
            return target;
        }
        return 0;
    }

    get stragglersAfLabel() {
        if (this.mobile || this.portrait) {
            return 'Strg. AF';
        } else {
            return 'Stragglers in AF';
        }
    }

    get stragglersWePaLabel() {
        if (this.mobile || this.portrait) {
            return 'Strg. PA-WE'
        } else {
            return 'Stragglers in PA-WE';
        }
    }
}
