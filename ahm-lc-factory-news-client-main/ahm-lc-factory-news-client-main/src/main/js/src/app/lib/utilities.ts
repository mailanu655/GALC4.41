import { SortDir } from "./constants";

export class Utilities {

    // === property === //
    public static getProperty(item: any, propertyName: string) {
        if (!item) {
            return null;
        }
        if (!propertyName) {
            return item;
        }

        if (propertyName.indexOf('.') < 0) {
            return item[propertyName];
        }
        let names = propertyName.split('.');
        let val = item;
        for (let name of names) {
            if (!val) {
                break;
            }
            val = val[name];
        }
        return val;
    }

    // === equality === //
    public static createEquals(propertyName: string) {
        if (!propertyName) {
            return null;
        }
        let equals = function (a: any, b: any): boolean {
            return Utilities.equalsByProperty(a, b, propertyName);
        }
        return equals;
    }

    public static equalsById(obj1: any, obj2: any) {
        if (obj1 && obj2) {
            return obj1.id === obj2.id;
        } else {
            return obj1 === obj2;
        }
    }

    public static equalsByProperty(obj1: any, obj2: any, propertyName: string) {
        if (obj1 && obj2) {
            let val1 = Utilities.getProperty(obj1, propertyName);
            let val2 = Utilities.getProperty(obj2, propertyName);
            return val1 === val2;
        } else {
            return obj1 === obj2;
        }
    }


    // === compare === //
    public static createComparator(sortBy: string, sortDir?: string): any {
        if (!sortBy) {
            return null;
        }
        let comparator = null;
        if (SortDir.DESC.name === sortDir) {
            comparator = function (a: any, b: any): number {
                return Utilities.compareByPropertyDesc(a, b, sortBy);
            }
        } else {
            comparator = function (a: any, b: any): number {
                return Utilities.compareByProperty(a, b, sortBy);
            }
        }
        return comparator;
    }

    public static compareByProperty(a: any, b: any, name: string): number {
        let val1 = Utilities.getProperty(a, name);
        let val2 = Utilities.getProperty(b, name);
        return Utilities.compare(val1, val2);
    }

    public static compareByPropertyDesc(a: any, b: any, name: string): number {
        return -Utilities.compareByProperty(a, b, name);
    }

    public static compareById(a: any, b: any): number {
        if (a.id < b.id) {
            return -1;
        }
        if (a.id > b.id) {
            return 1;
        }
        return 0;
    }

    public static compareByIdDesc(a: any, b: any): number {
        return -Utilities.compareById(a, b);
    }

    public static compareByName(a: any, b: any) {
        if (a && b && a.name < b.name) {
            return -1;
        }
        if (a && b && a.name > b.name) {
            return 1;
        }
        return 0;
    }

    public static compareByNameDesc(a: any, b: any) {
        return - Utilities.compareByName(a, b);
    }

    public static compare(a: any, b: any) {
        if (!a && b) {
            return -1;
        }
        if (a && !b) {
            return 1;
        }
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        return 0;
    }

    public static compareDesc(a: any, b: any) {
        return -Utilities.compare(a, b);
    }

    // === collecitons === //
    public static arrayToString(list: string[]) {
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

    public static intersect(ar: any[], filter: any[]) {
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

    public static intersectByProperty(ar: any[], filter: any[], propertyName: string) {
        let intersetcion = [];
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

    public static delta(ar: any[], filter: any[]) {
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

    public static deltaByProperty(ar: any[], filter: any[], propertyName: string) {
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

    public static filterByProperty(ar: any[], propertyName: string, values: any[]) {
        let filtered: any[] = [];
        if (!ar || !propertyName || !values || values.length === 0) {
            return filtered;
        }
        filtered = ar.filter(x => values.indexOf(Utilities.getProperty(x, propertyName)) > -1);
        return filtered;
    }

    public static filterOutByProperty(ar: any[], propertyName: string, values: any[]) {
        let filtered: any[] = [];
        if (!ar) {
            ar = [];
        }
        if (!propertyName || !values) {
            filtered = filtered.concat(ar);
            return filtered;
        }
        filtered = ar.filter(x => values.indexOf(Utilities.getProperty(x, propertyName)) < 0);
        return filtered;
    }
}