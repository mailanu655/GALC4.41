import { ObjectUtilities } from "./common-object";

export class CollectionUtilities {

    public static arrayToString(list: string[]): string {
        let str = '';
        if (list && list.length > 0) {
            for (let item of list) {
                if (str.length > 0) {
                    str = str + ", ";
                }
                str = str + item;
            }
            return str;
        } else {
            return '';
        }
    }

    public static intersect(ar: any[], filter: any[]): any[] {
        let intersetcion = [];
        if (filter && filter.length > 0 && ar && ar.length > 0) {
            for (let item of ar) {
                let common = null;
                common = filter.find(x => {
                    return x === item;
                });
                if (common) {
                    intersetcion.push(common);
                }
            }
        }
        return intersetcion;
    }

    public static intersectByProperty(ar: any[], filter: any[], propertyName: string): any[] {
        let intersetcion: any[] = [];
        if (filter && filter.length > 0 && ar && ar.length > 0) {
            for (let item of filter) {
                let common = null;
                common = ar.find(x => {
                    return x && item && x[propertyName] && x[propertyName] === item[propertyName];
                });
                if (common) {
                    intersetcion.push(common);
                }
            }
        }
        return intersetcion;
    }

    public static delta(ar: any[], filter: any[]): any[] {
        let delta: any[] = [];
        if (!ar) {
            ar = [];
        }
        if (!filter || filter.length === 0) {
            delta = delta.concat(ar);
            return delta;
        }
        if (ar.length > 0) {
            for (let item of ar) {
                let common = null;
                common = filter.find(x => {
                    return x === item;
                });
                if (!common) {
                    delta.push(item);
                }
            }
        }
        return delta;
    }

    public static deltaByProperty(ar: any[], filter: any[], propertyName: string): any[] {
        let delta: any[] = [];
        if (!ar) {
            ar = [];
        }
        if (!filter || filter.length === 0) {
            delta = delta.concat(ar);
            return delta;
        }
        if (ar && ar.length > 0) {
            for (let item of ar) {
                let common = null;
                common = filter.find(x => {
                    return x && item && x[propertyName] && x[propertyName] === item[propertyName];
                });
                if (!common) {
                    delta.push(item);
                }
            }
        }
        return delta;
    }

    public static filterByProperty(ar: any[], propertyName: string, values: any[]): any[] {
        let filtered: any[] = [];
        if (!ar || !propertyName || !values || values.length === 0) {
            return filtered;
        }
        filtered = ar.filter(x => values.indexOf(ObjectUtilities.getProperty(x, propertyName)) > -1);
        return filtered;
    }

    public static filterOutByProperty(ar: any[], propertyName: string, values: any[]): any[] {
        let filtered: any[] = [];
        if (!ar) {
            ar = [];
        }
        if (!propertyName || !values) {
            filtered = filtered.concat(ar);
            return filtered;
        }
        filtered = ar.filter(x => values.indexOf(ObjectUtilities.getProperty(x, propertyName)) < 0);
        return filtered;
    }
}