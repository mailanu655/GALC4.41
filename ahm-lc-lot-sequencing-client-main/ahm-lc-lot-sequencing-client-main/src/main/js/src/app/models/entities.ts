export interface ProcessPoint {
    id?: string;
    name?: string;
}

export interface Location {
    id?: string;
    name?: string;
    type?: string;
    description?: string;
    version?: number;
}

export interface MaskedDestination {
    mask?: string;
    location: Location;
}

export interface Route {
    id?: number;
    productType?: string;
    processPointId?: string;
    condition?: string;
    priority?: number;
    simpleDestinations?: Location[];
    maskedDestinations?: MaskedDestination[];
    version?: number;
}

export interface RouteDestination {
    routeId?: number;
    productType?: string;
    processPointId?: string;
    condition?: string;
    conditionResult?: boolean;
    priority?: number;
    mask?: string;
    location?: Location;
}