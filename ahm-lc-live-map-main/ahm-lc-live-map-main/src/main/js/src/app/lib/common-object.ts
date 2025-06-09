import { SortDir } from "./constants";

export class ObjectUtilities {

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
            return ObjectUtilities.equalsByProperty(a, b, propertyName);
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
            let val1 = ObjectUtilities.getProperty(obj1, propertyName);
            let val2 = ObjectUtilities.getProperty(obj2, propertyName);
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
                return ObjectUtilities.compareByPropertyDesc(a, b, sortBy);
            }
        } else {
            comparator = function (a: any, b: any): number {
                return ObjectUtilities.compareByProperty(a, b, sortBy);
            }
        }
        return comparator;
    }

    public static compareByProperty(a: any, b: any, name: string): number {
        let val1 = ObjectUtilities.getProperty(a, name);
        let val2 = ObjectUtilities.getProperty(b, name);
        return ObjectUtilities.compare(val1, val2);
    }

    public static compareByPropertyDesc(a: any, b: any, name: string): number {
        return -ObjectUtilities.compareByProperty(a, b, name);
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
        return -ObjectUtilities.compareById(a, b);
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
        return - ObjectUtilities.compareByName(a, b);
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
        return -ObjectUtilities.compare(a, b);
    }
}