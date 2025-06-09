import { Injectable } from "@angular/core";
import { Location, Route } from "./entities";

@Injectable({
    providedIn: 'root'
})
export class SessionModel {

    sessionId: any; //?

    productType?: string;
    processPointId?: string;
    route?: Route;
    location?: Location;

    product: any;

    reset() {
        // delete this.route;
        // delete this.processPointId;
        // delete this.productType;
        this.product = null;
    }
}