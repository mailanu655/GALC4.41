import { ProductType } from "../constants";

export class TokenFormat {

    constructor(public name: string, public startIx: number, public length: number, public label: string) {
    }

    get endIx(): number {
        return this.startIx + this.length;
    }
}

export class SpecFormat {

    public static readonly WILD_CARD = '*';
    public static readonly SPACE = ' ';
    public static readonly SPACE_MARKER = '␣';
    private static readonly instances: Map<string, SpecFormat> = new Map<string, SpecFormat>();

    private tokenMap: Map<string, TokenFormat> = new Map<string, TokenFormat>();

    constructor(public productType: string, tokens: TokenFormat[]) {
        if (tokens) {
            tokens.forEach(token => {
                this.tokenMap.set(token.name, token);
            });
        }
    }

    static initInstances() {
        SpecFormat.instances.set(ProductType.FRAME, new SpecFormat(ProductType.FRAME, defineFrame()));
        SpecFormat.instances.set(ProductType.ENGINE, new SpecFormat(ProductType.FRAME, defineEngine()));
        SpecFormat.instances.set(ProductType.MBPN, new SpecFormat(ProductType.FRAME, defineMbpn()));
    }

    public static get(productType: string) {
        return SpecFormat.instances.get(productType);
    }

    parseToken(str: string, tokenName: string): string {
        if (!str) {
            return str;
        }
        let tokenFormat: TokenFormat | undefined = this.getTokenFormat(tokenName);
        if (!tokenFormat) {
            return '';
        }
        let start: number = tokenFormat.startIx;
        let end: number = tokenFormat.endIx;
        let token = str.substring(start, end);
        if (token.length < tokenFormat.length) {
            token = token.padEnd(tokenFormat.length, ' ');
        }
        return token;
    }

    maskToRegEx(mask: string) {
        if (!mask) {
            return mask;
        }
        let regExp = '^' + mask.replace(new RegExp('/*/', 'g'), '.');
        return regExp;
    }

    maskToSqlMask(mask: string) {
        mask = mask.trimEnd();
        let sqlMask = mask.replace(new RegExp('/*/', 'g'), '_');
        sqlMask = sqlMask + "%";
        return sqlMask;
    }

    sqlMaskToSpecMask(sqlMask: string) {
        sqlMask = sqlMask.replace('%', '');
        let mask = sqlMask.replace(new RegExp('/_/', 'g'), '*');
        return mask;
    }

    formatMask(mask: string) {
        let txt = mask;
        txt = txt.replace(new RegExp('/' + SpecFormat.SPACE + '/', 'g'), SpecFormat.SPACE_MARKER);
        txt = txt.padEnd(this.length, SpecFormat.SPACE_MARKER);
        return txt;
    }

    // === get/set === //
    get tokens() {
        return Array.from(this.tokenMap.values());
    }

    get length() {
        let length = 0;
        for (let tf of this.tokens) {
            length = length + tf.length;
        }
        return length;
    }

    getTokenFormat(tokenName: string) {
        let format: TokenFormat | undefined = this.tokenMap.get(tokenName);
        return format;
    }
}

function defineYmto() {
    let list: TokenFormat[] = [];
    list.push(new TokenFormat('YEAR', 0, 1, 'Year'));
    list.push(new TokenFormat('MODEL', 1, 3, 'Model'));
    list.push(new TokenFormat('TYPE', 4, 3, 'Type'));
    list.push(new TokenFormat('OPTION', 7, 3, 'Option'));
    return list;
}

function defineEngine() {
    return defineYmto();
}

function defineFrame() {
    let list: TokenFormat[] = defineYmto();
    list.push(new TokenFormat('EXT_COLOR', 10, 10, 'Ext Color'));
    list.push(new TokenFormat('INT_COLOR', 20, 2, 'Int Color'));
    return list;
}

function defineMbpn() {
    let list: TokenFormat[] = [];
    list.push(new TokenFormat('MAIN', 0, 5, 'Main'));
    list.push(new TokenFormat('CLASS', 5, 3, 'Class'));
    list.push(new TokenFormat('PROTOTYPE', 8, 1, 'Prototype'));
    list.push(new TokenFormat('TYPE', 9, 4, 'Type'));
    list.push(new TokenFormat('SUPPLEMENTARY', 13, 2, 'Supplementary'));
    list.push(new TokenFormat('TARGET', 15, 2, 'Target'));
    list.push(new TokenFormat('SPACE', 17, 1, 'Space'));
    list.push(new TokenFormat('HES_COLOR', 18, 11, 'Hes Color'));
    return list;
}

SpecFormat.initInstances();